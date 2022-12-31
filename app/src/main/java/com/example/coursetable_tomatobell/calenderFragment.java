package com.example.coursetable_tomatobell;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coursetable_tomatobell.database.DataBaseHelper;
import com.example.coursetable_tomatobell.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class calenderFragment extends Fragment {

    private CalendarView calendarView;
    private ListView myList;
    private ImageView myBack;
    private TextView myDate;
    private DataBaseHelper mHelper;
    private ImageView addNewTask;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private CalendarAdapter calendarItemAdapter;

    public calenderFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper= DataBaseHelper.getInstance(getActivity());
        mHelper.openReadLink();
        mHelper.openWriteLink();

        // activity传递数据，回调接受数据
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            添加任务
            if (result.getResultCode()== 1) {

            }
//            查看任务
            if (result.getResultCode()== 2) {

            }
            viewSetting(Calendar.getInstance());
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        myDate = (TextView) view.findViewById(R.id.calendarbar_text);
        myList = (ListView) view.findViewById(R.id.task_ListView);
        myBack = (ImageView) view.findViewById(R.id.back);

//        设置添加任务点击事件
        addNewTask = (ImageView) view.findViewById(R.id.calendarbar_addNewList);
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Addtask_Intent = new Intent(addNewTask.getContext(),AddTaskActivity.class);
                activityResultLauncher.launch(Addtask_Intent);
            }
        });
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();

            }
        });

        viewSetting(Calendar.getInstance());

        final ArrayList<String> taskItems = new ArrayList<>();
        final ArrayList<String> taskDes = new ArrayList<>();
        final ArrayList<Integer> taskId = new ArrayList<>();
        final ArrayList<Integer> taskStatus = new ArrayList<>();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                Calendar cal_2 = Calendar.getInstance();
                cal_2.set(year,month,day);
                viewSetting(cal_2);

            }
        });

        return view;
    }
//显示选中改天的所有任务、设置点击一项任务查看细节
    private void viewSetting(Calendar cal) {

        String task_date=getFormatDate(cal);
        myDate.setText(DateUtil.toDateView(task_date));

        final ArrayList<String> taskItems = new ArrayList<>();
        final ArrayList<String> taskDes = new ArrayList<>();
        final ArrayList<Integer> taskId = new ArrayList<>();
        final ArrayList<Integer> taskStatus = new ArrayList<>();

        Cursor todo_task_data =mHelper.getTaskbyDate(task_date);
        while(todo_task_data.moveToNext()) {
            taskId.add(todo_task_data.getInt(0));
            taskItems.add(todo_task_data.getString(1));
            taskDes.add(todo_task_data.getString(2));
            taskStatus.add(todo_task_data.getInt(4));

        }
        calendarItemAdapter= new CalendarAdapter(getContext(), taskId, taskItems, taskDes,taskStatus);
        myList.setAdapter(calendarItemAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskid = taskId.get(position);
                Intent item_detail = new Intent(view.getContext(), ItemDetailActivity.class);
                item_detail.putExtra("TASK_ID",taskid);
                activityResultLauncher.launch(item_detail);

            }
        });
    }
//获取日历时间
    private String getFormatDate(Calendar cal) {
        final String mm = (String) DateFormat.format("MM",cal);
        final String dd = (String) DateFormat.format("dd",cal);
        final String yy = (String) DateFormat.format("yyyy",cal);
        String task_date = mm+dd+yy;
        return task_date;

    }
}
