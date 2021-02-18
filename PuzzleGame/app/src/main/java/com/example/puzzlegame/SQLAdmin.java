package com.example.puzzlegame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puzzlegame.Helper.PaperDBHelper;
import com.example.puzzlegame.Info.OptionInfo;
import com.example.puzzlegame.Info.QuestionInfo;
import com.example.puzzlegame.game.MainActivity;

import java.util.ArrayList;
import java.util.Map;

public class SQLAdmin extends AppCompatActivity {
    private PaperDBHelper mHelper = new PaperDBHelper(this);
    private ArrayList<QuestionInfo> Q = new ArrayList<QuestionInfo>();
    private ArrayList<OptionInfo> O =new ArrayList<OptionInfo>();
    private String ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_l_admin);
        mHelper =PaperDBHelper.getInstance(this, 2);
        mHelper.openWriteLink();
        mHelper.openReadLink();
        initSpinner();

        Button insert = findViewById(R.id.item_add);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mHelper.closeLink();
    }
    private void initSpinner(){
        //声明一个数组适配器
        Q = mHelper.query_Q("paper_id = 1");

        String str = "";
        for(int i = 0; i < Q.size(); i++)
            str = str + String.valueOf(i+1) + "." + Q.get(i).getQuestion()+" ";
        String[] starArray = str.split(" ");
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(this, R.layout.item, R.id.item_question, starArray);
        ListView list = findViewById(R.id.list_question);
        list.setAdapter(starAdapter);
        //注册上下文菜单
        registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                O = mHelper.query_O("Option_id = '" + Q.get(position).get_id() + "'");
                String result = parent.getItemAtPosition(position).toString();//获取选择项的值
                //Toast.makeText(SQLAdmin.this,"您点击了"+result,Toast.LENGTH_SHORT).show();
                //修改单词
                new AlertDialog.Builder(SQLAdmin.this)
                        .setTitle(Q.get(position).getQuestion())//标题
                        // 确定按钮及其动作
                        .setMessage("\nA."+O.get(0).getContent()+"\nB."+O.get(1).getContent()
                                +"\nC."+O.get(2).getContent()+"\nD."+O.get(3).getContent()+"\n正确答案为"+Q.get(position).getType())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        //取消按钮及其动作
                        .create()//创建对话框
                        .show();//显示对话框
            }
        });

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView question=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        EditText test=null;
        switch (item.getItemId()){
            case R.id.action_delete:
                //删除单词
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                question = (TextView)itemView.findViewById(R.id.item_question);
                if(question!=null){
                    String str = question.getText().toString();
                    str = str.substring(2);
                   delete(str);
                }
                break;
            case R.id.action_update:
                //修改单词
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                question = (TextView)itemView.findViewById(R.id.item_question);
                if(question!=null){
                    String str = question.getText().toString();
                    str = str.substring(2);
                    update(str);
                }
                break;
        }
        return true;
    }

    public void delete(String question) {
        mHelper.delete("question = '"+ question +"'", "question_info");
        initSpinner();
    }

    public void update(String str) {
        Q = mHelper.query_Q("question = '" + str + "'");
        O = mHelper.query_O("Option_id = '" + Q.get(0).get_id() + "'");
        final RelativeLayout tableLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.insert, null);
        RadioGroup rg = tableLayout.findViewById(R.id.txt_ans);
        initRg(rg);
        //设置单选组被选择的框
        if(Q.get(0).getType().equals("B")){
            rg.check(R.id.ans_B);
        }
        else if(Q.get(0).getType().equals("C")){
            rg.check(R.id.ans_C);
        }
        else if(Q.get(0).getType().equals("D")){
            rg.check(R.id.ans_D);
        }
        EditText txt_q = ((EditText)tableLayout.findViewById(R.id.txt_question));
        EditText txt_a = ((EditText)tableLayout.findViewById(R.id.txt_A));
        EditText txt_b = ((EditText)tableLayout.findViewById(R.id.txt_B));
        EditText txt_c = ((EditText)tableLayout.findViewById(R.id.txt_C));
        EditText txt_d = ((EditText)tableLayout.findViewById(R.id.txt_D));
        txt_q.setText(Q.get(0).getQuestion());
        txt_a.setText(O.get(0).getContent());
        txt_b.setText(O.get(1).getContent());
        txt_c.setText(O.get(2).getContent());
        txt_d.setText(O.get(3).getContent());
        new AlertDialog.Builder(this)
                .setTitle("修改问题")//标题
                .setView(tableLayout)//设置视图
                // 确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String str_question = ((EditText) tableLayout.findViewById(R.id.txt_question)).getText().toString();
                        String str_A = ((EditText) tableLayout.findViewById(R.id.txt_A)).getText().toString();
                        String str_B = ((EditText) tableLayout.findViewById(R.id.txt_B)).getText().toString();
                        String str_C = ((EditText) tableLayout.findViewById(R.id.txt_C)).getText().toString();
                        String str_D = ((EditText) tableLayout.findViewById(R.id.txt_D)).getText().toString();

                        Q.get(0).setQuestion(str_question);
                        Q.get(0).setType(ans);
                        mHelper.update_Q(Q.get(0),"question = '" + str + "'");

                        O.get(0).setContent(str_A);
                        mHelper.update_O(O.get(0),"_id = '" + O.get(0).get_id() + "'");
                        O.get(1).setContent(str_B);
                        mHelper.update_O(O.get(1),"_id = '" + O.get(1).get_id() + "'");
                        O.get(2).setContent(str_C);
                        mHelper.update_O(O.get(2),"_id = '" + O.get(2).get_id() + "'");
                        O.get(3).setContent(str_D);
                        mHelper.update_O(O.get(3),"_id = '" + O.get(3).get_id() + "'");
                        initSpinner();
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

    //新增对话框
    private void InsertDialog() {
        final RelativeLayout tableLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.insert, null);
        RadioGroup rg = tableLayout.findViewById(R.id.txt_ans);
        initRg(rg);
        new AlertDialog.Builder(this)
                .setTitle("新增问题")//标题
                .setView(tableLayout)//设置视图
                // 确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str_question = ((EditText)tableLayout.findViewById(R.id.txt_question)).getText().toString();
                        String str_type = ans;
                        String str_A = ((EditText)tableLayout.findViewById(R.id.txt_A)).getText().toString();
                        String str_B = ((EditText)tableLayout.findViewById(R.id.txt_B)).getText().toString();
                        String str_C = ((EditText)tableLayout.findViewById(R.id.txt_C)).getText().toString();
                        String str_D = ((EditText)tableLayout.findViewById(R.id.txt_D)).getText().toString();

                        String sql="insert into  question_info(question,type,paper_id) values(?,?,?)";
                        //Gets the data repository in write mode*/
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        db.execSQL(sql,new String[]{str_question,str_type,"1"});

                        Long option_id = mHelper.query_Q("question = '" + str_question + "'").get(0).get_id();

                        sql="insert into  option_info(value,content,option_id) values(?,?,?)";
                        //Gets the data repository in write mode*/
                        db = mHelper.getWritableDatabase();
                        db.execSQL(sql,new String[]{"A", str_A, option_id.toString()});

                        sql="insert into  option_info(value,content,option_id) values(?,?,?)";
                        //Gets the data repository in write mode*/
                        db = mHelper.getWritableDatabase();
                        db.execSQL(sql,new String[]{"B", str_B, option_id.toString()});

                        sql="insert into  option_info(value,content,option_id) values(?,?,?)";
                        //Gets the data repository in write mode*/
                        db = mHelper.getWritableDatabase();
                        db.execSQL(sql,new String[]{"C", str_C, option_id.toString()});

                        sql="insert into  option_info(value,content,option_id) values(?,?,?)";
                        //Gets the data repository in write mode*/
                        db = mHelper.getWritableDatabase();
                        db.execSQL(sql,new String[]{"D", str_D, option_id.toString()});
                        initSpinner();
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

    public void initRg(RadioGroup rg) {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ans_A:
                        ans="A";
                        break;
                    case R.id.ans_B:
                        ans="B";
                        break;
                    case R.id.ans_C:
                        ans="C";
                        break;
                    case R.id.ans_D:
                        ans="D";
                        break;
                }
            }
        });
    }
}