package datastructures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
 
public class DatabaseManager {
 
	private static DatabaseManager instance;
	private InputStream inputStream = null;
    private JSONArray jArray = null;
    private String json = "";
    private String url = "http://mobileapprwz.bplaced.net/db_query.php";
 
    //Singleton Pattern
    public static DatabaseManager instance()
    {
        if(instance == null)
        {
            instance = new DatabaseManager();
        }
        return instance;
    }
    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONArray makeHttpRequest(String query) 
    {
        // Making HTTP request
        try 
        {
            // request method is POST
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query", query));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
        } 
        catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        } 
        catch (ClientProtocolException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        try 
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            json = sb.toString();
        } 
        catch (Exception e) 
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try 
        {
        	Log.i("Database", json);
            jArray = new JSONArray(json);
        } 
        catch (JSONException e) 
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jArray;
    }
}