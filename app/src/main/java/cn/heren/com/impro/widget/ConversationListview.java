package cn.heren.com.impro.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 用于会话的ListView
 * Created by Administrator on 2016/9/9.
 */
public class ConversationListview extends ListView {

    public ConversationListview(Context context) {
        super(context);
    }

    public ConversationListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConversationListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
