package com.mbd.jarmo;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public interface OwnMotionView {
	public void myTouchEvent (MotionEvent event);
	public void myDragEvent (MotionEvent event, View errePotty);
	public Rect getClickSurface ();
}