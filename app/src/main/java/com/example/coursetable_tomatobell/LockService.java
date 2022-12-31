package com.example.coursetable_tomatobell;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.example.coursetable_tomatobell.database.DataBaseHelper;
import com.example.coursetable_tomatobell.util.Debug;
import com.example.coursetable_tomatobell.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LockService extends Service {
    private FrameLayout iconFloatView;
    private String awakenTime;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    public static final String TYPE = "TYPE";
    public static final String TAG = "LockService";
    public static final String TIME = "TIME";
    private boolean isAddView;
    private long alarmTime;
    private DataBaseHelper mHelper;


    @Override
    public void onCreate() {
        Debug.debug("我是lockservice create");
        super.onCreate();

        mHelper = DataBaseHelper.getInstance(getApplicationContext());
        mHelper.openReadLink();
        mHelper.openWriteLink();

        iconFloatView = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.floating_icon,null);
        iconFloatView.findViewById(R.id.lock_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getApplicationContext(),"此手机已被锁定，预计解锁时间 : \n" + awakenTime);
            }
        });

        mWindowManager = (WindowManager)(getSystemService(Context.WINDOW_SERVICE));
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.CENTER;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Debug.debug("我是lockservice startCommand");
        if(intent == null){
            return super.onStartCommand(intent, flags, startId);
        }
        if(intent.getStringExtra("TYPE").equals(TAG)){
            //解锁界面，停止服务
            removeView();
            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(this).edit();
            editor.putLong(TIME,-1);
            editor.apply();
            stopSelf();////
            return super.onStartCommand(intent, flags, startId);
        }else if(intent.getStringExtra(TYPE).equals(TaskFragment.TAG)){
            //获取用户输入的时长，换算成毫秒
            int hours = intent.getIntExtra(TaskFragment.HOUR,-1) * 60 * 60 * 1000;//把设置的小时换算成毫秒数
            int minutes = intent.getIntExtra(TaskFragment.MINUTE,-1) * 60 * 1000;//把设置的分钟换算成毫秒数
            alarmTime = System.currentTimeMillis() + hours + minutes;

//            将本次专注的时间插入到数据库中
            Calendar cal= Calendar.getInstance();
            int y=cal.get(Calendar.YEAR);
            int m=cal.get(Calendar.MONTH);
            int d=cal.get(Calendar.DATE);
            String date=String.valueOf(String.valueOf(m)+'/'+String.valueOf(d)+'/'+String.valueOf(y));
            mHelper.updateTimeByDate(date,hours/(1000*60)+minutes/(1000*60));


            //根据用户提交的时长格式化一个时刻
            formatTime(alarmTime);

            //开始定时任务
            startAlarmTask();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startAlarmTask() {
        //开始定时任务
        Intent i = new Intent(this,LockService.class);
        i.putExtra(TYPE,TAG);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);

        //设置定时任务，用户设置的时间到了开始服务解锁界面
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.setExact(AlarmManager.RTC_WAKEUP,alarmTime,pi);

        addView();
    }

    private void formatTime(long alarmTime) {
        //根据用户提交的时长格式化一个时刻
        SimpleDateFormat format=new SimpleDateFormat("MM 月 dd 号 HH : mm : ss");
        Date date=new Date(alarmTime);
        awakenTime = format.format(date);
    }

    private synchronized void addView(){
        if(!isAddView){
            mWindowManager.addView(iconFloatView,mLayoutParams);
            isAddView = true;
        }

    }

    private synchronized void removeView(){
        if(isAddView){
            mWindowManager.removeView(iconFloatView);
            isAddView = false;
        }
    }
}
