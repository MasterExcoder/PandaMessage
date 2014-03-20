package at.panda.pandamessage;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.spec.ECFieldF2m;

import android.content.Context;

public class MessageReceiver extends Message implements Runnable {
	private Context context;
	private byte[] buffer;
	
	public MessageReceiver() {
		// TODO Auto-generated constructor stub
		this.context = null;
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
	
	public MessageReceiver(String ip,Context context){
		this.context = context;
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
		}

	}
	
	public void receive(){
		try{
			socket.receive(packet);
			content = new String(buffer,0,buffer.length);
		} catch(IOException e){
			
		}
		
	}
	
	public Context getContext(){
		return this.context;
	}
	
	public void setContext(Context context){
		this.context=context;
	}

}
