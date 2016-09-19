package cn.heren.com.presentation.presenter;

import android.os.Handler;
import android.text.TextUtils;

import com.tencent.TIMCallBack;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.heren.com.presentation.viewinterface.FriendGroupManageView;
import cn.heren.com.presentation.viewinterface.LoginView;

/**
 * 好友组管理
 * Created by Administrator on 2016/9/14.
 */
public class FriendGroupManagePresenter {

    private FriendGroupManageView view;
    private Handler handler = new Handler();

    public FriendGroupManagePresenter(FriendGroupManageView view){
        this.view = view;
    }

    /**
     * 创建组
     */
    public void createFriendGroup(final String groupName) {
        if (!TextUtils.isEmpty(groupName)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    TIMFriendshipManager.getInstance().createFriendGroup(Collections.singletonList(groupName), new ArrayList<String>(), new TIMValueCallBack<List<TIMFriendResult>>() {
                        @Override
                        public void onError(int i, String s) {
                            view.createFriendGroupError(i);
                        }
                        @Override
                        public void onSuccess(List<TIMFriendResult> timFriendResults) {
                            view.createFriendGroupSucc(groupName);
                        }
                    });
                }
            });
        }else {
            view.groupNameNotNull();
        }
    }

    /**
     * 编辑群
     */
    public void editFriendGroup(final String oldName, final String newName,final int position) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(newName)) {
                    FriendshipManagerPresenter.changeFriendGroupName(oldName, newName, new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            view.editFriendGroupError(i);
                        }

                        @Override
                        public void onSuccess() {
                            view.editFriendGroupSucc(newName,position);
                        }
                    });
                }else {
                    view.groupNameNotNull();
                }

            }
        });
    }

    /**
     * 删除群
     */
    public void delFriendGroup(final List<String> groups, final int position) {
       handler.post(new Runnable() {
           @Override
           public void run() {
               TIMFriendshipManager.getInstance().deleteFriendGroup(Collections.singletonList(groups.get(position)), new TIMCallBack() {
                   @Override
                   public void onError(int i, String s) {
                       view.delFriendGroupError();
                   }

                   @Override
                   public void onSuccess() {
                       view.delFriendGroupSucc(position);
                   }
               });
           }
       });

    }

    /**
     * 将presenter获取到Activity对象view = null，避免内存泄漏
     */
    public void onDestroy() {
        view = null;
    }
}

