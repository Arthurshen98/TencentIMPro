package cn.heren.com.impro.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.heren.com.impro.R;


/**
 * @author Tom.Cai
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 */
public class EditDialog extends Dialog {
    private EditText editText;
    private Button positiveButton, negativeButton;
    private TextView title1,title2;

    public EditDialog(Context context) {
        super(context, R.style.AlertDialogStyle);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_with_edittext, null);
        title1 = (TextView) mView.findViewById(R.id.title1);
        title1 = (TextView) mView.findViewById(R.id.title2);
        editText = (EditText) mView.findViewById(R.id.et_editDialog_content);
        positiveButton = (Button) mView.findViewById(R.id.btn_editDialog_sure);
        negativeButton = (Button) mView.findViewById(R.id.btn_editDialog_cancel);
        super.setContentView(mView);
    }

    public View getEditText() {
        return editText;
    }

    public View getTextTitle() {
        return title1;
    }

    public View getTextContent() {
        return title1;
    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);
    }

    /**
     * 取消键监听器
     *
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }
}