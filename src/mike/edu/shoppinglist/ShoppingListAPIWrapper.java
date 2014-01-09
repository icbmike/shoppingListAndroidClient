package mike.edu.shoppinglist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ShoppingListAPIWrapper {
	
	
	public enum APICall{
		GET_SHOPPING_LIST,
		GET_ITEM,
		ADD_ITEM,
		DELETE_ITEM,
		DELETE_LIST
	}
	
	public static List<String> getShoppingList(){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		HttpUriRequest request = new HttpGet("http://shoppinglistmike.herokuapp.com/shopping_list");
		List<String> result = null; 
		try {
			HttpResponse response = httpClient.execute(request);
			String responseBody = getResponseBody(response);
			
			JSONArray jsonArray = new JSONArray(responseBody);
			result = new ArrayList<String>();
			
			for (int i = 0; i < jsonArray.length(); i++) {
				result.add(jsonArray.getString(i));
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}


	private static String getResponseBody(HttpResponse response) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line;
		StringBuilder sb = new StringBuilder();
		while((line = bufferedReader.readLine()) != null){
			sb.append(line);
		}
		return sb.toString();
	}
	
	public static void addShoppingListItem(String item){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost("http://shoppinglistmike.herokuapp.com/shopping_list");
		request.addHeader("Content-Type", "application/json");
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("shopping_list_item", item);
			request.setEntity(new StringEntity(jsonObject.toString()));
			HttpResponse response = httpClient.execute(request);
			Log.i("ShoppingList", "Status: " + response.getStatusLine().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteShoppingListItem(int index){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest request = new HttpDelete("http://shoppinglistmike.herokuapp.com/shopping_list/" + index);
		try {
			httpClient.execute(request);
					
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteShoppingList(){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest request = new HttpDelete("http://shoppinglistmike.herokuapp.com/shopping_list");
		try {
			httpClient.execute(request);
					
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getShoppingListItem(int index){
		return "Heyo";
	}
	
	
	
	
}
