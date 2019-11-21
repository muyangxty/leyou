package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 消息监听类---搜索相关的监听类
 * @author MuYang
 * @Date 2019-11-18
 */
@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;

    /**
     * 处理insert和update的消息
     * @param id
     * @throws IOException
     */
    //消息监听
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SEARCH.SAVE.QUEUE", durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))
    public void save(Long id) throws IOException {
        if (id == null){
            return;
        }
        this.searchService.save(id);
    }

    /**
     * 删除的监听方法
     * @param id
     * @throws IOException
     */
    //消息监听
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SEARCH.DELETE.QUEUE", durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void delete(Long id) throws IOException {
        if (id == null){
            return;
        }
        this.searchService.delete(id);
    }

}
