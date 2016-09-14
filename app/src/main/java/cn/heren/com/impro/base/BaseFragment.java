package cn.heren.com.impro.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.heren.com.impro.R;

/**
 * A simple {@link Fragment} subclass.
 * 基类Fragment，所有Fragment继承该类
 */
public abstract class BaseFragment extends Fragment {

    private View rootView;
    protected Context mContext;

    public BaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutView(), null);
            mContext = getActivity();
            initView();
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView() {
        findId();
        addOnListener();
        processDatas();
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int resId) {
        return (T) rootView.findViewById(resId);
    }

    /**
     * 加载布局文件
     *
     */
    public abstract int getLayoutView();

    /**
     * 寻找控件
     */
    public abstract void findId();

    /**
     * 添加监听
     */
    public abstract void addOnListener();

    /**
     * 处理数据
     */
    public abstract void processDatas();

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }
}
