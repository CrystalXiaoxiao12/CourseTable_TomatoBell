package com.example.coursetable_tomatobell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_course);

        final EditText inputCourseName = (EditText) findViewById(R.id.course_name);
        final EditText inputTeacher = (EditText) findViewById(R.id.teacher_name);
        final EditText inputClassRoom = (EditText) findViewById(R.id.class_room);
        final TextView inputDay = (TextView) findViewById(R.id.day);
        final TextView inputTime = (TextView) findViewById(R.id.time_information);

//        返回键点击事件
        Button backButton = (Button) findViewById(R.id.back_add);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        接受查看的课程名
        Intent intent=getIntent();
        ArrayList<String> str_course=intent.getStringArrayListExtra("str_course");

//        课程信息显示
        inputCourseName.setText(str_course.get(0));
        inputTeacher.setText(str_course.get(1));
        inputClassRoom.setText(str_course.get(2));
        inputDay.setText("星期"+dayToString(str_course.get(3)));
        inputTime.setText("第 "+str_course.get(4)+" - "+str_course.get(5)+" 节 ");

        Button okButton =(Button)findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_course.set(0,inputCourseName.getText().toString());
                str_course.set(1,inputTeacher.getText().toString());
                str_course.set(2,inputClassRoom.getText().toString());
                Intent intent1 = new Intent(MessageCourseActivity.this, MainActivity.class);
//                   两个activity之间不能传自定义类，所以转换成String
                intent1.putExtra("course", str_course);
                setResult(2, intent1);
                finish();
            }
        });


    }

    private String dayToString(String day) {
        String dayString;
        switch (day){
            case "1":
                dayString = "一";
                break;
            case "2":
                dayString = "二";
                break;
            case "3":
                dayString = "三";
                break;
            case "4":
                dayString = "四";
                break;
            case "5":
                dayString = "五";
                break;
            case "6":
                dayString= "六";
                break;
            case "7":
                dayString= "日";
                break;
            default:
                dayString= "";
                break;
        }
        return dayString;
    }
}