package cn.heren.com.impro.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import cn.heren.com.impro.model.PhoneContactsInfo;

/**
 * 获取手机通讯录工具类
 * Created by Administrator on 2016/9/19.
 */
public class GetPhoneContactsUtil {
    /**
     * 获取所有联系人内容
      * @param context
      * @return
     */
   public static List<PhoneContactsInfo> getContacts(Context context) {
       String uri_contacts = "content://com.android.contacts/raw_contacts";
       String uri_contacts_phones = "content://com.android.contacts/data/phones";
       PhoneContactsInfo info = new PhoneContactsInfo();
       List<PhoneContactsInfo> list = new ArrayList<>();

       ContentResolver cr = context.getContentResolver();
       // 从raw_contacts表中或许联系人的id和联系人的姓名。
       Cursor cursor_contacts = cr.query(Uri.parse(uri_contacts), new String[]{"_id", "display_name"},
                     null, null, null);
           while (cursor_contacts.moveToNext()){
                     int contacts_id = cursor_contacts.getInt(cursor_contacts
                             .getColumnIndex("_id"));

                     String display_name = cursor_contacts.getString(cursor_contacts
                             .getColumnIndex("display_name"));
                     //第一条不用换行
                     info.setName(display_name);
                     // 以下开始获取电话号码
                     // 根据每个联系人的id再去data表中查找相应的电话号码。
                     Cursor cursor_phones = cr.query(
                             Uri.parse(uri_contacts_phones), new String[]{
                                     "raw_contact_id", "data1"}, "raw_contact_id=?",
                             new String[]{contacts_id + ""}, null);
                     // 因为电话号码可能是多个，所以需要再遍历，组合在一起形成一个电话号码的字符串，放到StringBuilder中
                     List<String> lists = new ArrayList<String>();
                     while (cursor_phones.moveToNext()) {
                         if (cursor_phones.getString(1) == null)
                             continue;
                         lists.add(cursor_phones.getString(1));
                         info.setPhoneNumber(cursor_phones.getString(1));
                     }
                     cursor_phones.close();
                     list.add(info);
                 }
       cursor_contacts.close();
       return list;
   }
}
