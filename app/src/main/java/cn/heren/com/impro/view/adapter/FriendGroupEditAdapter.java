package cn.heren.com.impro.view.adapter;

import android.content.Context;

import java.util.List;
import java.util.Observer;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.CommonAdapter;
import cn.heren.com.impro.base.ViewHolder;
import cn.heren.com.impro.model.FriendGroupInfo;
import cn.heren.com.impro.model.FriendProfile;

/**
 * 组资料
 * Created by Administrator on 2016/9/11.
 */
public class FriendGroupEditAdapter extends CommonAdapter<String> {

    public FriendGroupEditAdapter(Context context, List<String> data) {
        super(context, data, R.layout.item_fge);
    }

    @Override
    public void convert(ViewHolder holder, String info) {

        holder.setText(R.id.tv_fge_name, info.toString());
    }
}
