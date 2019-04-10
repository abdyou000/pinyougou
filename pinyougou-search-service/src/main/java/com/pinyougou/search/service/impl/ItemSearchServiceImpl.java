package com.pinyougou.search.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.search.service.ItemSearchService;
import com.pinyougou.seller.service.ItemCatService;
import com.pinyougou.seller.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Value("${SOLR_PREFIX}")
    private String SOLR_PREFIX;

    @Value("${SOLR_POSTFIX}")
    private String SOLR_POSTFIX;

    @Value("${SOLR_DEFAULT_PAGESIZE}")
    private Integer SOLR_DEFAULT_PAGESIZE;

    @Value("${SOLR_DEFAULT_PAGENUM}")
    private Integer SOLR_DEFAULT_PAGENUM;

    @Override
    public Map<String, Object> search(Map<String, Object> param) {
        //去除空格
        param.put("keywords",((String)param.get("keywords")).replace(" ",""));
        Map<String, Object> map = Maps.newHashMap();
        map.putAll(searchItemList(param));
        List<String> categoryList = findCategoryList(param);
        map.put("categoryList", categoryList);

        String categoryName = (String) param.get("category");
        if (Strings.isNullOrEmpty(categoryName)) {
            if (categoryList.size() > 0) {
                categoryName = categoryList.get(0);
            }
        }
        map.putAll(searchBrandAndSpecList(categoryName));
        return map;
    }

    @Override
    public void importGoodsItemList(List<TbItem> itemList) {
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    @Override
    public void deleteByGoodsIds(Long[] goodsIds) {
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_goodsid").in(Arrays.asList(goodsIds));
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private ItemCatService itemCatService;

    @Reference
    private TypeTemplateService typeTemplateService;


    private Map<String, Object> searchBrandAndSpecList(String categoryName) {
        Map<String, Object> map = Maps.newHashMap();
        List<Map> brandList = null;
        List<Map<String, Object>> specList = null;
        Long typeTemplateId = (Long) redisTemplate.boundHashOps("itemCat").get(categoryName);
        if (Objects.nonNull(typeTemplateId)) {
            brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeTemplateId);
            specList = (List<Map<String, Object>>) redisTemplate.boundHashOps("specList").get(typeTemplateId);
        }
        //缓存没有，查询数据库
        if (CollUtil.isEmpty(brandList) || CollUtil.isEmpty(specList)) {
            typeTemplateId = itemCatService.findByName(categoryName).getTypeId();
            TbTypeTemplate typeTemplate = typeTemplateService.findOne(typeTemplateId);
            brandList = JSON.parseArray(typeTemplate.getBrandIds(), Map.class);
            specList = typeTemplateService.findSpecList(typeTemplateId);
        }
        map.put("brandList", brandList);
        map.put("specList", specList);
        return map;
    }

    private Map<String, Object> searchItemList(Map<String, Object> param) {
        Map<String, Object> map = Maps.newHashMap();
        //高亮
        HighlightQuery query = new SimpleHighlightQuery();
        Criteria keywordsCriteria = new Criteria("item_keywords").is(param.get("keywords"));
        query.addCriteria(keywordsCriteria);
        HighlightOptions options = new HighlightOptions().addField("item_title");
        options.setSimplePrefix(SOLR_PREFIX);
        options.setSimplePostfix(SOLR_POSTFIX);
        query.setHighlightOptions(options);
        //根据商品类目过滤
        String categoryName = (String) param.get("category");
        if (!Strings.isNullOrEmpty(categoryName)) {
            query.addFilterQuery(createFilterQuery("item_category",criteria -> criteria.is(categoryName)));
        }
        //根据品牌过滤
        String brandName = (String) param.get("brand");
        if (!Strings.isNullOrEmpty(brandName)) {
            query.addFilterQuery(createFilterQuery("item_brand", criteria -> criteria.is(brandName)));
        }
        //根据规格过滤
        Map<String, String> specMap = (Map<String, String>) param.get("spec");
        if (Objects.nonNull(specMap)) {
            specMap.forEach((key, value) -> {
                String specPrefix = "item_spec_";
                query.addFilterQuery(createFilterQuery(specPrefix.concat(key), criteria -> criteria.is(value)));
            });
        }
        //根据价格过滤
        String priceRegion = (String) param.get("price");
        if (!Strings.isNullOrEmpty(priceRegion)) {
            String[] prices = priceRegion.split("-");
            query.addFilterQuery(createFilterQuery("item_price",criteria -> criteria.greaterThanEqual(prices[0])));
            if (!Objects.equals("*", prices[1])) {
                query.addFilterQuery(createFilterQuery("item_price",criteria -> criteria.lessThanEqual(prices[1])));
            }
        }
        //分页
        Integer pageNum = (Integer) param.get("pageNum");
        if (Objects.isNull(pageNum)) {
            pageNum = SOLR_DEFAULT_PAGENUM;
        }
        Integer pageSize = (Integer) param.get("pageSize");
        if(Objects.isNull(pageSize)) {
            pageSize = SOLR_DEFAULT_PAGESIZE;
        }
        Integer offset = (pageNum - 1) * pageSize;
        query.setOffset(offset);//开始记录数
        query.setRows(pageSize);//每页记录数

        //排序
        String sortDirection = (String) param.get("sort");
        String sortField = (String) param.get("sortField");
        if (!Strings.isNullOrEmpty(sortDirection) && !Strings.isNullOrEmpty(sortField)) {
            String sortPrefix = "item_";
            Sort.Direction direction;
            if (Objects.equals("ASC",sortDirection)) {
                direction = Sort.Direction.ASC;
            } else {
                direction = Sort.Direction.DESC;
            }
            query.addSort(new Sort(direction,sortPrefix.concat(sortField)));
        }

        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        page.getHighlighted().forEach(entry -> {
            List<HighlightEntry.Highlight> highlights = entry.getHighlights();
            if (!highlights.isEmpty()) {
                List<String> snipplets = highlights.get(0).getSnipplets();
                if (!snipplets.isEmpty()) {
                    entry.getEntity().setTitle(snipplets.get(0));
                }
            }
        });
        map.put("rows", page.getContent());
        map.put("totalPages", page.getTotalPages());
        map.put("total", page.getTotalElements());
        return map;
    }

    private FilterQuery createFilterQuery(String fieldName, Consumer<Criteria> consumer) {
        FilterQuery filterQuery = new SimpleFilterQuery();
        Criteria criteria = new Criteria(fieldName);
        consumer.accept(criteria);
        return filterQuery.addCriteria(criteria);
    }

    private List<String> findCategoryList(Map<String, Object> param) {
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(param.get("keywords"));
        query.addCriteria(criteria);
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        return page.getGroupResult("item_category").getGroupEntries()
                .getContent()
                .stream()
                .map(GroupEntry::getGroupValue)
                .collect(Collectors.toList());

    }
}
