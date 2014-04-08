package at.panda.pandamessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import android.widget.Toast;

public class MainActivity extends Activity {
	MessageReceiver receiver;
	MessageSender sender;
	
	EditText targetipview;
	EditText messageview;
	String targetip;

    Thread sendthread,receivethread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
		targetipview = (EditText) findViewById(R.id.textfield_targetIP);
		messageview = (EditText) findViewById(R.id.textfield_messageText);
	}

    @Override
    protected void onPause() {
        super.onPause();
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

            if(receiver ==null){
                receiver=new MessageReceiver(this);
                receivethread = new Thread(receivethread);
                receivethread.start();
            }
            Toast.makeText(this,"ZielIp: "+targetip,Toast.LENGTH_SHORT).show();
            if(sender ==null){
                sender = new MessageSender(this,InetAddress.getByName(targetip),8888,"OPENCONVERSATION");
                sendthread = new Thread(sender);
            } else {
                sender.setIp(InetAddress.getByName(targetip));
                sender.setContent("OPENCONVERSATION");
                sendthread = new Thread(sender);
            }
            sendthread.start();


		} catch(UnknownHostException e){
			e.printStackTrace();
		}
		
	}
	
	public void send(View v){
		try{
            String message="Error";
            if(messageview != null){
                message = messageview.getText().toString();
            }
			sender.setContent("message");
            sendthread.start();
            TextView messageview = (TextView) findViewById(R.id.textview_messages);
            if(messageview.getText().toString().equals("No Messages!")){
                messageview.setText("You: "+message);
            } else{
                messageview.setText(messageview.getText()+"\nYou: "+message);
            }
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

    public void setMessages(String message){
        if(messageview != null && messageview.getText().toString().equals("No Messages!")){
            messageview.setText("Partner: "+message);
        } else if(messageview != null)
        {
            messageview.setText(messageview.getText().toString()+"\nPartner: "+message);
        }
    }
}
