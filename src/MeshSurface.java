import java.util.ArrayList;
import java.util.Iterator;


public class MeshSurface implements Surface{
	private Material mat;
	public Mesh3D mesh; 
	public Point3D center;
	
	public Material getMat() {
		return mat;
	}

	public MeshSurface(Point3D center, Material mat,int uStepTotal, int vStepTotal) {
		this.center = center;
		this.mat = mat;
		mesh = new Mesh3D(vStepTotal, uStepTotal);
	}

	@Override
	public void drawFlatShading(ArrayList<LightSource> lightSources, Vector3D viewVec) {
		
		Vector3D triangle_normal = new Vector3D();
		int stepu = mesh.uStepTotal;
		int stepv = mesh.vStepTotal;
		Vector3D v0, v1, v2, n0, n1, n2;
		Point3D p0,p1,p2;
		for (int i = 0; i < stepv - 1; i++) {
			for (int j = 0; j < stepu - 1; j++) {
				v0 = mesh.coordinates[i][j];
				v1 = mesh.coordinates[i][j + 1];
				v2 = mesh.coordinates[i + 1][j + 1];
				p0 = new Point3D((int)v0.x, (int)v0.y, (int)v0.z);
				p1 = new Point3D((int)v1.x, (int)v1.y, (int)v1.z);
				p2 = new Point3D((int)v2.x, (int)v2.y, (int)v2.z);
				
				triangle_normal = computeTriangleNormal(v0 , v1 , v2);
				triangle_normal.normalize();
				n0 = n1 = n2 = triangle_normal;
				
				
				if (viewVec.dotProduct(triangle_normal) > 0.0) {
					for (Iterator<LightSource> it = lightSources.iterator(); it
							.hasNext();) {
						LightSource light = (LightSource) it.next();
						light.applyLight(mat, viewVec, n0, p0);
						light.applyLight(mat, viewVec, n1, p1);
						light.applyLight(mat, viewVec, n2, p2);
						
					}
					
					Triangle tri = new Triangle(p0, p1, p2);
					tri.drawFlatShading();
				}

				v0 = mesh.coordinates[i][j];
				v1 = mesh.coordinates[i + 1][j + 1];
				v2 = mesh.coordinates[i + 1][j];

				p0 = new Point3D((int)v0.x, (int)v0.y, (int)v0.z);
				p1 = new Point3D((int)v1.x, (int)v1.y, (int)v1.z);
				p2 = new Point3D((int)v2.x, (int)v2.y, (int)v2.z);
				
				triangle_normal = computeTriangleNormal(v0 , v1 , v2);
				triangle_normal.normalize();
				n0 = n1 = n2 = triangle_normal;
				
				triangle_normal = computeTriangleNormal(v0, v1, v2);
				if (viewVec.dotProduct(triangle_normal) > 0.0) {
					for (Iterator<LightSource> it = lightSources.iterator(); it
							.hasNext();) {
						LightSource light = (LightSource) it.next();
						light.applyLight(mat, viewVec, n0, p0);
						light.applyLight(mat, viewVec, n1, p1);
						light.applyLight(mat, viewVec, n2, p2);
						
					}
					
					Triangle tri = new Triangle(p0, p1, p2);
					tri.drawFlatShading();
				}
			}
		}
	}

	@Override
	public void drawGouraudShading(ArrayList<LightSource> lightSources, Vector3D viewVec) {
		Vector3D triangle_normal = new Vector3D();
		int stepu = mesh.uStepTotal;
		int stepv = mesh.vStepTotal;
		Vector3D v0, v1, v2, n0, n1, n2;
		Point3D p0,p1,p2;
		for (int i = 0; i < stepv - 1; i++) {
			for (int j = 0; j < stepu - 1; j++) {
				v0 = mesh.coordinates[i][j];
				v1 = mesh.coordinates[i][j + 1];
				v2 = mesh.coordinates[i + 1][j + 1];
				p0 = new Point3D((int)v0.x, (int)v0.y, (int)v0.z);
				p1 = new Point3D((int)v1.x, (int)v1.y, (int)v1.z);
				p2 = new Point3D((int)v2.x, (int)v2.y, (int)v2.z);
				
				n0 = mesh.normal[i][j];
				n1 = mesh.normal[i][j + 1];
				n2 = mesh.normal[i + 1][j + 1];
				
				triangle_normal = computeTriangleNormal(v0, v1, v2);
				if (viewVec.dotProduct(triangle_normal) > 0.0) {
					for (Iterator<LightSource> it = lightSources.iterator(); it
							.hasNext();) {
						LightSource light = (LightSource) it.next();
						light.applyLight(mat, viewVec, n0, p0);
						light.applyLight(mat, viewVec, n1, p1);
						light.applyLight(mat, viewVec, n2, p2);
						
					}
					
					Triangle tri = new Triangle(p0, p1, p2);
					tri.drawGouraudShading();
				}

				v0 = mesh.coordinates[i][j];
				v1 = mesh.coordinates[i + 1][j + 1];
				v2 = mesh.coordinates[i + 1][j];

				p0 = new Point3D((int)v0.x, (int)v0.y, (int)v0.z);
				p1 = new Point3D((int)v1.x, (int)v1.y, (int)v1.z);
				p2 = new Point3D((int)v2.x, (int)v2.y, (int)v2.z);
				
				n0 = mesh.normal[i][j];
				n1 = mesh.normal[i][j + 1];
				n2 = mesh.normal[i + 1][j + 1];
				
				triangle_normal = computeTriangleNormal(v0, v1, v2);
				if (viewVec.dotProduct(triangle_normal) > 0.0) {
					for (Iterator<LightSource> it = lightSources.iterator(); it
							.hasNext();) {
						LightSource light = (LightSource) it.next();
						light.applyLight(mat, viewVec, n0, p0);
						light.applyLight(mat, viewVec, n1, p1);
						light.applyLight(mat, viewVec, n2, p2);
						
					}
					
					Triangle tri = new Triangle(p0, p1, p2);
					tri.drawGouraudShading();
				}
			}
		}
	}
	

	// helper method that computes the unit normal to the plane of the triangle
	// degenerate triangles yield normal that is numerically zero
	private Vector3D computeTriangleNormal(Vector3D v0, Vector3D v1, Vector3D v2) {
		Vector3D e0 = v1.minus(v2);
		Vector3D e1 = v0.minus(v2);
		Vector3D norm = e0.crossProduct(e1);

		if (norm.magnitude() > 0.000001)
			norm.normalize();
		else
			// detect degenerate triangle and set its normal to zero
			norm.set((float) 0.0, (float) 0.0, (float) 0.0);

		return norm;
	}

	@Override
	public void drawPhongShading(ArrayList<LightSource> lightSources, Vector3D viewVec) {
		int stepu = mesh.uStepTotal;
		int stepv = mesh.vStepTotal;
		for(int i = 0 ; i < stepv - 1 ; i++)
		{
			for(int j = 0; j < stepu-1 ; j++)
			{
				Point3D p0,p1,p2; // 3 vertices for triangles; 
				Vector3D v0,v1,v2; 
				
				
				// draw the first triangle of the mesh
				v0 = mesh.coordinates[i][j];
				v1 = mesh.coordinates[i][j+1];
				v2 = mesh.coordinates[i+1][j+1];

				p0 = new Point3D((int)v0.x , (int)v0.y , (int)v0.z);
				p1 = new Point3D((int)v1.x , (int)v1.y , (int)v1.z);
				p2 = new Point3D((int)v2.x , (int)v2.y , (int)v2.z);
				
				p0.normal = mesh.normal[i][j];
				p1.normal = mesh.normal[i][j+1];
				p2.normal = mesh.normal[i+1][j+1];
				
				
				Triangle tri = new Triangle(p0 , p1 , p2);
				
				tri.drawPhongShading(mat,lightSources, viewVec);
								
				// draw the second triangle of the mesh
				v0 = mesh.coordinates[i][j];
				v1 = mesh.coordinates[i+1][j+1];
				v2 = mesh.coordinates[i+1][j];
				
				p0 = new Point3D((int)v0.x , (int)v0.y , (int)v0.z);
				p1 = new Point3D((int)v1.x , (int)v1.y , (int)v1.z);
				p2 = new Point3D((int)v2.x , (int)v2.y , (int)v2.z);
				
				p0.normal = mesh.normal[i][j];
				p1.normal = mesh.normal[i+1][j+1];
				p2.normal = mesh.normal[i+1][j];
				
				tri = new Triangle(p0 , p1 , p2);				
				tri.drawPhongShading(mat ,lightSources, viewVec);
			}
		}
	}
}
