package cn.heren.com.impro.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.heren.com.impro.R;
import cn.heren.com.impro.util.StatusBarUtil;

/**
 * 基类Activity，所有Activity继承该类，便于操作
 */
public abstract class BaseActivity extends FragmentActivity {

    private LinearLayout base_content;
    private View inflate;
    protected RelativeLayout rl_back_icon, rl_title;
    protected TextView tv_title, tv_right;
    protected ImageView iv_right, img_back;

    private boolean isAllowFullScreen = true;

    private StatusBarUtil statusBarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE); //设置是否显示系统默认的bar,默认隐藏
        }
        setStatusBar(); //设置沉浸式状态栏
        setTopTitleBar(); //设置顶部标题栏以及统一的布局
        initView(); //初始化抽象方法
    }

    /**
     * 初始化抽象方法
     */
    private void initView() {
        loadActivityLayout(); //加载Activity布局:记得要删除每一个子Activity自动生成的oncreate方法,否则会被oncreate代替，不能正常显示
        findId(); //寻找控件id
        addOnListener(); //添加监听
        processDatas(); //处理数据
    }
    /**
     * 加载Activity布局
     */
    protected abstract void loadActivityLayout();

    /**
     * 寻找控件id
     */
    protected abstract void findId();

    /**
     * 添加监听
     */
    protected abstract void addOnListener();

    /**
     * 处理数据
     */
    protected abstract void processDatas();

    @Override
    public void setContentView(int layoutResID) {
        inflate = getLayoutInflater().inflate(layoutResID, null);
        setContentView(inflate);
    }

    @Override
    public void setContentView(View view) {
        base_content.removeAllViews();
        base_content.addView(inflate);
    }

    @Override
    public View findViewById(int id) {
        return inflate.findViewById(id);
    }
    /**
     * 设置顶部标题栏以及统一的布局
     */
    private void setTopTitleBar() {
        super.setContentView(R.layout.activity_base);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏

        base_content = (LinearLayout) super.findViewById(R.id.base_content);
        rl_back_icon = (RelativeLayout) super.findViewById(R.id.rl_back_icon);
        img_back = (ImageView) super.findViewById(R.id.img_back);
        tv_right = (TextView) super.findViewById(R.id.tv_right);
        rl_title = (RelativeLayout) super.findViewById(R.id.rl_title);
        iv_right = (ImageView) super.findViewById(R.id.iv_right);
        rl_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.this.finish();
            }
        });
    }

    /**
     * 设置是否显示返回按钮；true:显示, false:不显示
     */
    public void setBackIconVisibility(boolean isShown){
        if (isShown) {
            rl_back_icon.setVisibility(View.VISIBLE);
        } else {
            rl_back_icon.setVisibility(View.GONE);
        }
    }

    /**
     * 设置返回按钮图片
     */
    public void setBackIconResource (int resId) {
        img_back.setImageResource(resId);
    }

    /**
     * 设置是否显示标题栏右侧文本；true:显示, false:不显示
     * @param isShown
     */
    public void setRightTextVisibility(boolean isShown){
        if (isShown) {
            tv_right.setVisibility(View.VISIBLE);
        } else {
            tv_right.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题显示内容
     * @param text 标题文字
     */
    public void setShownTitle(String text) {
        tv_title = (TextView) super.findViewById(R.id.tv_title);
        tv_title.setText(text);
    }

    /**
     * 设置标题显示内容
     * @param resId 资源ID
     */
    public void setShownTitle(int resId) {
        tv_title = (TextView) super.findViewById(R.id.tv_title);
        tv_title.setText(resId);
    }

    /**
     * 设置标题栏右侧文本显示的内容
     * @param text 传入的文本
     */
    public void setRightText(String text){
        tv_right.setText(text);
    }

    /**
     * 设置标题栏右侧文本显示的内容
     * @param resId 资源ID
     */
    public void setRightText(int resId) {
        iv_right.setVisibility(View.GONE);
        tv_right.setText(resId);
    }

    /**
     * 设置标题右侧文本颜色
     * @param color
     */
    public void setRightTextColor(int color) {
        tv_right.setTextColor(color);
    }

    /**
     * 设置标题栏右侧的图片资源
     *
     * @param resId
     */
    public void setRightImage(int resId) {
        iv_right.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.GONE);
        iv_right.setImageResource(resId);
    }

    /**
     * 设置标题栏右侧的图片按钮是否可见
     *
     * @param visibility true:可见,false:不可见[默认不可见]
     */
    public void setRightImageVisibility(boolean visibility) {
        if (visibility) {
            iv_right.setVisibility(View.VISIBLE);
        } else {
            iv_right.setVisibility(View.GONE);
        }
    }

    /**
     * 设置title是否显示
     */
    public void setTitleBarIsShow(Boolean bool) {
        if (!bool) {
            rl_title.setVisibility(View.GONE);
        } else {
            rl_title.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置是否允许全屏(无状态栏，无标题栏)，默认：不全屏显示 ，false为全屏
     * @param isAllowFullScreen
     */
    public void setAllowFullScreen(boolean isAllowFullScreen) {
        this.isAllowFullScreen = isAllowFullScreen;
        if (isAllowFullScreen) {
            rl_title.setVisibility(View.GONE);
        } else {
            rl_title.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
    }

    /**
     * 设置沉浸式状态栏
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }
        statusBarUtil = new StatusBarUtil(this);
        statusBarUtil.setStatusBarTintEnabled(true);
        statusBarUtil.setStatusBarTintResource(R.color.colorAccent);
        setStatusBarColor(getResources().getColor(R.color.colorAccent));// 主色
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 针对5.0及以上版本设置[状态栏][StatusBar]的背景颜色[可随时使用]
     *
     * @param color
     */
    protected void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Logger.e("VERSION_CODES >= LOLLIPOP");
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            // window.setNavigationBarColor(Color.TRANSPARENT);// 针对5.0及以上版本设置[虚拟导航栏][NavigationBar]的背景颜色
        }
    }

    /**
     * 返回键调成此方法
     */
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    /**
     * 在Activity中跳转的时候
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    /**
     * finish时也调用该方法，完成该效果
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
