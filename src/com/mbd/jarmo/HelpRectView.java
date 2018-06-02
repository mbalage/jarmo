package com.mbd.jarmo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class HelpRectView extends View {

    public static Runnable currentRunnable;
    
	private Paint pk, pb;	
    public Rect box;
    public Paint[]textPaint;
   	int width=(int)(JarmoMain.WIDTH*0.9);
	int height=(int)(JarmoMain.HEIGHT*0.5);
//	private Paint currTextPaint;
	String[] lines;
	int timeout;
	int delay;
	int isOpen=0;
	int isRotated=0;
    Context context;
    MyCallback myCallback;
    public static Handler winHandler=new Handler();
	
	public HelpRectView (Context l_context) {		
		super (l_context);
		context=l_context;
	}
	
	public static String[] convertHelp (String in) {
		String []words=in.split("[ ]");
		ArrayList<String> lines=new ArrayList<String>();
		int count=0;
		int linecount=0;
		
		lines.add("");
		while (count<words.length) {
			System.out.println ("Words "+words[count]);
			if ((lines.get(linecount)+words[count]).length() < 30 && (words[count].indexOf('%')==-1)) { // max sor length
				lines.set(linecount, lines.get(linecount) + words[count] + " ");
			} else { //nem fer el, uj sor
				words[count]=words[count].replace('%', ' ');
				linecount++;
				lines.add(words[count]+" ");
			}
		//	JarmoService.JarmoLog  (linecount+" "+lines[linecount]);
			count++;
		}
		if (lines.size()>6) {
			lines.add(0, "");
			lines.add("");
		}
		return (String[])lines.toArray(new String[0]);
	}
	
    public void showHelpRectView (String l_lines, int l_timeout, int widthPercent, int l_delay, MyCallback l_m, int rotated) 
    {
//		super(context);
		lines = HelpRectView.convertHelp(l_lines);
		delay=l_delay;
		myCallback=l_m;
		isRotated=rotated;
		height=(int)((((lines.length)*JarmoMain.BOGYORADIUS)+(JarmoMain.BOGYORADIUS*3))*1.1);
		width=(int)JarmoMain.WIDTH*widthPercent/100-(JarmoMain.BOGYORADIUS/2);
		JarmoService.JarmoLog  ("HELPRECT height ="+lines.length*JarmoMain.BOGYORADIUS);
		timeout=l_timeout;
    	textPaint=new Paint[lines.length];
//		JarmoMain.helpRectView=this;
		pk=new Paint ();
		pb=new Paint ();
		pk.setColor(0x00FFFFFF);
		pk.setStyle(Style.STROKE);
		pk.setStrokeWidth((int)(JarmoMain.BOGYORADIUS/3));
		pb.setColor(0x00333333);
		pb.setStyle(Style.FILL);
		box=new Rect ((int)((JarmoMain.SCREENWIDTH/2)-((width/2)/(50))),
					(int)((JarmoMain.SCREENHEIGHT/2)-((height/2)/(50))),
					(int)((JarmoMain.SCREENWIDTH/2)+((width/2)/(50))),
					(int)((JarmoMain.SCREENHEIGHT/2)+((height/2)/(50))));
		
		for (int t=0;t<textPaint.length;t++) {
			textPaint[t]=new Paint ();
			textPaint[t].setColor(0xFFFFFFFF);
			textPaint[t].setAlpha(0);
			textPaint[t].setTextSize((int)(JarmoMain.BOGYORADIUS*1.5));
			textPaint[t].setTextAlign(Align.CENTER);
		  	textPaint[t].setTypeface(JarmoSetting.tf);   			    
		}
		
		for (int t=1;t<=50;t++) {
			final int tt=t;
			final Paint fpk=pk;
			final Paint fpb=pb;
			JarmoMain.handler.postDelayed(new Runnable () {
				public void run () { 	

//					JarmoService.JarmoLog  ("Expires "+tt);
					float xunit=(float)width/100;
					float yunit=(float)height/100;
	//				JarmoService.JarmoLog  ("height"+height+ " y"+yunit);
					box.set ((int)((JarmoMain.SCREENWIDTH/2)-(xunit*tt)),
							 (int)((JarmoMain.SCREENHEIGHT/2)-(yunit*tt)),
							 (int)((JarmoMain.SCREENWIDTH/2)+(xunit*tt)),
							 (int)((JarmoMain.SCREENHEIGHT/2)+(yunit*tt)));
					fpk.setAlpha(tt*5);
					fpb.setAlpha(tt*4);
					JarmoMain.hrv.invalidate();
				}
			}, delay+(t*10));
		}
		
		for (int t=0;t<lines.length;t++) {
			final int tt=t, utsosor=lines.length;
			for (int i=1;i<=25;i++) {
				final int ii=i;
				final Paint currTextPaint=textPaint[t];
			    JarmoMain.handler.postDelayed(new Runnable () {
			    	public void run () {
			    		currTextPaint.setAlpha(ii*10);
			    		JarmoMain.hrv.invalidate();
						if (tt==utsosor-1 && ii==25) {
						//	System.out.println ("!!!ISopen lehet mostmar 1");
							JarmoMain.hrv.isOpen=1;
						}

			    	}
			    }, delay+500+(t*100)+(i*10));
			} 
		}
        

		final HelpRectView f_helpRectView=this;
		JarmoService.JarmoLog  ("======ablak nyit, varas lesz "+delay+" "+timeout);
		HelpRectView.winHandler.postDelayed(new Runnable () {
			public void Runnable () {
				//super();
				HelpRectView.currentRunnable=this;
			}
			public void run() {
				JarmoService.JarmoLog  ("Timer lejart, ablak bezar");
				if (isOpen==0) return;
				f_helpRectView.delWin();
			}
		}, delay+timeout);
//		JarmoMain.ed.registerView(this);
	}
	
	
	public void delWin () {
//		if (isClosed==1) return;
	//	JarmoMain.ed.unregisterView(this);
		JarmoService.JarmoLog  ("delwin starts");
		isOpen=0;
		for (int t=0;t<lines.length;t++) {
			JarmoService.JarmoLog  ("sor eltuntet");
			for (int i=1;i<=25;i++) {
			//	final int tt=t;
				final int ii=i;
				final Paint currTextPaint=textPaint[t];
			    JarmoMain.handler.postDelayed(new Runnable () {
			    	public void run () {
			    		currTextPaint.setAlpha((25-ii)*10);
			    		JarmoMain.hrv.invalidate();
			    	}
			    }, (t*100)+(i*10));
			}   				
		} 
		
		
			for (int t=1;t<=50;t++) {
			final int tt=t;
			final Paint fpk=pk;
			final Paint fpb=pb;
			JarmoMain.handler.postDelayed(new Runnable () {
				public void run () { 	

//					JarmoService.JarmoLog  ("Expires "+tt);
					float xunit=(float)width/100;
					float yunit=(float)height/100;
					int ttt=50-tt;
					box.set ((int)((JarmoMain.SCREENWIDTH/2)-(xunit*ttt)),
							 (int)((JarmoMain.SCREENHEIGHT/2)-(yunit*ttt)),
							 (int)((JarmoMain.SCREENWIDTH/2)+(xunit*ttt)),
							 (int)((JarmoMain.SCREENHEIGHT/2)+(yunit*ttt)));
					fpk.setAlpha(ttt*5);
					fpb.setAlpha(ttt*4);
                 
					JarmoMain.hrv.invalidate();
					if (tt==50) {
						JarmoService.JarmoLog  ("Most helprectView nullaz");
						lines=null;
						if (myCallback!=null) myCallback.doStuff();
					}
					
				}
			}, (lines.length*100)+(t*10)+400);
		}
	}
	
	public void onDraw (Canvas canvas) {
		if (lines==null) return;
		if (isRotated==1) {
    		canvas.save();
    		canvas.rotate(180, JarmoMain.SCREENWIDTH/2, JarmoMain.SCREENHEIGHT/2);
		}
// 			JarmoService.JarmoLog  ("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW "+box.top+" "+box.bottom);
		canvas.drawRect(box, pk);
		canvas.drawRect(box, pb);
	//	canvas.drawRect(0,0,100,100,pk);
		for (int t=0;t<lines.length;t++) {
			if (lines[t]==null) break;
		//	JarmoService.JarmoLog  ("kirak"+t);
			int perline = (int)(JarmoMain.BOGYORADIUS*1.5);
			int total = perline*lines.length;
		    canvas.drawText(lines[t], 
		    		        (int)(JarmoMain.WIDTH/2), 
		    		        (int)((JarmoMain.HEIGHT/2)-(total/2)+(t*perline)+JarmoMain.BOGYORADIUS), 
		    		        textPaint[t]);
		}

	}


	public boolean onTouchEvent (MotionEvent event) {
		if (isOpen==0) return false;
		isOpen=0;
		HelpRectView.winHandler.removeCallbacksAndMessages(null);
		JarmoService.JarmoLog  ("gyari touchevent");
		this.delWin();
		return false;
	}



}
