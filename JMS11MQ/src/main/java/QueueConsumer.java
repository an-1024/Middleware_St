import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Package: PACKAGE_NAME
 * @ClassName: QueueConsumer
 * @Author: AZ
 * @CreateTime: 2021/8/5 11:48
 * @Description: 消费者
 */
public class QueueConsumer {

    //默认用户名
    public static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    //默认密码
    public static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    //默认连接地址
    public static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        System.out.println("消费者启动");
        //创建连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);
        try{
            //创建连接
            Connection connection = connectionFactory.createConnection();
            //开启连接
            connection.start();
            //创建会话
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建队列，消息容器
            Queue myTestQueue = session.createQueue("activemq-quwuw-test1");
            //创建消息消费者
            MessageConsumer consumer = session.createConsumer(myTestQueue);
            //消费者实现监听接口消息
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("消费者消费的消息为-》" + textMessage.getText());
                    }catch (JMSException e){
                        System.out.println("消费异常-》"+e.getMessage());
                    }
                    try{
                        //提交事务
                        session.commit();
                    }catch (JMSException e){
                        System.out.println("消费异常-》"+e.getMessage());
                    }
                }
            });

            //让主线程休眠100秒，使消费者对象能继续存活一段时间，从而能监听到消息
            try {
                Thread.sleep(100 * 1000);
            }catch (InterruptedException e){
                System.out.println("中断异常");
            }
            //关闭资源
            session.close();
            connection.close();

        }catch (JMSException e){
            System.out.println("消费者服务异常"+e.getMessage());
        }

    }

}
