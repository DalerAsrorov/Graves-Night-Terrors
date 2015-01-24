package Digga;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.util.glu.Sphere;

public class GhostObject {

	float x;
	float y;
	float z;
	float rotation;
	int type;
	boolean normal = true;
	boolean tracking = false; 
	boolean damage = false;
	int randomDirection = 0;

	
	public GhostObject()
	{
		x = 25f;
		y = 0f;
		z = 25f;
		rotation = 0f;
		type = 1;
		randomDirection = 0;
	}	
	public GhostObject(float x, float y, float z, float rotation, int type, int randomDirection)
	{
		this.x = x; 
		this.y = y;
		this.z = z;
		this.rotation = rotation;
		this.type = type;
		this.randomDirection = randomDirection;
	}
	
	public void ghostLoop()
	{

		
		//glTranslatef(0, 0, 0);
		renderSphere(x, y, z);
		//glEnd();
		
	}
	private static void renderSphere(float x, float y, float z) 
	{
		     glPushMatrix();
		     glTranslatef(x, y, z);
		     Sphere s = new Sphere();
		     
		     s.draw(0.5f, 16, 16);
		     glPopMatrix();
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