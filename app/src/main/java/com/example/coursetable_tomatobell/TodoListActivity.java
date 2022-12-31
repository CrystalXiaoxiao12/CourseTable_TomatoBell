package com.example.coursetable_tomatobell;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.example.coursetable_tomatobell.database.DataBaseHelper;
import com.example.coursetable_tomatobell.util.Debug;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TodoListActivity extends AppCompatActivity {
    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;
    private TaskFragment taskFragment;
    private calenderFragment calenderFragment;
    private DataBaseHelper mHelper;
    private ListFragment listFragment;
    private BarFragment barFragment;
    private LineFragment linefragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        mainNav =(BottomNavigationView) findViewById(R.id.main_nav);
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);
        taskFragment = new TaskFragment();
        listFragment = new ListFragment();
        linefragment=new LineFragment();
        calenderFragment = new calenderFragment();
        barFragment =new BarFragment();
        Debug.debug("定义");
        mHelper= DataBaseHelper.getInstance(this);
        mHelper.openReadLink();
        mHelper.openWriteLink();

        setFragment(taskFragment);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.nav_task:
                        setFragment(taskFragment);
                        return true;

                    case R.id.nav_list:
                        setFragment(linefragment);
                        return true;

                    case R.id.nav_calender:
                        setFragment(calenderFragment);
                        return true;

                    default:
                        return false;
                }
        }
        });
    }
    private void setFragment(Fragment   fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
