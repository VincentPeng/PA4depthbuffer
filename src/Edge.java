
public class Edge {
	public Point3D p0,p1;
	private int deltaY;
	private int deltaX;
	
	private float dX;
	private float dY;
	private float dZ;
	
	private int startX;
	private int startY;
	
	private int endX;
	private int endY;
	
	
	public Edge(Point3D _p0 , Point3D _p1)
	{
		p0 = _p0;
		p1 = _p1;
		
		deltaX = Math.abs(p1.x - p0.x);
		deltaY = Math.abs(p1.y - p0.y);
		
		if(p0.y <= p1.y)
		{
			startY = p0.y;
			startX = p0.x;
			endY = p1.y;
			endX = p1.x;
		}
		else
		{
			startY = p1.y;
			startX = p1.x;
			endY = p0.y;
			endX = p0.x;
		}
		
	}
	
	public int getDeltaX()
	{
		return deltaX;
	}
	
	public int getDeltaY()
	{
		return deltaY;
	}
	
	public int getXfromY(float y)
	{
		float x = 0;

		float dX = p1.x - p0.x;
		float dY = p1.y - p0.y;

		float offsetY = y - p0.y;
		float offsetX = offsetY * dX / dY;

		x = p0.x + offsetX;

		return Math.round(x);
	}
	
	public int getZfromY(float y)
	{
		float z = 0;

		float dZ = p1.z - p0.z;
		float dY = p1.y - p0.y;

		float offsetY = y - p0.y;
		float offsetZ = offsetY * dZ / dY;

		z = p0.z + offsetZ;

		return Math.round(z);
		
	}
	
	public int getZfromX(float x)
	{
		float z = 0;

		float dZ = p1.z - p0.z;
		float dX = p1.x - p0.x;

		float offsetX = x - p0.x;
		float offsetZ = offsetX * dZ / dX;

		z = p0.z + offsetZ;

		return Math.round(z);
	}
	
	public Vector3D getNormalFromY(float y)
	{
		float dY = p1.y - p0.y;
		
		float dnx = p1.normal.x - p0.normal.x;
		float dny = p1.normal.y - p0.normal.y;
		float dnz = p1.normal.z - p0.normal.z;
		
		float offsetY = y - p0.y;
		
		float offset_nx = dnx * offsetY / dY;
		float offset_ny = dny * offsetY / dY;
		float offset_nz = dnz * offsetY / dY;
		
		Vector3D normal = new Vector3D(0 , 0 , 0);
		
		normal.x = p0.normal.x + offset_nx;
		normal.y = p0.normal.y + offset_ny;
		normal.z = p0.normal.z + offset_nz;
		
		return normal;
		
	}
	
	public Vector3D getNormalFromX(float x)
	{
		float dX = p1.x - p0.x;
		
		float dnx = p1.normal.x - p0.normal.x;
		float dny = p1.normal.y - p0.normal.y;
		float dnz = p1.normal.z - p0.normal.z;
		
		float offsetX = x - p0.x;
		
		float offset_nx = dnx * offsetX / dX;
		float offset_ny = dny * offsetX / dX;
		float offset_nz = dnz * offsetX / dX;
		
		Vector3D normal = new Vector3D(0 , 0 , 0);
		
		normal.x = p0.normal.x + offset_nx;
		normal.y = p0.normal.y + offset_ny;
		normal.z = p0.normal.z + offset_nz;
		
		return normal;
		
	}
	
	
	public ColorType getColorFromY(float y)
	{
		float dY = p1.y - p0.y;
		
		float dR = p1.c.r - p0.c.r;
		float dG = p1.c.g - p0.c.g;
		float dB = p1.c.b - p0.c.b;
		
		float offsetY = y - p0.y;
		
		float offsetR = dR * offsetY / dY;
		float offsetG = dG * offsetY / dY;
		float offsetB = dB * offsetY / dY;
		
		ColorType color = new ColorType(0.0 , 0.0 , 0.0);
		
		color.r = p0.c.r + offsetR;
		color.g = p0.c.g + offsetG;
		color.b = p0.c.b + offsetB;
		
		
		return color;
	}
	
	public ColorType getColorFromX(float x)
	{
		float dX = p1.x - p0.x;
		
		float dR = p1.c.r - p0.c.r;
		float dG = p1.c.g - p0.c.g;
		float dB = p1.c.b - p0.c.b;
		
		float offsetY = x - p0.x;
		
		float offsetR = dR * offsetY / dX;
		float offsetG = dG * offsetY / dX;
		float offsetB = dB * offsetY / dX;
		
		ColorType color = new ColorType(0.0 , 0.0 , 0.0);
		
		color.r = p0.c.r + offsetR;
		color.g = p0.c.g + offsetG;
		color.b = p0.c.b + offsetB;
		
		
		return color;
	}

	public int getStartY()
	{
		return startY;
		
	}
	
	public int getEndY()
	{
		return endY;
		
	}
	
}
