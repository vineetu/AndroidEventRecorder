package makemachine.android.examples;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import android.app.Activity;
import android.os.*;
import android.widget.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;














import  com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class PhotoCaptureExample extends Activity implements LocationListener
{
	protected Button _button;
	protected ImageView _image;
	protected TextView _field;
	protected String _path;
	protected boolean _taken;
	protected String _name;
	protected Integer _userid;
//	private TextView latituteField;
//	  private TextView longitudeField;
	  private LocationManager locationManager;
	  private String provider;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
       
        _image = ( ImageView ) findViewById( R.id.image );
        _field = ( TextView ) findViewById( R.id.field );
        _button = ( Button ) findViewById( R.id.button );
        _button.setOnClickListener( new ButtonClickHandler() );
        
        _name = UUID.randomUUID().toString()+"jpg";
        _path = Environment.getExternalStorageDirectory() + "/Pictures/"+_name;
       // testMethod();
    //    testMethod1();
        

        
       
    }
    @Override
    public void onLocationChanged(Location location) {
      int lat = (int) (location.getLatitude());
      int lng = (int) (location.getLongitude());
//      latituteField.setText(String.valueOf(lat));
//      longitudeField.setText(String.valueOf(lng));
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
     // locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
      super.onPause();
    //  locationManager.removeUpdates(this);
    }
    private void testMethod() {
    	SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    	if(sharedPref.getInt("loggedInUser",-1)!=-1)
    	{
    		_userid=Integer.valueOf(sharedPref.getInt("loggedInUser",-1));
    	}
    	else
    	{
    		_userid=getUserid("vineetu@gmail.com","12345");
    		SharedPreferences.Editor editor = sharedPref.edit();
    		editor.putInt("loggedInUser", _userid);
    		editor.commit();
    	}
    	
		
	}
    private void testMethod1() {
    	SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    	
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove("loggedInUser");
		editor.commit();
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
	public class ButtonClickHandler implements View.OnClickListener 
    {
    	public void onClick( View view ){
    		Log.i("MakeMachine", "ButtonClickHandler.onClick()" );
    		startCameraActivity();
    	}
    }
    
    protected void startCameraActivity()
    {
    	Log.i("MakeMachine", "startCameraActivity()" );
    	File file = new File( _path );
    	Uri outputFileUri = Uri.fromFile( file );
    	
    	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
    	intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
    	
    	startActivityForResult( intent, 0 );
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
    	Log.i( "MakeMachine", "resultCode: " + resultCode );
    	switch( resultCode )
    	{
    		case 0:
    			Log.i( "MakeMachine", "User cancelled" );
    			break;
    			
    		case -1:
    			onPhotoTaken();
    			break;
    	}
    }
    
    protected void onPhotoTaken()
    {
    	Log.i( "MakeMachine", "onPhotoTaken" );
    	
    	_taken = true;
    	
    	BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
    	
    	Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
    	
    	_image.setImageBitmap(bitmap);
    	
    	_field.setVisibility( View.GONE );
    	
    	
    	
    	 Intent intentMessage=new Intent();
         
         
         // put the message to return as result in Intent
         intentMessage.putExtra("MESSAGE",_name);
         // Set The Result in Intent
         setResult(90,intentMessage);
         // finish The activity 
         finish();
    	  
    }
    
//    public void post(String url, List<NameValuePair> nameValuePairs) {
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpContext localContext = new BasicHttpContext();
//       
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.addHeader("dataType", "APPLICATION_JSON");
//        httpPost.addHeader("contentType", "false");
//        httpPost.addHeader("processData", "false");
//        
//        // httpPost.setHeader(name, value);
//        HttpResponse response;
//        try {
//        	MultipartEntityBuilder builder = MultipartEntityBuilder.create(); 
//           // MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//            
//            for(int index=0; index < nameValuePairs.size(); index++) {
//                if(nameValuePairs.get(index).getName().equalsIgnoreCase("image")) {
//                    // If the key equals to "image", we use FileBody to transfer the data
//                   // entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue())));
//                    builder.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue())));
//                } else {
//                    // Normal string data
//                  //  entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
////                    builder.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue(),ContentType.TEXT_PLAIN));
//                    builder.addTextBody(nameValuePairs.get(index).getName(),  nameValuePairs.get(index).getValue());
//                }
//            }
//            
//            httpPost.setEntity(builder.build());
//
//             response = httpClient.execute(httpPost);
//             System.out.print("");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        catch(Exception e)
//        {
//        	System.out.print(e.getMessage());
//        }
//    }
    public String apachePost()
    {
    	String s;
    	 try {
    		 
    		 SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    			 AmazonS3 s3 = new AmazonS3Client();
    			 FileInputStream fileInputStream = new FileInputStream(new File(_path));
    			 String filePath;// = SERVER_UPLOAD_LOCATION_FOLDER + headerOfFilePart.getFileName();
    			 //saveFile(fileInputStream, filePath);
    			 ObjectMetadata temp = new ObjectMetadata();
    			 temp.setContentType("image/jpeg");
    			 s=_name;
    			 PutObjectRequest putObject = new PutObjectRequest("eventrecorder", "content/"+s+".jpg",fileInputStream, temp);
    				s3.putObject(putObject);
    				SharedPreferences.Editor editor = sharedPref.edit();
    	    		editor.putString("photoTakenPath", s);
    	    		editor.commit();
    				fileInputStream.close();
    				return "https://s3.amazonaws.com/eventrecorder/content/"+s;
    				
    		  } catch (MalformedURLException e) {
    	 
    			e.printStackTrace();
    	 
    		  } catch (IOException e) {
    	 
    			e.printStackTrace();
    	 
    		  }
    	 return "";
    	 
    }
    
   
    
    @Override 
    protected void onRestoreInstanceState( Bundle savedInstanceState){
    	Log.i( "MakeMachine", "onRestoreInstanceState()");
    	if( savedInstanceState.getBoolean( PhotoCaptureExample.PHOTO_TAKEN ) ) {
    		onPhotoTaken();
    	}
    }
    
    @Override
    protected void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( PhotoCaptureExample.PHOTO_TAKEN, _taken );
    }
}