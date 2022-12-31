package com.example.coursetable_tomatobell;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursetable_tomatobell.database.DataBaseHelper;
import com.example.coursetable_tomatobell.util.DateUtil;
import com.example.coursetable_tomatobell.util.Debug;
import com.example.coursetable_tomatobell.util.ToastUtil;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private DataBaseHelper mHelper;

    private String task_name;
    private String task_des;
    private String task_date;

    private Button btnClear;
    private Button btnDone;

    EditText tname, tdes;

    private ImageView IconBack;
    private TextView myDate;
    private DatePickerDialog.OnDateSetListener myDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mHelper= DataBaseHelper.getInstance(this);
        mHelper.openReadLink();
        mHelper.openWriteLink();

        tname = (EditText)findViewById(R.id.addtask_name);
        tdes = (EditText)findViewById(R.id.addtask_description);
        btnClear = (Button)findViewById(R.id.addtask_clear);
        btnDone = (Button)findViewById(R.id.addtask_Done);
        IconBack = (ImageView)findViewById(R.id.addtask_toolbar_back);

        myDate = (TextView) findViewById(R.id.addtask_Date_select);

        addTask();
        viewClear();
        comeBack();
    }
    public void comeBack(){
        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void viewClear(){
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tname.getText().clear();
                tdes.getText().clear();
            }
        });
    }
    public void addTask(){
        addDate();
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the input
                task_name = tname.getText().toString();
                task_des = tdes.getText().toString();
                task_date = myDate.getText().toString();

                //insert the task into database
                if(task_name.length() != 0){ //valid input

                    //public boolean addTaskData( String name, String des, String date, int list)
                    boolean insertTask = mHelper.addTaskData(task_name,task_des, DateUtil.toDateReal(task_date));
                    if(insertTask){
                        finish();
                        ToastUtil.show(AddTaskActivity.this,"添加成功！");
                    }else{
                        //show input error
                        ToastUtil.show(AddTaskActivity.this,"添加失败！");
                    }
                }else{
                    ToastUtil.show(AddTaskActivity.this,"任务名字不能为空！");
                }
            }
        });
    }
    public void addDate() {
        myDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ //show the dialog for user to select date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddTaskActivity.this, DatePickerDialog.THEME_HOLO_LIGHT,
                        myDateSetListener, year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                task_date = m + d + y; // generate the task_date for backend
                dateview = m + "/" + d + "/"+ y;
                myDate.setText(dateview); // show the date on frontend
            }
        };
    }
}