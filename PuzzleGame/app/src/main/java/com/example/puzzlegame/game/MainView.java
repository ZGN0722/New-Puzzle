package com.example.puzzlegame.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.puzzlegame.PuzzGame;
import com.example.puzzlegame.R;
import com.example.puzzlegame.util.MLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Currency;
import java.util.Timer;
import java.util.TimerTask;

public class MainView extends View {
    private int recLen = 0;
    Canvas canvas ;
    Timer timer = new Timer();

    private static final String TAG = MainView.class.getSimpleName();
    private Context context;
    private Bitmap back;
    private Paint paint;
    private int tileWidth;
    private int tileHeight;
    private Bitmap[] bitmapTiles;
    private int[][] dataTiles;
    private Board tilesBoard;
    private int COL = 3;
    private int ROW = 3;
    private int[][] dir = {
            {-1, 0},//左
            {0, -1},//上
            {1, 0},//右
            {0, 1}//下
    };
    private boolean isSuccess;

    int steps = 0;

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            recLen++;
            invalidate();
            //Log.i("Time",String.valueOf(recLen));
        }
    };

    public MainView(Context context,String image,int level) {
        super(context);
        this.COL=level;
        this.ROW=level;
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        init(image);
        startGame();
        MLog.d(TAG, PuzzGame.getScreenWidth() + "," +PuzzGame.getScreenHeight());
    }
    public MainView(Context context,Bitmap image,int level) {
        super(context);
        this.COL=level;
        this.ROW=level;
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        init(image);
        startGame();
        MLog.d(TAG, PuzzGame.getScreenWidth() + "," +PuzzGame.getScreenHeight());
    }


    private void init(String image) {

        AssetManager assetManager = context.getAssets();
        try {
            InputStream assetInputStream = assetManager.open(image);
            Bitmap bitmap = BitmapFactory.decodeStream(assetInputStream);
            back = Bitmap.createScaledBitmap(bitmap, 1080, 2085, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tileWidth = back.getWidth() / COL;
        tileHeight = back.getHeight() / ROW;
        bitmapTiles = new Bitmap[COL * ROW];
        int idx = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                bitmapTiles[idx++] = Bitmap.createBitmap(back, j * tileWidth, i * tileHeight, tileWidth, tileHeight);
            }
        }
    }
    private void init(Bitmap bitmap) {
        back = Bitmap.createScaledBitmap(bitmap, 1080, 2085, true);
        tileWidth = back.getWidth() / COL;
        tileHeight = back.getHeight() / ROW;
        bitmapTiles = new Bitmap[COL * ROW];
        int idx = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                bitmapTiles[idx++] = Bitmap.createBitmap(back, j * tileWidth, i * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    private void startGame() {
        timer.schedule(task, 1000, 1000);

        tilesBoard = new Board();
        dataTiles = tilesBoard.createRandomBoard(ROW, COL);
        isSuccess = false;
        steps = 0;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        this.canvas=canvas;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int idx = dataTiles[i][j];
                if (idx == ROW * COL - 1 && !isSuccess)
                    continue;
                Bitmap time = fromText(100, "时间:" + String.valueOf(recLen));
                canvas.drawBitmap(time,0,0, paint);
                Bitmap steps = fromText(100, "步数:" + String.valueOf(this.steps));
                canvas.drawBitmap(steps,0,100, paint);
                canvas.drawBitmap(bitmapTiles[idx], j * tileWidth, i * tileHeight, paint);
            }
        }
        if(recLen==5 || recLen==10) {
            timer.cancel();
            timer=null;
            task.cancel();
            task=null;
            final TableLayout tableLayout = (TableLayout) LayoutInflater.from(context).inflate(R.layout.insert, null);
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("新增问题")//标题
                    .setView(tableLayout)//设置视图
                    // 确定按钮及其动作
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            timer = new Timer();
                            task = new TimerTask() {
                                @Override
                                public void run() {
                                    recLen++;
                                    invalidate();
                                    //Log.i("Time",String.valueOf(recLen));
                                }
                            };
                            timer.schedule(task, 1000, 1000);
                        }
                    })
                    //取消按钮及其动作
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()//创建对话框
                    .show();//显示对话框
        }
    }

    private Point xyToIndex(int x, int y) {
        int extraX = x % tileWidth > 0 ? 1 : 0;
        int extraY = x % tileWidth > 0 ? 1 : 0;
        int col = x / tileWidth + extraX;
        int row = y / tileHeight + extraY;

        return new Point(col - 1, row - 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Point point = xyToIndex((int) event.getX(), (int) event.getY());

            for (int i = 0; i < dir.length; i++) {
                int newX = point.getX() + dir[i][0];
                int newY = point.getY() + dir[i][1];

                if (newX >= 0 && newX < COL && newY >= 0 && newY < ROW) {
                    if (dataTiles[newY][newX] == COL * ROW - 1) {
                        steps++;
                        int temp = dataTiles[point.getY()][point.getX()];
                        dataTiles[point.getY()][point.getX()] = dataTiles[newY][newX];
                        dataTiles[newY][newX] = temp;
                        invalidate();
                        if (tilesBoard.isSuccess(dataTiles)) {
                            timer.cancel();
                            isSuccess = true;
                            invalidate();
                            String successText = String.format("恭喜你拼图成功\n移动了%d次\n花费了%d秒", steps, recLen);
                            new AlertDialog.Builder(context)
                                    .setTitle("拼图成功")
                                    .setCancelable(false)
                                    .setMessage(successText)
                                    .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startGame();
                                        }
                                    })
                                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent();
                                            intent.setClass(getContext(), MainActivity.class);
                                            context.startActivity(intent);
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }
            }
        }
        return true;
    }

    private void printArray(int[][] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                sb.append(arr[i][j] + ",");
            }
            sb.append("\n");
        }
        MLog.d(TAG, sb.toString());
    }

    public static Bitmap fromText(float textSize, String text) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();

        int width = (int)paint.measureText(text);
        int height = fm.descent - fm.ascent;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, 0, fm.leading - fm.ascent, paint);
        canvas.save();

        return bitmap;
    }


}
