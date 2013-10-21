package com.music.mp3player;

import android.app.Application;

public class CrashApplication extends Application {  
	  
    @Override  
    public void onCreate() {  
        super.onCreate();  
        CrashHandler crashHandler = CrashHandler.getInstance();  
        //ע��crashHandler  
        crashHandler.init(getApplicationContext());  
        //������ǰû���͵ı���(��ѡ)  
        crashHandler.sendPreviousReportsToServer();  
    }       
}  
