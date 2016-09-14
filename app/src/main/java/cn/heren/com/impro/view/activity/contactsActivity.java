package cn.heren.com.impro.view.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.model.FriendProfile;
import cn.heren.com.impro.model.FriendshipInfo;
import cn.heren.com.impro.view.adapter.ExpandGroupListAdapter;

/**
 * 所有朋友
 */
public class ContactsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_conn_search;
    private TextView tv_conn_add;
    private RelativeLayout rl_conn_back_icon;
    private RelativeLayout ll_conn_search;
    private View headerView;
    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView =null;
    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_contacts);
        setTitleBarIsShow(false);
    }

    @Override
    protected void findId() {
        mGroupListView = (ExpandableListView) findViewById(R.id.groupList);
        rl_conn_back_icon = (RelativeLayout) findViewById(R.id.rl_conn_back_icon);
        tv_conn_add = (TextView)findViewById(R.id.tv_conn_add);
       // iv_conn_search = (ImageView)findViewById(R.id.iv_conn_search);
        headerView = LayoutInflater.from(this).inflate(R.layout.item_header_connect, null);
        ll_conn_search = (RelativeLayout) headerView.findViewById(R.id.ll_conn_search);
    }

    @Override
    protected void addOnListener() {
        tv_conn_add.setOnClickListener(this);
       // iv_conn_search.setOnClickListener(this);
        rl_conn_back_icon.setOnClickListener(this);
        ll_conn_search.setOnClickListener(this);
    }

    @Override
    protected void processDatas() {
        final Map<String, List<FriendProfile>> friends = FriendshipInfo.getInstance().getFriends();

        mGroupListView.addHeaderView(headerView);
        mGroupListAdapter = new ExpandGroupListAdapter(this, FriendshipInfo.getInstance().getGroups(), friends);
        mGroupListView.setAdapter(mGroupListAdapter);
        mGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                friends.get(FriendshipInfo.getInstance().getGroups().get(groupPosition)).get(childPosition).onClick(ContactsActivity.this);
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
            case R.id.tv_conn_add: //管理分组
                startActivity(new Intent(this,FriendGroupManageActivity.class));
                break;
            case R.id.ll_conn_search: //搜素好友
            startActivity(new Intent(this,SearchFriendActivity.class));
                break;
            case R.id.rl_conn_back_icon: //finish
                finish();
                break;
        }
    }

}
