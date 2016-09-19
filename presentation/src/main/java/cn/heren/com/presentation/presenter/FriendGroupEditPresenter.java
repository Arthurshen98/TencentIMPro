package cn.heren.com.presentation.presenter;

import android.os.Handler;

import com.tencent.TIMAddFriendRequest;
import com.tencent.TIMDelFriendType;
import com.tencent.TIMFriendGroup;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

import cn.heren.com.presentation.viewinterface.FriendGroupEditView;

/**
 * Created by Administrator on 2016/9/18.
 * 组资料
 */
public class FriendGroupEditPresenter {
    private FriendGroupEditView view;
    private Handler handler = new Handler();
    private ArrayList<String> memberList;
    private List<Long> counts;
    private List<String> delMemberName;
    private List<TIMAddFriendRequest> reqList ;

    public FriendGroupEditPresenter(FriendGroupEditView view) {
        this.view = view;
        counts = new ArrayList<Long>();
        memberList = new ArrayList<String>();
        delMemberName = new ArrayList<>();
        reqList = new ArrayList<TIMAddFriendRequest>();
    }

    /**
     * 获取组成员
     */
    public void getFriendGroups(final List<String> groupNames) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TIMFriendshipManager.getInstance().getFriendGroups(groupNames, new TIMValueCallBack<List<TIMFriendGroup>>() {
                    @Override
                    public void onError(int i, String s) {
                        view.getFriendGroupsError();
                    }

                    @Override
                    public void onSuccess(List<TIMFriendGroup> timFriendGroups) {
                        if (timFriendGroups == null) return;
                        for (TIMFriendGroup group : timFriendGroups) {
                            counts.add(group.getCount());
                            for (int i = 0; i < group.getUsers().size(); i++) {
                                memberList.add(group.getUsers().get(i).toString());
                            }
                        }
                        view.getFriendGroupsSucc(counts, memberList);
                    }
                });
            }
        });
    }

    /**
     * 删除组成员,删除好友,指定的双向好友
     */
    public void delFriendsFromFriendGroup(final int position, final String groupNa, final List<String> delMemberName) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TIMAddFriendRequest request = new TIMAddFriendRequest();
                request.setIdentifier(delMemberName.get(0));
                reqList.add(request);
                TIMFriendshipManager.getInstance().delFriend(TIMDelFriendType.TIM_FRIEND_DEL_BOTH, reqList, new TIMValueCallBack<List<TIMFriendResult>>() {
                    @Override
                    public void onError(int i, String s) {
                        view.delMemberError();
                    }

                    @Override
                    public void onSuccess(List<TIMFriendResult> timFriendResults) {
                        view.delMemberSucc(position);
                    }
                });
            }
        });
    }

    /**
     * 添加成员
     */
    public void addFriendsToFriendGroup(final String groupNa, final ArrayList<String> selectedMem) {
      handler.post(new Runnable() {
          @Override
          public void run() {
             TIMFriendshipManager.getInstance().addFriendsToFriendGroup(groupNa, selectedMem, new TIMValueCallBack<List<TIMFriendResult>>() {
                 @Override
                 public void onError(int i, String s) {
                      view.addMemberError();
                 }

                 @Override
                 public void onSuccess(List<TIMFriendResult> timFriendResults) {
                     for (TIMFriendResult item : timFriendResults) {
                         memberList.add(item.getIdentifer());
                     }
                   view.addMemberSucc(memberList);
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