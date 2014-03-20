package at.panda.pandamessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.content.Context;

public class MessageSender extends Message implements Runnable {

	Context context;
	
	public MessageSender(Context context, InetAddress ip, int port, String content) {
		this.context = context;
		setIp(ip);
		setPort(port);
		try {
			setSocket(new DatagramSocket(port));
		} catch (SocketException e) {
			e.printStackTrace();
		}
		setContent(content);
	}

	@Override
	public void run() {
		
	}

}
