package Digga;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.Random;

import org.lwjgl.util.glu.Sphere;

public class Ghost 
{
	
	
	float x;
	float z;
	float y = 3f;
	float xSpawn;
	float zSpawn;
	float speedInv = 200f;
	float xSpeed;
	float zSpeed;
	public static float patrolRadius = 10f;
	float pursuitRange = 5f;
	float rotation;
	float AI;
	

	public Ghost(float xSpawn, float zSpawn, float rotation)
	{
		x = xSpawn;
		z = zSpawn;
		this.xSpawn = x;
		this.zSpawn = z;
		this.rotation = rotation;
		
	}
	
	public Ghost(float xSpawn, float zSpawn, float rotation, int AI)
	{
		x = xSpawn;
		z = zSpawn;
		this.xSpawn = x;
		this.zSpawn = z;
		this.rotation = rotation;
		this.AI = AI;
		xSpeed = (float)Math.cos(rotation*Math.PI/180f)/speedInv;
		zSpeed = (float)Math.sin(rotation*Math.PI/180f)/speedInv;
		AI = 2;
	}
	
	public void setAI(int AI)
	{
		
		System.out.println(AI);
	}
	
	public void patrol()
	{	
	
		for(int i = 0; i < Server.realWorld.NUM_DIGGAS; i++)
		{
			Digga digga = Server.realWorld.diggas[i];
			if((Math.pow(digga.x - x, 2) + Math.pow(digga.z - z, 2)) < (pursuitRange*pursuitRange))
			{
				xSpeed = digga.x - x;
				zSpeed = digga.z - z;
				float speed = (float)Math.sqrt(xSpeed*xSpeed + zSpeed*zSpeed)*speedInv;
				xSpeed /= speed;
				zSpeed /= speed;
				if(AI == 1)
				{
					xSpeed *= 2;
					zSpeed *= 2;
				}
				break;
			}
		}
		
		if((Math.pow(x - xSpawn, 2) + Math.pow(z - zSpawn, 2)) > (patrolRadius*patrolRadius))
		{
			if(AI == 1)
			{
				xSpeed *= -1;
				zSpeed *= -1;
			}
			else if(AI == 2)
			{
				Random rand = new Random();
				rotation = rand.nextFloat()*rand.nextInt((int)rotation + 270) - 180f;
				xSpeed = (float)Math.cos(rotation*Math.PI/180f)/speedInv;
				zSpeed = (float)Math.sin(rotation*Math.PI/180f)/speedInv;
			}
		}
			
		x += xSpeed;
		z += zSpeed;
	}
	
	
	public void ghostLoop()
	{

		renderSphere(x, y, z);
		//System.out.println("ok");
	}
	
	public void renderSphere(float x, float y, float z) 
	{
		     glPushMatrix();
		     glTranslatef(x, y, z);
		     Sphere s = new Sphere();
		    // if(AI == 1)
		    // {
		    	 glColor3f(1f, 1f, 1f);
		    	 s.draw(2f, 16, 16);
		     /*}
		     else if(AI == 2)
		     {
		    	 glColor3f(.1f, 0, 0);
		    	 s.draw(2f, 16, 16);
		     }*/
		     
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
	
	
	public String createAIStream()
	{
		String os = "GhostAI:" + AI;
		return os;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*boolean normal = false;
	boolean tracking = false;
	boolean attack = false;
	boolean killed = false;
	final float speed = (float) 0.032;
	float newX = 0;
	float newY = 0;
	int newdirection = 5;
	float newremain = 0;
	boolean changedirection = false;
	
	public Ghost(float x, float y, float coorX, float coorY, String phase, int direction, float remain){
		double distance =  Math.sqrt((Math.abs(x - coorX) * Math.abs(x-coorX)) + (Math.abs(y-coorY) * Math.abs(y - coorY)));
		float deltaX = coorX - x ; 
		float deltaY = y - coorY;
		float degree = (float) Math.toDegrees(Math.acos(deltaX/distance));
		newX = x;
		newY = y;
		if(phase.equals("normal")){
			if(distance < 15){
				newX = x;
				newY = y;
				tracking = true;
			}else{
				newX = x;
				newY = y;
				
				if(direction == 1){
					if(newX > remain){
						newX = (float) (x -speed);
						newY = (float) (y );
						if(newX <= -50){
							changedirection = true;
							newX = (float) (x);
							newY = (float) (y);
						}
					}else{
						changedirection = true;
					}
				
				}else if(direction == 2){
					if(newY > remain){
					newX = (float) (x);
					newY = (float) (y - speed);
					if(newY <= -50){
						changedirection = true;
						newX = (float) (x);
						newY = (float) (y);
					}
					}else{
						changedirection = true;
					}
				}else if(direction == 3){
					if(newY < remain){
					newX = (float) (x);
					newY = (float) (y +  speed);
					if(newY >= 50){
						changedirection = true;
						newX = (float) (x);
						newY = (float) (y);
					}
					}else{
						changedirection = true;
					}
				}else if(direction == 0){
					if(newX < remain){
					newX = (float) (x + speed);
					newY = (float) (y);
					if(newX >= 50){
						changedirection = true;
						newX = (float) (x);
						newY = (float) (y);
					}
					}else{
						changedirection = true;
					}
				}
				
				if(changedirection == true){
					newdirection =  (int) (Math.random() * 4);
					if(newdirection == 0){
						newremain = newX + 10;
					}else if(newdirection == 1){
						newremain = newX - 10;
					}else if(newdirection == 2){
						newremain = newY - 10;
					}else if(newdirection == 3){
						newremain = newY + 10;
					}
				}
				
			}
		}else if(phase.equals("tracking")){
			
			if(distance < 20){
				if(distance < 2){
					newX = x;
					newY = y;
					attack = true;
				}else if(degree > 90 && (degree < 180 ||degree == 180)&& coorY < y){
				degree -= 90;
				float goY = (float) (speed*Math.cos(Math.toRadians(degree)));
				float goX = (float) (speed*Math.sin(Math.toRadians(degree)));
				newX = (float) (x - goX);
				newY = (float) (y - goY);
			}else if((degree <90 || degree == 90) && coorY < y){
				float goY = (float) (speed*Math.cos(Math.toRadians(degree)));
				float goX = (float) (speed*Math.sin(Math.toRadians(degree)));
				newX = (float) (x + goX);
				newY = (float) (y - goY);
			}else if( degree > 90 && (degree < 180 ||degree == 180) && y < coorY){
				float goX = (float) (speed*Math.cos(Math.toRadians(degree)));
				float goY = (float) (speed*Math.sin(Math.toRadians(degree)));
				newX = (float) (x + goX);
				newY = (float) (y + goY);
			}else if((degree <90 || degree == 90) && y < coorY){
				float goX = (float) (speed*Math.cos(Math.toRadians(degree)));
				float goY = (float) (speed*Math.sin(Math.toRadians(degree)));
				newX = (float) (x + goX);
				newY = (float) (y + goY);
			}
			}else{
				newX = x;
				newY = y;
				normal = true;
			}
			
		}else if(phase.equals("attack")){

				newX = x;
				newY = y;
				killed = true;
				normal = true;
			
		}else{
			newX = x;
			newY = y;
		}
	
	}
}

/*public class Ghost {

	float x;
	float y;
	float z;
	int type;
	
	boolean normal = false;
	boolean damage = false;
	float r = 0;
	int randomDirection = 0;
	float newX = 0;
	float newY = 0;
	
	public Ghost(int type)
	{
		if(type == 3)
		{
			AI3();
		}
	}	
	
	public void AI3(Digga [] diggas, String phase, float remain, int direction)
	{
		for(int i = 0; i < G)
		double distance =  Math.sqrt((Math.abs(x - coorX) * Math.abs(x-coorX)) + (Math.abs(y-coorY) * Math.abs(y - coorY)));
	
		if(phase.equals("normal")){
			if(distance < 100){
				damage = true;
			}
			
			if(remain > 0 && x < 600 && y < 600 && x > 0 && y > 0){
				
				
				if(direction == 1){
				newX = (int) (x - 10);
				newY = y;
				}else if(direction == 2){
					newX = (int) (x + 10);
					newY = y;
				}else if(direction == 3){
					newX = x;
					newY = (int) (y + 10);
				}else if(direction == 0){
					newX =x;
					newY = (int) (y - 10);
				}
				
				
				randomDirection = direction;
				r = remain - 10;
			}else if (remain == 0 || x == 600 || y == 600 || x == 0 || y == 0){
				if(x == 600 ){
					newX = x - 10;
					newY = y;
				}else if(y == 600){
					newX = x;
					newY = y - 10;
				}else if(x == 0){
					newX = x + 10;
					newY = y;
				}else if(y == 0){
					newX = x;
					newY = y + 10;
				}else{
				newX = x;
				newY = y;
				}
				randomDirection = (int) (Math.random() * 4);
				int randomDistance = (int) (Math.random() * 20);
				r = (10 * randomDistance) + 100;
			}
		}else{
			newX = x;
			newY = y;
		}
	
	}*/
}
