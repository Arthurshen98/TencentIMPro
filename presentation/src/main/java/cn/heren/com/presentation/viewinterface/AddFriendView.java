package cn.heren.com.presentation.viewinterface;

import com.tencent.TIMUserProfile;

import java.util.List;

/**
 * 添加好友
 * Created by Administrator on 2016/9/19.
 */
public interface AddFriendView {


    void showUserInfo(List<TIMUserProfile> users); //用户信息

    void searchUserError(); //按照phone搜索好友失败

    void searchFriendError(); //按照id搜索好友失败

}
