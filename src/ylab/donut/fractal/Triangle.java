package ylab.donut.fractal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.*;

public class Triangle{
	public Triangle(){
		// set vertices and colors
		int one = 0x10000;
		int vertices[] ={
				-one,0,0, // left
				one,0,0, // right
				0,one,0 // top
		};
		int colors[]={
				one,0,0,one, // red
				0,one,0,one, // green
				0,0,one,one // blue
		};
		byte indices[]={
				0,1,2
		};
		
		// prepare vertex
		ByteBuffer vertexBuffer = ByteBuffer.allocateDirect(vertices.length*4);
		vertexBuffer.order(ByteOrder.nativeOrder());
		mVertexBuffer = vertexBuffer.asIntBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
		
		// prepare color
		ByteBuffer colorBuffer = ByteBuffer.allocateDirect(colors.length*4);
		colorBuffer.order(ByteOrder.nativeOrder());
		mColorBuffer = colorBuffer.asIntBuffer();
		mColorBuffer.put(colors);
		mColorBuffer.position(0);
		
		// prepare index
		mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		mIndexBuffer.put(indices);
		mIndexBuffer.position(0);
	}
	
	public void draw(GL10 gl){
		// Culling
		gl.glFrontFace(GL10.GL_CW);
		// set Vertex
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
		// set Color
		gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
		// draw
		gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
	}
	private IntBuffer mVertexBuffer;
	private IntBuffer mColorBuffer;
	private ByteBuffer mIndexBuffer;
}
