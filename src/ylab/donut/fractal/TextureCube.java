package ylab.donut.fractal;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;

public class TextureCube{
	public TextureCube(){
		int one = 0x10000;
		float vertices[] = {
				//前面
		        -0.5f, -0.5f,  0.5f,
		         0.5f, -0.5f,  0.5f,
		        -0.5f,  0.5f,  0.5f,
		         0.5f,  0.5f,  0.5f,
		        //背面
		        -0.5f, -0.5f, -0.5f,
		        -0.5f,  0.5f, -0.5f,
		         0.5f, -0.5f, -0.5f,
		         0.5f,  0.5f, -0.5f,
		        //左面
		        -0.5f, -0.5f,  0.5f,
		        -0.5f,  0.5f,  0.5f,
		        -0.5f, -0.5f, -0.5f,
		        -0.5f,  0.5f, -0.5f,
		        //右面
		         0.5f, -0.5f, -0.5f,
		         0.5f,  0.5f, -0.5f,
		         0.5f, -0.5f,  0.5f,
		         0.5f,  0.5f,  0.5f,
		        //上面
		        -0.5f,  0.5f,  0.5f,
		         0.5f,  0.5f,  0.5f,
		         -0.5f,  0.5f, -0.5f,
		         0.5f,  0.5f, -0.5f,
		        //下面
		        -0.5f, -0.5f,  0.5f,
		        -0.5f, -0.5f, -0.5f,
		         0.5f, -0.5f,  0.5f,
		         0.5f, -0.5f, -0.5f
		};
		float textures[] = {
				//前面
		         0.0f, 0.0f,
		         1.0f, 0.0f,
		         0.0f, 1.0f,
		         1.0f, 1.0f,
		        //背面
		         1.0f, 0.0f,
		         1.0f, 1.0f,
		         0.0f, 0.0f,
		         0.0f, 1.0f,
		        //左面
		         1.0f, 0.0f,
		         1.0f, 1.0f,
		         0.0f, 0.0f,
		         0.0f, 1.0f,
		        //右面
		         1.0f, 0.0f,
		         1.0f, 1.0f,
		         0.0f, 0.0f,
		         0.0f, 1.0f,
		        //上面
		         0.0f, 0.0f,
		         1.0f, 0.0f,
		         0.0f, 1.0f,
		         1.0f, 1.0f,
		        //下面
		         1.0f, 0.0f,
		         1.0f, 1.0f,
		         0.0f, 0.0f,
		         0.0f, 1.0f
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
		mColorBuffer = setupIntBuffer(textures);
		mIndexBuffer = setupByteBuffer(indices);
	}
	
	// draw
	//-------------------------
	public void draw(GL10 gl){
		gl.glFrontFace(GL10.GL_CW);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mColorBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, mIndexBuffer.capacity(), GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
	}
	
	// util
	//-------------------------
	private FloatBuffer setupIntBuffer( float[] arrays ){
		// prepare vertex
		ByteBuffer arrayBuffer = ByteBuffer.allocateDirect(arrays.length*4);
		arrayBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = arrayBuffer.asFloatBuffer();
		floatBuffer.put(arrays);
		floatBuffer.position(0);
		return floatBuffer;
	}
	private ByteBuffer setupByteBuffer( byte []arrays ){
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(arrays.length);
		byteBuffer.put(arrays);
		byteBuffer.position(0);
		return byteBuffer;
	}
	private FloatBuffer mColorBuffer;
	private FloatBuffer mVertexBuffer;
	private ByteBuffer mIndexBuffer;
}

