package ylab.donut.fractal;

import android.graphics.*;
import android.util.Log;

/**
 * fractal object for calculation and painting thread
 */
public abstract class FractalObject {
	private final String LOGTAG = "FractalObject";
	protected Bitmap bitmap;		// fractal bitmap
	protected int[] pixels;		// bitmap pixels [width*height*3]
	protected double[] data;		// fractal data[]
	
	protected int width;			// display width
	protected int height;			// display height
	protected Rect currentRect;	// region
	
	/**
	 * initialize image map
	 */
	abstract void init();
	
	/**
	 * update data
	 */
	abstract void updateData(RectF dataarea);
	
	/**
	 * update image
	 */
	abstract void updateImage(Rect src, Rect dst);
	
	/**
	 * get Bitmap
	 */
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	/**
	 * copy bitmap
	 */
	public void copyBitmap(Rect src, Rect dst){
		int minx = (src.left<dst.left) ? dst.left : src.left;
		int miny = (src.top<dst.top) ? dst.top : src.top;
		int maxx = (src.right > dst.right) ? dst.right : src.right;
		int maxy = (src.bottom > dst.bottom) ? dst.bottom : src.bottom;
		int width = (maxx-minx);
		int height = (maxy-miny);
		int widthStep = src.right-src.left;
		double tmp[] = new double[width*height];
		Log.d(LOGTAG,"w,h:"+width+","+height);
		Log.d(LOGTAG,"miny-src.top:"+(miny-src.top));
		Log.d(LOGTAG,"miny-dst.top:"+(miny-dst.top));
		Log.d(LOGTAG,"minx-src.left:"+(minx-src.left));
		Log.d(LOGTAG,"minx-dst.left:"+(minx-dst.left));
		for( int i=miny-src.top,ti=0; i<height; i++,ti++ ){
			for( int j=minx-src.left,tj=0; j<width; j++,tj++ ){
				tmp[ti*width+tj]=data[i*widthStep+j];
			}
		}
		for( int i=miny-dst.top,ti=0; i<height; i++,ti++ ){
			for( int j=minx-dst.left,tj=0; j<width; j++,tj++ ){
				data[i*widthStep+j]=tmp[ti*width+tj];
			}
		}
	}
}
