package cn.heren.com.impro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 * Created by Administrator on 2016/9/19.
 */
public class RegexUtils {
    private boolean startCheck(String reg, String string) {
        boolean tem = false;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        tem = matcher.matches();
        return tem;
    }
    /**
     * 判断手机号是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        String telRegex = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";
        return phone.matches(telRegex);
    }

    /**
     * 验证密码格式
     * 6-16位 大小写特殊字符，无划线，横线等
     * @param password
     * @return
     */

    public static boolean checkPassword(String password) {
        String psRegex = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{6,16}$";

        return password.matches(psRegex);
    }


    /**
     * 验证用户名
     */
    /**
     * 检验用户名 取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾 用户名有最小长度和最大长度限制，比如用户名必须是4-20位
     * */
    public boolean checkUsername(String username, int min, int max) {
        String regex = "[\\w\u4e00-\u9fa5]{" + min + "," + max + "}(?<!_)";
        return startCheck(regex, username);
    }


    /**
     * 检验空白符
     * */
    public boolean checkWhiteLine(String line) {
        String regex = "(\\s|\\t|\\r)+";

        return startCheck(regex, line);
    }
}
