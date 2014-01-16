package jp.crudefox.ricoh.bresto.chikara.manager;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;


/**
 * 		@author Chikara Funabashi
 * 		@date 2013/07/06
 *
 */


public class MapManager {



	public static class Keyword implements Serializable{
		public int kid = -1;
		public String keyword = null;
		public int x,y,w,h;
		public int good;
	}

	public static class KeywordRelation implements Serializable{
		public int kid1 = -1;
		public int kid2 = -1;
		public Date modified;
	}



	//private static String LOGIN_URL = CFConst.SERVER + CFConst.LOGIN;
	//private static String SUGGESTED__URL = CFConst.SERVER + CFConst.SUGGEST_KEYWORDS;
	//private static String KEYWORDS_URL = CFConst.SERVER + CFConst.KEYWORDS;
	//private static String CONNECT_KEYWORD_URL = CFConst.SERVER + CFConst.CONNECT_KEYWORD;
	//private static String KEYWORDS_RELATIONS_URL = CFConst.SERVER + CFConst.KEYWORDS_RELATIONS;

	public static String SOCKET_NODE_EDGE_URL = CFConst.SERVER_WS + CFConst.SOCKET_NODE_EDGE;


	private Context mContext;
	//private String mProjectId;



	final public HashMap<Integer, Keyword> mNodes = new HashMap<Integer, Keyword>();
	final public ArrayList<KeywordRelation> mEdges = new ArrayList<MapManager.KeywordRelation>();


	public MapManager(Context context){
		mContext = context;
		//mProjectId = project_id;
	}



	public Keyword getNodeById(int kid){
		return mNodes.get(kid);
	}
	public Keyword[] getNodesArray(){
		Keyword[] arr = new Keyword[mNodes.size()];
		int i=0;
		for(Entry<Integer, Keyword> e : mNodes.entrySet()){
			arr[i++] = e.getValue();
		}
		return arr;
	}

	public KeywordRelation getEdge(int index){
		return mEdges.get(index);
	}
	public KeywordRelation[] getEdgeArray(){
		KeywordRelation[] arr = new KeywordRelation[mEdges.size()];
		int i=0;
		for(KeywordRelation e : mEdges){
			arr[i++] = e;
		}
		return arr;
	}



	public synchronized void parseJson(String str){

		try {
			JSONObject json = new JSONObject(str);

			String type = json.getString("type");
			if(type.equals("node_edge")){
				JSONObject jdata = json.getJSONObject("data");
				{
					JSONObject jnodes = jdata.getJSONObject("node");
					JSONArray jedges = jdata.getJSONArray("edge");

					for(Iterator<String> i = jnodes.keys(); i.hasNext(); ){
						JSONObject jnode = jnodes.getJSONObject(i.next());
						String kid = jnode.getString("kid");
						String keyword = jnode.getString("keyword");
						int x = jnode.getInt("x");
						int y = jnode.getInt("y");
						int w = jnode.getInt("w");
						int h = jnode.getInt("h");
						int good = jnode.getInt("good");

						Keyword kw = new Keyword();
						kw.kid = Integer.parseInt(kid);
						kw.keyword = keyword;
						kw.x = x;
						kw.y = y;
						kw.w = w;
						kw.h = h;
						kw.good = good;

						mNodes.put(kw.kid, kw);
					}

					for(int i=0;i<jedges.length();i++){
						JSONObject jedge = jedges.getJSONObject(i);
						int from_kid = jedge.getInt("from_kid");
						int to_kid = jedge.getInt("to_kid");

						KeywordRelation kr = new KeywordRelation();
						kr.kid1 = from_kid;
						kr.kid2 = to_kid;

						boolean has_edge = false;
						for(KeywordRelation e : mEdges){
							if( e.kid1 == from_kid && e.kid2 == to_kid ){
								has_edge = true;
								break;
							}
						}

						if(!has_edge) mEdges.add(kr);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


//
//	//キーワード候補を取得します。
//	public List<String> suggestKeywords(String keyword){
//
//		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//
//		//params.add(new BasicNameValuePair("sid", li.sid.sid));
//		params.add(new BasicNameValuePair("keyword", keyword));
//
//
//
//		String data = null;
//
//		data = CFUtil.postData(SUGGESTED__URL, params);
//
//		if(data==null){
//			return null;
//		}
//
//
//		try {
//			JSONObject json = new JSONObject(data);
//
//			//String result = json.getString("result");
//
//			JSONArray jDataList = json.getJSONArray("data_list");
//
//			List<String> result = new ArrayList<String>();
//
//			for(int i=0;i<jDataList.length();i++){
//				JSONObject ji = jDataList.getJSONObject(i);
//				String sug = ji.getString("data");
//
//				result.add(sug);
//			}
//
//			return result;
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return null;
//		}
//		 catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}


//	//
//	public List<Keyword> getKeywords(LoginInfo li){
//
//		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//
//		params.add(new BasicNameValuePair("sid", li.sid.sid));
//		params.add(new BasicNameValuePair("project_id", mProjectId));
//		//params.add(new BasicNameValuePair("keyword", keyword));
//
//
//		String data = null;
//
//		data = CFUtil.postData(KEYWORDS_URL, params);
//
//		if(data==null){
//			return null;
//		}
//
//
//		try {
//			JSONObject json = new JSONObject(data);
//
//			//String result = json.getString("result");
//
//			JSONArray jDataList = json.getJSONArray("data_list");
//
//			List<Keyword> result = new ArrayList<Keyword>();
//
//			for(int i=0;i<jDataList.length();i++){
//				JSONObject ji = jDataList.getJSONObject(i);
//				Keyword kw = new Keyword();
//
//				kw.kid = ji.getInt("kid");
//				kw.keyword = ji.getString("keyword");
//
//				result.add(kw);
//			}
//
//			return result;
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return null;
//		}
//		 catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//
//	public List<KeywordRelation> getKeywordsRelations(LoginInfo li,Date start_time){
//
//		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//
//		params.add(new BasicNameValuePair("sid", li.sid.sid));
//		params.add(new BasicNameValuePair("project_id", mProjectId));
//		params.add(new BasicNameValuePair("start_time", CFUtil.toDateTimeString(start_time) ));
//
//
//		String data = null;
//
//		data = CFUtil.postData(KEYWORDS_RELATIONS_URL, params);
//
//		if(data==null){
//			return null;
//		}
//
//
//		try {
//			JSONObject json = new JSONObject(data);
//
//			//String result = json.getString("result");
//
//			JSONArray jDataList = json.getJSONArray("data_list");
//
//			List<KeywordRelation> result = new ArrayList<KeywordRelation>();
//
//			for(int i=0;i<jDataList.length();i++){
//				JSONObject ji = jDataList.getJSONObject(i);
//				KeywordRelation kr = new KeywordRelation();
//
//				kr.kid1 = ji.getInt("kid1");
//				kr.kid2 = ji.getInt("kid2");
//				kr.modified = CFUtil.parseDateTme( ji.getString("modified_time") );
//
//				result.add(kr);
//			}
//
//			return result;
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return null;
//		}
//		 catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//
//
//	//
//	public List<Keyword> connectKeyword(LoginInfo li, Keyword k1, String keyword){
//
//		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//
//		params.add(new BasicNameValuePair("sid", li.sid.sid));
//		params.add(new BasicNameValuePair("project_id", mProjectId));
//		params.add(new BasicNameValuePair("from_kid", ""+k1.kid));
//		params.add(new BasicNameValuePair("to_keyword", keyword));
//
//
//		String data = null;
//
//		data = CFUtil.postData(CONNECT_KEYWORD_URL, params);
//
//		if(data==null){
//			return null;
//		}
//
//
//		try {
//			JSONObject json = new JSONObject(data);
//
//			String result = json.getString("result");
//			//JSONArray jDataList = json.getJSONArray("data_list");
//
//			if( !"OK".equalsIgnoreCase(result) ) throw new Exception();
//
//			List<Keyword> ret = new ArrayList<Keyword>();
//
//			for(int i=0;i<2;i++){
//				Keyword kw = new Keyword();
//
//				kw.kid = json.getInt("kid"+(i+1));
//				kw.keyword = json.getString("keyword"+(i+1));
//
//				ret.add(kw);
//			}
//
//			return ret;
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return null;
//		}
//		 catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}







	public static void log(String str){
		android.util.Log.d("test", str);
	}





}
