package com.apptarixtv.constant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DoPost {
	 
	 
	 String msg;
	 String urlBasePath;
	 public boolean errorinpost;
	 public DoPost() {
		
		 //doPost(detail);
	}
	public String doPost(String detail,String myurl) {
		URL url = null;
//        String apiKey="c01fdc81727feb664b6b9880975b7000";
        this.urlBasePath=myurl;
        DataOutputStream wr = null;
       // urlBasePath="http://api.geanly.in/ma/order_ma/index";
      //  String detail = "API-Key=e6d871be90a689&orderInfo={\"booking\":{\"restaurantinfo\":{\"id\":\"5722\"},\"referrer\":{\"id\": \"9448476530\" },	\"bookingdetails\":{\"instructions\":\"Make the stuff spicy\",\"bookingtime\": \"2012-02-06 07:45 pm\", \"num_guests\": \"5\"},	\"customerinfo\":{\"name\":\"Mohsin Bagwan\",	\"mobile\":\"1234567890\",	\"email\": \"sonali@pappilon.in\",	\"landline\":{ \"number\":\"0908998393\",\"ext\":\"456\"}}}}";
        HttpURLConnection connection = null;
		int statusCode;
        String statusReason;
		// TODO Auto-generated method stub
		try {
			url = new URL(urlBasePath);
			errorinpost=false;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block.
			errorinpost=true;
			e.printStackTrace();
			
		}
        try {
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput (true);
			errorinpost=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorinpost=true;
			e.printStackTrace();
		}
        connection.setConnectTimeout(10000);
        connection.setUseCaches(false);
        connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
        connection.setRequestProperty("Accept","*/*");
//        connection.setRequestProperty("X-API-KEY", apiKey);
        connection.setRequestProperty("source", "je-android-app");
		//connection.setRequestProperty("Authorization", "Basic " +BasicAuth.encode("admin", "1234"));
       // connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Type", "application/json");
    try {
		connection.setRequestProperty("Content-Length", "" + Integer.toString(detail.getBytes("UTF-8").length));
		errorinpost=false;
    } catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		errorinpost=true;
		e1.printStackTrace();
	}

       
		
			try {
				wr = new DataOutputStream(connection.getOutputStream());
				errorinpost=false;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				errorinpost=true;
				e1.printStackTrace();
				System.out.println("Error in wr :"+e1.toString());
			}
			
		
		try {
			wr.write(detail.getBytes("UTF-8"));
			errorinpost=false;
		} catch (UnsupportedEncodingException e) {
			errorinpost=true;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			errorinpost=true;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			wr.flush();
			wr.close();
			errorinpost=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorinpost=true;
			e.printStackTrace();
		}
        
        try {
        	
			statusCode = connection.getResponseCode();
			System.out.println("Status Code: "+statusCode);
			errorinpost=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorinpost=true;
			e.printStackTrace();
		}
        try {
			statusReason = connection.getResponseMessage();
			System.out.println("Status Response: "+statusReason);
			
			//Get Response	
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			
//			StringBuffer response = new StringBuffer(); 

			while((msg = rd.readLine()) != null) {
 			     System.out.println("Resonse from server== "+msg);
 			    return msg;
 			    // getResponse();
			}

			rd.close();
			errorinpost=false;
		} catch (IOException e) {
			errorinpost=true;
			e.printStackTrace();
		} 
       return msg;
	}
	
	
	
		public String convertStreamToString(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				inputStream.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}
/*private void getResponse() throws JSONException {
if(msg!=null){
	Log.e("Msg=============", msg);
	System.out.println("Message :"+msg);
	   JSONObject rest_info_root = null;
		   try {
		   	rest_info_root = new JSONObject(msg);
		   } catch (JSONException e) {
		   	// TODO Auto-generated catch block
		   	e.printStackTrace();
		   }
		   try {
		                       
		   	  ORDER_ID=rest_info_root.getString("order_id");
		   	  Log.e("Order id is================== ", ORDER_ID);
		   	  System.out.println("Order id is================== "+ORDER_ID);
		   	  
		      ERROR=rest_info_root.getString("error");
		      Log.e("Error is================== ", ERROR);
		       System.out.println("Error is====================== "+ERROR);
		   } catch (JSONException ex) {
		        ERROR=rest_info_root.getString("error");
		       ex.printStackTrace();
		     //  return ERROR;
		   }
}else{
	System.out.println("I am in else.......");
}	
}*/


}
