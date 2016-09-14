package cn.heren.com.tls.service;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;


import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSPwdRegListener;
import tencent.tls.platform.TLSPwdResetListener;
import tencent.tls.platform.TLSRefreshUserSigListener;
import tencent.tls.platform.TLSSmsLoginListener;
import tencent.tls.platform.TLSSmsRegListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by dgy on 15/7/8.
 */
public class TLSService {

    private TLSLoginHelper loginHelper;
    private TLSAccountHelper accountHelper;


    private AccountLoginService accountLoginService;

    private static int lastErrno = -1;

    private static TLSService tlsService = null;

    private TLSService(){}

    private RefreshUserSigListener refreshUserSigListener = new RefreshUserSigListener() {
        @Override
        public void onUserSigNotExist() {}
        @Override
        public void OnRefreshUserSigSuccess(TLSUserInfo tlsUserInfo) {}
        @Override
        public void OnRefreshUserSigFail(TLSErrInfo tlsErrInfo) {}
        @Override
        public void OnRefreshUserSigTimeout(TLSErrInfo tlsErrInfo) {}
    };

    public static TLSService getInstance() {
        if (tlsService == null) {
            tlsService = new TLSService();
        }
        return tlsService;
    }

    public static void setLastErrno(int errno) {
        lastErrno = errno;
    }

    public static int getLastErrno() {
        return lastErrno;
    }

    /**
     * @function: 初始化TLS SDK, 必须在使用TLS SDK相关服务之前调用
     * @param context: 关联的activity
     * */
    public void initTlsSdk(Context context) {

        loginHelper = TLSLoginHelper.getInstance().init(context.getApplicationContext(),
                TLSConfiguration.SDK_APPID, TLSConfiguration.ACCOUNT_TYPE, TLSConfiguration.APP_VERSION);
        loginHelper.setTimeOut(TLSConfiguration.TIMEOUT);
        loginHelper.setLocalId(TLSConfiguration.LANGUAGE_CODE);
        loginHelper.setTestHost("", true);                   // 走sso

        accountHelper = TLSAccountHelper.getInstance().init(context.getApplicationContext(),
                TLSConfiguration.SDK_APPID, TLSConfiguration.ACCOUNT_TYPE, TLSConfiguration.APP_VERSION);
        accountHelper.setCountry(Integer.parseInt(TLSConfiguration.COUNTRY_CODE)); // 存储注册时所在国家，只须在初始化时调用一次
        accountHelper.setTimeOut(TLSConfiguration.TIMEOUT);
        accountHelper.setLocalId(TLSConfiguration.LANGUAGE_CODE);
        accountHelper.setTestHost("", true);                 // 走sso

    }

    /**
     * 代理TLSLoginHelper的接口
     */

    public void initAccountLoginService(Context context,
                                        EditText txt_username,
                                        EditText txt_password,
                                        Button btn_login) {
        accountLoginService = new AccountLoginService(context, txt_username, txt_password, btn_login);
    }


    public int smsLoginVerifyCode(String code, TLSSmsLoginListener listener) {
        return loginHelper.TLSSmsLoginVerifyCode(code, listener);
    }

    public String getUserSig(String identify) {
        return loginHelper.getUserSig(identify);
    }

    public TLSUserInfo getLastUserInfo() {
        return loginHelper.getLastUserInfo();
    }

    public String getLastUserIdentifier() {
        TLSUserInfo userInfo = getLastUserInfo();
        if (userInfo != null)
            return userInfo.identifier;
        else
            return null;
    }

    public void clearUserInfo(String identifier) {
        loginHelper.clearUserInfo(identifier);
        lastErrno = -1;
    }

    public boolean needLogin(String identifier) {
        if (identifier == null)
            return true;
        return loginHelper.needLogin(identifier);
    }

    public String getSDKVersion() {
        return loginHelper.getSDKVersion();
    }

    public void refreshUserSig(String identifier, RefreshUserSigListener refreshUserSigListener) {
        if (null == refreshUserSigListener)
            refreshUserSigListener = this.refreshUserSigListener;

        if (!needLogin(identifier))
            loginHelper.TLSRefreshUserSig(identifier, refreshUserSigListener);
        else
            refreshUserSigListener.onUserSigNotExist();
    }

    public int TLSPwdLogin(String identifier, String password, TLSPwdLoginListener listener) {
        return loginHelper.TLSPwdLogin(identifier, password.getBytes(), listener);
    }

    public interface RefreshUserSigListener extends TLSRefreshUserSigListener {
        public void onUserSigNotExist();
    }
}
