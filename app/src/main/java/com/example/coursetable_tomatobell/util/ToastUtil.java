package com.example.coursetable_tomatobell.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void show(Context ctx,String desc ){
        Toast.makeText(ctx,desc,Toast.LENGTH_SHORT).show();
    }
    public static void show(Context ctx,int id ){
        Toast.makeText(ctx,id,Toast.LENGTH_SHORT).show();
    }
}
