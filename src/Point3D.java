
/**
 *  Name: Ke Peng
 *  Class: CS680 Introduction to Computer Graphics
 *  Assignment: PA4
 *  Due date: 12/03/2014
 *  Description: Point has three dimensions
 */
public class Point3D {
	public int x;
	public int y;
	public int z;
	public ColorType c;
	public Vector3D normal;
	
	public Point3D(int x, int y, int z) {
		this.x = x;
	    this.y = y;
	    this.z = z;
	    c = new ColorType(0.0, 0.0, 0.0);
	}
	
	
	public Point3D(int x, int y, int z, ColorType c, Vector3D normal) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = c;
		this.normal = normal;
	}


	/**
	 * 
	 * <b>Description: Get the distance between two point3D</b></br>
	 *
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 *
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param point3d
	 *   @return
	 */
	public double distance(Point3D point3d) {
		double tmp = (x-point3d.x)*(x-point3d.x) + (y-point3d.y)*(y-point3d.y) + (z-point3d.z)*(z-point3d.z);
		return Math.sqrt(tmp);
	}
	
	
}
