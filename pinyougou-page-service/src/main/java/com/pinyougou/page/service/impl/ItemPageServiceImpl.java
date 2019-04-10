package com.pinyougou.page.service.impl;

import com.google.common.collect.Maps;
import com.pinyougou.mapper.TbGoodsDao;
import com.pinyougou.mapper.TbGoodsDescDao;
import com.pinyougou.mapper.TbItemCatDao;
import com.pinyougou.mapper.TbItemDao;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemPageServiceImpl implements ItemPageService {

    @Autowired
    private FreeMarkerConfigurer configurer;

    @Autowired
    private TbGoodsDao goodsDao;

    @Autowired
    private TbGoodsDescDao goodsDescDao;

    @Autowired
    private TbItemCatDao itemCatDao;

    @Autowired
    private TbItemDao itemDao;

    @Value("${pagedir}")
    private String pagedir;

    @Override
    public boolean generateItemHtml(Long goodsId) {

        Configuration configuration = configurer.getConfiguration();
        Writer writer = null;
        try {
            Template template = configuration.getTemplate("item.ftl");
            Map<String, Object> map = Maps.newHashMap();
            TbGoods goods = goodsDao.selectByPrimaryKey(goodsId);
            TbGoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(goodsId);
            String itemCat1 = itemCatDao.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2 = itemCatDao.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3 = itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName();
            List<TbItem> itemList = findItemList(goodsId);
            map.put("goods", goods);
            map.put("goodsDesc", goodsDesc);
            map.put("itemCat1", itemCat1);
            map.put("itemCat2", itemCat2);
            map.put("itemCat3", itemCat3);
            map.put("itemList", itemList);
            String fileName = new StringBuilder()
                    .append(pagedir)
                    .append(goodsId)
                    .append(".html")
                    .toString();
            writer = new FileWriter(fileName);
            template.process(map, writer);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (Objects.nonNull(writer)) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private List<TbItem> findItemList(Long goodsId) {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        criteria.andGoodsIdEqualTo(goodsId);
        example.setOrderByClause("is_default desc");
        return itemDao.selectByExample(example);
    }

    @Override
    public boolean deleteItemHtml(Long goodsId) {
        String fileName = new StringBuilder()
                .append(pagedir)
                .append(goodsId)
                .append(".html")
                .toString();
        return new File(fileName).delete();
    }
}
