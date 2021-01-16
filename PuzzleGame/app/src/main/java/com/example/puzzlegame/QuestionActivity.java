package com.example.puzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.puzzlegame.Helper.PaperDBHelper;
import com.example.puzzlegame.Info.AnswerInfo;
import com.example.puzzlegame.Info.OptionInfo;
import com.example.puzzlegame.Info.PaperInfo;
import com.example.puzzlegame.Info.QuestionInfo;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    private PaperDBHelper mHelper = new PaperDBHelper(this);
    private PaperInfo info_P = new PaperInfo();
    private QuestionInfo info_Q = new QuestionInfo();
    private OptionInfo info_O = new OptionInfo();
    private AnswerInfo info_A = new AnswerInfo();
    ArrayList<PaperInfo> infoArray_P = new ArrayList<PaperInfo>();
    ArrayList<QuestionInfo> infoArray_Q = new ArrayList<QuestionInfo>();
    ArrayList<OptionInfo> infoArray_O = new ArrayList<OptionInfo>();
    ArrayList<AnswerInfo> infoArray_A = new ArrayList<AnswerInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //获取SharedPreferences参数
        //SharedPreferences shared = getSharedPreferences("share",MODE_PRIVATE);
        //String name = shared.getString("name","";
        /*info_P.set_id(1);
        info_P.setTitle("问卷一2.0");
        info_P.setPreface("完成问卷以进行拼图");
        infoArray_P.add(info_P);

        info_Q.set_id(1);
        info_Q.setPaper_id(1);
        info_Q.setType("单选");
        info_Q.setQuestion("你喜欢拼图么？2.0");
        infoArray_Q.add(info_Q);

        info_O.set_id(1);
        info_O.setContent("喜欢2.0");
        info_O.setValue("A");
        info_O.setOption_id(1);
        infoArray_O.add(info_O);*/

        /*info_A.set_id(1);
        info_A.setPaper_id(1);
        info_A.setAnswer_value("B");
        info_A.setQuestion_id(1);
        infoArray_A.add(info_A);*/
    }

    //在App生命周期的onStart方法中初始化数据库，并打开写入
    @Override
    protected void onStart() {
        super.onStart();
        mHelper =PaperDBHelper.getInstance(this, 2);
        mHelper.openWriteLink();
        mHelper.openReadLink();

        /*mHelper.update_P(info_P, "_id = 1");
        mHelper.update_Q(info_Q, "_id = 1");
        mHelper.update_O(info_O, "_id = 1");*/
        /*Long insert = mHelper.insert_P(infoArray_P);
        if(insert == -1)
            Log.i("insert","插入问卷失败");
        else
            Log.i("insert","插入问卷成功");

        insert = mHelper.insert_Q(infoArray_Q);
        if(insert == -1)
            Log.i("insert","插入问题失败");
        else
            Log.i("insert","插入问题成功");

        insert = mHelper.insert_O(infoArray_O);
        if(insert == -1)
            Log.i("insert","插入选项失败");
        else
            Log.i("insert","插入选项成功");

        insert = mHelper.insert_A(infoArray_A);
        if(insert == -1)
            Log.i("insert","插入答案失败");
        else
            Log.i("insert","插入答案成功");*/

        TextView text1 = findViewById(R.id.question1);
        text1.setText(String.format(mHelper.query_P("_id = 1").get(0).getTitle()));

        TextView text2 = findViewById(R.id.question2);
        text2.setText(String.format(mHelper.query_Q("_id = 1").get(0).getQuestion()));

        TextView text3 = findViewById(R.id.question3);
        text3.setText(String.format(mHelper.query_O("_id = 1").get(0).getValue()) + "." + String.format(mHelper.query_O("_id = 1").get(0).getContent()));

        TextView text4 = findViewById(R.id.question4);
        text4.setText(String.format(mHelper.query_A("_id = 1").get(0).getAnswer_value()));
    }

    //在App生命周期的onStop方法中关闭数据库
    @Override
    protected void onStop() {
        super.onStop();
        mHelper.closeLink();
    }
}