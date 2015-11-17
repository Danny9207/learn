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
	//��������  
    private final static String QUEUE_NAME = "hello3";  
  
    public static void main(String[] argv) throws java.io.IOException,  
            java.lang.InterruptedException, TimeoutException  
    {  
        //�����Ӻʹ���Ƶ�����뷢�Ͷ�һ��  
        ConnectionFactory factory = new ConnectionFactory();  
        factory.setHost("localhost");  
        Connection connection = factory.newConnection();  
        Channel channel = connection.createChannel();  
        //�������У���ҪΪ�˷�ֹ��Ϣ�����������д˳��򣬶��л�������ʱ�������С�  
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
        //����������ת����Ϣ���� ����֤һ�������ߴ���һ����Ϣ�����Զ�̬��������ߡ�
        int prefetchCount = 1;  
        channel.basicQos(prefetchCount); 
        //��������������  
        QueueingConsumer consumer = new QueueingConsumer(channel);  
        //��Ӧ����ơ���֤ĳ�������߶Ͽ�Ҳ��ȫ�������귢������Ϣ��
        boolean ack = false ; 
        //ָ�����Ѷ���  
        channel.basicConsume(QUEUE_NAME, ack, consumer);
        int i = 0;
        while (true)  
        {  
            //nextDelivery��һ�������������ڲ�ʵ����ʵ���������е�take������  
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
