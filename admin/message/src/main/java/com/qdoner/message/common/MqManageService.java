package com.qdoner.message.common;

import com.kakarote.core.common.Result;
import com.qdoner.message.entity.PO.Exchanges;
import com.qdoner.message.entity.PO.Queues;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * mq服务管理
 */
@Component
public class MqManageService {

    @Value("${spring.rabbitmq.host}")
    private String mqHost;
    @Value("${spring.rabbitmq.port}")
    private int mqPort;
    @Value("${spring.rabbitmq.username}")
    private String mqUsername;
    @Value("${spring.rabbitmq.password}")
    private String mqPassWord;

    @Autowired
    private AmqpTemplate rabbitAmqpTemplate;

    /**
     * 获取MQ的连接
     *
     * @return MQ连接资源
     */
    private Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(mqHost);
        factory.setPort(mqPort);
        //vhost
        //factory.setVirtualHost("diy");
        factory.setUsername(mqUsername);
        factory.setPassword(mqPassWord);

        return factory.newConnection();
    }

    /**
     * 推送消息
     *
     * @param params 消息内容
     * @return
     */
    public Result send(String exchangeName, String channelName, Map<String, Object> params) {
        /**
         * convertAndSend - 转换并发送消息的template方法。
         * 是将传入的普通java对象，转换为rabbitmq中需要的message类型对象，并发送消息到rabbitmq中。
         * 参数1：交换器。 类型是String
         * 参数2：路由键。 类型是String -- 未使用
         * 参数3：消息，是要发送的消息内容对象。类型是Object
         */
        rabbitAmqpTemplate.convertAndSend(exchangeName, channelName, params);
        return Result.ok();
    }

    /**
     * 交换机删除
     *
     * @param name
     * @return
     */
    public void deleteExchange(String name) {
        try {
            //获取一个链接
            Connection connection = getConnection();
            //从连接中获取一个通道
            Channel channel = connection.createChannel();
            channel.exchangeDelete(name);
            //关闭通道
            channel.close();
            //关闭连接
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 交换机部署
     *
     * @param exchanges
     * @return
     */
    public void deployExchange(Exchanges exchanges) {
        try {
            //获取一个链接
            Connection connection = getConnection();
            //从连接中获取一个通道
            Channel channel = connection.createChannel();
            channel.exchangeDelete(exchanges.getName());
            //处理扩展参数
            Map<String, Object> arguments = new HashMap<>();
            channel.exchangeDeclare(exchanges.getName(),
                    exchanges.getType(),
                    exchanges.getDurability().equals("1") ? true : false,
                    exchanges.getAutoDelete().equals("1") ? true : false,
                    exchanges.getInternal().equals("1") ? true : false,
                    arguments);
            //关闭通道
            channel.close();
            //关闭连接
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 序列删除
     *
     * @param name
     * @return
     */
    public void deleteQueue(String name) {
        try {
            //获取一个链接
            Connection connection = getConnection();
            //从连接中获取一个通道
            Channel channel = connection.createChannel();
            channel.queueDelete(name);
            //关闭通道
            channel.close();
            //关闭连接
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 序列部署并绑定
     *
     * @param queues
     * @return
     */
    public void deployQueue(Queues queues) {
        try {
            //获取一个链接
            Connection connection = getConnection();
            //从连接中获取一个通道
            Channel channel = connection.createChannel();
            channel.queueDelete(queues.getCode());
            //处理扩展参数
            Map<String, Object> arguments = new HashMap<>();
            /**
             * 参数明细
             * 1、queue 队列名称
             * 2、durable 是否持久化，如果持久化，mq重启后队列还在
             * 3、exclusive 是否独占连接，队列只允许在该连接中访问，如果connection连接关闭队列则自动删除,如果将此参数设置true可用于临时队列的创建
             * 4、autoDelete 自动删除，队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
             * 5、arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
             */
            channel.queueDeclare(queues.getCode(),
                    queues.getDurability().equals("1") ? true : false,
                    false,
                    queues.getAutoDelete().equals("1") ? true : false,
                    arguments);
            /**
             * 参数明细：
             * 1、queue 队列名称
             * 2、exchange 交换机名称
             * 3、routingKey 路由key，作用是交换机根据路由key的值将消息转发到指定的队列中，在发布订阅模式中调协为空字符串
             */
            channel.queueBind(queues.getCode(), queues.getExchangeName(), queues.getChannelName());
            //关闭通道
            channel.close();
            //关闭连接
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }
}
