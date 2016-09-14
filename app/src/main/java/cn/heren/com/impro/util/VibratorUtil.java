package cn.heren.com.impro.util;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * 点击让手机震动
 * Created by Administrator on 2016/9/9.
 */
public class VibratorUtil {
    /**
     * final Activity activity  ：调用该方法的Activity实例
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */

    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
    public static void Vibrate(final Activity activity, long[] pattern,boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        //// 下边是可以使震动有规律的震动   -1：表示不重复 0：循环的震动
       // long[] pattern = { 200, 2000, 2000, 200, 200, 200 };
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
