package cn.heren.com.impro.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.model.PhoneContactsInfo;
import cn.heren.com.impro.util.GetPhoneContactsUtil;
import cn.heren.com.impro.view.adapter.PingyinAdapter;
import cn.heren.com.impro.widget.FancyIndexer;

public class PhoneContactsActivity extends BaseActivity {

    private ExpandableListView lv_content;

    private PingyinAdapter<PhoneContactsInfo> adapter;
    private List<PhoneContactsInfo> datas;
    private Handler handler = new Handler(Looper.getMainLooper());
    // 查询通讯录成功
    private static int GET_CONTACTS_SUCCE;
    private static int GET_CONTACTS_FAIL;

    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_phone_contacts);
        setShownTitle(R.string.phone_con_title);
        setRightTextVisibility(false);
    }

    @Override
    protected void findId() {
        lv_content = (ExpandableListView) findViewById(R.id.lv_content);
    }

    @Override
    protected void addOnListener() {

    }

    @Override
    protected void processDatas() {
        datas = new ArrayList<>();
        lv_content.setGroupIndicator(null);
        getPhoneBook(); //获取手机通信录
    }
    /**
     * 接收到查询通讯录结果的消息，进行下一步任务
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.arg1 == GET_CONTACTS_SUCCE) {

            } else {
                Toast.makeText(PhoneContactsActivity.this, "查询失败,请检查通讯录访问权限", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 获取手机通信录
     */
    private void getPhoneBook() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    datas = GetPhoneContactsUtil.getContacts(PhoneContactsActivity.this);
                    Message message = mHandler.obtainMessage();
                    message.arg1 = GET_CONTACTS_SUCCE;
                    mHandler.sendMessage(message);
                    toRefreshUI();//更新UI
                }
                catch (Exception ex2)
                {
                   ex2.printStackTrace();
                    Message message = mHandler.obtainMessage();
                    message.arg1 = GET_CONTACTS_FAIL;
                    mHandler.sendMessage(message);
                }
            }
        }).start();

    }

    private void toRefreshUI() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (datas.size() > 0) {
                    initUI(); //异步更新ui
                }else{
                    Toast.makeText(PhoneContactsActivity.this,"您手机无联系人，添加后再试啊~",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initUI() {

        /**加入支持泛型*/
        adapter=new PingyinAdapter<PhoneContactsInfo>(lv_content,datas,R.layout.item_man){

            @Override
            public String getItemName(PhoneContactsInfo goodMan) {
                return goodMan.getName();
            }
            /**返回viewholder持有*/
            @Override
            public ViewHolder getViewHolder(PhoneContactsInfo goodMan) {
                /**View holder*/
                class DataViewHolder extends ViewHolder implements View.OnClickListener {
                    public TextView tv_name;
                    public Button btn_fancy_invate;
                    public DataViewHolder(PhoneContactsInfo goodMan) {
                        super(goodMan);
                    }
                    /**初始化*/
                    @Override
                    public ViewHolder getHolder(View view) {
                        tv_name = (TextView) view.findViewById(R.id.tv_name);
                        btn_fancy_invate = (Button) view.findViewById(R.id.btn_fancy_invate);
                        btn_fancy_invate.setOnClickListener(this);
                        return this;
                    }
                    /**显示数据*/
                    @Override
                    public void show() {
                        tv_name.setText(getItem().getName());
                    }

                    @Override
                    public void onClick(View view) {
                        //跳转到短信页面
                        toIntentMsg(getItem().getPhoneNumber());
                    }
                }
                return new DataViewHolder(goodMan);
            }

            @Override
            public void onItemClick(PhoneContactsInfo goodMan, View view, int position) {
                Toast.makeText(view.getContext(),position+" "+goodMan.getName(),Toast.LENGTH_SHORT).show();
            }
        };
        /**展开并设置adapter*/
        adapter.expandGroup();

        FancyIndexer mFancyIndexer = (FancyIndexer) findViewById(R.id.bar);
        mFancyIndexer.setOnTouchLetterChangedListener(new FancyIndexer.OnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String letter) {
                for(int i=0,j=adapter.getKeyMapList().getTypes().size();i<j;i++)
                {
                    String str=adapter.getKeyMapList().getTypes().get(i);
                    if(letter.toUpperCase().equals(str.toUpperCase())){
                        /**跳转到选中的字母表*/
                        lv_content.setSelectedGroup(i);
                    }
                    if (letter.toUpperCase().equals("^")) {
                        lv_content.smoothScrollToPosition(0);
                    }
                }
            }
        });

        adapter.notifyDataSetChanged();

    }


    //跳转到短信页面发信息
    public void toIntentMsg(String number) {
        Uri smsToUri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                smsToUri);
        intent.putExtra("sms_body",
                getString(R.string.sms_invite));
        startActivity(intent);
    }

}
