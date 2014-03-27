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

public class MainActivity extends Activity {
	MessageReceiver receiver;
	MessageSender sender;
	
	EditText targetipview;
	EditText messageview;
	String targetip;
	
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
			if(targetipview != null){
				targetip = targetipview.getText().toString();
			}
			receiver=new MessageReceiver("10.0.0.10",this);
			sender = new MessageSender(this,InetAddress.getByName(targetip),8888,"OPENCONVERSATION");
		} catch(UnknownHostException e){
			e.printStackTrace();
		}
		
	}
	
	public void send(View v){
		try{
			String message = messageview.getText().toString();
			sender = new MessageSender(this,InetAddress.getByName(targetip),8888,message);
			Thread send = new Thread(sender);
			send.start();
		} catch (UnknownHostException e){
			e.printStackTrace();
		}
		
	}

}
