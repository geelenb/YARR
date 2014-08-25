package be.geelen.yarr.tools;

import android.os.AsyncTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class HttpAsyncTask extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String...strings){
        String response="";

        HttpClient client=new DefaultHttpClient();
        try{
            HttpGet httpGet=new HttpGet(strings[0]);
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            response=client.execute(httpGet,responseHandler);
        }catch(ClientProtocolException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return response;
    }
}
