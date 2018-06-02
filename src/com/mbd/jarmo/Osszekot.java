package com.mbd.jarmo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

class Osszekot extends View {
	private Mezo m1, m2;
	private int lathato=0;
	private Paint p, p2;
	int col=0xFFFFFF;
	int mezoradius1, mezoradius2;
	int thick=(int)(JarmoMain.RADIUS/5);
	int startx, starty, endx, endy;
	int percent=0;
	
	public Osszekot (Context context, Mezo mezo1, Mezo mezo2) {
			super(context);
			m1=mezo1;
			m2=mezo2;
			
			
			p = new Paint (Paint.ANTI_ALIAS_FLAG);
			p.setStrokeWidth(JarmoMain.BOGYORADIUS/10);
			p.setStrokeWidth(thick);
			p.setColor(0xFF7B4616);
			
			if ((m1.id>=1&&m1.id<=5) || (m1.id>=21&&m1.id<=25)) 
               mezoradius1=JarmoMain.RADIUS;
            else
               mezoradius1=(int)(JarmoMain.RADIUS*0.7);
			
			if ((m2.id>=1&&m2.id<=5) || (m2.id>=21&&m2.id<=25)) 
	           mezoradius2=JarmoMain.RADIUS;
	        else 
	           mezoradius2=(int)(JarmoMain.RADIUS*0.7);
			
			startx=m1.px;//+(int)((m2.px-m1.px)/mezoradius1);
			starty=m1.py;//+(int)((m2.py-m1.py)/mezoradius1);
			endx=m1.px;//-(int)((m2.px-m1.px)/mezoradius2);
			endy=m1.py;//-(int)((m2.py-m1.py)/mezoradius2);

	}
	
	public void setLathato () {
		for (int t=1; t<=20; t++) {
	        final int tt=t;
	        final Osszekot oo=this;
			JarmoMain.handler.postDelayed(new Runnable () {
				public void run () {
					
				   // int r=0x95+(((0xFF-0x95)/20)*(20-tt));
				   // int g=0x42+(((0xFF-0x42)/20)*(20-tt));
				   // int b=0x16+(((0xFF-0x16)/20)*(20-tt));
				    
				//	oo.col=(r<<16) + (g<<8) + (b);
				//	oo.thick=(JarmoMain.RADIUS/5);
					//oo.percent=tt;
					
					endx= startx+((m2.px-m1.px)*tt)/20;
					endy= starty+((m2.py-m1.py)*tt)/20;
					oo.invalidate();
				}
			}, t*15);
		  }
		  
        lathato=1;		
	}
	
    public void onDraw (Canvas canvas) {
			if (lathato==0) return;
			//p.setStyle(style);
			canvas.drawLine(startx, starty, endx, endy, p);
	//		canvas.drawLine((int)(startx+thick), (int)(starty+thick), (int)(endx+thick), (int)(endy+thick), p2);
		  }
}