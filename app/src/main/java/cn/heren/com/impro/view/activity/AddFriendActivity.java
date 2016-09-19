package cn.heren.com.impro.view.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tencent.TIMFriendshipManager;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;

public class AddFriendActivity extends BaseActivity implements TextWatcher {

    private EditText et_add_friend_search;
    private ImageView iv_add_fri_search_clear;
    private RelativeLayout rl_add_fri_phone_book;
    private ListView lv_add_friend;

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
        lv_add_friend = (ListView) findViewById(R.id.lv_add_friend);
    }

    @Override
    protected void addOnListener() {
        et_add_friend_search.addTextChangedListener(this);
    }

    @Override
    protected void processDatas() {

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

    }


}
