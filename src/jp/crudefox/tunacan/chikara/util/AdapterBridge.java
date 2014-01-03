package jp.crudefox.tunacan.chikara.util;

import java.util.ArrayList;


public class AdapterBridge<T>{
	
	ArrayList<Data> mList = new ArrayList<AdapterBridge<T>.Data>();
	
	public AdapterBridge() {
		super();
	}

	public void addItem(String text,T value){
		mList.add(new Data(text, value));
	}
	public int getItemPosition(T item){
		for(int i=0;i<mList.size();i++){
			if(mList.get(i).value.equals(item)){
				return i;
			}
		}
		return -1;
	}
	
	public void clearItems(){
		mList.clear();
	}


	public int getCount() {
		return mList.size();
	}

	public T getItem(int position) {
		return mList.get(position).value;
	}
	
	public String[] getItemTextArray(){
		String[] arr = new String[mList.size()];
		for(int i=0;i<arr.length;i++){
			arr[i] = mList.get(i).text;
		}
		return arr;
	}

	
	class Data{
		String text;
		T value;
		public Data(String text,T value){
			this.text = text;
			this.value = value;
		}
	}
	
}
