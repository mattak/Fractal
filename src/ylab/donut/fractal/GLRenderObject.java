package ylab.donut.fractal;

import javax.microedition.khronos.opengles.*;

public interface GLRenderObject{
	void draw(GL10 gl);
	void init(GL10 gl);
}