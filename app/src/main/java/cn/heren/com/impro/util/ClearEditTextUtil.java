package cn.heren.com.impro.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 主要用于edittext框输入信息的清理
 * Created by Administrator on 2015/12/18.
 */
public class ClearEditTextUtil {

    /**
     * edittext框中输入监听改变clear图标
     */
    public static void editTextInputOnListener(final ImageView clearIcon, EditText editText) {
        clearIcon.setVisibility(View.GONE);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clearIcon.setVisibility(View.GONE);
                } else {
                    clearIcon.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    /**
     * 清除edittext中的信息
     */
    public static void clearAccountInfo( ImageView clearIcon,EditText editText) {
        //点击按钮删除文本
        editText.setText("");
        clearIcon.setVisibility(View.GONE);
    }
}
