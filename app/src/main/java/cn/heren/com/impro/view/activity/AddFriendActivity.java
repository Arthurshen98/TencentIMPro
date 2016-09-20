package cn.heren.com.impro.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;

import java.util.ArrayList;
import java.util.List;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.model.FriendProfile;
import cn.heren.com.impro.model.ProfileSummary;
import cn.heren.com.impro.util.ClearEditTextUtil;
import cn.heren.com.impro.util.RegexUtils;
import cn.heren.com.impro.view.adapter.AddFriendAdapter;
import cn.heren.com.presentation.presenter.AddFriendPresenter;
import cn.heren.com.presentation.viewinterface.AddFriendView;

public class AddFriendActivity extends BaseActivity implements AddFriendView, TextWatcher, View.OnClickListener {

    private EditText et_add_friend_search;
    private ImageView iv_add_fri_search_clear;
    private RelativeLayout rl_add_fri_phone_book;
    private ListView listView;
    private TextView tv_add_fir_no_user;

    private AddFriendPresenter presenter;
    private List<ProfileSummary> userList ;
    private List<String> datas;
    private AddFriendAdapter adapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String keyword;
    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_add_friend);
        setShownTitle(R.string.add_friend);
        setRightTextVisibility(false);
    }

    @Override
    protected void findId() {
        et_add_friend_search = (EditText) findViewById(R.id.et_add_friend_search);
        iv_add_fri_search_clear = (ImageView) findViewById(R.id.iv_add_fri_search_clear);
        rl_add_fri_phone_book = (RelativeLayout) findViewById(R.id.rl_add_fri_phone_book);
        tv_add_fir_no_user = (TextView) findViewById(R.id.tv_add_fir_no_user);
        listView = (ListView) findViewById(R.id.lv_add_friend);
    }

    @Override
    protected void addOnListener() {
        et_add_friend_search.addTextChangedListener(this);
        iv_add_fri_search_clear.setOnClickListener(this);
        rl_add_fri_phone_book.setOnClickListener(this);
    }

    @Override
    protected void processDatas() {
        userList = new ArrayList<>();
        datas = new ArrayList<>();
        presenter = new AddFriendPresenter(this);
    }

    /**
     * 对edittext的监听处理
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable != null && editable.length() > 0) {
            iv_add_fri_search_clear.setVisibility(View.VISIBLE); //显示清理符号
            rl_add_fri_phone_book.setVisibility(View.GONE); //隐藏通讯录item条
            tv_add_fir_no_user.setVisibility(View.GONE); //隐藏查询不到用户背景
            datas.clear();
            handler.post(new Runnable() {
               @Override
               public void run() {
                   toRefreshUI();
               }
           });
        }else {
            iv_add_fri_search_clear.setVisibility(View.GONE);//隐藏清理符号
            tv_add_fir_no_user.setVisibility(View.GONE); //隐藏背景
            listView.setVisibility(View.GONE);
            rl_add_fri_phone_book.setVisibility(View.VISIBLE); //显示通讯录item条
            datas.clear();
        }
    }

    /**
     * 刷新ui更新数据
     */
    public void toRefreshUI() {
        keyword = et_add_friend_search.getText().toString().trim();
        if (datas!=null) {
            datas.clear();
        }
        if (RegexUtils.checkPhone(keyword)){
            presenter.searchUser(keyword,true); //按手机号请求
        }else {
            presenter.searchFriendById(keyword);//按昵称请求
        }


    }

    /**
     * 搜素好友信息显示
     * @param users
     */
    @Override
    public void showUserInfo(List<TIMUserProfile> users) {
        if (users != null){
            for (TIMUserProfile item : users){
                if (needAdd(item.getIdentifier()))
                    userList.add(new FriendProfile(item));
            }
        }
        if (users.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            tv_add_fir_no_user.setVisibility(View.GONE);
        }else {
            listView.setVisibility(View.GONE);
            tv_add_fir_no_user.setVisibility(View.VISIBLE);
        }
        adapter = new AddFriendAdapter(this, userList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private boolean needAdd(String id){
        for (ProfileSummary item : userList){
            if (item.getIdentify().equals(id)) return false;
        }
        return true;
    }

    /**
     * 按照phone搜索好友失败
     */
    @Override
    public void searchUserError() {
        tv_add_fir_no_user.setVisibility(View.VISIBLE);
        tv_add_fir_no_user.setText(R.string.add_fri_user_error);
    }

    /**
     * 按照id搜索好友失败
     */
    @Override
    public void searchFriendError() {
        tv_add_fir_no_user.setVisibility(View.VISIBLE);
        tv_add_fir_no_user.setText(R.string.add_fri_user_error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_fri_search_clear:
                ClearEditTextUtil.clearAccountInfo(iv_add_fri_search_clear,et_add_friend_search); //清理edittext框
                break;
            case R.id.rl_add_fri_phone_book:
                startActivity(new Intent(this,PhoneContactsActivity.class));
                break;
        }
    }
}
