package Digga;

import java.util.Random;


public class GameWorld {

	final int NUM_DIGGAS = 4;
	final int NUM_GRAVES = 10;
	final int NUM_GHOSTS = 5;
	Digga [] diggas;
	Grave [] graves;
	Ghost [] ghosts;
	
	public GameWorld(int player)
	{
		graves = new Grave[NUM_GRAVES];
		diggas = new Digga[NUM_DIGGAS];
		ghosts = new Ghost[NUM_GHOSTS];
		
	}
	
	
	public void addDigga(int id, String s)
	{
		float [] xzr = parseObjectStream(s);
		diggas[id] = new Digga(this, xzr[0], 1.5f, xzr[1], 2, 3, 1, xzr[2]);
		diggas[id].changedPosition = false;
	}
	
	public void changeDigga(int id, String s)
	{
		float [] xzr = parseObjectStream(s);
		diggas[id].x = xzr[0];
		diggas[id].z = xzr[1];
		diggas[id].rotation = xzr[2];
		diggas[id].changedPosition = false;
	}
	
	public void changeGrave(int id, String s)
	{
		if(s.length() > 10)
		{
			float [] xzr = parseObjectStream(s);
			if(graves[id] == null)
			{
				graves[id] = new Grave(xzr[0], 1.5f, xzr[1], 2, 3, 1, xzr[2]);
			}
			else
			{		
				graves[id].x = xzr[0];
				graves[id].z = xzr[1];
				graves[id].rotation = xzr[2];
			}
		}
		else
		{
			graves[id].digState = Float.parseFloat(s);
		}
	}
	
	public boolean checkCollisions(Digga digga)
	{
		
		for(int i = 0; i < NUM_GRAVES; i++)
		{
			if(graves[i] != null && (Math.pow(graves[i].x - digga.x, 2) + Math.pow(graves[i].z - digga.z,  2)) < digga.collisionRadius)
			{
				if(digga.myBB.collisionCheck(graves[i].boundingBox))
				{
					return true;
				}
			}
		}
		for(int i = 0; i < NUM_DIGGAS; i++)
		{
			if(diggas[i] != null && diggas[i] != digga && (Math.pow(diggas[i].x - digga.x, 2) + Math.pow(diggas[i].z - digga.z,  2)) < digga.collisionRadius)
			{
				if(digga.myBB.collisionCheck(diggas[i].myBB))
				{
					return true;
				}
			}
		}
		
		return false;
		
		
	}
	public void changeGhost(int id, String s)
	{
		if(s.startsWith("GhostAI:"))
		{
			if(ghosts[id] != null)
			{
				ghosts[id].AI = Integer.parseInt(s.substring(s.indexOf(":")), s.length());
			}
			return;
		}
		float [] xzr = parseObjectStream(s);
		if(ghosts[id] == null)
		{
			ghosts[id] = new Ghost(xzr[0], xzr[1], xzr[2]); //GhostObject(xzr[0], 1.5f, xzr[1], 2, 3, 1);
		}
		else
		{		
			ghosts[id].x = xzr[0];
			ghosts[id].z = xzr[1];
			ghosts[id].rotation = xzr[2];
		}
	}
	
	public Grave checkDiggable(Digga digga)
	{
		for(int g = 0; g < NUM_GRAVES; g++)
		{
			if(graves[g] != null)
			{
				BoundingBox plotArea =  graves[g].plotArea;
				if(plotArea.isPointInsideBox(digga.x, digga.z))
				{
					return graves[g];
				}
				
			}
		}
		return null;
			
			
			
	}
	
	public float [] parseObjectStream(String s)
	{
		float [] os = new float[3];
		int index1 = s.indexOf("x: ");
		int index2 = s.indexOf("z");		 
		os[0] = Float.parseFloat(s.substring(index1 + 3, index2));
		index1 = s.indexOf("z: ");
		index2 = s.indexOf("r:");
		os[1] = Float.parseFloat(s.substring(index1 + 3, index2));
		index1 = s.indexOf("r: ");
		os[2] = Float.parseFloat(s.substring(index1 + 3, s.length()));
		return os;
		
	}
	
	public float parseDigState(String s)
	{	
		if(s.indexOf("-1") != -1)
		{
			
			graves[Integer.parseInt(s.substring(5, 6))].changed = true;
		}
		return Float.parseFloat(s.substring(6, s.length()));		
	}
	
}
