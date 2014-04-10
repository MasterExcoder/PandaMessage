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
    static boolean receive=true;
    static boolean ready = false;
    private DatagramPacket successfull;


	public MessageReceiver() {
		// TODO Auto-generated constructor stub
		this.activity = null;
		this.buffer = new byte[2048];
		setPort(7777);
		setContent("");
		setSocket(null);
		setPacket(null);
	}
	
	public MessageReceiver(MainActivity activity){
		try{
            this.activity = activity;
            this.buffer = new byte[2048];
            setContent("");
			setPort(7777);	
			setSocket(new DatagramSocket(getPort()));
			setPacket(new DatagramPacket(buffer, buffer.length));
		} catch (SocketException e) {
			// TODO: handle exception
			Toast.makeText(activity, "SocketException @ MessageReceiver", Toast.LENGTH_LONG).show();
			Log.d("Socket Exception",e.getMessage());
            e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
        try{
            while(receive){
                receive();
                messages = (TextView) activity.findViewById(R.id.textview_messages);
                if(content.compareTo("OPENCONVERSATION")==0) {
                    sendAnswer("SUCCESSFULL");
                    startSender();
                }
                if(content.compareTo("SUCCESSFULL")==0){
                    sendAnswer("ACCOMPLISHED");
                    ready=true;
                }
                if(content.compareTo("ACCOMPLISHED")==0){
                    ready=true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public void receive(){
		try{
			this.socket.receive(this.packet);
            content =new String(packet.getData(),0,packet.getLength());

		} catch(IOException e){
			Toast.makeText(activity, "IO exception @ MessageReceiver", Toast.LENGTH_LONG).show();
			Log.d("IO Exception",e.getMessage());
		}
		
	}

    public void startSender(){
        MessageSender sender = activity.getSender();
        if(sender == null){
            sender = new MessageSender(activity,packet.getAddress(),8888,"");
            activity.setSender(sender);
            Thread send = new Thread(sender);
            send.start();

        }
    }

    public void sendAnswer(String answer){
        DatagramPacket packet1 = new DatagramPacket(answer.getBytes(), 0, answer.getBytes().length, packet.getAddress(), port);
        try {
            socket.send(packet1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(activity, "FAILCONVERSATION @ MessageReceiver:run", Toast.LENGTH_LONG).show();
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
