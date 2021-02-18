package com.example.puzzlegame;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.puzzlegame.game.MainActivity;
import com.example.puzzlegame.game.MainView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Albums extends Activity {
    private static final Uri IMAGE_URI = Uri.parse("/sdcard/Pictures");;
    private ImageView albumsPicture;
    public static final int CHOOSE_PHOTO = 2;
    private Button pestDection = null;
    private Button pictureSave = null;
    private Intent intent2;
    private Albums HexOneToOneBitmapUtil;
    private MediaScannerConnection mMediaonnection;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums);
        pestDection = super.findViewById(R.id.pestDetection);
        pictureSave = super.findViewById(R.id.pictureSave);
        albumsPicture = super.findViewById(R.id.picture);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏通知栏

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO);
        } else {
            openAlbum();
        }

        intent2 = new Intent(getApplicationContext(), picture.class);
        //receivePicturefromMainActivaty();
        pestDection.setOnClickListener(new pestDectionFuntion());
        pictureSave.setOnClickListener(new pictureSaveFunction());

    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }

    private class pestDectionFuntion implements View.OnClickListener {
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(), "粮虫检测", Toast.LENGTH_SHORT).show();
        }
    }

    private class pictureSaveFunction implements View.OnClickListener {
        public void onClick(View view) {
            //Toast.makeText(getApplicationContext(),"图片保存成功！",Toast.LENGTH_SHORT).show();
            //Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);//创建窗口切换的Intent,MainActivity.class指切换到主界面
            //Bitmap savepicture=loadBitmapFromView(albumsPicture);
            //String name=String.valueOf(System.currentTimeMillis());
            BitmapDrawable bmpDrawable = (BitmapDrawable) albumsPicture.getDrawable();
            Bitmap bitmap = bmpDrawable.getBitmap();
            //saveToSystemGallery(bitmap);//将图片保存到本地
            //Toast.makeText(getApplicationContext(),"图片保存成功！",Toast.LENGTH_SHORT).show();
            //startActivity(intent2);//窗口切换
            setContentView(new MainView(Albums.this, bitmap, 3));
        }
    }

    public void saveToSystemGallery(Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "MyAlbums");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), fileName, null);//没保存系统图库成功
            Log.i("成功", file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("失败", file.getAbsolutePath());
        }

        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent); // 发送广播，通知图库更新
    }


    // 使用startActivityForResult()方法开启Intent的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                //相册照片
                if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && null != data) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitkat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //根据图片路径显示图片
        displayImage(imagePath);
        //Log.i("保存地址", imagePath);
        //Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        //Log.i("原地址",imagePath);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        PermisssionUtil.requestPermission(Albums.this);

        if (imagePath != null) {
            /*BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;

            Bitmap bitmap;
            BitmapFactory.decodeFile(imagePath, opts);
            opts.inSampleSize = 4;
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(imagePath, opts);
            //将图片放置在控件上
            albumsPicture.setImageBitmap(bitmap);*/
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            //Toast.makeText(this, "得到图片成功", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
            /*DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth=dm.widthPixels;
            if(bm.getWidth()<=screenWidth){
                albumsPicture.setImageBitmap(bm);
            }else{
                Bitmap bmp=Bitmap.createScaledBitmap(bm, screenWidth, bm.getHeight()*screenWidth/bm.getWidth(), true);
                albumsPicture.setImageBitmap(bmp);
            }*/
            albumsPicture.setImageBitmap(bm);
        } else {
            Toast.makeText(this, "得到图片失败", Toast.LENGTH_SHORT).show();
        }
    }
    public static class PermisssionUtil {

        // Storage Permissions
        private static final int REQUEST_EXTERNAL_STORAGE = 1;
        private static String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        //android6.0之后要动态获取权限
        public static void requestPermission(Activity activity) {
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //检测是否有写的权限
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        }
    }
}