package cn.heren.com.impro.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMFriendGroup;
import com.tencent.TIMFriendResult;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.model.FriendGroupInfo;
import cn.heren.com.impro.model.FriendProfile;
import cn.heren.com.impro.view.adapter.FriendGroupEditAdapter;
import cn.heren.com.impro.widget.ConversationListview;
import cn.heren.com.impro.widget.SwipeListView;
import cn.heren.com.presentation.event.FriendshipEvent;
import cn.heren.com.presentation.presenter.FriendGroupEditPresenter;
import cn.heren.com.presentation.presenter.FriendshipManagerPresenter;
import cn.heren.com.presentation.viewinterface.FriendGroupEditView;
import cn.heren.com.ui.SwipeMenu;
import cn.heren.com.ui.SwipeMenuCreator;
import cn.heren.com.ui.SwipeMenuItem;
import cn.heren.com.ui.SwipeMenuListView;

/**
 *  组资料
 */
public class FriendGroupEditActivity extends BaseActivity implements FriendGroupEditView,View.OnClickListener {

    private TextView tv_edit_group_name,tv_edit_group_count,tv_no_mamber;
    private ImageView iv_add_member;
    private SwipeListView listView_fge;
    private ScrollView sv_fge;

    private String groupName;
    private List groupNames ;
    private FriendGroupEditAdapter adapter;
    private FriendGroupInfo friendGroupInfo;
    private List<Long> counts ;
    private List<String> groups;
    private List<FriendProfile> friendItemList;
    private ArrayList<String> memberList;
    private int localWigth = 0;
    private int localHeigth = 0;
    private String groupNa; //群名
    private List<String> delMemberName;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final int ADDMEMBER = 101;
    private FriendGroupEditPresenter presenter;

    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_friend_group_edit);
        setShownTitle(R.string.fge_group_name);
        setRightTextVisibility(false);
    }

    @Override
    protected void findId() {
        tv_edit_group_name = (TextView) findViewById(R.id.tv_edit_group_name);
        tv_edit_group_count = (TextView) findViewById(R.id.tv_edit_group_count);
        listView_fge = (SwipeListView) findViewById(R.id.listView_fge);
        tv_no_mamber = (TextView) findViewById(R.id.tv_no_mamber);
        sv_fge = (ScrollView) findViewById(R.id.sv_fge);
        iv_add_member = (ImageView) findViewById(R.id.iv_add_member);
    }

    @Override
    protected void addOnListener() {
        iv_add_member.setOnClickListener(this);
        listView_fge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    protected void processDatas() {
        initArraylist(); //初始化list
        initFriendGroupName(); //初始化组名
        initGetMember(); //获取成员
        swipeDel();//处理滑动删除
    }

    /**
     * 获取成员
     */
    private void initGetMember() {
        friendGroupInfo = new FriendGroupInfo();
        presenter = new FriendGroupEditPresenter(this);
        presenter.getFriendGroups(groupNames);
    }

    /**
     * 初始化组名
     */
    private void initFriendGroupName() {
        groupNa = getIntent().getStringExtra("groupName");
        if (groupNa.equals("") || groupNa == null) {
            tv_edit_group_name.setText("默认分组");
            groupNames.add("默认分组");
        }else {
            tv_edit_group_name.setText(groupNa);
            groupNames.add(groupNa);
        }
    }

    /**
     * 初始化list
     */
    private void initArraylist() {
        friendItemList = new ArrayList<FriendProfile>();
        groups = new ArrayList<>();
        memberList = new ArrayList<String>();
        delMemberName = new ArrayList<>();
        groupNames = new ArrayList<String>();
        counts = new ArrayList<Long>();
    }

    /**
     * 处理滑动删除
     */
    private void swipeDel() {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建一个"删除"功能菜单
                SwipeMenuItem delItem = new SwipeMenuItem(FriendGroupEditActivity.this);
                // 设置菜单的背景
                delItem.setBackground(R.color.btn_red_hover);
                // 宽度：菜单的宽度是一定要有的，否则不会显示
                delItem.setWidth(dp2px(80));
                // 菜单标题
                delItem.setTitle("删除");
                // 标题文字大小
                delItem.setTitleSize(16);
                // 标题的颜色
                delItem.setTitleColor(Color.WHITE);
                // 添加到menu
                menu.addMenuItem(delItem);
            }
        };

        listView_fge.setMenuCreator(swipeMenuCreator);
        listView_fge.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                       delGroupMember(position);
                        break;
                }
                return false;
            }
        });


        listView_fge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        localWigth = (int)motionEvent.getX();
                        localHeigth = (int)motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int sx = (int)motionEvent.getX();
                        int sy = (int)motionEvent.getY();
                        if (localWigth - sx > 10 || localWigth - sx < 10) {
                            sv_fge.requestDisallowInterceptTouchEvent(true);
                        }else {
                            sv_fge.requestDisallowInterceptTouchEvent(false);
                        }
                        if (localHeigth - sy >= 50 || localHeigth - sy < -50){
                            sv_fge.requestDisallowInterceptTouchEvent(false);
                        }else {
                            sv_fge.requestDisallowInterceptTouchEvent(true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        localWigth = 0;
                        localHeigth= 0;
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 删除群中的成员
     * @param position
     */
    private void delGroupMember(final int position) {
        delMemberName.add(memberList.get(position));
        presenter.delFriendsFromFriendGroup(position,groupNa,delMemberName);
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_member:
                //添加组成员
                addFriendGroupMemb();
                break;
        }
    }

    /**
     * 添加组成员
     */
    private void addFriendGroupMemb() {
        Intent intent = new Intent(this, ChooseFriendActivity.class);
        intent.putStringArrayListExtra("MemberSelected", memberList);
        startActivityForResult(intent,ADDMEMBER);
    }

    private ArrayList<String> datas = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ADDMEMBER == requestCode) {
            datas = new ArrayList<>();
            if (data != null) {
                datas = data.getStringArrayListExtra("memberSelected");
                if (datas != null) {
                    presenter.addFriendsToFriendGroup(groupNa,datas);
                }
            }
        }else {
            Toast.makeText(FriendGroupEditActivity.this, getString(R.string.add_member_fail), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取成员失败
     */
    @Override
    public void getFriendGroupsError() {
        Toast.makeText(FriendGroupEditActivity.this, getString(R.string.get_member_fail), Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取成员成功
     */
    @Override
    public void getFriendGroupsSucc(List<Long> counts,ArrayList<String> memberList) {
        this.memberList = memberList;
        this.counts = counts;
        adapter = new FriendGroupEditAdapter(FriendGroupEditActivity.this, memberList);
        listView_fge.setAdapter(adapter);

        if (counts.size() == 0) {
            tv_edit_group_count.setText("0");
        }else {
            tv_edit_group_count.setText(counts.get(0).toString());
        }

        showNoMember(memberList);//如果memberList为空则显示无成员
        groupNames.clear();
    }

    /**
     * 成为空时处理背景
     */
    private void showNoMember(ArrayList<String> memberList) {
        if (memberList.size() == 0) {
            tv_no_mamber.setVisibility(View.VISIBLE);
        }else {
            tv_no_mamber.setVisibility(View.GONE);
        }
    }

    /**
     * 删除成员失败
     */
    @Override
    public void delMemberError() {
        Toast.makeText(FriendGroupEditActivity.this, getString(R.string.del_member_failed), Toast.LENGTH_SHORT).show();
    }

    /**
     * 删除成员成功
     */
    @Override
    public void delMemberSucc(int position) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                long c = counts.get(0);
                int a = (int) c;
                if (a == 0) {
                    tv_edit_group_count.setText("0");
                    return;
                }else {
                    tv_edit_group_count.setText((a - 1)+"");
                }
            }
        });
        Toast.makeText(FriendGroupEditActivity.this, getString(R.string.del_member_succ), Toast.LENGTH_SHORT).show();
        FriendshipEvent.getInstance().OnDelFriends(Collections.singletonList(memberList.get(position)));
        memberList.remove(position);
        handler.post(new Runnable() {
            @Override
            public void run() {
                showNoMember(memberList);
            }
        });
        adapter.notifyDataSetChanged();
    }

    /**
     * 添加成员失败
     */
    @Override
    public void addMemberError() {
        Toast.makeText(FriendGroupEditActivity.this, getString(R.string.add_member_fail), Toast.LENGTH_SHORT).show();
    }

    /**
     * 添加成员成功
     */
    @Override
    public void addMemberSucc(final ArrayList<String> memberList) {
        this.memberList = memberList;
        handler.post(new Runnable() {
            @Override
            public void run() {
                tv_edit_group_count.setText(memberList.size()+"");
                showNoMember(memberList);
            }
        });
        Toast.makeText(FriendGroupEditActivity.this, getString(R.string.add_member_succ), Toast.LENGTH_SHORT).show();
        FriendshipEvent.getInstance().OnFriendGroupChange();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
