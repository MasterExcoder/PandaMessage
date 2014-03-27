package at.panda.pandamessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class MessageSender extends Message implements Runnable {

	Activity activity;
	public MessageSender(Activity activity, InetAddress ip, int port,String content) {
		this.activity = activity;
		setContent(content);
		setIp(ip);
		setPort(port);
		try {
			setSocket(new DatagramSocket(getPort()));
		} catch (SocketException e) {
			Toast.makeText(activity, "SocketException @ MessageSender", Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	public void run() {
		try{
			setPacket(new DatagramPacket(content.getBytes(), content.getBytes().length,ip,7777));
			socket.send(packet);
			Toast.makeText(activity, "SENDED", Toast.LENGTH_LONG).show();
			TextView messageview = (TextView) activity.findViewById(R.id.textview_messages);
			if(messageview.getText().equals("No Messages!")){
				messageview.setText("You: "+content);
			} else{
				messageview.setText(messageview.getText()+"\nYou: "+content);
			}
			
			
		} catch (IOException e){
			Toast.makeText(activity, "IO Exception @ MessageSender", Toast.LENGTH_LONG).show();
		}
		
	}

}
