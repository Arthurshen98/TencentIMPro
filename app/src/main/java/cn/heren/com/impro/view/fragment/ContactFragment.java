package cn.heren.com.impro.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseFragment;
import cn.heren.com.impro.model.FriendProfile;
import cn.heren.com.impro.model.FriendshipInfo;
import cn.heren.com.impro.view.activity.ConversationActivity;
import cn.heren.com.impro.view.activity.HomeActivity;
import cn.heren.com.impro.view.adapter.ExpandGroupListAdapter;

/**
 * 联系人界面
 */
public class ContactFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private TextView tv_fragment_right;
    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView =null;

    public ContactFragment() {
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_contact;
    }

    @Override
    public void findId() {
        mGroupListView = (ExpandableListView) findViewById(R.id.groupList);
        tv_fragment_right = (TextView) findViewById(R.id.tv_fragment_right);

    }

    @Override
    public void addOnListener() {
        tv_fragment_right.setOnClickListener(this);
    }

    @Override
    public void processDatas() {
        mGroupListView.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.empty_connect_list, null));// 添加空页面

        final Map<String, List<FriendProfile>> friends = FriendshipInfo.getInstance().getFriends();
        mGroupListAdapter = new ExpandGroupListAdapter(getActivity(), FriendshipInfo.getInstance().getGroups(), friends);
        mGroupListView.setAdapter(mGroupListAdapter);
        mGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                friends.get(FriendshipInfo.getInstance().getGroups().get(groupPosition)).get(childPosition).onClick(getActivity());
                return false;
            }
        });

        mGroupListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        mGroupListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fragment_right: //添加
            startActivity(new Intent(mContext, ConversationActivity.class));
                break;
        }
    }
}
