package cn.heren.com.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 发送语音提示控件
 */
public class VoiceSendingView extends RelativeLayout {


    private AnimationDrawable frameAnimation;

    private ImageView img;
    private TextView tv_voice_sending_up;
    public VoiceSendingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.voice_sending, this);
        img = (ImageView)findViewById(R.id.microphone);
        tv_voice_sending_up = (TextView) findViewById(R.id.tv_voice_sending_up);

    }

    /**
     * 取消语音
     */
    public void cancelRecording() {
        frameAnimation.stop();
        tv_voice_sending_up.setText(R.string.chat_songkai_finger); //松开手指取消发送
        tv_voice_sending_up.setBackgroundResource(R.color.btn_red_hover);
        img.setBackgroundResource(R.drawable.record_cancel_normal_large);
    }

    public void showRecording(){
        tv_voice_sending_up.setText(R.string.chat_up_finger);
        tv_voice_sending_up.setBackgroundResource(R.color.transparent);
        img.setBackgroundResource(R.drawable.animation_voice);
        frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
    }

    public void showCancel(){
        frameAnimation.stop();
    }

    public void release(){
        frameAnimation.stop();
    }
}
