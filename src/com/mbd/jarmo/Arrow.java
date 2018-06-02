package com.mbd.jarmo;

import java.io.InputStream;

import com.mbd.jarmo.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

class Arrow extends View {
	private Paint p, p2;
	private Context context;
	private Paint nyilPaint;
	public int invalidate=0;
	public double arrowRotatePointY=0.175;
	
	
   	public Arrow(Context l_context) {
   		super(l_context);
   		context=l_context;
   		
   		nyilPaint = new Paint ();
    	}
	
   	public boolean szomszedFlipped (int id) {
   		if (JarmoMain.bogyoItt(JarmoMain.mezok[id-1])==null) return false;
   		
   		if (JarmoMain.bogyoItt(JarmoMain.mezok[id-1]).arrowFlipped==1) return true;
   		else return false;
   	}
   	
   	
   	
   	public void addArrow (Bogyo b, int pont, int delay) {
   		//make 
   		
   		int width=(int)(JarmoMain.BOGYORADIUS*5);
   		int height=(int)(JarmoMain.BOGYORADIUS*3);
   		int flipped=0; //flipped=jobbrol jon be a nyil
   		
   		if (b.mezo.id % 5==1 || b.mezo.id % 5==2 || 
   		   ((b.mezo.id % 5==3) && szomszedFlipped (b.mezo.id)==true)) {   			
   			b.arrowFlipped=1;
   		} else {
   			b.arrowFlipped=0;
   		}
 
//			b.arrowRect=new Rect (b.mezo.px-width, b.mezo.py-(int)(height/4),
//   					(int)(b.mezo.px), b.mezo.py+(int)(height));

   		if (b.arrowFlipped==1)
   			b.arrowRect=new Rect (b.mezo.px+1000, b.mezo.py-(int)(height/4),
   					(int)(b.mezo.px+width)+1000, b.mezo.py+(int)(height));
   		else 
   			b.arrowRect=new Rect (b.mezo.px-width-1000, b.mezo.py-(int)(height/4),
   					(int)(b.mezo.px)-1000, b.mezo.py+(int)(height));

  	b.currentPhase=1;

		if (b.szin==0) {
			if (pont==1) {
				b.arrowPhase=JarmoSetting.arrowBitmaps[21];
				b.startArrow=21;
			} else {
				b.arrowPhase=JarmoSetting.arrowBitmaps[31];
				b.startArrow=31;
			}
		}
		if (b.szin==1) {
			if (pont==1) {
				b.arrowPhase=JarmoSetting.arrowBitmaps[1];
				b.startArrow=1;
			} else {
				b.arrowPhase=JarmoSetting.arrowBitmaps[11];
				b.startArrow=11;
			}
		} 
		for (int t=1;t<=10;t++) {
			final int tt=t;
			final Bogyo bb=b;
			final int fheight=height;
		    final int fwidth=width;
			JarmoMain.handler.postDelayed(new Runnable (){
				public void run () {
					if (tt==1) JarmoSetting.playSound(R.raw.arrow);
					
					int distx=JarmoMain.SCREENWIDTH-(int)(JarmoMain.SCREENWIDTH/10)*tt;
			   		if (bb.arrowFlipped==1)
			   			bb.arrowRect=new Rect (bb.mezo.px+distx, bb.mezo.py-(int)(fheight/4),
			   					(int)(bb.mezo.px+fwidth+distx), bb.mezo.py+(int)(fheight));
			   		else 
			   			bb.arrowRect=new Rect (bb.mezo.px-fwidth-distx, bb.mezo.py-(int)(fheight/4),
			   					(int)(bb.mezo.px)-distx, bb.mezo.py+(int)(fheight));
					JarmoMain.arrowView.invalidate();
					
				}	
			}, (delay*200)+t*10);
		}
		
		for (int t=1;t<=8;t++) {
			final int tt=t;
			final Bogyo bb=b;
			JarmoMain.handler.postDelayed(new Runnable (){
				public void run () {
					bb.arrowPhase=JarmoSetting.arrowBitmaps[bb.startArrow+tt];
					bb.invalidate();
					JarmoMain.arrowView.invalidate();
				}
			}, (delay*200)+600+(t*50));
		}
	
   	}


   	public void onDraw (Canvas canvas) {
   		if (invalidate==1) {
   			return;
   		}
   		for (int t=1;t<=5;t++) {
   			Bogyo b=JarmoMain.feketebogyo[t];
   			int upsideDown=0;
   			
   			if (JarmoMain.jatekMode==3 &&                             //feketet akkor kell forgatni, ha
   			    ((JarmoMain.userSzine==1 &&	JarmoMain.userLent==0) || //userszine fekete es fent jatszik
   			    (JarmoMain.userSzine==0 &&	JarmoMain.userLent==1))   //userszine feher de nem fent jatszik
			    ) { 
   				upsideDown=1;
   			}
   			
   			if (b.arrowRect!=null) {
   				//		JarmoService.JarmoLog  ("van nyila="+t);
   				if (b.arrowFlipped==1) {
   					canvas.save();
   					canvas.scale (-1, 1, b.arrowRect.centerX(), b.arrowRect.top);
   				}
   				canvas.drawBitmap(JarmoSetting.arrowOnly, null, b.arrowRect, nyilPaint);   				
   				if (b.arrowFlipped==1) {
   					canvas.restore();
   				}
   				if (upsideDown==1) {
   					canvas.rotate(180, b.arrowRect.centerX(), b.arrowRect.top+(int)(b.arrowRect.height()*arrowRotatePointY));
   				    canvas.drawBitmap(b.arrowPhase, null, b.arrowRect, nyilPaint);
   				    canvas.restore();
   				} else {   				
   				  canvas.drawBitmap(b.arrowPhase, null, b.arrowRect, nyilPaint);
   				}

   			}
   			
   			b=JarmoMain.feherbogyo[t];
   			upsideDown=0;
 			if (JarmoMain.jatekMode==3 &&                                 //feheret akkor kell forgatni, ha
 	   			    ((JarmoMain.userSzine==0 &&	JarmoMain.userLent==0) || //userszine feher es fent jatszik
 	   			    (JarmoMain.userSzine==1 &&	JarmoMain.userLent==1))   //userszine fekete de nem fent jatszik
 				    ) { 
 	   				upsideDown=1;
 	   			}
 	   	
   			if (b.arrowRect!=null) {
   				//		JarmoService.JarmoLog  ("van nyila="+t);
   				if (b.arrowFlipped==1) {
   					canvas.save();
   					canvas.scale (-1, 1, b.arrowRect.centerX(), b.arrowRect.top);
   				}
   				canvas.drawBitmap(JarmoSetting.arrowOnly, null, b.arrowRect, nyilPaint);   				

   				if (b.arrowFlipped==1) {
   					canvas.restore();
   				}
  				if (upsideDown==1) {
   					canvas.rotate(180, b.arrowRect.centerX(), b.arrowRect.top+(int)(b.arrowRect.height()*this.arrowRotatePointY));
   				    canvas.drawBitmap(b.arrowPhase, null, b.arrowRect, nyilPaint);
   				    canvas.restore();
   				} else {   				
   				  canvas.drawBitmap(b.arrowPhase, null, b.arrowRect, nyilPaint);
   				}

   			}
   		}
   	}
}

