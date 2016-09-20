package cn.heren.com.impro.view.adapter;

import android.content.Context;

import java.util.List;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.CommonAdapter;
import cn.heren.com.impro.base.ViewHolder;
import cn.heren.com.impro.model.ProfileSummary;

/**
 * 添加新朋友
 * Created by Administrator on 2016/9/19.
 */
public class AddFriendAdapter extends CommonAdapter<ProfileSummary> {

    public AddFriendAdapter(Context context, List data) {
        super(context, data, R.layout.item_add_friend);
    }

    @Override
    public void convert(ViewHolder holder, ProfileSummary o) {
        holder.setText(R.id.tv_add_fir_id, o.getIdentify().toString());
    }
}
