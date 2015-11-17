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
	//��������  
	private final static String QUEUE_NAME = "hello3";
	public static void main(String[] args) throws IOException, TimeoutException {
		/** 
        * �����������ӵ�MabbitMQ 
        */  
       ConnectionFactory factory = new ConnectionFactory();  
       //����MabbitMQ��������ip����������  
       factory.setHost("localhost");  
       //����һ������  
       Connection connection = factory.newConnection();  
       //����һ��Ƶ��  
       Channel channel = connection.createChannel();  
       //ָ��һ������  
       channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
       //���͵���Ϣ  
//       String message = "hello world3333!";  
       File file=new File("E:\\historymessage\\2015\\09\\28");
       if(file.isDirectory()){
    	  File[] files=file.listFiles();
    	  int i=0;
    	  for(File fileTemp:files){
    		  i++;
    		  byte[] bytes=File2byte(fileTemp);
    		  //�������з���һ����Ϣ  
    		  channel.basicPublish("", QUEUE_NAME, null, bytes);  
    		  System.out.println(" [x] Sent '" + i + "'");  
    	  }
       }
       //�ر�Ƶ��������  
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
