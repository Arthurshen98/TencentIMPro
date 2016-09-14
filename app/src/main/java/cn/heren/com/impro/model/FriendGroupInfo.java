package cn.heren.com.impro.model;

import com.tencent.TIMUserProfile;
import com.tencent.imcore.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 组资料info
 * Created by Administrator on 2016/9/11.
 */
public class FriendGroupInfo {
    String name = "";
    long count = 0L;
    List<String> users = new ArrayList();
    List<TIMUserProfile> profiles = new ArrayList();

    public FriendGroupInfo() {
    }

    FriendGroupInfo(FriendGroup var1) {
        try {
            this.setName(new String(var1.getName(), "utf-8"));
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        this.setCount(var1.getCount());
        StrVec var2 = var1.getIdentifiers();
        ArrayList var3 = new ArrayList();

        for(int var4 = 0; (long)var4 < var2.size(); ++var4) {
            if(var2.get(var4).length() != 0) {
                var3.add(var2.get(var4));
            }
        }

        this.setUsers(var3);
        FriendProfileVec var8 = var1.getProfiles();

        for(int var6 = 0; (long)var6 < var8.size(); ++var6) {
            com.tencent.imcore.FriendProfile var7;
            if((var7 = var8.get(var6)) != null) {
                this.profiles.add(new TIMUserProfile(var7));
            }
        }

    }

    public String getGroupName() {
        return this.name;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long var1) {
        this.count = var1;
    }

    public List<String> getUsers() {
        return this.users;
    }

    public void setUsers(List<String> var1) {
        this.users = var1;
    }

    public List<TIMUserProfile> getProfiles() {
        return this.profiles;
    }


}
