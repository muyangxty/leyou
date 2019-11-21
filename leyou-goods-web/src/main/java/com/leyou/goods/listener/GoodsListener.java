package com.leyou.goods.listener;


import com.leyou.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息队列监听器---商品详情页相关的监听器
 *
 * @author MuYang
 * @date 2019-11-18
 */
//将监听器放入spring容器
@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 保存或修改的监听方法
     *
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            //绑定关系 value队列名称   durable是否持久化
            value = @Queue(value = "LEYOU.ITEM.SAVE.QUEUE", durable = "true"),
            //绑定的交换机 value交换机名称(必须生产者名称一致)  默认持久化   ignoreDeclarationExceptions是否忽略声明异常， type消息类型
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            //routing key
            key = {"item.insert", "item.update"}
    ))
    public void save(Long id) {
        if (id == null) {
            return;
        }
        this.goodsHtmlService.createHtml(id);
    }

    /**
     * 删除的监听方法
     *
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            //绑定关系 value队列名称   durable是否持久化
            value = @Queue(value = "LEYOU.ITEM.DELETE.QUEUE", durable = "true"),
            //绑定的交换机 value交换机名称(必须生产者名称一致)  默认持久化   ignoreDeclarationExceptions是否忽略声明异常， type消息类型
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            //routing key
            key = {"item.delete"}
    ))
    public void delete(Long id) {
        if (id == null) {
            return;
        }
        this.goodsHtmlService.deleteHtml(id);
    }


}
