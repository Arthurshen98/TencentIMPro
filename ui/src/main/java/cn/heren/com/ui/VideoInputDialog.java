package cn.heren.com.ui;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.heren.com.presentation.viewinterface.ChatView;

/**
 * 小视频输入控件
 */
public class VideoInputDialog extends DialogFragment {

    private static final String TAG = "VideoInputDialog";
    private Camera mCamera;
    private CameraPreview mPreview;
    private ProgressBar mProgressRight,mProgressLeft;
    private MediaRecorder mMediaRecorder;
    private Timer mTimer;
    private final int MAX_TIME = 1500;
    private int mTimeCount;
    private long time;
    private boolean isRecording = false;
    private String fileName;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            mProgressRight.setProgress(mTimeCount);
            mProgressLeft.setProgress(mTimeCount);
        }
    };
    private Runnable sendVideo = new Runnable() {
        @Override
        public void run() {
            recordStop();
        }
    };

    public static VideoInputDialog newInstance() {
        VideoInputDialog dialog = new VideoInputDialog();
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.maskDialog);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_video_input, container, false);
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getActivity(), mCamera);
        FrameLayout preview = (FrameLayout) v.findViewById(R.id.camera_preview);
        mProgressRight = (ProgressBar) v.findViewById(R.id.progress_right);
        mProgressLeft = (ProgressBar) v.findViewById(R.id.progress_left);
        mProgressRight.setMax(MAX_TIME);
        mProgressLeft.setMax(MAX_TIME);
        mProgressLeft.setRotation(180);
        ImageButton record = (ImageButton) v.findViewById(R.id.btn_record);
        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isRecording) {
                            if (prepareVideoRecorder()) {
                                time = Calendar.getInstance().getTimeInMillis();
                                mMediaRecorder.start();
                                isRecording = true;
                                mTimer = new Timer();
                                mTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mTimeCount++;
                                        mainHandler.post(updateProgress);
                                        if (mTimeCount == MAX_TIME) {
                                            mainHandler.post(sendVideo);
                                        }
                                    }
                                }, 0, 10);
                            } else {
                                releaseMediaRecorder();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        recordStop();
                        break;
                }
                return true;
            }
        });
        preview.addView(mPreview);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        recordStop();
        releaseMediaRecorder();
        releaseCamera();
    }


    private void recordStop(){
        if (isRecording) {
            isRecording = false;
            if (isLongEnough()){
                mMediaRecorder.stop();
            }
            releaseMediaRecorder();
            mCamera.lock();
            if (mTimer != null) mTimer.cancel();
            mTimeCount = 0;
            mainHandler.post(updateProgress);

        }
    }


    /**
     * 显示小视频输入控件
     */
    public static void show(FragmentManager ft){
        DialogFragment newFragment = VideoInputDialog.newInstance();
        newFragment.show(ft, "VideoInputDialog");
    }



    /** A safe way to get an instance of the Camera object. */
    private static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }



    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
            if (isLongEnough()){
                ((ChatView) getActivity()).sendVideo(fileName);
            }else{
                Toast.makeText(getContext(), getString(R.string.chat_video_too_short), Toast.LENGTH_SHORT).show();
            }
            dismiss();
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private boolean prepareVideoRecorder(){

        if (mCamera==null) return false;
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        mMediaRecorder.setOutputFile(getOutputMediaFile().toString());
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        try {
            mMediaRecorder.setOrientationHint(90);
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }



    /** Create a File for saving an image or video */
    private File getOutputMediaFile(){
        fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
        return  new File(getContext().getExternalCacheDir().getAbsolutePath() + "/" + fileName);
    }

    private boolean isLongEnough(){
        return Calendar.getInstance().getTimeInMillis() - time > 3000;
    }
}
