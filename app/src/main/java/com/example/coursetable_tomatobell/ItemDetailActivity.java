package com.example.coursetable_tomatobell;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursetable_tomatobell.database.DataBaseHelper;
import com.example.coursetable_tomatobell.util.ToastUtil;

import java.util.Calendar;

public class ItemDetailActivity extends AppCompatActivity {
    private DataBaseHelper mHelper;

    EditText taskName;
    EditText taskDes;
    TextView taskDate;

    Button btnSave;
    Button btnDelete;
    private ImageView IconBack;
    private int taskid;

    private DatePickerDialog.OnDateSetListener myDateSetListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        mHelper= DataBaseHelper.getInstance(this);
        mHelper.openReadLink();
        mHelper.openWriteLink();

        taskName = (EditText) findViewById(R.id.taskDetail_name);
        taskDes = (EditText) findViewById(R.id.taskDetail_description);
        taskDate = (TextView) findViewById(R.id.taskDetail_date);

        btnSave = (Button) findViewById(R.id.taskDetail_Edit);
        btnDelete = (Button) findViewById(R.id.taskDetail_Delete);

        IconBack = (ImageView)findViewById(R.id.taskdetail_toolbar_back);
        TextView bar_name = (TextView) findViewById(R.id.taskdetail_toolbar_barname);

        Intent intent = getIntent();
        taskid = intent.getIntExtra("TASK_ID",-1);
//从数据库中根据ID找到任务具体信息并显示
        Cursor task_detail = mHelper.getTaskbyId(taskid);
        if(task_detail.getCount() == 0){
            ToastUtil.show(this,"没有找到！");
            return;
        }

        while(task_detail.moveToNext()){

            String one = task_detail.getString(1);
            taskName.setText(one);
            bar_name.setText(task_detail.getString(1));
            taskDes.setText(task_detail.getString(2));
            String temp_date = task_detail.getString(3);
            String m = temp_date.substring(0,2);
            String d = temp_date.substring(2,4);
            String y = temp_date.substring(4);
            String new_temp = m + "/" + d + "/"+ y;
            taskDate.setText(temp_date);
        }

        addDate();

        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.deleteTaskbyId(taskid);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskname = taskName.getText().toString();
                String taskdes = taskDes.getText().toString();
                String taskdate = taskDate.getText().toString();
                mHelper.updateTaskByIdNoList(taskid, taskname, taskdes, taskdate);
                finish();
            }
        });


    }
//    调出日历方便用户修改时间
    private void addDate() {
        taskDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ //show the dialog for user to select date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        ItemDetailActivity.this, android.R.style.Theme_Material_Light,
                        myDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        myDateSetListener = new DatePickerDialog.OnDateSetListener() {
            String m,d,y,dateview;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                m = (String) DateFormat.format("MM",cal);
                d = (String) DateFormat.format("dd",cal);
                y = (String) DateFormat.format("yyyy",cal);
                String temp_date_1 = m + d + y; // generate the task_date for backend
                //dateview = m + "/" + d + "/"+ y;
                taskDate.setText(temp_date_1); // show the date on frontend
            }
        };
    }
}
