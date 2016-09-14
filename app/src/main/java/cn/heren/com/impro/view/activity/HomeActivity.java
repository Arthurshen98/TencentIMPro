package cn.heren.com.impro.view.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMManager;
import com.tencent.TIMUserStatusListener;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.model.FriendshipInfo;
import cn.heren.com.impro.model.GroupInfo;
import cn.heren.com.impro.model.UserInfo;
import cn.heren.com.impro.view.fragment.ContactFragment;
import cn.heren.com.impro.view.fragment.ConversationFragment;
import cn.heren.com.impro.view.fragment.SettingFragment;
import cn.heren.com.presentation.event.FriendshipEvent;
import cn.heren.com.presentation.event.GroupEvent;
import cn.heren.com.presentation.event.MessageEvent;
import cn.heren.com.tls.service.TlsBusiness;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private LayoutInflater layoutInflater;
    private FragmentTabHost mTabHost;

    private final Class fragmentArray[] = {ConversationFragment.class, ContactFragment.class, SettingFragment.class};
    private int mTitleArray[] = {R.string.home_conversation_tab, R.string.home_contact_tab, R.string.home_setting_tab};
    private int mImageViewArray[] = {R.drawable.tab_conversation, R.drawable.tab_contact, R.drawable.tab_setting};
    private String mTextviewArray[] = {"conversation ","contact", "setting"};
    private ImageView msgUnread;

    private View view;

    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_main);
        setTitleBarIsShow(false); //设置title不显示
    }

    @Override
    protected void findId() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
    }

    @Override
    protected void addOnListener() {

    }

    @Override
    protected void processDatas() {
        initTabView();  //初始化和处理tab
        initToUnLine();//互踢下线

        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();
    }

    /**
     * 互踢下线
     */
    private void initToUnLine() {
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.d(TAG, "receive force offline message");
//                Intent intent = new Intent(HomeActivity.this, DialogActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                 logout();
            }
        });
    }


    private void logout() {
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initTabView() {
        layoutInflater = LayoutInflater.from(this);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contentPanel);
        int fragmentCount = fragmentArray.length;
        for (int i = 0; i < fragmentCount; ++i) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().setDividerDrawable(null);
            mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String s) {
                    Toast.makeText(HomeActivity.this,s,Toast.LENGTH_SHORT).show();
                    /*if (s.toString().equals("conversation")) {
                        setShownTitle("会话");
                        setBackIconVisibility(false);
                        setRightTextVisibility(false);
                    }else if (s.toString().equals("contact")) {
                        setShownTitle("联系人");
                        setBackIconVisibility(false);
                        setRightTextVisibility(true);
                        setRightText(getString(R.string.home_add));
                    }else if (s.toString().equals("setting")){
                        setShownTitle("设置");
                        setBackIconVisibility(false);
                        setRightTextVisibility(true);
                    }*/
                }
            });
        }

    }

    private View getTabItemView(int index) {
        view = layoutInflater.inflate(R.layout.tab_home, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(mImageViewArray[index]);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(mTitleArray[index]);
        if (index == 0){
            msgUnread = (ImageView) view.findViewById(R.id.tabUnread);
        }
        return view;
    }


}
