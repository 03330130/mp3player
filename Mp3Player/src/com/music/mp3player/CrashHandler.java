package com.music.mp3player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeSet;

import com.music.notification.TrayNotification;
import com.music.utils.FileUtils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler{
	
	private static final String VERSION_NAME = "versionName";    
	private static final String VERSION_CODE = "versionCode";    
	private static final String STACK_TRACE = "STACK_TRACE"; 
	private static final String CRASH_REPORTER_EXTENSION = ".txt";

	private static CrashHandler crashHandler = null;
	private Context context = null;
	/**ϵͳĬ�ϵ�UncaughtException������ */    
	private Thread.UncaughtExceptionHandler defaultHandler = null;  
	private Properties deviceCrashInfo = new Properties();
	
	private CrashHandler() {		
		//����ģ��
	}
	
	public static CrashHandler getInstance() {		
		return crashHandler == null ? new CrashHandler() : crashHandler;		
	}

	public void init(Context context) {
		this.context = context;
		this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && defaultHandler != null) { 
			defaultHandler.uncaughtException(thread, ex);    
		} else {
			android.os.Process.killProcess(android.os.Process.myPid());    
			System.exit(10);  
		} 
	}
	
	private boolean handleException(Throwable ex) {
		TrayNotification.removeNotification(context);
		if(ex == null) {  
			return false;		
		}  
		final String msg = ex.getLocalizedMessage(); 
		Toast.makeText(context, "���������:" + msg, Toast.LENGTH_LONG).show();    		
		collectCrashDeviceInfo(); 
		saveCrashInfoToFile(ex);
		return false;		
	}
	
	private void collectCrashDeviceInfo() {
		try {    
			PackageManager pm = context.getPackageManager();    
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES); 
							           
			if (pi != null) {    
			   deviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);					  		  				     			                   
			   deviceCrashInfo.put(VERSION_CODE, String.valueOf(pi.versionCode));    
			}    
		} catch (NameNotFoundException e) {    
			Log.e("CrashHandler", "Error while collect package info", e);    
		} 
		
		//ʹ�÷������ռ��豸��Ϣ.��Build���а��������豸��Ϣ,
		//����: ϵͳ�汾��,�豸������ �Ȱ������Գ����������Ϣ
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {    
				field.setAccessible(true);    
				deviceCrashInfo.put(field.getName(), (String)field.get(null).toString());    				  
				Log.d("CrashHandler", field.getName() + " : " + field.get(null));    			    
				} 
			catch (Exception e) {    
				Log.e("CrashHandler", "Error while collect crash info", e);    
			} 
		}
	}
	
	private void saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(info);  
        ex.printStackTrace(printWriter);  
  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
  
        String result = info.toString();  
        printWriter.close();  
        deviceCrashInfo.put(STACK_TRACE, result);
        try {  
            String fileName = "crash_" + getCurrentTime() + CRASH_REPORTER_EXTENSION;  
            FileOutputStream trace = context.openFileOutput(fileName,Context.MODE_PRIVATE);  
            FileOutputStream fos = new FileOutputStream(FileUtils.CRASHDIR + fileName);        
            deviceCrashInfo.store(trace, null); 
            deviceCrashInfo.store(fos, null);  
            trace.flush();
            trace.close();
            fos.flush();
            fos.close();        
        } catch (Exception e) {  
            Log.e("CrashHandler", "an error occured while writing report file...", e);  
        }  
	}
	
    /** 
     * �ڳ�������ʱ��, ���Ե��øú�����������ǰû�з��͵ı��� 
     */  
    public void sendPreviousReportsToServer() {  
        sendCrashReportsToServer();  
    }  
  
    /** 
     * �Ѵ��󱨸淢�͸�������,�����²����ĺ���ǰû���͵�. 
     *  
     * @param ctx 
     */  
    private void sendCrashReportsToServer() {  
        String[] crFilesInSystem = getCrashFilesInSystem();
        String[] crFilesInSD = getCrashFileInSD();
        if (crFilesInSystem != null && crFilesInSystem.length > 0 && crFilesInSD != null && crFilesInSD.length > 0 ) {  
            TreeSet<String> sortedFiles = new TreeSet<String>();  
            sortedFiles.addAll(Arrays.asList(crFilesInSystem));  
            sortedFiles.addAll(Arrays.asList(crFilesInSD));
            for (String fileName : sortedFiles) {  
                File crashFile = new File(FileUtils.CRASHDIR, fileName);                             
                postReport(crashFile);  
                if(crashFile.length() == 0) 
                	crashFile.delete();// ɾ���ѷ��͵ı���  
            }  
        } 
    }  
  
    /** 
     * ��ȡ���󱨸��ļ��� 
     * @param ctx 
     * @return 
     */  
    private String[] getCrashFilesInSystem() {  
        File filesDir = context.getFilesDir();        
        FilenameFilter filter = new FilenameFilter() {  
            public boolean accept(File dir, String name) {  
                return name.endsWith(CRASH_REPORTER_EXTENSION);  
            }  
        };     
        return filesDir.list(filter);  
    } 
    
    private String[] getCrashFileInSD() {
    	File filesDir = new File(FileUtils.CRASHDIR);
    	return filesDir.list();
    }
    
    private void postReport(File file) {  
        // TODO ʹ��HTTP Post ���ʹ��󱨸浽������  
    } 
    
	private String getCurrentTime() {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_hh.mm.ss", Locale.getDefault());
		return sdf.format(new Date());		
	}
}
