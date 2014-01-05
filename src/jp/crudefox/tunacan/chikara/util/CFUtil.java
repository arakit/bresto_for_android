package jp.crudefox.tunacan.chikara.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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




	public static class HttpResponseData{
		public List<Cookie> cookies;
		public String data;
	}





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
		HttpResponseData hrd = postData(sUrl, params, null);
		if(hrd==null) return null;
		return hrd.data;
	}

	public static HttpResponseData postData(String sUrl, ArrayList<NameValuePair> params, List<Cookie> src_cookies) {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		//String sReturn = null;
		HttpResponseData result = null;

		try {
			CookieStore cs = httpClient.getCookieStore();
			if(src_cookies!=null){
				for(int i=0;i<src_cookies.size();i++){
					Cookie c = src_cookies.get(i);
					cs.addCookie(c);
				}
			}
			HttpContext httpContext = new BasicHttpContext();
			httpContext.setAttribute(ClientContext.COOKIE_STORE, cs);


			HttpPost httpPost = new HttpPost(sUrl);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));


			HttpParams httpparams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpparams, 1000*3);
			HttpConnectionParams.setSoTimeout(httpparams, 1000*10);


			if(true){
				HttpResponse objResponse = httpClient.execute(httpPost, httpContext);

			    // Cookieの取得.
			    List<Cookie> cookies = httpClient.getCookieStore().getCookies();
			    System.err.println("===== " + cookies.size() + " Cookies =====");
			    for (int i = 0; i < cookies.size(); i++) {
					StringBuffer sb = new StringBuffer();
					Cookie c = cookies.get(i);
					sb.append(""+i);
					sb.append("/");
					sb.append(c);
					sb.append(" / ");
					sb.append(c.getName());
					sb.append(" / ");
					sb.append(c.getValue());
					sb.append(" / ");
					sb.append(c.getDomain());
					sb.append(" / ");
					sb.append(c.getVersion());
					sb.append(" / ");
					sb.append(c.getExpiryDate());
					sb.append(" / ");
					sb.append(c.getPath());
					sb.append(" / ");
					sb.append(c.getComment());
					sb.append("\n");
					System.err.println(sb.toString());
			    }


				if (objResponse.getStatusLine().getStatusCode() < 400){
					InputStream objStream = objResponse.getEntity().getContent();
					InputStreamReader objReader = new InputStreamReader(objStream);
					BufferedReader objBuf = new BufferedReader(objReader);
					StringBuilder objJson = new StringBuilder();
					String sLine;
					while((sLine = objBuf.readLine()) != null){
						objJson.append(sLine);
					}
					String s_ret = objJson.toString();
					objStream.close();

					if(s_ret!=null){
						result = new HttpResponseData();
						result.data = s_ret;
						result.cookies = cookies;
					}
				}
			}
//			else{
//				httpClient.execute(httpPost);
//				return null;
//			}
		} catch (IOException e) {
			return null;
		}
			return result;
		}

		public static String getData(String sUrl) {

			HttpClient objHttp = new DefaultHttpClient();
			HttpParams params = objHttp.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 1000*3); //接続のタイムアウト
			HttpConnectionParams.setSoTimeout(params, 1000*10); //データ取得のタイムアウト
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
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = 8;
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
		return bmp;
	}

	public static Bitmap getImageToFile(String sUrl, int connect_time, int so_time, File file, int max_width, int max_height){
		if( !getDataToFile(sUrl, connect_time, so_time, file) ){
			return null;
		}
		return createBitmapByMax(file, max_width, max_height);
	}


	public static byte[] getBytes(String sUrl, int connect_time, int so_time) {

			HttpClient objHttp = new DefaultHttpClient();
			HttpParams params = objHttp.getParams();
			HttpConnectionParams.setConnectionTimeout(params, connect_time); //接続のタイムアウト
			HttpConnectionParams.setSoTimeout(params, so_time); //データ取得のタイムアウト
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
		} catch (Exception e) {
			return null;
		}
		return sReturn;
	}

	public static boolean getDataToFile(String sUrl, int connect_time, int so_time, File file) {

		HttpClient objHttp = new DefaultHttpClient();
		HttpParams params = objHttp.getParams();
		HttpConnectionParams.setConnectionTimeout(params, connect_time); //接続のタイムアウト
		HttpConnectionParams.setSoTimeout(params, so_time); //データ取得のタイムアウト
		boolean sReturn = false;
		try {
			BufferedOutputStream os = new BufferedOutputStream( new FileOutputStream(file) );

			HttpGet objGet = new HttpGet(sUrl);
			HttpResponse objResponse = objHttp.execute(objGet);
			if (objResponse.getStatusLine().getStatusCode() < 400){
				InputStream objStream = objResponse.getEntity().getContent();

				int len;
				byte[] buf = new byte[1024];
				while((len=objStream.read(buf)) != -1){
					os.write(buf, 0, len);
				}

				objStream.close();
			}
			os.close();

			sReturn = true;
		} catch (Exception e) {
			return false;
		}
		return sReturn;
	}



	public static HttpResponseData postMultiPartData(String sUrl, ArrayList<FormBodyPart> params, List<Cookie> src_cookies) {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponseData result=null;

		try {

			CookieStore cs = httpClient.getCookieStore();
			if(src_cookies!=null){
				for(int i=0;i<src_cookies.size();i++){
					Cookie c = src_cookies.get(i);
					cs.addCookie(c);
				}
			}
			HttpContext httpContext = new BasicHttpContext();
			httpContext.setAttribute(ClientContext.COOKIE_STORE, cs);

			HttpPost httpPost = new HttpPost(sUrl);

			//MultipartEntity entry = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "bjbjbhjbhjbjb", Charset.forName("utf-8"));
			MultipartEntity entry = new MultipartEntity();

			for(int i=0;i<params.size();i++){
				FormBodyPart bp = params.get(i);
				entry.addPart(bp);
			}

			httpPost.setEntity(entry);

			HttpParams httpparams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpparams, 1000*3);
			HttpConnectionParams.setSoTimeout(httpparams, 1000*10);


			if(true){
				HttpResponse objResponse = httpClient.execute(httpPost, httpContext);

			    // Cookieの取得.
			    List<Cookie> cookies = httpClient.getCookieStore().getCookies();

				if (objResponse.getStatusLine().getStatusCode() < 400){
					InputStream objStream = objResponse.getEntity().getContent();
					InputStreamReader objReader = new InputStreamReader(objStream);
					BufferedReader objBuf = new BufferedReader(objReader);
					StringBuilder objJson = new StringBuilder();
					String sLine;
					while((sLine = objBuf.readLine()) != null){
						objJson.append(sLine);
					}
					String s_ret = objJson.toString();
					objStream.close();

					if(s_ret!=null){
						result = new HttpResponseData();
						result.cookies = cookies;
						result.data = s_ret;
					}
				}
			}
//			else{
//				httpClient.execute(httpPost);
//				return null;
//			}
		} catch (IOException e) {
			return null;
		}
			return result;
		}































 	public static BitmapFactory.Options calcBitmapOption(InputStream is,int maxWidth,int maxHeight){
    	if(is!=null){
    		try {
    			BitmapFactory.Options opt = new BitmapFactory.Options();
    		    opt.inJustDecodeBounds = true;
    		    BitmapFactory.decodeStream(is, null, opt);
    		    is.close();
    		    float scale = Math.min((float)maxWidth / (float)opt.outWidth, (float)maxHeight / (float)opt.outHeight);
    		    opt.inJustDecodeBounds = false;
    		    opt.inPurgeable = true;
    		    if(scale<1.0f){
    		    	float bai = 1.0f / scale;
    		    	int i;
    		    	for(i=2;((float)i)<bai;i*=2);
    		    	opt.inSampleSize = i;
    		    }
    		    return opt;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return null;
	}

 	public static Bitmap createBitmapByMax(Context context,Uri uri,int maxWidth,int maxHeight){
		Bitmap bmp = null;
    	if(uri!=null){
    		try {
    			Context c = context.getApplicationContext();
    			ContentResolver cr = c.getContentResolver();
    			InputStream is = cr.openInputStream(uri);
    			if(is!=null){
	    			BitmapFactory.Options opt = calcBitmapOption(is, maxWidth, maxHeight);
	    		    is.close();
	    		    //もう一度
	    			if(opt!=null){
	    				is = cr.openInputStream(uri);
	    				if(is!=null){
		    				bmp = BitmapFactory.decodeStream(is, null, opt);
		        			is.close();
	    				}
	    			}
    			}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return bmp;
	}
 	public static Bitmap createBitmapByMax(File file,int maxWidth,int maxHeight){
		Bitmap bmp = null;
    	if(file!=null && file.exists() && file.isFile()){
    		try {
    			InputStream is = new FileInputStream(file);
    			BitmapFactory.Options opt = calcBitmapOption(is, maxWidth, maxHeight);
    		    is.close();
    		    //もう一度
    			if(opt!=null){
    				is = new FileInputStream(file);
    				bmp = BitmapFactory.decodeStream(is, null, opt);
    				is.close();
    			}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}

    	}
    	return bmp;
	}
 	public static BitmapDrawable createBitmapDrawableByMax(File file,int maxWidth,int maxHeight){
 		Bitmap bmp = createBitmapByMax(file, maxWidth, maxHeight);
 		if(bmp!=null){
 			return new BitmapDrawable(bmp);
 		}
 		return null;
 	}



}
