package cn.heren.com.presentation.presenter;

import android.os.Handler;

import cn.heren.com.presentation.viewinterface.SplashView;


/**
 * 闪屏界面逻辑
 */
public class SplashPresenter {
    SplashView view;
    private static final String TAG = SplashPresenter.class.getSimpleName();

    public SplashPresenter(SplashView view) {
        this.view = view;
    }


    /**
     * 加载页面逻辑
     */
    public void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view.isUserLogin()) {
                    view.navToHome();
                } else {
                    view.navToLogin();
                }
            }
        }, 500);
    }


}
