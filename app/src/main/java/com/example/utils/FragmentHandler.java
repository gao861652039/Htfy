/*
 * Copyright (c)  2017 河北宏泰丰业医疗器械有限公司
 *
 */

package com.example.utils;

import android.support.v4.app.Fragment;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by 高峰 on 2017/8/23.
 */

public class FragmentHandler {


    /**
     * 存放Fragment的map
     */
    private static Map<String, Fragment> fragments = new HashMap<String,Fragment>();

    /**
     * 添加Fragment
     */

    public  static void addFragment(Fragment fragment,String key){
        fragments.put(key,fragment);
    }

    /**
     * 删除Fragment
     */

     public  static void removeFragment(String key){

        fragments.remove(key);

     }

    /**
     * 删除所有Fragment
     */

     public static void removeAll(){

         fragments.clear();

     }

    /**
     * 检测Fragment是否存在
     */

      public static boolean checkExsit(String key){

         return fragments.containsKey(key);

      }

    /**
     * 得到对应key的value值
     */
      public static Fragment getValue(String key){

          return fragments.get(key);
      }


}
