package cn.heren.com.impro.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义的GridView
 * 
 * @author whwei
 * 
 */
public class DefineGridView extends GridView {

	public boolean hasScrollBar = true;

	public DefineGridView(Context context) {
		super(context, null);
	}

	public DefineGridView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public DefineGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = heightMeasureSpec;
		if (hasScrollBar) {
			expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);// 注意这里,这里的意思是直接测量出GridView的高度
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}
