package jp.crudefox.tunacan.chikara.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

public abstract class CFCardUIAdapter<T> extends BaseAdapter{

	private Context mContext;

	private int mViewTypeCount = 1;

	private class ItemInfo{
		int view_type = 0;
		boolean do_card_ui_motion = true;
		T user_item;
	}

	private final ArrayList<ItemInfo> mList = new ArrayList<ItemInfo>();

	public CFCardUIAdapter(Context context) {
		super();
		mContext = context;
	}



	public final void insertItem(int index, T user_item, int type){
		if(user_item==null) throw new IllegalArgumentException("can not insert null.");
		ItemInfo info = new ItemInfo();
		info.user_item = user_item;
		info.view_type = type;
		mList.add(index, info);
		this.notifyDataSetChanged();
	}

	public final T removeItemByInex(int position){
		ItemInfo info = mList.remove(position);
		this.notifyDataSetChanged();
		return info.user_item;
	}

	public void clearItems(){
		mList.clear();
		this.notifyDataSetChanged();
	}

	
	
	
	
	
	public final void addItem(T user_item, int type){
		insertItem( mList.size(), user_item, type);
	}

	public final void removeItemByItem(T user_item){
		ItemInfo info = getItemInfoByUserItem(user_item);
		if(info==null) return ;
		int index = mList.indexOf(info);
		mList.remove(index);
	}

	private ItemInfo getItemInfoByUserItem(T user_item){
		if(user_item==null) return null;
		for(int i=0;i<mList.size();i++){
			ItemInfo info = mList.get(i);
			if( info.user_item == user_item ) return info;
		}
		return null;
	}


	@Override
	public final int getCount() {
		return mList.size();
	}

	@Override
	public final T getItem(int position) {
		return mList.get(position).user_item;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);


	public final boolean isCardMotion(int position){
		ItemInfo info = mList.get(position);
		return info.do_card_ui_motion;
	}
	public final void setCardMotion(int position, boolean motion){
		ItemInfo info = mList.get(position);
		info.do_card_ui_motion = motion;
	}

	public final void doCardMotion(View view,int anim_id){
        Animation animation = AnimationUtils.loadAnimation(mContext, anim_id);
        view.startAnimation(animation);
	}



	@Override
	public final int getItemViewType(int position) {
		return mList.get(position).view_type;
	}

	@Override
	public final int getViewTypeCount() {
		return mViewTypeCount;
	}

	public final void setViewTypeCount(int num){
		if(num<=0) throw new IllegalArgumentException("view count err");
		mViewTypeCount = num;
	}



}
