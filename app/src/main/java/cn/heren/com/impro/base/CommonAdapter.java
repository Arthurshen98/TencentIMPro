package cn.heren.com.impro.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 对ListView通用处理的万能Adapter
 * 
 * @author whwei
 *
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mData;
	protected LayoutInflater mInflater;
	protected int layoutId;
	protected int mPosition;
	
	public CommonAdapter(Context context, List<T> data, int layoutId) {
		super();
		this.mContext = context;
		this.mData = data;
		this.mInflater = LayoutInflater.from(context);
		this.layoutId = layoutId;
	}
	
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mPosition = position;
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, position, layoutId);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}
	
	public abstract void convert(ViewHolder holder, T t);

}
