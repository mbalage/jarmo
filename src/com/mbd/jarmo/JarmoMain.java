package com.mbd.jarmo;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

import org.apache.commons.net.ftp.FTPClient;

//import com.google.ads.AdRequest;
import com.mbd.jarmo.R;
//import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
//import com.purplebrain.*;

public class JarmoMain extends Activity {
	
	public static int WIDTH=0;
	public static int HEIGHT=0;
	public static int SCREENWIDTH=0;
	public static int SCREENHEIGHT=0;
		
	public static int pointFent = 0;
	public static int pointLent = 0;
	
	public static int timerValue =0;
//	public static int timerGap =0;
	public static int timerTotal =30;
	
	public static final int NUMBER_OF_WARS=8;
	
	public static int warScores [];
	
	public static int pointUser=0;
	public static int pointUser2=0;
	public static int pointAndroid=0;
	
	public static int gameEnded;
	public static int strength=0;
	
	public static Activity jarmoMain;
	public static String enemyId="";
	
	public static Object synch = new Object();
	
	
	public static int RADIUS; 
	public static int BOGYORADIUS; 
	
	public static Mezo[] mezok = new Mezo [50];
	public static Osszekot[] osszekot = new Osszekot [530];
	
	public static Bogyo[] feherbogyo = new Bogyo [6];
	public static Bogyo[] feketebogyo = new Bogyo [6];
	
    public static Handler crossHandler;
    public static Mezo [] crossedMezok = new Mezo[5];
	
	
	public static String fentName = "";
	public static String lentName = "";
	
	public static EventDetector ed;
	public static Alap alap;
	public static FrameLayout main;
	public static Bundle staticInstanceState;
	
	public static Rect mainfield;
	public static Rect field1;
	public static Rect field2;
	public static Rect helpField;
	
	public static Rect nyilField1, nyilField2, timerField=new Rect();
	
	public static HelpRectView hrv;
	public static TableRectView trv;	
//	public static SwitchUserArrow sua;
		
	
	
	public static int userVisszarakhat;
	public static int userSzine=1;
	public static int userLent=1;
	public static int jatekMode;
	public static int jatekLevel;
	public static int crossLevel=0;
	
	public static BogyoPosWifi bogyoPosWifi = null;
	
	public static int androidHelyettLepUser = 0;
	public static int wifiActPass=0;
	
	//public static Vector<Runnable> runnables;
	public static Handler handler;
	
	public static Arrow arrowView;
//	public static Arrow arrowLent;
//	public static Typeface tf;
	
	
	public static int creaTest=0;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jarmo_main);
        
        JarmoMain.jarmoMain=this;
      
        FrameLayout main = (FrameLayout) findViewById(R.id.main_view);
        JarmoMain.handler = new Handler ();
        JarmoMain.crossHandler = new Handler ();
        Bundle extras = getIntent().getExtras();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
  //      JarmoSetting.adView = new AdView(JarmoSetting.js);
  //      JarmoSetting.adView.setAdSize(AdSize.BANNER);
  //      JarmoSetting.adView.setAdUnitId("ca-app-pub-1664907877798103/2278129678");
        
  //      AdRequest adRequest = new AdRequest.Builder()            
  //      .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
  //      .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
  //      .build();
        
  //      JarmoSetting.adView.loadAd(adRequest);

        
//		tf = Typeface.createFromAsset(this.getAssets(), "MTCORSVA.TTF"/*"ITCKRIST.TTF"*/);

		this.setVolumeControlStream (AudioManager.STREAM_NOTIFICATION);

        if (extras != null) {
        	JarmoService.JarmoLog  ("van intent!!!!!");
           jatekMode = extras.getInt("mode");
           strength = extras.getInt("strength"); 
 
            if (jatekMode==1) { // android ellen, fix szin
               	 JarmoMain.userSzine=extras.getInt("userWith");
               	 JarmoService.JarmoLog  ("fix szin, erkezett userszine = "+JarmoMain.userSzine);
            }
            if (jatekMode==2 || JarmoMain.userSzine==2) {//android ellen, valtozo szin;
              	 JarmoMain.userSzine=0;
            	 JarmoService.JarmoLog  ("valtozoszin, ero= "+JarmoMain.strength);
            }
        	   
           if (jatekMode==4) {
        	//   JarmoMain.androidHelyettLepUser=1;
        	   JarmoMain.wifiActPass=extras.getInt("userWith");
        	   if (JarmoMain.wifiActPass==0) {
        		   JarmoMain.userSzine=0;
        		   JarmoMain.userLent=0;
        		   JarmoMain.fentName=JarmoSetting.js.getString(R.string.NameYou); 
        		   String enemyId=extras.getString("enemyId");
        		   JarmoMain.lentName=enemyId;
        		   JarmoMain.enemyId=enemyId;
        	   }
        	   if (JarmoMain.wifiActPass==1) {
        		   JarmoMain.userSzine=1;
        		   JarmoMain.userLent=1;
        		   JarmoMain.lentName=JarmoSetting.js.getString(R.string.NameYou);
        		   String enemyId=extras.getString("enemyId");
        		   JarmoMain.fentName=enemyId;
           		   JarmoMain.enemyId=enemyId;
        	   }
        	   
           }
           if (jatekMode==5) {
        	   JarmoMain.userLent=0;
        	   JarmoMain.userSzine=0;
        	   JarmoMain.fentName=JarmoSetting.js.getString(R.string.NameYou);
        	   JarmoMain.lentName=JarmoSetting.js.getString(R.string.NameAndroid);
           }
           if (jatekMode==6) {//war, kezdes; 1/1 csata adatai
        	     JarmoMain.userLent=0;
            	 JarmoMain.userSzine=0;
            	 JarmoMain.strength=1; //kezdes kozepso szinten (0-1-2)
            	 JarmoMain.jatekLevel=1;
            	 JarmoMain.timerValue=0;            	 
            	 JarmoMain.timerTotal=0;
            	 JarmoMain.crossLevel=0;
            	 JarmoMain.stopTimer();
            	 JarmoMain.timerField.set(0,0,0,0);
        		 JarmoMain.warScores = new int[NUMBER_OF_WARS+1];
        		 for (int t=1;t<=NUMBER_OF_WARS;t++) {
        			 JarmoMain.warScores[t]=0;
        		 }
          	 JarmoService.JarmoLog  ("war, ero= "+JarmoMain.strength);
          }           

        } 

        JarmoMain.creaTest=1;
        
        main.postDelayed(new Runnable() {
          public void run () {	
        	Rect recttotal=new Rect ();
        	JarmoMain.main = (FrameLayout) findViewById(R.id.main_view);
    //        JarmoMain.main.getWindowVisibleDisplayFrame(recttotal);
//            Rect rectdecor = new Rect();
//            Window win = getWindow();
//            win.getDecorView().getWindowVisibleDisplayFrame(rectdecor);
          //  JarmoService.JarmoLog  ("decor height "+rectdecor.top);

            DisplayMetrics metrics =JarmoMain.main.getContext().getResources().getDisplayMetrics();
            JarmoMain.WIDTH = metrics.widthPixels;
            JarmoMain.HEIGHT = metrics.heightPixels-
            		JarmoSetting.adHeight;
            
            
        //    if (JarmoMain.WIDTH < 468) {JarmoMain.adSize=AdSize.AUTO_HEIGHT) JarmoMain.adHeight=50;
                
//            JarmoMain.WIDTH = recttotal.width();
//            JarmoMain.HEIGHT = recttotal.height();//-rectdecor.height();
            JarmoMain.SCREENHEIGHT=JarmoMain.HEIGHT;
            JarmoMain.SCREENWIDTH=JarmoMain.WIDTH;
            
            if (JarmoMain.HEIGHT > JarmoMain.WIDTH) JarmoMain.RADIUS=JarmoMain.WIDTH/25; //allo
            else JarmoMain.RADIUS=JarmoMain.HEIGHT/25;
            
            JarmoMain.BOGYORADIUS=(int)(JarmoMain.RADIUS*1.5);
            
       	    JarmoMain.pointAndroid=0;
            JarmoMain.pointUser=0;
            JarmoMain.pointUser2=0;
     
            JarmoMain.hrv=new HelpRectView (JarmoMain.jarmoMain);
        	JarmoMain.main.addView(hrv); 
     
            JarmoMain.trv=new TableRectView (JarmoMain.jarmoMain);
        	JarmoMain.main.addView(trv); 

            if (jatekMode==5) { //help
     	  	   String txt= JarmoSetting.js.getString(R.string.HelpStr1);
	  			   
           	   showHelp (txt, 15000, 100, 0, new MyCallback () {
           		   public void doStuff () {
           			   JarmoService.JarmoLog  ("myCallback is here");
           			   JarmoMain.buildGame (0);
           		   }
           	   });
            } else { //ha nem help, mehet a jatek azonnal
  
               JarmoMain.buildGame (0);
            }

            

          } //run() ends
        }, 200); //runnable ends
        
      
        staticInstanceState = savedInstanceState;
        

		//        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
//GDPR        IntentFilter intentFilter = new IntentFilter();
//GDPR        intentFilter.addAction(JarmoService.JARMOSOCKET);
//DDPR        intentFilter.addAction(JarmoService.JARMOREQUEST);
//GDPR        this.registerReceiver(jarmoReceiver, intentFilter);
        JarmoService.JarmoLog  ("JSetting registered$$$$");
     
} //onCreate
//GDPR
/*    
 public static BroadcastReceiver jarmoReceiver = new BroadcastReceiver() {
     @Override
     public void onReceive(Context context, Intent intent) {
     	JarmoService.JarmoLog  ("jarmoMain onReceive: "+intent.getAction());
         if(intent.getAction().equals(JarmoService.JARMOSOCKET)) {
             String line = intent.getStringExtra("line");
             JarmoService.JarmoLog  ("JarmoMain received line ="+line);
             if (line.startsWith ("I START")) {
   //  	         Toast.makeText(JarmoMain.jarmoMain, "Other party starts", Toast.LENGTH_SHORT).show();
      	  	   String txt= JarmoSetting.js.getString(R.string.OtherPartyStarts);
        	   showHelp (txt, 1000, 95, 3000, null);
 
     	         JarmoMain.engedelyezMozg(0);
            	 androidHelyettLepUser=1;
             }

             if (line.startsWith ("YOU START")) {
            	 //  	         Toast.makeText(JarmoMain.jarmoMain, "You start", Toast.LENGTH_SHORT).show();
            	 String txt= JarmoSetting.js.getString(R.string.YouStart);
            	 showHelp (txt, 1000, 80, 3000, null);

            	 JarmoMain.engedelyezMozg(1);
            	 androidHelyettLepUser=0;
            	 return;
             }
             if (line.startsWith ("WIFI PAUSED")) {
            	 //      Toast.makeText(JarmoMain.jarmoMain, "Other party paused", Toast.LENGTH_SHORT).show();
            	 String txt= JarmoSetting.js.getString(R.string.OtherPartyPaused);
            	 showHelp (txt, 1000, 95, 0, null);      	 
            	 return;
             }
             if (line.startsWith ("WIFI RESUMES")) {
            	 //    	         Toast.makeText(JarmoMain.jarmoMain, "Other party returned", Toast.LENGTH_SHORT).show();
            	 String txt= JarmoSetting.js.getString(R.string.OtherPartyReturned);
            	 showHelp (txt, 1000, 95, 0, null);      	 
            	 return;
             }
             if (line.startsWith ("BOGYO STARTMOVE")) {
            	 int id=Integer.parseInt (line.substring(16));
            	 //Bogyo bb = JarmoMain.bogyoItt(JarmoMain.mezok[id]);
            	 JarmoMain.redrawMezok (JarmoMain.mezok[id]);                	 
             }

             if (line.startsWith ("BOGYO MOVE")) {
            	 try {
            		 int id=Integer.parseInt (line.substring(11, 13));
            		 int x=Integer.parseInt (line.substring(14, 18)); 
            		 int y=Integer.parseInt (line.substring(19, 23)); 
            		 x=x*JarmoMain.SCREENWIDTH/1000;
            		 y=y*JarmoMain.SCREENHEIGHT/1000;
            		 JarmoService.JarmoLog  ("Bogyo move jott id"+id+" x"+x+" y"+y);
            		 Bogyo bb = JarmoMain.bogyoItt(JarmoMain.mezok[id]);
            		 bb.px=x;
            		 bb.py=y;
            		 bb.invalidate();
            		 return;
            	 } catch (Exception e) {e.printStackTrace();}

             }
             if (line.startsWith ("BOGYO ENGED")) {
         		 int id=Integer.parseInt (line.substring(12));
          		 Bogyo bb = JarmoMain.bogyoItt(JarmoMain.mezok[id]);
          	   	 bb.posToMezo (); //akarhol van, menjen bogyohoz        		 
            	 JarmoMain.redrawMezok (null);
            	 return;
             }
             
             if (line.startsWith ("BOGYO DOB")) {
          		 int from_id=Integer.parseInt (line.substring(10, 12));
         		 int to_id=Integer.parseInt (line.substring(17));
           		 Bogyo bb = JarmoMain.bogyoItt(JarmoMain.mezok[from_id]);
          		 JarmoService.JarmoLog  ("from "+from_id+" to "+to_id);
          		 JarmoMain.checkUserMove (bb, JarmoMain.mezok[to_id]); 
      		      JarmoService.JarmoLog  ("check ok");
        	   	 bb.posToMezo (); //akarhol van, menjen bogyohoz

          		 JarmoMain.redrawMezok (null);
            	 return;
             }
         }

    }
 }; 
*/
    

    	
   public void onResume () {
	   super.onResume();
//	   try {
//	    if (JarmoSetting.adView != null) {
//	    	JarmoSetting.adView.resume();
//	      }
//	   } catch (Exception e) {e.printStackTrace();}
	   JarmoService.JarmoLog  ("on resume, creaTest="+JarmoMain.creaTest);
	   if (JarmoMain.creaTest==0) {
	//		if (AdBuddiz.isReadyToShowAd(JarmoSetting.js)) { // this = current Activity
	//			AdBuddiz.showAd(JarmoSetting.js);
    //        }
	
		   JarmoMain.jarmoMain.finish();
		   return;
	   }
	   if (JarmoMain.alap==null) return;
		
	//   if (JarmoMain.alap==null || JarmoMain.alap.sepField1==null) { //valami tortent az alappal => inkabb bezar
	//	   JarmoMain.jarmoMain.finish();
	//	   return;
	//   }
	    for (int t=1; t<=5; t++) {
	    	JarmoMain.feherbogyo[t].updateForgat();
	    	JarmoMain.feketebogyo[t].updateForgat();
	    	JarmoMain.feherbogyo[t].updateFlash();
	    	JarmoMain.feketebogyo[t].updateFlash();
	    }

	   if (JarmoMain.alap.nyilFentHasFlash) JarmoMain.alap.doFlash(0);
	   if (JarmoMain.alap.nyilLentHasFlash) JarmoMain.alap.doFlash(1);	
	   if (JarmoMain.timerTotal>0) JarmoMain.startTimer(1);
   }

   public void onRestart () {
	   super.onRestart();
	   JarmoService.JarmoLog  ("on restart");
//	   if (JarmoMain.jatekMode==4) {
//		      JarmoService.sendToSocket("WIFI RESUMES");
//		   }
   }
   
   public void onPause () {
	   super.onPause();
	//   try {
	 //   if (JarmoSetting.adView != null) {
	//    	JarmoSetting.adView.pause();
	//      }
	//   } catch (Exception e) {e.printStackTrace();}
	   JarmoService.JarmoLog  ("onPause");
//	   if (JarmoMain.jatekMode==4) {
//	      JarmoService.sendToSocket("WIFI PAUSED");
//	   }
	//	if (AdBuddiz.isReadyToShowAd(JarmoSetting.js)) { // this = current Activity
	//		System.out.println ("isready");
	//			AdBuddiz.showAd(JarmoSetting.js);
	//	} else {
	//		System.out.println ("not ready for AD");
	//	}

	   JarmoMain.jarmoMain.finish();
	   
   }
   
   public void onStop () {
	   super.onStop();
	   JarmoService.JarmoLog  ("JarmoMain onStop &&&&&&&&&&&&&&&&");
   }
   
   public void onDestroy () {
	   super.onDestroy();
//	   try {
//	    if (JarmoSetting.adView != null) {
//	    	JarmoSetting.adView.destroy();
//	      }
//	   } catch (Exception e) {e.printStackTrace();}
	   JarmoService.JarmoLog  ("OnDestroy @@@@@@@@@@@@@@@@");
	   JarmoSetting.currentState=JarmoSetting.CurrentState.STATE_IDLE;
//GDPR       JarmoMain.jarmoMain.unregisterReceiver(jarmoReceiver);
       JarmoMain.handler.removeCallbacksAndMessages(null);
       JarmoMain.hrv.winHandler.removeCallbacksAndMessages(null);
       JarmoMain.trv.winHandler.removeCallbacksAndMessages(null);
	   if (JarmoMain.jatekMode==4) {
//GDPR		   JarmoService.sendToSocket("WIFI GAME STOP");
//GDPR		   JarmoService.resetWifiConnectionData();
		   JarmoSetting.jarmoMessage (JarmoSetting.js.getString(R.string.WifiGameTerminated));	           
           JarmoSetting.menuNyit ();
           

	   }
   }
    
 
   
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	
    	JarmoService.JarmoLog  ("BMO save instance\n");
    	
    	JarmoMain.handler.removeCallbacksAndMessages(null);
    	
        // Save the user's current game state
    	if (vanBogyoPalyan()) {
          savedInstanceState.putInt("feher1", feherbogyo[1].mezo.id);
          savedInstanceState.putInt("feher2", feherbogyo[2].mezo.id);
          savedInstanceState.putInt("feher3", feherbogyo[3].mezo.id);
          savedInstanceState.putInt("feher4", feherbogyo[4].mezo.id);
          savedInstanceState.putInt("feher5", feherbogyo[5].mezo.id);

          savedInstanceState.putInt("fekete1", feketebogyo[1].mezo.id);
          savedInstanceState.putInt("fekete2", feketebogyo[2].mezo.id);
          savedInstanceState.putInt("fekete3", feketebogyo[3].mezo.id);
          savedInstanceState.putInt("fekete4", feketebogyo[4].mezo.id);
          savedInstanceState.putInt("fekete5", feketebogyo[5].mezo.id);

          savedInstanceState.putInt("feher1a", feherbogyo[1].allapot);
          savedInstanceState.putInt("feher2a", feherbogyo[2].allapot);
          savedInstanceState.putInt("feher3a", feherbogyo[3].allapot);
          savedInstanceState.putInt("feher4a", feherbogyo[4].allapot);
          savedInstanceState.putInt("feher5a", feherbogyo[5].allapot);

          savedInstanceState.putInt("fekete1a", feketebogyo[1].allapot);
          savedInstanceState.putInt("fekete2a", feketebogyo[2].allapot);
          savedInstanceState.putInt("fekete3a", feketebogyo[3].allapot);
          savedInstanceState.putInt("fekete4a", feketebogyo[4].allapot);
          savedInstanceState.putInt("fekete5a", feketebogyo[5].allapot);

          savedInstanceState.putInt("pointUser", JarmoMain.pointUser);
          savedInstanceState.putInt("pointUser2", JarmoMain.pointUser2);
          savedInstanceState.putInt("pointAndroid", JarmoMain.pointAndroid);
          savedInstanceState.putInt("androidHelyett", JarmoMain.androidHelyettLepUser);
          
    	}      
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.activity_jarmo_main, menu);
        return true;
    }
    
    public static void buildGame (int cont) {

    	JarmoMain.handler.removeCallbacksAndMessages(null);
    	System.gc();
    	JarmoService.JarmoLog  ("BUILDGAME UserLent="+JarmoMain.userLent+" UserSzine="+JarmoMain.userSzine);
    	JarmoService.JarmoLog  ("-----> fentName="+JarmoMain.fentName+" lentName="+lentName);

    	JarmoMain.handler.removeCallbacksAndMessages(null);

    	if (cont==0) {
       	JarmoMain.alap = new Alap (JarmoMain.main.getContext());
      	JarmoMain.main.addView (alap);
       	JarmoMain.ed = new EventDetector (JarmoMain.main.getContext());
    	JarmoMain.main.addView (ed);
    	}
        
    	try {
  //      FrameLayout.LayoutParams adsParams =
  //      		new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
  //      				                     FrameLayout.LayoutParams.WRAP_CONTENT, 
  //      				                     android.view.Gravity.BOTTOM|android.view.Gravity.CENTER_HORIZONTAL); 
  //      JarmoMain.main.addView(JarmoSetting.adView, adsParams ); 
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        

    	JarmoMain.gameEnded=0;

    	//JarmoService.JarmoLog  ("W "+JarmoMain.WIDTH+"H "+JarmoMain.HEIGHT+"RAD "+JarmoMain.RADIUS);

    	JarmoMain.tablarajz (JarmoMain.main.getContext());

    	//View-k a mainhoz rendelese
    	int k=1;

    	if (cont==0) {    	
        	while (JarmoMain.osszekot[k]!=null) {  		
        		JarmoMain.main.addView(JarmoMain.osszekot[k]);
        		k++;        		
        	} 
        	for (k=1;k<=25;k++) {
        		JarmoMain.main.addView(JarmoMain.mezok[k]);
         	}


        	for (k=1; k<=5; k++) {
        		JarmoMain.main.addView(JarmoMain.mezok[30+k]);
        		JarmoMain.main.addView(JarmoMain.mezok[40+k]);
        	}
       } 

    	for (k=1;k<=25;k++) {
    		final int t=k;
    		JarmoMain.handler.postDelayed(new Runnable () {

    			public void run () {
    				JarmoMain.mezok[t].lathato=1;
    				JarmoMain.mezok[t].invalidate();
    			}
    		}, k*50);
    	}

    	k=1;
    	while (JarmoMain.osszekot[k]!=null) {  		
    		final int t=k;
    		JarmoMain.handler.postDelayed(new Runnable () {			
    			public void run () {
    				JarmoMain.osszekot[t].setLathato();
    				JarmoMain.osszekot[t].invalidate();
    			}
    		}, 1000+(t*40));
    		k++;        		
    	}

    	int feherlent=0;
    	if (JarmoMain.userSzine==0) {
    		if (JarmoMain.userLent==0) feherlent=0; else feherlent=1;
    	} else { //user with fekete
    		if (JarmoMain.userLent==0) feherlent=1; else feherlent=0;   		  
    	}

    	if (JarmoMain.userLent==0) {
    		JarmoMain.pointFent=JarmoMain.pointUser;
    		JarmoMain.pointLent=JarmoMain.pointAndroid;
    	} else {
    		JarmoMain.pointLent=JarmoMain.pointUser;
    		JarmoMain.pointFent=JarmoMain.pointAndroid;   		  
    	}

    	if (feherlent==0) {
    		JarmoMain.feherbogyo[1]=newBogyo (cont, 1,JarmoMain.main.getContext(), JarmoMain.mezok[31], 0);
    		JarmoMain.feherbogyo[2]=newBogyo (cont, 2,JarmoMain.main.getContext(), JarmoMain.mezok[32], 0);
    		JarmoMain.feherbogyo[3]=newBogyo (cont, 3,JarmoMain.main.getContext(), JarmoMain.mezok[33], 0);
    		JarmoMain.feherbogyo[4]=newBogyo (cont, 4,JarmoMain.main.getContext(), JarmoMain.mezok[34], 0);
    		JarmoMain.feherbogyo[5]=newBogyo (cont, 5,JarmoMain.main.getContext(), JarmoMain.mezok[35], 0);

    		JarmoMain.feketebogyo[1]=newBogyo (cont, 1,JarmoMain.main.getContext(), JarmoMain.mezok[41], 1);
    		JarmoMain.feketebogyo[2]=newBogyo (cont, 2,JarmoMain.main.getContext(), JarmoMain.mezok[42], 1);
    		JarmoMain.feketebogyo[3]=newBogyo (cont, 3,JarmoMain.main.getContext(), JarmoMain.mezok[43], 1);
    		JarmoMain.feketebogyo[4]=newBogyo (cont, 4,JarmoMain.main.getContext(), JarmoMain.mezok[44], 1);
    		JarmoMain.feketebogyo[5]=newBogyo (cont, 5,JarmoMain.main.getContext(), JarmoMain.mezok[45], 1);
    	} else { 
    		JarmoMain.feketebogyo[1]=newBogyo (cont, 1,JarmoMain.main.getContext(), JarmoMain.mezok[31], 1);
    		JarmoMain.feketebogyo[2]=newBogyo (cont, 2,JarmoMain.main.getContext(), JarmoMain.mezok[32], 1);
    		JarmoMain.feketebogyo[3]=newBogyo (cont, 3,JarmoMain.main.getContext(), JarmoMain.mezok[33], 1);
    		JarmoMain.feketebogyo[4]=newBogyo (cont, 4,JarmoMain.main.getContext(), JarmoMain.mezok[34], 1);
    		JarmoMain.feketebogyo[5]=newBogyo (cont, 5,JarmoMain.main.getContext(), JarmoMain.mezok[35], 1);

    		JarmoMain.feherbogyo[1]=newBogyo (cont, 1,JarmoMain.main.getContext(), JarmoMain.mezok[41], 0);
    		JarmoMain.feherbogyo[2]=newBogyo (cont, 2,JarmoMain.main.getContext(), JarmoMain.mezok[42], 0);
    		JarmoMain.feherbogyo[3]=newBogyo (cont, 3,JarmoMain.main.getContext(), JarmoMain.mezok[43], 0);
    		JarmoMain.feherbogyo[4]=newBogyo (cont, 4,JarmoMain.main.getContext(), JarmoMain.mezok[44], 0);
    		JarmoMain.feherbogyo[5]=newBogyo (cont, 5,JarmoMain.main.getContext(), JarmoMain.mezok[45], 0);

    	}
if (cont==0) {
    	k=1;
    	while (k<=5) {
    		JarmoMain.main.addView(JarmoMain.feherbogyo[k]);
    		JarmoMain.main.addView(JarmoMain.feketebogyo[k]);
    		JarmoMain.ed.registerView(JarmoMain.feherbogyo[k]);
    		JarmoMain.ed.registerView(JarmoMain.feketebogyo[k]);
    		k++;
    	}   
}
    	bogyoRedraw (JarmoMain.main.getContext(), 0); // tabla szelere rajzol, nincs becsusz


    	for (k=1; k<=5; k++) { 	           	
    		final int tt = k;
    		JarmoMain.handler.postDelayed(new Runnable () { //parkban megjelenit
    			public void run () {
    				JarmoMain.feherbogyo[tt].mezoHist.removeAllElements();
    				JarmoMain.feketebogyo[tt].mezoHist.removeAllElements();  				
    				JarmoMain.feherbogyo[tt].lathato=1;
    				JarmoMain.feketebogyo[tt].lathato=1;
    				JarmoMain.feherbogyo[tt].isParkolt=false;
    				JarmoMain.feketebogyo[tt].isParkolt=false;  				
    				JarmoMain.feherbogyo[tt].invalidate();
    				JarmoMain.feketebogyo[tt].invalidate();
    			}
    		}, 1000+ (k*100));
    	}
  //  	if (cont==0) {
    	   JarmoMain.arrowView = new Arrow (JarmoMain.jarmoMain);
    	   JarmoMain.main.addView(arrowView);
           JarmoMain.hrv.bringToFront();
           JarmoMain.trv.bringToFront();
  //  	}
    	
    	if (JarmoMain.jatekMode==5) { //help mode
 
    		String txt1=JarmoSetting.js.getString(R.string.HelpStr2);
    
    		showHelp (txt1, 15000, 100, 0, new MyCallback () {
    			public void doStuff () {
    				JarmoService.JarmoLog  ("myCallback is here2");
    				// JarmoMain.buildGame (0);
    				JarmoMain.engedelyezMozg(0);
    				JarmoMain.demoStep(1); //start demo
    			}
    		});
    		return;
    	}
    	JarmoMain.handler.postDelayed(new Runnable () { //palyara berak;
    		public void run () {
    			int fentSzin=0;
    			JarmoService.JarmoLog  ("MEZO AZONOSITO "+JarmoMain.feherbogyo[1].mezo.id);
    			if (JarmoMain.feherbogyo[1].mezo.id==31) fentSzin=0;
    			else fentSzin=1;

    			int turn=0;
    			if (JarmoMain.jatekMode==3 && 
    					(JarmoMain.userLent==0 || 
    					 (JarmoMain.userLent==1 && JarmoMain.androidHelyettLepUser==1)) )  {
    				turn=1;
    			} else {
    				turn=0;
    			}

    			initGame (JarmoMain.main.getContext(), JarmoMain.staticInstanceState); //bogyok a palyan a mefelelo helyre...
    			bogyoRedraw (JarmoMain.main.getContext(), 1); //...becsusznak	
    			JarmoMain.userVisszarakhat=0;

    			if (JarmoMain.jatekMode==6) { //war nyitas
 /*   		        
    		         	int war = ((JarmoMain.jatekLevel-1) / 4) + 1;
   		        	    int battle = ((JarmoMain.jatekLevel-1) % 4)+1;
   		        	    
   		        	    String txt=JarmoSetting.js.getString(R.string.War)+" "+war+" / "+JarmoSetting.js.getString(R.string.Battle)+" "+battle;
   		        	    switch (JarmoMain.strength) {
   		        	    case 0:
   		        	         txt+=" %"+JarmoSetting.js.getString(R.string.MenuAndroidWeak1)+" "+JarmoSetting.js.getString(R.string.MenuAndroidWeak2);
 		        	         break;
   		        	    case 1:
 	   		        	      txt+=" %"+JarmoSetting.js.getString(R.string.MenuAndroidMedium1)+" "+JarmoSetting.js.getString(R.string.MenuAndroidMedium2);
 	   		        	      break;
   		        	    case 2:
 	   		        	      txt+=" %"+JarmoSetting.js.getString(R.string.MenuAndroidStrong1)+" "+JarmoSetting.js.getString(R.string.MenuAndroidStrong2);
 	   		        	      break;
   		        	    }
   		        	    if (JarmoMain.timerTotal!=0) {
	   		        	      txt+=" %"+JarmoSetting.js.getString(R.string.TimeForStep)+": "+JarmoMain.timerTotal+ " "+JarmoSetting.js.getString(R.string.Seconds);
   		        	    }
 			        	 
		        	    if (JarmoMain.jatekLevel % 2 ==1)     
			        	    	 txt+=" %"+JarmoSetting.js.getString(R.string.AttackFromTop);
			        	    else	
			        	    	 txt+=" %"+JarmoSetting.js.getString(R.string.AttackFromBottom);
		        	             
		        	    if (JarmoMain.jatekLevel % 4 ==1 || JarmoMain.jatekLevel % 4 ==2)     
		        	    	 txt+=" %"+JarmoSetting.js.getString(R.string.YouStart);
		        	    else	
		        	    	 txt+=" %"+JarmoSetting.js.getString(R.string.JarmoidStarts);
	        	        
		        	    MyCallback afterMsg = new MyCallback () {
		        	    	public void doStuff () {
	    		   				if (JarmoMain.jatekLevel % 4 ==1 || JarmoMain.jatekLevel % 4 ==2) {//1,2,5,6... jatekos kezd
	    	    					JarmoMain.engedelyezMozg (1);
	    	    				} else { //3,4, 7,8 android kezd
	    	    					JarmoMain.makeAndroidMove();
	    	    				}
		        	    	}
		        	    };
    			     	showHelp (txt, 10000, 90, 0, afterMsg);
 */
	        	    final MyCallback afterMsg = new MyCallback () {
	        	    	public void doStuff () {
    		   				if (JarmoMain.jatekLevel % 4 ==1 || JarmoMain.jatekLevel % 4 ==2) {//1,2,5,6... jatekos kezd
    	    					JarmoMain.engedelyezMozg (1);
    	    				} else { //3,4, 7,8 android kezd
    	    					JarmoMain.makeAndroidMove();
    	    				}
	        	    	}
	        	    };

	        	    JarmoMain.handler.postDelayed(new Runnable () {
	        	     public void run () {	
	        	        JarmoMain.showNextGameTable(afterMsg);
	        	      }
	        	     }, 2000);
	        	      

    			}
    				
    			if (JarmoMain.jatekMode<4) {
    		//		Toast.makeText(JarmoMain.jarmoMain, "You start", Toast.LENGTH_SHORT).show();
    		   	  	   String txt= JarmoSetting.js.getString(R.string.YouStart);
    		   	  	   if (turn==1)
    	        	      showHelpTurned (txt, 1000, 50, 3000, null);
    		   	  	   else 
    		   	  		showHelp (txt, 1000, 50, 3000, null);
    				
    				engedelyezMozg (1); //jatek kezd, 1=engedelyez
    			}
    			if (JarmoMain.jatekMode==4 && JarmoMain.wifiActPass==0) { 
    				//sorsolas: ki kezd?
    				int r=(int)(Math.random()*2); //0-en kezdek, 1-te kezdesz
    				r=0;
    				if (r==0) { //random
    					engedelyezMozg (1);
//    					JarmoService.sendToSocket ("I START");
    					androidHelyettLepUser=0;

    	//				Toast.makeText(JarmoMain.jarmoMain, "You start", Toast.LENGTH_SHORT).show();
    			   	  	   String txt= JarmoSetting.js.getString(R.string.YouStart);
        	        	   showHelp (txt, 1000, 50, 3000, null);
        

    				} else {
    					engedelyezMozg (0);
    					androidHelyettLepUser=1;
//    					JarmoService.sendToSocket ("YOU START");
    			//		Toast.makeText(JarmoMain.jarmoMain, "Other party starts", Toast.LENGTH_SHORT).show();
    			   	  	   String txt= JarmoSetting.js.getString(R.string.OtherPartyStarts);
        	        	   showHelp (txt, 1000, 60, 3000, null);
        
    				}
    			}
    		}
    	}, 2000);

    }
    
    public static int toBeStopped=0;
    public static void startTimer (int cont) {
    	JarmoService.JarmoLog  ("startTimer");
    	JarmoMain.toBeStopped=0;
    	if (cont==0) JarmoMain.timerValue=100;
    	if (JarmoMain.timerTotal==0) return; // nincs timer
    	final int gap=JarmoMain.timerTotal*1000/100;
    	Runnable inc = new Runnable () {
    		public void run () {
    			
    			    JarmoService.JarmoLog  ("incr "+JarmoMain.timerValue);
    			    
    				JarmoMain.timerValue--;
    				 if (JarmoMain.userLent==1) 
    	    		   JarmoMain.timerField.set (0,
                             JarmoMain.HEIGHT-JarmoMain.BOGYORADIUS*3-JarmoMain.helpField.height(),
                             JarmoMain.mainfield.right* (JarmoMain.timerValue)/100, 
                             JarmoMain.HEIGHT-JarmoMain.BOGYORADIUS*2-JarmoMain.helpField.height());
    				 else 
                       JarmoMain.timerField.set(0, 
		                          JarmoMain.BOGYORADIUS*2,
		                          JarmoMain.mainfield.right* (JarmoMain.timerValue)/100,
		                          JarmoMain.BOGYORADIUS*3);           

    				if (JarmoMain.timerValue==0) {
    					JarmoMain.timeout ();
    				}
    		    JarmoService.JarmoLog  ("Timer ToBeStopped="+JarmoMain.toBeStopped);
    			if (JarmoMain.toBeStopped==1) {
                    JarmoMain.timerField.set(0, 
	                          JarmoMain.BOGYORADIUS*2,
	                          0,
	                          JarmoMain.BOGYORADIUS*3);    
     				return;
    			}
    			if (JarmoMain.timerTotal!=0) {
    			  JarmoMain.handler.postDelayed(this, gap);
    			}
    		}
    	};
    	JarmoMain.handler.postDelayed(inc, gap);
    }
    
    public static void stopTimer () {
    	JarmoMain.timerValue=-1;
    	JarmoMain.toBeStopped=1;
    }
    
    public static void timeout () {
    	JarmoService.JarmoLog  ("Timer timeout");
		JarmoSetting.playSound(R.raw.timer);
		JarmoMain.engedelyezMozg(0);
		JarmoMain.handler.postDelayed(new Runnable () {
			public void run () {
		        JarmoMain.endGame();				
			}
		}, 2000);
    }
    
    public static void demoStep (int phase) {
    	JarmoService.JarmoLog  ("Executing phase "+phase);
    	if (phase==1) { // bogyok becsusznak
    		initGame (JarmoMain.main.getContext(), JarmoMain.staticInstanceState); //bogyok a palyan a mefelelo helyre...
    		bogyoRedraw (JarmoMain.main.getContext(), 1); //...becsusznak	
    		JarmoMain.userVisszarakhat=0;
    		String txt= JarmoSetting.js.getString(R.string.HelpStr3);
    		
    		showHelp (txt, 15000, 100, 2000, new MyCallback () {
    			public void doStuff () {
    				JarmoService.JarmoLog  ("myCallback is here2");
    				// JarmoMain.buildGame (0);
    				JarmoMain.demoStep(2); //start demo
    			}
    		});
    		return;
    	}
    	if (phase==2) {
    		Mezo ujm = JarmoMain.mezok[14];
    		JarmoMain.feherbogyo[1].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
  //  		JarmoMain.feherbogyo[1].mezo=JarmoMain.mezok[14];
        	 /* 		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(3);
     			}
     		}, 700);
*/
    		
    		String txt= JarmoSetting.js.getString(R.string.HelpStr4);
    		
    		showHelp (txt, 15000, 100, 2000, new MyCallback () {
    			public void doStuff () {
    				JarmoService.JarmoLog  ("myCallback is here2");
    				// JarmoMain.buildGame (0);
    				JarmoMain.demoStep(3); //start demo
    			}
    		});
    		return;
    	}
    	if (phase==3) {
    		Mezo ujm = JarmoMain.mezok[14];
 //   		JarmoMain.feketebogyo[5].mezo=JarmoMain.mezok[14];
    		JarmoMain.feketebogyo[5].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.feketebogyo[5].allapot=1;
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(4);
     			}
     		}, 1500);
    	}
    	if (phase==4) {
       		Mezo ujm = JarmoMain.mezok[7];
 //      		JarmoMain.feherbogyo[2].mezo=JarmoMain.mezok[7];
    		JarmoMain.feherbogyo[2].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
     		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(5);
     			}
     		}, 700);
     		return;
    	}
       	if (phase==5) {
       		Mezo ujm = JarmoMain.mezok[12];
 //      		JarmoMain.feketebogyo[1].mezo=JarmoMain.mezok[12];
    		JarmoMain.feketebogyo[1].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
     		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(6);
     			}
     		}, 700);
     		return;
    	}
       	
      	if (phase==6) { //feketet leut
       		Mezo ujm = JarmoMain.mezok[12];
   // 		bogyoLeut (feherbogyo[2], JarmoMain.bogyoItt(ujm), 500); //regi el
    		JarmoMain.feherbogyo[2].allapot=1;
  //  	    JarmoMain.feherbogyo[2].mezo=JarmoMain.mezok[12];
    		JarmoMain.feherbogyo[2].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
     		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(7);
     			}
     		}, 1500);
     		return;
    	}
      	if (phase==7) {
       		Mezo ujm = JarmoMain.mezok[5];
      // 		JarmoMain.feketebogyo[5].mezo=JarmoMain.mezok[5];
      		JarmoMain.feketebogyo[1].startFlash();
       		JarmoMain.feketebogyo[5].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
       		String txt= JarmoSetting.js.getString(R.string.HelpStr5);
    		
    		showHelp (txt, 15000, 100, 2000, new MyCallback () {
    			public void doStuff () {
    				JarmoService.JarmoLog  ("myCallback is here2");
    				JarmoMain.demoStep(8); //start demo
    			}
    		});
     		return;
    	}
     	if (phase==8) {
       		Mezo ujm = JarmoMain.mezok[21];
 //     		JarmoMain.feketebogyo[1].mezo=JarmoMain.mezok[21];
       		JarmoMain.feketebogyo[1].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(9);
     			}
     		}, 1500);
        	return;
    	}
     	if (phase==9) {
     		JarmoMain.feketebogyo[1].stopFlash();
       		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(10);
     			}
     		}, 700);
     		String txt= JarmoSetting.js.getString(R.string.HelpStr6);
    		
    		showHelp (txt, 5000, 100, 0, new MyCallback () {
    			public void doStuff () {
    				JarmoService.JarmoLog  ("myCallback is here2");
    				//JarmoMain.demoStep(9); //start demo
    			}
    		});
    		
      		Mezo ujm = JarmoMain.mezok[19];
     // 		JarmoMain.feherbogyo[2].mezo=JarmoMain.mezok[19];
       		JarmoMain.feherbogyo[2].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
        	return;
    	}
     	if (phase==10) {
     		Mezo ujm = JarmoMain.mezok[18];
    //  		JarmoMain.feketebogyo[1].mezo=JarmoMain.mezok[18];
       		JarmoMain.feketebogyo[1].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(11);
     			}
     		}, 700);
        	return;
     	}
     	if (phase==11) {
     		Mezo ujm = JarmoMain.mezok[12];
    //  		JarmoMain.feherbogyo[3].mezo=JarmoMain.mezok[12];
       		JarmoMain.feherbogyo[3].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(12);
     			}
     		}, 700);
        	return;
     	}
     	if (phase==12) {
     		Mezo ujm = JarmoMain.mezok[11];
//    		bogyoLeut (feketebogyo[2], JarmoMain.bogyoItt(ujm), 500); //regi el
     //		JarmoMain.feketebogyo[2].mezo=JarmoMain.mezok[11];
       		JarmoMain.feketebogyo[2].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(13);
     			}
     		}, 700);
        	return;
     	}
     	if (phase==13) {
     		Mezo ujm = JarmoMain.mezok[21];
   //   		JarmoMain.feherbogyo[3].mezo=JarmoMain.mezok[21];
       		JarmoMain.feherbogyo[3].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(14);
     			}
     		}, 700);
        	return;
     	}
     	if (phase==14) {
     		Mezo ujm = JarmoMain.mezok[13];
 //   		bogyoLeut (feketebogyo[4], JarmoMain.bogyoItt(ujm), 500); //3-ast leut
  //  		JarmoMain.feketebogyo[4].mezo=JarmoMain.mezok[13];
    // 		JarmoMain.feketebogyo[4].allapot=1;
       		JarmoMain.feketebogyo[4].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(15);
     			}
     		}, 700);
        	return;
     	}
     	if (phase==15) {
     		Mezo ujm = JarmoMain.mezok[11];
    //   		bogyoLeut (feherbogyo[4], JarmoMain.bogyoItt(ujm), 500); //regi el
    //   		JarmoMain.feherbogyo[4].mezo=JarmoMain.mezok[11];
     		JarmoMain.feherbogyo[4].allapot=1;
       		JarmoMain.feherbogyo[4].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(16);
     			}
     		}, 1500);
        	return;
     	}
     	if (phase==16) {
     		Mezo ujm = JarmoMain.mezok[19];
    //		bogyoLeut (feketebogyo[1], JarmoMain.bogyoItt(ujm), 500); //regi el
    //		JarmoMain.feketebogyo[1].mezo=JarmoMain.mezok[19];
     		JarmoMain.feketebogyo[1].allapot=1;
       		JarmoMain.feketebogyo[1].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(17);
     			}
     		}, 1500);
        	return;
     	}
     	if (phase==17) {
     		Mezo ujm = JarmoMain.mezok[12];
    //   		JarmoMain.feherbogyo[5].mezo=JarmoMain.mezok[12];
     		JarmoMain.feherbogyo[5].allapot=1;
       		JarmoMain.feherbogyo[5].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(18);
     			}
     		}, 700);
        	return;
     	}
     	if (phase==18) {
     		Mezo ujm = JarmoMain.mezok[12];
    //		bogyoLeut (feketebogyo[1], JarmoMain.bogyoItt(ujm), 500); //regi el
    //		JarmoMain.feketebogyo[1].mezo=JarmoMain.mezok[12];
       		JarmoMain.feketebogyo[1].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(19);
     			}
     		}, 1500);
        	return;
     	}
     	if (phase==19) {
     		Mezo ujm = JarmoMain.mezok[22];
  //     		JarmoMain.feherbogyo[4].mezo=JarmoMain.mezok[22];
       		JarmoMain.feherbogyo[4].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(20);
     			}
     		}, 700);
        	return;
     	}
    	if (phase==20) {
     		Mezo ujm = JarmoMain.mezok[16];
//    		bogyoLeut (feketebogyo[1], JarmoMain.bogyoItt(ujm), 500); //regi el
 //   		JarmoMain.feketebogyo[3].mezo=JarmoMain.mezok[16];
       		JarmoMain.feketebogyo[3].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		JarmoMain.handler.postDelayed(new Runnable () {
     			public void run () {
     				JarmoMain.demoStep(21);
     			}
     		}, 100);
        	return;
     	}
      	if (phase==21) {
       		String txt= JarmoSetting.js.getString(R.string.HelpStr7);
    		
    		showHelp (txt, 15000, 100, 2000, new MyCallback () {
    			public void doStuff () {
    				JarmoService.JarmoLog  ("myCallback is here2");
    				JarmoMain.demoStep(22); //start demo
    			}
    		});
     		return;
      	}
      	if (phase==22) {
      		JarmoMain.endGame();
      		return;
      	}
      	
     	if (phase==30) {
          	hrv.bringToFront();
            
       		String txt=  JarmoSetting.js.getString(R.string.HelpStr8);
    		
    		showHelp (txt, 15000, 100, 0, new MyCallback () {
    			public void doStuff () {
    				JarmoService.JarmoLog  ("myCallback is here2");
    				//JarmoMain.demoStep(22); //start demo
    	//			if (AdBuddiz.isReadyToShowAd(JarmoSetting.js)) { // this = current Activity
        //					AdBuddiz.showAd(JarmoSetting.js);
        //   		    }
    				JarmoMain.jarmoMain.finish();
    				return;
    			}
    		});
     		return;
      	}
 
    }

    public static Bogyo newBogyo (int cont, int id, Context context, Mezo mezo, int szin) {
    	if (cont==0) { // uj bogyo?
    		return new Bogyo (context, mezo, szin);
    	} else { //regi felhasznal?
    		Bogyo b;
    		if (szin==0) {
    			b=JarmoMain.feherbogyo[id];
    		} else {
    			b=JarmoMain.feketebogyo[id];
    		}
   			b.mezo=mezo;
   			b.lathato=0;
   			b.hasFlash=false;
   			b.arrowRect=null;
   			b.arrowFlipped=0;
   			b.arrowPhase=null;
   			b.allapot=0;
   			b.startArrow=0;
   			
   			return b;
   	   	}
    
    }
    
    public static boolean vanBogyoPalyan () {
    	
     	for (int t=1;t<=5;t++) {
     		if (JarmoMain.feherbogyo[t]!=null && JarmoMain.feherbogyo[t].mezo.id<30) return true;
       		if (JarmoMain.feketebogyo[t]!=null && JarmoMain.feketebogyo[t].mezo.id<30) return true;
       	}
    	
    	return false;
    }
    
    public static void initGame (Context context, Bundle savedInstanceState) {
    	
      JarmoService.JarmoLog  ("initgame");	
      if (savedInstanceState!=null && savedInstanceState.getInt("feher1")!=0) {
    	  JarmoService.JarmoLog  ("van saved bundle, feher1="+savedInstanceState.getInt("feher1"));
    	  if (JarmoMain.mezok[savedInstanceState.getInt("feher1")]!=null) JarmoService.JarmoLog  ("mezo1 van");
    	  if (mezok[1]!=null) JarmoService.JarmoLog  ("mezo11 van");
    	JarmoMain.feherbogyo[1].mezo=JarmoMain.mezok[savedInstanceState.getInt("feher1")];
    	JarmoMain.feherbogyo[2].mezo=JarmoMain.mezok[savedInstanceState.getInt("feher2")];
    	JarmoMain.feherbogyo[3].mezo=JarmoMain.mezok[savedInstanceState.getInt("feher3")];
    	JarmoMain.feherbogyo[4].mezo=JarmoMain.mezok[savedInstanceState.getInt("feher4")];
    	JarmoMain.feherbogyo[5].mezo=JarmoMain.mezok[savedInstanceState.getInt("feher5")];
    	
    	JarmoMain.feketebogyo[1].mezo=JarmoMain.mezok[savedInstanceState.getInt("fekete1")];
    	JarmoMain.feketebogyo[2].mezo=JarmoMain.mezok[savedInstanceState.getInt("fekete2")];
    	JarmoMain.feketebogyo[3].mezo=JarmoMain.mezok[savedInstanceState.getInt("fekete3")];
    	JarmoMain.feketebogyo[4].mezo=JarmoMain.mezok[savedInstanceState.getInt("fekete4")];
    	JarmoMain.feketebogyo[5].mezo=JarmoMain.mezok[savedInstanceState.getInt("fekete5")];
 
      	JarmoMain.feherbogyo[1].allapot=savedInstanceState.getInt("feher1a");
    	JarmoMain.feherbogyo[2].allapot=savedInstanceState.getInt("feher2a");
    	JarmoMain.feherbogyo[3].allapot=savedInstanceState.getInt("feher3a");
    	JarmoMain.feherbogyo[4].allapot=savedInstanceState.getInt("feher4a");
    	JarmoMain.feherbogyo[5].allapot=savedInstanceState.getInt("feher5a");
    	
    	JarmoMain.feketebogyo[1].allapot=savedInstanceState.getInt("fekete1a");
    	JarmoMain.feketebogyo[2].allapot=savedInstanceState.getInt("fekete2a");
    	JarmoMain.feketebogyo[3].allapot=savedInstanceState.getInt("fekete3a");
    	JarmoMain.feketebogyo[4].allapot=savedInstanceState.getInt("fekete4a");
    	JarmoMain.feketebogyo[5].allapot=savedInstanceState.getInt("fekete5a");
    	
    	JarmoService.JarmoLog  ("pontok kinyer fekete"+savedInstanceState.getInt("pointFekete"));
    	JarmoMain.pointAndroid=savedInstanceState.getInt("pointAndroid");
    	JarmoMain.pointUser=savedInstanceState.getInt("pointUser");
    	JarmoMain.pointUser2=savedInstanceState.getInt("pointUser2");
    	JarmoMain.androidHelyettLepUser=savedInstanceState.getInt("androidHelyett");
   	
    	savedInstanceState.remove("fekete1");
    	savedInstanceState.remove("fekete2");
    	savedInstanceState.remove("fekete3");
    	savedInstanceState.remove("fekete4");
    	savedInstanceState.remove("fekete5");
   
    	savedInstanceState.remove("feher1");
      	savedInstanceState.remove("feher2");
      	savedInstanceState.remove("feher3");
      	savedInstanceState.remove("feher4");
      	savedInstanceState.remove("feher5");
      	
       	savedInstanceState.remove("fekete1a");
    	savedInstanceState.remove("fekete2a");
    	savedInstanceState.remove("fekete3a");
    	savedInstanceState.remove("fekete4a");
    	savedInstanceState.remove("fekete5a");
   
    	savedInstanceState.remove("feher1a");
      	savedInstanceState.remove("feher2a");
      	savedInstanceState.remove("feher3a");
      	savedInstanceState.remove("feher4a");
      	savedInstanceState.remove("feher5a");
 
      	savedInstanceState.remove("pointAndroid");
      	savedInstanceState.remove("pointUser");
      	savedInstanceState.remove("pointUser2");
      	savedInstanceState.remove("androidHelyett");
      	
      } else {
    	  JarmoService.JarmoLog  ("nincs savedBundle -> ujra kell osztani");
    	  int feherlent=0;
    	  if (JarmoMain.userSzine==0) {
    		  if (JarmoMain.userLent==0) feherlent=0; else feherlent=1;
    	  } else { //user with fekete
       		  if (JarmoMain.userLent==0) feherlent=1; else feherlent=0;   		  
    	  }
    	  
    	  if (feherlent==0) {
    		  JarmoMain.feherbogyo[1].mezo=JarmoMain.mezok[5];
    		  JarmoMain.feherbogyo[2].mezo=JarmoMain.mezok[4];
    		  JarmoMain.feherbogyo[3].mezo=JarmoMain.mezok[3];
    		  JarmoMain.feherbogyo[4].mezo=JarmoMain.mezok[2];
    		  JarmoMain.feherbogyo[5].mezo=JarmoMain.mezok[1];
    	
    		  JarmoMain.feketebogyo[1].mezo=JarmoMain.mezok[21];
    		  JarmoMain.feketebogyo[2].mezo=JarmoMain.mezok[22];
    		  JarmoMain.feketebogyo[3].mezo=JarmoMain.mezok[23];
    		  JarmoMain.feketebogyo[4].mezo=JarmoMain.mezok[24];
    		  JarmoMain.feketebogyo[5].mezo=JarmoMain.mezok[25];
    	  } else {
      		  JarmoMain.feketebogyo[1].mezo=JarmoMain.mezok[5];
    		  JarmoMain.feketebogyo[2].mezo=JarmoMain.mezok[4];
    		  JarmoMain.feketebogyo[3].mezo=JarmoMain.mezok[3];
    		  JarmoMain.feketebogyo[4].mezo=JarmoMain.mezok[2];
    		  JarmoMain.feketebogyo[5].mezo=JarmoMain.mezok[1];
    	
    		  JarmoMain.feherbogyo[1].mezo=JarmoMain.mezok[21];
    		  JarmoMain.feherbogyo[2].mezo=JarmoMain.mezok[22];
    		  JarmoMain.feherbogyo[3].mezo=JarmoMain.mezok[23];
    		  JarmoMain.feherbogyo[4].mezo=JarmoMain.mezok[24];
    		  JarmoMain.feherbogyo[5].mezo=JarmoMain.mezok[25];  		  
    	  }

          JarmoMain.androidHelyettLepUser=0;
     
      }
    }
    
    public static void bogyoRedraw (Context context, int isInit) {
    	int t;
    	for (t=1;t<=5;t++) {
    		JarmoService.JarmoLog  ("feher BOGYO redraw id="+feherbogyo[t].mezo.id);
   // 		feherbogyo[t].allapot=0;
    		feherbogyo[t].invalidate();
    		if (isInit==0) {
    		   feherbogyo[t].px=feherbogyo[t].mezo.px;
    		   feherbogyo[t].py=feherbogyo[t].mezo.py;
    		} else { // be kell usztatni
    			final int start_x=feherbogyo[t].px;
    			final int start_y=feherbogyo[t].py;
    			final int k=t;
    			JarmoMain.handler.postDelayed(new Runnable () {
  //  				int target_x=JarmoMain.feherbogyo[k].mezo.px;
  //  				int target_y=JarmoMain.feherbogyo[k].mezo.py;
    				Mezo targetMezo = JarmoMain.feherbogyo[k].mezo;
    				
    				public void run () {
    					JarmoMain.feherbogyo[k].px=start_x;
    					JarmoMain.feherbogyo[k].py=start_y;
    					JarmoMain.feherbogyo[k].lathato=1;
    					JarmoMain.feherbogyo[k].invalidate();
    					JarmoMain.feherbogyo[k].slideToPos (targetMezo, 60, 10, null); //suritve
    					}  					
    			}, (t*200));
       		}
    		
  //  		feketebogyo[t].allapot=0;
    		feketebogyo[t].invalidate();
    		if (isInit==0) {
    		   feketebogyo[t].px=feketebogyo[t].mezo.px;
    		   feketebogyo[t].py=feketebogyo[t].mezo.py;
    		} else { // be kell usztatni
    			final int start_x=feketebogyo[t].px;
    			final int start_y=feketebogyo[t].py;
    			final int k=t;
    			JarmoMain.handler.postDelayed(new Runnable () {
    				int target_x=JarmoMain.feketebogyo[k].mezo.px;
    				int target_y=JarmoMain.feketebogyo[k].mezo.py;
    				Mezo targetMezo = JarmoMain.feketebogyo[k].mezo;
    				
    				public void run () {
    					JarmoMain.feketebogyo[k].px=start_x;
    					JarmoMain.feketebogyo[k].py=start_y;
    					JarmoMain.feketebogyo[k].lathato=1;
    					JarmoMain.feketebogyo[k].invalidate();
    					JarmoMain.feketebogyo[k].slideToPos (targetMezo, 60, 10, null); //suritve
    					}  					
    			}, 1000+(t*200));
       		}

    	}
    }
    
    public static void redrawMezok (Mezo startlepes) {
        int t;
    	if (startlepes==null) {
    		for (t=1;t<=25;t++) {
    		   mezok[t].isActive=1;
    		   mezok[t].invalidate();
    		}
    	} else {
    		for (t=1;t<=25;t++) mezok[t].isActive=0;
    		 
    		startlepes.isActive=1;
    		int i=1;
    		while (startlepes.szomszedok[i]!=null) {
    			   startlepes.szomszedok[i].isActive=1;
    			   i++;
    		}
    		for (t=1;t<=25;t++) mezok[t].invalidate();
     		    		
    	}
    	
    }
    
    public static void redrawBogyok () {
    	for (int t=1;t<5;t++) {
    		JarmoMain.feherbogyo[t].posToMezo();
    		JarmoMain.feketebogyo[t].posToMezo();
    	}
    }
    public static void tablarajz (Context context) {
    	int sor, osz;
    	int MAXSOR=5;
    	int MAXOSZ=5;
    	int id=0, sz=0, kot;
    	   
    	for (int p=1; p<=5; p++) {
    		int margin = JarmoMain.RADIUS*2;

    		int cx, cy;
    		if (field1.width() > field1.height()) {
    		   cx = JarmoMain.field1.left + margin +
    				   (int)(JarmoMain.BOGYORADIUS*(p-1)*2.5);
    		   cy = JarmoMain.field1.top+JarmoMain.field1.height()/2;  
    	    } else {	
    	       cx = JarmoMain.field1.left+JarmoMain.field1.width()/2;	
    	       cy = JarmoMain.field1.top + margin +
    	    		   (int)(JarmoMain.BOGYORADIUS*(p-1)*2.5);
    	    }
    		JarmoMain.mezok[30+p] = new Mezo (context, 30+p, cx, cy);
    		JarmoMain.mezok[30+p].lathato=1;
    		JarmoMain.mezok[30+p].isActive=0;
    		
    		JarmoService.JarmoLog  ("field 1 cy"+cy);
 
       		int cx1, cy1;
       	 
    		if (JarmoMain.field2.width() > JarmoMain.field2.height()) {
     		   cx1 = JarmoMain.field2.right - margin -
     				  (int)(JarmoMain.BOGYORADIUS*(p-1)*2.5);
     		   cy1 = JarmoMain.field2.top+JarmoMain.field2.height()/2;  
     	    } else {	
     	       cx1 = JarmoMain.field2.left+JarmoMain.field2.width()/2;	
     	       cy1 = JarmoMain.field2.bottom - margin -
     	    		  (int)(JarmoMain.BOGYORADIUS*(p-1)*2.5);
     	    }
    		JarmoService.JarmoLog  ("field 2 cy"+cy1);

    		JarmoMain.mezok[40+p] = new Mezo (context, 40+p, cx1, cy1);
    		JarmoMain.mezok[40+p].lathato=1;
    		JarmoMain.mezok[40+p].isActive=0;
    		
    	}
    
    	
    	   for (sor=0;sor<MAXSOR;sor++) {
    		   for (osz=0; osz<MAXOSZ; osz++) {
    			   id++;
    			   int margin = JarmoMain.RADIUS*2;
    			   int cx=JarmoMain.mainfield.left+margin+
    					  (((JarmoMain.mainfield.width() -(2*margin))/(MAXOSZ-1))*(osz));
    	           int cy=JarmoMain.mainfield.top+margin+
    	        		  (((JarmoMain.mainfield.height()-(2*margin))/(MAXSOR-1))*(sor));
      	         		   
    	       //    JarmoService.JarmoLog  ("Sor: "+id+ " cx"+cx+"cy"+cy);
    	    	   mezok[id] = new Mezo (context, id, cx, cy);
    	    	   mezok[id].isCrossed=0;
    	    	//   mezok[id].setVisibility(View.INVISIBLE);
    		   }
    	   }
    	   
    	     
    		mezok [1].szomszedok[1]=mezok [8];
    		mezok [1].szomszedok[2]=mezok [12];
       		mezok [2].szomszedok[1]=mezok [9];
    		mezok [2].szomszedok[2]=mezok [11];
    		mezok [3].szomszedok[1]=mezok [10];
    		mezok [3].szomszedok[2]=mezok [12];
    		mezok [3].szomszedok[3]=mezok [6];
    		mezok [4].szomszedok[1]=mezok [15];
    		mezok [4].szomszedok[2]=mezok [7];
    		mezok [5].szomszedok[1]=mezok [8];
    		mezok [5].szomszedok[2]=mezok [14];
    		
     		mezok [6].szomszedok[1]=mezok [3];
    		mezok [6].szomszedok[2]=mezok [13];
       		mezok [7].szomszedok[1]=mezok [4];
       		mezok [7].szomszedok[2]=mezok [8];      	    
       		mezok [7].szomszedok[3]=mezok [12];
      		mezok [7].szomszedok[4]=mezok [14];
       		mezok [8].szomszedok[1]=mezok [1];
    		mezok [8].szomszedok[2]=mezok [5];
       		mezok [8].szomszedok[3]=mezok [7];
       		mezok [8].szomszedok[4]=mezok [9];
      		mezok [8].szomszedok[5]=mezok [17];
      		mezok [9].szomszedok[1]=mezok [2];
    		mezok [9].szomszedok[2]=mezok [14];
       		mezok [9].szomszedok[3]=mezok [8];
       		mezok [9].szomszedok[4]=mezok [20];
    		mezok [10].szomszedok[1]=mezok [3];
    		mezok [10].szomszedok[2]=mezok [13];
    	
     		mezok [11].szomszedok[1]=mezok [2];
    		mezok [11].szomszedok[2]=mezok [22];
       		mezok [12].szomszedok[1]=mezok [1];
    		mezok [12].szomszedok[2]=mezok [7];
       		mezok [12].szomszedok[3]=mezok [3];
    		mezok [12].szomszedok[4]=mezok [21];
       		mezok [12].szomszedok[5]=mezok [17];
    		mezok [12].szomszedok[6]=mezok [19];
       		mezok [13].szomszedok[1]=mezok [6];
    		mezok [13].szomszedok[2]=mezok [10];
       		mezok [13].szomszedok[3]=mezok [16];
    		mezok [13].szomszedok[4]=mezok [24];
     		mezok [14].szomszedok[1]=mezok [7];
    		mezok [14].szomszedok[2]=mezok [9];
       		mezok [14].szomszedok[3]=mezok [5];
    		mezok [14].szomszedok[4]=mezok [23];
       		mezok [14].szomszedok[5]=mezok [19];
    		mezok [14].szomszedok[6]=mezok [25];
    		mezok [15].szomszedok[1]=mezok [18];
      		mezok [15].szomszedok[2]=mezok [4];
      	    		
      		mezok [16].szomszedok[1]=mezok [13];
    		mezok [16].szomszedok[2]=mezok [23];
       		mezok [17].szomszedok[1]=mezok [12];
    		mezok [17].szomszedok[2]=mezok [8];
       		mezok [17].szomszedok[3]=mezok [18];
    		mezok [17].szomszedok[4]=mezok [24];
    		mezok [18].szomszedok[1]=mezok [21];
    		mezok [18].szomszedok[2]=mezok [17];
      		mezok [18].szomszedok[3]=mezok [15];
    		mezok [18].szomszedok[4]=mezok [19];
      		mezok [18].szomszedok[5]=mezok [25];
    		mezok [19].szomszedok[1]=mezok [22];
    		mezok [19].szomszedok[2]=mezok [18];
      		mezok [19].szomszedok[3]=mezok [12];
    		mezok [19].szomszedok[4]=mezok [14];
    		mezok [20].szomszedok[1]=mezok [23];
    		mezok [20].szomszedok[2]=mezok [9];
    		
    		mezok [21].szomszedok[1]=mezok [12];
    		mezok [21].szomszedok[2]=mezok [18];
       		mezok [22].szomszedok[1]=mezok [11];
    		mezok [22].szomszedok[2]=mezok [19];
    		
    		mezok [23].szomszedok[1]=mezok [16];
    		mezok [23].szomszedok[2]=mezok [14];
    		mezok [23].szomszedok[3]=mezok [20];
        		
    		mezok [24].szomszedok[1]=mezok [17];
    		mezok [24].szomszedok[2]=mezok [13];
    		mezok [25].szomszedok[1]=mezok [18];
    		mezok [25].szomszedok[2]=mezok [14];
       		
   
    		
       	    		
    		
    		kot=1;
    		for (id=1; id<25; id++) {
    			sz=1;
    			while (mezok [id].szomszedok[sz]!=null) {
    			    osszekot[kot]=new Osszekot (context, mezok[id], mezok[id].szomszedok[sz]);
    				kot++;
    			    sz++;
    			}
    		}
    	   
    }

     
    public static Bogyo bogyoItt (Mezo mezo) {
        JarmoService.JarmoLog  ("bogyoitt start for mezo"+mezo.id);
       	for (int t=1;t<=5;t++) {
  //  	  JarmoService.JarmoLog  (t+"feher on mezo"+JarmoMain.feherbogyo[t].mezo.id);	
  //  	  JarmoService.JarmoLog  (t+"fek on mezo"+JarmoMain.feketebogyo[t].mezo.id);
    	  
    	  if (JarmoMain.feherbogyo[t].mezo == mezo) return JarmoMain.feherbogyo[t];
    	  if (JarmoMain.feketebogyo[t].mezo == mezo) return JarmoMain.feketebogyo[t];
    	}
    	JarmoService.JarmoLog  (mezo.id+" itt nincs bogyo");
    	return null;
    }
    
    public static boolean bogyoPalyanVan (Bogyo bogyo) {
    	for (int t=1; t<=5;t++) {
    	  if (bogyo.mezo==JarmoMain.mezok[30+t]) return true;
       	  if (bogyo.mezo==JarmoMain.mezok[40+t]) return true;
    	}
    	return false;
    }
    
    public static int howManyParkolt (int szin) { //0=fent, 1=lent
    	int count=0;
    	for (int t=1;t<=5;t++) {
    		if (szin==0) {
    			if (JarmoMain.bogyoItt(JarmoMain.mezok[30+t])!=null) count++;
    		} else {
    			if (JarmoMain.bogyoItt(JarmoMain.mezok[40+t])!=null) count++;
    		}
    	} //for
    	JarmoService.JarmoLog  ("Parkolt bogyok="+count);
    	return count;
    }
    
 /*   public static void engedelyezMozgHaMindenAll () {
    	
    	for (int t=1;t<=5;t++) {
    		if (JarmoMain.feherbogyo[t].mozoge==1 ||
    		    JarmoMain.feketebogyo[t].mozoge==1) {
    			    JarmoMain.handler.postDelayed(new Runnable () {
    			    	public void run () {
    			    		JarmoService.JarmoLog("Meg mozgott valami, ujra check engedelyezMozg-hoz");
    			    		JarmoMain.engedelyezMozgHaMindenAll();
    			    	}
    			    }
    			    , 100);
    			    return;
    		} //if
    	} //for
    	engedelyezMozg (1);
    }
   */ 
    public static void engedelyezMozg (int erintheto) {
       	Bogyo userbogyo[]=new Bogyo [7];
       	Bogyo androidbogyo[]=new Bogyo [7];
    	int usercelmin, usercelmax;
    	int userstartmin, userstartmax;
    	int androidcelmin, androidcelmax;
    	int androidstartmin, androidstartmax;
    	
    	for (int t=1;t<=5;t++) 
    		JarmoService.JarmoLog("feher# "+ t +" isParkolt="+feherbogyo[t].isParkolt );
    	for (int t=1;t<=5;t++) 
    		JarmoService.JarmoLog("fekete# "+ t +" isParkolt="+feketebogyo[t].isParkolt );
    	
    	//ha meg mozog valami, hivjuk ujra a fv-t kesobb
      	for (int t=1;t<=5;t++) {
    		if (JarmoMain.feherbogyo[t].mozoge==1 ||
    		    JarmoMain.feketebogyo[t].mozoge==1) {
    			    final int eerintheto=erintheto;
    			    JarmoMain.handler.postDelayed(new Runnable () {
    			    	public void run () {
    			    		JarmoService.JarmoLog("Meg mozgott valami, ujra hiv engedelyezMozg-t");
    			    		JarmoMain.engedelyezMozg(eerintheto);
    			    	}
    			    }
    			    , 100);
    			    return;
    		} //if
    	} //for
    	
      	//timer
    //	if (erintheto==1) {
    //		JarmoMain.startTimer(JarmoMain.userLent);
    //	} else {
    //		JarmoMain.stopTimer(JarmoMain.userLent);
    //	}
    	
  		JarmoMain.alap.nyilFentStopFlash();
		JarmoMain.alap.nyilLentStopFlash();
     	if (JarmoMain.userLent==0) {
    		if (erintheto==1) JarmoMain.alap.nyilFentStartFlash();
    		if (erintheto==2) JarmoMain.alap.nyilLentStartFlash();
    	} else {
       		if (erintheto==1) JarmoMain.alap.nyilLentStartFlash();       		
       		if (erintheto==2) JarmoMain.alap.nyilFentStartFlash();
    	}
     	if (JarmoMain.jatekMode==6)
     		if (erintheto==1) JarmoMain.startTimer(0);
     		else JarmoMain.stopTimer();
    	
   		for (int t=1;t<=5;t++) {
   	    	if (JarmoMain.userSzine==0) {
    			userbogyo[t]=JarmoMain.feherbogyo[t];
    			androidbogyo[t]=JarmoMain.feketebogyo[t];
   	    	} else {
    			userbogyo[t]=JarmoMain.feketebogyo[t];   	    		
    			androidbogyo[t]=JarmoMain.feherbogyo[t];
       	    }
    	}
   		if (JarmoMain.userLent==1) { 
   			userstartmin=21; userstartmax=25;
   			usercelmin=1; usercelmax=5;
   			androidstartmin=1; androidstartmax=5;
   			androidcelmin=21; androidcelmax=25;
   			
   		} else {
   			userstartmin=1; userstartmax=5;
   			usercelmin=21; usercelmax=25;
   			androidstartmin=21; androidstartmax=25;
   			androidcelmin=1; androidcelmax=5;
   		}
   		
   		for (int t=1;t<=5;t++) {
    		  androidbogyo[t].setErintheto(0);
    		  userbogyo[t].setErintheto(0);
   		}
   	    for (int t=1;t<=5;t++) {
   	    	JarmoService.JarmoLog  ("engedelyezMozg, mezoid ="+userbogyo[t].mezo.id);
   	    	if (erintheto==1) {
    		  if (userbogyo[t].mezo.id <30) userbogyo[t].setErintheto(1);
    		  if (userbogyo[t].mezo.id >=usercelmin && userbogyo[t].mezo.id <=usercelmax) 
    			  userbogyo[t].setErintheto(0); //celbaert
   	        }
   	    //	JarmoService.JarmoLog  ("Erinthetosege "+userbogyo[t].erintheto);
   	    	
  	    	JarmoService.JarmoLog  ("engedelyezMozg, mezoid ="+androidbogyo[t].mezo.id);
   	    	if (erintheto==2) { //android helyett ellenfel lep
    		  if (androidbogyo[t].mezo.id <30) androidbogyo[t].setErintheto(1);
    		  if (androidbogyo[t].mezo.id >=androidcelmin && androidbogyo[t].mezo.id <=androidcelmax) 
    			  androidbogyo[t].setErintheto(0); //celbaert
   	        }
    	}
        
    }
    
    public static void parkbanOsszecsuztat (Mezo ittvolt, int szin) { //szin: 0=fent, 1=lent
    	
    	JarmoService.JarmoLog  ("Osszecsusztat, ittvolt = "+ittvolt.id+ "szin "+szin);
    	
    	if (ittvolt.id<30) return; //vedelem
    	
    	if (szin==0) {
    		JarmoService.JarmoLog  ("Fenti osszecsusztat");
    		for (int t=ittvolt.id+1; t<=(31+JarmoMain.howManyParkolt(szin)); t++) {
     			JarmoService.JarmoLog  ("ez csuszik fent "+t);
       			if (JarmoMain.bogyoItt(JarmoMain.mezok[t]) == null) continue;
    			JarmoMain.bogyoItt(JarmoMain.mezok[t]).slideToPos (JarmoMain.mezok[t-1], 20, 5, null); //suritve 
 //   			JarmoMain.bogyoItt(JarmoMain.mezok[t]).mezo=JarmoMain.mezok[t-1];
    		}
    	}
    	if (szin==1) {
    		JarmoService.JarmoLog  ("Lenti osszecsusztat");
    		for (int t=ittvolt.id+1; t<=(41+JarmoMain.howManyParkolt(szin)); t++) {
    			JarmoService.JarmoLog  ("ez csuszik lent "+t);
    			if (JarmoMain.bogyoItt(JarmoMain.mezok[t]) == null) continue;
    			JarmoMain.bogyoItt(JarmoMain.mezok[t]).slideToPos (JarmoMain.mezok[t-1], 20, 5, null); 
//    			JarmoMain.bogyoItt(JarmoMain.mezok[t]).mezo=JarmoMain.mezok[t-1];
    		}
    	}

    }
    
    public static void bogyoLeut (Bogyo b) { //melyik bogyo, melyik bogyot?
    	int leutottIdejut; //0=fel, 1=le
    	
    	//if (overSlideTime>-1) return;
    	
    	if (JarmoMain.userSzine==b.szin) { //usert utottek le
    		JarmoService.JarmoLog  ("Usert utottek le");
    		if (JarmoMain.userLent==1) leutottIdejut=1; else leutottIdejut=0;  		
    	} else { //androidot utottek le
    		JarmoService.JarmoLog  ("Androidot utottek le");
    		if (JarmoMain.userLent==1) leutottIdejut=0; else leutottIdejut=1;
    	}
 //   	int szin;
    	Mezo leutottMezo = b.mezo;
    	int pos = JarmoMain.howManyParkolt(leutottIdejut)+1;
    	if (leutottIdejut==0) //fel
    	   b.mezo=JarmoMain.mezok[30+pos];
    	else //le
    	   b.mezo=JarmoMain.mezok[40+pos];
       	JarmoService.JarmoLog  (pos+" "+b.mezo.id);
       	b.allapot=0;
       	final Bogyo bb=b;
       	
        JarmoMain.handler.postDelayed(new Runnable () {//parkbacsusz
        	public void run () {
    	        bb.slideToPos(bb.mezo, 60, 10, null);//suritve
        	}
        }, 200);
        
		if (JarmoMain.userMegMozoghat ()==false) {
		       //jatek vege
			   JarmoMain.endGame ();
		//	   return true;
		} 

    }
  /*  
    public static void utkozesCheck (Bogyo a, Bogyo b) { //ezzel (mozog), ezt (celban all)
    	
    	final Bogyo aa=a, bb=b;
    	
    	JarmoMain.handler.postDelayed(new Runnable () {
    		public void run () {
    			//test utkozes
    			int radius = Math.round (JarmoMain.BOGYORADIUS)+aa.strokewidth;
    			int xdist=Math.abs (aa.px-bb.px);
    			int ydist=Math.abs (aa.py-bb.py);
    			int dist=(int) (Math.sqrt ( (double)((xdist*xdist)+(ydist*ydist))));
    			
    			JarmoService.JarmoLog  ("aa.px="+aa.px+ "bb.px="+bb.px+" xdist="+xdist+" ydist"+ydist+ " Dist = "+dist+" radius="+radius);
    			
    			if (dist>=radius) JarmoMain.utkozesCheck(aa,bb);
    			else {
    			  JarmoService.JarmoLog  ("Bingooooooooooooooooooooooooooo");
    			  JarmoMain.utkozes(aa, bb);
    			}
    	    } //run
        }, 10);
    }
    */
    public static void utkozes (Bogyo a, Bogyo b) {//a mozog, b celban all
	    final Bogyo aa=a, bb=b;
 
		final int radius = Math.round (JarmoMain.BOGYORADIUS)+aa.strokewidth;
		final int xdist=bb.px-aa.px; //negativ, ha visszafele mozog
		final int ydist=bb.py-aa.py; //negativ, ha felfele mozog
	//	final int dist=(int) (Math.sqrt ( (double)((xdist*xdist)+(ydist*ydist))));

		final int visszapattanXUnit=(int)((xdist*1.7)/15);
		final int visszapattanYUnit=(int)((ydist*1.7)/15);
		final int px=b.px; // eltaroljuk, mert b bogyo is mozogni fog
		final int py=b.py;
		
		b.isParkolt=true;
		
		JarmoSetting.playSound(R.raw.poolpot);
        //visszapattan
		for (int t=1;t<=15;t++) {
			final int tt=t;
    	   JarmoMain.handler.postDelayed(new Runnable () {
    		  public void run () {
    			JarmoService.JarmoLog  ("visszapattan "+tt);  
    		    aa.px=px-visszapattanXUnit*tt;
    		    aa.py=py-visszapattanYUnit*tt;
    		    aa.invalidate();  			
    		    
    		    bb.px=px+visszapattanXUnit*tt;
       		    bb.py=py+visszapattanYUnit*tt;
       		    bb.invalidate();  			
  		   
       		  }
    	   }, t*6);
		} //for
		
        //visszacsuszik
		int visszacsusz_delay=200;
		final int visszacsuszXUnit=(int)((px-aa.px)/15); //cel: az eredeti hely (px/py)
		final int visszacsuszYUnit=(int)((py-aa.py)/15);
		for (int t=1;t<=15;t++) {
			final int tt=t;
    	   JarmoMain.handler.postDelayed(new Runnable () {
    		  public void run () {
    		    aa.px=px-(visszapattanXUnit*(15-tt));
    		    aa.py=py-(visszapattanYUnit*(15-tt));
    		    aa.invalidate();  			
    		    
      		  }
    	   }, visszacsusz_delay+(t*20));
		} //for
		
		bogyoLeut (b);

    }
    
    public static String timeToString (int time)
    {
    	return Integer.toString((int)Math.floor(time/10)) + "." + Integer.toString(time % 10);
    }
    
    public static void behozHaAndroidCelbaert (Mezo idelepett, int bogyoallapot) {
    	final Bogyo androidbogyo[]=new Bogyo [7];
    	final int andcelmin, andcelmax;
    	final int andstartmin, andstartmax;
    	final int androidPark, androidParkStart; 
    	
    	JarmoService.JarmoLog  (">>> Behoz, ha android celbaert start");
    	
   		for (int t=1;t<=5;t++) {
   	    	if (JarmoMain.userSzine==0) {
    			androidbogyo[t]=JarmoMain.feketebogyo[t];
   	    	} else {
    			androidbogyo[t]=JarmoMain.feherbogyo[t];   	    		
    		}
    	}
   		if (JarmoMain.userLent==1) {
   			androidPark=0;
   			androidParkStart=31;
   			andstartmin=1; andstartmax=5;
   			andcelmin=21; andcelmax=25;
   		} else { //user fent
   			androidPark=1;
   			androidParkStart=41;
   			andstartmin=21; andstartmax=25;
   			andcelmin=1; andcelmax=5;
   		}

   		JarmoService.JarmoLog  ("andcel "+andcelmin+" "+andcelmax);
   		if (idelepett.id>=andcelmin && idelepett.id <=andcelmax && bogyoallapot==1) {
   			JarmoService.JarmoLog  ("kijelolt androidbogyo celbaert, android visszarakhat ha tud "+idelepett.id);
   			JarmoMain.handler.postDelayed(new Runnable () {

   				public void run() {
   					int szabadcount=0;
   					int voltCelbaeres;
   					for (int t=andstartmin; t<=andstartmax; t++) 
   						if (JarmoMain.bogyoItt (JarmoMain.mezok[t]) ==null) szabadcount++;

   					if (szabadcount==0) {
   						JarmoService.JarmoLog  ("Android nem tud hova visszalepni");
   						checkAndroidGameEnd ();
   						return;
   					}

   					int maxparkolt = howManyParkolt (androidPark);
   					if (maxparkolt==0) {
   						JarmoService.JarmoLog  ("nincs parkolt android bogyo");
   						checkAndroidGameEnd ();
   						return;
   					}

   					JarmoService.JarmoLog  ("Android felert, van is parkolt:"+maxparkolt);

   					//villogas indit es leallit
   					for (int t=1;t<=5;t++) {
   						if (androidbogyo[t].mezo.id>30) androidbogyo[t].startFlash();  	  
   					}     	    
   					JarmoMain.handler.postDelayed(new Runnable () {
   						public void run () {
   							for (int t=1;t<=5;t++) {
   								androidbogyo[t].stopFlash();  	  
   							}
   						}
   					}, 2000);

   					int ezzellepjunkbe;
   					int retrycount=0;
   					while (true) {
   						ezzellepjunkbe = androidParkStart+(int)(Math.random()*(maxparkolt));
   						// test for problems? Elvileg nem lehetne null egyik sem, de egy ANR miatt kell ezt csinalni
   						retrycount++;
   						if (retrycount==100) {
   							//valami nagy baj van -> inkabb zarjuk le a jatekot
   							JarmoMain.endGame ();
   							return;
   						}
   						if (JarmoMain.mezok[ezzellepjunkbe]==null)  continue; 
   						if (JarmoMain.bogyoItt(JarmoMain.mezok[ezzellepjunkbe])==null)  continue; 
   						break;
   					}
   					int idelepjunkbe=1;
   					while (true) {
   						idelepjunkbe=andstartmin+(int)(Math.random()*5);
   						if (JarmoMain.bogyoItt(JarmoMain.mezok[idelepjunkbe])==null) break;
   					}
   					JarmoService.JarmoLog  ("Android belep innen "+ezzellepjunkbe+" ide"+JarmoMain.mezok[idelepjunkbe].id);
   					JarmoMain.bogyoItt(JarmoMain.mezok[ezzellepjunkbe]).isParkolt=false;   		   			
   					JarmoMain.bogyoItt(JarmoMain.mezok[ezzellepjunkbe]).slideToPos(
   							JarmoMain.mezok[idelepjunkbe], 40, 10, null);//suritve
   					//    		JarmoMain.bogyoItt(JarmoMain.mezok[ezzellepjunkbe]).mezo=JarmoMain.mezok[idelepjunkbe];
   					JarmoMain.parkbanOsszecsuztat(JarmoMain.mezok[ezzellepjunkbe], androidPark);
   					checkAndroidGameEnd ();

   				} //run
   			}, 1500);
   			return;  
   		} else {
   			checkAndroidGameEnd ();
   			return; 
   		}
    }

    public static void checkAndroidGameEnd () {
    	   JarmoService.JarmoLog("checking game end after android move");
 	       if (JarmoMain.userMegMozoghat ()) {
		    	   JarmoMain.engedelyezMozg(1); 
		    	   return;
		   } else {
			   //jatek vege
			   JarmoMain.endGame ();
			   return;
		   }
    }
    
    public static void makeAndroidMove () {
    	Bogyo androidbogyo[]=new Bogyo [7];
    	int andcelmin, andcelmax;
    	int andstartmin, andstartmax;
    	
    	
  //  	   String[] txt= { "Hello", "leo", "leoooo"};
  //  	   showHelp (txt, 0, 0);
    		   
   		for (int t=1;t<=5;t++) {
   	    	if (JarmoMain.userSzine==0) {
    			androidbogyo[t]=JarmoMain.feketebogyo[t];
   	    	} else {
    			androidbogyo[t]=JarmoMain.feherbogyo[t];   	    		
    		}
    	}
   		if (JarmoMain.userLent==0) { 
   			andstartmin=21; andstartmax=25;
   			andcelmin=1; andcelmax=5;
   		} else {
   			andstartmin=1; andstartmax=5;
   			andcelmin=21; andcelmax=25;
   		}
   		
    	JarmoService.JarmoLog  ("android move");
    	       		
    		for (int t=1;t<=5;t++) { //van -e leutheto babu?
    			JarmoService.JarmoLog  ("Leutheto bogyoja van?"+t);
    			int szcount=1;
   			    if (androidbogyo[t].mezo.id>=andcelmin && 
   			        androidbogyo[t].mezo.id<=andcelmax) continue; //mar celbaert, keressunk mast
   			    
   			    //gyengites strength=1 eseten: ne mindig ussuk le, csak 5-bol egyszer
   			    JarmoService.JarmoLog  ("Android strength lepesnel="+JarmoMain.strength+"user szine "+JarmoMain.userSzine);
   			    if (JarmoMain.strength==0) {
   				  int b = 1+(int)(Math.random()*5);
   	    		  if (b!=3) continue; 
   			    }

    			while (true) {
    				JarmoService.JarmoLog  ("Szomszed leutheto count"+szcount);
        				
    			   if (androidbogyo[t].mezo.szomszedok[szcount]==null) break;
    			   Mezo ujm = androidbogyo[t].mezo.szomszedok[szcount];
    			   if (JarmoMain.bogyoItt(ujm)!=null) JarmoService.JarmoLog  (szcount+". szomszedban ("+ujm.id+") van egy szin="+JarmoMain.bogyoItt(ujm).szin);
    			   if (JarmoMain.bogyoItt(ujm)!=null && JarmoMain.bogyoItt(ujm).szin==JarmoMain.userSzine) {//user szine? ussuk le
                       JarmoService.JarmoLog  ("Leut. Az uj helyen levo szin "+JarmoMain.bogyoItt(ujm).szin);   				  
    		//		   bogyoLeut (androidbogyo[t], JarmoMain.bogyoItt(ujm), 500); //regi el
    			       Mezo regimezo=androidbogyo[t].mezo;
    			       JarmoMain.crossAField(regimezo);
    				   androidbogyo[t].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    		//		   androidbogyo[t].mezo=ujm;   //uj ide mozg
    				   
    	    		   androidbogyo[t].mezoHist.add(ujm);
			           if (androidbogyo[t].mezoHist.size()>8) androidbogyo[t].mezoHist.remove(androidbogyo[t].mezoHist.firstElement());

    				   androidbogyo[t].allapot=1; //kivalasztott szin (X)
    			       JarmoMain.behozHaAndroidCelbaert (ujm, androidbogyo[t].allapot);
    			       return;
    			   }
    			   szcount++;
    			} //while szomszed
    		} // for
    		
    		for (int t=1;t<=5;t++) { //van -e celbaero babu?
    			int szcount=1;
    			while (true) {
    				JarmoService.JarmoLog  ("Szomszed celbaero count"+szcount);
    	 			Mezo ujm = androidbogyo[t].mezo.szomszedok[szcount];
    			    if (androidbogyo[t].mezo.szomszedok[szcount]==null) break; //nincs tobb szomszed
    			    if (JarmoMain.bogyoItt(ujm)!=null) {szcount++; continue;} //itt babu all
    			    if (ujm.id>=andcelmin && ujm.id<=andcelmax) { //celba erne?
    			    	   JarmoService.JarmoLog  ("igen, android celba erne itt: "+ujm.id);
        			       Mezo regimezo=androidbogyo[t].mezo;
        			       JarmoMain.crossAField(regimezo);
    			     	   androidbogyo[t].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    			     	   androidbogyo[t].mezoHist.add(ujm);
    			           if (androidbogyo[t].mezoHist.size()>8) androidbogyo[t].mezoHist.remove(androidbogyo[t].mezoHist.firstElement());

      			           JarmoMain.behozHaAndroidCelbaert (ujm, androidbogyo[t].allapot);
      			           return;
         			       
    			    } //if beert
    			    szcount++;
    			} // while
    		} //bogyo for
    		
    		//nincs leutheto bogyo, es nincs is celbalepo. Lepjunk valamit.
    		JarmoService.JarmoLog  ("Lepjunk valamit");
    		int randomCount=0;
    		while (true) {
    			  randomCount++;
    			  if (randomCount>500) {
    				  //jatek vege
    				  JarmoMain.endGame ();
    				  return;
    			  }
    			  int b = 1+(int)(Math.random()*5);
    			  JarmoService.JarmoLog  ("Random "+b);
    			  if (androidbogyo[b].mezo.id>25) continue; //palya szelen parkol, keressunk mast
     			  if (androidbogyo[b].mezo.id>=andcelmin && 
     				  androidbogyo[b].mezo.id<=andcelmax) continue; //mar celbaert, keressunk mast
     
    			  Bogyo ezzel = androidbogyo[b];
    			  int szcount=1;
    			  int szmax=1;
    			  while (true) {
    				  if (androidbogyo[b].mezo.szomszedok[szmax]==null) break; //nincs tobb szomszed, valasszunk mast
    	    		  szmax++;		  
    			  }
    			  JarmoService.JarmoLog("szomszedok szama="+szmax);
    			  
    			  while (true) {
    				  szcount=1+(int)(Math.random()*szmax);
    				  
    				  JarmoService.JarmoLog  (b+" kivalasztott szomszed "+szcount);
    				  if (androidbogyo[b].mezo.szomszedok[szcount]==null) break; //valamiert nincs ilyen szomszed, valasszunk mast  				
    				  Mezo ujm = androidbogyo[b].mezo.szomszedok[szcount];
    				  boolean voltmar4x = false;
    				  JarmoService.JarmoLog  ("$$$$ MEzohist size ="+ezzel.mezoHist.size());
    				  if (ezzel.mezoHist.size()>=8) {  
    					  JarmoService.JarmoLog  ("mostani"+ezzel.mezo+" ujm "+ujm);
    					  JarmoService.JarmoLog  ("Hist "+ezzel.mezoHist.get(0));
    					  JarmoService.JarmoLog  ("Hist "+ezzel.mezoHist.get(1));
    					  JarmoService.JarmoLog  ("Hist "+ezzel.mezoHist.get(2));
    					  JarmoService.JarmoLog  ("Hist "+ezzel.mezoHist.get(3));
    					  JarmoService.JarmoLog  ("Hist "+ezzel.mezoHist.get(4));
    					  JarmoService.JarmoLog  ("Hist "+ezzel.mezoHist.get(5));
    					  JarmoService.JarmoLog  ("Hist "+ezzel.mezoHist.get(6));
    					  JarmoService.JarmoLog  ("Hist "+ezzel.mezoHist.get(7));
    					  if (ezzel.mezoHist.get(0)==ujm &&   // 4x utanaz az odavissza lepes tilos
    							  ezzel.mezoHist.get(2)==ujm &&
    							  ezzel.mezoHist.get(4)==ujm &&
    							  ezzel.mezoHist.get(6)==ujm &&
    							  ezzel.mezoHist.get(1)==ezzel.mezo &&
    							  ezzel.mezoHist.get(3)==ezzel.mezo &&
    							  ezzel.mezoHist.get(5)==ezzel.mezo &&
    							  ezzel.mezoHist.get(7)==ezzel.mezo) 
    						  voltmar4x=true;
    				  }
    				  
    				 //ha az uj szomszedban fenyegetne valami, es meg van remeny arra, hogy talalunk mas
    				 //lepest (randomCount meg csak 200-nal jar), akkor jelezzuk a voltmar4x-rel, hogy ide inkabb
    				 //ne lepjunk
    				 if (randomCount<200 && JarmoMain.strength==2) {
    				        int szszcount=1; //szomszed szomszedja
    		    			while (true) {
    		    			   if (ujm.szomszedok[szszcount]==null) break;
    		    			   Mezo ujm_ujm = ujm.szomszedok[szszcount]; //szomszed szomszedja
    		    			   if (JarmoMain.bogyoItt(ujm_ujm)!=null) 
    		    				   JarmoService.JarmoLog  (szcount+". szomszedban ("+ujm_ujm.id+") van egy szin="+JarmoMain.bogyoItt(ujm_ujm).szin);
    		    			   if (JarmoMain.bogyoItt(ujm_ujm)!=null && JarmoMain.bogyoItt(ujm_ujm).szin==JarmoMain.userSzine) {
    		    				   //user szine? ajjaj, ez a mezo veszelyes
    		    				   voltmar4x=true; //ezzel jelzunk, hogy ide ne lepjen
    		    				   break;
    		    			   }
    		    			   szszcount++;
    		    			}
    				 }
    				 

    			     if (JarmoMain.bogyoItt(ujm)==null && voltmar4x==false) { //szabad szomszed, usgyi ide
    			    	// JarmoService.JarmoLog  ("ide mozg vegul"+ujm.id);
    			  //  	 androidbogyo[b].mezo=ujm;
    			    	 Mezo regimezo=androidbogyo[b].mezo;
    			    	 JarmoMain.crossAField(regimezo);
    			    	 androidbogyo[b].slideToPos(ujm, 60, 10, JarmoMain.bogyoItt(ujm));//suritve
    			    	 
    			    	 ezzel.mezoHist.add(ujm);
    			         if (ezzel.mezoHist.size()>8) ezzel.mezoHist.remove(ezzel.mezoHist.firstElement());
    			     
       			         JarmoMain.behozHaAndroidCelbaert (ujm, androidbogyo[b].allapot);
       			         return;
    			     }
    			     szcount++;
    			  } //while szomszed
    		} //while random;
    }
    
    public static void crossAField (Mezo lastmezo) {
    	for (int t=1;t<=25;t++) {
    		JarmoMain.mezok[t].isCrossed=0;
    		JarmoMain.mezok[t].invalidate();
    	}
    	
    	final Mezo llastmezo=lastmezo;
    	JarmoMain.handler.postDelayed(new Runnable() {
    		public void run () {
    			int numberOfFields=0;
    			switch (JarmoMain.crossLevel)
    			{
    			case 0: return;
    			case -1:
    				llastmezo.markCross();
    				break;
    			case 1:
    			case 2:
    			case 3:	    	
    				numberOfFields=JarmoMain.crossLevel;
    				for (int c=1;c<=numberOfFields;c++) {
    				  int field;	
    				  while (true) {////
    					  field=(int)(Math.random()*24)+1; 
    					  if (JarmoMain.bogyoItt(JarmoMain.mezok[field])==null &&
    					      JarmoMain.mezok[field].isCrossed==0)
    						     break;
    				  } //while
    				  JarmoMain.mezok[field].markCross();
    				} //for c
    				break;
    			case 11:

    				for (int c=1;c<=4;c++) {
    					final int cc=c;
    					final Runnable r=new Runnable () {
    						public void run () {	
    							JarmoService.JarmoLog ("Letelt "+cc);
    							int field;	
    							while (true) {////
    								field=(int)(Math.random()*24)+1; 
    								if (JarmoMain.bogyoItt(JarmoMain.mezok[field])==null &&
    										JarmoMain.mezok[field].isCrossed==0)
    									break;
    							} //while
    							
    							//regi cross torol
    							if (JarmoMain.crossedMezok[cc]!=null) { 
      							  JarmoMain.crossedMezok[cc].isCrossed=0;
      							  JarmoMain.crossedMezok[cc].invalidate ();
      							}
    							
    							JarmoMain.mezok[field].markCross();
    							JarmoMain.crossHandler.postDelayed(this, cc*8000);
    							JarmoMain.crossedMezok[cc]=JarmoMain.mezok[field];
    						}
    					};
    					
    					JarmoMain.crossHandler.postDelayed (r, c*200);
    				} //for c

    				
    			} //switch
    			   
    		}
    	}, 1000);
    }
    
    public static void parkolobolAktival () { //ha parkoloban kattintunk, userstart mezoket kell aktivalni
       	int userstartmin, userstartmax;

       	if (JarmoMain.androidHelyettLepUser==0) {
       		if (JarmoMain.userLent==1) { 
       			userstartmin=21; userstartmax=25;
       		} else {
       			userstartmin=1; userstartmax=5;
       		}
       	} else { //android helyett lepes  -> android startmezoit kell aktivalni 
       		if (JarmoMain.userLent==0) { 
       			userstartmin=21; userstartmax=25;
       		} else {
       			userstartmin=1; userstartmax=5;
       		}       		
       	}

    	for (int t=userstartmin;t<=userstartmax;t++) { //elso sor aktival
		    		if (JarmoMain.bogyoItt (JarmoMain.mezok[t])==null) {
		    			JarmoService.JarmoLog  ("kezdomezo aktival "+t);
		    			JarmoMain.mezok[t].isActive=1;
		    			JarmoMain.mezok[t].invalidate();
		    			JarmoMain.ed.addDragTargetLista (JarmoMain.mezok[t]);
		    	    }
		} //for

    }
    
    public static boolean userPalyaraVisszarak () { // returns: rakhato vissza valamelyik?
       	Bogyo userbogyo[]=new Bogyo [7];
    	int usercelmin, usercelmax;
    	int userstartmin, userstartmax;
  
    	if (JarmoMain.androidHelyettLepUser==0) {
    		for (int t=1;t<=5;t++) {
    			if (JarmoMain.userSzine==0) {
    				userbogyo[t]=JarmoMain.feherbogyo[t];
    			} else {
    				userbogyo[t]=JarmoMain.feketebogyo[t];   	    		
    			}
    		}
    		if (JarmoMain.userLent==1) { 
    			userstartmin=21; userstartmax=25;
    			usercelmin=1; usercelmax=5;
    		} else {
    			userstartmin=1; userstartmax=5;
    			usercelmin=21; usercelmax=25;
    		}
    	} else { //android helyett lep user, andoid bogyoval is lehet visszalepni. Minden pont forditva van
    		for (int t=1;t<=5;t++) {
    			if (JarmoMain.userSzine==1) {
    				userbogyo[t]=JarmoMain.feherbogyo[t];
    			} else {
    				userbogyo[t]=JarmoMain.feketebogyo[t];   	    		
    			}
    		}
    		if (JarmoMain.userLent==0) { 
    			userstartmin=21; userstartmax=25;
    			usercelmin=1; usercelmax=5;
    		} else {
    			userstartmin=1; userstartmax=5;
    			usercelmin=21; usercelmax=25;
    		}
    	}

    	
 
   		
    	engedelyezMozg (0);
    	int count=0;
 //   	int homeStart=1;
 
    	for (int t=1;t<=5;t++) {
   	    	 if (userbogyo[t].mezo.id >30) count++;
  	    }
  	    JarmoService.JarmoLog  ("User parkoloban babuk="+count);
  	    if (count==0) return false;
  	    
  	    count=0;
 	    for (int t=userstartmin;t<=userstartmax;t++) { //van e szabad hely a start sorban?
 	    	if (JarmoMain.bogyoItt(JarmoMain.mezok[t])==null) count++; 
 	    }
 	    JarmoService.JarmoLog  ("ureshelyek a sorban="+count);
  	    if (count==0) return false;
 	    
    	
    	//parkoloban legyenek erinthetok
   	    for (int t=1;t<=5;t++) {
      	     if (userbogyo[t].mezo.id >30) {
      	    	 if (JarmoMain.androidHelyettLepUser==1 && JarmoMain.jatekMode==4) {
      	    		 JarmoService.JarmoLog  ("nem erintheto");
        	    	 userbogyo[t].setErintheto(0);    	    		 
      	    	 } else {
      	    	     userbogyo[t].setErintheto(1);
      	    	 }
      	    	 userbogyo[t].startFlash();
      	    	 count++;    	     
      	     }          
      	}//for
    	return true;
    }
    
    
    public static boolean userMegMozoghat () {
       	Bogyo userbogyo[]=new Bogyo [7];
    	int usercelmin, usercelmax;
    	int userstartmin, userstartmax;
  
    	if (JarmoMain.androidHelyettLepUser==0) {
    		for (int t=1;t<=5;t++) {
    			if (JarmoMain.userSzine==0) {
    				userbogyo[t]=JarmoMain.feherbogyo[t];
    			} else {
    				userbogyo[t]=JarmoMain.feketebogyo[t];   	    		
    			}
    		}
    		if (JarmoMain.userLent==1) { 
    			userstartmin=21; userstartmax=25;
    			usercelmin=1; usercelmax=5;
    		} else {
    			userstartmin=1; userstartmax=5;
    			usercelmin=21; usercelmax=25;
    		}
    	} else { //android helyett lepett user -> mozoghat meg? Minden forditva van
    		for (int t=1;t<=5;t++) {
    			if (JarmoMain.userSzine==1) {
    				userbogyo[t]=JarmoMain.feherbogyo[t];
    			} else {
    				userbogyo[t]=JarmoMain.feketebogyo[t];   	    		
    			}
    		}
    		if (JarmoMain.userLent==0) { 
    			userstartmin=21; userstartmax=25;
    			usercelmin=1; usercelmax=5;
    		} else {
    			userstartmin=1; userstartmax=5;
    			usercelmin=21; usercelmax=25;
    		}    		
    	}
    	JarmoService.JarmoLog  ("Check user meg mozoghat?");

   	    for (int t=1;t<=5;t++) {	    
   	    	   
    	        Bogyo ezzel = userbogyo[t];
    	        JarmoService.JarmoLog  ("userMegMozoghat, bogyo"+t+" loc="+ezzel.mezo.id);
    	    	if (ezzel.mezo.id>30) continue; //ez parkolt;
    	    	
    	    	if (ezzel.mezo.id>=usercelmin && ezzel.mezo.id<=usercelmax) continue; //celbaert;
    	    	
    	    	//szomszed ellenorzese
  			    int szcount=1;
  			    while (true) {
  			      if (userbogyo[t].mezo.szomszedok[szcount]==null) break; //nincs tobb szomszed, valasszunk mast
  			      Mezo ujm = userbogyo[t].mezo.szomszedok[szcount];
     		      boolean voltmar4x = false;
  			      if (ezzel.mezoHist.size()>=8) {  	  	
		          if (ezzel.mezoHist.get(0)==ujm &&   // 4x utanaz az odavissza lepes tilos
		      	          ezzel.mezoHist.get(2)==ujm &&
		         	      ezzel.mezoHist.get(4)==ujm &&
		     		      ezzel.mezoHist.get(6)==ujm &&
		     		      ezzel.mezoHist.get(1)==ezzel.mezo &&
		     		      ezzel.mezoHist.get(3)==ezzel.mezo &&
		     		      ezzel.mezoHist.get(5)==ezzel.mezo &&
		     		      ezzel.mezoHist.get(7)==ezzel.mezo) 
		            	     voltmar4x=true;
		          } //if

  			     if (JarmoMain.bogyoItt(ujm)==null && voltmar4x==false) { //szabad szomszed, ide mozoghatna
  			    	 JarmoService.JarmoLog  ("Igen ide meg mozoghatna: "+ujm.id);
  			    	 return true;
  			     }
                 szcount++;
    	       } //while szomszed
    	    }//for
   	        JarmoService.JarmoLog  ("Nem mar nem mozoghat.");
    	    return false;
    }
 /*   
    public static void userBogyoLeut (Bogyo ezzel, Bogyo ezt) {
    	final Bogyo eezzel=ezzel, eezt=ezt;
    	
    	//kicsit oldalra huz
    	
        public static void utkozes (Bogyo a, Bogyo b) {//a mozog, b celban all
    	    final Bogyo aa=a, bb=b;
     
    		final int radius = Math.round (JarmoMain.BOGYORADIUS)+aa.strokewidth;
    		final int xdist=bb.px-aa.px; //negativ, ha visszafele mozog
    		final int ydist=bb.py-aa.py; //negativ, ha felfele mozog
    	//	final int dist=(int) (Math.sqrt ( (double)((xdist*xdist)+(ydist*ydist))));

    		final int visszapattanXUnit=(int)((xdist*1.7)/15);
    		final int visszapattanYUnit=(int)((ydist*1.7)/15);
    		final int px=b.px; // eltaroljuk, mert b bogyo is mozogni fog
    		final int py=b.py;
    		
            //visszapattan
    		for (int t=1;t<=15;t++) {
    			final int tt=t;
        	   JarmoMain.handler.postDelayed(new Runnable () {
        		  public void run () {
        		    aa.px=px-visszapattanXUnit*tt;
        		    aa.py=py-visszapattanYUnit*tt;
        		    aa.invalidate();  			
        		    
        		    bb.px=px+visszapattanXUnit*tt;
           		    bb.py=py+visszapattanYUnit*tt;
           		    bb.invalidate();  			
      		   
           		  }
        	   }, t*6);
    		} //for

    }
    
   */ 
    
    public static boolean checkUserMove (Bogyo ezzel, Mezo ide) {
       	Bogyo userbogyo[]=new Bogyo [7];
    	int usercelmin, usercelmax;
    	int userstartmin, userstartmax;
//       	Bogyo androidbogyo[]=new Bogyo [7];
    	int androidcelmin, androidcelmax;
    	int androidstartmin, androidstartmax;

	      // 	arrowFent.start(6,0);
	      //	arrowLent.start(3,1);
    	JarmoService.JarmoLog("checkUserMove ide="+ide.id);
    	
    	if (JarmoMain.androidHelyettLepUser==0) {
    		for (int t=1;t<=5;t++) {
    			if (JarmoMain.userSzine==0) {
    				userbogyo[t]=JarmoMain.feherbogyo[t];
    			} else {
    				userbogyo[t]=JarmoMain.feketebogyo[t];   	    		
    			}
    		}
    	} else {//userbogyo ugy tesz, mintha androidbogyo lenne
    		for (int t=1;t<=5;t++) {
    			if (JarmoMain.userSzine==0) {
    				userbogyo[t]=JarmoMain.feketebogyo[t];
    			} else {
    				userbogyo[t]=JarmoMain.feherbogyo[t];   	    		
    			}
    		}
    	} //androidHelyettLepUser ends

    	if (JarmoMain.androidHelyettLepUser==0) {    	
    		if (JarmoMain.userLent==1) { 
    			userstartmin=21; userstartmax=25;
    			usercelmin=1; usercelmax=5;
    		} else {
    			userstartmin=1; userstartmax=5;
    			usercelmin=21; usercelmax=25;
    		}
    	} else {//valojaban az android helye
    		if (JarmoMain.userLent==0) { 
    			userstartmin=21; userstartmax=25;
    			usercelmin=1; usercelmax=5;
    		} else {
    			userstartmin=1; userstartmax=5;
    			usercelmin=21; usercelmax=25;
    		}
    	}
    	

    	Mezo ittvolt=ezzel.mezo;
    	ezzel.isParkolt=false;
    	JarmoService.JarmoLog  ("BMO 1 userLent"+JarmoMain.userLent+" androidHelyettLepUser"+androidHelyettLepUser);
    	if (ittvolt.id>=usercelmin && ittvolt.id<=usercelmax) { return false;} //mar celba ert, maradjon ott
  //     	JarmoService.JarmoLog  ("BMO 2");
    	
    	if (ide.isCrossed==1) {
			JarmoSetting.playSound(R.raw.error);
			return false;    		
    	}
           	
    	if (JarmoMain.bogyoItt(ide)!=null) { //bogyora lepett
    		Bogyo celbogyo = JarmoMain.bogyoItt(ide);   		
    		JarmoService.JarmoLog  ("van itt bogyo");
    		if (celbogyo.szin == ezzel.szin) { //sajat szinre nem lepunk
    			JarmoSetting.playSound(R.raw.error);
    			return false;
    		} else {
    			utkozes (ezzel, celbogyo);
   		        ezzel.allapot=1;
			  
    		}
    	}
       	JarmoService.JarmoLog  ("BMO 3");
        
    	JarmoService.JarmoLog  ("size ="+ezzel.mezoHist.size());
    	if (ezzel.mezoHist.size()>=8) {  	  	
          if (ezzel.mezoHist.get(0)==ide &&   // 4x utanaz az odavissza lepes tilos
    	      ezzel.mezoHist.get(2)==ide &&
       	      ezzel.mezoHist.get(4)==ide &&
   		      ezzel.mezoHist.get(6)==ide &&
   		      ezzel.mezoHist.get(1)==ezzel.mezo &&
   		      ezzel.mezoHist.get(3)==ezzel.mezo &&
   		      ezzel.mezoHist.get(5)==ezzel.mezo &&
   		      ezzel.mezoHist.get(7)==ezzel.mezo) {
        		   JarmoSetting.playSound(R.raw.error);
   				   return false;
          }
    	}
    		// jo lepes -> lep
    	ezzel.mezoHist.add(ide);
       	JarmoService.JarmoLog  ("BMO 4");
        
   		if (ezzel.mezoHist.size()>8) ezzel.mezoHist.remove(ezzel.mezoHist.firstElement());
 
   		//atlep
   		ezzel.mezo=ide;
 
   		if (JarmoMain.userVisszarakhat==1) {
   			JarmoService.JarmoLog  ("visszarakas megvolt, ide="+ide.id);
   			JarmoMain.userVisszarakhat=0;
   			int hol=0;  
   			if (ide.id>=21) hol=1; // ha visszarakas volt, ott csusztat ossze parkban, amelyik terfelen van
   			JarmoMain.parkbanOsszecsuztat (ittvolt, hol); //0-fent, 1-lent
   			
   			for (int t=1;t<=5;t++) {
     			   userbogyo[t].stopFlash();
      		}
   			
	    	for (int t=1;t<=5;t++) { //parkmezok visszaszinezese
			   	JarmoMain.mezok[30+t].isActive=1;
			   	JarmoMain.mezok[30+t].invalidate();
			   	JarmoMain.mezok[40+t].isActive=1;
		    	JarmoMain.mezok[40+t].invalidate();
	    	}
	
   		}
 
   		
   		//kivalasztott celba jutott?
   		if (ide.id>=usercelmin && ide.id<=usercelmax && ezzel.allapot==1) {
   			JarmoMain.userVisszarakhat=1;
   			if (JarmoMain.userPalyaraVisszarak()==true) {
   	    		JarmoMain.userVisszarakhat=1;
  		    	JarmoService.JarmoLog  ("user palyara visszarak");
  		    	return true;
   			} else {
   				JarmoService.JarmoLog  ("user nem rakhat vissza babut");
   			}
   		}

   		
   		//kesz, android johet
   		JarmoService.JarmoLog  ("CheckUserMove kesz, jatekmode "+JarmoMain.jatekMode);
   		switch (JarmoMain.jatekMode) 
   		{
   		case 1:
   		case 6: /*war*/
   		case 2:	/*androidellen*/
   			engedelyezMozg (0);   		
   			JarmoMain.handler.postDelayed(new Runnable() {
   				public void run () {
   					JarmoMain.makeAndroidMove();
   				}
   			},1000);
   			break;
   			
   		case 3: //egymasellen
   			engedelyezMozg (0); //mindenkit kezar
   			if (JarmoMain.androidHelyettLepUser==0) JarmoMain.androidHelyettLepUser=1;
   			else JarmoMain.androidHelyettLepUser=0;
   			if (JarmoMain.userMegMozoghat ()==false) {
   			       //jatek vege
				   JarmoMain.endGame ();
				   return true;
   			} 
   			if (JarmoMain.androidHelyettLepUser==1) engedelyezMozg (2); //android helyett engedelyez
  			if (JarmoMain.androidHelyettLepUser==0) engedelyezMozg (1); //valodi usert engedelyez
  			   			break;

   		case 4: //wifi
   			engedelyezMozg (0); //mindenkit kezar
   			if (JarmoMain.androidHelyettLepUser==0) JarmoMain.androidHelyettLepUser=1;
   			else JarmoMain.androidHelyettLepUser=0;
   			if (JarmoMain.userMegMozoghat ()==false) {
   			       //jatek vege
				   JarmoMain.endGame ();
				   return true;
   			} 
  			if (JarmoMain.androidHelyettLepUser==0) { 
  				JarmoSetting.playSound(R.raw.next);
  				engedelyezMozg (1); //lokalis usert engedelyez
  			}
  			break;
			   			
   		default:
   			break;
   		}
   		
   		return true;
    }

    public static void endGame () {
       	Bogyo userbogyo[]=new Bogyo [7];
    	Bogyo androidbogyo[]=new Bogyo [7];
    	
    	if (JarmoMain.gameEnded==1) return;
    	JarmoMain.gameEnded=1;
        	
    	int usercelmin, usercelmax, andcelmin, andcelmax;
    	
   		for (int t=1;t<=5;t++) {
   	    	if (JarmoMain.userSzine==0) {
    			userbogyo[t]=JarmoMain.feherbogyo[t];
    			androidbogyo[t]=JarmoMain.feketebogyo[t];
   	    	} else {
    			userbogyo[t]=JarmoMain.feketebogyo[t];   	    		
       			androidbogyo[t]=JarmoMain.feherbogyo[t];
   	    	}
    	}
   		
   		if (JarmoMain.userLent==1) { 
   			andcelmin=21; andcelmax=25;
   			usercelmin=1; usercelmax=5;
   		} else {
   			andcelmin=1; andcelmax=5;
   			usercelmin=21; usercelmax=25;
   		}

    	int fcount=1;
    	int currUserPoint=0;
    	for (int t=1;t<=5;t++) {
    		if (userbogyo[t].mezo.id>30 || userbogyo[t].isParkolt==true) continue;
    		
    		final int pont;
   			final Bogyo fb=userbogyo[t];
   		 
    		if (userbogyo[t].mezo.id>=usercelmin &&
    			userbogyo[t].mezo.id<=usercelmax) {
    			pont=2; 
    		} else {
    			pont=1; 
    		}
    		currUserPoint+=pont;
    		
	    	if (pont>0) JarmoMain.arrowView.addArrow(fb, pont, t);

   
          	JarmoMain.alap.postDelayed(new Runnable () {
    			public void run () {
    			//	fb.bogyoScore=Integer.toString(pont);
    			//	fb.doMegmutat ();
    				
    				JarmoService.JarmoLog  ("user pont ="+pont);
    				JarmoMain.pointUser+=pont;
        	
    	
    				if (JarmoMain.userLent==0)
    				   JarmoMain.pointFent=JarmoMain.pointUser;   				   
    				else 
    				   JarmoMain.pointLent=JarmoMain.pointUser;
    				JarmoMain.alap.invalidate();
    			}
    		}, 1000+fcount*200);
    
    		fcount++;
    	} //for
    	int currAndroidPoint=0;
    	for (int t=1;t<=5;t++) {
    		if (androidbogyo[t].mezo.id>30 || androidbogyo[t].isParkolt==true) continue;

    		final int pont;
    		final Bogyo fb=androidbogyo[t];			 
    		if (androidbogyo[t].mezo.id>=andcelmin &&
    				androidbogyo[t].mezo.id<=andcelmax) {
    			pont=2; 
    		} else {
    			pont=1; 
    		}
            currAndroidPoint+=pont;
    		if (pont>0) JarmoMain.arrowView.addArrow(fb, pont, 5+t);

    		JarmoMain.alap.postDelayed(new Runnable () {
    			public void run () {
    				//	fb.doMegmutat ();
    				JarmoService.JarmoLog  ("android pont "+pont);
    				JarmoMain.pointAndroid+=pont;

    				JarmoService.JarmoLog  ("pontandroid ="+JarmoMain.pointAndroid);
    				if (JarmoMain.userLent==0)
    					JarmoMain.pointLent=JarmoMain.pointAndroid; //android pontjai
    				else 
    					JarmoMain.pointFent=JarmoMain.pointAndroid;

    				JarmoMain.alap.invalidate();
    			}
    		}, 1000+fcount*200);

    		fcount++;
    	} //for

    	final int cca=currAndroidPoint, ccu=currUserPoint;
    //	JarmoService.JarmoLog  ("cca="+cca+" ccu"+ccu);
    	JarmoMain.main.postDelayed(new Runnable () {//veghang
    		public void run () {
    			if (JarmoMain.jatekMode<=2)
    				if (cca < ccu) //android ellen
    			       JarmoSetting.playSound(R.raw.tada);
    			    else 
    			       JarmoSetting.playSound(R.raw.lose);
    		    
			    if (JarmoMain.jatekMode==3 ) //egymas ellen
 			        JarmoSetting.playSound(R.raw.tada);
			
			    if (JarmoMain.jatekMode==4 ) //wifin ellen
				    if (JarmoMain.pointAndroid<JarmoMain.pointUser) //wifin
				       JarmoSetting.playSound(R.raw.tada);
	    			else 
	    			   JarmoSetting.playSound(R.raw.lose);
	  		}
    	}, 3500);
 

       	JarmoService.JarmoLog  ("Jatek vege kint");
       	int osszegzesmulva;
       	if (JarmoMain.jatekMode==6) osszegzesmulva=5000;
       	else osszegzesmulva=7000;
       	JarmoMain.stopTimer();
 
   		// build new game in 7 sec
   		JarmoMain.alap.postDelayed(new Runnable () {
   			public void run () {
   				JarmoService.JarmoLog  ("JAtek timer 7s end");
   		//		JarmoMain.arrowView.invalidate=1;
   		//		JarmoMain.arrowView.invalidate();
   				

   		       	if (jatekMode==6) {///war
   		       		boolean timeout=false;
   		       		if (JarmoMain.timerValue==0 && JarmoMain.timerTotal>0) {
   		       			//timer lejarta miatt van game over -> nincs gyujtott pont
   		       			timeout=true;
   		       		}
   		       		//nyertes jatekok osszeadasa
   		       		int wonPoints=0;
		       		SharedPreferences settings = JarmoSetting.js.getSharedPreferences(JarmoSetting.SPREF_MYRECORD, 0);
		       		int hsv = settings.getInt("myRecord", 0);
	       			
 	       				 
		       		JarmoService.JarmoLog  ("nyertes dontesnel cca="+cca+" ccu"+ccu);
   		       		if (cca > ccu || timeout) { //ha vesztett, itt a vege
   		       			//JarmoMain.handleWarEnd (cca, ccu);
   		       			MyCallback wscallback = new MyCallback () {
   		       				public void doStuff () {
   		 //      				if (AdBuddiz.isReadyToShowAd(JarmoSetting.js)) { // this = current Activity
   	     //    					AdBuddiz.showAd(JarmoSetting.js);
         //           	    }
        	    	
   		       				    JarmoSetting.nyitoMode=2;
   		       					JarmoMain.jarmoMain.finish();	
   		       				}
   		       			};
   		       			JarmoMain.showWarStatus (wscallback, true); 
   		       			JarmoSetting.playSound(R.raw.lose);

   	  		       		wonPoints=0;
   	   		       		for (int c=1; c<=JarmoMain.NUMBER_OF_WARS;c++)  wonPoints+=JarmoMain.warScores[c];    		        
//   			       		if (hsv < wonPoints) JarmoSetting.setRecord(wonPoints); //GDPR

   		       			return;
   		       		}
   		       		
                    //game won -> tabla, majd megyunk tovabb
   		       		MyCallback mcb = new MyCallback () {
   		       			public void doStuff () {
     		       	      	JarmoMain.timerValue=0;
     		       	      	
     		       	      	for (int xx=1;xx<=25;xx++) JarmoMain.mezok[xx].isCrossed=0;
     		       	      	JarmoMain.crossHandler.removeCallbacksAndMessages(null);
     		       	      	for (int cc=1;cc<=4;cc++) JarmoMain.crossedMezok[cc]=null;
     		       	      	
   		   		       		JarmoMain.jatekLevel++;   
		   		       		if (JarmoMain.jatekLevel==3) {JarmoMain.strength=1; JarmoMain.timerTotal=20;JarmoMain.crossLevel=0;} 		   		       		
   		   		       		if (JarmoMain.jatekLevel==5) {JarmoMain.strength=2; JarmoMain.timerTotal=0;JarmoMain.crossLevel=0;} 
   		   		       		if (JarmoMain.jatekLevel==9) {JarmoMain.strength=2; JarmoMain.timerTotal=20;JarmoMain.crossLevel=0;} 
   		   		       		if (JarmoMain.jatekLevel==13) {JarmoMain.strength=2; JarmoMain.timerTotal=15;JarmoMain.crossLevel=0;} 
   		   		       		if (JarmoMain.jatekLevel==17) {JarmoMain.strength=2; JarmoMain.timerTotal=15;JarmoMain.crossLevel=3;} 
   		   		       		if (JarmoMain.jatekLevel==21) {JarmoMain.strength=2; JarmoMain.timerTotal=10;JarmoMain.crossLevel=4;} 
   		   		       		if (JarmoMain.jatekLevel==25) {JarmoMain.strength=2; JarmoMain.timerTotal=10;JarmoMain.crossLevel=11;} 
   		   		       		if (JarmoMain.jatekLevel==29) {JarmoMain.strength=2; JarmoMain.timerTotal=5;JarmoMain.crossLevel=11;} 
   		   		       		if (JarmoMain.jatekLevel==33) { //nincs tobb jateklevel
   	    //	    				if (AdBuddiz.isReadyToShowAd(JarmoSetting.js)) { // this = current Activity
   	    //     					AdBuddiz.showAd(JarmoSetting.js);
        //            		    }
        	    	
   		   		       			JarmoMain.jarmoMain.finish();	

   		   		       		} //33   	

   		    		      	if (jatekMode==2) {//android ellen, valtozo szin;
   		    		       		if (JarmoMain.userSzine==0) JarmoMain.userSzine=1;
   		    		       		else JarmoMain.userSzine=0;
   		    		       	}	 

   		    		      	//terfelcsere
   		    		      	if (JarmoMain.userLent==0) userLent=1; //cserelni kell a usert
   		    		      	else userLent=0;

   		   		       		//prepare for restart
   		   		       		JarmoMain.main.removeView(JarmoMain.arrowView);
   		   		       		JarmoMain.arrowView=null;

   		   		       		if (JarmoMain.jatekMode==5) {
   		   		       			JarmoMain.demoStep(30);
   		   		       			return;
   		   		       		}

   		   		       		for (int t=1; t<=49; t++) {
   		   		       			if (JarmoMain.mezok[t]!=null) JarmoMain.mezok[t]=null;
   		   		       		}
   		   		       		for (int t=1; t<=200; t++) {
   		   		       			if (JarmoMain.osszekot[t]!=null) JarmoMain.osszekot[t]=null;
   		   		       		}
   		   		       		JarmoService.JarmoLog  ("Jatek ujraaaa");

   		   		       		JarmoMain.buildGame(1); 		

   		       			}
   		       		};

   		       		//eredmenyt eltarol
   		       		int war = ((JarmoMain.jatekLevel-1) / 4)+1;
   		       		JarmoMain.warScores[war]+=ccu; //ccu-cca?
   		       		JarmoService.JarmoLog  ("Szamolt war ="+war+ "uj erteke="+JarmoMain.warScores[war]);


   		       		//a mostanival valo rekordot elment ha kell
   		       		wonPoints=0;
   		       		for (int c=1; c<=JarmoMain.NUMBER_OF_WARS;c++)  wonPoints+=JarmoMain.warScores[c];    		        
//   		       		if (hsv < wonPoints) JarmoSetting.setRecord(wonPoints); //GDPR

   		       		JarmoSetting.playSound(R.raw.tada);
   		       		JarmoMain.showWarStatus (mcb, false);
   		       		return;

   		       	} //6 ends

   		       	//prepare for restart
 		       	JarmoMain.main.removeView(JarmoMain.arrowView);
 		       	JarmoMain.arrowView=null;

 				if (JarmoMain.jatekMode==5) {
   					JarmoMain.demoStep(30);
   					return;
 				}
  
   		       	for (int t=1; t<=49; t++) {
   			       if (JarmoMain.mezok[t]!=null) JarmoMain.mezok[t]=null;
   		        }
 		       	for (int t=1; t<=200; t++) {
 			       if (JarmoMain.osszekot[t]!=null) JarmoMain.osszekot[t]=null;
 		       	}
 		       	JarmoService.JarmoLog  ("Jatek ujraaaa");
 		       	
   		      	if (jatekMode==2) {//android ellen, valtozo szin;
   		       		if (JarmoMain.userSzine==0) JarmoMain.userSzine=1;
   		       		else JarmoMain.userSzine=0;
   		       	}	 

  		       		//terfelcsere
   		       	if (jatekMode==1 || jatekMode==2 || jatekMode==3 || jatekMode==4 || jatekMode==6) {//cserelni kell a terfelet          	 
   		       		if (JarmoMain.userLent==0) userLent=1; //cserelni kell a usert
   		       		else userLent=0;
   		       	}
		       	
 		       	JarmoMain.buildGame(1); 		
   			}
   		}, osszegzesmulva); 	

    }
    
   public static void showWarStatus (MyCallback whatToDoNext, boolean isLost) {
	   String[] lines=new String [20];
	   String[] status=new String [20];
	   int war = ((JarmoMain.jatekLevel-1) / 4)+1;
	   int battle = ((JarmoMain.jatekLevel-1) % 4)+1;
	   int total=0;

	   for (int t=0; t<JarmoMain.NUMBER_OF_WARS; t++)
	      lines[t]=JarmoSetting.js.getString(R.string.War)+" #"+(t+1)+":";
	   for (int t=0; t<JarmoMain.NUMBER_OF_WARS; t++) {
	      int sorwar=t+1;
	      
	      total+=JarmoMain.warScores[t+1];
		    
	      JarmoService.JarmoLog  ("war = "+war);
		  if (sorwar<war) { //megvivott
			  status[t]=JarmoMain.warScores[t+1]+" "+JarmoSetting.js.getString(R.string.Points);
		  }
		  if (sorwar==war) {
			  if (isLost==false) 
			    status[t]=JarmoMain.warScores[t+1] + " [" + JarmoSetting.js.getString(R.string.Battle)+": "+ battle+"/4]";
			  else 
				status[t]=JarmoSetting.js.getString(R.string.Lost);
				  
		  }
		  if (sorwar>war) { //megvivott
			  status[t]="-";
		  }
		  JarmoService.JarmoLog  (t+" sor "+lines[t]);
	    } //for sor

	   JarmoMain.trv.showTableRectView(JarmoSetting.js.getString(R.string.BattlesAndPoints)+":", 
	    		                        lines, 
	    		                        status, 
	    		                        JarmoSetting.js.getString(R.string.Total)+": "+total+" "+JarmoSetting.js.getString(R.string.Points),
	    		                        JarmoMain.NUMBER_OF_WARS, 
	    		                        war-1, //valaszto!!!, 
	    		                        10000, 
	    		                        98, 
	    		                        0, //delay,
	    		                        whatToDoNext, //callback, elobb definialtuk -> buildgame-t hivja
	    		                        -JarmoMain.BOGYORADIUS
	    		                        );
	
   }
    
   public static void showNextGameTable (MyCallback whatToDoNext) {
	   String[] lines=new String [20];////
	   String[] status=new String [20];////
       lines[0]=JarmoSetting.js.getString(R.string.AttackFromTop)+", ";
       lines[1]=JarmoSetting.js.getString(R.string.AttackFromBottom)+", ";
       lines[2]=JarmoSetting.js.getString(R.string.AttackFromTop)+", ";
       lines[3]=JarmoSetting.js.getString(R.string.AttackFromBottom)+", ";

       status[0]=JarmoSetting.js.getString(R.string.YouStart);
       status[1]=JarmoSetting.js.getString(R.string.YouStart);
       status[2]=JarmoSetting.js.getString(R.string.JarmoidStarts);
       status[3]=JarmoSetting.js.getString(R.string.JarmoidStarts);

       //title
       int war = ((JarmoMain.jatekLevel-1) / 4) + 1;
       int battle = ((JarmoMain.jatekLevel-1) % 4)+1;

       String title=JarmoSetting.js.getString(R.string.War)+" #"+war+", "+JarmoSetting.js.getString(R.string.Battle)+" #"+battle;
       String bottomline="";
       switch (JarmoMain.strength) {
       case 0:
    	   bottomline+=JarmoSetting.js.getString(R.string.MenuAndroidWeak1)+" "+JarmoSetting.js.getString(R.string.MenuAndroidWeak2);
    	   break;
       case 1:
    	   bottomline+=JarmoSetting.js.getString(R.string.MenuAndroidMedium1)+" "+JarmoSetting.js.getString(R.string.MenuAndroidMedium2);
    	   break;
       case 2:
    	   bottomline+=JarmoSetting.js.getString(R.string.MenuAndroidStrong1)+" "+JarmoSetting.js.getString(R.string.MenuAndroidStrong2);
    	   break;
       }
       if (JarmoMain.timerTotal!=0) {
    	   bottomline=JarmoSetting.js.getString(R.string.TimeForStep)+": "+JarmoMain.timerTotal+ " "+JarmoSetting.js.getString(R.string.Seconds);
       } 
    	   
       JarmoService.JarmoLog  ("Kiirasnal szint "+JarmoMain.jatekLevel+" battle"+battle);
       
	   JarmoMain.trv.showTableRectView(title, 
               lines, 
               status, 
               bottomline,
               4, 
               battle-1, //valaszto!!!, 
               10000, 
               100, 
               0, //delay,
               whatToDoNext, //callback, elobb definialtuk -> buildgame-t hivja
               JarmoMain.BOGYORADIUS
               );


   }
   
   
   public static void showHelp ( String l_lines, int l_timeout, int l_widthPercent, int delay, MyCallback myCallback) {
    	
	    JarmoMain.hrv.showHelpRectView (l_lines, l_timeout, l_widthPercent, delay, myCallback, 0);
    	
   }
   public static void showHelpTurned ( String l_lines, int l_timeout, int l_widthPercent, int delay, MyCallback myCallback) {
   	
	    JarmoMain.hrv.showHelpRectView (l_lines, l_timeout, l_widthPercent, delay, myCallback, 1);
   	
  }

} 


class Alap extends View {
	private Paint p,pker, pHamvasit, adBlackPaint;
	private int fekvo=0;
	private Paint pontPaint, nyilPaint1, nyilPaint2, timerPaint;
	private Paint helpTextPaint;
	private int helpTextOffset=0;
	private Context vcontext;
	private String helpText="";
//	public Typeface tf, tfh;
   	int helpTextMag=0;
   	private AssetManager assetManager;
   	private InputStream nyilStream;
   	private Bitmap nyilBitmap1, nyilBitmap2, nyilBitmap3, nyilBitmap4;
   	private int nyilalphaFent=0, nyilalphaLent=0;
   	boolean nyilLentHasFlash=false, nyilFentHasFlash=false;
   	public Rect sepField1, sepField2, adBlackRect;
   	private RectF frame;
   	

   			
   	
	
	public Alap (Context context) {
		super(context);
		vcontext=context;
		
			   	 
      		fekvo=0;
        	JarmoMain.helpField = new Rect (0, JarmoMain.HEIGHT-(helpTextMag), JarmoMain.WIDTH,JarmoMain.HEIGHT);
      		JarmoMain.mainfield = new Rect (0, 
        			                JarmoMain.BOGYORADIUS*3, 
                 	                JarmoMain.WIDTH,
  	                                JarmoMain.HEIGHT-(JarmoMain.BOGYORADIUS*3)-JarmoMain.helpField.height());
      		
      		 JarmoMain.field1 = new Rect (0,0,JarmoMain.WIDTH, JarmoMain.BOGYORADIUS*2);
      		 JarmoMain.field2 = new Rect (0,
      				                      JarmoMain.HEIGHT-JarmoMain.BOGYORADIUS*2-JarmoMain.helpField.height(),
      				                      JarmoMain.WIDTH, 
       			                          JarmoMain.HEIGHT-JarmoMain.helpField.height());
      		 JarmoMain.nyilField2 = new Rect (JarmoMain.mainfield.centerX()/2,
	                                          JarmoMain.HEIGHT-JarmoMain.BOGYORADIUS*3-JarmoMain.helpField.height(),
	                                          JarmoMain.mainfield.centerX()/2*3, 
	                                          JarmoMain.HEIGHT-JarmoMain.BOGYORADIUS*2-JarmoMain.helpField.height());
      		 JarmoMain.nyilField1 = new Rect (JarmoMain.mainfield.centerX()/2, 
      				                          JarmoMain.BOGYORADIUS*2,
      				                          JarmoMain.mainfield.centerX()/2*3,
      				                          JarmoMain.BOGYORADIUS*3);           
      		 sepField2 = new Rect (0,
                     JarmoMain.HEIGHT-JarmoMain.BOGYORADIUS*3-JarmoMain.helpField.height(),
                     JarmoMain.WIDTH, 
                     JarmoMain.HEIGHT-JarmoMain.BOGYORADIUS*2-JarmoMain.helpField.height());
             sepField1 = new Rect (0, 
                       JarmoMain.BOGYORADIUS*2,
                       JarmoMain.WIDTH,
                       JarmoMain.BOGYORADIUS*3);  
             frame=new RectF (0,0,JarmoMain.WIDTH, JarmoMain.HEIGHT);
             
             adBlackRect=new Rect (0, JarmoMain.HEIGHT+(int)(getResources().getDisplayMetrics().widthPixels/50),
                                   JarmoMain.WIDTH, JarmoMain.HEIGHT+JarmoSetting.adHeight);
      	
	
	    p = new Paint (Paint.ANTI_ALIAS_FLAG);
	    pker = new Paint (Paint.ANTI_ALIAS_FLAG);
	    pontPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
	    helpTextPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
	    nyilPaint1 = new Paint (Paint.ANTI_ALIAS_FLAG);
	    nyilPaint2 = new Paint (Paint.ANTI_ALIAS_FLAG);
	    timerPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
	    adBlackPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
	    pHamvasit = new Paint ();
        timerPaint.setColor(0x66FFFFFF);
  	    pHamvasit.setColor(0x33441111);
	    pHamvasit.setStyle(Style.FILL);
	    adBlackPaint.setColor(0xFF000000);
	    adBlackPaint.setStyle(Style.FILL);
	    try {
	    this.setBackgroundResource(R.drawable.wooden1);
	    } catch (OutOfMemoryError e) {
	    	e.printStackTrace();
			Toast.makeText(JarmoSetting.js, "Out of memory.", Toast.LENGTH_SHORT).show();
	//		if (AdBuddiz.isReadyToShowAd(JarmoSetting.js)) { // this = current Activity
	//				AdBuddiz.showAd(JarmoSetting.js);
    //        }
			JarmoMain.jarmoMain.finish();
	    }
	}
	public void nyilFentStopFlash () {
	    	nyilFentHasFlash=false;
	    }
	    
	public void nyilFentStartFlash () {
	    	nyilFentHasFlash=true;
	    	doFlash (0);
	    }
	public void nyilLentStopFlash () {
	    	nyilLentHasFlash=false;
	    }
	    
	public void nyilLentStartFlash () {
	    	nyilLentHasFlash=true;
	    	doFlash (1);
	    }
	
	   public void doFlash (int melyik) {
		//    JarmoService.JarmoLog  ("nyilFlash "+melyik);
	    	for (int t=1;t<=25; t++) {
               final int fmelyik=melyik;	    	  
	    	   final int k=t;
	    	   JarmoMain.handler.postDelayed(new Runnable () {
	    		   public void run () {
	    		//	   JarmoService.JarmoLog  ("Fmelyik ="+fmelyik+"Fent flash="+nyilFentHasFlash+" lent flash="+nyilLentHasFlash);
	    			   if (nyilFentHasFlash==false) {nyilalphaFent=0; JarmoMain.alap.invalidate(); }
	    			   if (nyilLentHasFlash==false) {nyilalphaLent=0; JarmoMain.alap.invalidate(); }

	    			   if (fmelyik==0 && nyilFentHasFlash==true) {
	    				   nyilalphaFent=255-(k*10);
	    				   JarmoMain.alap.invalidate();
	    			   } 
	    			   if (fmelyik==1 && nyilLentHasFlash==true) { 
	    				   nyilalphaLent=255-(k*10);
	    				   JarmoMain.alap.invalidate();
	    			   }
	    			//   JarmoService.JarmoLog  ("NyilAlphaLent "+nyilalphaFent);
	    		   }
	    	   }, (t*20));
	    	   JarmoMain.handler.postDelayed(new Runnable () {
	    		   public void run () {
	    			   if (nyilFentHasFlash==false) {nyilalphaFent=0; JarmoMain.alap.invalidate(); }
	    			   if (nyilLentHasFlash==false) {nyilalphaLent=0; JarmoMain.alap.invalidate(); }

	    			   if (fmelyik==0 && nyilFentHasFlash==true) {
	    				   nyilalphaFent=(k*10);
	    				   JarmoMain.alap.invalidate();
	    			   }
	    			   if (fmelyik==1 && nyilLentHasFlash==true) { 
	    				   nyilalphaLent=(k*10);
	    				   JarmoMain.alap.invalidate();
	    			   }
	    		   }
	    	   }, 500+(t*20));
	    	}//for
	    	
	    	final int fmelyik=melyik;	    	  
		    JarmoMain.handler.postDelayed(new Runnable () {
	    		public void run () {
	    		   if (fmelyik==0) {
	    		     if (nyilFentHasFlash==false) {
	    			   nyilalphaFent=0;	    			   
	    	    	   return;
	    		     } else {
	         			doFlash(0);
	    		     }
	    		   } else { //fmelyik
	    	//		   JarmoService.JarmoLog  ("nyillent flash "+nyilalphaFent);
			          if (nyilLentHasFlash==false) {
			    	    nyilalphaLent=0;	    			   
			    	    return;
         		      } else {
			         	doFlash(1);
			         }
	    		  } //fmelyik
	    		} //run
	    	}, 1000);
	    }
	 
        
    
    public void onDraw (Canvas canvas) {
      try { 	
    	p.setColor(0x77990000);
    	pker.setColor (0xFF000000);
    	pker.setStyle (Style.STROKE);
    	pker.setStrokeWidth(JarmoMain.BOGYORADIUS/3);
//    	p.setStyle(Style.STROKE);
 //   	p.setStrokeWidth(JarmoMain.BOGYORADIUS/3);
 //   	JarmoService.JarmoLog   ("Alap redraw");

      	helpTextMag=JarmoMain.RADIUS;

 //   	canvas.drawRect(JarmoMain.mainfield, p);
    	canvas.drawRect(sepField1, p);
    	canvas.drawRect(sepField2, p);
    	
    	canvas.drawRect(JarmoMain.field1, this.pHamvasit);
    	canvas.drawRect(JarmoMain.field2, this.pHamvasit);
   // 	canvas.drawRoundRect(frame, JarmoMain.BOGYORADIUS/2, JarmoMain.BOGYORADIUS/2, pker);
    //	canvas.drawRect(JarmoMain.nyilField1, p);
    //	canvas.drawRect(JarmoMain.nyilField2, p);
    	
    	
       	nyilPaint1.setStrokeWidth(2);
       	nyilPaint1.setColor(0xFFFFFFFF);
       	nyilPaint2.setStrokeWidth(2);
       	nyilPaint2.setColor(0xFFFFFFFF);
    	nyilPaint1.setAlpha(nyilalphaFent);
       	nyilPaint2.setAlpha(nyilalphaLent);
       	
       	canvas.drawRect (JarmoMain.timerField, timerPaint);
      
       	canvas.drawLine (JarmoMain.nyilField1.left, JarmoMain.nyilField1.top,
       			         JarmoMain.nyilField1.left+JarmoMain.nyilField1.width()/2, JarmoMain.nyilField1.bottom, 
       			         nyilPaint1
       			         );
       	canvas.drawLine (JarmoMain.nyilField1.left+JarmoMain.nyilField1.width()/2, JarmoMain.nyilField1.bottom, 
       			         JarmoMain.nyilField1.right, JarmoMain.nyilField1.top,
			             nyilPaint1
			             );

       	canvas.drawLine (JarmoMain.nyilField2.left, JarmoMain.nyilField2.bottom,
			             JarmoMain.nyilField2.left+JarmoMain.nyilField2.width()/2, JarmoMain.nyilField2.top,
			             nyilPaint2
			         );
    	canvas.drawLine (JarmoMain.nyilField2.left+JarmoMain.nyilField2.width()/2, JarmoMain.nyilField2.top, 
		   	         JarmoMain.nyilField2.right, JarmoMain.nyilField2.bottom,
	                  nyilPaint2
	             );
    //	canvas.drawL
    	
  //     	canvas.drawBitmap(nyilBitmap4, null, JarmoMain.nyilField1, nyilPaint1);
  //  	canvas.drawBitmap(nyilBitmap2, null, JarmoMain.nyilField2, nyilPaint2);
    	
//       	canvas.drawRect(JarmoMain.helpField, p);
         
    	//pontok
    //	pontPaint.setTextSize((int)(JarmoMain.BOGYORADIUS*1.0));
    	pontPaint.setTextSize((int)(JarmoMain.field1.height()/2));
    	pontPaint.setColor(0xFF00FF00);
    	pontPaint.setAntiAlias(true);
    	if (JarmoMain.jatekMode==1 || JarmoMain.jatekMode==2 || JarmoMain.jatekMode==6) {
    		if (JarmoMain.userLent==0) {
    			JarmoMain.fentName=JarmoSetting.js.getString(R.string.NameYou);
    			JarmoMain.lentName=JarmoSetting.js.getString(R.string.NameAndroid);
    		} else {
       			JarmoMain.fentName=JarmoSetting.js.getString(R.string.NameAndroid);
    			JarmoMain.lentName=JarmoSetting.js.getString(R.string.NameYou);
    		}
    	}
    	if (JarmoMain.jatekMode==3) {
    		if (JarmoMain.userSzine==1) {
    		if (JarmoMain.userLent==0) {
    			JarmoMain.fentName=JarmoSetting.js.getString(R.string.NameBlack);
    			JarmoMain.lentName=JarmoSetting.js.getString(R.string.NameWhite);
    		} else {
       			JarmoMain.fentName=JarmoSetting.js.getString(R.string.NameWhite);
    			JarmoMain.lentName=JarmoSetting.js.getString(R.string.NameBlack);
    		}
    		} else {
        		if (JarmoMain.userLent==0) {
        			JarmoMain.fentName=JarmoSetting.js.getString(R.string.NameWhite);
        			JarmoMain.lentName=JarmoSetting.js.getString(R.string.NameBlack);
        		} else {
           			JarmoMain.fentName=JarmoSetting.js.getString(R.string.NameBlack);
        			JarmoMain.lentName=JarmoSetting.js.getString(R.string.NameWhite);
        		}    			
    		}
    	} 
    	if (JarmoMain.jatekMode==4) {
    		if (JarmoMain.userLent==0) {
    			JarmoMain.fentName=JarmoSetting.js.getString(R.string.NameWifiYou); 
    			JarmoMain.lentName=JarmoMain.enemyId;
    		} else {
    			JarmoMain.fentName=JarmoMain.enemyId;
    			JarmoMain.lentName=JarmoSetting.js.getString(R.string.NameWifiYou);     			
    		}
    	}
    
    	pontPaint.setTypeface(JarmoSetting.tfa);
    	if (fekvo==0) { 
    		//fent
    		if (JarmoMain.userLent==0) {
    			if (JarmoMain.userSzine==0)
            	   pontPaint.setColor(0xFFFFFFFF);//FFFF0000  
    			else
    			   pontPaint.setColor(0xFF000000); //
    		} else {
    			if (JarmoMain.userSzine==0)
             	   pontPaint.setColor(0xFF000000);  
     			else
     			   pontPaint.setColor(0xFFFFFFFF); //
    		}
    		if (JarmoMain.jatekMode==3) {
    			pontPaint.setTextAlign(Paint.Align.LEFT);
    			canvas.save();
    			canvas.rotate(180, JarmoMain.field1.right, JarmoMain.field1.top+pontPaint.getTextSize()); 
    			canvas.drawText(JarmoMain.fentName.substring(0, Math.min(10, JarmoMain.fentName.length())), JarmoMain.field1.right, 
    					JarmoMain.field1.top+pontPaint.getTextSize()-(int)(JarmoMain.BOGYORADIUS*.3), pontPaint);
    	//		canvas.save();
    //			pontPaint.setColor(0xFF00FF00);
    //			canvas.rotate(180, JarmoMain.field1.right, JarmoMain.field1.top+JarmoMain.field2.height()); 
    			canvas.drawText("[ "+Integer.toString(JarmoMain.pointFent)+" ]", JarmoMain.field1.right, 
    					JarmoMain.field1.top+JarmoMain.field2.height()-(int)(JarmoMain.BOGYORADIUS*.3), pontPaint);
    		    super.onDraw(canvas); 

    		    //restore the old matrix. 
    		    canvas.restore(); 
    	//	    canvas.rotate(0,0,0);
    		} else {
    			pontPaint.setTextAlign(Paint.Align.RIGHT);
    			canvas.drawText(JarmoMain.fentName.substring(0, Math.min(10, JarmoMain.fentName.length())), JarmoMain.field1.right, 
    					JarmoMain.field1.top+(JarmoMain.field1.height()/2)-(int)(JarmoMain.BOGYORADIUS*.3), pontPaint);
  //  			pontPaint.setColor(0xFF00FF00);
    			canvas.drawText("[ "+Integer.toString(JarmoMain.pointFent)+" ]", JarmoMain.field1.right, 
    					JarmoMain.field1.top+JarmoMain.field1.height()-(int)(JarmoMain.BOGYORADIUS*.3), pontPaint);
    		}
    	//lent
    	    pontPaint.setTextAlign(Paint.Align.LEFT);    
    	    
    		if (JarmoMain.userLent==0) 
    			if (JarmoMain.userSzine==1)
            	   pontPaint.setColor(0xFFFFFFFF); //FFFF0000  
    			else
    			   pontPaint.setColor(0xFF000000); //FF0000FF
    		else
    			if (JarmoMain.userSzine==1)
             	   pontPaint.setColor(0xFF000000);  
     			else
     			   pontPaint.setColor(0xFFFFFFFF); //

    	    
    	    canvas.drawText(JarmoMain.lentName.substring (0,Math.min(10, JarmoMain.lentName.length())), JarmoMain.field2.left, 
    			JarmoMain.field2.top+pontPaint.getTextSize()-(int)(JarmoMain.BOGYORADIUS*.3), pontPaint);
        	//pontPaint.setColor(0xFFDDFFDD); //sarga:ff00ff00
    	    canvas.drawText("[ "+Integer.toString(JarmoMain.pointLent)+" ]", JarmoMain.field2.left, 
    			JarmoMain.field2.top+JarmoMain.field2.height()-(int)(JarmoMain.BOGYORADIUS*.3), pontPaint);
    	} else {
    		pontPaint.setTextAlign(Paint.Align.CENTER);
    		String tmpTxt=Integer.toString(JarmoMain.pointFent);
    		for (int t=0;t<tmpTxt.length();t++) {
    			int pos;
    			pos=(int) (JarmoMain.field1.bottom-((tmpTxt.length()-t-1)*pontPaint.getTextSize()));
       	   	    canvas.drawText(tmpTxt.toCharArray(), t, 1, JarmoMain.field1.left+JarmoMain.field1.width()/2, 
        		    	pos, pontPaint);
    		}
    		for (int t=0;t<tmpTxt.length();t++) {
    			int pos;       	  
       	   	    tmpTxt=Integer.toString(JarmoMain.pointLent);
       			pos=(int) (JarmoMain.field2.top+((t+1)*pontPaint.getTextSize()));
       	   	    canvas.drawText(tmpTxt.toCharArray(), t, 1, JarmoMain.field2.left+JarmoMain.field2.width()/2, 
        		    	pos, pontPaint);   	
    		}
    	}
    	
    	canvas.drawRect(adBlackRect,  adBlackPaint);
    	
    	//helptext
 /*      	helpTextPaint.setTextAlign(Paint.Align.CENTER);
       	helpTextPaint.setTextSize((int)(JarmoMain.helpField.height()*0.8));
       	helpTextPaint.setTypeface(tfh);
       	helpTextPaint.setColor(0xFFFFFFFF);
       	helpTextPaint.setAlpha(255-(helpTextOffset*20));
    	canvas.drawText(this.helpText, JarmoMain.helpField.width()/2, 
    			JarmoMain.helpField.top+helpTextPaint.getTextSize()+(helpTextOffset*2), helpTextPaint);
   */ 
    	//helpwindow
      } catch (Exception e) {
    	  e.printStackTrace();
	//	  if (AdBuddiz.isReadyToShowAd(JarmoSetting.js)) { // this = current Activity
	//				AdBuddiz.showAd(JarmoSetting.js);
    //      }
	
    	  JarmoMain.jarmoMain.finish();
      }
    }
    
     
    public void showText (String txt) {
    	this.helpText=txt;
    	this.helpTextOffset=JarmoMain.helpField.height(); //logjon le az elejen
    	for (int t=1; t<=JarmoMain.helpField.height();t++) {
    		
    	    final int k=t;
    	    final Alap a=this;
        	this.postDelayed(new Runnable () {
        		public void run() {
    			    a.helpTextOffset=JarmoMain.helpField.height()-k;
    			    a.invalidate();
    	    	}
    	    }, t*50);
        }
    }
}

class BogyoPosWifi extends Thread {
 
	public Bogyo bogyo=null;
	public int stop=0;
	
	public BogyoPosWifi (Bogyo b) {
		bogyo=b;
		JarmoService.JarmoLog  ("BogyoPosWifi start at "+b.mezo.id);
	}
	
	public void run () {
		if (JarmoMain.jatekMode!=4) return;
		while (stop==0) {
			int x=bogyo.px;
			int y=bogyo.py;

			x=1000*x/JarmoMain.SCREENWIDTH;
			y=1000*y/JarmoMain.SCREENHEIGHT;

//GDPR			
/*
			JarmoService.sendToSocket("BOGYO MOVE "+
					String.format("%02d", bogyo.mezo.id)+" "+
					String.format("%04d", x)+" "+
					String.format("%04d", y));
			try {	
				synchronized (JarmoService.socket) {
					JarmoService.socket.wait(100);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
*/
		}
	}
}
