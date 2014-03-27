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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
			
			if(content.equals("OPENCONVERSATION")){
				
				String answer = "SUCCESSFULL";
				packet = new DatagramPacket(answer.getBytes(), 0, answer.getBytes().length, packet.getAddress(), port);
				try {
					socket.send(packet);
					Toast.makeText(activity, "OPENCONVERSATION", Toast.LENGTH_LONG);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(content.equals("SUCCESSFULL")){
				EditText message = (EditText) activity.findViewById(R.id.textfield_messageText);
				Button send = (Button) activity.findViewById(R.id.btn_send);
				EditText targetip = (EditText) activity.findViewById(R.id.textfield_targetIP);
				Button start = (Button) activity.findViewById(R.id.btn_start);
				Toast.makeText(activity, "SUCCESSFULL", Toast.LENGTH_LONG);
				targetip.setClickable(false);
				start.setClickable(false);
				message.setClickable(true);
				send.setClickable(true);
			}
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
