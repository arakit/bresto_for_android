package jp.crudefox.ricoh.bresto.chikara.manager;


import java.util.ArrayList;
import java.util.List;

import jp.co.ricoh.com.discovery.DiscoveryAgent;
import jp.co.ricoh.com.discovery.impl.DiscoverAgentImpl;
import android.content.Context;


/**
 * 		@author Chikara Funabashi
 * 		@date 2013/07/06
 *
 */
public class ProjectorManager {






	public static class Projector{
		public String ip;
		public String description;
	}



	private Context mContext;


	public ProjectorManager(Context context){
		mContext = context;
	}



	public List<Projector> listProjector(){

		DiscoveryAgent discover = new DiscoverAgentImpl();
		List<String> device_ip_list = discover.getDevice("hoge");

		ArrayList<Projector> results = new ArrayList<ProjectorManager.Projector>();

		log("projector list num = "+device_ip_list.size());

		for(int i=0;i<device_ip_list.size();i++){
			String pZrojector_ip =device_ip_list.get(i);

			Projector pro = new Projector();
			pro.ip = pZrojector_ip;

			log(""+i+" : "+pZrojector_ip);

			results.add(pro);
		}

		return results;
	}




	public static void log(String str){
		android.util.Log.d("test", str);
	}





}
