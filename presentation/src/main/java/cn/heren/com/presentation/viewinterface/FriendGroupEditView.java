package cn.heren.com.presentation.viewinterface;

import com.tencent.TIMFriendResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 * 组资料
 */
public interface FriendGroupEditView {

    void getFriendGroupsError(); //获取成员失败

    void getFriendGroupsSucc(List<Long> counts,ArrayList<String> memberList); //获取成员成功

    void delMemberError(); //删除成员失败

    void delMemberSucc(int position); //删除成员成功

    void addMemberError(); //添加成员成功

    void addMemberSucc(ArrayList<String> memberList); //添加成员失败
}
