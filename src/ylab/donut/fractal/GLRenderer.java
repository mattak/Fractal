package ylab.donut.fractal;

import java.io.*;
import android.content.Context;
import android.graphics.*;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.GLU;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer{
	public GLRenderer(Context context){
		mTriangle = new Triangle();
		mCube = new Cube();
		mTextureTriangle = new TextureTriangle();
		mContext = context;
	}
	// Renderer implements
	//-------------------------------
	
	public void onDrawFrame(GL10 gl){
		gl.glDisable(GL10.GL_DITHER);
       gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                GL10.GL_MODULATE);
		
		// Display Clear
		gl.glClearColor(1.0f,1.0f,0, 0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// model mode
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		
		// move view point
		GLU.gluLookAt(gl, 0, 0, -6, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		
		// draw cube
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		//gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		//gl.glEnable(GL10.GL_CULL_FACE); //enable background culling
		gl.glActiveTexture(GL10.GL_TEXTURE0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		
		
		// draw
		long time = SystemClock.uptimeMillis() % 4000L;
       float angle = 0.090f * ((int) time);
		gl.glRotatef( angle, 1.0f, .0f, .0f);
		//mTextureTriangle.draw(gl);
		mTextureTriangle.draw(gl);
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height){
		// set viewport
		gl.glViewport(0, 0, width, height);
		
		// frustum rendering area in perspective projection system
		float ratio = (float) width/ height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);
	}
	
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config){
		// disable default quality improvement
		gl.glDisable(GL10.GL_DITHER);
		
		// one time initialization 
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		// create texture
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		
		mTextureID = textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
		
		InputStream is = mContext.getResources().openRawResource(R.drawable.robot);
		Bitmap bitmap;
		try{
			bitmap = BitmapFactory.decodeStream(is);
		}finally{
			try{
				is.close();
			}catch(IOException e){
				// ignore
			}
		}
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();// never be read or written
		
	}
	
	private Triangle mTriangle;
	private Cube mCube;
	private TextureTriangle mTextureTriangle;
	private GLRenderObject mRenderObject;
	private int mTextureID;
	private Context mContext;
	private FractalObject fractalBitmap;
}

