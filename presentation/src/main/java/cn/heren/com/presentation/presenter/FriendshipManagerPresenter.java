package cn.heren.com.presentation.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.TIMAddFriendRequest;
import com.tencent.TIMCallBack;
import com.tencent.TIMDelFriendType;
import com.tencent.TIMFriendAddResponse;
import com.tencent.TIMFriendAllowType;
import com.tencent.TIMFriendFutureMeta;
import com.tencent.TIMFriendGroup;
import com.tencent.TIMFriendResponseType;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGetFriendFutureListSucc;
import com.tencent.TIMPageDirectionType;
import com.tencent.TIMUserProfile;
import com.tencent.TIMUserSearchSucc;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.heren.com.presentation.event.FriendshipEvent;

/**
 * 好友关系链管理逻辑
 * Created by Administrator on 2016/9/10.
 */
public class FriendshipManagerPresenter {
    private static final String TAG = "FriendManagerPresenter";

//    private FriendshipMessageView friendshipMessageView;
//    private FriendshipManageView friendshipManageView;
//    private FriendInfoView friendInfoView;
    private final int PAGE_SIZE = 20;
    private int index;
    private long pendSeq, decideSeq, recommendSeq;
    private boolean isEnd;

//    public FriendshipManagerPresenter(FriendshipMessageView view){
//        this(view, null, null);
//    }
//
//    public FriendshipManagerPresenter(FriendInfoView view){
//        this(null, null, view);
//    }
//
//    public FriendshipManagerPresenter(FriendshipManageView view){
//        this(null, view, null);
//    }
//
//    public FriendshipManagerPresenter(FriendshipMessageView view1, FriendshipManageView view2, FriendInfoView view3){
//        friendshipManageView = view2;
//        friendshipMessageView = view1;
//        friendInfoView = view3;
//    }




    /**
     * 获取好友关系链最后一条消息,和未读消息数
     * 包括：好友已决系统消息，好友未决系统消息，推荐好友消息
     */
    public void getFriendshipLastMessage(){
        TIMFriendFutureMeta meta = new TIMFriendFutureMeta();
        meta.setReqNum(1);
        meta.setDirectionType(TIMPageDirectionType.TIM_PAGE_DIRECTION_DOWN_TYPE);
        long reqFlag = 0, futureFlags = 0;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_NICK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_REMARK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_ALLOW_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_DECIDE_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_RECOMMEND_TYPE;
        TIMFriendshipManager.getInstance().getFutureFriends(reqFlag, futureFlags, null, meta ,new TIMValueCallBack<TIMGetFriendFutureListSucc>(){

            @Override
            public void onError(int arg0, String arg1) {
                Log.i(TAG, "onError code" + arg0 + " msg " + arg1);
            }

            @Override
            public void onSuccess(TIMGetFriendFutureListSucc arg0) {
                long unread = arg0.getMeta().getPendencyUnReadCnt() +
                        arg0.getMeta().getDecideUnReadCnt() +
                        arg0.getMeta().getRecommendUnReadCnt();
//                if (friendshipMessageView != null && arg0.getItems().size() > 0){
//                    friendshipMessageView.onGetFriendshipLastMessage(arg0.getItems().get(0), unread);
//                }
            }

        });
    }


    /**
     * 同意好友请求
     *
     * @param identify 同意对方的ID
     * @param callBack 回调
     */
    public static void acceptFriendRequest(String identify, TIMValueCallBack<TIMFriendResult> callBack){
        TIMFriendAddResponse response = new TIMFriendAddResponse();
        response.setIdentifier(identify);
        response.setType(TIMFriendResponseType.AgreeAndAdd);
        TIMFriendshipManager.getInstance().addFriendResponse(response, callBack);
    }

    /**
     * 拒绝好友请求
     *
     * @param identify 同意对方的ID
     * @param callBack 回调
     */
    public static void refuseFriendRequest(String identify, TIMValueCallBack<TIMFriendResult> callBack){
        TIMFriendAddResponse response = new TIMFriendAddResponse();
        response.setIdentifier(identify);
        response.setType(TIMFriendResponseType.Reject);
        TIMFriendshipManager.getInstance().addFriendResponse(response, callBack);
    }


    /**
     * 获取好友关系链消息
     * 包括：好友已决系统消息，好友未决系统消息，推荐好友消息
     */
    public void getFriendshipMessage(){
        TIMFriendFutureMeta meta = new TIMFriendFutureMeta();
        meta.setReqNum(PAGE_SIZE);
        //设置用于分页拉取的seq
        meta.setPendencySeq(pendSeq);
        meta.setDecideSeq(decideSeq);
        meta.setRecommendSeq(recommendSeq);
        meta.setDirectionType(TIMPageDirectionType.TIM_PAGE_DIRECTION_DOWN_TYPE);
        long reqFlag = 0, futureFlags = 0;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_NICK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_REMARK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_ALLOW_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_DECIDE_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE;
        futureFlags |= TIMFriendshipManager.TIM_FUTURE_FRIEND_RECOMMEND_TYPE;
        TIMFriendshipManager.getInstance().getFutureFriends(reqFlag, futureFlags, null, meta ,new TIMValueCallBack<TIMGetFriendFutureListSucc>(){

            @Override
            public void onError(int arg0, String arg1) {
                Log.e(TAG, "onError code" + arg0 + " msg " + arg1);
            }

            @Override
            public void onSuccess(TIMGetFriendFutureListSucc arg0) {
                pendSeq = arg0.getMeta().getPendencySeq();
                decideSeq = arg0.getMeta().getDecideSeq();
                recommendSeq = arg0.getMeta().getRecommendSeq();
//                if (friendshipMessageView != null){
//                    friendshipMessageView.onGetFriendshipMessage(arg0.getItems());
//                }
            }

        });
    }


    /**
     * 获取自己的资料
     */
//    public void getMyProfile(){
//        if (friendInfoView == null) return;
//        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onSuccess(TIMUserProfile profile) {
//                friendInfoView.showUserInfo(Collections.singletonList(profile));
//            }
//        });
//    }




    /**
     * 按照名称搜索好友
     *
     * @param key 关键字
     * @param start 是否从头开始
     */
//    public void searchFriendByName(String key, boolean start){
//        if (friendInfoView == null) return;
//        if (start){
//            isEnd = false;
//            index = 0;
//        }
//        if (!isEnd){
//            TIMFriendshipManager.getInstance().searchUser(key, index++, PAGE_SIZE, new TIMValueCallBack<TIMUserSearchSucc>() {
//
//                @Override
//                public void onError(int arg0, String arg1) {
//
//                }
//
//                @Override
//                public void onSuccess(TIMUserSearchSucc data) {
//                    int getNum = data.getInfoList().size() + (index-1)*PAGE_SIZE;
//                    isEnd = getNum == data.getTotalNum();
//                    friendInfoView.showUserInfo(data.getInfoList());
//                }
//            });
//        }else{
//            friendInfoView.showUserInfo(null);
//        }
//
//    }



    /**
     * 按照ID搜索好友
     *
     * @param identify id
     */
//    public void searchFriendById(String identify){
//        if (friendInfoView == null) return;
//        TIMFriendshipManager.getInstance().searchFriend(identify, new TIMValueCallBack<TIMUserProfile>() {
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onSuccess(TIMUserProfile profile) {
//                friendInfoView.showUserInfo(Collections.singletonList(profile));
//            }
//        });
//
//    }


    /**
     * 添加好友
     *
     * @param id 添加对象Identify
     * @param remark 备注名
     * @param group 分组
     * @param message 附加消息
     */
//    public void addFriend(final String id,String remark,String group,String message){
//        if (friendshipManageView == null) return;
//        List<TIMAddFriendRequest> reqList = new ArrayList<>();
//        TIMAddFriendRequest req = new TIMAddFriendRequest();
//        req.setAddWording(message);
//        req.setIdentifier(id);
//        req.setRemark(remark);
//        req.setFriendGroup(group);
//        reqList.add(req);
//        TIMFriendshipManager.getInstance().addFriend(reqList, new TIMValueCallBack<List<TIMFriendResult>>() {
//
//            @Override
//            public void onError(int arg0, String arg1) {
//                Log.e(TAG, "onError code" + arg0 + " msg " + arg1);
//                friendshipManageView.onAddFriend(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN);
//            }
//
//            @Override
//            public void onSuccess(List<TIMFriendResult> arg0) {
//                for (TIMFriendResult item : arg0) {
//                    if (item.getIdentifer().equals(id)) {
//                        friendshipManageView.onAddFriend(item.getStatus());
//                    }
//                }
//            }
//
//        });
//    }


    /**
     * 删除好友
     *
     * @param id 删除对象Identify
     */
//    public void delFriend(final String id){
//        if (friendshipManageView == null) return;
//        List<TIMAddFriendRequest> reqList = new ArrayList<>();
//        TIMAddFriendRequest req = new TIMAddFriendRequest();
//        req.setIdentifier(id);
//        reqList.add(req);
//        TIMFriendshipManager.getInstance().delFriend(TIMDelFriendType.TIM_FRIEND_DEL_BOTH, reqList, new TIMValueCallBack<List<TIMFriendResult>>() {
//            @Override
//            public void onError(int i, String s) {
//                friendshipManageView.onAddFriend(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN);
//            }
//
//            @Override
//            public void onSuccess(List<TIMFriendResult> timFriendResults) {
//                for (TIMFriendResult item : timFriendResults) {
//                    if (item.getIdentifer().equals(id)) {
//                        friendshipManageView.onDelFriend(item.getStatus());
//                    }
//                }
//            }
//        });
//    }


    /**
     * 设置添加好友验证类型
     *
     * @param type 好友验证类型
     * @param callBack 回调
     */
    public static void setFriendAllowType(TIMFriendAllowType type, TIMCallBack callBack){
        TIMFriendshipManager.getInstance().setAllowType(type, callBack);
    }



    /**
     * 设置我的昵称
     *
     * @param name 昵称
     * @param callBack 回调
     */
    public static void setMyNick(String name, TIMCallBack callBack){
        TIMFriendshipManager.getInstance().setNickName(name, callBack);
    }


    /**
     * 设置好友备注
     *
     * @param identify 好友identify
     * @param name 备注名
     * @param callBack 回调
     */
    public static void setRemarkName(String identify, String name, TIMCallBack callBack){
        TIMFriendshipManager.getInstance().setFriendRemark(identify, name, callBack);
    }


    /**
     * 好友关系链消息已读上报
     * 同时把已决未决消息和好友推荐消息已读
     *
     * @param timestamp 时间戳
     */
    public void readFriendshipMessage(long timestamp){
        TIMFriendshipManager.getInstance().pendencyReport(timestamp, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                FriendshipEvent.getInstance().OnFriendshipMessageRead();
            }
        });
        TIMFriendshipManager.getInstance().recommendReport(timestamp, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                FriendshipEvent.getInstance().OnFriendshipMessageRead();
            }
        });
    }


    /**
     * 切换好友分组
     *
     * @param identify 好友identify
     * @param src 源分组，为空表示默认分组
     * @param dest 目的分组，为空表示默认分组
     */
//    public void changeFriendGroup(final String identify, final @Nullable String src, final @Nullable String dest){
//        if (friendshipManageView == null || src != null && dest != null && src.equals(dest)) return;
//        if (src != null){
//            TIMFriendshipManager.getInstance().delFriendsFromFriendGroup(src, Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
//                @Override
//                public void onError(int i, String s) {
//                    Log.e(TAG, "changeFriendGroup.del src,code" + i + " msg " + s);
//                    friendshipManageView.onChangeGroup(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN, src);
//                }
//
//                @Override
//                public void onSuccess(List<TIMFriendResult> timFriendResults) {
//                    if (dest != null){
//                        TIMFriendshipManager.getInstance().addFriendsToFriendGroup(dest, Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
//                            @Override
//                            public void onError(int i, String s) {
//                                Log.e(TAG, "changeFriendGroup.add dest,code" + i + " msg " + s);
//                                friendshipManageView.onChangeGroup(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN, null);
//                            }
//
//                            @Override
//                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
//                                friendshipManageView.onChangeGroup(timFriendResults.get(0).getStatus(), dest);
//                            }
//                        });
//                    }else{
//                        friendshipManageView.onChangeGroup(TIMFriendStatus.TIM_FRIEND_STATUS_SUCC, dest);
//                    }
//                }
//            });
//        }else{
//            if (dest == null) return;
//            TIMFriendshipManager.getInstance().addFriendsToFriendGroup(dest, Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
//                @Override
//                public void onError(int i, String s) {
//                    Log.e(TAG, "changeFriendGroup.add dest,code" + i + " msg " + s);
//                    friendshipManageView.onChangeGroup(TIMFriendStatus.TIM_FRIEND_STATUS_UNKNOWN, null);
//                }
//
//                @Override
//                public void onSuccess(List<TIMFriendResult> timFriendResults) {
//                    friendshipManageView.onChangeGroup(timFriendResults.get(0).getStatus(), dest);
//                }
//            });
//        }
//    }


    /**
     * 删除一个分组
     *
     * @param groupName 好友分组名称
     * @param callBack 回调
     */
    public static void delFriendGroup(String groupName, TIMCallBack callBack){
        TIMFriendshipManager.getInstance().deleteFriendGroup(Collections.singletonList(groupName), callBack);
    }

    /**
     * 创建一个新分组
     *
     * @param groupName 好友分组名称
     * @param callBack 回调
     */
    public static void createFriendGroup(String groupName, TIMValueCallBack<List<TIMFriendResult>> callBack) {
        TIMFriendshipManager.getInstance().createFriendGroup(Collections.singletonList(groupName), new ArrayList<String>(), callBack);
    }

    /**
     * 更改分组名
     * @param oldName
     * @param newName
     * @param cb
     */
    public static void changeFriendGroupName(String oldName, String newName, TIMCallBack cb) {
        TIMFriendshipManager.getInstance().renameFriendGroupName(oldName,newName,cb);
    }

    /**
     * 获取指定分组的好友
     * @param groupNames
     * @param cb
     */
    public static void getFriendGroups(List<String> groupNames, TIMValueCallBack<List<TIMFriendGroup>> cb) {
        TIMFriendshipManager.getInstance().getFriendGroups(groupNames,cb);
    }

    /**
     * 删除指定分组中的成员
     * @param groupName
     * @param users
     * @param cb
     */
    public static void delFriendsFromFriendGroup(String groupName, List<String> users, TIMValueCallBack<List<TIMFriendResult>> cb) {
        TIMFriendshipManager.getInstance().delFriendsFromFriendGroup(groupName,users,cb);
    }

    /**
     *  添加好友到某分组
     * @param groupName
     * @param users
     * @param cb
     */
    public static void addFriendsToFriendGroup(String groupName, List<String> users, TIMValueCallBack<List<TIMFriendResult>> cb){
        TIMFriendshipManager.getInstance().addFriendsToFriendGroup(groupName,users,cb);
    }

    /**
     * 加入黑名单
     *
     * @param identfiy 加黑名单列表
     * @param callBack 回调
     */
    public static void addBlackList(List<String> identfiy, TIMValueCallBack<List<TIMFriendResult>> callBack){
        TIMFriendshipManager.getInstance().addBlackList(identfiy, callBack);
    }


    /**
     * 移除黑名单
     *
     * @param identfiy 移除黑名单列表
     * @param callBack 回调
     */
    public static void delBlackList(List<String> identfiy, TIMValueCallBack<List<TIMFriendResult>> callBack){
        TIMFriendshipManager.getInstance().delBlackList(identfiy, callBack);
    }

}
