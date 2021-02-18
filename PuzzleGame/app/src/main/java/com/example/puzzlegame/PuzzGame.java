package com.example.puzzlegame;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.puzzlegame.game.MainActivity;
import com.example.puzzlegame.game.MainView;

public class PuzzGame extends AppCompatActivity {

    private static   int screenWidth;
    private static int screenHeight;
    MainView a;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        Intent intent = getIntent();
        String back = intent.getStringExtra("back");
        int level = Integer.valueOf(intent.getStringExtra("level"));
        a = new MainView(this,back,level);
        setContentView(a);
    }


    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    //返回键设置,解决异常退出游戏导致的定时器没有正常停止
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            a.stop();
            Intent intent=new Intent();
            intent.setClass(PuzzGame.this,MainActivity.class);
            startActivityForResult(intent,0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}