package com.mbd.jarmo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

class Mezo extends View implements OwnMotionView {

	private Paint p, p2, p3, p4;
	
	public int id;
	//private int allapot=0;
	public int px, py;
	public int isActive;
	public Mezo []szomszedok = new Mezo[20];
	public int lathato=0;
	public int isCrossed=0, crossWidth=JarmoMain.RADIUS/3;
	private int percent1, percent2;
	
  public Mezo (Context context, int l_id, int l_x, int l_y) {
	super(context);
	id=l_id;
	px=l_x;
	py=l_y;
	
	//this.setTranslationX(l_x-JarmoMain.RADIUS); 
	//this.setTranslationY(l_y-JarmoMain.RADIUS);
	
	
	isActive=1;
	
	p = new Paint (Paint.ANTI_ALIAS_FLAG);
	p2 = new Paint (Paint.ANTI_ALIAS_FLAG);
	p3 = new Paint (Paint.ANTI_ALIAS_FLAG);
	p4 = new Paint (Paint.ANTI_ALIAS_FLAG);
	p4.setColor(0xFF000000);
	p4.setStrokeWidth(crossWidth);
	p4.setStrokeCap(Cap.BUTT);
	//st=new Style ();
  }
  


  
  public void onDraw (Canvas canvas) {
		if (isActive==1) {
			   p.setColor(0xFF000000); //FF853216
			//   p2.setColor(0xFF6B4015); //FF853216
			   p2.setStyle(Paint.Style.FILL); 
			   p2.setShader(JarmoSetting.mezoShader);
	    	   p3.setColor(0xFFFFFFFF);
            } else {
			   p3.setColor(0xFFFFFFFF); //FFAB5411
//			   p2.setColor(0xFFCB7015); //FF853216
			   p2.setShader(JarmoSetting.mezoShader);
			   p.setColor(0xff000000); //FFAB5411
			}

		this.layout(px-JarmoMain.RADIUS,py-JarmoMain.RADIUS,px+JarmoMain.RADIUS*2, py+JarmoMain.RADIUS*2);
	  if(lathato==0) return;
		
	if ((id>=1&&id<=5) || (id>=21&&id<=25)) {
	   canvas.drawCircle((int)(JarmoMain.RADIUS*1.), JarmoMain.RADIUS, JarmoMain.RADIUS, p);	
	   canvas.drawCircle((int)(JarmoMain.RADIUS*1.2), JarmoMain.RADIUS, JarmoMain.RADIUS, p3);	
	   canvas.drawCircle((int)(JarmoMain.RADIUS*1.1), JarmoMain.RADIUS, JarmoMain.RADIUS, p2);	
	} else {
	   canvas.drawCircle((int)(JarmoMain.RADIUS*1), JarmoMain.RADIUS, (int)(JarmoMain.RADIUS*0.8), p);			
	   canvas.drawCircle((int)(JarmoMain.RADIUS*1.2), JarmoMain.RADIUS, (int)(JarmoMain.RADIUS*0.8), p3);			
	   canvas.drawCircle((int)(JarmoMain.RADIUS*1.1), JarmoMain.RADIUS, (int)(JarmoMain.RADIUS*0.8), p2);			
	}
	
	if (this.isCrossed==1) {
		if (percent1>0) canvas.drawLine(crossWidth, crossWidth, (JarmoMain.RADIUS*2*percent1/100)-crossWidth, (JarmoMain.RADIUS*2*percent1/100)-crossWidth, p4);
		if (percent2>0) canvas.drawLine((JarmoMain.RADIUS*2)-crossWidth, crossWidth, (((JarmoMain.RADIUS*2) )-(((JarmoMain.RADIUS*2)-crossWidth ))*percent2/100), ((((JarmoMain.RADIUS*2)-crossWidth)*percent2)/100), p4);
	}
  }
  
  public void markCross () {
	isCrossed=1; 
	percent1=0;
	percent2=0;
	for (int t=1;t<=20;t++) {
		final int tt=t;
		final Mezo mm=this;
		JarmoMain.handler.postDelayed(new Runnable () {
			public void run () {
				percent1=tt*5;
				mm.invalidate();
				
			}
		}, t*10);
	}
	for (int t=1;t<=20;t++) {
		final int tt=t;
		final Mezo mm=this;
		JarmoMain.handler.postDelayed(new Runnable () {
			public void run () {
				percent2=tt*5;
				mm.invalidate();
			}
		}, 200+t*10);
	}
  }

@Override
public void myTouchEvent(MotionEvent event) {
	
	// TODO Auto-generated method stub
	JarmoService.JarmoLog  ("Mezo klikk, id "+id);
    }

@Override
public Rect getClickSurface() {
	return new Rect (
			(int)(this.px-JarmoMain.RADIUS*4), //was: 3
			(int)(this.py-JarmoMain.RADIUS*4), 
			(int)(this.px+JarmoMain.RADIUS*4), 
			(int)(this.py+JarmoMain.RADIUS*4)
			);
}

@Override
public void myDragEvent(MotionEvent event, View target) {
	// TODO Auto-generated method stub	
}
 
}