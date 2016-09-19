package cn.heren.com.impro.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.model.FriendProfile;
import cn.heren.com.impro.model.FriendshipInfo;
import cn.heren.com.impro.view.adapter.ExpandGroupListAdapter;

/**
 * 选择成员（添加组成员）（添加群成员）
 */
public class ChooseFriendActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_choose_right;
    private RelativeLayout rl_choose_back_icon;

    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView;
    private List<FriendProfile> selectList = new ArrayList<>();
    private List<String> mAlreadySelect = new ArrayList<>();
    private List<FriendProfile> alreadySelectList = new ArrayList<>();
    private  ArrayList<String> result = null;
    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_choose_friend);
        setTitleBarIsShow(false);
    }

    @Override
    protected void findId() {
        tv_choose_right = (TextView)findViewById(R.id.tv_choose_right);
        rl_choose_back_icon = (RelativeLayout) findViewById(R.id.rl_choose_back_icon);
    }

    @Override
    protected void addOnListener() {
        tv_choose_right.setOnClickListener(this);
        rl_choose_back_icon.setOnClickListener(this);
    }

    @Override
    protected void processDatas() {
        List<String> selected = getIntent().getStringArrayListExtra("MemberSelected");
        if (selected != null){
            mAlreadySelect.addAll(selected);
        }

        final Map<String, List<FriendProfile>> friends = FriendshipInfo.getInstance().getFriends();
        //处理已经被选中的、
        memberSelected();

        mGroupListView = (ExpandableListView) findViewById(R.id.groupList);
        mGroupListAdapter = new ExpandGroupListAdapter(this, FriendshipInfo.getInstance().getGroups(), friends, true);
        mGroupListView.setAdapter(mGroupListAdapter);
        mGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                FriendProfile profile = friends.get(FriendshipInfo.getInstance().getGroups().get(groupPosition)).get(childPosition);
                if (alreadySelectList.contains(profile)) return false;
                onSelect(profile);
                mGroupListAdapter.notifyDataSetChanged();
                return false;
            }
        });
        mGroupListAdapter.notifyDataSetChanged();
    }

    /**
     * 已经被选中的成员
     */
    private void memberSelected() {
        final Map<String, List<FriendProfile>> friends = FriendshipInfo.getInstance().getFriends();
        for (String id : mAlreadySelect){
            for (String key : friends.keySet()){
                for (FriendProfile profile : friends.get(key)){
                    if (id.equals(profile.getIdentify())){  //id改成name
                        profile.setIsSelected(true);
                        alreadySelectList.add(profile);
                    }
                }
            }
        }
    }

    private void onSelect(FriendProfile profile){
        if (!profile.isSelected()){
            selectList.add(profile);
        }else{
            selectList.remove(profile);
        }
        profile.setIsSelected(!profile.isSelected());
    }

    private ArrayList<String> getSelectIds(){
        result = new ArrayList<>();
        for (FriendProfile item : selectList){
            result.add(item.getIdentify());
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_choose_right: //完成，回调数据
                Intent intent = new Intent();
                intent.putStringArrayListExtra("memberSelected", getSelectIds());
                setResult(RESULT_OK, intent);
                mAlreadySelect.clear();
                finish();
                break;
            case R.id.rl_choose_back_icon:
                finish();
                break;
        }
    }
}
