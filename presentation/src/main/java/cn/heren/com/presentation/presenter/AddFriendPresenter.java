package cn.heren.com.presentation.presenter;

import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMUserSearchSucc;
import com.tencent.TIMValueCallBack;

import java.util.Collections;

import cn.heren.com.presentation.viewinterface.AddFriendView;

/**
 * 添加好友
 * Created by Administrator on 2016/9/19.
 */
public class AddFriendPresenter {

    private AddFriendView view;

    private final int PAGE_COUNT = 20;
    private int index;
    private boolean isEnd;

    public AddFriendPresenter(AddFriendView view) {
        this.view = view;
    }

    /**
     * 按照phone搜索好友
     */
    public void searchUser(String key,boolean start) {
        if (view == null) return;
        if (start){
            isEnd = false;
            index = 0;
        }
        if (!isEnd) {
            TIMFriendshipManager.getInstance().searchUser(key, index++, PAGE_COUNT, new TIMValueCallBack<TIMUserSearchSucc>() {
                @Override
                public void onError(int i, String s) {
                    view.searchUserError();
                }

                @Override
                public void onSuccess(TIMUserSearchSucc timUserSearchSucc) {

                }
            });
        }else {
            view.showUserInfo(null);
        }
    }

    /**
     * 按照ID和昵称搜索好友
     *
     * @param key id
     */
    public void searchFriendById(String key){
        if (view == null) return;
        if (checkPhone(key)){
            key = "86-" + key;
        }
        TIMFriendshipManager.getInstance().searchFriend(key, new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                view.searchFriendError();
            }

            @Override
            public void onSuccess(TIMUserProfile profile) {
                view.showUserInfo(Collections.singletonList(profile));
            }
        });

    }

    public static boolean checkPhone(String phone) {
        String telRegex = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\\\d{8}$";
        return phone.matches(telRegex);
    }

    /**
     * 将presenter获取到Activity对象view = null，避免内存泄漏
     */
    public void onDestroy() {
        view = null;
    }
}
