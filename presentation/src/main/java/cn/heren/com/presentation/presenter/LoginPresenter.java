package cn.heren.com.presentation.presenter;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;

import cn.heren.com.presentation.event.FriendshipEvent;
import cn.heren.com.presentation.event.GroupEvent;
import cn.heren.com.presentation.event.MessageEvent;
import cn.heren.com.presentation.viewinterface.LoginView;
import cn.heren.com.sdk.Constant;
import cn.heren.com.tls.helper.Util;
import cn.heren.com.tls.service.TLSService;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 登录
 * Created by Administrator on 2016/8/29.
 */
public class LoginPresenter {

    private LoginView view;
    private Handler handler = new Handler();

    public LoginPresenter(LoginView view){
        this.view = view;
    }

    /**
     * 处理登录逻辑
     */
    public void login() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TLSLoginHelper t = TLSLoginHelper.getInstance();
                t.TLSPwdLogin(view.getUserName(), view.getPassword().getBytes(), new TLSPwdLoginListener() {
                    @Override
                    public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
                         serviceLogin(); //服务器登录
                    }

                    @Override
                    public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {

                    }

                    @Override
                    public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {

                    }

                    @Override
                    public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
                        TLSService.getInstance().setLastErrno(-1);
                        view.loginAccountFailed(tlsErrInfo);
                    }

                    @Override
                    public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
                        TLSService.getInstance().setLastErrno(-1);
                    }
                });
            }
        },1000);
    }

    public void serviceLogin() {
        String identif = null;
        identif = view.getUserName();
        //发起到app TIM登录
        TIMUser timUser = new TIMUser();
        timUser.setAccountType(String.valueOf(Constant.ACCOUNT_TYPE));
        timUser.setAppIdAt3rd(String.valueOf(Constant.SDK_APPID));
        timUser.setIdentifier(identif);

        String userSig = TLSService.getInstance().getUserSig(identif);
        //发起到IM后台登录请求
        TIMManager.getInstance().login(Constant.SDK_APPID, timUser, userSig, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                 view.loginServiceFailed(i,s); //登录失败
            }

            @Override
            public void onSuccess() {
                view.loginServiceSuccess(); //登录成功
            }
        });
    }

    /**
     * 将presenter获取到Activity对象view = null，避免内存泄漏
     */
    public void onDestroy() {
        view = null;
    }
}

