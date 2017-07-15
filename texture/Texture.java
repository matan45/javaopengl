package texture;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Texture {
	private int id;
	private int width;
	private int height;

	
	public Texture(String fileName){
		BufferedImage bi;
		try {
			bi=ImageIO.read(new File("src/resources/texture/"+fileName+".png"));
		
		width=bi.getWidth();
		height=bi.getHeight();
		
		int[] pixels_raw=new int[width*height];
		pixels_raw=bi.getRGB(0, 0, width, height, null, 0, width);
		
		ByteBuffer pixels=BufferUtils.createByteBuffer(width*height*4);
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				int pixel=pixels_raw[i*width+j];
				pixels.put((byte)((pixel >> 16)& 0xFF)); //RED
				pixels.put((byte)((pixel >> 8)& 0xFF)); //GREEN
				pixels.put((byte)(pixel & 0xFF)); //BLUE
				pixels.put((byte)((pixel >> 24)& 0xFF)); //ALPHA
			}
		}
		pixels.flip();
		id=GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		
		glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public int getId() {
		return id;
	}
	
	
}
