package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemDao;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SolrUtil {
    private static final Logger logger = LoggerFactory.getLogger(SolrUtil.class);
    @Autowired
    private TbItemDao itemDao;

    @Autowired
    private SolrTemplate solrTemplate;

    private void importItemList() {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");//审核通过
        List<TbItem> itemList = itemDao.selectByExample(example);
        itemList.forEach(item -> {
            item.setSpecMap(JSON.parseObject(item.getSpec()));
        });
        logger.info("开始导入...");
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        logger.info("导入成功");
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = context.getBean(SolrUtil.class);

        solrUtil.importItemList();
    }
}
