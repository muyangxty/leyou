package com.leyou.goods.listener;


import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @RabbitListener(bindings = @QueueBinding(
            //绑定关系 value队列名称   durable是否持久化
            value = @Queue(value = "LEYOU.ITEM.SAVE.QUEUE", durable = "true"),
            //绑定的交换机 value交换机名称(必须生产者名称一致)  默认持久化   ignoreDeclarationExceptions是否忽略声明异常， type消息类型
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            //routing key
            key = {"item.insert", "item.update"}
    ))
    public void save(Long id){
        if (id == null){
            return;
        }
        this.goodsHtmlService.createHtml(id);
    }


}
