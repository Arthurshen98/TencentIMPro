package cn.heren.com.impro.widget;

import android.content.Context;
import android.util.AttributeSet;

import cn.heren.com.ui.SwipeMenuListView;

/**
 * 左滑删除listView item
 * Created by Administrator on 2016/9/12.
 */
public class SwipeListView extends SwipeMenuListView {
    public SwipeListView(Context context) {
        super(context);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
