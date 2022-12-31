package com.example.coursetable_tomatobell.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.coursetable_tomatobell.util.Debug;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "courseTable_TomatoBell2.db";
    private static final String COURSE_TABLE = "course_info";
    private static final int DB_VERSION = 1;
    private static DataBaseHelper mHelper = null;
    private static SQLiteDatabase mRDB = null;
    private static SQLiteDatabase mWDB = null;


    private String create_course_table_statement =
            "CREATE TABLE IF NOT EXISTS " + COURSE_TABLE + "(" +
                    "id integer primary key autoincrement," +
                    "course_name text," +
                    "teacher text," +
                    "class_room text," +
                    "day integer," +
                    "class_start integer," +
                    "class_end integer);";

    private static String TASK_TABLE_NAME = "task_info";
    private static String TASK_COL0_ID = "id";
    private static String TASK_COL1_NAME = "task_name";
    private static String TASK_COL2_DES = "task_desc";
    private static String TASK_COL3_DATE = "task_date";
    private static String TASK_COL4_STATUS = "task_status";

    private String create_task_table_statement =
            "CREATE TABLE IF NOT EXISTS " + TASK_TABLE_NAME + "( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "task_name TEXT NOT NULL, " +
                    "task_desc TEXT, " +
                    "task_date TEXT, " +
                    "task_status INTEGER NOT NULL " +
                    ");";

    private static String TIME_TABLE_NAME = "time_info";
    private static String TIME_ID = "time_id";
    private static String TIME_DATE = "time_date";
    private static String TIME_MINUTES = "time_minutes";
    private static String TIME_COUNT = "time_count";

    private String create_time_table_statement =
            "CREATE TABLE IF NOT EXISTS " + TIME_TABLE_NAME + "( " +
                    TIME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TIME_DATE + " TEXT, " +
                    TIME_MINUTES + " INTEGER NOT NULL, " +
                    TIME_COUNT + " INTEGER NOT NULL" +
                    ");";

    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Debug.debug("数据库初始化");
    }

    static public DataBaseHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new DataBaseHelper(context);
        }
        return mHelper;

    }

    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    public void closeLink() {
        if (mWDB != null && mRDB.isOpen()) {
            mWDB.close();
        }
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();

        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Debug.debug("建表");
        sqLiteDatabase.execSQL(create_course_table_statement);
        sqLiteDatabase.execSQL(create_task_table_statement);
        sqLiteDatabase.execSQL(create_time_table_statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //返回结果为空
    public void exeSQL(String sql) {
        mWDB.execSQL(sql);
    }

    //返回结果为cursor
    public Cursor select(String sql) {
        //这里是 mRDB ，记住，否则可能同时读写会有错误，别问我是怎么知道的。。。
        return mRDB.rawQuery(sql, null);
    }
//对 task_table 操作

    //Get task by date and status
    public Cursor getTaskbyDate(String date, int status) {

        return mRDB.rawQuery("SELECT * FROM " + TASK_TABLE_NAME +
                        " WHERE  task_date = ?" +
                        " AND task_status = ?",
                new String[]{date, String.valueOf(status)});
    }

    //    Get task by date order by status
    public Cursor getTaskbyDate(String task_date) {
        return mRDB.rawQuery("SELECT * FROM " + TASK_TABLE_NAME +
                        " WHERE  task_date = ?" +
                        " ORDER BY " + TASK_COL4_STATUS,
                new String[]{task_date});

    }

    //change task status to 1
    public void setTaskComplete(int id) {

        mWDB.execSQL("UPDATE " + TASK_TABLE_NAME
                        + " SET " + TASK_COL4_STATUS + " = 1"
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    //change task status to 0
    public void setTaskIncomplete(int id) {
        mWDB.execSQL("UPDATE " + TASK_TABLE_NAME
                        + " SET " + TASK_COL4_STATUS + " = 0"
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    //get task by id
    public Cursor getTaskbyId(int index) {
        Cursor cursor = mRDB.rawQuery("SELECT * FROM " +
                TASK_TABLE_NAME +
                " WHERE " + TASK_COL0_ID + " = ?", new String[]{String.valueOf(index)});

        return cursor;
    }

    //delete task by id
    public void deleteTaskbyId(int index) {
        mWDB.execSQL("DELETE FROM "
                        + TASK_TABLE_NAME
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[]{String.valueOf(index)});
    }

    //update task without list
    public void updateTaskByIdNoList(int index, String name, String des, String date) {
        mWDB.execSQL("UPDATE "
                        + TASK_TABLE_NAME + " SET "
                        + TASK_COL1_NAME + " = ?, "
                        + TASK_COL2_DES + " = ?, "
                        + TASK_COL3_DATE + " = ? "
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[]{name, des, date, String.valueOf(index)});
    }

    //Add task by name, des, date
    public boolean addTaskData(String name, String des, String date) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_COL1_NAME, name);
        contentValues.put(TASK_COL2_DES, des);
        contentValues.put(TASK_COL3_DATE, date);
        contentValues.put(TASK_COL4_STATUS, 0);

        long result = mWDB.insert(TASK_TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

//    对 TIME_TABLE 进行操作

    //    将本次专注时间更新到数据库
    public void updateTimeByDate(String date, int minutes) {

        int count = 0;
        int minutes0 = 0;
        Cursor cursor = mRDB.rawQuery("SELECT * FROM " +
                TIME_TABLE_NAME +
                " WHERE " + TIME_DATE + " = ?", new String[]{date});
//        该天第一次专注
        if (cursor.getCount()==0) {
            mWDB.execSQL("INSERT INTO "
                            + TIME_TABLE_NAME + " values( null,?,?,1);",
                    new String[]{date, String.valueOf(minutes)});
            Debug.debug("插入时间");
            return;
        }
        while (cursor.moveToNext()) {
            count = cursor.getInt(3);
            minutes0 = cursor.getInt(2);
        }
        count += 1;
        minutes0 += minutes;

        mWDB.execSQL("UPDATE "
                        + TIME_TABLE_NAME + " SET "
                        + TIME_MINUTES + " = ?, "
                        + TIME_COUNT + " = ? "
                        + "WHERE " + TIME_DATE + " = ?",
                new String[]{String.valueOf(minutes0), String.valueOf(count), date});
    }

    //    获取前10条专注的时长
    public ArrayList<Integer> getMinutesForTen() {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        Cursor cursor = mRDB.rawQuery("SELECT * FROM " + TIME_TABLE_NAME, new String[]{});
        while (cursor.moveToNext()) {
            temp.add(cursor.getInt(2));
        }
        return temp;
    }

    //    获取前10条专注的日期
    public ArrayList<String> getDateForTen() {
        ArrayList<String> temp = new ArrayList<String>();
        Cursor cursor = mRDB.rawQuery("SELECT * FROM " + TIME_TABLE_NAME, new String[]{});
        while (cursor.moveToNext()) {
            temp.add(cursor.getString(1));
        }
        return temp;
    }

    //    获取前10条专注的次数
    public ArrayList<Integer> getCountForTen() {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        Cursor cursor = mRDB.rawQuery("SELECT * FROM " + TIME_TABLE_NAME, new String[]{});
        while (cursor.moveToNext()) {
            temp.add(cursor.getInt(3));
        }
        return temp;
    }


}
