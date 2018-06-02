package com.mbd.jarmo;

import java.util.Vector;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

class EventDetector extends View {
	private Vector viewlista;
	private Vector dragtargetlista;
	private View currentDragView;

	public EventDetector(Context context) {
		super(context);
		viewlista=new Vector ();
		dragtargetlista=new Vector ();
		// TODO Auto-generated constructor stub
	}
	
	public void registerView (View newView) {
		viewlista.addElement(newView);
	}
	
	public void unregisterView (View newView) {
		viewlista.removeElement(newView);
	}
	
	//Drag start. Torold ki, ha nem kell drag
    public void setDraggingView (View dview) {
    	currentDragView = dview;
    }
    
    public void addDragTargetLista (View newelement) {
    	dragtargetlista.addElement(newelement);
    }
    
    public void clearDragTargetLista () {
    	dragtargetlista.removeAllElements();
    }
	
	
    private View melyikViewFolottVan (Vector melyiklista, int posx, int posy) {
    
    	if (melyiklista==null) return null;
    	int t=0;	
		while (t<melyiklista.size()) {
			View currview=(View)melyiklista.get(t);
			if (currview==null) break;
			//JarmoService.JarmoLog  ("csekk "+posx+"-"+posy+" box "+((OwnMotionView) currview).getClickSurface().toString());
		    
			if (((OwnMotionView)currview).getClickSurface()!=null) {
					if (((OwnMotionView)currview).getClickSurface().contains (posx, posy)) {
		            	return currview;
					}
		    }
			t++;
		}
		return null;
    }
    
	public boolean onTouchEvent (MotionEvent event){ 
		int posx=(int)event.getX();
		int posy=(int)event.getY();
		int action=event.getAction();
		
		if (action==MotionEvent.ACTION_DOWN) {
				View v = melyikViewFolottVan (viewlista, posx, posy);
				if (v!=null) {
				   ((OwnMotionView)v).myTouchEvent(event);	
				   if (this.currentDragView==null) {
					  JarmoService.JarmoLog  ("drag indul");
					  this.currentDragView=v;
				   }
				}
		}
		if (action==MotionEvent.ACTION_MOVE) {
			if (this.currentDragView!=null) {
				 //drag folyamatban - kapja meg az ACTION_MOVE-t
				((OwnMotionView)this.currentDragView).myTouchEvent(event);
			}
		}
		if (action==MotionEvent.ACTION_UP) {
			View v = melyikViewFolottVan (dragtargetlista, posx, posy);
			if (this.currentDragView!=null) {
			  ((OwnMotionView)this.currentDragView).myDragEvent(event, v);
			} else {
				if (v!=null) ((OwnMotionView)v).myTouchEvent(event);
			}
			this.currentDragView=null;
			
			
		} 
		
	return true;	
	}
}