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

public class TableRectView extends View {

    public static Runnable currentRunnable;
    
	private Paint pk, pb;	
    public Rect box, kboxcel, kbox;
    public Paint[]textPaint1, textPaint2;
    public Paint textPaint, kboxPaint; //cimnek
    public Paint  textPaint3;;
   	int width=(int)(JarmoMain.WIDTH*0.9);
	int height=(int)(JarmoMain.HEIGHT*0.5);
//	private Paint currTextPaint;
	String[] lines1, lines2;
	String title;
	int linecount;
	int valaszto;
	int timeout;
	int delay;
	int isOpen=0;
	int isRotated=0;
	int perline; //px
	int total; //px
	int eltolas;
	String bottomline;

    Context context;
    MyCallback myCallback;
    public static Handler winHandler=new Handler();
	
	public TableRectView (Context l_context) {		
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
			if ((lines.get(linecount)+words[count]).length() < 30) { // max sor length
				lines.set(linecount, lines.get(linecount) + words[count] + " ");
			} else { //nem fer el, uj sor
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
	
    public void showTableRectView (String l_title, String[] l_lines1, String[] l_lines2, String l_bottomline, int count, int valaszto, int l_timeout, int widthPercent, int l_delay, MyCallback l_m, int l_eltolas) 
    {
//		super(context);
		lines1 = l_lines1;
		lines2 = l_lines2;
		title=l_title;
		bottomline=l_bottomline;
		linecount=count+1;
		delay=l_delay;
		eltolas=l_eltolas;
		myCallback=l_m;
		isRotated=0;
		height=(int)((((count+3)*JarmoMain.BOGYORADIUS)+(JarmoMain.BOGYORADIUS*3))*1.1);
		width=(int)JarmoMain.WIDTH*widthPercent/100-(JarmoMain.BOGYORADIUS/2);
		JarmoService.JarmoLog  ("TABLERECT height ="+linecount*JarmoMain.BOGYORADIUS);
		timeout=l_timeout;
		textPaint=new Paint();
		kboxPaint=new Paint ();
    	textPaint1=new Paint[count];
     	textPaint2=new Paint[count];
		pk=new Paint ();
		pb=new Paint ();
		pk.setColor(0x00FFFFFF);
		pk.setStyle(Style.STROKE);
		pk.setStrokeWidth((int)(JarmoMain.BOGYORADIUS/3));
		pb.setColor(0x00333333);
		pb.setStyle(Style.FILL);
		
		perline = (int)(JarmoMain.BOGYORADIUS*1.5);
		total = perline*(linecount+3);

		box=new Rect ((int)((JarmoMain.SCREENWIDTH/2)-((width/2)/(50))),
					(int)((JarmoMain.SCREENHEIGHT/2)-((height/2)/(50))),
					(int)((JarmoMain.SCREENWIDTH/2)+((width/2)/(50))),
					(int)((JarmoMain.SCREENHEIGHT/2)+((height/2)/(50))));
		kboxcel=new Rect ((int)((JarmoMain.WIDTH/2)-(width/2)),
				       (int)((JarmoMain.HEIGHT/2)-(total/2)+((valaszto+1)*perline)+(JarmoMain.BOGYORADIUS*1.5)),
				       (int)((JarmoMain.WIDTH/2)+(width/2)),
				       (int)((JarmoMain.HEIGHT/2)-(total/2)+((valaszto+2)*perline)+(JarmoMain.BOGYORADIUS*1.5))
				       );
	    kbox = new Rect ();   
		kboxPaint.setColor(0x88FFFFFF);

		//title paintje
		textPaint.setColor(0xFFFFCCAA);
		textPaint.setAlpha(0);
		textPaint.setTextSize((int)(JarmoMain.BOGYORADIUS*1.3));
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setFakeBoldText(true);
	  	textPaint.setTypeface(JarmoSetting.tf); 
	  	textPaint3=new Paint (textPaint);
	  	
	//	textPaint.setUnderlineText(true);
		
		for (int t=0;t<count;t++) {
			textPaint1[t]=new Paint ();
			textPaint1[t].setColor(0xFFFFFFFF);
			textPaint1[t].setAlpha(0);
			textPaint1[t].setTextSize((int)(JarmoMain.BOGYORADIUS*1));
			textPaint1[t].setTextAlign(Align.RIGHT);
		  	textPaint1[t].setTypeface(JarmoSetting.tfa); 
		  	
			textPaint2[t]=new Paint ();
		    textPaint2[t].setColor(0xFFFFFFFF);
			textPaint2[t].setAlpha(0);
			textPaint2[t].setTextSize((int)(JarmoMain.BOGYORADIUS*1));
			textPaint2[t].setTextAlign(Align.LEFT);
		  	textPaint2[t].setTypeface(JarmoSetting.tfa);   			    
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
					JarmoMain.trv.invalidate();
				}
			}, delay+(t*10));
		}
		
		//bal oszlop
		for (int t=0;t<count;t++) {
			final int tt=t;
			for (int i=1;i<=25;i++) {
				final int ii=i;
				final Paint currTextPaint=textPaint1[t];
				final Paint headTextPaint=textPaint;
			    JarmoMain.handler.postDelayed(new Runnable () {
			    	public void run () {
			    		if (tt==0) { //title-t is egyuttal
			    		  headTextPaint.setAlpha(ii*10);
			    		  JarmoMain.trv.invalidate();
			    		}
			    		currTextPaint.setAlpha(ii*10);
			    		JarmoMain.trv.invalidate();
				//		if (tt==utsosor-1 && ii==25) {
				//		//	System.out.println ("!!!ISopen lehet mostmar 1");
				//			JarmoMain.trv.isOpen=1;
				//		}

			    	}
			    }, delay+500+(t*100)+(i*10));
			} 
		}
        
		//jobb oszlop
		for (int t=0;t<count;t++) {
			final int tt=t;
			final int ccount=count;
			for (int i=1;i<=25;i++) {
				final int ii=i;
			//	System.out.println ("t ="+t);
				final Paint currTextPaint=textPaint2[t];
				final Paint ttextPaint3=textPaint3;
			    JarmoMain.handler.postDelayed(new Runnable () {
			    	public void run () {
			    		currTextPaint.setAlpha(ii*10);
			    		if (tt==ccount-1) { //bottomline
			    			ttextPaint3.setAlpha(ii*10);			    			
			    		}
			    		JarmoMain.trv.invalidate();
					
			    	}
			    }, delay+500+(linecount*50)+(t*100)+(i*10));
			} 
		}
		//fehercsik es kipirosodik es a nem aktivak beszurkulnek
		for (int i=1;i<=25;i++) {
			final int ii=i;
			final int vvalaszto=valaszto;
			final int llinecount=linecount;
			final Rect kkbox=kbox;
			final Paint kivalasztottPaint1=textPaint1[valaszto];
			final Paint kivalasztottPaint2=textPaint2[valaszto];
			final Paint[] ttextPaint1 = textPaint1;
			final Paint[] ttextPaint2 = textPaint2;
			JarmoMain.handler.postDelayed(new Runnable () {
		    	public void run () {
			      kkbox.set (kboxcel.left,kboxcel.top, (int)((kboxcel.right*ii)/25), kboxcel.bottom);
	   //           System.out.println ("kkbox="+kkbox.width());
			      kivalasztottPaint1.setColor(0xFFFFFFFF-(ii*10));
			      kivalasztottPaint2.setColor(0xFFFFFFFF-(ii*10));
			      for (int j=vvalaszto+1; j<llinecount-1; j++) {
			//    	  System.out.println (j);
			    	  ttextPaint2 [j].setColor(0xFFAAAAAA);
			    	  ttextPaint1 [j].setColor(0xFFAAAAAA);
			      }
			      if (ii==25) {
	//		 		System.out.println ("!!!ISopen lehet mostmar 1");
					JarmoMain.trv.isOpen=1;
				  }

			      JarmoMain.trv.invalidate();
		    	}
			}, delay+500+(linecount*150)+(i*15) );
		}
		
		final TableRectView f_tableRectView=this;
		JarmoService.JarmoLog  ("======ablak nyit, varas lesz "+delay+" "+timeout);
		TableRectView.winHandler.postDelayed(new Runnable () {
			public void Runnable () {
				//super();
				TableRectView.currentRunnable=this;
			}
			public void run() {
				JarmoService.JarmoLog  ("Timer lejart, ablak bezar");
				if (isOpen==0) return;
				f_tableRectView.delWin();
			}
		}, delay+timeout);
//		JarmoMain.ed.registerView(this);
	}
	
 	
	public void delWin () {
//		if (isClosed==1) return;
	//	JarmoMain.ed.unregisterView(this);
		JarmoService.JarmoLog  ("delwin starts");
		isOpen=0;
		kboxPaint.setAlpha(0);
		for (int t=0;t<linecount-1;t++) {
			JarmoService.JarmoLog  ("sor eltuntet");
			for (int i=1;i<=25;i++) {
			//	final int tt=t;
				final int ii=i;
				final int tt=t;
				final Paint currTextPaint1=textPaint1[t];
				final Paint currTextPaint2=textPaint2[t];
				final Paint headTextPaint=textPaint;
				final Paint bottomTextPaint=textPaint3;
			    JarmoMain.handler.postDelayed(new Runnable () {
			    	public void run () {
			    		if (tt==0) { //title-t is egyuttal
				    		  headTextPaint.setAlpha((25-ii)*10);
				    		  bottomTextPaint.setAlpha((25-ii)*10);
						   	  JarmoMain.trv.invalidate();
				        }

			    		currTextPaint1.setAlpha((25-ii)*10);
			    		JarmoMain.trv.invalidate();
			    		currTextPaint2.setAlpha((25-ii)*10);
			    		JarmoMain.trv.invalidate();

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
                 
					JarmoMain.trv.invalidate();
					if (tt==50) {
						JarmoService.JarmoLog  ("Most tablerectView nullaz");
						lines1=null;
						lines2=null;
						if (myCallback!=null) myCallback.doStuff();
					}
					
				}
			}, (linecount*100)+(t*10)+00);
		}
	}
	
	public void onDraw (Canvas canvas) {
	//	System.out.println ("TRV OnDraw ");
		if (lines1==null) return;
		if (lines1==null) return;
		if (isRotated==1) {
    		canvas.save();
    		canvas.rotate(180, JarmoMain.SCREENWIDTH/2, JarmoMain.SCREENHEIGHT/2);
		}
// 			JarmoService.JarmoLog  ("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW "+box.top+" "+box.bottom);
		canvas.drawRect(box, pk);
		canvas.drawRect(box, pb);
		
		canvas.drawRect(kbox, kboxPaint);
	//	System.out.println ("kbox "+kbox.toString());
	//	canvas.drawRect(0,0,100,100,pk);
		for (int t=0;t<=(linecount+3);t++) {
			
		//	JarmoService.JarmoLog  ("kirak"+t);
			if (t==0) {
				canvas.drawText("", 
	    		        (int)(JarmoMain.WIDTH/2), 
	    		        (int)((JarmoMain.HEIGHT/2)-(total/2)+(t*perline)+JarmoMain.BOGYORADIUS), 
	    		        textPaint);	
				continue;
			}
			if (t==1) { //title
				canvas.drawText(title, 
	    		        (int)(JarmoMain.WIDTH/2), 
	    		        (int)((JarmoMain.HEIGHT/2)-(total/2)+(t*perline)+JarmoMain.BOGYORADIUS), 
	    		        textPaint);	
				continue;
			}
	//		System.out.println ("table sorkiir "+t+"linecount "+linecount);
			if (t==(linecount+1)) { //utsouressor
	//			System.out.println ("Utolso sort kiir");
				canvas.drawText(bottomline, 
	    		        (int)(JarmoMain.WIDTH/2), 
	    		        (int)((JarmoMain.HEIGHT/2)-(total/2)+(t*perline)+JarmoMain.BOGYORADIUS), 
	    		        textPaint3);	
				continue;
			}
			
			//minden egyeb sor
			if (lines1[t-2]==null) break;
		    canvas.drawText(lines1[t-2], 
		    		        (int)(JarmoMain.WIDTH/2)+eltolas, 
		    		        (int)((JarmoMain.HEIGHT/2)-(total/2)+(t*perline)+JarmoMain.BOGYORADIUS), 
		    		        textPaint1[t-2]);
//		    System.out.println ("Sor kiir="+(t-2));
		    canvas.drawText(lines2[t-2], 
    		        (int)(JarmoMain.WIDTH/2)+(JarmoMain.BOGYORADIUS/4)+eltolas, 
    		        (int)((JarmoMain.HEIGHT/2)-(total/2)+(t*perline)+JarmoMain.BOGYORADIUS), 
    		        textPaint2[t-2]);
		}

	}


	public boolean onTouchEvent (MotionEvent event) {
		if (isOpen==0) return false;
		isOpen=0;
		TableRectView.winHandler.removeCallbacksAndMessages(null);
		JarmoService.JarmoLog  ("gyari touchevent");
		this.delWin();
		return false;
	}



}
