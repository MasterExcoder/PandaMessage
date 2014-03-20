package at.panda.pandamessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.app.Activity;
import android.content.Context;

public class MessageSender extends Message implements Runnable {

	Activity activity;
	public MessageSender(Activity activity, InetAddress ip, int port,String content) {
		this.activity = activity;
		setContent(content);
		setIp(ip);
		setPort(port);
		try {
			setSocket(new DatagramSocket(8888));
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		try{
			setPacket(new DatagramPacket(content.getBytes(), content.getBytes().length,ip,7777));
			socket.send(packet);
		} catch (IOException e){
			
		}
		
	}

}
