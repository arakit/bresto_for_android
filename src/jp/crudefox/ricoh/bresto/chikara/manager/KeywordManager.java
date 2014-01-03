package jp.crudefox.ricoh.bresto.chikara.manager;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.crudefox.tunacan.chikara.util.CFUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class KeywordManager {



	/**
	 * 		@author Chikara Funabashi
	 * 		@date 2013/07/06
	 *
	 */


	public static class Keyword implements Serializable{
		public int kid = -1;
		public String keyword = null;
	}

	public static class KeywordRelation implements Serializable{
		public int kid1 = -1;
		public int kid2 = -1;
		public Date modified;
	}



	private static String LOGIN_URL = CFConst.SERVER + CFConst.LOGIN;
	private static String SUGGESTED__URL = CFConst.SERVER + CFConst.SUGGEST_KEYWORDS;
	private static String KEYWORDS_URL = CFConst.SERVER + CFConst.KEYWORDS;
	private static String CONNECT_KEYWORD_URL = CFConst.SERVER + CFConst.CONNECT_KEYWORD;
	private static String KEYWORDS_RELATIONS_URL = CFConst.SERVER + CFConst.KEYWORDS_RELATIONS;


	private Context mContext;
	private String mProjectId;



	public KeywordManager(Context context, String project_id){
		mContext = context;
		mProjectId = project_id;
	}





	//キーワード候補を取得します。
	public List<String> suggestKeywords(String keyword){

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		//params.add(new BasicNameValuePair("sid", li.sid.sid));
		params.add(new BasicNameValuePair("keyword", keyword));



		String data = null;

		data = CFUtil.postData(SUGGESTED__URL, params);

		if(data==null){
			return null;
		}


		try {
			JSONObject json = new JSONObject(data);

			//String result = json.getString("result");

			JSONArray jDataList = json.getJSONArray("data_list");

			List<String> result = new ArrayList<String>();

			for(int i=0;i<jDataList.length();i++){
				JSONObject ji = jDataList.getJSONObject(i);
				String sug = ji.getString("data");

				result.add(sug);
			}

			return result;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		 catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	//
	public List<Keyword> getKeywords(LoginInfo li){

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("sid", li.sid.sid));
		params.add(new BasicNameValuePair("project_id", mProjectId));
		//params.add(new BasicNameValuePair("keyword", keyword));


		String data = null;

		data = CFUtil.postData(KEYWORDS_URL, params);

		if(data==null){
			return null;
		}


		try {
			JSONObject json = new JSONObject(data);

			//String result = json.getString("result");

			JSONArray jDataList = json.getJSONArray("data_list");

			List<Keyword> result = new ArrayList<Keyword>();

			for(int i=0;i<jDataList.length();i++){
				JSONObject ji = jDataList.getJSONObject(i);
				Keyword kw = new Keyword();

				kw.kid = ji.getInt("kid");
				kw.keyword = ji.getString("keyword");

				result.add(kw);
			}

			return result;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		 catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	public List<KeywordRelation> getKeywordsRelations(LoginInfo li,Date start_time){

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("sid", li.sid.sid));
		params.add(new BasicNameValuePair("project_id", mProjectId));
		params.add(new BasicNameValuePair("start_time", CFUtil.toDateTimeString(start_time) ));


		String data = null;

		data = CFUtil.postData(KEYWORDS_RELATIONS_URL, params);

		if(data==null){
			return null;
		}


		try {
			JSONObject json = new JSONObject(data);

			//String result = json.getString("result");

			JSONArray jDataList = json.getJSONArray("data_list");

			List<KeywordRelation> result = new ArrayList<KeywordRelation>();

			for(int i=0;i<jDataList.length();i++){
				JSONObject ji = jDataList.getJSONObject(i);
				KeywordRelation kr = new KeywordRelation();

				kr.kid1 = ji.getInt("kid1");
				kr.kid2 = ji.getInt("kid2");
				kr.modified = CFUtil.parseDateTme( ji.getString("modified_time") );

				result.add(kr);
			}

			return result;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		 catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}



	//
	public List<Keyword> connectKeyword(LoginInfo li, Keyword k1, String keyword){

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("sid", li.sid.sid));
		params.add(new BasicNameValuePair("project_id", mProjectId));
		params.add(new BasicNameValuePair("from_kid", ""+k1.kid));
		params.add(new BasicNameValuePair("to_keyword", keyword));


		String data = null;

		data = CFUtil.postData(CONNECT_KEYWORD_URL, params);

		if(data==null){
			return null;
		}


		try {
			JSONObject json = new JSONObject(data);

			String result = json.getString("result");
			//JSONArray jDataList = json.getJSONArray("data_list");

			if( !"OK".equalsIgnoreCase(result) ) throw new Exception();

			List<Keyword> ret = new ArrayList<Keyword>();

			for(int i=0;i<2;i++){
				Keyword kw = new Keyword();

				kw.kid = json.getInt("kid"+(i+1));
				kw.keyword = json.getString("keyword"+(i+1));

				ret.add(kw);
			}

			return ret;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		 catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}







	public static void log(String str){
		android.util.Log.d("test", str);
	}





}
