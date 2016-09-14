package cn.heren.com.impro.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

import cn.heren.com.impro.R;
import cn.heren.com.impro.base.BaseActivity;
import cn.heren.com.impro.util.FileUtil;

/**
 * 主要是用于查看图片
 */
public class ImageViewActivity extends BaseActivity {

    private RelativeLayout root;
  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_view);
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String file = getIntent().getStringExtra("filename");
        ImageView imageView = (ImageView) findViewById(R.id.image);
        Bitmap bitmap = getImage(FileUtil.getCacheFilePath(file));
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
    }*/

    @Override
    protected void loadActivityLayout() {
        setContentView(R.layout.activity_image_view);
        setTitleBarIsShow(false);
        setBackIconResource(R.color.black);
    }

    @Override
    protected void findId() {
         root = (RelativeLayout) findViewById(R.id.root);
    }

    @Override
    protected void addOnListener() {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void processDatas() {
        String file = getIntent().getStringExtra("filename");
        ImageView imageView = (ImageView) findViewById(R.id.image);
        Bitmap bitmap = getImage(FileUtil.getCacheFilePath(file));
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap getImage(String path){
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
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            if (bitmap == null) {
                Toast.makeText(this, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
                return null;
            }
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
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        }catch (IOException e){
            return null;
        }
    }
}
