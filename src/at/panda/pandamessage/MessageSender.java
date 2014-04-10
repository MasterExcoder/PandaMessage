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
        try {
            this.activity = activity;
            setPacket(new DatagramPacket(content.getBytes(), 0, content.getBytes().length));
            setContent(content);
            setIp(ip);
            setPort(port);
            setSocket(new DatagramSocket(getPort()));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void run() {
        socket.connect(this.getIp(), 7777);
        send();
	}

    public void send(){
        try {
            byte[] raw = content.getBytes();
            this.packet.setData(raw,0,raw.length);
            this.packet.setAddress(getIp());
            this.packet.setPort(7777);
            socket.send(this.getPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
