package Digga;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class DiggerCam 
{	
	private float fov;
	private float aspect;
	private float near;
	private float far;
	
	private float y;
	
	Digga myDigga;
	
	public DiggerCam(float fo, float a, float n, float f, Digga digga)
	{
		
		fov = fo;
		aspect = a;
		near = n;
		far = f;
		y = 2;
		myDigga = digga;
		initProjection();
		
	}
	
	private void initProjection()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, aspect, near, far);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_DEPTH_TEST);
	}
	
	public void setView()
	{		
		//third person view
		glRotatef(-myDigga.rotation + 180, 0, 1, 0);
		glTranslatef(-myDigga.x + 10*myDigga.frontx, -y, -myDigga.z + 10*myDigga.frontz);		
		
		
		//top-down view
		//glRotatef(90, 1, 0, 0);
		//glTranslatef(-myDigga.x, -3*y, -myDigga.z);
	}		
	
	
	
	
	
}
