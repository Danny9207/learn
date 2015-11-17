package com.danny.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RecvMQ {
	//队列名称  
    private final static String QUEUE_NAME = "hello3";  
  
    public static void main(String[] argv) throws java.io.IOException,  
            java.lang.InterruptedException, TimeoutException  
    {  
        //打开连接和创建频道，与发送端一样  
        ConnectionFactory factory = new ConnectionFactory();  
        factory.setHost("localhost");  
        Connection connection = factory.newConnection();  
        Channel channel = connection.createChannel();  
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。  
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
        //设置最大服务转发消息数量 。保证一个消费者处理一个消息，可以动态添加消费者。
        int prefetchCount = 1;  
        channel.basicQos(prefetchCount); 
        //创建队列消费者  
        QueueingConsumer consumer = new QueueingConsumer(channel);  
        //打开应答机制。保证某个消费者断开也能全部处理完发出的消息。
        boolean ack = false ; 
        //指定消费队列  
        channel.basicConsume(QUEUE_NAME, ack, consumer);
        int i = 0;
        while (true)  
        {  
            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）  
            QueueingConsumer.Delivery delivery = consumer.nextDelivery(); 
            i++;
            System.out.println(" [x] Received '" + i + "'");  
            byte2File(delivery.getBody(), "E:\\testMQ", i+".jpg");
//            String message = new String(delivery.getBody());
        }  
  
    }

	public static void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
