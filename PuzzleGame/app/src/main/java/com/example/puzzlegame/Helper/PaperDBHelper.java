package com.example.puzzlegame.Helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.puzzlegame.Info.AnswerInfo;
import com.example.puzzlegame.Info.OptionInfo;
import com.example.puzzlegame.Info.PaperInfo;
import com.example.puzzlegame.Info.QuestionInfo;

import java.util.ArrayList;

public class PaperDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "puzzle.db";
    private static final int DB_VERSION= 1;
    private static PaperDBHelper mHelper = null;
    private SQLiteDatabase mDB = null;
    public static final String TABLE_1 = "paper_info";
    public static final String TABLE_2 = "question_info";
    public static final String TABLE_3 = "option_info";
    public static final String TABLE_4 = "answer_info";

    public PaperDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private PaperDBHelper(Context context, int version){
        super(context, DB_NAME, null,version);
    }

    //利用单例模式获取数据库帮助器的唯一实例
    public static PaperDBHelper getInstance(Context context, int version){
        if(version > 0 && mHelper == null){
            mHelper = new PaperDBHelper(context, version);
        } else if(mHelper == null){
            mHelper = new PaperDBHelper(context);
        }
        return mHelper;
    }

    //打开数据库的读连接
    public SQLiteDatabase openReadLink(){
        if(mDB == null || !mDB.isOpen()){
            mDB = mHelper.getReadableDatabase();
        }
        return mDB;
    }

    //打开数据库的写连接
    public SQLiteDatabase openWriteLink(){
        if(mDB == null || mDB.isOpen()){
            mDB = mHelper.getWritableDatabase();
        }
        return mDB;
    }

    //关闭数据库连接
    public void closeLink(){
        if(mDB == null || !mDB.isOpen()){
            mDB.close();
            mDB = null;
        }
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String drop1_sql = "DROP TABLE IF EXISTS " + TABLE_1 + ";";
        sqLiteDatabase.execSQL(drop1_sql);
        String create1_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_1 + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "title VARCHAR NOT NULL," + "preface VARCHAR NOT NULL" + ")";
        sqLiteDatabase.execSQL(create1_sql);

        String drop2_sql = "DROP TABLE IF EXISTS " + TABLE_2 + ";";
        sqLiteDatabase.execSQL(drop2_sql);
        String create2_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_2 + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "question VARCHAR NOT NULL," + "type VARCHAR NOT NULL,"
                + "paper_id INTEGER,"
                + "FOREIGN KEY(paper_id) REFERENCES paper_info(_id))";
        sqLiteDatabase.execSQL(create2_sql);

        String drop3_sql = "DROP TABLE IF EXISTS " + TABLE_3 + ";";
        sqLiteDatabase.execSQL(drop3_sql);
        String create3_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_3 + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "value VARCHAR NOT NULL," + "content VARCHAR NOT NULL,"
                + "option_id INTEGER,"
                + "FOREIGN KEY(option_id) REFERENCES question_info(_id))";
        sqLiteDatabase.execSQL(create3_sql);

        String drop4_sql = "DROP TABLE IF EXISTS " + TABLE_4 + ";";
        sqLiteDatabase.execSQL(drop4_sql);
        String create4_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_4 + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "paper_id INTEGER,"
                + "question_id INTEGER,"
                + "answer_value VARCHAR NOT NULL,"
                + "FOREIGN KEY(paper_id) REFERENCES paper_info(_id),"
                + "FOREIGN KEY(question_id) REFERENCES question_info(_id))";
        sqLiteDatabase.execSQL(create4_sql);
    }

    //修改数据库，执行表结构变更语句
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){ }

    //根据指定条件删除表记录
    public int delete(String condition, String table){
        //执行删除记录动作，该语句返回删除记录的数目
        return mDB.delete(table, condition, null);
    }

    //往表里添加多条记录
    public long insert_P(ArrayList<PaperInfo> infoArray){
        long result = -1;
        for(int i = 0; i < infoArray.size(); i++){
            PaperInfo info = infoArray.get(i);
            //不存在唯一性重复的记录，则插入新纪录
            ContentValues cv = new ContentValues();
            cv.put("_id",info.get_id());
            cv.put("title",info.getTitle());
            cv.put("preface",info.getPreface());
            //执行插入记录动作，该语句返回插入记录的行号
            result = mDB.insert(TABLE_1,"", cv);
            //添加成功返回行号，失败后返回-1
            if(result == -1)
                return result;
        }
        return result;
    }

    public long insert_Q(ArrayList<QuestionInfo> infoArray){
        long result = -1;
        for(int i = 0; i < infoArray.size(); i++){
            QuestionInfo info = infoArray.get(i);
            ContentValues cv = new ContentValues();
            cv.put("_id", info.get_id());
            cv.put("question", info.getQuestion());
            cv.put("paper_id", info.getPaper_id());
            cv.put("type", info.getType());
            //执行插入记录动作，该语句返回插入记录的行号
            result = mDB.insert(TABLE_2,"", cv);
            //添加成功返回行号，失败后返回-1
            if(result == -1)
                return result;
        }
        return result;
    }

    public long insert_O(ArrayList<OptionInfo> infoArray){
        long result = -1;
        for(int i = 0; i < infoArray.size(); i++){
            OptionInfo info = infoArray.get(i);
            ContentValues cv = new ContentValues();
            cv.put("_id", info.get_id());
            cv.put("value", info.getValue());
            cv.put("content", info.getContent());
            cv.put("option_id", info.getOption_id());
            //执行插入记录动作，该语句返回插入记录的行号
            result = mDB.insert(TABLE_3,"", cv);
            //添加成功返回行号，失败后返回-1
            if(result == -1)
                return result;
        }
        return result;
    }

    public long insert_A(ArrayList<AnswerInfo> infoArray){
        long result = -1;
        for(int i = 0; i < infoArray.size(); i++){
            AnswerInfo info = infoArray.get(i);
            ContentValues cv = new ContentValues();
            cv.put("paper_id", info.getPaper_id());
            cv.put("question_id", info.getQuestion_id());
            cv.put("answer_value", info.getAnswer_value());
            //执行插入记录动作，该语句返回插入记录的行号
            result = mDB.insert(TABLE_4,"", cv);
            //添加成功返回行号，失败后返回-1
            if(result == -1)
                return result;
        }
        return result;
    }

    //根据条件更新指定的表记录
    public int update_P(PaperInfo info, String condition){
        ContentValues cv = new ContentValues();
        cv.put("_id",info.get_id());
        cv.put("title",info.getTitle());
        cv.put("preface",info.getPreface());
        //执行更新记录动作，该语句返回记录更新的数目
        return mDB.update(TABLE_1, cv, condition, null);
    }

    public int update_Q(QuestionInfo info, String condition){
        ContentValues cv = new ContentValues();
        cv.put("_id",info.get_id());
        cv.put("question",info.getQuestion());
        cv.put("type",info.getType());
        cv.put("paper_id",info.getPaper_id());
        //执行更新记录动作，该语句返回记录更新的数目
        return mDB.update(TABLE_2, cv, condition, null);
    }
    public int update_O(OptionInfo info, String condition){
        ContentValues cv = new ContentValues();
        cv.put("_id",info.get_id());
        cv.put("value",info.getValue());
        cv.put("content",info.getContent());
        cv.put("option_id",info.getOption_id());
        //执行更新记录动作，该语句返回记录更新的数目
        return mDB.update(TABLE_3, cv, condition, null);
    }

    public int update_A(AnswerInfo info, String condition){
        ContentValues cv = new ContentValues();
        cv.put("_id",info.get_id());
        cv.put("paper_id",info.getPaper_id());
        cv.put("question_id",info.getQuestion_id());
        cv.put("answer_value",info.getAnswer_value());
        //执行更新记录动作，该语句返回记录更新的数目
        return mDB.update(TABLE_4, cv, condition, null);
    }

    //根据指定条件查询记录，并返回结果数据队列
    public ArrayList<PaperInfo> query_P(String condition){
        String sql = String.format("select * "
                + "from %s where %s",TABLE_1,condition);
        ArrayList<PaperInfo> infoArray = new ArrayList<PaperInfo>();
        //执行记录查询动作，该语句返回查询结果集的游标
        Cursor cursor = mDB.rawQuery(sql, null);
        //循环取除游标指向的每条记录
        while(cursor.moveToNext()){
            PaperInfo info = new PaperInfo();
            info.set_id(cursor.getLong(0));
            info.setTitle(cursor.getString(1));
            info.setPreface(cursor.getString(2));
            infoArray.add(info);
        }
        cursor.close();
        return infoArray;
    }

    public ArrayList<QuestionInfo> query_Q(String condition){
        String sql = String.format("select * "
                + "from %s where %s", TABLE_2, condition);
        ArrayList<QuestionInfo> infoArray = new ArrayList<QuestionInfo>();
        //执行记录查询动作，该语句返回查询结果集的游标
        Cursor cursor = mDB.rawQuery(sql, null);
        //循环取除游标指向的每条记录
        while(cursor.moveToNext()){
            QuestionInfo info = new QuestionInfo();
            info.set_id(cursor.getLong(0));
            info.setQuestion(cursor.getString(1));
            info.setType(cursor.getString(2));
            info.setPaper_id(cursor.getLong(3));
            infoArray.add(info);
        }
        cursor.close();
        return infoArray;
    }

    public ArrayList<OptionInfo> query_O(String condition){
        String sql = String.format("select * "
                + "from %s where %s", TABLE_3, condition);
        ArrayList<OptionInfo> infoArray = new ArrayList<OptionInfo>();
        //执行记录查询动作，该语句返回查询结果集的游标
        Cursor cursor = mDB.rawQuery(sql, null);
        //循环取除游标指向的每条记录
        while(cursor.moveToNext()){
            OptionInfo info = new OptionInfo();
            info.set_id(cursor.getLong(0));
            info.setValue(cursor.getString(1));
            info.setContent(cursor.getString(2));
            info.setOption_id(cursor.getLong(3));
            infoArray.add(info);
        }
        cursor.close();
        return infoArray;
    }

    public ArrayList<AnswerInfo> query_A(String condition){
        String sql = String.format("select * "
                + "from %s where %s", TABLE_4, condition);
        ArrayList<AnswerInfo> infoArray = new ArrayList<AnswerInfo>();
        //执行记录查询动作，该语句返回查询结果集的游标
        Cursor cursor = mDB.rawQuery(sql, null);
        //循环取除游标指向的每条记录
        while(cursor.moveToNext()){
            AnswerInfo info = new AnswerInfo();
            info.set_id(cursor.getLong(0));
            info.setPaper_id(cursor.getLong(1));
            info.setQuestion_id(cursor.getLong(2));
            info.setAnswer_value(cursor.getString(3));
            infoArray.add(info);
        }
        cursor.close();
        return infoArray;
    }
}
