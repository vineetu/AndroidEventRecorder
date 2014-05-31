package makemachine.android.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainLayoutActivity  extends Activity implements LocationListener{
	Button buttonTakePic;
	Button buttonSave,buttonLogout;
	EditText editText;
	ImageView im;
	String photourl;
	private LocationManager locationManager;
	  private String provider;
	private String gps;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.mainlayout);
		photourl="";
		gps="";
		addListenerOnButton();
		im = (ImageView) findViewById(R.id.imageView1);
		buttonLogout = (Button) findViewById(R.id.buttonlogout);
		buttonLogout.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				
				logout();
				finish();
			}
 
		});
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
              // TODO Auto-generated method stub
              super.onActivityResult(requestCode, resultCode, data);
              
 
               // check if the request code is same as what is passed  here it is 2
                if(requestCode==90)
                      {
                         // fetch the message String
                	photourl=data.getStringExtra("MESSAGE"); 
                          // Set the message string in textView
                	im.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + "/Pictures/"+photourl));
                          
            
                      }
            
  }
	public void addListenerOnButton() {
 
		final Context context = this;
 
		buttonTakePic = (Button) findViewById(R.id.buttonTakePicture);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		editText = (EditText) findViewById(R.id.editText1);
		buttonTakePic.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, PhotoCaptureExample.class);
                            startActivityForResult(intent, 90);   
 
			}
 
		});
		buttonSave.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
 
				// Get the location manager
		        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		        // Define the criteria how to select the locatioin provider -> use
		        // default
		        Criteria criteria = new Criteria();
		        provider = locationManager.getBestProvider(criteria, false);
		        Location location = locationManager.getLastKnownLocation(provider);

		        // Initialize the location fields
		        if (location != null) {
		          double lat = (double) (location.getLatitude());
		          double lng = (double) (location.getLongitude());
		   	      gps = String.valueOf(lat) +","+ String.valueOf(lng);
		          onLocationChanged(location);
		        } else {
		        	gps = "Not Available";
		        }
		        
//		        getPhotoPath();
//		        editText.getText();
		        apachePost(editText.getText().toString(),gps,photourl);
		        
		        editText.setText("");
		        im.setImageURI(Uri.EMPTY);
		        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
		        im.setLayoutParams(layoutParams);
			}
 
		});
 
	}
	public String getPhotoPath()
	{
		SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        
    	if(sharedPref.getString("photoTakenPath","")!="")
    	{
    		return sharedPref.getString("photoTakenPath","");
    	}
    	else
    	{
    		return "";
    	}
	}
	public void apachePost(String notes,String gps, String photourl)
    {
    	 try {
    		 
    			@SuppressWarnings("deprecation")
				DefaultHttpClient httpClient = new DefaultHttpClient();
    			HttpPost postRequest = new HttpPost(
    				"http://ancient-caverns-1756.herokuapp.com/services/Recorder/RecordNewEvent1");
    			AmazonS3 s3 = new AmazonS3Client();
   			 FileInputStream fileInputStream = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/Pictures/"+photourl));
   			 String filePath;// = SERVER_UPLOAD_LOCATION_FOLDER + headerOfFilePart.getFileName();
   			 //saveFile(fileInputStream, filePath);
   			 ObjectMetadata temp = new ObjectMetadata();
   			 temp.setContentType("image/jpeg");
   			
   			 PutObjectRequest putObject = new PutObjectRequest("eventrecorder", "content/"+photourl+".jpg",fileInputStream, temp);
   				s3.putObject(putObject);
   				
   				fileInputStream.close();
    			StringEntity input = new StringEntity("{\"gpslocation\":\""+gps+"\",\"status\":\""+notes+"\",\"userid\":\"1\",\"photourl\":\""+"https://s3.amazonaws.com/eventrecorder/content/"+photourl+"\"}");
    			input.setContentType("application/json");
    			postRequest.setEntity(input);
    			
    			HttpResponse response = httpClient.execute(postRequest);
    	 
    			
    	 
    			BufferedReader br = new BufferedReader(
    	                        new InputStreamReader((response.getEntity().getContent())));
    	 
    			String output;
    			System.out.println("Output from Server .... \n");
    			while ((output = br.readLine()) != null) {
    				System.out.println(output);
    			}
    	 
    			httpClient.getConnectionManager().shutdown();
    	 
    		  } catch (MalformedURLException e) {
    	 
    			e.printStackTrace();
    	 
    		  } catch (IOException e) {
    	 
    			e.printStackTrace();
    	 
    		  }
    	 
    }
	 @Override
	    public void onLocationChanged(Location location) {
	      int lat = (int) (location.getLatitude());
	      int lng = (int) (location.getLongitude());
//	      latituteField.setText(String.valueOf(lat));
//	      longitudeField.setText(String.valueOf(lng));
	    }
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	      // TODO Auto-generated method stub

	    }

	    @Override
	    public void onProviderEnabled(String provider) {
	      Toast.makeText(this, "Enabled new provider " + provider,
	          Toast.LENGTH_SHORT).show();

	    }

	    @Override
	    public void onProviderDisabled(String provider) {
	      Toast.makeText(this, "Disabled provider " + provider,
	          Toast.LENGTH_SHORT).show();
	    }
	    /* Request updates at startup */
	    @Override
	    protected void onResume() {
	      super.onResume();
	  //    locationManager.requestLocationUpdates(provider, 400, 1, this);
	    }

	    /* Remove the locationlistener updates when Activity is paused */
	    @Override
	    protected void onPause() {
	      super.onPause();
	    //  locationManager.removeUpdates(this);
	    }
	    private void logout() {
	    	SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
	    	
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.remove("loggedInUser");
			editor.commit();
	    }
}
