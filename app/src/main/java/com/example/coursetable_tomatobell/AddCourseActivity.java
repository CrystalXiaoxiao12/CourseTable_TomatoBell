package com.example.coursetable_tomatobell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.coursetable_tomatobell.entity.Course;
import com.example.coursetable_tomatobell.util.ToastUtil;

import java.util.ArrayList;

public class AddCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setFinishOnTouchOutside(false);

        final EditText inputCourseName = (EditText) findViewById(R.id.course_name);
        final EditText inputTeacher = (EditText) findViewById(R.id.teacher_name);
        final EditText inputClassRoom = (EditText) findViewById(R.id.class_room);
        final EditText inputDay = (EditText) findViewById(R.id.day);
        final EditText inputStart = (EditText) findViewById(R.id.classes_begin);
        final EditText inputEnd = (EditText) findViewById(R.id.classes_ends);
        Button backButton = (Button) findViewById(R.id.back_add);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button okButton = (Button) findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseName = inputCourseName.getText().toString();
                String teacher = inputTeacher.getText().toString();
                String classRoom = inputClassRoom.getText().toString();
                String day = inputDay.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();
                if (courseName.equals("") || day.equals("") || start.equals("") || end.equals("")) {
                    ToastUtil.show(AddCourseActivity.this,"基本课程信息未填写");
                }
                else {
                    Course course = new Course(courseName, teacher, classRoom,
                            Integer.valueOf(day), Integer.valueOf(start), Integer.valueOf(end)
                            );
                    Intent intent = new Intent(AddCourseActivity.this, MainActivity.class);
//                   两个activity之间不能传自定义类，所以转换成String
                    ArrayList<String> str_course=course.toArrayList();
                    intent.putExtra("course", str_course);
                    setResult(1, intent);
                    finish();
                }

            }
        });

    }
}