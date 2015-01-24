package Digga;



public class Rectangle 
{
	public float xMin;
	public float zMin;
	public float xMax;
	public float zMax;
	
	public Rectangle(float x1, float x2, float z1, float z2)
	{
		if(x1 <= x2)
		{
			xMin = x1;
			xMax = x2;
		}
		else
		{
			xMin = x2;
			xMax = x1;
		}
		
		if(z1 <= z2)
		{
			zMin = z1;
			zMax = z2;
		}
		else
		{
			zMin = z2;
			zMax = z1;
		}
	}
}
