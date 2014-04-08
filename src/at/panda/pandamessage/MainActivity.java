package at.panda.pandamessage;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import android.widget.Toast;

public class MainActivity extends Activity {
	MessageReceiver receiver;
	MessageSender sender;
	
	EditText targetipview;
	EditText messageview;
	String targetip,prevmessage;

    Thread sendthread,receivethread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
        prevmessage="";

        sender = null;

        receiver=new MessageReceiver(this);
        receivethread = new Thread(receiver);
        receivethread.start();

        Timer updateTimer = new Timer("sensorUpdate");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateGUI();
            }
        }, 0, 1000);
	}


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void start(View v){
		try{
			targetipview = (EditText) findViewById(R.id.textfield_targetIP);
			targetip = targetipview.getText().toString();


            Log.d("Status","Reiceiver: "+receivethread.getState().toString());
            Toast.makeText(this,"Ziel-Ip: "+targetip,Toast.LENGTH_SHORT).show();

            if(sender==null){
                sender = new MessageSender(this,InetAddress.getByName(targetip),8888,"OPENCONVERSATION");
                sendthread = new Thread(sender);
                sendthread.start();
             }

		} catch(UnknownHostException e){
			e.printStackTrace();
		}
		
	}
	
	public void send(View v){
		try{
            String message="Error";
            messageview = (EditText) findViewById(R.id.textfield_messageText);

            if(messageview != null && messageview.getText()!=null){
                message = messageview.getText().toString();
                messageview.setText("");
            }
			sender.setContent(message);
            sender.send();
            TextView messages = (TextView) findViewById(R.id.textview_messages);
            if(messages.getText().toString().equals("No Messages!")){
                messages.setText("You: "+message);
            } else{
                messages.setText(messages.getText()+"\nYou: "+message);
            }
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

    public void setMessages(String message){
        if(!message.equals("OPENCONVERSATION")&&!message.equals("SUCCESSFULL")&&!message.equals("ACCOMPLISHED")&&!message.equals("")&&!message.equals(prevmessage))
        {
            TextView messages = (TextView) findViewById(R.id.textview_messages);
            if(messages != null && messages.getText()!=null) {
                if (messages.getText().toString().compareTo("No Messages!") == 0) {
                    messages.setText("Partner: " + message);
                    prevmessage=message;
                } else {
                    messages.setText(messages.getText().toString() + "\nPartner: " + message);
                    prevmessage=message;
                }
            }
        }

    }


    private void updateGUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                setButtonsForConversation();
                if(receiver!=null){
                    setMessages(receiver.getContent());
                }
            }
        });
    }


    public void setButtonsForConversation(){

        if(MessageReceiver.ready==true){
            EditText message = (EditText) this.findViewById(R.id.textfield_messageText);
            Button send = (Button) this.findViewById(R.id.btn_send);
            EditText targetip = (EditText) this.findViewById(R.id.textfield_targetIP);
            Button start = (Button) this.findViewById(R.id.btn_start);

            targetip.setText(sender.getIp().toString());
            targetip.setEnabled(false);
            start.setEnabled(false);
            message.setEnabled(true);
            send.setEnabled(true);
        }

    }

    public MessageReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(MessageReceiver receiver) {
        this.receiver = receiver;
    }

    public MessageSender getSender() {
        return sender;
    }

    public void setSender(MessageSender sender) {
        this.sender = sender;
    }
}
