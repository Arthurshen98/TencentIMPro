package cn.heren.com.impro.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.model.UserInfo;
import cn.heren.com.presentation.event.FriendshipEvent;
import cn.heren.com.presentation.event.GroupEvent;
import cn.heren.com.presentation.event.MessageEvent;
import cn.heren.com.presentation.presenter.LoginPresenter;
import cn.heren.com.presentation.viewinterface.LoginView;
import cn.heren.com.tls.service.TLSService;
import tencent.tls.platform.TLSErrInfo;

public class LoginActivity extends BaseActivity implements View.OnClickListener,LoginView {

    private EditText login_account, login_password;
    private Button btn_login;

    private TLSService tlsService;

    private LoginPresenter loginPresenter;
    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_login);
        setShownTitle(getString(R.string.login_title)); //标题名
        setRightTextVisibility(false); //隐藏右边文字
    }

    @Override
    protected void findId() {
        login_account = (EditText) findViewById(R.id.login_account);
        login_password = (EditText) findViewById(R.id.login_password);
        btn_login = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void addOnListener() {
        btn_login.setOnClickListener(this);
    }

    @Override
    protected void processDatas() {
        login_account.setText("mimi");
        login_password.setText("sf1234");
        tlsService = TLSService.getInstance();
        loginPresenter = new LoginPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginPresenter.login();
                break;
        }
    }

    @Override
    public String getUserName() {
        return login_account.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return login_password.getText().toString().trim();
    }

    /**
     * 登录失败
     */
    @Override
    public void loginAccountFailed(TLSErrInfo errInfo) {
        Toast.makeText(LoginActivity.this,"登录账号失败"+errInfo.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginServiceSuccess() {
        Toast.makeText(LoginActivity.this,"登录成功".toString(),Toast.LENGTH_SHORT).show();
        //初始化消息监听
        MessageEvent.getInstance();
        //登录之前要初始化群和好友关系链缓存
        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();

        UserInfo.getInstance().setId(login_account.getText().toString().trim());
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(login_account.getText().toString().trim()));
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void loginServiceFailed(int i , String s) {
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                /*NotifyDialog dialog = new NotifyDialog();
                dialog.show("登录失败，当前无网络", getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });*/
                break;
            case 6200:
                Toast.makeText(LoginActivity.this,"登录失败，当前无网络",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(LoginActivity.this,"登录失败,"+s,Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
