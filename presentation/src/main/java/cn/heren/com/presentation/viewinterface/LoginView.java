package cn.heren.com.presentation.viewinterface;

import tencent.tls.platform.TLSErrInfo;

/**
 * 登录要实现的接口
 * Created by Administrator on 2016/8/29.
 */
public interface LoginView {

    String getUserName(); //用户名

    String getPassword(); //密码

    void loginAccountFailed(TLSErrInfo tlsErrInfo); //登录账户失败

    void loginServiceSuccess(); //登录服务器成功

    void loginServiceFailed(int i ,String s); //登录服务器失败

}
