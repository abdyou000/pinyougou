package com.pinyougou.seller.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.pinyougou.common.enums.GoodsStatusEnum;
import com.pinyougou.common.pojo.GoodsEntity;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.*;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.search.service.ItemSearchService;
import com.pinyougou.seller.service.GoodsService;
import com.pinyougou.seller.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import javax.jms.Queue;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsDao goodsMapper;

    @Autowired
    private TbGoodsDescDao goodsDescDao;

    @Autowired
    private TbItemDao itemDao;

    @Autowired
    private TbItemCatDao itemCatDao;

    @Autowired
    private TbBrandDao brandDao;

    @Autowired
    private TbSellerDao sellerDao;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(GoodsEntity goodsEntity) {
        //add goods
        TbGoods goods = goodsEntity.getGoods();
        goods.setAuditStatus(GoodsStatusEnum.NOTAUDIT.getCode());
        goodsMapper.insert(goods);
        //add goodsDesc
        TbGoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
        goodsDesc.setGoodsId(goods.getId());
        goodsDescDao.insert(goodsDesc);
        //add itemList

        insertItemLIst(goodsEntity);
    }


    private void insertItemLIst(GoodsEntity goodsEntity) {
        TbGoods goods = goodsEntity.getGoods();
        TbGoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
        if (Objects.equals(goods.getIsEnableSpec(), "1")) {
            List<TbItem> itemList = goodsEntity.getItemList();
            itemList.forEach(item -> {
                String title = goods.getGoodsName();
                Map<String, Object> map = JSON.parseObject(item.getSpec());
                title += Joiner.on(' ').join(map.values());
                item.setTitle(title);
                List<Map> maps = JSON.parseArray(goodsDesc.getItemImages(), Map.class);
                if (!maps.isEmpty()) {
                    item.setImage(String.valueOf(maps.get(0).get("url")));
                }
                fillItem(goods, item);
            });
            itemDao.insertList(itemList);
        } else {
            TbItem item = new TbItem();
            item.setTitle(goods.getGoodsName());
            item.setPrice(goods.getPrice());
            fillItem(goods, item);
        }
    }

    private void fillItem(TbGoods goods, TbItem item) {

        item.setCategoryid(goods.getCategory3Id());
        TbItemCat itemCat = itemCatDao.selectByPrimaryKey(goods.getCategory3Id());
        item.setCategory(itemCat.getName());
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        item.setGoodsId(goods.getId());
        TbSellerExample example = new TbSellerExample();
        TbSellerExample.Criteria sellerCriteria = example.createCriteria();
        sellerCriteria.andSellerIdEqualTo(goods.getSellerId());
        List<TbSeller> sellerList = sellerDao.selectByExample(example);
        item.setSellerId(goods.getSellerId());
        item.setSeller(sellerList.get(0).getNickName());
        TbBrand tbBrand = brandDao.selectByPrimaryKey(goods.getBrandId());
        item.setBrand(tbBrand.getName());

    }

    /**
     * 修改
     */
    @Override
    public void update(GoodsEntity goodsEntity) {
        TbGoods goods = goodsEntity.getGoods();
        goodsMapper.updateByPrimaryKey(goods);
        TbGoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
        goodsDescDao.updateByPrimaryKey(goodsDesc);

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria itemCriteria = example.createCriteria();
        itemCriteria.andGoodsIdEqualTo(goods.getId());
        itemDao.deleteByExample(example);

        insertItemLIst(goodsEntity);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public GoodsEntity findOne(Long id) {
        GoodsEntity goodsEntity = new GoodsEntity();
        TbGoods goods = goodsMapper.selectByPrimaryKey(id);
        goodsEntity.setGoods(goods);
        TbGoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        goodsEntity.setGoodsDesc(goodsDesc);
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria itemCriteria = example.createCriteria();
        itemCriteria.andGoodsIdEqualTo(id);
        List<TbItem> itemList = itemDao.selectByExample(example);
        goodsEntity.setItemList(itemList);
        return goodsEntity;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        if (ids != null && ids.length > 0) {
            criteria.andIdIn(Arrays.asList(ids));
            goodsMapper.deleteByExample(example);
            //删除索引库
            jmsTemplate.send(queueSolrDeleteDestination, session -> session.createObjectMessage(ids));
            //删除模板文件
            jmsTemplate.send(topicPageDeleteDestination,session -> session.createObjectMessage(ids));
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }


    @Autowired
    private ItemService itemService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination queueSolrDestination;

    @Autowired
    private Destination queueSolrDeleteDestination;

    @Autowired
    private Destination topicPageDestination;

    @Autowired
    private Destination topicPageDeleteDestination;

    @Override
    public void updateStatus(Long[] ids, String status) {
        TbGoods goods = new TbGoods();
        goods.setAuditStatus(status);
        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        goodsMapper.updateByExampleSelective(goods, example);

        //更新索引库 删除静态页面
        if (Objects.equals("1", status)) {
            List<TbItem> itemList = itemService.findByGoodsIds(ids, status);
            String jsonString = JSON.toJSONString(itemList);
            jmsTemplate.send(queueSolrDestination, session -> session.createTextMessage(jsonString));
//            itemSearchService.importGoodsItemList(itemList);
//            Stream.of(ids).forEach(goodsId -> itemPageService.generateItemHtml(goodsId));
            jmsTemplate.send(topicPageDestination, session -> session.createObjectMessage(ids));
        }
    }

}
