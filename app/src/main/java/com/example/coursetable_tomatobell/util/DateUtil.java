package com.example.coursetable_tomatobell.util;

public class DateUtil {
    public DateUtil(){
    }
    public static String toDateReal(String date){
        String res=date.substring(0,2)+date.substring(3,5)+date.substring(6);
        return res;
    }
    public static String toDateView(String date){
        String res=date.substring(0,2)+'/'+date.substring(2,4)+'/'+date.substring(4);
        return res;
    }
}
