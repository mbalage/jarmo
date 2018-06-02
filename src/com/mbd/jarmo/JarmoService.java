package com.mbd.jarmo;
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.graphics.Paint;
import android.os.IBinder;
//
//
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.io.PrintWriter;
//import java.net.InetSocketAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.net.ftp.FTPClient;
//
//import com.mbd.jarmo.R;
//
//import android.Manifest;
//import android.provider.Contacts;
//import android.provider.ContactsContract;
//import android.provider.ContactsContract.PhoneLookup;
//import android.provider.Settings;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.content.res.AssetManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Paint;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Binder;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.StrictMode;
//import android.support.v4.content.LocalBroadcastManager;
//import android.telephony.SmsMessage;
//import android.telephony.TelephonyManager;
import android.util.Log;
//import android.widget.Toast;

//public class JarmoService extends Service {
public class JarmoService  {
//
//    	private final IBinder mBinder = new LocalBinder();
//    	LocationManager locationManager;
//    	String currentLocationFilename="";
//    	String sd="";
//    	int locationCounter=0;
//    	static int LOCPERFILE=5;
//    	static int LOCTIME=80; //sec
//    	
    	static int JARMOVERSION=1;
//    	
    	public static int inGame=0;
//    	public static final String JARMOSOCKET = "com.mbd.jarmo.JARMOSOCKET";
//    	public static final String JARMOREQUEST = "com.mbd.jarmo.JARMOREQUEST";
    	public static final int NOTIFY_ID=111;
//    	
//    	public static Thread receiverThread;
//    	public static ServerSocket serverSocket;
//    	public static Socket socket;
    	public static JarmoService jarmoService;
//    	public static String remoteWifiIP="";
//    	public static MySocketListener mySocketListener;
//    	public static String lastLineSent ="";
//    	public static Pinger pinger;
//      	
    	public static int logging=0;
//    	
//    	
//    	
//    	static Handler handler = new Handler ();
//
//        public class LocalBinder extends Binder {
//        	JarmoService getService() {
//                return JarmoService.this;
//            }
//        }
//        
//	   @Override
//	    public int onStartCommand(Intent intent, int flags, int startId) {
//		   
//            JarmoService.JarmoLog  ("onStartCommand ----"+ locationCounter);
//            if (locationCounter>0) return START_STICKY;
//            
//         try {   
//   //         getCurrentLocation ();
//   //         start1mTimer();
//         }catch (Exception e) {e.printStackTrace();}
//         
//	    //	uploadWU ();
//	    	JarmoService.JarmoLog  ("onstart complete");
//	    
//     
//	        return START_STICKY;
//	    }
//
//	    @Override
//	    public void onCreate() {
//	    	pinger = new Pinger();
//	    	JarmoService.JarmoLog  ("Service oncreate -------");
//	        // Acquire a reference to the system Location Manager
//	    	JarmoService.jarmoService=this;
// //           locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
// //           currentLocationFilename=filenameForLocation();
//	    	sd = "/mnt/sdcard2/"; //Environment.getExternalStorageDirectory().getPath();
////	    	File h=new File ("/mnt/sdcard2/");
////	    	for (int t=0;t<1000;t++) JarmoService.JarmoLog  ("--> "+h.listFiles()[t].getName());
//	    	JarmoService.JarmoLog  ("BMO SD="+sd);
//	    	
//	    	if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }
//
//	    	startSocketListener ();
//
////	        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
////	        IntentFilter intentFilter = new IntentFilter();
////	        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
////	        intentFilter.addAction("android.provider.Telephony.SMS_SENT");
////	        this.registerReceiver(bReceiver, intentFilter);
//	        
//	    	startSocketListener ();
//	    	JarmoService.remoteWifiIP="";
//    		resetWifiConnectionData ();
//
//	    }
//	    
//    	//handle incoming socket received and broadcast by JarmoService
///*        BroadcastReceiver bReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
//                    JarmoService.JarmoLog  ("sms arrived");
//                    Object[] smsExtra = (Object[]) intent.getExtras().get("pdus");
//                    for ( int i = 0; i < smsExtra.length; ++i )
//                    {
//                        SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
//                        JarmoService.JarmoLog  ("sms message "+sms.getOriginatingAddress()+" "+sms.getDisplayMessageBody());
//                        appendToFile ("sms message "+sms.getOriginatingAddress()+" "+sms.getDisplayMessageBody(), currentLocationFilename);
//                    }
//                }
//                if(intent.getAction().equals("android.provider.Telephony.SMS_SENT")) {
//                    JarmoService.JarmoLog  ("sms sent");
//                    Object[] smsExtra = (Object[]) intent.getExtras().get("pdus");
//                    for ( int i = 0; i < smsExtra.length; ++i )
//                    {
//                        SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
//                        JarmoService.JarmoLog  ("sms message "+sms.getOriginatingAddress()+" "+sms.getDisplayMessageBody());
//                    }
//                }
// 
//            }
// 
//        };
//*/	    
///*	    
//	    public void start1mTimer () {
//	    	JarmoService.JarmoLog  ("Start 1m timer...");
//	    	
//			getCurrentLocation ();	
//	    	
//	    	handler.postDelayed(new Runnable () {
//	    		public void run () {
//	    			JarmoService.JarmoLog  ("\n\n\n\nBMO 1mTimer Expired, locationCounter="+locationCounter);
//	    			JarmoService.JarmoLog  ("\n\n\n\nBMO 1mTimer Expired, locationCounter="+locationCounter);
//	    			JarmoService.JarmoLog  ("\n\n\n\nBMO 1mTimer Expired, locationCounter="+locationCounter);
//	    			JarmoService.JarmoLog  ("\n\n\n\nBMO 1mTimer Expired, locationCounter="+locationCounter);
//	    			JarmoService.JarmoLog  ("\n\n\n\nBMO 1mTimer Expired, locationCounter="+locationCounter);
//	    			getCurrentLocation ();
//	    			
//	                locationCounter++;
//		    		  if (locationCounter >=LOCPERFILE+1) {
//		    				locationCounter=1;
//		    				uploadFile (sd+"/temp/"+currentLocationFilename, "loc/"+currentLocationFilename);
//		    				JarmoService.JarmoLog  ("Torlese ennek ="+currentLocationFilename);
//		    				//new File (sd+"/temp/"+currentLocationFilename).delete();
//		    				currentLocationFilename=filenameForLocation();			
//		    	      }
//
//	                  start1mTimer ();
//
//	    		}
//	    	}, LOCTIME*1000);
//	    	
//	    }
//	*/    
//
//	    @Override
//	    public IBinder onBind(Intent intent) {
//	        return mBinder;
//	    }
//	    
//	    
//	    public String filenameForLocation () {
//	    	JarmoService.JarmoLog  ("Ez lenne az sd"+sd);
//	    	return "/jl_"+getPhoneNumber()+"_"+getCurrentTimestamp()+".log";
//	    }
//	    
//	    public String getCurrentTimestamp () {
//	    	Date d = new Date();
//	    	return d.getMonth() +"_"+ d.getDate() +"_"+ d.getHours() +"_"+ d.getMinutes() +"_"+ d.getSeconds();
//	    }
//	    
//	    public static String getPhoneNumber () {
//	    	TelephonyManager tMgr =(TelephonyManager)JarmoService.jarmoService.getSystemService(Context.TELEPHONY_SERVICE);
//	    	JarmoService.JarmoLog  ("TMGR "+tMgr.getNetworkOperatorName());
//	    	 String myPhoneNumber = tMgr.getLine1Number();
//	    	 if (myPhoneNumber==null) myPhoneNumber="";
//	    	// if (myPhoneNumber=="") tMgr.getSimSerialNumber();
//	    //	 if (myPhoneNumber.equals("")) myPhoneNumber=android.os.Build.MODEL;
//	    	 if (myPhoneNumber.equals("")) myPhoneNumber="unknown";
//	    	 JarmoService.JarmoLog  ("telszam="+myPhoneNumber);
//	    	 return myPhoneNumber;
//	    }
//	    
//	   
//	    
//	/*    
//	    public void getCurrentLocation () {
//	    	
//	    	JarmoService.JarmoLog  ("Getting current location");
//
//            // Define a listener that responds to location updates
//            LocationListener locationListener = new LocationListener() {
//                public void onLocationChanged(Location location) {
//                  // Called when a new location is found by the network location provider.
//                    JarmoService.JarmoLog  ("\n\n\n\n\n\n\n!!!!!!!!!!!!!!BMO pos="+location.getLongitude()+" "+location.getLatitude());
//                  locationManager.removeUpdates(this);
//                  appendToFile (location.getLatitude()+", "+location.getLongitude()+",   accurracy="+location.getAccuracy() +", "+getCurrentTimestamp(), 
//                                currentLocationFilename);
//      
//                     }
//
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//                	JarmoService.JarmoLog  ("Loc status changed: "+provider+"status ="+status);
//                }
//
//                public void onProviderEnabled(String provider) {
//                   	JarmoService.JarmoLog  ("Location provider ensabled: "+provider);
//                    
//                }
//
//                public void onProviderDisabled(String provider) {
//                	JarmoService.JarmoLog  ("Location provider Disabled: "+provider);
//                }
//              };
//
//              Criteria crit = new Criteria();
//              crit.setAccuracy(Criteria.ACCURACY_COARSE);
//              crit.setAltitudeRequired(false);
//        //      crit.setSpeedAccuracy(Criteria.NO_REQUIREMENT);
//              crit.setSpeedRequired(false);
//        //      crit.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
//             
//              String best = locationManager.getBestProvider(crit, true);
//         //     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//              locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//         //     locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);    
//              JarmoService.JarmoLog  ("Isprovider ="+locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
//              
//              
//	    }
//
//	    
//	    public void appendToFile (String line, String filename) {
//	      try {	
//	    	File file = new File(sd+"/temp/"+filename);
//	    	JarmoService.JarmoLog  ("--- appending to file="+sd+"/temp/"+filename);
//	   		FileWriter filewriter = new FileWriter(file, true);
//            BufferedWriter out = new BufferedWriter(filewriter);
//            out.write(line+"\n");
//            out.flush();
//            filewriter.flush();
//            filewriter.close();                
//	      } catch (Exception e) {e.printStackTrace();}
//       	
//	    }
//
//	    
//        public static void uploadFile (String fromFile, String toFile) {
//        	
//            JarmoService.JarmoLog  ("\n\n\n\n\n+++++++Net start, fromfile ="+fromFile+" toFile ="+toFile);
//                       try {
//               FTPClient f = new FTPClient();
//               f.connect("capaco.cvn.hu",21);
//               f.login("mbd-hu","cickafark");
//               f.setFileType(FTPClient.BINARY_FILE_TYPE, FTPClient.BINARY_FILE_TYPE);
//               f.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
//     
//               FileInputStream fis = new FileInputStream(fromFile);
//               f.storeFile(toFile, fis);
//               JarmoService.JarmoLog  ("BMO Ftp status"+f.getStatus());
//               f.logout(); 
//               f.disconnect(); 
//            } catch (Exception e)
//            {
//            	e.printStackTrace();
//            	//repeat later?
//            }
//        }
//        
//      */  
//        
//        /////////////////////////////////////////////////////////////////
//        
        public static void JarmoLog (String s) {
        	if (JarmoService.logging==0) return;
        	Log.d("Jarmo", s);
        }
//        
    	public static String[] parseTextToScreen (String in, Paint p, int width) {
    		String []words=in.split("[ ]");
    		ArrayList<String> lines=new ArrayList<String>();
    		int count=0;
    		int linecount=0;
    		
    		lines.add("");
    		while (count<words.length) {
    //			System.out.println ("Words "+words[count]);
    			if (p.measureText(lines.get(linecount)+words[count]+" ") < width) {
    		//	if ((lines.get(linecount)+words[count]).length() < 30 && (words[count].indexOf('%')==-1)) { // max sor length
    				lines.set(linecount, lines.get(linecount) + words[count] + " ");
    			} else { //nem fer el, uj sor
    				words[count]=words[count].replace('%', ' ');
    				linecount++;
    				lines.add(words[count]+" ");
    			}
    		//	JarmoService.JarmoLog  (linecount+" "+lines[linecount]);
    			count++;
    		}
    		
    		return (String[])lines.toArray(new String[0]);
    	}
//
//        
//        public static String getContactName(final String phoneNumber) 
//        {  
//            Uri uri;
//            String[] projection;
//
//            if (Build.VERSION.SDK_INT >= 5)
//            {
//                uri = Uri.parse("content://com.android.contacts/phone_lookup");
//                projection = new String[] { "display_name" };
//            }
//            else
//            { 
//                uri = Uri.parse("content://contacts/phones/filter");
//                projection = new String[] { "name" }; 
//            } 
//
//            uri = Uri.withAppendedPath(uri, Uri.encode(phoneNumber)); 
//            Cursor cursor = JarmoService.jarmoService.getContentResolver().query(uri, projection, null, null, null); 
//
//            String contactName = phoneNumber;
//
//            if (cursor.moveToFirst()) 
//            { 
//                contactName = cursor.getString(0);
//            } 
//
//            cursor.close();
//            cursor = null;
//
//            return contactName; 
//        }  
//        
//        
// 	   public static void startSocketListener () {
// 		   JarmoService.JarmoLog  ("BMO startSocketListener()");
// 		   if (JarmoService.serverSocket!=null) return;
// 		   
// 		   try {
// 			   JarmoService.serverSocket = new ServerSocket (JarmoSetting.DATAPORT);
// 			   JarmoService.serverSocket.setReuseAddress(true);
// 			  JarmoService.serverSocket.setSoTimeout(100);
// 			   JarmoService.JarmoLog  ("BMO ServerSocket started: "+serverSocket.getLocalSocketAddress().toString());
// 			   mySocketListener = new MySocketListener ();
// 			   if (android.os.Build.VERSION.SDK_INT > 11) {
// 				   mySocketListener.executeOnExecutor (AsyncTask.THREAD_POOL_EXECUTOR, 0);
// 			   } else {
// 				  mySocketListener.execute (0);
// 		       }
//	  	  
// 		   } catch (Exception e) {
// 			   e.printStackTrace();
// 		   }
// 	   }
//
//        
//        static void resetWifiConnectionData () {
//        	JarmoService.JarmoLog  ("BMO reseting wifi connection");
//        	JarmoService.remoteWifiIP="";
//        	JarmoService.pinger.stop=1;
//        	if (JarmoService.mySocketListener.isListening==1) {
//	    		JarmoService.mySocketListener.reset=1;
//	    	}
//
//        	try{
//     //   	JarmoService.socket.setReuseAddress(true);	
//        	JarmoService.socket.close();
//        	JarmoService.socket=null;
//        	}catch (Exception e) {}
//        }
//        
//        static int sendToSocket (String line) {
//        	try {
//        		JarmoService.JarmoLog  ("SendToSocket start, line"+line+"target="+JarmoService.remoteWifiIP);
//        		
//          //     JarmoService.socket=new Socket();
//        		if (JarmoService.socket==null && JarmoService.remoteWifiIP!="") {
//        			JarmoService.JarmoLog  ("SendToSocket new socket created.");
//        			JarmoService.socket=new Socket();	
//        			JarmoService.socket.setReuseAddress(true);
//        			JarmoService.socket.connect(new InetSocketAddress(JarmoService.remoteWifiIP, JarmoSetting.DATAPORT), 3000);
//        		}
//               PrintWriter pw = new PrintWriter(JarmoService.socket.getOutputStream(),true);
//			   pw.write(""+line+"\r\n");
//			   pw.flush();
//		//	   socket.close();
//			   JarmoService.lastLineSent=line;
//				JarmoService.JarmoLog  ("SendToSocket OK.");	        	   
//			   return 1;
//        	} catch (Exception e) {
//        	//	socket=null; 
//        		e.printStackTrace();
//        		return 0;
//        	}
//        	
//        }
//	
//}
//
//class MySocketListener extends AsyncTask<Integer, Integer, Integer> {
//
//	public int isStopped = 0;
//	public int reset =0;
//	public int isListening=0;
//	public int isClientSocket = 0;
//	
//	public Object sync=new Object();
//
//	@Override
//	protected Integer doInBackground(Integer... arg0) {
//		while (true) {
//			try {
//				JarmoService.JarmoLog  ("elso accept()-re varva");
//
//				Socket f_socket = null;
//				while (true) {
//					try {
//						if (isClientSocket==1) {
//							JarmoService.JarmoLog  ("ClientSocket, no more accept waiting");
//							break;
//						}
//						if (isStopped==0) {
//							//	JarmoService.JarmoLog  ("Accept wait");
//							JarmoService.serverSocket.setReuseAddress(true);
//							f_socket=JarmoService.serverSocket.accept();
//							break;
//						} else {
//							synchronized (sync) {
//								sync.wait(100);
//							}              			
//						}
//						//delay
//
//					} catch (Exception e) {
//						//e.printStackTrace();
//					}
//				}
//				if (isClientSocket==0) {
//		    		JarmoService.socket=f_socket;
//			    	JarmoService.socket.setReuseAddress(true);
//				} else {
//					f_socket=JarmoService.socket;
//				}
//				
//				if (JarmoService.remoteWifiIP!="" && f_socket!=null) {
//					String currIp=f_socket.getInetAddress().getHostAddress().toString();
//		//			currIp=currIp.substring(0, currIp.indexOf("/"));
//					JarmoService.JarmoLog  ("reconnect, ip compare, old="+JarmoService.remoteWifiIP+" new="+currIp);
//					if (!currIp.equals(JarmoService.remoteWifiIP)) {
//						PrintWriter pw = new PrintWriter(f_socket.getOutputStream(),true);
//						JarmoService.JarmoLog  ("Foreign ip, closing connection "+currIp);
//						pw.write("JARMO BUSY.\r\n");
//						pw.flush();
//						f_socket.close();
//						continue;
//					}
//				}
//				JarmoService.JarmoLog  ("ServerSocket Accepted connection from "+JarmoService.socket.getRemoteSocketAddress().toString());
//		
//				isListening=1;
//				isClientSocket=0; //itt mar nem szamit, de legkozelebb ne legyen emiatt baj
//				BufferedReader br = new BufferedReader(new InputStreamReader(JarmoService.socket.getInputStream())); 
//				while (JarmoService.socket.isConnected()) {
//						JarmoService.JarmoLog  ("Sorra varunk");
//					//JarmoService.socket.setSoTimeout(100);
//					String inLine=null;
//					while (inLine==null) {
//						if (reset==1) {break;}	
//						try {
//							//	JarmoService.JarmoLog  ("Readline wait");
//
//							if (isStopped==0) inLine = br.readLine(); 
//							else {
//								synchronized (sync) {
//									sync.wait(100);
//								}								
//							}
//						}
//						catch (Exception e) {
//							//e.printStackTrace();
//						}
//
//					} //while
//
//					if (reset==1) {break;}
//
//					JarmoService.JarmoLog  ("Received ="+inLine);
//					//	JarmoService.socket.close();
//					synchronized (sync) {
//						sync.wait(100);
//					}              			
//			
//					if (!JarmoService.lastLineSent.equals("") &&
//							JarmoService.lastLineSent.trim().equals(inLine.trim())) {
//						JarmoService.JarmoLog  ("Echo, eldob" +inLine);
//						continue;
//					}
//
//			//		    	if (inLine.startsWith("1 YES I AM JARMO")) {
//			//					JarmoSetting.wifiSearch_done++;
//			//					JarmoSetting.wifiSearch_ok++;
//			//					JarmoService.socket=null;
//			//					int res=JarmoService.sendToSocket("CONNECT WIFI "+JarmoService.getPhoneNumber());
//			//				}
//
//					if (inLine.startsWith("1 CONNECT ACCEPTED ")) {
//						//   Intent i = new Intent(JarmoService.JARMOSOCKET);
//						//   i.putExtra("line", "RECV  "+inLine); 
//						//   jarmoService.sendBroadcast(i);
//						JarmoService.remoteWifiIP=f_socket.getRemoteSocketAddress().toString();
//						JarmoService.remoteWifiIP=JarmoService.remoteWifiIP.substring(0, JarmoService.remoteWifiIP.indexOf("/"));						
//						JarmoSetting.enemyId=inLine.substring(19);
//						
//					//	JarmoSetting.enemyId = JarmoService.getContactName(JarmoSetting.enemyId);
//								
//						JarmoService.JarmoLog  ("MEgvan enemy ="+JarmoSetting.enemyId);
//					}
//					
//					if ((inLine.indexOf("YOU START")>-1
//						 || inLine.indexOf("I START")>-1
//						 || inLine.indexOf("BOGYO DOB")>-1
//						 || inLine.indexOf("BOGYO ENGED")>-1
//						 || inLine.indexOf("BOGYO MOVE")>-1
//						 || inLine.indexOf("WIFI PAUSED")>-1
//						 || inLine.indexOf("WIFI RESUMES")>-1
//						 || inLine.indexOf("WIFI GAME STOP")>-1
//						 || inLine.indexOf("BOGYO STARTMOVE")>-1 
//						 ) &&
//						JarmoSetting.currentState==JarmoSetting.CurrentState.STATE_IN_WIFIGAME) {
//						Intent i = new Intent(JarmoService.JARMOSOCKET);
//						i.putExtra("line", inLine); 
//						JarmoService.jarmoService.sendBroadcast(i);				
//					}
//					
//					if ((inLine.indexOf("1 WIFI GAME ACCEPTED")>-1) ||
//							(inLine.indexOf("0 WIFI GAME REJECTED")>-1)) {
//						JarmoService.JarmoLog  ("Broadcasting command="+inLine);
//						Intent i = new Intent(JarmoService.JARMOSOCKET);
//						i.putExtra("line", "RECV  "+inLine); 
//						JarmoService.jarmoService.sendBroadcast(i);			
//					}
//					if (inLine.indexOf("PING")>-1) {
//						JarmoService.sendToSocket ("PONG");						
//					}
//					if (inLine.indexOf("PONG")>-1) {
//						Pinger.response=inLine;					
//					}
//
//					if (inLine.indexOf("ARE YOU JARMO?")>-1) {
//						JarmoService.remoteWifiIP=f_socket.getInetAddress().getHostAddress().toString();  //f_socket.getRemoteSocketAddress().toString();
//						String verzio = inLine.substring(15,18);
//						JarmoService.JarmoLog  ("Tuloldali verzio ="+Integer.parseInt(verzio));
//						try {
//						   if (Integer.parseInt(verzio) > JarmoService.JARMOVERSION) {
//								JarmoService.sendToSocket ("0 JARMO VERSION NOT SUPPORTED");
//								Intent i = new Intent(JarmoService.JARMOSOCKET);
//								i.putExtra("line", "VERSION TOO OLD"); 
//								JarmoService.jarmoService.sendBroadcast(i);			
//										break;
//						   }
//						}	catch (Exception e) {e.printStackTrace();}						   
//						JarmoService.JarmoLog  ("Got Remote IP"+JarmoService.remoteWifiIP);
//						JarmoService.sendToSocket ("1 YES I AM JARMO");
//					//	JarmoService.resetWifiConnectionData();
//
//						   break;
//					}
//					if (inLine.indexOf("CONNECT WIFI")>-1) {
//						if (!JarmoService.pinger.isAlive()) {
//							JarmoService.pinger = new Pinger ();
//							JarmoService.pinger.start ();
//						}
//						inLine=inLine.substring(inLine.indexOf("CONNECT WIFI"));
//						
//						JarmoService.sendToSocket ("1 CONNECT ACCEPTED "+JarmoSetting.getUsername());//JarmoService.getPhoneNumber()
//						JarmoSetting.enemyId=inLine.substring(12);
//					//	JarmoSetting.enemyId = JarmoService.getContactName(JarmoSetting.enemyId);
//						JarmoService.JarmoLog  ("MEgvan enemy ="+JarmoSetting.enemyId);
//
//						JarmoService.JarmoLog  ("Kezdodik");
//						JarmoService.JarmoLog  ("Setting Remote wifi ip"+JarmoService.remoteWifiIP);
//						if (JarmoSetting.isRunning==0) {
//							Intent intent = new Intent(JarmoService.jarmoService, JarmoSetting.class);
//							intent.setAction(JarmoService.JARMOREQUEST);
//
//							PendingIntent pIntent = PendingIntent.getActivity(JarmoService.jarmoService, 0, intent, 0);
//
//							Notification noti = new Notification (R.drawable.archer, "Jarmo game request", System.currentTimeMillis());
//
//							noti.setLatestEventInfo(JarmoService.jarmoService, "Jarmo request on WiFi", "Sender: "+JarmoSetting.enemyId,
//									PendingIntent.getActivity(JarmoService.jarmoService, 1, intent, 0));
//
//							NotificationManager notificationManager = 
//									(NotificationManager) JarmoService.jarmoService.getSystemService(JarmoService.jarmoService.NOTIFICATION_SERVICE);
//
//							// Hide the notification after its selected
//							noti.flags |= Notification.FLAG_AUTO_CANCEL;
//							noti.defaults |= Notification.DEFAULT_ALL;
//
//							notificationManager.notify(JarmoService.NOTIFY_ID, noti); 
//							JarmoService.JarmoLog  ("BMO Notify OK");
//							//   break;
//
//						} else {
//							JarmoService.JarmoLog  ("Trying directly");
//							Intent intent = new Intent(JarmoService.JARMOSOCKET);
//							intent.putExtra("line", "INCOMING CALL"); 
//							JarmoService.jarmoService.sendBroadcast(intent);
//							JarmoService.JarmoLog  ("Lekezelve");
//
//							//   break;
//						}
//
//					} //WIFI CONNECT
//				} //while connected
//				if (JarmoSetting.currentState==JarmoSetting.CurrentState.STATE_IN_WIFIGAME) {
//					Intent intent = new Intent(JarmoService.JARMOSOCKET);
//					intent.putExtra("line", "CONNECT CLOSED"); 
//					JarmoService.jarmoService.sendBroadcast(intent);				
//				}
//				try {
//				JarmoService.socket.close();
//				} catch (Exception e) {}
//				JarmoService.JarmoLog  ("Connection closed.");
//				isListening=0;
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			isListening =0;
//			JarmoService.JarmoLog  ("nullazas 1");
//			reset=0;
//			JarmoService.socket=null;
//		}
//		//return 0;
//	}	
}
//
//class Pinger extends Thread {
//	
//	public static String response ="";
//	public int stop=0;
//	
//	public Pinger () {
//		JarmoService.JarmoLog  ("Pinger created.");
//	}
//	
//	public void run () {
//
//		stop=0;
//		while (true) {
//			try {
//				JarmoService.JarmoLog  ("Pinger, stop ="+stop);
//				if (stop==1) break;
//				response="";
//				synchronized (response) {
//					response.wait(3000);
//				}
//				int res = JarmoService.sendToSocket("PING");
//				JarmoService.JarmoLog  ("Ping sent, res="+res);
//					
//				synchronized (response) {
//					response.wait(10000);
//				}
//				if (!response.startsWith("PONG")) {
//					JarmoService.JarmoLog  ("Ping timeout");
//					Intent intent = new Intent(JarmoService.JARMOSOCKET);
//					intent.putExtra("line", "CONNECT CLOSED"); 
//					JarmoService.jarmoService.sendBroadcast(intent);
//
//				}
//			} catch (Exception e) {e.printStackTrace();}
//		}
//	}
//}
//
