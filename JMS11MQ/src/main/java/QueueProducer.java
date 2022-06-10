import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Package: PACKAGE_NAME
 * @ClassName: QueueProducer
 * @Author: AZ
 * @CreateTime: 2021/8/5 10:18
 * @Description: 消息生产者
 */
public class QueueProducer {
    //默认用户名
    public static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    //默认密码
    public static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    //默认连接地址
    public static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    //创建工厂
    public static void main(String[] args) {
        System.out.println("生产者启动");
        //创建连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);

        try{
            //创建连接
            Connection connection = connectionFactory.createConnection();

            //启动连接
            connection.start();

            //创建会话: 第一个参数：是否开启事物，第二个参数：消息确认模式
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

            //创建队列，需要指定队列名称，消息生产者和消息消费者将根据它来发送、接收对应的消息-》消息容器
            Queue myTestQueue = session.createQueue("activemq-quwuw-test1");

            //创建消息生产者
            MessageProducer producer = session.createProducer(myTestQueue);

            //创建一个消息对象
            TextMessage message = session.createTextMessage("测试点对点的一条消息");
            System.out.println("生产者生产消息-》"+message.getText());

            //发送一条消息

            producer.send(message);

            //提交事务
            session.commit();

            //关闭资源
            session.close();
            connection.close();

        }catch (JMSException e){
            System.out.println("消息生产异常-》"+e.getMessage());
        }
    }
}
