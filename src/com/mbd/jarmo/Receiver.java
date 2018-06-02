package com.mbd.jarmo;


import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;  
import android.content.SharedPreferences;  
import android.preference.PreferenceManager;  
  
//import com.jjoe64.BackgroundService;  
  
public class Receiver extends BroadcastReceiver {  
 @Override  
 public void onReceive(Context context, Intent intent) {  
  if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {  
   Intent pushIntent = new Intent(context, JarmoService.class);  
   context.startService(pushIntent);  
  }  
 }  
}  