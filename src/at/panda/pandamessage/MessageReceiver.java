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
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MessageReceiver extends Message implements Runnable {
	private MainActivity activity;
	private byte[] buffer;
    private TextView messages;
	
	public MessageReceiver() {
		// TODO Auto-generated constructor stub
		this.activity = null;
		this.buffer = new byte[2048];
		try{
			setPort(7777);
			setContent("");
			setSocket(new DatagramSocket(port));
			setPacket(new DatagramPacket(buffer, buffer.length));
		} catch (SocketException e) {
			// TODO: handle exception
		}
		
		
	}
	
	public MessageReceiver(MainActivity activity){
		this.activity = activity;
		this.buffer = new byte[2048];
		try{
            if(this.getSocket()!=null){
                this.getSocket().setReuseAddress(true);
            }
			setPort(7777);	
			setSocket(new DatagramSocket(getPort()));
			setPacket(new DatagramPacket(buffer, buffer.length));
		} catch (SocketException e) {
			// TODO: handle exception
			Toast.makeText(activity, "SocketException @ MessageReceiver", Toast.LENGTH_LONG).show();
			Log.d("Socket Exception",e.getMessage());
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
        Looper.prepare();
		while(true){

			receive();
			messages = (TextView) activity.findViewById(R.id.textview_messages);
			if(content.equals("OPENCONVERSATION")){
				String answer = "SUCCESSFULL";
				packet = new DatagramPacket(answer.getBytes(), 0, answer.getBytes().length, packet.getAddress(), port);
                Toast.makeText(activity, "OPENCONVERSATION", Toast.LENGTH_LONG).show();
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(activity, "FAILCONVERSATION @ MessageReceiver:run", Toast.LENGTH_LONG).show();
					Log.d("IO Exception",e.getMessage());
				}
			}
			if(content.equals("SUCCESSFULL")){
				EditText message = (EditText) activity.findViewById(R.id.textfield_messageText);
				Button send = (Button) activity.findViewById(R.id.btn_send);
				EditText targetip = (EditText) activity.findViewById(R.id.textfield_targetIP);
				Button start = (Button) activity.findViewById(R.id.btn_start);
				Toast.makeText(activity, "SUCCESSFULL", Toast.LENGTH_LONG).show();
				targetip.setClickable(false);
				start.setClickable(false);
				message.setClickable(true);
				send.setClickable(true);
			}
            else{
                if(activity != null){
                    activity.setMessages(buffer.toString());
                }
            }
			
		}

	}
	
	public void receive(){
		try{
			socket.receive(packet);
            buffer = packet.getData();
			this.content = new String(buffer,0,buffer.length);
		} catch(IOException e){
			Toast.makeText(activity, "IO exception @ MessageReceiver", Toast.LENGTH_LONG).show();
			Log.d("IO Exception",e.getMessage());
		}
		
	}
	
	public Activity getContext(){
		return this.activity;
	}
	
	public void setContext(MainActivity activity){
		this.activity=activity;
	}

}
