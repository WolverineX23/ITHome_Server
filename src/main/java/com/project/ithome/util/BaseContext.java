package com.project.ithome.util;

//基于ThreadLocal封装工具类，用户保存和获取当前登陆的id/用户名userName
public class BaseContext {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setCurrentUserId(String userId){
        threadLocal.set(userId);
    }

    public static String getCurrentUserId(){
        return threadLocal.get();
    }
}
