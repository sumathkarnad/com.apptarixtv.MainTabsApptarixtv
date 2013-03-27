/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apptarixtv.constant;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import android.util.Log;



/**
 *
 * @author PSSPL Dev1
 */
public class HttpContn {
	HttpURLConnection httpConn;
	
    public HttpContn(){
        
    }
    public String coonectHttp(String url){
        String RESP_SRVR="ERROR";
        
        
    //   boolean ret = false;
     
        try  {
        	
			/* http = (HttpConnection) Connector.open(url);
            http.setRequestMethod(HttpConnection.GET);
            iStrm = http.openInputStream();*/
        	InputStream in=openHttpConnection( url);
           // HttpURLConnection httpConn = null;
			
			RESP_SRVR=processServerResponse(httpConn,in );
            
            System.out.println("Response from server:@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +RESP_SRVR);
            return  RESP_SRVR;
        }
        catch(Exception e){
            System.out.println("http error:"+e.toString());
        }
        return RESP_SRVR;
    }
    private String processServerResponse(HttpURLConnection http, InputStream inputStream)  {
        String RESP_SRVR="ERROR";
        try {
        	final char[] buffer = new char[1024];
        	StringBuilder out = new StringBuilder();
        	Reader in = new InputStreamReader(inputStream, "UTF-8");

        	int read;
        	do {
        		
        	  read = in.read(buffer, 0, buffer.length);
        	  if (read>0) {
        	    out.append(buffer, 0, read);
        	  }
        	} while (read>=0);
        	RESP_SRVR = new String(out);
        	
        	System.out.println("SIZE:::::::::::::::ByTES READ=="+ RESP_SRVR.length());
        	
                     //return RESP_SRVR;
            

        } catch (IOException ex) {
            ex.printStackTrace();
            
        }
        return RESP_SRVR;
    }
    
    public InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;
        String res_stautus;
  //      HttpGet get=new HttpGet(urlStr);
        
     //   String apiKey="e6d871be90a689";
            try {


                URL url = new URL(urlStr);
                URLConnection urlConn = url.openConnection();

                if (!(urlConn instanceof HttpURLConnection)) {

                    throw new IOException ("URL is not an Http URL");

                }

                httpConn = (HttpURLConnection)urlConn;
                httpConn.setConnectTimeout(2000);
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
//                httpConn.setRequestProperty("Authorization", "Basic " + BasicAuth.encode("admin", "1234"));             
                httpConn.connect();
                resCode =httpConn.getResponseCode();
                res_stautus=httpConn.getResponseMessage();
                Log.e("", "Response code========"+resCode);
                Log.e("", "Response message====="+res_stautus);

                if (resCode == HttpURLConnection.HTTP_OK) {

                	in = httpConn.getInputStream(); 

                } 

            } catch (MalformedURLException e) {
            e.printStackTrace();
            
            } catch (IOException e) {
            e.printStackTrace();
            }

        return in;
        }
}
