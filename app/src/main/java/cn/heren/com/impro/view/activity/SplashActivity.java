package cn.heren.com.impro.view.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

import com.tencent.TIMLogLevel;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.model.UserInfo;
import cn.heren.com.presentation.business.InitBusiness;
import cn.heren.com.presentation.event.FriendshipEvent;
import cn.heren.com.presentation.event.GroupEvent;
import cn.heren.com.presentation.event.MessageEvent;
import cn.heren.com.presentation.event.RefreshEvent;
import cn.heren.com.presentation.presenter.SplashPresenter;
import cn.heren.com.presentation.viewinterface.SplashView;
import cn.heren.com.tls.service.TLSService;
import cn.heren.com.tls.service.TlsBusiness;

public class SplashActivity extends BaseActivity implements SplashView {
    private SplashPresenter presenter;

    private final int REQUEST_PHONE_PERMISSIONS = 0;

    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_splash);
        setBackIconVisibility(false);//隐藏返回箭头
        setRightTextVisibility(false);//隐藏右边的字
        setShownTitle(getString(R.string.start_page));
    }

    @Override
    protected void findId() {

    }

    @Override
    protected void addOnListener() {

    }

    @Override
    protected void processDatas() {
        clearNotification();//清除所有通知栏通知
        init();//初始化工作
    }

    /**
     * 初始化
     */
    private void init() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(),loglvl);
        //初始化TLS
        TlsBusiness.init(getApplicationContext());
        //设置刷新监听
        RefreshEvent.getInstance();

        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();
        MessageEvent.getInstance();

        String id =  TLSService.getInstance().getLastUserIdentifier();
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
        presenter = new SplashPresenter(this);
        presenter.start();
    }

    public void toLogin(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void navToHome() {
        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void navToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean isUserLogin() {
     //   return UserInfo.getInstance().getId()!= null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()));
       return false;
    }

    /**
     * 清楚所有通知栏通知
     */
    private void clearNotification(){
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(this, getString(R.string.need_permission),Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
