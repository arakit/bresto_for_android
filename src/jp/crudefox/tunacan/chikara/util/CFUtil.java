package jp.crudefox.tunacan.chikara.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

public class CFUtil {

	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/07/06
	 *
	 */

	/*		便利メソッドをここにまとめてます、。q
	 *
	 *
	 */


	public static boolean LOCAL_DEBUG = false;


	public static final String TAG = "cf_hellin";


	public static void Log(String str){
		if(isDebug()) android.util.Log.d(TAG,str);
	}
	public static void Log(String str,Throwable tr){
		if(isDebug()) android.util.Log.d(TAG,str,tr);
	}

	public static boolean isDebug(){
		return true;
	}






	public static byte[] toBytesFtomInputStream(InputStream is){
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = 0;
		try {
			while((len=is.read(buf))!=-1){
				ba.write(buf, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return ba.toByteArray();
	}

	public static String toBase64FromBytes(byte[] data){
		if(data==null) return null;
		return new String( android.util.Base64.encode(data, android.util.Base64.DEFAULT) );
	}

	public static byte[] toBytesFromBase64(String data){
		if(data==null) return null;
		return android.util.Base64.decode(data, android.util.Base64.DEFAULT);
	}




	public static final String getStringH(JSONObject j, String name, String def){
		if(j==null) return null;
		if( j.has(name) ){
			try { return j.getString(name); } catch (JSONException e) {  }
		}
		return def;
	}
	public static final String getStringN(JSONObject j, String name, String def){
		if(j==null) return def;
		try { return j.getString(name); } catch (JSONException e) {  }
		return def;
	}
	public static final long getLongN(JSONObject j, String name, long def){
		if(j==null) return def;
		try { return j.getLong(name); } catch (JSONException e) {  }
		return def;
	}



	private static final SimpleDateFormat sDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static final Date parseDateTmeOrDate(String str){
		Date date = parseDateTme(str);
		if(date==null) date = parseDate(str);
		return date;
	}
	public static final Date parseDateTme(String str){
		try {
			return sDateTimeFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static final Date parseDate(String str){
		try {
			return sDateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final String toDateString(Date date){
		try {
			return sDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static final String toDateTimeString(Date date){
		try {
			return sDateTimeFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


//	 public static boolean containsZenkaku ( String str ){
//
//		 Pattern ptn = Pattern.compile("[^-~｡-ﾟ]");
//		 Matcher mat = ptn.matcher(str);
//		 if(mat.matches()) return true;
//		 return false;
//
//		 // Regular expression.
//		 //return str.matches("[^-~｡-ﾟ]");
//	 }


	 public static boolean isEisuuzi( String str ){
		// ^[0-9a-zA-Z]*$
		return str.matches("^[0-9a-zA-Z]*$") ;
	 }

//	 public static boolean isEisuuziAnd( String str ){
//		// ^[0-9a-zA-Z]*$
//		return str.matches("^[0-9a-zA-Z^-~｡-ﾟ]*$") ;
//	 }

		public static boolean isOk_SDK(int sdk_int){
			return android.os.Build.VERSION.SDK_INT >= sdk_int;
		}



	public static String getRawText(Context context,int id){
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new  BufferedReader( new InputStreamReader( context.getResources().openRawResource(id) ));

		try {
			String line;
			while((line =r.readLine())!=null){
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	public static JSONObject getRawTextJson(Context context,int id){
		String data = getRawText(context, id);
		if(TextUtils.isEmpty(data)) return null;
		try {
			JSONObject json = new JSONObject(data);
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}


	
	public static boolean use_proxy = false;
	public static String proxy_host = "ccproxy.kanagawa-it.ac.jp";
	public static int proxy_port = 10080;
	
	
	public static JSONObject postDataReturnJson(String sUrl, ArrayList<NameValuePair> params){
		String data = postData(sUrl, params);
		if(TextUtils.isEmpty(data)) return null;
		try {
			JSONObject json = new JSONObject(data);
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String postData(String sUrl, ArrayList<NameValuePair> params) {

		HttpClient httpClient = new DefaultHttpClient();
		String sReturn = null;

		try {

			HttpPost httpPost = new HttpPost(sUrl);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpParams httpparams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpparams, 1000*3);
			HttpConnectionParams.setSoTimeout(httpparams, 1000*10);
			
			if(use_proxy){
				HttpHost proxy = new HttpHost(proxy_host, proxy_port);
				httpparams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}

			if(true){
				HttpResponse objResponse = httpClient.execute(httpPost);

				if (objResponse.getStatusLine().getStatusCode() < 400){
					InputStream objStream = objResponse.getEntity().getContent();
					InputStreamReader objReader = new InputStreamReader(objStream);
					BufferedReader objBuf = new BufferedReader(objReader);
					StringBuilder objJson = new StringBuilder();
					String sLine;
					while((sLine = objBuf.readLine()) != null){
						objJson.append(sLine);
					}
					sReturn = objJson.toString();
					objStream.close();
				}
			}
//			else{
//				httpClient.execute(httpPost);
//				return null;
//			}
		} catch (IOException e) {
			return null;
		}
			return sReturn;
		}

		public static String getData(String sUrl) {
			HttpClient objHttp = new DefaultHttpClient();
			HttpParams params = objHttp.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 1000*3); //接続のタイムアウト
			HttpConnectionParams.setSoTimeout(params, 1000*10); //データ取得のタイムアウト

			if(use_proxy){
				HttpHost proxy = new HttpHost(proxy_host, proxy_port);
				params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			
			String sReturn = null;
		try {

			HttpGet objGet = new HttpGet(sUrl);
			HttpResponse objResponse = objHttp.execute(objGet);
			if (objResponse.getStatusLine().getStatusCode() < 400){
				InputStream objStream = objResponse.getEntity().getContent();

				InputStreamReader objReader = new InputStreamReader(objStream);
				BufferedReader objBuf = new BufferedReader(objReader);
				StringBuilder objJson = new StringBuilder();
				String sLine;

				while((sLine = objBuf.readLine()) != null){
					objJson.append(sLine);
				}
				sReturn = objJson.toString();
				objStream.close();
			}
		} catch (IOException e) {
			return null;
		}
		return sReturn;
	}

	public static Bitmap getImage(String sUrl, int connect_time, int so_time){
		byte[] data =getBytes(sUrl, connect_time, so_time);
		if(data==null) return null;
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		return bmp;
	}

	public static byte[] getBytes(String sUrl, int connect_time, int so_time) {

			HttpClient objHttp = new DefaultHttpClient();
			HttpParams params = objHttp.getParams();
			HttpConnectionParams.setConnectionTimeout(params, connect_time); //接続のタイムアウト
			HttpConnectionParams.setSoTimeout(params, so_time); //データ取得のタイムアウト

			if(use_proxy){
				HttpHost proxy = new HttpHost(proxy_host, proxy_port);
				params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}

			byte[] sReturn = null;
		try {

			HttpGet objGet = new HttpGet(sUrl);
			HttpResponse objResponse = objHttp.execute(objGet);
			if (objResponse.getStatusLine().getStatusCode() < 400){
				InputStream objStream = objResponse.getEntity().getContent();
				ByteArrayOutputStream os = new ByteArrayOutputStream();

				int len;
				byte[] buf = new byte[1024];
				while((len=objStream.read(buf)) != -1){
					os.write(buf, 0, len);
				}
				sReturn = os.toByteArray();

				objStream.close();
			}
		} catch (IOException e) {
			return null;
		}
		return sReturn;
	}



}
