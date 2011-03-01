package ylab.donut.fractal;

import android.view.*;
import android.graphics.*;

/**
 * Multi Touch Event Handler
 */
public class MultiTouchEvent{
	public final static int GESTURE_PAN = 0;				//
	public final static int GESTURE_ROTATE = 1;			//
	public final static int GESTURE_SWIPE = 2;			//
	public final static int GESTURE_ZOOM = 3;				//
	public final static int GESTURE_TWO_FINGER_TAP = 4;	// 
	private RectF[] doubleTouchLog = new RectF[3];
	private PointF[] singleTouchLog = new PointF[3];
	private MotionEvent event;
	private boolean touching=false;
	
	
	public MultiTouchEvent(MotionEvent e){
		event = e;
	}
	
	public void update(MotionEvent e){
		event = e;
	}
	
	/*
	public int getEventType(MotionEvent e){
		update(e);
		
	}*/
	
	/**
	 * Single Touch Event Type
	 * supports: Pan, Swipe, Tap
	 
	public int getSingleTouchEventType(MotionEvent e){
		// save log
		saveLog(e.getX(),e.getY());
		
		switch(e.getAction() ){
		case MotionEvent.ACTION_UP:
			
			clearLog();
			break;
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_CANCEL:
			
			clearLog();
			touching = false;
			break;
		}
	}*/
	
	/**
	 * Double Touch Event Type
	 * supports: Pan, Swipe, Rotate, Zoom, Tap, Press&Tap, DoubleTap
	 
	public int getDoubleTouchEventType(MotionEvent e){
		// save log
		saveLog(e.getX(0),e.getY(0),e.getX(1), e.getY(1));
		
		
		
	}*/
	
	public boolean isSingleTouch(MotionEvent e){
		if( e.getPointerCount() == 1 ){
			return true;
		}
		return false;
	}
	
	public boolean isDoubleTouch(MotionEvent e){
		if( e.getPointerCount() == 2 ){
			return true;
		}
		return false;
	}
	
	/**
	 * save single touch log
	 * 0:first touch point
	 * 1:second latest touch point
	 * 2:latest touch point
	 */
	private void saveLog(float x, float y){
		PointF point = new PointF(x,y);
		if(touching){
			if( singleTouchLog[2] == null ){
				singleTouchLog[1] = singleTouchLog[2] = point;
			}else{
				singleTouchLog[1] = singleTouchLog[2];
				singleTouchLog[2] = point;
			}
		}
	}
	
	/**
	 * save double touch log
	 * 0:first touch area
	 * 1:second latest touch area
	 * 2:latest touch area
	 */
	private void saveLog(float x1, float y1, float x2, float y2){
		RectF rect = new RectF(x1,y1,x2,y2);
		rect.sort();
		if(touching){
			if( doubleTouchLog[2] == null ){
				doubleTouchLog[1] = doubleTouchLog[2] = new RectF(rect);
			}else{
				doubleTouchLog[1] = new RectF(doubleTouchLog[2]);
				doubleTouchLog[2] = new RectF(rect);
			}
		}else{
			doubleTouchLog[0] = new RectF(rect);
		}
	}
	
	private void clearLog(){
		for(int i=0; i<3; i++ ){
			doubleTouchLog[i]=null;
			singleTouchLog[i]=null;
		}
	}
}
