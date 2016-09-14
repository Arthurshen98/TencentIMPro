package cn.heren.com.impro.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMFriendResult;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.model.FriendshipInfo;
import cn.heren.com.impro.view.adapter.FriendGroupManageAdapter;
import cn.heren.com.impro.widget.EditDialog;
import cn.heren.com.impro.widget.NewAlertDialog;
import cn.heren.com.impro.widget.SwipeListView;
import cn.heren.com.presentation.event.FriendshipEvent;
import cn.heren.com.presentation.presenter.FriendshipManagerPresenter;
import cn.heren.com.ui.SwipeMenu;
import cn.heren.com.ui.SwipeMenuCreator;
import cn.heren.com.ui.SwipeMenuItem;
import cn.heren.com.ui.SwipeMenuListView;


/**
 * 分组管理
 */
public class FriendGroupManageActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_mana_add_group;
    private EditText et_content;//创建分组的输入框
    private SwipeListView listView_fgm;
    private ScrollView sv_fgm;

    private FriendGroupManageAdapter adapter;

    private EditDialog mEditDialog,editNameDialog;
    private NewAlertDialog mAlerDialog;
    private List<String> groups = new ArrayList<>();
    private int localWigth = 0;
    private int localHeigth = 0;
    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_friend_group_manage);
        setShownTitle(R.string.friend_manager);
        setRightTextVisibility(false);
    }

    @Override
    protected void findId() {
        ll_mana_add_group = (LinearLayout) findViewById(R.id.ll_mana_add_group);
        listView_fgm = (SwipeListView) findViewById(R.id.listView_fgm);
        sv_fgm = (ScrollView) findViewById(R.id.sv_fgm);

    }

    @Override
    protected void addOnListener() {
        ll_mana_add_group.setOnClickListener(this);
        listView_fgm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position == 0) {
//                    Toast.makeText(FriendGroupManageActivity.this, getString(R.string.no_edit_name), Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Intent intent = new Intent(FriendGroupManageActivity.this,FriendGroupEditActivity.class);
                intent.putExtra("groupName", groups.get(position));
                FriendGroupManageActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void processDatas() {
        groups.addAll(FriendshipInfo.getInstance().getGroups());
        adapter = new FriendGroupManageAdapter(this,groups,FriendGroupManageActivity.this);
        listView_fgm.setAdapter(adapter);
        listView_fgm.setFocusable(false);
        sv_fgm.smoothScrollBy(0,20);

        initEditDialog(); //初始化dialog

        swipeDelete();  //滑动重命名或删除
    }

    /**
     * 创建群
     */
    public void initEditDialog() {
        mEditDialog = new EditDialog(this);
        et_content = (EditText) mEditDialog.getEditText();
        mEditDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String groupname = et_content.getText().toString().trim();
                if (!TextUtils.isEmpty(groupname)) {
                    FriendshipManagerPresenter.createFriendGroup(groupname, new TIMValueCallBack<List<TIMFriendResult>>() {
                        @Override
                        public void onError(int i, String s) {
                            switch (i){
                                case 32218:
                                    //分组名称已存在
                                    Toast.makeText(FriendGroupManageActivity.this, getString(R.string.add_group_error_existed), Toast.LENGTH_SHORT).show();
                                    break;
                                case 32214:
                                    //分组达到上限
                                    Toast.makeText(FriendGroupManageActivity.this, getString(R.string.add_group_error_limit), Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(FriendGroupManageActivity.this, getString(R.string.add_group_error), Toast.LENGTH_SHORT).show();
                                    break;

                            }
                        }

                        @Override
                        public void onSuccess(List<TIMFriendResult> timFriendResults) {
                            Toast.makeText(FriendGroupManageActivity.this, getString(R.string.add_group_succ), Toast.LENGTH_SHORT).show();
                            FriendshipEvent.getInstance().OnAddFriendGroups(null);
                            groups.add(groupname);
                            adapter.notifyDataSetChanged();
                            FriendshipEvent.getInstance().OnAddFriendGroups(null);
                        }
                    });
                }else {
                    Toast.makeText(FriendGroupManageActivity.this, getString(R.string.add_dialog_null), Toast.LENGTH_SHORT).show();
                }
                mEditDialog.dismiss();
            }
        });
        mEditDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditDialog.dismiss();
            }
        });
    }

    /**
     * 删除群
     * @param position
     */
    public void deleteGroup(int position) {
        deleteDialog(position);
    }

    private void deleteDialog(final int position) {

        mAlerDialog = new NewAlertDialog(this).builder();
        mAlerDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlerDialog.dismissDialog();
            }
        }).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendshipManagerPresenter.delFriendGroup(groups.get(position), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(FriendGroupManageActivity.this, getString(R.string.get_member_group), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(FriendGroupManageActivity.this, getString(R.string.del_group_succ), Toast.LENGTH_SHORT).show();
                        FriendshipEvent.getInstance().OnDelFriendGroups(Collections.singletonList(groups.get(position)));
                        groups.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).setMsg("您确定删除该分组吗？").show();

    }

    /**
     * 编辑群名称
     */
    public void editGroupName(final int position) {
        editNameDialog = new EditDialog(this);
        et_content = (EditText) editNameDialog.getEditText();
        final String oldGroupName = groups.get(position);

        et_content.setText(oldGroupName);
        editNameDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newGroupName = et_content.getText().toString().trim();
                if (!TextUtils.isEmpty(newGroupName)) {
                    FriendshipManagerPresenter.changeFriendGroupName(oldGroupName,newGroupName, new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            switch (i){
                                case 32218:
                                    //分组名称已存在
                                    Toast.makeText(FriendGroupManageActivity.this, getString(R.string.add_group_error_existed), Toast.LENGTH_SHORT).show();
                                    break;
                                case 32214:
                                    //分组达到上限
                                    Toast.makeText(FriendGroupManageActivity.this, getString(R.string.add_group_error_limit), Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(FriendGroupManageActivity.this, getString(R.string.change_group_error), Toast.LENGTH_SHORT).show();
                                    break;

                            }
                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(FriendGroupManageActivity.this, getString(R.string.change_group_succ), Toast.LENGTH_SHORT).show();
                            FriendshipEvent.getInstance().OnAddFriendGroups(null);
                            groups.remove(position);
                            groups.add(position,newGroupName);
                            adapter.notifyDataSetChanged();
                            FriendshipEvent.getInstance().OnAddFriendGroups(null);
                        }

                    });
                }else {
                    Toast.makeText(FriendGroupManageActivity.this, getString(R.string.add_dialog_null), Toast.LENGTH_SHORT).show();
                }
                editNameDialog.dismiss();
            }
        });
        editNameDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNameDialog.dismiss();
            }
        });

        editNameDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_mana_add_group:
                if(!TextUtils.isEmpty(et_content.getText().toString().trim())){
                    et_content.setText("");
                }
                mEditDialog.show();  //显示show
                break;
        }
    }


    /**
     * 滑动重命名或删除
     */
    private void swipeDelete() {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建一个"重命名"功能菜单
                SwipeMenuItem openItem = new SwipeMenuItem(FriendGroupManageActivity.this);
                // 设置菜单的背景
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                // 宽度：菜单的宽度是一定要有的，否则不会显示
                openItem.setWidth(dp2px(80));
                // 菜单标题
                openItem.setTitle("重命名");
                // 标题文字大小
                openItem.setTitleSize(16);
                // 标题的颜色
                openItem.setTitleColor(Color.WHITE);
                // 添加到menu
                menu.addMenuItem(openItem);

                //创建一个"删除"功能菜单
                SwipeMenuItem delItem = new SwipeMenuItem(FriendGroupManageActivity.this);
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

        listView_fgm.setMenuCreator(swipeMenuCreator);
        listView_fgm.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        if (position == 0){
                            Toast.makeText(FriendGroupManageActivity.this, getString(R.string.no_reset_name), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        editGroupName(position);

                        break;
                    case 1:
                        if (position == 0){
                            Toast.makeText(FriendGroupManageActivity.this, getString(R.string.no_del_name), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        deleteGroup(position); //删除群
                        break;
                }
                return false;
            }
        });

      //  sv_fgm.requestDisallowInterceptTouchEvent(false);

        listView_fgm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        localWigth = (int)motionEvent.getX();
                        localHeigth = (int)motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int scrollY = sv_fgm.getScrollY();

                        int sx = (int)motionEvent.getX();
                        int sy = (int)motionEvent.getY();
                        if (localWigth - sx > 10 || localWigth - sx < 10) {
                            sv_fgm.requestDisallowInterceptTouchEvent(true);
                        }else {
                            sv_fgm.requestDisallowInterceptTouchEvent(false);
                        }
//
                        if (localHeigth - sy >= 50 || localHeigth - sy < -50){
                            sv_fgm.requestDisallowInterceptTouchEvent(false);
                        }else {
                            sv_fgm.requestDisallowInterceptTouchEvent(true);
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
