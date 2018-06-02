package com.mbd.jarmo;

import java.io.InputStream;
import java.util.Vector;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

class Bogyo extends View implements OwnMotionView {
	public int szin=0;
	public Mezo mezo;
	public Vector<Mezo> mezoHist;
	public int allapot=0; //0=normal; 1=szuperuser
	public int lathato=0;
	public int px;
	public int py;
	private int erintheto=0;
	public int mozoge=0;
	public int alpha=255;
	public boolean hasFlash;
	public int flashcol=0;
	private Handler moveTimerHandler;
	
	public Paint p, p2, p3, p_burok;
	private Context context;
	private Rect imageRect;
	AssetManager assetManager;
	int strokewidth;
	public Rect arrowRect;
	public Bitmap arrowPhase;
	public int currentPhase, startArrow;
	public int arrowFlipped;
	public int kulsoforog=0;
	public int kulsoforogdegree=0;
	private int noAnimPlaying=0;
	public boolean isParkolt = false;
	
	
    public Bogyo (Context l_context, Mezo startmezo, int l_szin) {
    	super(l_context);
    	context=l_context;
    	szin=l_szin;
    	
    	strokewidth=(int)(JarmoMain.BOGYORADIUS/6);	
    	moveTimerHandler = new Handler();

  //  	maskPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
  //  	maskPaint.setStrokeWidth(strokewidth);
  //  	maskPaint.setColor(Color.BLACK);
  //  	maskPaint.setStyle(Style.STROKE);

  //  	gradientPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
  //  	gradientPaint.setStyle(Style.FILL);
   		
    	allapot=0;
    	this.mezo=startmezo;
    	px=mezo.px;
    	py=mezo.py;
    	mezoHist = new Vector<Mezo> ();
    	//this.setTranslationX((float) (mezo.px-JarmoMain.RADIUS*1.5));
    	//this.setTranslationY((float) (mezo.py-JarmoMain.RADIUS*1.5));
        imageRect = new Rect (strokewidth*3, strokewidth*3, (JarmoMain.BOGYORADIUS*2)+strokewidth, (JarmoMain.BOGYORADIUS*2)+strokewidth);
	

    	
    	p = new Paint (Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Style.FILL);
    	
    	p2 = new Paint (Paint.ANTI_ALIAS_FLAG);
        p_burok = new Paint (Paint.ANTI_ALIAS_FLAG);
    	p2.setStyle(Paint.Style.STROKE);
    	p_burok.setStyle(Paint.Style.STROKE);
   // 	p2.setColor(0x77777777);
    	p2.setAlpha(alpha-100);
    	p2.setAntiAlias(true);
    	p_burok.setAntiAlias(true);
    	p2.setStrokeWidth(strokewidth);
    	p_burok.setStrokeWidth(strokewidth);
 //   	p2.setTextSize(JarmoMain.BOGYORADIUS);
    	Shader linearGradientShader = new LinearGradient (0, 0, JarmoMain.BOGYORADIUS+strokewidth, 0, Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR);
   		p2.setShader(linearGradientShader);
   	  	Shader linearGradientShaderKulso = new LinearGradient (0, 0, JarmoMain.BOGYORADIUS*2+strokewidth, 0, Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR);
   		p_burok.setShader(linearGradientShaderKulso);
 
   		p3 = new Paint (Paint.ANTI_ALIAS_FLAG);
   		p3.setStyle(Paint.Style.FILL);
   		p3.setColor(0x33000000);
   		p3.setMaskFilter(new BlurMaskFilter((int)(JarmoMain.RADIUS), BlurMaskFilter.Blur.NORMAL));


    }
    
    public void setErintheto (int l_erintheto) {
    	erintheto=l_erintheto;
    	if (erintheto==1) {
   // 	  System.out.println ("Indit");	
    	  this.kulsoforog=1;
    	  this.kulsoforogdegree=0;
    	  this.kulsoForgat();
    	} else {
    		this.kulsoforog=0;
         	this.kulsoforogdegree=0;
    	}
    }
    
    public void stopKulsoForgat () {
    	this.kulsoforog=0;
    }

    public void updateForgat () {
    	if (erintheto==0) return;
    //	//System.out.println ("updateForgat, most szoge="+this.kulsoforogdegree);
    //	if (this.kulsoforogdegree!=0) return;
    	this.kulsoForgat();
    }
    
    public void kulsoForgat () {
    	for (int t=1;t<=360;t=t+5) {
    		final Bogyo bb=this;
    		final int tt=t;
    		JarmoMain.handler.postDelayed(new Runnable () {
    			public void run () {
    				if (bb.kulsoforog==0) {
    					bb.kulsoforogdegree=0;
    					return;
    				}
    			//	//System.out.println ("tt"+tt);
    				if (tt>=355 && bb.kulsoforogdegree >= 348) {
    	//   				System.out.println ("ujraindulaskor ="+bb.kulsoforogdegree);
       					bb.kulsoForgat();
       					return;
    				}
    				bb.kulsoforogdegree=tt;
    				bb.invalidate();
    			}
    		}, t*4);
    	}
    }
    
    public void posToMezo () {
    	px=mezo.px;
    	py=mezo.py;
    	this.invalidate();
    }
    
    public void slideToPos (Mezo target,  int step, int ms, Bogyo targetBogyo) {
        int start_x=px;
        int start_y=py;
        int target_x=target.px;
        int target_y=target.py;
        
        JarmoService.JarmoLog("slideToPos");
      //  mozoge=1;
        
        final Bogyo tb=targetBogyo;
        if (targetBogyo!=null) {
        	    targetBogyo.setErintheto(0);
        	    JarmoService.JarmoLog  ("slideToPos, van bogyo a celban");
            	
        	    int radius = Math.round (JarmoMain.BOGYORADIUS)+this.strokewidth;
            	
       	        if (start_x > target_x) target_x+=(radius*1.3);
       	        else target_x-=(radius*1.3);
       	        if (start_y > target_y) target_y+=(radius*1.3);
       	        else target_y-=(radius*1.3);
             	        
        } else {// nincs target bogyo, mezoig csuszhat
        	
        }
        
        this.mezo=target;
        int dist= (int)Math.sqrt( ((target_x-start_x)*(target_x-start_x))+
        		                  ((target_y-start_y)*(target_y-start_y)) );
        
   //     step=(int)(dist/3);
   //     ms=(int)(80/step);
   //     ms=ms*(dist/JarmoMain.BOGYORADIUS);
        if (dist<JarmoMain.BOGYORADIUS*3)
        	step=step/3;
        
        double mod=0;
        int time=0;
    	for (int j=0; j<=step; j++) {
    		if (mod<(ms-2)) mod=mod+.1;
    		time=time+ms-(int)Math.round(mod);
    	//	if (j==step) mod=0;
			final int currX=start_x+(target_x-start_x)*j/step;
			final int currY=start_y+(target_y-start_y)*j/step;
		
			final Bogyo s=this;
			final int visszarak;
			final int jj=j, sstep=step, mms=ms;
			if (step==j) visszarak=1;
			else visszarak=0;
		    JarmoMain.handler.postDelayed (new Runnable() {
		        public void run () {
		        //	//System.out.println ("Mozgasban epp lejar="+(int)(mmod) );
		        	if (visszarak==1) s.mozoge=0; 
		        	else s.mozoge=1;
		        	
		        	s.px=currX;
		        	s.py=currY;
		        	s.invalidate();
		        	if (jj==sstep) { //utso megmozdulas
		        		if (tb!=null) JarmoMain.utkozes (s, tb); //ha van targetbogyo, itt fogunk utkozni
		        	}
		        	
		        }		        
		    }, time  );
	//	    //System.out.println ("time "+time);
       }
    }
    
    public void stopFlash () {
    	hasFlash=false;
    }
    
    public void startFlash () {
    	hasFlash=true;
    	doFlash ();
    }
    
    public void updateFlash () {
       	if (hasFlash) doFlash ();           	
    }
    
    public void doFlash () {
    	for (int t=1;t<=25; t++) {
    	   final Bogyo melyik=this;
    	   final int k=t;
    	   JarmoMain.handler.postDelayed(new Runnable () {
    		   public void run () {
    			   melyik.alpha=255-(k*10);
    			   melyik.invalidate();
    		   }
    	   }, (t*20));
    	   JarmoMain.handler.postDelayed(new Runnable () {
    		   public void run () {
    			   melyik.alpha=(k*10);
    			   melyik.invalidate();
    		   }
    	   }, 500+(t*20));
    	}//for
 	   final Bogyo melyik=this;
 	   JarmoMain.handler.postDelayed(new Runnable () {
    		public void run () {
    		   if (melyik.hasFlash==false) {
    		   	  melyik.alpha=255;
    	    	  return;
    		   } else {
         			melyik.doFlash();
    		   }
    		}
    	}, 1000);
    }
    
    public void doMegmutat () {
    	JarmoService.JarmoLog  ("megmutat");
     	for (int t=1;t<=25; t++) {
     	   final Bogyo melyik=this;
     	   final int k=t;
     	   JarmoMain.handler.postDelayed(new Runnable () {
     		   public void run () {
     			   melyik.flashcol = 250-(k*10);
     			   melyik.invalidate();
     		   }
     	   }, (t*20));
     	}//for
  	 	
    }
   
    public void onDraw (Canvas canvas) {
    	
      // (this.mBitmapFeher.isRecycled() || this.mBitmapSzurke.isRecycled()) return;
    	
  	   this.layout(px-JarmoMain.BOGYORADIUS-(strokewidth*2),
	    		   py-JarmoMain.BOGYORADIUS-(strokewidth*2),
	    		   px+JarmoMain.BOGYORADIUS*3,
   		           py+JarmoMain.BOGYORADIUS*3);
       if (lathato==0) return;
       
      	int radius=(int) Math.round (JarmoMain.BOGYORADIUS);
      	        
//        JarmoService.JarmoLog  ("flashcol "+flashcol);
      	if (mozoge==1) {
      		  radius=(int)(radius*1.2);
     	      imageRect.set(strokewidth*3, strokewidth*3, (int)(JarmoMain.BOGYORADIUS*2.4)+strokewidth, (int)(JarmoMain.BOGYORADIUS*2.4)+strokewidth);
        	  canvas.drawCircle((int)(radius*2), (int)(radius*2), (int)radius+strokewidth, p3);	
        }  else {
        	   imageRect.set(strokewidth*3, strokewidth*3, (int)(JarmoMain.BOGYORADIUS*2)+strokewidth, (int)(JarmoMain.BOGYORADIUS*2)+strokewidth);
        }
      
    	if (szin==0)
    	  p.setColor(0xFFFFFFFF+(flashcol<<16)+flashcol); //FFFF0000
    	else
    	  p.setColor(0xFF000000+(flashcol<<24)+(flashcol<<16));
       	p.setAlpha(alpha);
    	
   
    	canvas.drawCircle(radius+strokewidth*2, radius+strokewidth*2, radius, p);
       	canvas.drawCircle(radius+strokewidth*2, radius+strokewidth*2, radius+strokewidth, p_burok);
           	
    	//kor
    	canvas.save();
    	canvas.rotate(this.kulsoforogdegree, radius+strokewidth*2, radius+strokewidth*2);
       	canvas.drawCircle(radius+strokewidth*2, radius+strokewidth*2, radius, p2);	
       	canvas.restore();
       	
         
    	p.setAntiAlias(false);
    	p.setFilterBitmap(true);
    	
    	int felsoszin=0;
    	if (JarmoMain.userSzine==1 && JarmoMain.userLent==0) felsoszin=1;
    	if (JarmoMain.userSzine==0 && JarmoMain.userLent==1) felsoszin=1;
    	
    	
    	if (JarmoMain.jatekMode==3 && this.szin==felsoszin) {
    		canvas.save();
    		canvas.rotate(180, imageRect.left+imageRect.width()/2, imageRect.top+imageRect.height()/2);
    	}
    	int androide=0;
    	if (szin==1 && JarmoMain.userSzine==0) androide=1;
    	if (szin==0 && JarmoMain.userSzine==1) androide=1;
    	
    	if ((JarmoMain.jatekMode==1 || JarmoMain.jatekMode==2 || JarmoMain.jatekMode==6) && androide==1) {
		    if (allapot==1) {
			canvas.drawBitmap(JarmoSetting.mBitmapSargaAndroid, null, imageRect, p);    	
	    	} else {
			canvas.drawBitmap(JarmoSetting.mBitmapAndroid, null, imageRect, p);    	     		 
    		}
    	} else { 
    		if (allapot==1) {
    			canvas.drawBitmap(JarmoSetting.mBitmapFeher, null, imageRect, p);    	
    		} else {
    			canvas.drawBitmap(JarmoSetting.mBitmapSzurke, null, imageRect, p);    	     		 
    		}
    	}
    	
        if (JarmoMain.jatekMode==3 && this.szin==felsoszin) {
    		canvas.restore();
    	}
  
  
      }

    public void noMoveAnim () {
       	final Bogyo bb=this;
    	final int xx=bb.px;
       	noAnimPlaying=1;
        
       	for (double t=0; t<=Math.PI*2; t=t+0.1f) {
 			final double tt=t;
			JarmoMain.handler.postDelayed(new Runnable () {
				public void run () {
					bb.px=xx+(int)((double)JarmoMain.RADIUS*Math.sin(tt));
	//				System.out.println ("sin "+Math.sin(tt));
					bb.invalidate();
				}
			}, (int)(t*80));
       	}
      	for (double t=0; t<=Math.PI*2; t=t+0.1f) {
 			final double tt=t;
			JarmoMain.handler.postDelayed(new Runnable () {
				public void run () {
					bb.px=xx+(int)((double)JarmoMain.RADIUS*Math.sin(tt));
		//			System.out.println ("sin "+Math.sin(tt));
					bb.invalidate();
					if (tt+0.1>Math.PI*2) {
						bb.noAnimPlaying=0;
		//				System.out.println ("vege");
					}
				}
			}, (int)((Math.PI*2+t)*80));
       	}
    }

    public void noMoveAnimOld () {
    	final Bogyo bb=this;
    	final int xx=bb.px;
    	noAnimPlaying=1;
    	for (int c=0;c<1;c++) {
    	   	//System.out.println ("noMoveAnim c"+c);
    	       		
    		final int cc=c*(JarmoMain.RADIUS*5*7);	
    		
    		for (int t=1;t<JarmoMain.RADIUS;t++) {   		
    			final int tt=t;
    			JarmoMain.handler.postDelayed(new Runnable () {
    				public void run () {
    					////System.out.printlnout.println ("NoMoveAnim 1 tt"+tt+"cc "+cc);
    					if (bb.mozoge==1) return;
    					bb.px=xx-tt;
    					bb.invalidate();
    				}
    			}, cc+(tt*5));
    		}
    		for (int t=1;t<=JarmoMain.RADIUS;t++) {   		
    			final int tt=t;
    			JarmoMain.handler.postDelayed(new Runnable () {
    				public void run () {
    					//System.out.println ("NoMoveAnim 2 tt"+tt+"cc "+cc);
    					if (bb.mozoge==1) return;
       					bb.px=xx-(JarmoMain.RADIUS-tt);
       					bb.invalidate();
    				}
    			}, cc+(JarmoMain.RADIUS*10)+(tt*5));
    		}
    //		if (xx!=0) return;
    		for (int t=1;t<JarmoMain.RADIUS;t++) {   		
    			final int tt=t;
    			JarmoMain.handler.postDelayed(new Runnable () {
    				public void run () {
    					//System.out.println ("NoMoveAnim 4 tt"+tt+"cc "+cc);
    					if (bb.mozoge==1) return;
        				bb.px=xx+tt;
    					bb.invalidate();      				
    				}
    			}, cc+(JarmoMain.RADIUS*15)+(tt*5));
    		}
    		for (int t=1;t<=JarmoMain.RADIUS;t++) {   		
    			final int tt=t;
    			JarmoMain.handler.postDelayed(new Runnable () {
    				public void run () {
     					//System.out.println ("NoMoveAnim 4 tt"+tt+"cc "+cc);
    					if (bb.mozoge==1) return;     					  
    					bb.px=xx+(JarmoMain.RADIUS-tt);
    					bb.invalidate(); 
    					if (tt==JarmoMain.RADIUS&&cc==(1*(JarmoMain.RADIUS*5*7))) {
         					//System.out.println ("NoMoveAnim vege"+tt);
         					  
    						bb.noAnimPlaying=0;
    						bb.px=xx;
    					}
    				}
    			}, cc+(JarmoMain.RADIUS*20)+(tt*5));
    		}
    	}//c
    }
    	
	@Override
	public void myTouchEvent(MotionEvent event) {
		JarmoService.JarmoLog  ("myTouchEvent, Bogyo erintheto ="+mezo.id+"action="+event.getAction());
		for (int t=1;t<=5;t++) {
			if (JarmoMain.feketebogyo[t]==this) {JarmoService.JarmoLog  ("ez az "+erintheto);}
			JarmoService.JarmoLog  ("erintheto utana"+JarmoMain.feketebogyo[t].erintheto);
		}

		if (erintheto==0) {
        	if (noAnimPlaying==0) {
        	  JarmoSetting.playSound(R.raw.wrongarcher);
          	  this.noMoveAnim ();
        	}
           	return;
        }
        
		int action=event.getAction();
		
		JarmoService.JarmoLog  ("klikk, szin action"+ action);
		if (action==MotionEvent.ACTION_MOVE) {
			
			JarmoService.JarmoLog  ("BMO move, pos"+event.getX()+" "+event.getY());	
            this.px=(int)event.getX();
            this.py=(int)event.getY(); 
            this.mozoge=1;
	  	    this.invalidate();
	  	    ///gyanusan sokaig tartott a move
	  	    this.moveTimerHandler.removeCallbacksAndMessages(null); //regi timer torlese
	  	    final Bogyo bbogyo = this;
	  	    this.moveTimerHandler.postDelayed(new Runnable() {
	  	    	public void run () {
	  	    		if (bbogyo.mozoge==1) {
	  	    			JarmoService.JarmoLog  ("!! Tul sokaig tartott a move, reset");
	  	    			JarmoMain.redrawMezok (null);
	  	    			bbogyo.mozoge=0;
	  	    			JarmoMain.ed.clearDragTargetLista();	
	  	    		}

	  	    	}
	  	    }, 1000);
	  	    
	  	}
		
		if (action==MotionEvent.ACTION_UP) {
			
 	  	     this.moveTimerHandler.removeCallbacksAndMessages(null);

			 JarmoService.JarmoLog  ("action up");
			 JarmoMain.redrawMezok (null);
			 this.mozoge=0;
	   		 JarmoMain.ed.clearDragTargetLista();	
		}
		
		if (action==MotionEvent.ACTION_DOWN) {
			JarmoMain.redrawBogyok();
			
			JarmoMain.bogyoPosWifi = new BogyoPosWifi (this);
			JarmoMain.bogyoPosWifi.start();
			
//			if (JarmoMain.jatekMode==4) { //wifi
//GDPR		    	JarmoService.sendToSocket("BOGYO STARTMOVE "+mezo.id);
//			}
			
			JarmoService.JarmoLog  ("Click\n");

			JarmoMain.redrawMezok (mezo); 
	        JarmoMain.ed.clearDragTargetLista();

   		    if (mezo.id<30) { //csak ha nem parkolopalyas
   		        int t=1;
   		        while (mezo.szomszedok[t]!=null) {
   		           JarmoMain.ed.addDragTargetLista (mezo.szomszedok[t]);
   		           t++;
   		        }
   		    } else {
   		      JarmoMain.parkolobolAktival ();
   		    } //parkolomezo else
   		    
   		 //   JarmoMain.ed.setDraggingView (this);
   		   
		}
	}

	@Override
	public Rect getClickSurface() {
		return new Rect (
				(int)px-JarmoMain.BOGYORADIUS*2, 
				(int)py-JarmoMain.BOGYORADIUS*2, 
				(int)(px+JarmoMain.BOGYORADIUS*2), 
				(int)(py+JarmoMain.BOGYORADIUS*2)
				);
	}

	@Override
	public void myDragEvent(MotionEvent event, View target) {
	 
		JarmoService.JarmoLog  ("myDragEvent, action="+event.getAction());
		int action=event.getAction();
		
		if (this.erintheto==0) return;
		if (action==MotionEvent.ACTION_UP) {
		   if (JarmoMain.bogyoPosWifi!=null) JarmoMain.bogyoPosWifi.stop=1;
		   mozoge=0;	
		   if (target!=null) {
			 JarmoService.JarmoLog  ("mezore dob"); 
//			 if (JarmoMain.jatekMode==4) {
//GDPR				 JarmoService.sendToSocket("BOGYO DOB "+String.format("%02d", this.mezo.id)+" IDE "+((Mezo)target).id);
//			 }
		 
	   		 JarmoMain.ed.clearDragTargetLista();
	   		 
	   		JarmoMain.checkUserMove (this, (Mezo)target); 
	   		// this.mezo=(Mezo)target;
	   	    posToMezo (); //akarhol van, menjen bogyohoz
		  
		   } else { //nem mezore dob
			   
			 JarmoMain.ed.clearDragTargetLista();		   
//			 if (JarmoMain.jatekMode==4) {
//GDPR				 JarmoService.sendToSocket("BOGYO ENGED "+this.mezo.id);
//			 }
		     this.slideToPos(this.mezo, 30, 5, null);
		   }
		   JarmoMain.redrawMezok (null);
				
		}
		
	  } 
	
}
