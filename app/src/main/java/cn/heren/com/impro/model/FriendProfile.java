package cn.heren.com.impro.model;

import android.content.Context;
import android.content.Intent;

import com.tencent.TIMConversationType;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;

import cn.heren.com.impro.MyApplication;
import cn.heren.com.impro.R;
import cn.heren.com.impro.view.activity.ChatActivity;


/**
 * 好友资料
 */
public class FriendProfile implements ProfileSummary {


    private TIMUserProfile profile;
    private boolean isSelected;

    public FriendProfile(TIMUserProfile profile){
        this.profile = profile;
    }


    /**
     * 获取头像资源
     */
    @Override
    public int getAvatarRes() {
        return R.drawable.head_other;
    }

    /**
     * 获取头像地址
     */
    @Override
    public String getAvatarUrl() {
        return null;
    }

    /**
     * 获取名字
     */
    @Override
    public String getName() {
        if (!profile.getRemark().equals("")){
            return profile.getRemark();
        }else if (!profile.getNickName().equals("")){
            return profile.getNickName();
        }
        return profile.getIdentifier();
    }

    /**
     * 获取描述信息
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     * 显示详情
     *
     * @param context 上下文
     */
    @Override
    public void onClick(Context context) {
        Intent person = new Intent(context,ChatActivity.class);
        person.putExtra("identify",profile.getIdentifier());
        person.putExtra("type", TIMConversationType.C2C);
        context.startActivity(person);
      //  if (FriendshipInfo.getInstance().isFriend(profile.getIdentifier())){
          //  ProfileActivity.navToProfile(context, profile.getIdentifier());
       // }else{
         //   Intent person = new Intent(context,AddFriendActivity.class);
        //    person.putExtra("id",profile.getIdentifier());
        //    person.putExtra("name",getName());
         //   context.startActivity(person);
       // }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    /**
     * 获取用户ID
     */
    @Override
    public String getIdentify(){
        return profile.getIdentifier();
    }


    /**
     * 获取用户备注名
     */
    public String getRemark(){
        return profile.getRemark();
    }


    /**
     * 获取好友分组
     */
    public String getGroupName(){
        if (profile.getFriendGroups().size() == 0){
            return "默认分组";
        }else{
            return profile.getFriendGroups().get(0);
        }
    }

}
