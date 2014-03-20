package at.panda.pandamessage;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.spec.ECFieldF2m;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

public class MessageReceiver extends Message implements Runnable {
	private Activity activity;
	private byte[] buffer;
	
	public MessageReceiver() {
		// TODO Auto-generated constructor stub
		this.activity = null;
		this.buffer = new byte[2048];
		try{
			setIp(InetAddress.getByName("10.0.0.10"));
			setPort(7777);
			setContent("");
			setSocket(new DatagramSocket(port, ip));
			setPacket(new DatagramPacket(buffer, buffer.length));
			
		} catch (UnknownHostException e){
			
		} catch (SocketException e) {
			// TODO: handle exception
		}
		
		
	}
	
	public MessageReceiver(String ip,Activity activity){
		this.activity = activity;
		this.buffer = new byte[2048];
		try{
			setIp(InetAddress.getByAddress(ip.getBytes()));
			setPort(7777);	
			setSocket(new DatagramSocket(this.port,this.ip));
			setPacket(new DatagramPacket(buffer, buffer.length));
		
		} catch (UnknownHostException e){
			
		} catch (SocketException e) {
			// TODO: handle exception
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true){
			receive();
			TextView messages = (TextView) activity.findViewById(R.id.textview_messages);
			if(messages.getText().equals("No Messages!")){
				messages.setText("Partner: "+content);
			} else 
			{
				messages.setText(messages.getText()+"\nPartner: "+content);
			}
			
		}

	}
	
	public void receive(){
		try{
			socket.receive(packet);
			content = new String(buffer,0,buffer.length);
		} catch(IOException e){
			
		}
		
	}
	
	public Activity getContext(){
		return this.activity;
	}
	
	public void setContext(Activity activity){
		this.activity=activity;
	}

}
