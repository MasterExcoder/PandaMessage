package at.panda.pandamessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MessageSender extends Message implements Runnable {

	Activity activity;
	public MessageSender(Activity activity, InetAddress ip, int port,String content) {
		this.activity = activity;
        setPacket(new DatagramPacket(content.getBytes(),0,content.getBytes().length));
		setContent(content);
		setIp(ip);
		setPort(port);
		try {
            if(this.getSocket()!=null){
                this.getSocket().setReuseAddress(true);
            }
			setSocket(new DatagramSocket(getPort()));

		} catch (SocketException e) {
			Toast.makeText(activity, "SocketException @ MessageSender", Toast.LENGTH_LONG).show();
            Log.d("Socket Exception",e.getMessage());
            e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try{
            Looper.prepare();
            socket.connect(this.getIp(),7777);
            this.packet.setData(content.getBytes(),0,content.getBytes().length);
            this.packet.setAddress(ip);
            this.packet.setPort(7777);
			socket.send(this.getPacket());
            Toast.makeText(activity, "SENDED", Toast.LENGTH_LONG).show();

		} catch (IOException e){
			Toast.makeText(activity, "IO Exception @ MessageSender", Toast.LENGTH_LONG).show();
			Log.d("IO Exception",e.getMessage());
		}

		
	}

}
