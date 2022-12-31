package com.example.coursetable_tomatobell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursetable_tomatobell.database.DataBaseHelper;
import com.example.coursetable_tomatobell.util.Debug;
import com.example.coursetable_tomatobell.util.ToastUtil;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter {
    private final DataBaseHelper mHelper;
    LayoutInflater mInflater;
    Context context;

    ArrayList<String> task_items;
    ArrayList<String> task_descriptions;
    ArrayList<Integer>task_id;
    ArrayList<Integer>task_status;

    public CalendarAdapter(Context context, ArrayList<Integer> taskId, ArrayList<String> taskItems, ArrayList<String> taskDes, ArrayList<Integer> taskStatus) {
        task_id = taskId;
        task_items = taskItems;
        task_descriptions = taskDes;
        task_status = taskStatus;
        this.context =context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mHelper= DataBaseHelper.getInstance(context.getApplicationContext());
        mHelper.openReadLink();
        mHelper.openWriteLink();
    }

    @Override
    public int getCount() {
        return task_items.size();
    }

    @Override
    public Object getItem(int position) {
        return task_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_task, null);


        TextView nameTextView = (TextView)v.findViewById(R.id.taskName_TextView);
        TextView decriptionTextView = (TextView) v.findViewById(R.id.taskDes_TextView);
        final CheckBox taskCheckBox = (CheckBox) v.findViewById(R.id.taskCheckBox);

        final int index = position;

        String name = task_items.get(position);
        String desc = task_descriptions.get(position);
        int status = task_status.get(position);

        nameTextView.setText(name);
        decriptionTextView.setText(desc);

        if (status == 1) {
            taskCheckBox.setChecked(true);
        } else {
            taskCheckBox.setChecked(false);
        }

        taskCheckBox.setOnClickListener(new View.OnClickListener() {

            int id = task_id.get(index);

            @Override
            public void onClick(View v) {
                if(taskCheckBox.isChecked()){
                    mHelper.setTaskComplete(id);
                    Debug.debug(3);

                    ToastUtil.show( context,"设置为已完成！");
                    Debug.debug(3);

                }
                else{
                    mHelper.setTaskIncomplete(id);
                   ToastUtil.show(context,"设置为未完成！");

                }
            }
        });
        return v;
    }

}
