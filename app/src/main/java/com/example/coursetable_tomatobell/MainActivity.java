package com.example.coursetable_tomatobell;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.coursetable_tomatobell.database.DataBaseHelper;
import com.example.coursetable_tomatobell.entity.Course;
import com.example.coursetable_tomatobell.util.Debug;
import com.example.coursetable_tomatobell.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper mHelper;
    private DrawerLayout mDrawerLayerout;
    private RelativeLayout day;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Debug.debug("create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper= DataBaseHelper.getInstance(this);
        mHelper.openReadLink();
        mHelper.openWriteLink();

//        activity传递数据，回调接受数据
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            添加课程界面传回来的course加载进数据库
            if (result.getResultCode()== 1) {
                ArrayList<String> course=result.getData().getStringArrayListExtra("course");
                String sql=String.format("insert into course_info values (null,'%s','%s','%s','%s','%s','%s')"
                        ,course.get(0),course.get(1),course.get(2),course.get(3),course.get(4),course.get(5));
                Debug.debug(sql);
                mHelper.exeSQL(sql);
                loadData();
            }
            if(result.getResultCode()==2){
                ArrayList<String> course=result.getData().getStringArrayListExtra("course");
                String sql=String.format("UPDATE course_info set course_name ='%s',teacher='%s',class_room = '%s' where day='%s' and class_start='%s' and class_end = '%s'"
                        ,course.get(0),course.get(1),course.get(2),course.get(3),course.get(4),course.get(5));
                Debug.debug(sql);
                mHelper.exeSQL(sql);
                loadData();

            }
        });

        mDrawerLayerout=(DrawerLayout)findViewById(R.id.draw);

//        左边侧滑菜单
        NavigationView navView=(NavigationView) findViewById(R.id.nav_view);
//        工具条(ps:如果遇到程序闪退，将values/themes里头主题设置为不带actionbar
//        绑定menu和drawer
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setHomeButtonEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示侧滑菜单
                mDrawerLayerout.openDrawer(GravityCompat.START);
            }
        });

//        导航栏点击
        navView.setCheckedItem(R.id.courseTable);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.courseTable:
                        break;
                    case R.id.add_courses:
                        Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
                        // 两个activity之间传递数据
                        activityResultLauncher.launch(intent);
                        break;
                    case R.id.time_manage:
                        Intent intent1 = new Intent(MainActivity.this, TodoListActivity.class);
                        // 两个activity之间传递数据
                        activityResultLauncher.launch(intent1);
                        break;


                }
                mDrawerLayerout.closeDrawers();
                return true;
            }
        });

//        创建节数视图
        createLeftView();

//        从数据库中加载课程信息
        loadData();
    }
//从数据库中加载数据
    @SuppressLint("Range")
    private void loadData() {
        ArrayList<Course> coursesList=new ArrayList<Course>();
        String sql="select * from course_info";
        Cursor cursor=mHelper.select(sql);
        if(cursor.moveToFirst()){
            do{
                coursesList.add(new Course(
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end"))
                       ));
            }while (cursor.moveToNext());
        }
        cursor.close();
        //将数据库读出来的课程显示
        for(Course course :coursesList){
            createCourseView(course);
        }
    }
//创建课程视图
    private void createCourseView(Course course) {

        final int[] course_bj = {R.drawable.coursetable1, R.drawable.coursetable2,
                R.drawable.coursetable3, R.drawable.coursetable4, R.drawable.coursetable5,
                R.drawable.coursetable6, R.drawable.coursetable7, R.drawable.coursetable8,
                R.drawable.coursetable9, R.drawable.coursetable10};
        int getDay = course.getDay();
        if (getDay > 7 || getDay < 1 || course.getStart() > course.getEnd()) {
            ToastUtil.show(this, "请检查星期和课程节次输入是否有误");
        } else {
            switch (getDay) {
                case 1:
                    day = (RelativeLayout) findViewById(R.id.monday);
                    break;
                case 2:
                    day = (RelativeLayout) findViewById(R.id.tuesday);
                    break;
                case 3:
                    day = (RelativeLayout) findViewById(R.id.wednesday);
                    break;
                case 4:
                    day = (RelativeLayout) findViewById(R.id.thursday);
                    break;
                case 5:
                    day = (RelativeLayout) findViewById(R.id.friday);
                    break;
                case 6:
                    day = (RelativeLayout) findViewById(R.id.saturday);
                    break;
                case 7:
                    day = (RelativeLayout) findViewById(R.id.weekday);
                    break;

            }

//        设置该课程视图的宽高
            View v = LayoutInflater.from(this).inflate(R.layout.course_card, null);
            int height = 180;
            v.setY(height * (course.getStart() - 1));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, (course.getEnd() - course.getStart() + 1) * height);
            v.setLayoutParams(params);

//            设置课程视图的文字、随机背景颜色
            TextView textView = v.findViewById(R.id.text_view);
            textView.setBackgroundResource(course_bj[(int) (Math.random()*10)]);
            textView.setText(course.getCourseName() + "\n\n" + course.getClassRoom());

            day.addView(v);
//            长按删除课程
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    v.setVisibility(View.GONE);
                    day.removeView(v);
                    String sql="delete from course_info where course_name = '" + course.getCourseName() + "' and day = '" + getDay + "' and class_start = '" + course.getStart()+"'";
                    Debug.debug(sql);
                    mHelper.exeSQL(sql);
                    return true;
                }
            });
//            点击编辑并查看课程
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MessageCourseActivity.class);

                    ArrayList<String> str_course=course.toArrayList();
                    intent.putExtra("str_course", str_course);
                    // 两个activity之间传递数据
                    activityResultLauncher.launch(intent);
                }
            });
        }
    }
//创建左边节数视图
    private void createLeftView() {
        for(int i=1;i<11;i++){
            View view = LayoutInflater.from(this).inflate(R.layout.left_view, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,180);
            view.setLayoutParams(params);
            TextView text = (TextView) view.findViewById(R.id.class_number_text);
            text.setText(String.valueOf(i));
            LinearLayout leftViewLayout = (LinearLayout) findViewById(R.id.left_view_layout);
            leftViewLayout.addView(view);

        }
    }
    @Override
    protected void onStop() {
//        Debug.debug("Stop");
        super.onStop();
    }

    @Override
    protected void onStart() {
//        Debug.debug("start");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Debug.debug("destroy");
        super.onDestroy();
        mHelper.closeLink();
    }
}