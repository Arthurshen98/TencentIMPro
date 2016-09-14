package cn.heren.com.impro.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.heren.com.impro.R;
import cn.heren.com.impro.view.activity.FriendGroupManageActivity;

/**
 * 分组管理
 * Created by Administrator on 2016/9/10.
 */
public class FriendGroupManageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mGroupList;
    private FriendGroupManageActivity manageActivity;

    public FriendGroupManageAdapter(Context context, List<String> grouplist,FriendGroupManageActivity activity) {
        mContext = context;
        mGroupList = grouplist;
        manageActivity = activity;
    }

    @Override
    public int getCount() {
        return mGroupList.size();
    }

    @Override
    public Object getItem(int i) {
        return mGroupList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int groupPosition, View convertView, ViewGroup viewGroup) {
        ItemHolder Holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friend_gm, viewGroup,false);
            Holder = new ItemHolder();
            Holder.groupname = (TextView) convertView.findViewById(R.id.groupName);
            //Holder.delete = (ImageView) convertView.findViewById(R.id.delete_group);
            Holder.editName = (TextView) convertView.findViewById(R.id.tv_fgm_edit);

            convertView.setTag(Holder);
        }else {
            Holder = (ItemHolder) convertView.getTag();
        }



        if(mGroupList.get(groupPosition).equals("")){
            Holder.groupname.setText(mContext.getResources().getString(R.string.default_group_name));
           // Holder.delete.setVisibility(View.INVISIBLE);
        }else{
            Holder.groupname.setText(mGroupList.get(groupPosition));
          //  Holder.delete.setVisibility(View.VISIBLE);
        }

        /*Holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageActivity.deleteGroup(groupPosition);
            }
        });*/

        Holder.editName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                manageActivity.editGroupName(groupPosition);
            }
        });

        return convertView;
    }

    class ItemHolder {
        public TextView groupname,editName;
        public ImageView delete;
    }

}
