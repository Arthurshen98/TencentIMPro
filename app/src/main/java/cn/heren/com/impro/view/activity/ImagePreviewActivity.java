package cn.heren.com.impro.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.util.StatusBarUtil;

public class ImagePreviewActivity extends Activity implements View.OnClickListener {

    private String path;
    private RadioButton isOri;
    private ImageView iv_preview_back;
    private TextView tv_preview_right;
    private StatusBarUtil statusBarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_preview);
        setStatusBar(); //设置沉浸式状态栏
        iv_preview_back = (ImageView) findViewById(R.id.iv_preview_back);
        tv_preview_right = (TextView) findViewById(R.id.tv_preview_right);
        iv_preview_back.setOnClickListener(this);
        tv_preview_right.setOnClickListener(this);

        path = getIntent().getStringExtra("path");
       // isOri = (RadioButton) findViewById(R.id.isOri);

        showImage();
    }

    private void showImage(){
        if (path.equals("")) return;
        File file = new File(path);
        if (file.exists()&&file.length()>0){
           // isOri.setText(getString(R.string.chat_image_preview_ori) + "(" + getFileSize(file.length()) + ")");
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int reqWidth, reqHeight, width=options.outWidth, height=options.outHeight;
            if (width > height){
                reqWidth = getWindowManager().getDefaultDisplay().getWidth();
                reqHeight = (reqWidth * height)/width;
            }else{
                reqHeight = getWindowManager().getDefaultDisplay().getHeight();
                reqWidth = (width * reqHeight)/height;
            }
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            try{
                options.inSampleSize = inSampleSize;
                options.inJustDecodeBounds = false;
                float scaleX = (float) reqWidth / (float) (width/inSampleSize);
                float scaleY = (float) reqHeight / (float) (height/inSampleSize);
                Matrix mat = new Matrix();
                mat.postScale(scaleX, scaleY);
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                ExifInterface ei =  new ExifInterface(path);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        mat.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        mat.postRotate(180);
                        break;
                }
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true));
            }catch (IOException e){
                Toast.makeText(this, getString(R.string.chat_image_preview_load_err), Toast.LENGTH_SHORT).show();
            }
        }else{
            finish();
        }
    }

    private String getFileSize(long size){
        StringBuilder strSize = new StringBuilder();
        if (size < 1024){
            strSize.append(size).append("B");
        }else if (size < 1024*1024){
            strSize.append(size/1024).append("K");
        }else{
            strSize.append(size/1024/1024).append("M");
        }
        return strSize.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_preview_back:
                finish();
                break;
            case R.id.tv_preview_right:
                Intent intent = new Intent();
                intent.putExtra("path", path);
                intent.putExtra("isOri", false);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }
        statusBarUtil = new StatusBarUtil(this);
        statusBarUtil.setStatusBarTintEnabled(true);
        statusBarUtil.setStatusBarTintResource(R.color.colorAccent);
        setStatusBarColor(getResources().getColor(R.color.colorAccent));// 主色
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 针对5.0及以上版本设置[状态栏][StatusBar]的背景颜色[可随时使用]
     *
     * @param color
     */
    protected void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Logger.e("VERSION_CODES >= LOLLIPOP");
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            // window.setNavigationBarColor(Color.TRANSPARENT);// 针对5.0及以上版本设置[虚拟导航栏][NavigationBar]的背景颜色
        }
    }

}
