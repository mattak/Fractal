package ylab.donut.fractal;

import android.app.Activity;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.View;

public class FractalActivity extends Activity {
	private GLSurfaceView mGLView;
	private FractalView fractalView;
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        /*mGLView = new GLSurfaceView(this);
        mGLView.setRenderer(new GLRenderer(this));
        this.setContentView(mGLView);*/
        fractalView = new FractalView(this);
        this.setContentView(fractalView);
    }
    protected void onPause(){
    	super.onPause();
    	fractalView.stop();
    	//mGLView.onPause();
    }
    protected void onResume(){
    	super.onResume();
    	fractalView.start();
    	//mGLView.onResume();
    }
}
