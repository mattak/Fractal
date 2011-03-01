package ylab.donut.fractal;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.*;

public class TextureTriangle{
	public TextureTriangle(){
		// set vertices and colors
		float s = 0.5f;
		float coords[] = new float[]{
				-0.25f, -0.25f, 0.0f,
				 0.25f, -0.25f, 0.0f,
				  0.0f, 0.5569f, 0.0f
		};
		
		mVertexBuffer = getFloatBuffer( coords, 3, 3, 2.0f, 0.0f );
		mTextureBuffer = getFloatBuffer( coords, 3, 2, 2.0f, 0.5f );
		mIndexBuffer = getShortBuffer( 3 );
	}
	
	public void draw(GL10 gl){
		// Culling
		gl.glFrontFace(GL10.GL_CCW);
		// set Vertex
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		// 
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// set Color
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
		// draw
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 3, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
	}
	
	private FloatBuffer getFloatBuffer( float[] points,
			int rowsize, int columnsize, float scale, float offset ){
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect( rowsize * columnsize * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		for( int i=0; i<rowsize; i++ ){
			for( int j=0; j<columnsize; j++ ){
				floatBuffer.put(points[i*3+j]*scale + offset);
			}
		}
		floatBuffer.position(0);
		return floatBuffer;
	}
	
	private ShortBuffer getShortBuffer( int size ){
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect( size * 2 );
		byteBuffer.order(ByteOrder.nativeOrder());
		ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
		for( int i=0; i<size; i++ ){
			shortBuffer.put((short) i);
		}
		shortBuffer.position(0);
		return shortBuffer;
	}
	
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
	private ShortBuffer mIndexBuffer;
}
