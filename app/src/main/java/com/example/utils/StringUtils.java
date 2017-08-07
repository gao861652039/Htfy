package com.example.utils;

import java.util.ArrayList;

/**
 * Created by 高峰 on 2017/6/26.
 */

public class StringUtils {

    public static void getIndexOf(char[] array,char[] target,int index,ArrayList<Integer> list){
        while(true){
            if(array.length == index){
                break;
            }
            if(array[index] == target[0]){
                if(new String(array,index,target.length).equals(new String(target))){
                    list.add(index);
                    index = index+target.length;
                    continue;
                }else{
                    index = index+1;
                    continue;
                }
            }
            if(array[index]!=target[0]){
                index = index+1;
                continue;
            }
        }
    }
    public static String changeType(int num) {
        String str = "" + num;
        if (str.length() == 1) {
            return "0" + str;
        } else {
            return str;
        }
    }

}
