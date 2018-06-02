package com.mbd.jarmo;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.concurrent.Executor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import com.mbd.jarmo.R;
import com.mbd.jarmo.JarmoMain;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Shader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.util.EventLog.Event;
import android.util.Xml;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class JarmoSetting extends Activity {
	public static int WIDTH=0;
	public static int HEIGHT=0;
	public static int MENUWIDTH;
	public static int MENUHEIGHT;
	public static int TEXTSIZE;
	
	public static int isRunning=0;

	public static int adHeight=0;
	//public static AdView adView;
	
	public static View nyitoView;

	public static EventDetector ed;
	public static FrameLayout main;
//	public static FrameLayout menuoldal2;
	public static Bundle staticInstanceState;
	
	public static Handler handler;
	public static FrameLayout menuoldal;
	public static Activity js;
	public static int nyitoMode; //0-normal, 1-wifi
	
	public static int DATAPORT=35347;
	public static EgyMenu backMenu;
	
	public static final String SPREF_MYRECORD = "JarmoSharedPref";
	
//	public static Object panelCallback;
//	public static int panelCallbackIndex;
//	public static Smalltext currentText;
	public static int wifiSearch_done=0;
	public static int wifiSearch_ok=0;
	public static int wifiSearch_nok=0;
	
	public static String enemyId;
	public static int valasztottszin=0;
	public static TryWifiNeighbourhood wifiSearch;
	
	public static CurrentState currentState;
	
	public static EditText nameEditText;
	
	public static InputStream inputStreamFeher, inputStreamSzurke, inputStreamAndroid, inputStreamSargaAndroid;
	public static Bitmap mBitmapFeher, mBitmapSzurke, mBitmapAndroid, mBitmapSargaAndroid;


	public static Bitmap [] arrowBitmaps = new Bitmap [50];
	public static Bitmap arrowOnly;
	
	public static Bitmap nyitoBitmap;
	public static int loaded=0;
	
	public static Bitmap radarBitmap;
    public static String rekordHttpResp="";
    public static BitmapShader mezoShader;
	
	public static Typeface tf, tfa;

	public static enum CurrentState { 
		STATE_IDLE, 
		STATE_WIFISEARCH,
		STATE_INCOMING_REQUEST,
		STATE_WAIT_RESPONSE,
		STATE_IN_WIFIGAME,
		STATE_IN_LOCALGAME
		}
    
	public static EgyMenu mm1, mm2, mm3, mm4;
	
	public static Context vcontext;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		JarmoService.JarmoLog  ("JST1");
		setContentView(R.layout.activity_jarmo_settings);
		JarmoService.JarmoLog  ("JST2");
		JarmoSetting.main = (FrameLayout) findViewById(R.id.main_view);   
		//		     JarmoSetting.main.setBackgroundResource(R.drawable.bground);
		JarmoService.JarmoLog  ("JST3");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		JarmoService.JarmoLog  ("JST4");
		//  	         vcontext = JarmoMain.main.getContext()
		JarmoSetting.nyitoMode=0;
		JarmoService.JarmoLog  ("JarmoSetting nyito intent action="+getIntent().getAction());
//GDPR
/*		if (getIntent().getAction().equals(JarmoService.JARMOREQUEST)) {
			JarmoService.JarmoLog  ("JST5");
			JarmoSetting.nyitoMode=1;
			JarmoSetting.currentState=CurrentState.STATE_INCOMING_REQUEST;
		}
*/		

		handler = new Handler ();
		js=this;
		tf = Typeface.createFromAsset(this.getAssets(), "MTCORSVA.TTF");
		tfa = Typeface.createFromAsset(this.getAssets(), "ARIALNBI.TTF");
		this.setVolumeControlStream (AudioManager.STREAM_NOTIFICATION);
		
		menuoldal = new FrameLayout (JarmoSetting.main.getContext());

      //  JarmoSetting.adHeight=
      //  		(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()) +
      //          getResources().getDisplayMetrics().widthPixels/50;
       
		JarmoSetting.handler.removeCallbacksAndMessages(null);
		JarmoSetting.handler.postDelayed(new Runnable() {
			public void run () {	

				DisplayMetrics metrics =JarmoSetting.main.getContext().getResources().getDisplayMetrics();
				JarmoSetting.WIDTH = metrics.widthPixels;
				JarmoSetting.HEIGHT = metrics.heightPixels;
				
				JarmoSetting.main.addView(new View (JarmoSetting.js) {
					public Rect rect = new Rect (0,0,JarmoSetting.WIDTH, JarmoSetting.HEIGHT);
					private AssetManager assetManager;
					private InputStream bmpStream, mezobmpStream;
					private Bitmap hatterBitmap, mezoBitmap;
					private Paint p;
					private Paint pkocka;
					{
						JarmoSetting.nyitoView=this;
						JarmoService.JarmoLog  ("Belso class constructor");
						assetManager = JarmoSetting.js.getAssets();
						p=new Paint ();
						pkocka=new Paint ();
						pkocka.setTextSize((int)(JarmoSetting.HEIGHT*0.02));
						pkocka.setTextAlign(Align.RIGHT);
						pkocka.setColor(0xFF888888);
						try {
							bmpStream = assetManager.open("nyito1.jpg");
							hatterBitmap = BitmapFactory.decodeStream(bmpStream);
							mezobmpStream = assetManager.open("mezobg.jpg");
							mezoBitmap = BitmapFactory.decodeStream(mezobmpStream);
							JarmoSetting.nyitoBitmap = hatterBitmap;
						}catch (Exception e) {e.printStackTrace();}
						mezoShader = new BitmapShader (mezoBitmap, Shader.TileMode.MIRROR, Shader.TileMode.REPEAT); 
						JarmoSetting.nyitoView.invalidate();
					}

					public void onDraw (Canvas canvas) {
						JarmoService.JarmoLog  ("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ Inner class onDraw");
						this.layout(0,0, JarmoSetting.WIDTH, JarmoSetting.HEIGHT);
						canvas.drawBitmap(hatterBitmap, null, rect, p);
						canvas.drawText("VER 2.40 * (C)MBD", JarmoSetting.WIDTH, (int)(JarmoSetting.HEIGHT*0.98), pkocka);
						
						
					}
		
				});

			}
		},100);

		JarmoSetting.handler.postDelayed(new Runnable() {
			public void run () {		
				JarmoService.JarmoLog  ("Dimensions "+JarmoSetting.WIDTH+" "+JarmoSetting.HEIGHT);
				JarmoSetting.MENUWIDTH = (int)(JarmoSetting.WIDTH*0.8);
				JarmoSetting.MENUHEIGHT = (int)(JarmoSetting.HEIGHT*0.8);
				JarmoSetting.TEXTSIZE = (int)JarmoSetting.WIDTH/25;
				JarmoService.JarmoLog  ("Nyitomode="+JarmoSetting.nyitoMode);

				initArrows ();
				JarmoSetting.main.removeAllViews();
				JarmoSetting.main.invalidate();
				JarmoSetting.nyitoBitmap.recycle();
				
				JarmoService.JarmoLog("nyitomode="+nyitoMode+" isOnline="+isOnline());

				if (JarmoSetting.nyitoMode==0) nyitoMenu ();	           
				if (JarmoSetting.nyitoMode==1) incomingCall (1);	
					menuNyit ();
				JarmoSetting.nyitoView.invalidate();

			}
		},2000);

		//        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
		IntentFilter intentFilter = new IntentFilter();
//GDPR		intentFilter.addAction(JarmoService.JARMOSOCKET);
//GDPR		intentFilter.addAction(JarmoService.JARMOREQUEST);
//GDPR		this.registerReceiver(jarmoReceiver, intentFilter);
		JarmoService.JarmoLog  ("JSetting registered$$$$");

	} //onCreate
	
	public void onBackPressed () {
		JarmoService.JarmoLog("back pressed");
		if (JarmoSetting.backMenu!=null) {
			JarmoService.JarmoLog("van back");			
			JarmoSetting.backMenu.onTouchEvent(MotionEvent.obtain(1, 1, MotionEvent.ACTION_DOWN, 0, 0, 0));
		} else 
			this.finish();
	}
	
//GDPR
/*	
	     public static BroadcastReceiver jarmoReceiver = new BroadcastReceiver() {
	         @Override
	         public void onReceive(Context context, Intent intent) {
	         	JarmoService.JarmoLog  ("jarmo onReceive: "+intent.getAction());
	             if(intent.getAction().equals(JarmoService.JARMOREQUEST)) {
	            	 JarmoSetting.currentState=CurrentState.STATE_INCOMING_REQUEST;
	            	 incomingCall (1);
	            	 menuNyit ();
	            	 return;
	             }
	             if(intent.getAction().equals(JarmoService.JARMOSOCKET)) {
	                 String line = intent.getStringExtra("line");
	                 if (line.startsWith ("INCOMING CALL")) {
	                	 JarmoService.JarmoLog  ("Incoming call");
	                	 JarmoSetting.currentState=CurrentState.STATE_INCOMING_REQUEST;
	                	 incomingCall (1);
	 	            	 menuNyit ();
		            	 return;	                	 
	                 }
	                 if (line.indexOf ("1 WIFI GAME ACCEPTED")>-1) {
	                	 JarmoSetting.currentState=CurrentState.STATE_IN_WIFIGAME;
	                	 JarmoService.JarmoLog  ("JarmoSetting, game accepted");
	                	 JarmoSetting.jarmoMessage (JarmoSetting.enemyId+" "+JarmoSetting.js.getString(R.string.AcceptedGame));	         	           
	                	 JarmoSetting.startGame(4, 0, 0, JarmoSetting.enemyId); //passziv
	                 }
	                 
	                 if (line.indexOf("VERSION TOO OLD")>-1) {
	                	 if (JarmoSetting.wifiSearch_ok<=0) {
			     		 		   JarmoSetting.jarmoMessage (JarmoSetting.js.getString(R.string.JarmoNotUptodate));           
			    		           menuNyit ();	                		   
		                	 }                	 
	                 }
	                 
	                 if (line.indexOf("WIFI GAME STOP")>-1) {
               	          		   JarmoService.resetWifiConnectionData();
	              		 		   JarmoSetting.jarmoMessage (JarmoSetting.js.getString(R.string.OtherPartyLeftGame));	           
			    		           menuNyit ();	                		   
		                	                	 
	                 }

	                 if (line.indexOf("0 WIFI GAME REJECTED")>-1) {
	                	 JarmoSetting.currentState=CurrentState.STATE_IDLE;
	              	     JarmoService.resetWifiConnectionData();
	                     JarmoSetting.wifiSearch_ok--;
	                	 if (JarmoSetting.wifiSearch_ok<=0) {
		     		 		   JarmoSetting.jarmoMessage (JarmoSetting.js.getString(R.string.OtherPartyRejected));	           
		    		           menuNyit ();	                		   
	                	 }
	                     JarmoService.JarmoLog  ("rejected, megjelenit");
	                 }
	
                     if (line.startsWith("CONNECT CLOSED")) {
                    	   if (JarmoMain.jarmoMain!=null) JarmoMain.jarmoMain.finish();
                    	   JarmoService.resetWifiConnectionData();
                    	   JarmoSetting.currentState=CurrentState.STATE_IDLE;
	                    	JarmoService.JarmoLog  ("connect closed, megjelenit");
	     		 		    JarmoSetting.jarmoMessage (JarmoSetting.js.getString(R.string.OtherPartyLeftGame));	           
	    		           menuNyit ();
	                 }

	             }
	         }
	     };
	     
	     */
	
	     public static void initArrows () {
	    	 AssetManager assetManager = JarmoSetting.js.getAssets();
	    	 
	    	 try {
	    		 InputStream arrowStream = assetManager.open("arrowonly.png");
	    		 if (arrowStream==null) {JarmoService.JarmoLog  ("nyilStream null");}
	    		 JarmoSetting.arrowOnly = BitmapFactory.decodeStream(arrowStream);
	    	 } catch (Exception e) {e.printStackTrace();}
	    	 
	         //Bitmap preparation
	       	try {
	         	  inputStreamAndroid = assetManager.open("androidicon.png");
	         	  inputStreamSargaAndroid = assetManager.open("androidicongold.png");
	         	  inputStreamFeher = assetManager.open("archergray2.png");
	         	  inputStreamSzurke = assetManager.open("archergray.png");
	       	      mBitmapFeher = BitmapFactory.decodeStream(inputStreamFeher);
	       	      mBitmapSzurke = BitmapFactory.decodeStream(inputStreamSzurke);
	       	      mBitmapAndroid = BitmapFactory.decodeStream(inputStreamAndroid);
	       	      mBitmapSargaAndroid = BitmapFactory.decodeStream(inputStreamSargaAndroid);
	           } catch (Exception e) {
	       		e.printStackTrace();
	       	}

	    	 
	    	 for (int t=1;t<=9;t++) {
	    		 //fekete1
	    		 try {
	    			 JarmoService.JarmoLog  ("initarrows b1 t="+t);	
	    			 InputStream nyilStream = assetManager.open("black1000"+t+".png");
	    			 if (nyilStream==null) {JarmoService.JarmoLog  ("nyilStream null");}
	    			 JarmoSetting.arrowBitmaps[0+t] = BitmapFactory.decodeStream(nyilStream);
	    			 JarmoService.JarmoLog  ("initarrows b2 t="+t);	
	    			 nyilStream = assetManager.open("black2000"+t+".png");
	    			 JarmoSetting.arrowBitmaps[10+t] = BitmapFactory.decodeStream(nyilStream);

	    			 JarmoService.JarmoLog  ("initarrows w1 t="+t);	
	    			 nyilStream = assetManager.open("white1000"+t+".png");
	    			 JarmoSetting.arrowBitmaps[20+t] = BitmapFactory.decodeStream(nyilStream);
	    			 JarmoService.JarmoLog  ("initarrows w2 t="+t);	
	    			 nyilStream = assetManager.open("white2000"+t+".png");
	    			 JarmoSetting.arrowBitmaps[30+t] = BitmapFactory.decodeStream(nyilStream);
	    			 nyilStream = assetManager.open("radar.png");
	    			 JarmoSetting.radarBitmap = BitmapFactory.decodeStream(nyilStream);

	    		 } catch (Exception e) {
	    			 e.printStackTrace();
	    		 }

	    	 }
	     }

	   public void onDestroy () {  
		   super.onDestroy();
 // 	       Intent is = new Intent(JarmoSetting.js, JarmoService.class);
 //		   js.startService(is);        
	   }

	   public void onPause () {
		   super.onPause();
           JarmoSetting.isRunning=0;
    	   JarmoService.JarmoLog  ("JarmoSetting onpause");
 	   }

	   public void onStart () {
		   super.onStart();
           JarmoSetting.isRunning=1;
    	   JarmoService.JarmoLog  ("JarmoSetting onstart");
    	   Intent is = new Intent(JarmoSetting.js, JarmoService.class);
 //   		JarmoSetting.handler.removeCallbacksAndMessages(null);

		   js.startService(is);  

	   }

	   public void onResume () {
		   super.onResume();
		   
//		   System.out.println ("onResume");
	//	   JarmoSetting.handler.removeCallbacksAndMessages(null);

//Block commented out for GDPR reasons		   
//		   JarmoSetting.handler.postDelayed(new Runnable () {
//			   public void run () {
//			         SharedPreferences settings = JarmoSetting.js.getSharedPreferences(JarmoSetting.SPREF_MYRECORD, 0);
//			         JarmoSetting.getMyData ();
 //                    JarmoSetting.getToplista ();
 

//			   }
//		   }, 500);
 		   
		   JarmoService.JarmoLog  ("\n\n\n\n\n\n\nJarmoSetting onresume ++++++");
           //service
	
		   if (JarmoSetting.WIDTH==0) return;

		   if (menuoldal!=null) {
			   JarmoService.JarmoLog  ("redraw menu");
//		       menuoldal.layout ((int)(JarmoSetting.WIDTH*0.1), 
//		    		             (int)(JarmoSetting.HEIGHT*0.1), 
//		    		             (int)(JarmoSetting.WIDTH*0.9), 
//		    		             (int)(JarmoSetting.HEIGHT*0.9));		          
//			   menuoldal.invalidate();
			   JarmoSetting.main.removeAllViews();
			   JarmoSetting.main.invalidate();
			   JarmoSetting.handler.postDelayed (new Runnable () {
				  public void run () { 
					  System.out.println ("mehet");
		   		    JarmoSetting.nyitoMenu ();
                    JarmoSetting.menuNyit ();
				  }
			   }, 500);
			   return;// menuoldal.removeAllViews();
		   } else {
				if (JarmoSetting.nyitoMode==2) {
					if (isOnline()) {
//GDPR					   toplista ();
					} else 
					   JarmoSetting.main.removeAllViews();
					   JarmoSetting.main.invalidate();
					   JarmoSetting.handler.postDelayed (new Runnable () {
						  public void run () { 
							  System.out.println ("mehet2");
				   		    JarmoSetting.nyitoMenu ();
		                    JarmoSetting.menuNyit ();
						  }
					   }, 500);
	   		} else {
				   JarmoSetting.main.removeAllViews();
				   JarmoSetting.main.invalidate();
				   JarmoSetting.handler.postDelayed (new Runnable () {
					  public void run () { 
						  System.out.println ("mehet3");
			   		    JarmoSetting.nyitoMenu ();
	                    JarmoSetting.menuNyit ();
					  }
				   }, 500);
			}
              JarmoSetting.isRunning=1;
		   }
		   JarmoSetting.main.invalidate();
	   }
	   
	   
	   
	   public static void menuNyit () {
		   JarmoService.inGame=1;
		   if (menuoldal==null) return;
	//	   JarmoSetting.main.removeView(JarmoSetting.menuoldal);
		   final int toPos =(int)(JarmoSetting.WIDTH*0.1);
		   final int Ypos = (int)(JarmoSetting.HEIGHT*0.1);
	       //BMO
		   //JarmoSetting.menuoldal.layout(-JarmoSetting.MENUWIDTH*2, Ypos, JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);
	       //BMO 
		   JarmoSetting.menuoldal.layout(toPos, Ypos, toPos+JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);
	       JarmoSetting.main.invalidate();
		   JarmoService.JarmoLog  ("Screenwidth="+ JarmoSetting.WIDTH);
		  int c=1;
		   for (int t=0; t<=15; t++) {
			  JarmoService.JarmoLog  ("menunyit Itt "+t); 
			  c++;
			  final int cc=c;
		      final int tt=toPos+JarmoSetting.MENUWIDTH*(15-t)/15;
		      final int t1=t;
		      JarmoSetting.handler.postDelayed(new Runnable () {
			     public void run () {
			    	 System.out.println ("cc ="+cc);
			    	 //if (cc==3) System.exit(1);
			    	 //if (JarmoSetting.menuoldal==null) return;
//			    	 if (tt==0) JarmoSetting.main.addView(JarmoSetting.menuoldal);
			    	 JarmoService.JarmoLog  ("mozog "+tt);
			    	 JarmoSetting.main.setAlpha ((float)t1/15);
					   JarmoSetting.menuoldal.layout(toPos, Ypos, toPos+JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);

			//    	 JarmoSetting.menuoldal.layout(tt, Ypos, tt+JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);
			         JarmoSetting.menuoldal.invalidate();
			         JarmoSetting.menuoldal.setVisibility(View.VISIBLE);
			     }
			     
		      }, cc*25);
		   }
	   }

	   public static void menuZar () { //negativ iranyba, gyorsulva
		   if (menuoldal==null) return;
		   final int toPos =(int)(JarmoSetting.WIDTH*0.1);
		   final int Ypos = (int)(JarmoSetting.HEIGHT*0.1);
		  JarmoService.JarmoLog  ("Screenwidth="+ JarmoSetting.WIDTH);
		  int c=1;
		   for (int t=1; t<=18; t++) {
			  JarmoService.JarmoLog  ("menuzar Itt c="+c+" t="+t); 
			  c++;
			  final int cc=c;
		      final int tt=toPos-((toPos+JarmoSetting.MENUWIDTH*2)/15*t);
		      final int t1=t;
		      JarmoSetting.handler.postDelayed(new Runnable () {
			     public void run () {
			    	 JarmoService.JarmoLog  ("menuzar realtime "+cc);
			    	 if (cc==15) {
			    		 if (JarmoSetting.mm1!=null) JarmoSetting.mm1.destroyEgyMenu();
			    		 if (JarmoSetting.mm2!=null) JarmoSetting.mm2.destroyEgyMenu();
			    		 if (JarmoSetting.mm3!=null) JarmoSetting.mm3.destroyEgyMenu();
			    		 if (JarmoSetting.mm4!=null) JarmoSetting.mm4.destroyEgyMenu();
			    	 }
			    //	 if (cc>=15) return;
			    	 //JarmoService.JarmoLog  ("mozog-> "+cc+". hely ="+tt);
			    	 if (JarmoSetting.menuoldal==null) return;
//			    	 JarmoSetting.menuoldal.layout(tt, Ypos, tt+JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);
					   JarmoSetting.menuoldal.layout(toPos, Ypos, toPos+JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);
			    	 JarmoSetting.main.setAlpha (1-(float)t1/15);
			    	  JarmoSetting.main.invalidate();
			         JarmoSetting.menuoldal.setVisibility(View.VISIBLE);
			     }
			     
		      }, cc*25);
		   }
	   }

	   public static void menuVisszaBe () { //negativ iranybol, lassul
		   JarmoService.JarmoLog  ("Menuvisszabe ");
	//	   menuoldal=null;
		   
		   if (menuoldal==null) return;
		   final int toPos =(int)(JarmoSetting.WIDTH*0.1);
		   final int Ypos = (int)(JarmoSetting.HEIGHT*0.1);
		   JarmoSetting.menuoldal.layout(-JarmoSetting.MENUWIDTH*2, Ypos, JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);
    		JarmoSetting.menuoldal.invalidate();

		  JarmoService.JarmoLog  ("Screenwidth="+ JarmoSetting.WIDTH);
		  int c=1;
		   for (int t=2; t<=15; t++) {
			  c++;
			  final int cc=c;
		      final int tt;
		      final int t1=t;
		      if (t<15)
		        tt=-JarmoSetting.MENUWIDTH+((JarmoSetting.MENUWIDTH+toPos)/15*t);
		      else tt=toPos;
			  JarmoService.JarmoLog  ("Itt cc"+cc+"t="+tt); 
		      JarmoSetting.handler.postDelayed(new Runnable () {
			     public void run () {
			    	 if (JarmoSetting.menuoldal==null) return;
			    	 
			    	 JarmoService.JarmoLog  ("mozog "+tt);
	//		    	 JarmoSetting.menuoldal.layout(tt, Ypos, tt+JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);
					   JarmoSetting.menuoldal.layout(toPos, Ypos, toPos+JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);

			    	 JarmoSetting.main.setAlpha ((float)t1/15);
			    	  JarmoSetting.main.invalidate();
			         JarmoSetting.menuoldal.setVisibility(View.VISIBLE);
			     }
			     
		      }, cc*25);
		   }
	   }

	   public static void menuVisszaKi () { // pozitiv iranyba, gyorsul
		   if (menuoldal==null) return;
		   final int toPos =(int)(JarmoSetting.WIDTH*0.1);
		   final int Ypos = (int)(JarmoSetting.HEIGHT*0.1);
		  JarmoService.JarmoLog  ("Screenwidth="+ JarmoSetting.WIDTH);
		  int c=1;
		   for (int t=1; t<=18; t++) {
			  JarmoService.JarmoLog  ("Itt cc="+c+" ="+t); 
			  c++;
			  final int cc=c;
		      final int tt=toPos+(JarmoSetting.MENUWIDTH*2)/15*t;
		      final int t1=t;
		      JarmoSetting.handler.postDelayed(new Runnable () {
			     public void run () {
			    	 if (JarmoSetting.menuoldal==null) return;
			    	 if (cc==18) {
			    		 if (JarmoSetting.mm1!=null) JarmoSetting.mm1.destroyEgyMenu();
			    		 if (JarmoSetting.mm2!=null) JarmoSetting.mm2.destroyEgyMenu();
			    		 if (JarmoSetting.mm3!=null) JarmoSetting.mm3.destroyEgyMenu();
			    		 if (JarmoSetting.mm4!=null) JarmoSetting.mm4.destroyEgyMenu();
			    		 return;
			    	 }
			  //  	 if (cc>=15) return;
			    	 if (JarmoSetting.menuoldal==null) return;

			    	 JarmoService.JarmoLog  ("mozog "+tt);
			    	// JarmoSetting.menuoldal.layout(tt, Ypos, tt+JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);
    				   JarmoSetting.menuoldal.layout(toPos, Ypos, toPos+JarmoSetting.MENUWIDTH, Ypos+JarmoSetting.MENUHEIGHT);

			    	 JarmoSetting.main.setAlpha (1-(float)t1/15);
			    	  JarmoSetting.main.invalidate();
			    	
			    	 JarmoSetting.menuoldal.setVisibility(View.VISIBLE);
			     }
			     
		      }, cc*25);
		   }
	   }
/*
	   public static void androidMenu () {
		     JarmoService.JarmoLog  ("Androidmenu start");
	          if (menuoldal!=null) menuoldal.removeAllViews();

	         menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb2 = new LinearLayout (JarmoSetting.main.getContext());
	 
	         egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
	         egysorgomb2.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));

	          EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(),   JarmoSetting.js.getString(R.string.MenuBlackArchers1), JarmoSetting.js.getString(R.string.MenuBlackArchers2), "random.png", 6, false);
	           EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuWhiteArchers1), JarmoSetting.js.getString(R.string.MenuWhiteArchers2), "race.png", 5, false);
	           EgyMenu m3 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuRandomArchers1), JarmoSetting.js.getString(R.string.MenuRandomArchers2), "reset.png", 7, false);
	           EgyMenu m4 = new EgyMenu (JarmoSetting.main.getContext(),  JarmoSetting.js.getString(R.string.MenuBack), "", "back.png", 8, true);

	           
	           egysorgomb1.addView (m,0);
	           egysorgomb1.addView(m2,1);
	           egysorgomb2.addView(m3,0);
	           egysorgomb2.addView(m4,1);
	 
	         gombtarto.setOrientation(LinearLayout.VERTICAL);
	           
	         gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	         gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.setGravity(Gravity.CENTER);

	         menuoldal.setLayoutParams(
	  		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
	 
	           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuTitlePhone));           
	           gombtarto.addView(welcome, 0);                  
	           welcome.invalidate();
	           gombtarto.addView(egysorgomb1, 1);
	           gombtarto.addView(egysorgomb2, 2);
	           
	           menuoldal.addView(gombtarto);
	     
	           gombtarto.setBackgroundColor(0xFF111111);
	           JarmoSetting.main.addView (menuoldal);
	   }
*/
//commented out for GDPR reasons 2018	   
/*	   public static boolean setRecord (int newVal) {
		      SharedPreferences settings = JarmoSetting.js.getSharedPreferences(JarmoSetting.SPREF_MYRECORD, 0);
		      SharedPreferences.Editor editor = settings.edit();
		      editor.putInt("myRecord", newVal);
		      // Commit the edits!
		      editor.commit();
		      
		      
	          if (!JarmoSetting.isRecordAllowed()) return false;
		      
		      String username=JarmoSetting.getUsername();
              String country=JarmoSetting.getCountry();

		      if (username.equals("")) return false;
			  if (!JarmoSetting.isOnline()) return false;   
				
			  JarmoService.JarmoLog  ("Writing new record "+newVal);
			  try {
			     httpGet ("http://www.mbd.hu/jarmo/server/update_score.php?user="+username+"&score="+newVal+"&country="+country+"&");
			   } catch (Exception e) {
				   e.printStackTrace();
			   }		
			    //frissitsunk toplistat
	    	   JarmoSetting.getToplista ();
               return true;
	   }
*/	   
	   
	   
	   public static void android2Menu () {
		     JarmoService.JarmoLog  ("Androidmenu start");
	          if (JarmoSetting.menuoldal!=null) menuoldal.removeAllViews();

	         JarmoSetting.menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb2 = new LinearLayout (JarmoSetting.main.getContext());
	 
	         egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
	         egysorgomb2.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));

	          EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(),   JarmoSetting.js.getString(R.string.MenuClassicGame1), JarmoSetting.js.getString(R.string.MenuClassicGame2), "practice.png", 6, false);
	          EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuTournament1), JarmoSetting.js.getString(R.string.MenuTournament2), "race.png", 5, false);
	          EgyMenu m3 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuManageHighscores1), JarmoSetting.js.getString(R.string.MenuManageHighscores2), "toplist.png", 7, false);
	          EgyMenu m4 = new EgyMenu (JarmoSetting.main.getContext(),  JarmoSetting.js.getString(R.string.MenuBack), "", "back.png", 8, true);
		       JarmoSetting.backMenu=m4;

	          JarmoSetting.mm1=m;
	          JarmoSetting.mm2=m2;
	          JarmoSetting.mm3=m3;
	          JarmoSetting.mm4=m4;
	      	      	           
	           egysorgomb1.addView (m,0);
	           egysorgomb1.addView(m2,1);
	           egysorgomb2.addView(m3,0);
	           egysorgomb2.addView(m4,1);
	           egysorgomb1.invalidate();
	  	     
	         gombtarto.setOrientation(LinearLayout.VERTICAL);
	           
	         gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	         gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.setGravity(Gravity.CENTER);
	         
	         

	         JarmoSetting.menuoldal.setLayoutParams(
	  		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
	 
	           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuTitlePhone));           
	           gombtarto.addView(welcome, 0);                  
	           welcome.invalidate();
	           gombtarto.addView(egysorgomb1, 1);
	           gombtarto.addView(egysorgomb2, 2);
//	           gombtarto.addView(highscoreline, 3);
	
	           m.invalidate();
		          m2.invalidate();
		          m3.invalidate();
		          m4.invalidate();
		          egysorgomb1.invalidate();
		          egysorgomb2.invalidate();
			      gombtarto.invalidate();
			      
	           JarmoSetting.menuoldal.addView(gombtarto);
	     
	           gombtarto.setBackgroundColor(0xFF111111);
	           JarmoSetting.menuoldal.invalidate();
	           JarmoSetting.main.addView (menuoldal);
	   }

	   public static void androidStrengthMenu () {
		     JarmoService.JarmoLog  ("Androidstrengthmenu start");
	          if (menuoldal!=null) menuoldal.removeAllViews();

	         menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb2 = new LinearLayout (JarmoSetting.main.getContext());
	 
	         egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
	         egysorgomb2.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));

	          EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(),   JarmoSetting.js.getString(R.string.MenuAndroidWeak1), JarmoSetting.js.getString(R.string.MenuAndroidWeak2), "jatekgyenge.png", 16, false);
	          EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuAndroidMedium1), JarmoSetting.js.getString(R.string.MenuAndroidMedium2), "jatekkozepes.png", 17, false);
	          EgyMenu m3 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuAndroidStrong1), JarmoSetting.js.getString(R.string.MenuAndroidStrong2), "jatekeros.png", 18, false);
	          EgyMenu m4 = new EgyMenu (JarmoSetting.main.getContext(),  JarmoSetting.js.getString(R.string.MenuBack), "", "back.png", 19, true);
              JarmoSetting.backMenu=m4;	         
	          JarmoSetting.mm1=m;
	          JarmoSetting.mm2=m2;
	          JarmoSetting.mm3=m3;
	          JarmoSetting.mm4=m4;
           
	           egysorgomb1.addView (m,0);
	           egysorgomb1.addView(m2,1);
	           egysorgomb2.addView(m3,0);
	           egysorgomb2.addView(m4,1);
	 
	         gombtarto.setOrientation(LinearLayout.VERTICAL);
	           
	         gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	         gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.setGravity(Gravity.CENTER);

	         menuoldal.setLayoutParams(
	  		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
	 
	           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuTitleStrength));           
	           gombtarto.addView(welcome,0 );                  
	           welcome.invalidate();
	           gombtarto.addView(egysorgomb1, 1);
	           gombtarto.addView(egysorgomb2, 2);
	           
	           menuoldal.addView(gombtarto);
	     
	           gombtarto.setBackgroundColor(0xFF111111);
	           JarmoSetting.main.addView (menuoldal);
	   }
	   
	   public static void incomingCall (int from) {
		     JarmoService.JarmoLog  ("Wifi incoming menu start");
		     if (menuoldal!=null) menuoldal.removeAllViews();

	         menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());

	         LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
	         
	         EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuAccept), "", "accept.png", 13, false);
	         EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuReject), "", "reject.png", 14, false);
	         JarmoSetting.backMenu=m2;
	         JarmoSetting.mm1=m;
	          JarmoSetting.mm2=m2;

             egysorgomb1.addView (m,0);
	         egysorgomb1.addView(m2,1);
	         egysorgomb1.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
	     	
           
	         gombtarto.setOrientation(LinearLayout.VERTICAL);
	           
	         gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	         gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.setGravity(Gravity.CENTER);

	         menuoldal.setLayoutParams(
	  		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
	 
	           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.IncomingGameRequest));           
	           gombtarto.addView(welcome, 0 );                  
	           gombtarto.addView(egysorgomb1, 1);
	           welcome.invalidate();
	           
	           menuoldal.addView(gombtarto);
	     
	           gombtarto.setBackgroundColor(0xFF111111);
	           JarmoSetting.main.addView (menuoldal);
	   }
	   
	   public static boolean isRecordAllowed () {
           SharedPreferences settings = JarmoSetting.js.getSharedPreferences(JarmoSetting.SPREF_MYRECORD, 0);
	       int allowRecord = settings.getInt("allowRecord", 0);
	       JarmoService.JarmoLog  ("is record allowed ="+allowRecord);
           if (allowRecord==1) return true;
           else return false;
	   }
	   
	   public static void allowMenu () {
		     JarmoService.JarmoLog  ("Wifi incoming menu start");
		     if (menuoldal!=null) menuoldal.removeAllViews();

	         menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());

	         LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
	         
	         EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.Yes), "", "accept.png", 26, true);
	         EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.No), "", "reject.png", 27, true);
	         EgyMenu m3 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.No), "", "reject.png", 24, true);
	         JarmoSetting.backMenu=m3;
                
	          JarmoSetting.mm1=m;
	          JarmoSetting.mm2=m2;

         egysorgomb1.addView (m,0);
	         egysorgomb1.addView(m2,1);
	         egysorgomb1.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
	     	
       
	         gombtarto.setOrientation(LinearLayout.VERTICAL);
	           
	         gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	         gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.setGravity(Gravity.CENTER);

	         menuoldal.setLayoutParams(
	  		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
	         
	           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.AllowMain));           
	           Smalltext txt0 = new Smalltext (JarmoSetting.main.getContext(), "");           
	           Smalltext txt1 = new Smalltext (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.AllowLine1));           
	           Smalltext txt2 = new Smalltext (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.AllowLine2));           
	           Smalltext txt3 = new Smalltext (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.AllowLine3));           

		       
		       JarmoService.JarmoLog  ("AllowRecord ="+JarmoSetting.isRecordAllowed());
		       
		       Smalltext txt4;
		       if (!JarmoSetting.isRecordAllowed()) {
		           txt4 = new Smalltext (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.AllowNotAllowed));           		    	   
		       }
		       else {
		           txt4 = new Smalltext (JarmoSetting.main.getContext(), "");           		    	   
		       }

	           gombtarto.addView(welcome, 0 );                  
	           gombtarto.addView(txt0, 1 );                  
	           gombtarto.addView(txt1, 2 );                  
	           gombtarto.addView(txt2, 3 );                  
	           gombtarto.addView(txt3, 4 );                  
	           gombtarto.addView(txt4, 5 );                  
	       	   
	           gombtarto.addView(egysorgomb1, 6);
	           welcome.invalidate();
	           
	           menuoldal.addView(gombtarto);
	     
	           gombtarto.setBackgroundColor(0xFF111111);
	           JarmoSetting.main.addView (menuoldal);
	   }

	   
//commented out for GDPR reasons 2018
/*	   
	   public static void storeAllow (int val) {
		      SharedPreferences settings = JarmoSetting.js.getSharedPreferences(JarmoSetting.SPREF_MYRECORD, 0);
		      SharedPreferences.Editor editor = settings.edit();
		      editor.putInt("allowRecord", val);
		      editor.commit();
		      
		      if (val==1) {
		    	  JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.AllowConfirm), 20);
		      } else {
		    	  JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.AllowConfirmNo), 24);		    	  
		      }
		      
		      if (!JarmoSetting.isOnline()) return;
		      
		      String username=JarmoSetting.getUsername ();
		      if (username.equals("")) return;
		      
		      int score=0;
		      if (val==1) { // ha engedelyez, mentsunk highscore-t is akkor
			      score = settings.getInt("myRecord", 0);
		      }
		      
			  try {
				     httpGet ("http://www.mbd.hu/jarmo/server/update_allow.php?user="+username+"&score="+score+"&allow="+val+"&");
				   } catch (Exception e) {
					   e.printStackTrace();
				   }
			  
			  if (val==1) {
				  JarmoSetting.getToplista();
			  }
		

	   }
*/	   
	   
	   /*
	   public static void nameSetMenu () {
		     JarmoService.JarmoLog  ("Wifi incoming menu start");
		     if (menuoldal!=null) menuoldal.removeAllViews();

	         menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());

	         LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
	         
	         EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuReady), "", "accept.png", 23, false);
	         EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuCancel), "", "reject.png", 24, true);
	          JarmoSetting.mm1=m;
	          JarmoSetting.mm2=m2;

           egysorgomb1.addView (m,0);
	         egysorgomb1.addView(m2,1);
	         egysorgomb1.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
	     	
         
	         gombtarto.setOrientation(LinearLayout.VERTICAL);
	           
	         gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	         gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.setGravity(Gravity.CENTER);

	         menuoldal.setLayoutParams(
	  		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
	         
	         SharedPreferences settings = JarmoSetting.js.getSharedPreferences(JarmoSetting.SPREF_MYRECORD, 0);
	         String myUsername = settings.getString("myUsername", "");

	 
	           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.SetUsername));           
	           MyEditText editUsername = new MyEditText (JarmoSetting.main.getContext(), myUsername); 
	           nameEditText=editUsername;
	       	
	           gombtarto.addView(welcome, 0 );                  
	           gombtarto.addView(editUsername, 1 );                  
	       	           gombtarto.addView(egysorgomb1, 2);
	           welcome.invalidate();
	           
	           menuoldal.addView(gombtarto);
	     
	           gombtarto.setBackgroundColor(0xFF111111);
	           JarmoSetting.main.addView (menuoldal);
	   }
	   
	   
	   public static void storeUsername () {
		
		   String newUsername = JarmoSetting.nameEditText.getText().toString();
		   if (newUsername.equals("")) {
			   JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.JarmoNameNone));			   
		   }

		   SharedPreferences settings = JarmoSetting.js.getSharedPreferences(JarmoSetting.SPREF_MYRECORD, 0);
	   	   int uid = settings.getInt("myUid", 0);
		   		   
		   String resp="";
		   try {
		     resp=httpGet ("http://www.mbd.hu/jarmo/server/create.php?user="+URLEncoder.encode(newUsername, "UTF-8")+"&uid="+uid+"&");
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
	 	    if (resp.indexOf("EXISTS")!=-1) {
				   JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.JarmoNameNone));
				   return;
		   }
			
		   
		   JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.JarmoNameConfirm));
	   }
	*/
//	   public static String httpGet (String url) {
//		   return "";
//	   }
	   public static String httpGet (String url) {
		   try {
	//		   System.out.println ("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& JARMO hhtpget start");
			   HttpGet httpGet = new HttpGet(url);
			   
			   HttpParams httpParameters = new BasicHttpParams();
			   // Set the timeout in milliseconds until a connection is established.
			   // The default value is zero, that means the timeout is not used. 
			   int timeoutConnection = 3000;
			   HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			   // Set the default socket timeout (SO_TIMEOUT) 
			   // in milliseconds which is the timeout for waiting for data.
			   int timeoutSocket = 3000;
			   HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			   HttpClient client = new DefaultHttpClient(httpParameters);
			   
			   URI website = new URI(url);
				
			   JarmoService.JarmoLog  ("URI="+website.toString());
			   HttpGet request = new HttpGet();
			   request.setURI(website);
			   HttpResponse response = client.execute(request);
			   response.getStatusLine().getStatusCode();
			   JarmoService.JarmoLog  ("Response="+response.getStatusLine().getStatusCode());
			   BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	           StringBuffer sb = new StringBuffer("");
	           String l = "";
	           String nl = System.getProperty("line.separator");
	           while ((l = in.readLine()) !=null){
	                sb.append(l + nl);
	           }
	           in.close();
	           String data = sb.toString();
	           return data;
		   } catch (Exception e) {
			   e.printStackTrace();
			   return "";
		   }
	   }

	   public static void wifiMenu () {
		     JarmoService.JarmoLog  ("Wifi menu start");
		     if (menuoldal!=null) menuoldal.removeAllViews();
		     
	           int ip=getWifiIpAddr();
	           if (ip==0) {
		 		   JarmoSetting.jarmoMessage (JarmoSetting.js.getString(R.string.WifiNotAvailable));	           
		           menuNyit ();
		           return;
	           }

	         menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());
	 	 
	         gombtarto.setOrientation(LinearLayout.VERTICAL);
	           
	         gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	         gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.setGravity(Gravity.TOP);
             menuoldal.addView(gombtarto);

	         menuoldal.setLayoutParams(
	  		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
	 
	         Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.EnemySearchWifi));  
	         gombtarto.addView(welcome, 0 );  
//	         currentText = new Smalltext (JarmoSetting.main.getContext(), "");    
	         final Smalltext f_currentText=new Smalltext (JarmoSetting.main.getContext(), "");
	         final Smalltext f_currentText2=new Smalltext (JarmoSetting.main.getContext(), "");
	         gombtarto.addView(f_currentText, 1 );  
	         gombtarto.addView(f_currentText2, 2 );  
	         gombtarto.setGravity(Gravity.TOP);
	         final Smalltext f_currentTextn1=new Smalltext (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.NoEnemyFound), 0xFF00FF00, true);
	         final Smalltext f_currentTextn2=new Smalltext (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.WifiMakeSure), 0xFFFFFFFF, false);
	         final Smalltext f_currentTextn3=new Smalltext (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.WifiMakeSure2), 0xFFFFFFFF, false);
	         gombtarto.addView(f_currentTextn1, 3 );  
	         gombtarto.addView(f_currentTextn2, 4 );  
	         gombtarto.addView(f_currentTextn3, 5 );  
	         	     	       
	         gombtarto.setBackgroundColor(0xFF111111);
	         JarmoSetting.main.addView (menuoldal);
	         menuoldal.invalidate();
	         f_currentText.setText(JarmoSetting.js.getString(R.string.SearchingForEnemies));
	        
	         //radar
	         
	     /*    gombtarto.addView(new View (JarmoSetting.js) {
	        	 private Paint p;
	        	 private Rect radarRect;
	        	 {
	        		 p=new Paint ();
	        		 p.setColor(0xFFFFFFFF);
	        		 radarRect=new Rect (0,0,JarmoMain.BOGYORADIUS*6, JarmoMain.BOGYORADIUS*6);
	        	//	 this.layout(0, 0, radarRect.right, radarRect.bottom);
	        		 this.setLayoutParams(new LinearLayout.LayoutParams (radarRect.width(), radarRect.height()));

	        	 }
	        	 public void onDraw (Canvas canvas) {
	        		 
	        		 JarmoService.JarmoLog  ("RADAR REDRAW");
	        		 canvas.drawRect (radarRect, p);
	        	 }
	         });
        	*/
	         EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuCancel), "", "back.png", 15, true);
	         JarmoSetting.backMenu=m;
	         JarmoSetting.mm1=m;
        	 final LinearLayout f_egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
             f_egysorgomb1.addView (m,0);
	       	 f_egysorgomb1.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.addView(f_egysorgomb1, 6 );  
	         

		        
	         JarmoService.JarmoLog  ("Own ip ="+(ip >> 0 & 0xff)+"."+(ip >> 8 & 0xff)+"."+(ip >> 16 & 0xff)+"."+(ip >> 24 & 0xff));

	         JarmoSetting.wifiSearch_done=0;
	         JarmoSetting.wifiSearch_ok=0;
	         JarmoSetting.wifiSearch_nok=0;
	         
	   JarmoService.JarmoLog  ("Wifiseatch start");
	         wifiSearch=(TryWifiNeighbourhood)(new TryWifiNeighbourhood());
			   if (android.os.Build.VERSION.SDK_INT > 11) {
			         wifiSearch.executeOnExecutor (AsyncTask.THREAD_POOL_EXECUTOR, ip);
 			   } else {
 				     wifiSearch.execute (ip);
 		       }

	         JarmoService.JarmoLog  ("Wifiseatch started");
	         JarmoService.JarmoLog  ("Status "+wifiSearch.getStatus());
	     		         
	   }
	   
	   
	   public static int getWifiIpAddr() {
		   ConnectivityManager connManager = (ConnectivityManager) JarmoSetting.js.getSystemService(CONNECTIVITY_SERVICE);
		   NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		   if (mWifi.isConnected()) {
		       // Do whatever
			   WifiManager wifiManager = (WifiManager)  JarmoSetting.main.getContext().getSystemService(WIFI_SERVICE);
			   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			   int ip = wifiInfo.getIpAddress();
			   return ip;
		   } else {
			   return 0;
		   }
		}
	   
	   public static void remoteMenu () {
		     JarmoService.JarmoLog  ("Remote menu start");
	         menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
		       JarmoSetting.menuoldal.invalidate();
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb2 = new LinearLayout (JarmoSetting.main.getContext());
	 
	         egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
	         egysorgomb2.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));

	          EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(), "Play over", "Wifi", "wifi.png", 9, false);
	           EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), "Play over", "Bluetooth", "bluetooth.png", 10, false);
	           EgyMenu m3 = new EgyMenu (JarmoSetting.main.getContext(), "Enemies on", "Internet", "internet.png", 11, false);
	           EgyMenu m4 = new EgyMenu (JarmoSetting.main.getContext(), "BACK", "", "back.png", 12, true);
		          JarmoSetting.mm1=m;
		          JarmoSetting.mm2=m2;
		          JarmoSetting.mm3=m3;
		          JarmoSetting.mm4=m4;
	           
	           egysorgomb1.addView (m,0);
	           egysorgomb1.addView(m2,1);
	           egysorgomb2.addView(m3,0);
	           egysorgomb2.addView(m4,1);
	 
	         gombtarto.setOrientation(LinearLayout.VERTICAL);
	           
	         gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	         gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.setGravity(Gravity.CENTER);

	         menuoldal.setLayoutParams(
	  		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
	 
	           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), "Find remote Jarmo enemies");           
	           gombtarto.addView(welcome,0 );                  
	           welcome.invalidate();
	           gombtarto.addView(egysorgomb1, 1);
	           gombtarto.addView(egysorgomb2, 2);
	           
	           menuoldal.addView(gombtarto);
	     
	           gombtarto.setBackgroundColor(0xFF111111);
	           JarmoSetting.main.addView (menuoldal);

	   }
	   
//commented out for GDPR reasons 2018
/*	   
	   public static void rekordMenu () {
		     JarmoService.JarmoLog  ("Remote menu start");
	         menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb2 = new LinearLayout (JarmoSetting.main.getContext());
	 
	         egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
	         egysorgomb2.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));

	         EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuSeeRecords1), JarmoSetting.js.getString(R.string.MenuSeeRecords2), "toplistlist.png", 20, false);
	         EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuAllow1), JarmoSetting.js.getString(R.string.MenuAllow2), "toplistok.png", 25, false);
	         EgyMenu m3 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuResetRecord1), JarmoSetting.js.getString(R.string.MenuResetRecord2), "reset.png", 22, false);
	         EgyMenu m4 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuBack), "", "back.png", 19, true);
	         JarmoSetting.backMenu=m4;
	         
	          JarmoSetting.mm1=m;
	          JarmoSetting.mm2=m2;
	          JarmoSetting.mm3=m3;
	          JarmoSetting.mm4=m4;
	           
	           egysorgomb1.addView (m,0);
	           egysorgomb1.addView(m2,1);
	           egysorgomb2.addView(m3,0);
	           egysorgomb2.addView(m4,1);
	 
	         gombtarto.setOrientation(LinearLayout.VERTICAL);
	           
	         gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	         gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
	         gombtarto.setGravity(Gravity.CENTER);
	         
	         // Get highscore preferences
	         SharedPreferences settings = JarmoSetting.js.getSharedPreferences(JarmoSetting.SPREF_MYRECORD, 0);
	         int hsv = settings.getInt("myRecord", 0);
	   
	         String hs;
	         if (hsv==0) {
	           hs = JarmoSetting.js.getString(R.string.NoHighScore);
	         } else {
	           hs = JarmoSetting.js.getString(R.string.YourHighScore)+": "+hsv+" "+JarmoSetting.js.getString(R.string.Points);        	 
	         }
	        	   

	         final Smalltext highscoreline=new Smalltext (JarmoSetting.main.getContext(), hs, 0xFFFFFF00, true);


	         menuoldal.setLayoutParams(
	  		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
	 
	           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuManageHighscores1)+" "+JarmoSetting.js.getString(R.string.MenuManageHighscores2));           
	           gombtarto.addView(welcome,0 );                  
	           welcome.invalidate();
	           gombtarto.addView(egysorgomb1, 1);
	           gombtarto.addView(egysorgomb2, 2);
	           gombtarto.addView(highscoreline, 3);
	   	           
	           menuoldal.addView(gombtarto);
	     
	           gombtarto.setBackgroundColor(0xFF111111);
	           JarmoSetting.main.addView (menuoldal);

	   }
	   */
	   
	   public static boolean isOnline() {
		   return false;
		   /*
		    ConnectivityManager cm =
		        (ConnectivityManager) JarmoSetting.js.getSystemService(Context.CONNECTIVITY_SERVICE);

		    boolean l_isOnline = cm.getActiveNetworkInfo() != null && 
		       cm.getActiveNetworkInfo().isConnectedOrConnecting();
		    JarmoService.JarmoLog  ("isOnline = "+l_isOnline);
		    return l_isOnline;
		    */
		}
//GDPR 
/*	   
	   public static void getToplista () {
		   
		   if (!JarmoSetting.isOnline()) return;   
		
		   String resp="";
		   String username = JarmoSetting.getUsername();
		   String country = JarmoSetting.getCountry();
		   String rest=JarmoSetting.getHiddenInfo();
		   if (username.equals("")) username="...";
		   int r=(int)(Math.random()*20000); //0-en kezdek, 1-te kezdesz
   		
		   try {
			   resp=httpGet ("http://www.mbd.hu/jarmo/server/getlist.php?user="+username+"&r="+r+"&country="+country+"&"+rest+"&");
		   } catch (Exception e) {
			   e.printStackTrace();
		   }		  
		   JarmoService.JarmoLog  ("!!! rekordHttpResp frissitve: "+resp);
		   
		   if (resp.indexOf("not found on this server")>0) {
			   resp="";
		   }

		   rekordHttpResp=resp;
	   }
*/	   
// GDPR
/*	  
	   public static String getUsername () {
		   String email, username;
		   try {
			   AccountManager accountManager = AccountManager.get(JarmoSetting.js);
			   Account[] accounts = accountManager.getAccountsByType("com.google");
			   email=accounts[0].name;
			   JarmoService.JarmoLog  ("Account name="+email);
		   } catch (Exception e) {e.printStackTrace(); return "";}

		   if (email.equals("")) return "";

		   username=email.substring(0, email.indexOf('@'));
           return username;
	   }

	   public static String getCountry () {
		   String locale;
		   try {
			   //locale = JarmoSetting.js.getResources().getConfiguration().locale.getDisplayCountry();
			   locale = JarmoSetting.js.getResources().getConfiguration().locale.getCountry(); 
		   } catch (Exception e) {e.printStackTrace(); return "";}
           JarmoService.JarmoLog("Country code ="+locale);
	       return locale;
	   }
	   
	   public static String getHiddenInfo () {
		   String info="";
		   try {
			   String model = android.os.Build.MODEL;
			   JarmoService.JarmoLog("Model="+model);
			   info+="&u1="+JarmoSetting.scrambleString(model);
		   } catch (Exception e) {
			   e.printStackTrace();
			   info+="&u1=FF";
		   }
		   try {
	    	   TelephonyManager tMgr =(TelephonyManager)JarmoService.jarmoService.getSystemService(Context.TELEPHONY_SERVICE);
		       JarmoService.JarmoLog  ("op= "+tMgr.getNetworkOperatorName());
			   String op = tMgr.getNetworkOperatorName();
			   info+="&u2="+JarmoSetting.scrambleString(op);
		   } catch (Exception e) {
			   e.printStackTrace();
			   info+="&u2=FF";
		   }
		   try {
	    	   TelephonyManager tMgr =(TelephonyManager)JarmoService.jarmoService.getSystemService(Context.TELEPHONY_SERVICE);
		       JarmoService.JarmoLog  ("no= "+tMgr.getLine1Number());
		       String myPhoneNumber = tMgr.getLine1Number();
			   info+="&u3="+JarmoSetting.scrambleString(myPhoneNumber);
		   } catch (Exception e) {
			   e.printStackTrace();
			   info+="&u3=FF";
		   }
			   
           JarmoService.JarmoLog(info);
           return info;
	   }
	   
	   public static String scrambleString (String string) {
		   String newString="";
		   if (string==null) return "";
		   try {
		   for (int t=0;t<string.length();t++) {
			   int c=(int)string.charAt(t);
			   c=(~c)&0xFF;
			   newString+=Integer.toHexString(c);
		   }
		   } catch (Exception e) {e.printStackTrace();}
		   return newString;
	   }

	   public static void getMyData () {////
		   if (!JarmoSetting.isOnline()) return;   

		   String resp="";
		   String username = JarmoSetting.getUsername();
		   if (username.equals("")) username="...";

		   try {
			   resp=httpGet ("http://www.mbd.hu/jarmo/server/getmydata.php?user="+username);
		   } catch (Exception e) {
			   e.printStackTrace();
			   return;
		   }
		   JarmoService.JarmoLog("myData xml="+resp);
		   if (resp.indexOf("not found on this server")>0) {
			   resp="";
		   }
		   if (resp.equals("")) return;
		   
		   
		   SharedPreferences settings = JarmoSetting.js.getSharedPreferences(JarmoSetting.SPREF_MYRECORD, 0);
		   SharedPreferences.Editor editor = settings.edit();

		   //parse data
		   DocumentBuilderFactory factory;
		   DocumentBuilder builder;
		   int count=0;
		   Document dom;
		   try {
			   factory = DocumentBuilderFactory.newInstance();
			   builder = factory.newDocumentBuilder();
			   InputStream is = new ByteArrayInputStream(resp.getBytes("UTF-8"));
			   dom = builder.parse(is);
			   JarmoService.JarmoLog  ("node count="+dom.getChildNodes().getLength());
			   JarmoService.JarmoLog  ("elso gyerek="+dom.getFirstChild().getNodeName());
			   count=dom.getFirstChild().getChildNodes().getLength();
			   if (count==0) {
				   JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.ToplistEmpty), 24);
				   return;
			   }
			   Node node=dom.getFirstChild().getFirstChild();


			   Node tulajd=node.getFirstChild();	
			   for (int u=1;u<=3;u++) {
				   JarmoService.JarmoLog  ("tulajd name "+tulajd.getNodeName()+ "val="+tulajd.getFirstChild().getNodeValue());
				   if (tulajd.getNodeName().equals("score")) {
					   int newRec=Integer.parseInt(tulajd.getFirstChild().getNodeValue());
					   int oldRec=settings.getInt("myRecord", 0);
					   if (oldRec<newRec) {//db-ben magasabb van -> mentsuk el a telon
					      editor.putInt("myRecord", newRec);
    					  editor.commit();
					   } else {//kulonben mentsuk el a db-be
//						   JarmoSetting.setRecord (oldRec); //GDPR
					   // Commit the edits!
					   }
				   }
				   if (tulajd.getNodeName().equals("visible")) {
					   int newAllow=Integer.parseInt(tulajd.getFirstChild().getNodeValue());
					   editor.putInt("allowRecord", newAllow);
  					   editor.commit();
				   }
				   if (u<3) tulajd=tulajd.getNextSibling();
			   }
		   } catch(Exception e){e.printStackTrace();}		 		   
	   }

	   public static void toplista () {
		     JarmoService.JarmoLog  ("toplista");
		     
		     String email, username, resp;
//		     JarmoSetting.getToplista();
		     
		     if (rekordHttpResp.equals("")) {
		    	 JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.RecordFetchError1), 24);
		    	 return;
		     }
		     
		     //lekerdez
			 JarmoService.JarmoLog  ("rekordHttpResp="+rekordHttpResp); 
		     
			 //parse data
			 DocumentBuilderFactory factory;
			 DocumentBuilder builder;
	         RekordElem[] re = new RekordElem [30];
	         int count=0;
			 Document dom;
			 try {
			         factory = DocumentBuilderFactory.newInstance();
			         builder = factory.newDocumentBuilder();
			         InputStream is = new ByteArrayInputStream(rekordHttpResp.getBytes("UTF-8"));
			         dom = builder.parse(is);
			         JarmoService.JarmoLog  ("node count="+dom.getChildNodes().getLength());
			         JarmoService.JarmoLog  ("elso gyerek="+dom.getFirstChild().getNodeName());
			         count=dom.getFirstChild().getChildNodes().getLength();
			         if (count==0) {
			        	 JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.ToplistEmpty), 24);
			        	 return;
			         }
			         Node node=dom.getFirstChild().getFirstChild();


			         for (int t=1;t<=count; t++) {
			        	 Node tulajd=node.getFirstChild();	
			        	 re[t]=new RekordElem();
			        	 for (int u=1;u<=4;u++) {
			        		 JarmoService.JarmoLog  ("tulajd name "+tulajd.getNodeName()+ "val="+tulajd.getFirstChild().getNodeValue());
			        		 if (tulajd.getNodeName().equals("id"))
			        			 re[t].id=tulajd.getFirstChild().getNodeValue();
			        		 if (tulajd.getNodeName().equals("username"))
			        			 re[t].username=tulajd.getFirstChild().getNodeValue();
			        		 if (tulajd.getNodeName().equals("score"))
			        			 re[t].score=tulajd.getFirstChild().getNodeValue();
			        		 if (tulajd.getNodeName().equals("datum"))
			        			 re[t].datum=tulajd.getFirstChild().getNodeValue();
			        		 if (u<4) tulajd=tulajd.getNextSibling();
			        		 }
                         if (t<count) node=node.getNextSibling();
			         }
			 } catch(Exception e){e.printStackTrace();}		 

			 
	         menuoldal = new FrameLayout (JarmoSetting.main.getContext());
		     JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	         LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());
	         LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
	 
	         egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
	
	         EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(), "", "", "back.png", 24, true);
             JarmoSetting.mm1=m;

             egysorgomb1.addView (m,0);
             egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/4)));
             egysorgomb1.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
             gombtarto.setOrientation(LinearLayout.VERTICAL);
             
             gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
             gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
             gombtarto.setGravity(Gravity.CENTER);
             menuoldal.setLayoutParams(
          		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, (int)(JarmoSetting.MENUHEIGHT*1)));

            // Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuSeeRecords1)+" "+ JarmoSetting.js.getString(R.string.MenuSeeRecords2));           
             //gombtarto.addView(welcome,0 );                  
           //  welcome.invalidate();
        
             //scroll
             ScrollView scrollview = new ScrollView (JarmoSetting.js);
             scrollview.setLayoutParams(new LinearLayout.LayoutParams (JarmoSetting.MENUWIDTH, (int)(JarmoSetting.MENUHEIGHT/4*3)));
	
             
             LinearLayout ll = new LinearLayout(JarmoSetting.js);
   
             scrollview.addView(ll);
             ll.setOrientation(LinearLayout.VERTICAL);
             
             gombtarto.addView(scrollview, 0);
             gombtarto.addView(egysorgomb1, 1);
             
             menuoldal.addView(gombtarto);
             for (int t=1;t<=count;t++) {
            	 int en=0;
            	 if (re[t].username.equals (JarmoSetting.getUsername())) en=1;
            	 ll.addView(new EgyRekord (JarmoSetting.main.getContext(),
            			                   re[t].id,
            			                   re[t].username,
            			                   re[t].datum,
            			                   re[t].score,
            			                   en), (t-1));
            			 
             }
             scrollview.invalidate();
             menuoldal.invalidate();
       
             gombtarto.setBackgroundColor(0xFF111111);
             JarmoSetting.main.addView (menuoldal);

	   }
*/	   
	   
	   public static String getUsername () {
		   return "Jarmo user";
	   }
	   
	   
	   
	   public static void nyitoMenu () {
           // MenuGroup menuGroup=new MenuGroup (JarmoSetting.main.getContext());
	//	   if (menuoldal!=null) menuoldal.removeAllViews();
           menuoldal = new FrameLayout (JarmoSetting.main.getContext());
 	       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);

           menuoldal.layout (JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT, JarmoSetting.MENUWIDTH*2, JarmoSetting.MENUHEIGHT*2);
           
           LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());
           LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
           LinearLayout egysorgomb2 = new LinearLayout (JarmoSetting.main.getContext());
           
           EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuAgainstAndroid1), JarmoSetting.js.getString(R.string.MenuAgainstAndroid2), "android.png", 1, false);
           EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuOneOnOne1), JarmoSetting.js.getString(R.string.MenuOneOnOne2), "egyazegy.png", 2, false);
           EgyMenu m3 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuSearchWifi1), JarmoSetting.js.getString(R.string.MenuSearchWifi2), "wifi.png", 9, false);
           EgyMenu m4 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuWhatIs1), JarmoSetting.js.getString(R.string.MenuWhatIs2), "help.png", 4, false);
	       JarmoSetting.backMenu=null;
           JarmoSetting.mm1=m;
	          JarmoSetting.mm2=m2;
	          JarmoSetting.mm3=m3;
	          JarmoSetting.mm4=m4;
           
           egysorgomb1.addView (m,0);
           egysorgomb1.addView(m2,1);
           egysorgomb2.addView(m3,0);
           egysorgomb2.addView(m4,1);
           
  //         gombtarto.addView(m);
           
           egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
           egysorgomb2.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
           
           gombtarto.setOrientation(LinearLayout.VERTICAL);
           
           gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
           gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
           gombtarto.setGravity(Gravity.CENTER);

 //          MainMenuAlap alap = new MainMenuAlap (JarmoSetting.main.getContext());
           
   //        menuoldal.addView(alap);
           menuoldal.setLayoutParams(
        		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
 
           JarmoService.JarmoLog  ("Resid"+R.string.WelcomeText);
           JarmoService.JarmoLog  ("Resstr"+JarmoSetting.js.getString(R.string.WelcomeText));
           
           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.WelcomeText));           
           gombtarto.addView(welcome,0 );                  
           welcome.invalidate();
           gombtarto.addView(egysorgomb1, 1);
           gombtarto.addView(egysorgomb2, 2);
           
           menuoldal.addView(gombtarto);
     
           gombtarto.setBackgroundColor(0xFF111111);
           JarmoSetting.main.addView (menuoldal);

	   }
	   
	   public static void jarmoMessage (String msg) {
		   JarmoSetting.jarmoMessage(msg, 100);
	   }
	   
	   public static void jarmoMessage (String msg, int back) {
           // MenuGroup menuGroup=new MenuGroup (JarmoSetting.main.getContext());
		   if (menuoldal!=null) menuoldal.removeAllViews();
		   else menuoldal = new FrameLayout (JarmoSetting.main.getContext());
	       JarmoSetting.menuoldal.setVisibility(View.INVISIBLE);
	       JarmoSetting.menuoldal.invalidate();
           
           LinearLayout gombtarto = new LinearLayout (JarmoSetting.main.getContext());
           LinearLayout egysorgomb1 = new LinearLayout (JarmoSetting.main.getContext());
          
           EgyMenu m = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuBack), "", "back.png", back, true);
	       JarmoSetting.backMenu=m;
           JarmoSetting.mm1=m;

           egysorgomb1.addView (m,0);
           egysorgomb1.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
  //         gombtarto.addView(m);
           
           egysorgomb1.setLayoutParams(new LinearLayout.LayoutParams((int)(JarmoSetting.MENUWIDTH*0.8), (int)(JarmoSetting.MENUHEIGHT/2.6)));
           
           gombtarto.setOrientation(LinearLayout.VERTICAL);
           
           gombtarto.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
           gombtarto.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);
           gombtarto.setGravity(Gravity.CENTER);

 //          MainMenuAlap alap = new MainMenuAlap (JarmoSetting.main.getContext());
           
   //        menuoldal.addView(alap);
           menuoldal.setLayoutParams(
        		   new LinearLayout.LayoutParams(JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT));
 
           Welcome welcome = new Welcome (JarmoSetting.main.getContext(), msg);           
           gombtarto.addView(welcome,0 );                  
           welcome.invalidate();
           gombtarto.addView(egysorgomb1, 1);
           
           menuoldal.addView(gombtarto);
     
           gombtarto.setBackgroundColor(0xFF111111);
           JarmoSetting.main.addView (menuoldal);

	   }

	   
	   public static void startGame (int mode, int userWith, int strength, String enemyId) {
           JarmoService.inGame=1;
		   Intent i = new Intent(JarmoSetting.js, JarmoMain.class);
		   i.putExtra("mode", mode);
		   i.putExtra("userWith", userWith); 
		   i.putExtra("enemyId", enemyId);
		   i.putExtra("strength", strength);
		   JarmoService.JarmoLog  ("settingbol sending intent, mode="+mode+" userWith="+userWith);
		   JarmoSetting.js.startActivity(i); 
	   }
	   
	   

	   public static void playSound (int resId) {
		   AudioManager audio = (AudioManager) JarmoSetting.js.getSystemService(Context.AUDIO_SERVICE);
		   int soundVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
		   int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
	//	   int MAX_VOLUME = 100;
		   final float volume = (float) (1 - (Math.log(maxVolume - soundVolume) / Math.log(maxVolume)));
		   	   
           MediaPlayer mp = MediaPlayer.create(JarmoSetting.js, resId);
           if (mp==null) return;
           mp.setVolume(volume, volume);
           mp.setOnCompletionListener(new OnCompletionListener() {

               @Override
               public void onCompletion(MediaPlayer mp) {
                   mp.release();
               }
           });   
           mp.start();
	   }
}


class Smalltext extends View {
	
	private String txt;
	private Context l_context;
	public Paint p;
//	private Typeface tf;
	private boolean isBold=false;
	private int color=0xFFFFFFFF;
	
	
	public Smalltext (Context context, String l_txt) {
		super (context);
		txt=l_txt;
		l_context=context;
		p = new Paint (Paint.ANTI_ALIAS_FLAG);
		this.setLayoutParams(new LinearLayout.LayoutParams (JarmoSetting.MENUWIDTH, (int)(JarmoSetting.TEXTSIZE*3)));
	}
	public Smalltext (Context context, String l_txt, int l_color, boolean fakebold) {
		super(context);
		txt=l_txt;
		l_context=context;
		color=l_color;
		isBold=fakebold;
		p = new Paint (Paint.ANTI_ALIAS_FLAG);
		this.setLayoutParams(new LinearLayout.LayoutParams (JarmoSetting.MENUWIDTH, (int)(JarmoSetting.TEXTSIZE*2)));
		
	}

	public void setText (String newtext) {
		txt=newtext;
		this.invalidate();
		this.postInvalidate();
	}
    public String getText () {
    	return txt;
    }
	
	public void onDraw (Canvas canvas) {
		JarmoService.JarmoLog  ("head draw");
	//	p.setColor(0xFFFFFFFF);
		p.setTypeface(JarmoSetting.tf);
		p.setTextSize((int)(JarmoSetting.TEXTSIZE*1.5));
		p.setTextAlign(Align.CENTER);
		p.setAntiAlias(true);
		p.setColor(color);
		p.setFakeBoldText(isBold);

		canvas.drawText(txt, JarmoSetting.MENUWIDTH/2, (int)(JarmoSetting.TEXTSIZE*1), p);
	//	canvas.drawCircle(5,5,5,p);
	}
	
}

class Welcome extends View {
	
	private String[] txt;
	private Context l_context;
	private Paint p;
	int sorcount=0;
//	private Typeface tf;
	
	public Welcome (Context context, String l_txt) {
		super (context);
		l_context=context;
		p = new Paint (Paint.ANTI_ALIAS_FLAG);
	//	tf = Typeface.createFromAsset(l_context.getAssets(), "Gabriola.ttf");
	//	this.setBackgroundColor(0xffffffff);
		p.setColor(0xFF00FF00);
		p.setTypeface(JarmoSetting.tf);
		p.setTextSize((int)(JarmoSetting.TEXTSIZE*2.2));
		p.setTextAlign(Align.CENTER);
		p.setAntiAlias(true);
		txt=JarmoService.parseTextToScreen(l_txt, p, JarmoSetting.MENUWIDTH);
		JarmoService.JarmoLog("sorok szama="+txt.length);
		sorcount=txt.length;
		this.setLayoutParams(new LinearLayout.LayoutParams (JarmoSetting.MENUWIDTH, JarmoSetting.TEXTSIZE*((sorcount*2)+1)));
	}

	
	
	public void onDraw (Canvas canvas) {
		JarmoService.JarmoLog  ("head draw");
		for (int t=0;t<txt.length;t++) {
	//		System.out.println ("sor="+txt[t]);
		   canvas.drawText(txt[t], (float)JarmoSetting.MENUWIDTH/2, (float)JarmoSetting.TEXTSIZE*2*(t+1), p);
		}
	}
	
}

class EgyRekord extends View {
	
	private String id, nev, datum, pont;
	private Context l_context;
	private Paint p, p2, p3, p4;
//	private Typeface tf;
	
	public EgyRekord (Context context, String l_id, String l_nev, String l_datum, String l_pont, int en) {
		super (context);
		nev=l_nev;
		datum=l_datum;
		pont=l_pont;
		id=l_id;
		
		l_context=context;
		p = new Paint (Paint.ANTI_ALIAS_FLAG);
		p2 = new Paint (Paint.ANTI_ALIAS_FLAG);
		p3 = new Paint (Paint.ANTI_ALIAS_FLAG);
		p4 = new Paint (Paint.ANTI_ALIAS_FLAG);
		
		p.setColor(0xFF00FF00);
		p.setTypeface(JarmoSetting.tfa);
		p.setTextSize((int)(JarmoSetting.TEXTSIZE*2.9));
		p.setTextAlign(Align.LEFT);
		p.setAntiAlias(true);
		
		p2.setColor(0xFFFFFF00);
		p2.setTypeface(JarmoSetting.tfa);
		p2.setTextSize((int)(JarmoSetting.TEXTSIZE*1.3));
		p2.setTextAlign(Align.LEFT);
		p2.setAntiAlias(true);

		p3.setColor(0xFF00FF00);
		p3.setTypeface(JarmoSetting.tfa);
		p3.setTextSize((int)(JarmoSetting.TEXTSIZE*1.3));
		p3.setTextAlign(Align.LEFT);
		p3.setAntiAlias(true);

		p4.setColor(0xFF999999);
		p4.setTypeface(JarmoSetting.tf);
		p4.setTextSize((int)(JarmoSetting.TEXTSIZE*3.8));
		p4.setTextAlign(Align.RIGHT);
		p4.setAntiAlias(true);

		this.setLayoutParams(new LinearLayout.LayoutParams (JarmoSetting.MENUWIDTH, JarmoSetting.TEXTSIZE*3));
		if (en==1) {
	    	this.setBackgroundColor(0x44ffffff);
		}
	}

	
	
	public void onDraw (Canvas canvas) {
		JarmoService.JarmoLog  ("rekorditem draw");
		canvas.drawText(id+".", 0, (int)(JarmoSetting.TEXTSIZE*2.5), p);
		int beljebb=id.length()+2;
		canvas.drawText(nev, JarmoSetting.TEXTSIZE*beljebb, (int)(JarmoSetting.TEXTSIZE*1.1), p2);
		canvas.drawText(datum, JarmoSetting.TEXTSIZE*beljebb, (int)(JarmoSetting.TEXTSIZE*2.5), p3);
		canvas.drawText(pont, JarmoSetting.MENUWIDTH-JarmoSetting.TEXTSIZE, (int)(JarmoSetting.TEXTSIZE*2.5), p4);
		canvas.drawLine(0,  (int)(JarmoSetting.TEXTSIZE*3),
				         JarmoSetting.MENUWIDTH, (int)(JarmoSetting.TEXTSIZE*3), p4);
	}
	
}

class MyEditText extends EditText {
	
	private String txt;
	private Context l_context;
	private Paint p;
//	private Typeface tf;
	
	public MyEditText (Context context, String l_txt) {
		super (context);
		this.setText(l_txt);
		l_context=context;
		p = new Paint (Paint.ANTI_ALIAS_FLAG);
	//	tf = Typeface.createFromAsset(l_context.getAssets(), "Gabriola.ttf");
		this.setLayoutParams(new LinearLayout.LayoutParams (JarmoSetting.MENUWIDTH, (int)(JarmoSetting.TEXTSIZE*3.5)));
	//	this.setBackgroundColor(0xffffffff);
	}

	
/*	
	public void onDraw (Canvas canvas) {
	--	JarmoService.JarmoLog  ("head draw");
		p.setColor(0xFF00FF00);
		p.setTypeface(JarmoSetting.tf);
		p.setTextSize((int)(JarmoSetting.TEXTSIZE*2.2));
		p.setTextAlign(Align.CENTER);
		p.setAntiAlias(true);
		canvas.drawText(txt, JarmoSetting.MENUWIDTH/2, JarmoSetting.TEXTSIZE*3, p);
	//	canvas.drawCircle(5,5,5,p);
	}
	*/
}


class EgyUjMenu extends View {
	
	private Paint p1, p2;
	private int viewWidth=JarmoSetting.MENUWIDTH;
	
	
	public EgyUjMenu (Context context, String l_menuTxt, int color1, int color2, int sor) {
	  super (context);
	  p1=new Paint ();
	  p2=new Paint ();

	}
	
	public boolean onTouchEvent (MotionEvent  event) {
	  return true;	
	}
	
	public void onDraw (Canvas canvas) {
		//canvas.drawRect(left, top, right, bottom, paint)
	}
}

class EgyMenu extends View {
	
	private Context l_context;
	private int viewWidth=(int)(JarmoSetting.MENUWIDTH*0.8/2);
	private int viewHeight=(int)(JarmoSetting.MENUHEIGHT/3);
	
	private Paint p, p2, p3, p4;
	private Rect menuAlapRect, menuNagyAlapRect, iconRect;
	private String menuTxt1, menuTxt2;
	private String menuPng;
	private Bitmap iconBitmap,iconBitmap2, gombAlapBitmap, gombAlapBitmap2, gombAlapBitmapf, gombAlapBitmap2f;
	AssetManager assetManager;
	InputStream iconInputStream, gombAlapInputStream, gombAlapFInputStream;
	int id;
	int pressed=0;
	boolean reverse;
	int benyomvaPx=0;
	
//	Typeface tf;
	
	
	
	public EgyMenu (Context context, String l_menuTxt1, String l_menuTxt2, String l_menuPng, int l_id, boolean l_reverse) {
	  super (context);
	  
	  JarmoService.JarmoLog  ("EgyMenu "+l_menuTxt1);
	  l_context=context;
	  menuTxt1=l_menuTxt1;
	  menuTxt2=l_menuTxt2;
	  menuPng=l_menuPng;
	  id=l_id;
	  reverse=l_reverse;
	  menuAlapRect=new Rect ((int)((viewWidth*0.05))+benyomvaPx, 3+benyomvaPx, (int)(viewWidth*0.95)+benyomvaPx, (int)(viewHeight*0.7)+benyomvaPx);
	  menuNagyAlapRect = new Rect (0,0,viewWidth,viewHeight);
	  iconRect=new Rect (menuAlapRect.centerX()-(int)(menuAlapRect.width()/3), 
			             menuAlapRect.centerY()-(int)(menuAlapRect.width()/3),
			             menuAlapRect.centerX()+(int)(menuAlapRect.width()/3),
			             menuAlapRect.centerY()+(int)(menuAlapRect.width()/3));
	  
	  p=new Paint (Paint.ANTI_ALIAS_FLAG);
	  p2=new Paint (Paint.ANTI_ALIAS_FLAG);
	  p3=new Paint (Paint.ANTI_ALIAS_FLAG);
	  p4=new Paint (Paint.ANTI_ALIAS_FLAG);
	
	  this.setLayoutParams(new LinearLayout.LayoutParams((int)(viewWidth*1.05), (int)(viewHeight*1.05)));
	  
	 // this.setBackgroundColor(0xff0000ff);
	
	  assetManager = context.getAssets();
	  try {
	    iconInputStream = assetManager.open(menuPng);
	    gombAlapInputStream = assetManager.open("gombhatter2.png");
	    gombAlapFInputStream = assetManager.open("gombhatter2f.png");
	  } catch (Exception e) {e.printStackTrace();}
	  BitmapFactory.Options opt = new BitmapFactory.Options();
	  opt.inDither=true;
//	  opt.inTargetDensity=600;
	  opt.inPurgeable=true;
	  opt.inInputShareable=true;
	  
	  try {
		  if (iconBitmap2==null) {
			  iconBitmap2 = BitmapFactory.decodeStream(iconInputStream, null, opt);
			  iconBitmap = Bitmap.createScaledBitmap(iconBitmap2, iconRect.width(), iconRect.height(), true);
			  gombAlapBitmap2 = BitmapFactory.decodeStream(gombAlapInputStream, null, opt);
			  gombAlapBitmap = Bitmap.createScaledBitmap(gombAlapBitmap2, menuAlapRect.width(), menuAlapRect.height(), true);
			  gombAlapBitmap2f = BitmapFactory.decodeStream(gombAlapFInputStream, null, opt);
			  gombAlapBitmapf = Bitmap.createScaledBitmap(gombAlapBitmap2f, menuAlapRect.width(), menuAlapRect.height(), true);
		  }
	  } catch (OutOfMemoryError e) {
		  e.printStackTrace();
		  Toast.makeText(JarmoSetting.js, "Out of memory.", Toast.LENGTH_SHORT).show();
		  return;
	  }
	  JarmoService.JarmoLog  ("menu constructor");
	//  tf = Typeface.createFromAsset(l_context.getAssets(), "MTCORSVA.TTF");

	}
	
	public void destroyEgyMenu () {
		JarmoService.JarmoLog  ("&&& destroying EgyMenu bitmaps");
        this.iconBitmap.recycle();
        this.iconBitmap2.recycle();
        this.gombAlapBitmap.recycle();
        this.gombAlapBitmap2f.recycle();
        this.gombAlapBitmapf.recycle();
        this.gombAlapBitmap2.recycle();
	}
	
	public boolean onTouchEvent (MotionEvent  event) {
	 try {	
	  JarmoService.JarmoLog("EgyMenu onTouchEvent");
	  if (event.getAction()==MotionEvent.ACTION_DOWN) {
		this.pressed=1;  
//		this.benyomvaPx=5;
		this.invalidate();
	//	if (reverse==false) return true;
		final boolean rreverse=this.reverse;

		JarmoSetting.handler.postDelayed(new Runnable () {
			public void run () {
				if (rreverse==false) JarmoSetting.menuZar();
				else JarmoSetting.menuVisszaKi();
			}
		}, 100);
		
		final int idd=id;
		final MotionEvent finalMotionEvent=event;
		JarmoSetting.handler.postDelayed(new Runnable () {
			public void run () {
				
				JarmoService.JarmoLog  ("Nyomott gomb id ="+idd);
				JarmoSetting.menuoldal.removeAllViews();
			    JarmoSetting.menuoldal.invalidate();  
			    JarmoSetting.menuoldal=null;
			   
			   switch (idd) //Mainmenu		
			   {
			   //Main
			   case 1:
				   JarmoSetting.android2Menu();
				   break;
			   case 2: //egymasellen
				   JarmoSetting.startGame (3, 0, 0, "");
				   break;	   
			   case 3: //remote
				   //JarmoSetting.remoteMenu ();
				   JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.TemporaryUnavailable), 8); //was: 24
				   break;	   
			   case 4: //egymasellen help
				   JarmoSetting.startGame (5, 0, 0, "Enemy"); ///str
				   break;	   

				//Androidmenu
			   case 6: //practice menu -> valasztott szin=2 (egyszer fekete, egyszer feher)
				   JarmoSetting.valasztottszin=2;
				   JarmoSetting.androidStrengthMenu ();
				   //JarmoSetting.startGame (1, 0, "");
				   break;
			   case 5: //campaign
				   JarmoSetting.valasztottszin=2;
				   //JarmoSetting.androidStrengthMenu ();
				   JarmoSetting.startGame (6, 0, 0, ""); // war
				   break;
			   case 7: //highscores
//				   JarmoSetting.rekordMenu();
				   JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.TemporaryUnavailable), 8); //was: 24				   
				   break;
			   case 8:
				   JarmoSetting.nyitoMenu();
				   break;
				   
		
			   case 9: //wifi
				   JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.TemporaryUnavailable), 8); //was: 24
				   //JarmoSetting.wifiMenu();
				   break;
			   case 10: //bluetooth
				   //JarmoSetting.wifiMenu();
				   break;
			   case 11: //net
				   //JarmoSetting.wifiMenu();
				   break;
			   case 12:
				   JarmoSetting.nyitoMenu();
				   break;

			   case 13:
//GDPR				   JarmoService.sendToSocket("1 WIFI GAME ACCEPTED");
//GDPR				   JarmoSetting.currentState=JarmoSetting.CurrentState.STATE_IN_WIFIGAME;
//GDPR				   JarmoSetting.startGame(4, 1, 0, JarmoSetting.enemyId); //passziv
				   break;
			   case 14:
//GDPR				   JarmoService.sendToSocket ("0 WIFI GAME REJECTED");
//GDPR				   JarmoService.remoteWifiIP="";
//GDPR				   JarmoSetting.nyitoMenu(); //vissza
				   break;
			   case 15:  //stop scanning -> cancel
				   try {
					   JarmoSetting.wifiSearch.cancel(true);
					   JarmoService.JarmoLog  ("STOP");
				   } catch (Exception e) {e.printStackTrace();}
				   JarmoSetting.nyitoMenu(); //vissza
				   break;

				   
			   //androidStrengthMenu
			   case 16:
				   JarmoSetting.startGame (1, JarmoSetting.valasztottszin, 0, "");
				   break;
			   case 17:
				   JarmoSetting.startGame (1, JarmoSetting.valasztottszin, 1, "");
				   break;
			   case 18:
				   JarmoSetting.startGame (1, JarmoSetting.valasztottszin, 2, "");
				   break;
			   case 19:
				   JarmoSetting.android2Menu();
				   break;
				   
			   //rekordmenu
			   case 20:
//GDPR				   JarmoSetting.toplista ();
				   break;
			   case 21:
				   //JarmoSetting.nameSetMenu();
				   break;
			   case 22:	   
//				   JarmoSetting.setRecord(0);  //GDPR
				   JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.HighScoreResetOK), 24);
				 //  JarmoSetting.nameSetMenu();
				   break;
			   //JarmoNameSetMenu	   
			   case 23: //store username
				  // JarmoSetting.storeUsername();
				   break;
			   case 24: //megse
//				   JarmoSetting.rekordMenu(); //GDPR
				   break;
			   case 25: //allowmenu  
				 //  JarmoSetting.allowMenu ();
				   break;
			   case 26: //allowmenu -> yes 
//				   JarmoSetting.storeAllow (1);
				   break;
			   case 27: //allowmenu -> no
//				   JarmoSetting.storeAllow (0);
				   break;

			   case 100: //hamarosanbol vissza
				   JarmoSetting.nyitoMenu();
				   break;
				   
			   default:	   
				   JarmoSetting.jarmoMessage(JarmoSetting.js.getString(R.string.NotAvailable));
				   break;
			   }

		       
		   		if (reverse==false) JarmoSetting.menuNyit();
				   else JarmoSetting.menuVisszaBe();


			}
		}, 700);
	  }//ACTION_DOWN
	  return true;
	 } catch (Exception e) {
		 e.printStackTrace();
		 JarmoSetting.js.finish();
		 return true;
	 }
	}
	

	
	public void onDraw (Canvas canvas) {
		p.setStyle(Style.FILL);
		p.setColor(0xFF222222);
		if (gombAlapBitmap.isRecycled()) {
			JarmoService.JarmoLog  ("reciklalt :(");
			return;
		}
		if (!menuTxt1.equals("")) {
	    	canvas.drawRect(menuNagyAlapRect, p);
		}

	//	canvas.drawRect(menuAlapRect, p3);
		if (pressed==0) canvas.drawBitmap (gombAlapBitmap, null, menuAlapRect, p3);
		else canvas.drawBitmap (gombAlapBitmapf, null, menuAlapRect, p3);
		
	//	canvas.drawRect(iconRect, p2);
		p4.setColor(0xFFFFFFFF);
		p4.setTextAlign(Align.CENTER);
		p4.setTextSize(JarmoSetting.TEXTSIZE);
	//	p.setTypeface(tf);
		if (!menuTxt1.equals("")) {
		  canvas.drawText(menuTxt1, iconRect.left+(iconRect.width()/2), menuAlapRect.bottom+JarmoSetting.TEXTSIZE, p4);
		  canvas.drawText(menuTxt2, iconRect.left+(iconRect.width()/2), menuAlapRect.bottom+JarmoSetting.TEXTSIZE*2, p4);
		}
		canvas.drawBitmap(iconBitmap, null, iconRect, p4);
		
	}

}


class RekordElem {
	String id;
	String username;
	String datum;
	String score;

}