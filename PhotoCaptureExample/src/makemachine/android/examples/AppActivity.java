package makemachine.android.examples;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
 
public class AppActivity extends Activity {
	
	Button button,register;
	EditText email,password;
	Context context;// = this;
	SharedPreferences sharedPref;// = this.getPreferences(Context.MODE_PRIVATE);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		addListenerOnButton();
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		context = this;
		checkIfUserIsLoggedIn();
		
	}
 
	public void addListenerOnButton() {
 
		
		register = (Button) findViewById(R.id.buttonRegister);
		button = (Button) findViewById(R.id.buttonLogin);
		email = (EditText) findViewById(R.id.editTextemail);
		password = (EditText) findViewById(R.id.editTextPassword);
 
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				Integer _userid;
				_userid=getUserid(email.getText().toString(),password.getText().toString());
	    		SharedPreferences.Editor editor = sharedPref.edit();
	    		editor.putInt("loggedInUser", _userid);
	    		editor.commit();
	    		Intent intent = new Intent(context, MainLayoutActivity.class);
                startActivity(intent); 
			}
 
		});
		register.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				
				Integer _userid;
				_userid=getUserid(email.getText().toString(),password.getText().toString());
	    		SharedPreferences.Editor editor = sharedPref.edit();
	    		editor.putInt("loggedInUser", _userid);
	    		editor.commit();
	    		Intent intent = new Intent(context, MainLayoutActivity.class);
                startActivity(intent); 
			}
 
		});
 
	}
	 private void checkIfUserIsLoggedIn() {
	    	SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
	    	if(sharedPref.getInt("loggedInUser",-1)!=-1)
	    	{
	    		Intent intent = new Intent(context, MainLayoutActivity.class);
                startActivity(intent);   	    		
	    	}
	    	
	    	
			
		}
	
	public Integer getUserid(String email,String Password)
    {
    	 try {
    		 
 			@SuppressWarnings("deprecation")
				DefaultHttpClient httpClient = new DefaultHttpClient();
 			HttpPost postRequest = new HttpPost(
 				"http://ancient-caverns-1756.herokuapp.com/services/Recorder/AuthenticateUser");
 	 
 			StringEntity input = new StringEntity("{\"email\":\""+email+"\",\"password\":\""+ Password +"\"}");
 			input.setContentType("application/json");
 			postRequest.setEntity(input);
 			
 			HttpResponse response = httpClient.execute(postRequest);
 	 
 			if (response.getStatusLine().getStatusCode() != 201) {
 				BufferedReader br = new BufferedReader(
	                        new InputStreamReader((response.getEntity().getContent())));
	 
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				return Integer.valueOf(output);
			}
 				return 0;
 			}
 	 
 			BufferedReader br = new BufferedReader(
 	                        new InputStreamReader((response.getEntity().getContent())));
 	 
 			String output;
 			System.out.println("Output from Server .... \n");
 			while ((output = br.readLine()) != null) {
 				System.out.println(output);
 				return Integer.valueOf(output);
 			}
 			
 			httpClient.getConnectionManager().shutdown();
 			return 1;
 		  } catch (MalformedURLException e) {
 	 
 			e.printStackTrace();
 	 
 		  } catch (IOException e) {
 	 
 			e.printStackTrace();
 	 
 		  }
    	 return 0;
    }
	public Integer registerUserid(String email,String Password)
    {
    	 try {
    		 
 			@SuppressWarnings("deprecation")
				DefaultHttpClient httpClient = new DefaultHttpClient();
 			HttpPost postRequest = new HttpPost(
 				"http://ancient-caverns-1756.herokuapp.com/services/Recorder/CreateUser");
 	 
 			StringEntity input = new StringEntity("{\"email\":\""+email+"\",\"password\":\""+ Password +"\"}");
 			input.setContentType("application/json");
 			postRequest.setEntity(input);
 			
 			HttpResponse response = httpClient.execute(postRequest);
 	 
 			if (response.getStatusLine().getStatusCode() != 201) {
 				BufferedReader br = new BufferedReader(
	                        new InputStreamReader((response.getEntity().getContent())));
	 
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				return Integer.valueOf(output);
			}
 				return 0;
 			}
 	 
 			BufferedReader br = new BufferedReader(
 	                        new InputStreamReader((response.getEntity().getContent())));
 	 
 			String output;
 			System.out.println("Output from Server .... \n");
 			while ((output = br.readLine()) != null) {
 				System.out.println(output);
 				return Integer.valueOf(output);
 			}
 			
 			httpClient.getConnectionManager().shutdown();
 			return 1;
 		  } catch (MalformedURLException e) {
 	 
 			e.printStackTrace();
 	 
 		  } catch (IOException e) {
 	 
 			e.printStackTrace();
 	 
 		  }
    	 return 0;
    }
 
}