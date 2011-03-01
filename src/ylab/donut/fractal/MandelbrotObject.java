package ylab.donut.fractal;

import android.graphics.*;
import android.view.Display;
import android.view.WindowManager;
import android.content.*;
import android.util.*;

/**
 * Mandelbrot Object
 */
public class MandelbrotObject extends FractalObject{
	private String LOGTAG="MandelbrotObject";
	private int state=0xffee4455;
	private RectF prect;
	
	/**
	 * Mandelbrot set
	 * @param Context context
	 */
	public MandelbrotObject(Context context){
		// find width height
		WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		this.width = disp.getWidth();
		this.height = disp.getHeight();
		
		// initial values
		prect = new RectF(-2,-2,2,2);
		
		// bitmap
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		pixels = new int[width*height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
	}
	
	/**
	 * initialize image pixel
	 */
	public void init(){
		
		if( data == null ){
			data = new double[height*width];
		}
		
		for( int y=0; y<height; y++ ){
			for(int x=0; x<width; x++ ){
				pixels[x+y*width] = 0;
				data[x+y*width] = 0;
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	}
	
	/**
	 * update image
	 */
	public void updateImage(Rect src, Rect dst){
		/* copy data to bitmap image */
		if(src == null || dst == null){
			for(int y=0; y<height; y++){
				for(int x=0; x<width; x++){
					int v = (int)(255.0d * data[y*width + x]);
					int a = 0xff000000;
					int r = v;
					int g = v;
					int b = v;
					r = (r<<16);
					g = (g<<8);
					pixels[x+y*width] = a | r | g | b;
				}
			}
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		}else{
			Log.d(LOGTAG,"copybitmap");
			copyBitmap(src,dst);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		}
	}
	
	/**
	 * update data
	 */
	public void updateData(RectF rect){
		if( rect.equals(prect) ){
			return;
		}
		prect = new RectF(rect);
		
		/* re-scale rectangle, fitting with display aspect.
		 * concept is "big area is preferred area"
		 */
		/*float whalf;
		float hhalf;
		float cx;
		float cy;
		if( aspect > 1.0 ){
			// width is large
			whalf = (prect.right - prect.left);// * aspect;
			hhalf = (prect.bottom - prect.top);
		}else{
			//height is large
			whalf = (prect.right - prect.left);
			hhalf = (prect.bottom - prect.top);// /aspect;
		}
		cx = (prect.right + prect.left)/2;
		cy = (prect.right + prect.left)/2;
		prect.left = cx - whalf;
		prect.right = cx + whalf;
		prect.top = cy - hhalf;
		prect.bottom = cy + hhalf;*/
		
		/* setup steps */
		double xstep = (prect.right - prect.left)/this.width;
		double ystep = (prect.bottom - prect.top)/this.height;
		double dx = prect.left;
		double dy = prect.top;
		android.util.Log.d("Mandel","dxdy:"+dx+" "+dy);
		
		/* mandel brot data storing */
		for(int y=0; y<height; y++){
			dx = prect.left;
			for(int x=0; x<width; x++){
				data[y*width + x] = getMandelbrot(0,0,dx,dy);
				dx+=xstep;
			}
			dy += ystep;
		}
	}
	
	/**
	 * mandelbrot calculation
	 * 
	 */
	private double getMandelbrot(double x, double y, double a, double b){
		// calculate 100 times 
		double zn=0;
		double xn,yn;
		int cnt=0;
		final int LOOPMAX=100;
		while( cnt < LOOPMAX ){
			xn=x*x-y*y +a;
			yn=2*x*y+b;
			zn=Math.abs(xn*xn+yn*yn);
			if(zn>4)break;
			x=xn;y=yn;
			cnt++;
		}
		if( cnt<LOOPMAX ){
			return (double)cnt/LOOPMAX;
		}
		return 0;
	}
}
