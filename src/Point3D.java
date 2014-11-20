
/**
 *  Name: Ke Peng
 *  Class: CS680 Introduction to Computer Graphics
 *  Assignment: PA4
 *  Due date: 11/18/2014
 *  Description: Point has three dimensions
 */
public class Point3D {
	public double x;
	public double y;
	public double z;
	
	public Point3D(double x, double y, double z) {
		this.x = x;
	    this.y = y;
	    this.z = z;
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
