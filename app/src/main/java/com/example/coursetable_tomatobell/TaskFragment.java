package com.example.coursetable_tomatobell;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coursetable_tomatobell.database.DataBaseHelper;
import com.example.coursetable_tomatobell.util.Debug;
import com.example.coursetable_tomatobell.util.ToastUtil;

import java.util.Calendar;

public class TaskFragment extends Fragment {
    private DataBaseHelper mHelper;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private EditText mHour;
    private EditText mMinute;
    private final String HOUR_MAXVALUE = "24";
    private final String MINUTE_MAXVALUE = "86400";

    public static final String TAG = "MainActivity";
    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";

    public static int OVERLAY_PERMISSION_REQ_CODE = 110;
    private TextView myTitle;

    public TaskFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = DataBaseHelper.getInstance(getActivity());
        mHelper.openReadLink();
        mHelper.openWriteLink();

        // activity传递数据，回调接受数据
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == OVERLAY_PERMISSION_REQ_CODE) {
                if (Build.VERSION.SDK_INT >= 23) {
                    mHour.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!Settings.canDrawOverlays(getActivity())) {
                                ToastUtil.show(getActivity(), "获取权限失败，应用将无法工作");
                            } else {
                                ToastUtil.show(getActivity(), "获取权限成功！应用可以正常使用了");
                            }
                        }
                    }, 500);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lock, container, false);

        mHour = (EditText) view.findViewById(R.id.hour);
        mMinute = (EditText) view.findViewById(R.id.minute);
//        设置点击事件
        Button btn_ok = (Button) view.findViewById(R.id.positive);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(view);
            }
        });
//        返回
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();

            }
        });
//        设置标题
        myTitle=(TextView)view.findViewById(R.id.titleText);
        myTitle.setText("定时锁机");
        //权限自检
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(getActivity())) {

            } else {
                //没有悬浮窗权限,去开启悬浮窗权限
                ToastUtil.show(getActivity(), "您需要授予应用在其他应用的上层显示的权限才可正常使用");
                try {
                    Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                    activityResultLauncher.launch(intent1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(),LockService.class));
    }

    public void Click(View v) {

        int intHour;
        if (mHour.length() != 0) {
            String hour = mHour.getText().toString();
            intHour = Integer.valueOf(hour);
        } else {
            intHour = 0;
        }
        if (intHour > Integer.valueOf(HOUR_MAXVALUE)) {
            ToastUtil.show(getActivity(), R.string.error_hour);
            return;
        }


        int intMinute;
        if (mMinute.length() != 0) {
            String minute = mMinute.getText().toString();
            intMinute = Integer.valueOf(minute);
        } else {
            intMinute = 0;
        }
        if (intMinute > Integer.valueOf(MINUTE_MAXVALUE)) {
            ToastUtil.show(getActivity(), R.string.error_minute);
            return;
        }
        if (intHour == 0 && intMinute == 0) {
            ToastUtil.show(getActivity(), R.string.error_zero);
            return;
        }

        final Intent intent = new Intent(getActivity(), LockService.class);
        intent.putExtra(HOUR, intHour);
        intent.putExtra(MINUTE, intMinute);
        intent.putExtra(LockService.TYPE, TAG);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.Dialog)
                .setTitle(R.string.alert)
                .setMessage(R.string.alert_hint)
                .setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Debug.debug("我是 start service");
                                getActivity().startService(intent);
                                dialog.cancel();
                                //模拟点击 Home 键
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                homeIntent.addCategory(Intent.CATEGORY_HOME);
                                startActivity(homeIntent);
                            }
                        }).setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
        alertDialog.show();


    }
}
