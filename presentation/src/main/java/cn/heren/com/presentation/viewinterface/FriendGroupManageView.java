package cn.heren.com.presentation.viewinterface;

/**
 * 好友组管理
 * Created by Administrator on 2016/9/14.
 */
public interface FriendGroupManageView {

    void createFriendGroupError(int i); //创建组错误

    void createFriendGroupSucc(String groupname); //创建组成功

    void groupNameNotNull();//分组不能为空提示

    void delFriendGroupError(); //删除组失败

    void delFriendGroupSucc(int position); //删除组成功

    void editFriendGroupError(int i);//编辑组失败

    void editFriendGroupSucc(String newGroupName,int position); //编辑组成功
}
