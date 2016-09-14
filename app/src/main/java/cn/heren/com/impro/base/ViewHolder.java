package cn.heren.com.impro.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.heren.com.impro.util.GridViewUtil;
import cn.heren.com.impro.widget.DefineGridView;


/**
 * 对ListView通用处理的ViewHolder
 * 
 * @author whwei
 * 
 */
public class ViewHolder {
	private Context mContext;
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.mContext = context;
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int position, int layoutId) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			// 更新position
			holder.mPosition = position;
			return holder;
		}
	}

	/**
	 * 通过viewId获取控件
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public int getPosition() {
		return mPosition;
	}

	/**
	 * 设置TextView的值
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	/**
	 * 设置Text ForegroundColorSpan的值
	 * 
	 * @param viewId
	 */
	public ViewHolder setText(int viewId, SpannableStringBuilder span) {
		TextView tv = getView(viewId);
		tv.setText(span);
		return this;
	}

	/**
	 * 设置文本颜色
	 * 
	 * @param viewId
	 * @param color
	 * @return
	 */
	public ViewHolder setTextColor(int viewId, int color) {
		TextView tv = getView(viewId);
		tv.setTextColor(color);
		return this;
	}
	
	/**
	 * 设置文本颜色，使用资源文件中的颜色
	 * 
	 * @param viewId 指定控件的id
	 * @param resColorId 资源文件中color的id
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public ViewHolder setTextColorRes(int viewId, int resColorId) {
		TextView tv = getView(viewId);
		tv.setTextColor(mContext.getResources().getColor(resColorId));
		return this;
	}

	/**
	 * 给对象设置Tag
	 * 
	 * @param viewId
	 * @param key
	 * @param tag
	 * @return
	 */
	public ViewHolder setTag(int viewId, int key, Object tag) {
		View view = getView(viewId);
		view.setTag(key, tag);
		return this;
	}

	/**
	 * 给View对象设置Tag
	 * 
	 * @param viewId
	 * @param tag
	 * @return
	 */
	public ViewHolder setTag(int viewId, Object tag) {
		View view = getView(viewId);
		view.setTag(tag);
		return this;
	}

	/**
	 * 给控件设置点击事件的监听器
	 * 
	 * @return
	 */
	public ViewHolder setOnClickListener(int viewId, OnClickListener listener) {
		View view = getView(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	/**
	 * 设置TextView的Drawable
	 * 
	 * @param viewId
	 * @param location
	 *            图片位置 left top right down
	 * @param ImageId
	 */
	@SuppressWarnings("deprecation")
	public ViewHolder setTextDrawable(int viewId, String location, int ImageId) {
		TextView tv = getView(viewId);
		if (location != null && location.length() != 0) {
			Drawable img = mContext.getResources().getDrawable(ImageId);
			img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
			if ("left".equals(location)) {
				tv.setCompoundDrawables(img, null, null, null);
			} else if ("top".equals(location)) {
				tv.setCompoundDrawables(null, img, null, null);
			} else if ("right".equals(location)) {
				tv.setCompoundDrawables(null, null, img, null);
			} else if ("down".equals(location)) {
				tv.setCompoundDrawables(null, null, null, img);
			}
		} else {
			tv.setCompoundDrawables(null, null, null, null);
		}
		return this;
	}

	/**
	 * 设置ImageView网络图片
	 * 
	 * @param viewId
	 * @param url
	 * @param options
	 */
	/*public ViewHolder setImageView(int viewId, String url,
			DisplayImageOptions options) {
		ImageView iv = getView(viewId);
		ImageLoader.getInstance().displayImage(url, iv, options);
		return this;
	}*/

	/**
	 * 设置ImageView bitmap图片
	 * 
	 * @param viewId
	 * @param bitmap
	 */
	public ViewHolder setImageView(int viewId, Bitmap bitmap) {
		ImageView iv = getView(viewId);
		iv.setImageBitmap(bitmap);
		return this;
	}

	/**
	 * 设置ImageView本地图片
	 * 
	 * @param viewId
	 */
	@SuppressWarnings("deprecation")
	public ViewHolder setImageView(int viewId, int imageId) {
		ImageView iv = getView(viewId);
		iv.setImageDrawable(mContext.getResources().getDrawable(imageId));
		return this;
	}

	/**
	 * 设置九宫格
	 * 
	 * @param viewId
	 * @param baseAdapter
	 */
	public ViewHolder setGridViewAdapter(int viewId, BaseAdapter baseAdapter) {
		GridView gv = getView(viewId);
		gv.setAdapter(baseAdapter);
		return this;
	}

	public ViewHolder updateGridViewLayoutParameters(int viewId, int colNum) {
		DefineGridView dgv = getView(viewId);
		GridViewUtil.updateGridViewLayoutParams(dgv, colNum);
		return this;
	}

	/**
	 * 设置控件的Clickable
	 * 
	 * @param viewId
	 * @param clickable
	 * @return
	 */
	public ViewHolder setClickable(int viewId, boolean clickable) {
		View view = getView(viewId);
		view.setClickable(clickable);
		return this;
	}

	/**
	 * 设置控件的Enable
	 * 
	 * @param viewId
	 * @param isEnable
	 * @return
	 */
	public ViewHolder setEnable(int viewId, boolean isEnable) {
		View view = getView(viewId);
		view.setEnabled(isEnable);
		return this;
	}

	/**
	 * 设置控件的Pressed
	 * 
	 * @param viewId
	 * @param pressed
	 * @return
	 */
	public ViewHolder setPressed(int viewId, boolean pressed) {
		View view = getView(viewId);
		view.setPressed(pressed);
		return this;
	}
	
	/**
	 * 设置控件的Selected
	 * @param viewId
	 * @param selected
	 * @return
	 */
	public ViewHolder setSelected(int viewId, boolean selected) {
		View view = getView(viewId);
		view.setSelected(selected);
		return this;
	}

	/**
	 * 设置布局背景
	 * 
	 * @param viewId
	 * @param resid
	 */
	public ViewHolder setBackground(int viewId, int resid) {
		View view = getView(viewId);
		view.setBackgroundResource(resid);
		return this;
	}

	/**
	 * 设置控件是否隐藏
	 * 
	 */
	public ViewHolder setVisibility(int viewId, boolean visible) {
		View view = getView(viewId);
		if (visible) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
		return this;
	}

	/**
	 * 设置控件是否隐藏
	 */
	public ViewHolder setVisibility(int viewId, int visibility) {
		View view = getView(viewId);
		if (visibility == View.VISIBLE || visibility == View.INVISIBLE || visibility == View.GONE) {
			view.setVisibility(visibility);
		}
		return this;
	}

}
