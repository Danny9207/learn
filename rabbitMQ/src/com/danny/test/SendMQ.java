package com.danny.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendMQ {
	//队列名称  
	private final static String QUEUE_NAME = "hello3";
	public static void main(String[] args) throws IOException, TimeoutException {
		/** 
        * 创建连接连接到MabbitMQ 
        */  
       ConnectionFactory factory = new ConnectionFactory();  
       //设置MabbitMQ所在主机ip或者主机名  
       factory.setHost("localhost");  
       //创建一个连接  
       Connection connection = factory.newConnection();  
       //创建一个频道  
       Channel channel = connection.createChannel();  
       //指定一个队列  
       channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
       //发送的消息  
//       String message = "hello world3333!";  
       File file=new File("E:\\historymessage\\2015\\09\\28");
       if(file.isDirectory()){
    	  File[] files=file.listFiles();
    	  int i=0;
    	  for(File fileTemp:files){
    		  i++;
    		  byte[] bytes=File2byte(fileTemp);
    		  //往队列中发出一条消息  
    		  channel.basicPublish("", QUEUE_NAME, null, bytes);  
    		  System.out.println(" [x] Sent '" + i + "'");  
    	  }
       }
       //关闭频道和连接  
       channel.close();  
       connection.close(); 
	}
	public static byte[] File2byte(File file)  
    {  
        byte[] buffer = null;  
        try  
        {  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            byte[] b = new byte[1024];  
            int n;  
            while ((n = fis.read(b)) != -1)  
            {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        }  
        catch (FileNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        return buffer;  
    }
}
