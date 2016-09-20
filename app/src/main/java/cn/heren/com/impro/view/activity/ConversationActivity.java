package cn.heren.com.impro.view.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.view.adapter.ConversationAdapter;
import cn.heren.com.impro.widget.ConversationListview;

/**
 * 会话
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener {

    private ConversationListview lv_conv;
    private ScrollView sv_conv;
    private LinearLayout ll_all_friend;
    private View view;

    private ConversationAdapter adapter;
    private List datas = null;

    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_conversation);
        setShownTitle(R.string.conversation_tab);
        setRightTextVisibility(false);
        setRightImageVisibility(true);
        setRightImage(R.drawable.icon_invit_member);
    }

    @Override
    protected void findId() {
        sv_conv = (ScrollView) findViewById(R.id.sv_conv);
        lv_conv = (ConversationListview) findViewById(R.id.lv_conv);
        ll_all_friend = (LinearLayout) findViewById(R.id.ll_all_friend);
    }

    @Override
    protected void addOnListener() {
        ll_all_friend.setOnClickListener(this);
        iv_right.setOnClickListener(this);
    }

    @Override
    protected void processDatas() {
        datas = new ArrayList<Object>();
        for (int i = 0 ;i < 10; i ++) {
            datas.add(i+"");
        }
        adapter = new ConversationAdapter(this, datas);
        lv_conv.setAdapter(adapter);
        lv_conv.setFocusable(false);
        sv_conv.smoothScrollBy(0,20);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_all_friend:
                startActivity(new Intent(this,ContactsActivity.class)); //所有好友
                break;
            case R.id.iv_right:
                startActivity(new Intent(this,AddFriendActivity.class)); //添加朋友
                break;
        }
    }
}
