package com.example.utils;

import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ActivityManager {
    /** 
     * 存放Activity的map 
     */  
    private static Map<String, AppCompatActivity> activitys = new HashMap<String, AppCompatActivity>();
  
    /** 
     * 获取管理类中注册的所有Activity的map 
     * 
     * @return 
     */  
    public static Map<String, AppCompatActivity> getActivitys() {  
        return activitys;  
    }  
  
    /** 
     * 根据键值取对应的Activity 
     * 
     * @param key 键值 
     * @return 键值对应的Activity 
     */  
    public static AppCompatActivity getActivity(String key) {  
        return activitys.get(key);  
    }  
  
    /** 
     * 注册Activity 
     * 
     * @param value 
     * @param key 
     */  
    public static void addActivity(AppCompatActivity value, String key) {  
        activitys.put(key, value);  
    }  
  
    /** 
     * 将key对应的Activity移除掉 
     * 
     * @param key 
     */  
    public static void removeActivity(String key) {  
        activitys.remove(key);  
    }  
  
    /** 
     * finish掉所有的Activity移除所有的Activity 
     */  
    public static void removeAllActivity() {  
        Iterator<AppCompatActivity> iterActivity = activitys.values().iterator();
        while (iterActivity.hasNext()) {  
            iterActivity.next().finish();  
        }  
        activitys.clear();  
    }  
}  