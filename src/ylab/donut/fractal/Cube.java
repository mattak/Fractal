package ylab.donut.fractal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;

public class Cube{
	public Cube(){
		int one = 0x10000;
		int vertices[] = {
				-one, -one, -one,
				one, -one, -one,
				one, one, -one,
				
				-one, one, -one,
				-one, -one, one,
				one, -one, one,
				
				one, one, one,
				-one, one, one
		};
		int colors[] = {
				0, 0, 0, one,
				one, 0, 0, one,
				one, one, 0, one,
				0, one, 0, one,
				0, 0, one, one,
				one, 0, one, one,
				one, one, one, one,
				0, one, one, one
		};
		byte indices[] = {
				0,4,5, 0,5,1,
				1,5,6, 1,6,2,
				2,6,7, 2,7,3,
				3,7,4, 3,4,0,
				4,7,6, 4,6,5,
				3,0,1, 3,1,2
		};
		
		mVertexBuffer = setupIntBuffer(vertices);
		mColorBuffer = setupIntBuffer(colors);
		mIndexBuffer = setupByteBuffer(indices);
	}
	
	// draw
	//-------------------------
	public void draw(GL10 gl){
		gl.glFrontFace(GL10.GL_CW);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, mIndexBuffer.capacity(), GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
	}
	
	// util
	//-------------------------
	private IntBuffer setupIntBuffer( int[] arrays ){
		// prepare vertex
		ByteBuffer arrayBuffer = ByteBuffer.allocateDirect(arrays.length*4);
		arrayBuffer.order(ByteOrder.nativeOrder());
		IntBuffer intBuffer = arrayBuffer.asIntBuffer();
		intBuffer.put(arrays);
		intBuffer.position(0);
		return intBuffer;
	}
	private ByteBuffer setupByteBuffer( byte []arrays ){
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(arrays.length);
		byteBuffer.put(arrays);
		byteBuffer.position(0);
		return byteBuffer;
	}
	private IntBuffer mColorBuffer;
	private IntBuffer mVertexBuffer;
	private ByteBuffer mIndexBuffer;
}

