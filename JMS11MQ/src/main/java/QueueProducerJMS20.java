import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Package: PACKAGE_NAME
 * @ClassName: QueueProducer
 * @Author: AZ
 * @CreateTime: 2021/8/5 10:18
 * @Description: 消息生产者 JMS 2.0
 */
public class QueueProducerJMS20 {
    //默认用户名
    public static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    //默认密码
    public static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    //默认连接地址
    public static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    //创建工厂
    public static void main(String[] args) {
        System.out.println("生产者启动JMS2.0");
        //创建连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD, BROKER_URL);

        try{
            JMSContext context = connectionFactory.createContext();
            Queue queue = context.createQueue("activemq-quwuw-test1");
            //开始发送
            String text = "发送第一条JMS2.0消息";
            context.createProducer().send(queue, text);
            System.out.println("发送的消息为-》"+ text);
        }catch (JMSRuntimeException e){

        }
    }
}
