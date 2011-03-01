package ylab.donut.fractal;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.view.GestureDetector.*;
import android.os.*;
import android.util.*;
import java.util.concurrent.*;

/**
 * FractalView
 * 
 * @author T.Maruyama
 * @version 0.0
 */
public class FractalView extends View{
	private String LOGTAG = "FractalView";
	private Bitmap bitmap;						// 
	private FractalObject fractalObject;		// 
	private ScheduledExecutorService drawService;	// 
	private ScheduledExecutorService calcService; //
	private Handler handler = new Handler();	// 
	private Rect drawArea;	//draw area
	private Rect bitmapArea;	//bitmap area
	private Rect canvasArea;
	private RectF calcArea;
	private RectF calcCanvasArea;
	private RectF[] touchRect = new RectF[2];	// touch area. [0]:first [1]:second
	private int canvasWidth;
	private int canvasHeight;
	private boolean nowTouch=false;			// 
	private boolean hasChanged = false;
	private GestureDetector mGestureDetector;
	private TouchEventState mTouchEventState;
	
	/**
	 * FractalView
	 */
	public FractalView(Context context){
		super(context);
		setBackgroundColor(Color.YELLOW);
		
		// touch event
		mTouchEventState = new TouchEventState();
		mGestureDetector = new GestureDetector(context, simpleOnGestureListener);
		
		// rect
		WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		canvasWidth = disp.getWidth();
		canvasHeight = disp.getHeight();
		float aspect = (float)canvasWidth / canvasHeight;
		canvasArea = new Rect( 0, 0, canvasWidth, canvasHeight );
		drawArea = new Rect( 0, 0, canvasWidth, canvasHeight );
		bitmapArea = new Rect( drawArea );
		calcArea = new RectF( 0, -0.5f, aspect, 0.5f );
		touchRect[0]=new RectF(0,0,30,30);
		touchRect[1]=new RectF(10,10,20,20);
		
		// bitmap init
		fractalObject = new MandelbrotObject(context);
		fractalObject.init();
		fractalObject.updateData(calcArea);
		bitmap = fractalObject.getBitmap();
		
	}
	
	public void start(){
		runDraw();
		runCalc();
	}
	
	public void stop(){
		if( drawService != null ){
			drawService.shutdown();
		}
		if( calcService != null ){
			calcService.shutdown();
		}
	}
	
	// Draw
	//-----------------------------------------------
	
	/**
	 * onDraw
	 * <p>override method for drawing</p>
	 */
	protected void onDraw(Canvas canvas){
		Paint p = new Paint();
		p.setColor(Color.BLUE);
		
		//canvas.drawBitmap(bitmap, 0, 0, null);
		//canvas.drawBitmap(bitmap, null, bitmapArea, null);
		if( drawArea.equals(bitmapArea) && hasChanged){
			hasChanged = false;
			// calulate new area
			float scale = (float)(drawArea.right - drawArea.left)/canvasWidth;
			float coordRatio = (float)(calcArea.right - calcArea.left)/(drawArea.right-drawArea.left);
			float x1 = (canvasArea.left - drawArea.left)*coordRatio + calcArea.left;
			float y1 = (canvasArea.top - drawArea.top)*coordRatio + calcArea.top;
			float x2 = (canvasArea.right - drawArea.right)*coordRatio + calcArea.right;
			float y2 = (canvasArea.bottom - drawArea.bottom)*coordRatio + calcArea.bottom;
			calcCanvasArea = new RectF( x1, y1, x2, y2 );
			calcArea = new RectF(x1,y1, x2, y2);
			Log.d(LOGTAG,"updateimage src to dst");
			//fractalObject.updateImage(drawArea, canvasArea);
			drawArea = new Rect(canvasArea);
			bitmapArea = new Rect(canvasArea);
		}else{
			fractalObject.updateImage( null, null );
		}
		canvas.drawBitmap(bitmap, null, bitmapArea, null);
	}
	
	/**
	 * runDraw
	 * <p>run drawing thread</p>
	 */
	protected void runDraw(){
		drawService = Executors.newSingleThreadScheduledExecutor();
		drawService.scheduleAtFixedRate(new Runnable(){
			public void run(){
				handler.post(new Runnable(){
					public void run(){
						invalidate();
					}
				});
			}
		}, 0, 30, TimeUnit.MILLISECONDS);
	}
	
	
	/**
	 * runCalc
	 */
	protected void runCalc(){
		calcService = Executors.newSingleThreadScheduledExecutor();
		calcService.scheduleAtFixedRate(new Runnable(){
			public void run(){
				fractalObject.updateData(calcArea);
				/*Log.d(LOGTAG, "ratio:"+ratio);
				Log.d(LOGTAG, "canvasArea:"+ canvasArea.left + ","+canvasArea.top + "," + canvasArea.right +","+canvasArea.bottom);
				Log.d(LOGTAG, "drawArea:"+ drawArea.left + ","+drawArea.top + "," + drawArea.right +","+drawArea.bottom);
				*/Log.d(LOGTAG, "calcArea:"+ calcArea.left + ","+calcArea.top + "," + calcArea.right +","+calcArea.bottom);
				/*Log.d(LOGTAG, "calc updated:" + x1 + "," + y1 + "," + x2 + "," + y2);*/
				Log.d(LOGTAG, "calcrun");
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	// Touch
	//-----------------------------------------------
	
	/**
	 * onTouchEvent
	 * Single Touch Event(move) &amp; MultiTouchEvent(scaling) <br/>
	 * Prefer
	 * <ol>
	 * <li>action</li>
	 * <li>flow</li>
	 * <li></li>
	 * </ol>
	 */
	public boolean onTouchEvent(MotionEvent event){
		/*
		final int pointerCount = event.getPointerCount();
		// should be multi pointer support
		if( pointerCount < 1 ){
			return false;
		}
		
		final int eventCode = event.getAction();
		final int historyLength = event.getHistorySize();
		final int firstPointerIndex = event.findPointerIndex(0) / pointerCount;
		final int secondPointerIndex = (pointerCount>1)? event.findPointerIndex(1) : -1;
		
		if( event.getAction() == MotionEvent.ACTION_UP ){
			nowTouch = false;
			boolean isContain = touchRect[0].contains(touchRect[1]);
			if( isContain ){
				//setCenter(touchRect[1]);
				//setWidthHeight(touchRect[0],touchRect[1]);
			}
			return true;
		}
		
		if( pointerCount != 2 )return true;
		if( event.getAction() == MotionEvent.ACTION_MOVE ){
			if( !nowTouch ){
				touchRect[0].set(event.getX(0),event.getY(0), event.getX(1),event.getY(1));
				touchRect[0].sort();
				nowTouch=true;
			}else{
				touchRect[1].set(event.getX(0),event.getY(0), event.getX(1),event.getY(1));
				touchRect[1].sort();
			}
		}
		*/

		mGestureDetector.onTouchEvent(event);
		
		switch( event.getAction() ){
		case MotionEvent.ACTION_DOWN:
			Log.d(LOGTAG, "ACTION_DOWN"+event.getX()+","+event.getY());
			break;
		case MotionEvent.ACTION_UP:
			drawArea.set(bitmapArea);
			hasChanged = true;
			Log.d(LOGTAG,"true?"+drawArea.equals(bitmapArea));
			Log.d(LOGTAG, "ACTION_UP");
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d(LOGTAG, "ACTION_MOVE"+event.getX()+","+event.getY());
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.d(LOGTAG, "ACTION_CANCEL");
			break;
		}
		return true;
	}
	
	
	// utility
	//--------------------------------------
	private final SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener(){

		/**
		 * onDown
		 * allow: Move, Zoom, Rotate
		 */
		public boolean onDown(MotionEvent event){
			Log.d(LOGTAG, "onDown");
			mTouchEventState.allow(event);
			return super.onDown(event);
		}
		
		
		/**
		 * onFling
		 * activate: Move
		 */
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
			Log.d(LOGTAG, "onFling,xy1:"+event1.getX() + "," + event1.getY() + " xy2:" + event2.getX() + "," + event2.getY() );
			mTouchEventState.doMove(event1, event2);
			return super.onFling(event1, event2, velocityX, velocityY);
		}

		/**
		 * onScroll
		 * activate: Move
		 */
		public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY){
			Log.d(LOGTAG, "onScroll xy1:"+event1.getX() + "," + event1.getY() + " xy2:" + event2.getX() + "," + event2.getY());
			mTouchEventState.doMove(event1, event2);
			return super.onScroll(event1, event2, distanceX, distanceY);
		}
		
		/**
		 * onLongPress
		 * activate: ZoomOut
		 */
		public void onLongPress(MotionEvent event){
			Log.d(LOGTAG, "onLongPress");
			mTouchEventState.doCenteredZoom(event,1.0/2);
		}
		
		/**
		 * onShowPress
		 * don't care
		 */
		public void onShowPress(MotionEvent event){
			Log.d(LOGTAG, "onShowPress");
		}
		
		/**
		 * onDoubleTap
		 * activate: ZoomIn
		 */
		public boolean onDoubleTap(MotionEvent event){
			Log.d(LOGTAG, "onDoubleTap");
			mTouchEventState.doCenteredZoom(event,2);
			return super.onDoubleTap(event);
		}
		
		/**
		 * onSingleTapUp
		 * don't care
		 */
		public boolean onSingleTapUp(MotionEvent event){
			Log.d(LOGTAG, "onSingleTapUp");
			return super.onSingleTapUp(event);
		}
		
		//-- never used function --
		public boolean onDoubleTapEvent(MotionEvent event){
			Log.d(LOGTAG, "onDoubleTapEvent");
			return super.onDoubleTapEvent(event);
		}
		/*public boolean onSinleTapConfirmed(MotionEvent event){
			Log.d(LOGTAG, "onSingleTapConfirmed");
			return super.onSingleTapConfirmed(event);
		}*/
	};
	
	class TouchEventState{
		protected boolean allowMove;
		protected boolean allowZoom;
		protected boolean allowRotate;
		protected RectF[] rect = new RectF[2];
		public TouchEventState(){
			deny();
		}
		
		public void doMove(MotionEvent event1, MotionEvent event2){
			allowZoom = false;
			allowRotate = false;
			
			// describe do move
			int x1 = (int)event1.getX();
			int y1 = (int)event1.getY();
			int x2 = (int)event2.getX();
			int y2 = (int)event2.getY();
			int dx = x2 - x1;
			int dy = y2 - y1;
			bitmapArea.set(drawArea.left + dx , drawArea.top + dy,
					drawArea.right + dx, drawArea.bottom + dy );
		}
		
		public void doZoom(MotionEvent event){
			allowMove = false;
			allowRotate = false;
			if( rect[0] == null ){
				rect[0] = new RectF(event.getX(0),event.getY(0),event.getX(1),event.getY(1));
				return;
			}
			rect[1] = new RectF(event.getX(0),event.getY(0),event.getX(1),event.getY(1));
		}
		
		public void doCenteredZoom(MotionEvent event, double zoomlevel){
			int canvasCX = (int)event.getX();//canvasWidth / 2;
			int canvasCY = (int)event.getY();//canvasHeight / 2;
			int drawWidth = drawArea.right - drawArea.left;
			int drawHeight = drawArea.bottom - drawArea.top;
			int drawCX = drawWidth / 2;
			int drawCY = drawHeight / 2;
			int x1 = (int)(zoomlevel*(drawArea.left - canvasCX)) + canvasCX;
			int y1 = (int)(zoomlevel*(drawArea.top - canvasCY)) + canvasCY;
			int x2 = (int)(zoomlevel*(drawArea.right - canvasCX)) + canvasCX;
			int y2 = (int)(zoomlevel*(drawArea.bottom -canvasCY)) + canvasCY;
			
			bitmapArea.set(x1, y1, x2, y2);
		}
		
		public void doRotate(){
			allowMove = false;
		}
		
		public void allow(MotionEvent event){
			allowMove = true;
			allowZoom = true;
			allowRotate = true;
			int pointerCount = event.getPointerCount();
			if( pointerCount < 1 ){
				return ;
			}
			if( pointerCount == 2 ){
				rect[0] = new RectF(event.getX(0),event.getY(0),event.getX(1),event.getY(1));
				rect[0].sort();
			}
		}
		
		public void deny(){
			allowMove = false;
			allowZoom = false;
			allowRotate = false;
			rect[0] = null;
			rect[1] = null;
		}
		
	}
}

