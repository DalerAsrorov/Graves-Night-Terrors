package Digga;



public class BoundingBox {

	float [] cornerx;
	float [] cornerz;
	float [] x;
	float [] z;
	float rotation;
	
	public BoundingBox(float centerx, float centerz, float length, float width)
	{
		float l = length/2f;
		float w = width/2f;
		cornerx = new float[4];
		cornerz = new float[4];
		x = new float[4];
		z = new float[4];
		
		cornerx[0] = centerx - l;
		cornerz[0] = centerz + w;
		cornerx[1] = centerx + l;
		cornerz[1] = centerz + w;
		cornerx[2] = centerx + l;
		cornerz[2] = centerz - w;
		cornerx[3] = centerx - l;
		cornerz[3] = centerz - w;
		
		setBounds(centerx, centerz, 0);
		
	}
	
	public void setBounds(float offsetx, float offsetz, float rotation)
	{
		rotation *= Math.PI/180f;
		
		for(int i = 0; i < 4; i++)
		{			
			x[i] = offsetx - (float)Math.cos(rotation)*(cornerx[i]) + (float)Math.sin(rotation)*(cornerz[i]);
			z[i] = offsetz + (float)Math.sin(rotation)*(cornerx[i]) + (float)Math.cos(rotation)*(cornerz[i]);
		}
		this.rotation = rotation;
		
	}
	
	public boolean collisionCheck(BoundingBox other)
	{
		
		for(int i = 0; i < 4; i++)
		{
			if(separatingAxis(other, i))
			{
				//System.out.println("false");
				return false;
				
			}
			
		
		}
		//System.out.println("true");
		return true;		
	}
	
	
	public boolean separatingAxis(BoundingBox other, int side)
	{
		float xAxis;
		float zAxis;
		if(side < 2)
		{
			xAxis = x[side + 1] - x[side];
			zAxis = z[side + 1] - z[side];
		}
		else
		{
			xAxis = x[side - 1] - x[side - 2];
			zAxis = z[side - 1] - z[side - 2];
		}
			
		float magnitude = (float)Math.sqrt(xAxis*xAxis + zAxis*zAxis);	
		xAxis /= magnitude;
		zAxis /= magnitude;
			
		
		float myEnd1 = x[side]*xAxis + z[side]*zAxis;
		float myEnd2 = x[(side + 2) % 4]*xAxis + z[(side + 2) % 4]*zAxis;
		
		float myMin;
		float myMax;
		
		if(myEnd1 > myEnd2)
		{
			myMin = myEnd2;
			myMax = myEnd1;
		}
		else
		{
			myMin = myEnd1;
			myMax = myEnd2;
		}
		
		float otherMin = 100;
		float otherMax = -100;
		float proj;
		for(int i = 0; i < 4; i++)
		{
			if(side < 2)
			{
				proj = other.x[i]*xAxis + other.z[i]*zAxis;
			}
			else
			{
				proj =x[i]*xAxis + z[i]*zAxis;
			}
			
			if(proj < otherMin)
			{
				otherMin = proj;
			}
			if(proj > otherMax)
			{
				otherMax = proj;
			}
		}
		
		
		if(((myMax <= otherMin) || (otherMax <= myMin) || (myMin >= otherMax) || (otherMin >= myMax)))
		{
			//System.out.println("true");
			return true;
		}
		//System.out.println("false");
		return false;
	}
	
	public boolean isPointInsideBox(float pointx, float pointz)
	{
		for(int i = 1; i < 4; i += 2)
		{					
			float sidex = x[i] - x[0];
			float sidez = z[i] - z[0];
				
			float distx = pointx - x[0];
			float distz = pointz - z[0];
				
			float dot1 = distx*sidex + distz*sidez;
			float dot2 = sidex*sidex + sidez*sidez;
				
			if((dot1 > 0 && dot1 < dot2))
			{
				if(i == 3)
				{
					return true;
				}						
			}					
			else
			{
				break;
			}
				
		}
		return false;
	}
	
}
