package Digga;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


public class Digga 
{
	GameWorld myWorld;
	float xSpawn;
	float zSpawn;
	float x;
	float y;
	float z;
	float length;
	float width;
	float height;
	float rotation;
	float frontx;
	float frontz;
	float collisionRadius;
	
	
	boolean changedPosition;
	
	
	public BoundingBox myBB;
	
	public Digga(GameWorld myWorld, float x, float y, float z, float length, float height, float width, float rotation)
	{
		this.myWorld = myWorld;
		this.x = x;
		this.y = y;
		this.z = z;
		xSpawn = x;
		zSpawn = z;
		this.rotation = rotation;
		this.length = length;
		this.width = width;
		this.height = height;
		myBB = new BoundingBox(0, 0, length, width);
		myBB.setBounds(x, z, rotation);
		setVectors();
		
		collisionRadius = length*length + height*height + .1f;
		//changedPosition = false;
		//myBB.collisionCheck(test);
	}
	
	public void diggaLoop()
	{
		
		if(ghostAround())
		{
			x = xSpawn;
			z = zSpawn;
			try {
				GameFrame.client.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			forward(.005f);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			forward(-.005f);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			turn(-.05f);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			turn(.05f);
		}
		//myWorld.checkCollisions(this);
		//System.out.println(x + " " + z + " " + rotation);
		
		Grave diggable = myWorld.checkDiggable(this);
		while(Mouse.next() && diggable != null)
		{
			
			if(Mouse.getEventButtonState())
			{
				//if(Mouse.isButtonDown(0))
			}
			else
			{
				if(Mouse.getEventButton() == 0 && diggable.digState > -1)
				{
					//Sound.play("dig.wav");
					diggable.digState -= 1;
				}
				
			}
		}
		
		
	}
	
	public void setVectors()
	{
		frontx = (float)(Math.sin(Math.toRadians(rotation)));
		frontz = (float)(Math.cos(Math.toRadians(rotation)));		
	}
	
	public void forward(float speed)
	{
		x += frontx*speed;
		z += frontz*speed;
		myBB.setBounds(x, z, rotation);
		while(myWorld.checkCollisions(this)|| outOfBounds())
		{
			x -= frontx*speed/10f;
			z -= frontz*speed/10f;
			myBB.setBounds(x, z, rotation);
		
		}	
		changedPosition = true;
	}
	
	public void turn(float ang)
	{
		rotation += ang;
		myBB.setBounds(x, z, rotation);
		while(myWorld.checkCollisions(this))
		{
			rotation -= ang/10f;
			myBB.setBounds(x, z, rotation);
		}
		setVectors();	
		changedPosition = true;
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
	
	public boolean outOfBounds()
	{
		for(int i = 0; i < 4; i++)
		{
			if(myBB.x[i] < -GameFrame.WORLD_LENGTH || myBB.x[i] > GameFrame.WORLD_LENGTH || myBB.z[i] < -GameFrame.WORLD_WIDTH || myBB.z[i] > GameFrame.WORLD_WIDTH)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean ghostAround()
	{
		for(int i = 0; i < myWorld.NUM_GHOSTS; i++)
		{
			if(myWorld.ghosts[i] != null)
			{				
				if((Math.pow(myWorld.ghosts[i].x - x, 2) + Math.pow(myWorld.ghosts[i].z - z, 2)) < (1f))
				{
					return true;
				}
			}
		}
		return false;
	}
	
}
