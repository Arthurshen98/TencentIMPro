package cn.heren.com.impro.view.adapter;

import android.content.Context;

import java.util.List;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.CommonAdapter;
import cn.heren.com.impro.base.ViewHolder;

/**
 * Created by Administrator on 2016/9/9.
 */
public class ConversationAdapter extends CommonAdapter<Object> {
    public ConversationAdapter(Context context, List data) {
        super(context, data, R.layout.item_conversation);
    }

    @Override
    public void convert(ViewHolder holder, Object o) {
        holder.setText(R.id.name, o.toString());
    }
}
