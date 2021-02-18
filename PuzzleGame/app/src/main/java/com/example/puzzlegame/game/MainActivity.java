package com.example.puzzlegame.game;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.puzzlegame.PuzzGame;
import com.example.puzzlegame.R;
import com.example.puzzlegame.SQLAdmin;
import com.example.puzzlegame.picture;

public class MainActivity extends AppCompatActivity {

    private static   int screenWidth;
    private static int screenHeight;
    private  int level=3;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzz_game);

        View image1 = findViewById(R.id.image1);
        View image2 = findViewById(R.id.image2);
        Button button1 = findViewById(R.id.start);
        //共享参数
        /*SharedPreferences shared = getSharedPreferences("share",MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("name","Miss New");
        editor.commit();*/

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, PuzzGame.class);
                intent.putExtra("back", "back.jpg");
                intent.putExtra("level", String.valueOf(level));
                //Log.i("123",String.valueOf(level));
                startActivityForResult(intent,0);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,PuzzGame.class);
                intent.putExtra("back", "timg.jpg");
                intent.putExtra("level", String.valueOf(level));
                startActivityForResult(intent,0);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                //intent.setClass(MainActivity.this, QuestionActivity.class);
                intent.setClass(MainActivity.this, picture.class);
                startActivityForResult(intent,0);
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_set:
                set();
                return true;
            case R.id.sql_set:
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, SQLAdmin.class);
                startActivityForResult(intent,0);
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void set() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final View viewDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog,null,false);

        Button Text1 = viewDialog.findViewById(R.id.text1);
        Button Text2 = viewDialog.findViewById(R.id.text2);
        Button Text3 = viewDialog.findViewById(R.id.text3);

        Text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level=3;
                showInfo("难度3x3设置成功");
            }
        });
        Text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level=4;
                showInfo("难度4x4设置成功");
            }
        });
        Text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level=5;
                showInfo("难度5x5设置成功");
            }
        });

        builder.setTitle("设置难度")
                .setView(viewDialog)
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
    }
    private void showInfo(String info) {
        Toast.makeText(this,info,Toast.LENGTH_SHORT).show();
    }

}
