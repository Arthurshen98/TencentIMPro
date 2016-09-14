package cn.heren.com.impro.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMImage;
import com.tencent.TIMImageElem;
import com.tencent.TIMImageType;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import java.io.IOException;

import cn.heren.com.impro.MyApplication;
import cn.heren.com.impro.R;
import cn.heren.com.impro.util.FileUtil;
import cn.heren.com.impro.view.activity.ImageViewActivity;
import cn.heren.com.impro.view.adapter.ChatAdapter;

/**
 * 图片消息数据
 */
public class ImageMessage extends Message {

    private static final String TAG = "ImageMessage";
    private boolean isDownloading;

    public ImageMessage(TIMMessage message){
        this.message = message;
    }

    public ImageMessage(String path){
        this(path, false);
    }

    /**
     * 图片消息构造函数
     *
     * @param path 图片路径
     * @param isOri 是否原图发送
     */
    public ImageMessage(String path, boolean isOri){
        message = new TIMMessage();
        TIMImageElem elem = new TIMImageElem();
        elem.setPath(path);
        elem.setLevel(isOri?0:1);
        message.addElement(elem);
    }


    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context 显示消息的上下文
     */
    @Override
    public void showMessage(final ChatAdapter.ViewHolder viewHolder, final Context context) {
        clearView(viewHolder);
        TIMImageElem e = (TIMImageElem) message.getElement(0);
        switch (message.status()){
            case Sending:

                ImageView imageView = new ImageView(MyApplication.getContext());
                imageView.setImageBitmap(getThumb(e.getPath()));
                clearView(viewHolder);
                getBubbleView(viewHolder).addView(imageView);
                break;
            case SendSucc:
                for(final TIMImage image : e.getImageList()) {
                    if (image.getType() == TIMImageType.Thumb){
                        final String uuid = image.getUuid();
                        if (FileUtil.isCacheFileExist(uuid)){
                            showThumb(viewHolder,uuid);
                        }else{
                            image.getImage(new TIMValueCallBack<byte[]>() {
                                @Override
                                public void onError(int code, String desc) {//获取图片失败
                                    //错误码code和错误描述desc，可用于定位请求失败原因
                                    //错误码code含义请参见错误码表
                                    Log.e(TAG, "getImage failed. code: " + code + " errmsg: " + desc);
                                }

                                @Override
                                public void onSuccess(byte[] data) {//成功，参数为图片数据
                                    FileUtil.createFile(data, uuid);
                                    showThumb(viewHolder,uuid);
                                }
                            });
                        }
                    }
                    if (image.getType() == TIMImageType.Original){
                        final String uuid = image.getUuid();
//                        setImageEvent(viewHolder, uuid,context);
                        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                navToImageview(image, context);
                            }
                        });
                    }
                }
                break;
        }
        showStatus(viewHolder);


    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        return MyApplication.getContext().getString(R.string.summary_image);
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {
        final TIMImageElem e = (TIMImageElem) message.getElement(0);
        for(TIMImage image : e.getImageList()) {
            if (image.getType() == TIMImageType.Original) {
                final String uuid = image.getUuid();
                image.getImage(new TIMValueCallBack<byte[]>() {
                    @Override
                    public void onError(int i, String s) {
                        Log.e(TAG, "getFile failed. code: " + i + " errmsg: " + s);
                    }

                    @Override
                    public void onSuccess(byte[] bytes) {
                        if (FileUtil.createFile(bytes, uuid+".jpg", Environment.DIRECTORY_DOWNLOADS)) {
                            Toast.makeText(MyApplication.getContext(), MyApplication.getContext().getString(R.string.save_succ), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyApplication.getContext(), MyApplication.getContext().getString(R.string.save_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    /**
     * 生成缩略图
     * 缩略图是将原图等比压缩，压缩后宽、高中较小的一个等于198像素
     * 详细信息参见文档
     */
    private Bitmap getThumb(String path){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width=options.outWidth, height=options.outHeight;
        if (width > height){
            reqWidth = 198;
            reqHeight = (reqWidth * height)/width;
        }else{
            reqHeight = 198;
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

    private void showThumb(final ChatAdapter.ViewHolder viewHolder,String filename){
        Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getCacheFilePath(filename));
        ImageView imageView = new ImageView(MyApplication.getContext());
        imageView.setImageBitmap(bitmap);
        getBubbleView(viewHolder).addView(imageView);
    }

    private void setImageEvent(final ChatAdapter.ViewHolder viewHolder, final String fileName, final Context context){
        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("filename", fileName);
                context.startActivity(intent);
            }
        });
    }

    private void navToImageview(final TIMImage image, final Context context){
        if (FileUtil.isCacheFileExist(image.getUuid())){
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("filename", image.getUuid());
            context.startActivity(intent);
        }else{
            if (!isDownloading){
                isDownloading = true;
                image.getImage(FileUtil.getCacheFilePath(image.getUuid()), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code含义请参见错误码表
                        Log.e(TAG, "getImage failed. code: " + i + " errmsg: " + s);
                        Toast.makeText(context, MyApplication.getContext().getString(R.string.download_fail), Toast.LENGTH_SHORT).show();
                        isDownloading = false;
                    }

                    @Override
                    public void onSuccess() {
                        isDownloading = false;
                        Intent intent = new Intent(context, ImageViewActivity.class);
                        intent.putExtra("filename", image.getUuid());
                        context.startActivity(intent);
                    }
                });
            }else{
                Toast.makeText(context, MyApplication.getContext().getString(R.string.downloading), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
