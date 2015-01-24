package Digga;

import static org.lwjgl.opengl.GL11.*;

public class Grave {

	public float x;
	public float y;
	public float z;
	public float length;
	public float width;
	public float height;
	public float rotation;
	public float digState;
	public float digDone = 0;
	public BoundingBox boundingBox;
	public BoundingBox plotArea;
	public Rectangle myRect;
	
	public boolean changed;
	
	public Grave(float x, float y, float z, float l, float h, float w, float r)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		length = l;
		width = w;
		height = h;
		rotation = r;
		
		boundingBox = new BoundingBox(0, 0, l, w);
		boundingBox.setBounds(x, z, r);
		plotArea = new BoundingBox(0,.5f + 2.5f, length, 5f);
		plotArea.setBounds(x, z, r);
		
		if(rotation % 180 <= 90)
		{
			myRect = new Rectangle(boundingBox.x[2], plotArea.x[0], boundingBox.z[3], plotArea.z[1]);
		}
		else
		{
			myRect = new Rectangle(boundingBox.x[3], plotArea.x[1], boundingBox.z[2], plotArea.z[0]);
		}
		
		digState = 0;
		
		changed = false;
	}
	
	public void DrawPlot()
	{
		glPushMatrix();
		{
			glTranslatef(x, 0, z);
			glRotatef(rotation, 0, 1, 0);
			glTranslatef(-x, 0, -z);
		glBegin(GL_QUADS);
		{
			if(digState == 0)
			{
				glColor3f(.3f, .3f, .3f);
			}
			else
			{

				glColor3f(139f/600f, 69f/600f, 19f/600f);
			}
			
			
			glVertex3f(x - length/2f, digState, z + width/2f + 5f);
			glVertex3f(x + length/2f, digState, z + width/2f + 5f);
			glVertex3f(x + length/2f, digState, z + width/2f);
			glVertex3f(x - length/2f, digState, z + width/2f);
			
			glColor3f(139f/510f, 69f/510f, 19f/510f);
			glVertex3f(x - length/2f, 0, z + width/2f + 5f);
			glVertex3f(x - length/2f, -1, z + width/2f + 5f);
			glVertex3f(x + length/2f, -1, z + width/2f + 5f);
			glVertex3f(x + length/2f, 0, z + width/2f + 5f);
			
			glVertex3f(x - length/2f, 0, z + width/2f);
			glVertex3f(x - length/2f, -1, z + width/2f);
			glVertex3f(x + length/2f, -1, z + width/2f);
			glVertex3f(x + length/2f, 0, z + width/2f);
			
			glVertex3f(x + length/2f, 0, z + width/2f);
			glVertex3f(x + length/2f, -1, z + width/2f);
			glVertex3f(x + length/2f, -1, z + width/2f + 5f);
			glVertex3f(x + length/2f, 0, z + width/2f + 5f);
			
			glVertex3f(x - length/2f, 0, z + width/2f);
			glVertex3f(x - length/2f, -1, z + width/2f);
			glVertex3f(x - length/2f, -1, z + width/2f + 5f);
			glVertex3f(x - length/2f, 0, z + width/2f + 5f);
		}		
		glEnd();
		
	
		
		}
		glPopMatrix();
	}
	public void GraveDisplay()
	{
		glColor3f(.3f, .3f, .3f);
		GameFrame.drawRectPrism(x, y, z, length, height, width, 0, rotation, 0);
	
		
		
		
		
		glBegin(GL_TRIANGLES);
		{
			glColor3f(.1f,.1f,.1f);
			glVertex3f(boundingBox.x[2], 0, boundingBox.z[2]);
			glVertex3f(boundingBox.x[3], 0, boundingBox.z[3]);
			
			if(rotation % 180 <= 90)
			{
				glVertex3f(boundingBox.x[2], 0, boundingBox.z[3]);
			}
			else
			{
				glVertex3f(boundingBox.x[3], 0,  boundingBox.z[2]);
			}
					
					
			glVertex3f(boundingBox.x[3], 0, boundingBox.z[3]);
			glVertex3f(plotArea.x[0], 0, plotArea.z[0]);
			if(rotation % 180 <= 90)
			{
				glVertex3f(plotArea.x[0], 0, boundingBox.z[3]);
			}
			else
			{
				glVertex3f(boundingBox.x[3], 0, plotArea.z[0]);
			}
					
			glVertex3f(plotArea.x[0], 0, plotArea.z[0]);
			glVertex3f(plotArea.x[1], 0, plotArea.z[1]);
			if(rotation % 180 <= 90)
			{
				glVertex3f(plotArea.x[0], 0, plotArea.z[1]);
			}
			else
			{
				glVertex3f(plotArea.x[1], 0, plotArea.z[0]);
			}
					
			glVertex3f(plotArea.x[1], 0, plotArea.z[1]);
			glVertex3f(boundingBox.x[2], 0, boundingBox.z[2]);
			if(rotation % 180 <= 90)
			{
				glVertex3f(boundingBox.x[2], 0, plotArea.z[1]);
			}
			else
			{
				glVertex3f(plotArea.x[1], 0, boundingBox.z[2]);
			}
		}
		glEnd();
	}
	
	public String createObjectStream()
	{
		String os = "";
		os += "x: " + x;
		os += " ";
		os += "z: " + z;
		os += "r: " + rotation;
		return os;
	}
	
	
}
