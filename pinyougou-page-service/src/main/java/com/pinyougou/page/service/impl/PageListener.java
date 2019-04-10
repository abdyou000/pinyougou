package com.pinyougou.page.service.impl;

import javax.jms.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.page.service.ItemPageService;

import java.util.stream.Stream;

/**
 * 监听类（用于生成网页）
 *
 * @author Administrator
 */
@Component
public class PageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            Stream.of(goodsIds).forEach(goodsId -> itemPageService.generateItemHtml(goodsId));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
