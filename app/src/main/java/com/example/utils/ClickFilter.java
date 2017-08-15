package com.example.utils;

public class ClickFilter
{
 public static final long INTERVAL = 1000L; //防止连续点击的时间间隔
 private static long lastClickTime = 0L; //上一次点击的时间
 
 public static boolean filter()
 {
  long time = System.currentTimeMillis();

  if ( ( time - lastClickTime ) > INTERVAL )
  {
   return false;
  }
  lastClickTime = time;
  return true;
 }
}