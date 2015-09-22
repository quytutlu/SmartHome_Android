package nguyenquytu.smarthome;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.ParseException;

public class WebServiceHandler {
	
	public WebServiceHandler() {
		
	}
	public String getJSONData(String url){
		String jsonstr="";
		URL url1 = null;
		try {
			url1 = new URL(url);
			URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(), url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
			url1 = uri.toURL();
			HttpGet httppost = new HttpGet(uri.toString());
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			jsonstr = EntityUtils.toString(entity);
			return jsonstr;
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
}
