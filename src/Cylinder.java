
public class Cylinder extends Object3D {

	private float rx,ry,umax,umin;
	private static final float VMIN = (float)-Math.PI;
	private static final float VMAX = (float)Math.PI;
	public Cylinder(Point3D center, Material mat, float rx, float ry, float umax, int uStepTotal,
			int vStepTotal) {
		super(center, mat);
		this.rx = rx;
		this.ry = ry;
		this.umax = umax;
		this.umin = -umax;
		mesh = new Mesh3D(vStepTotal, uStepTotal);
		fillMesh();
	}
	
	private void fillMesh() {
		float x,y,z;
		float angleu = (float)umin;
		float anglev = (float)-Math.PI;
		float angleuStep = (umax-umin)/(mesh.uStepTotal-3);
		float anglevStep = (VMAX-VMIN)/(mesh.vStepTotal-1);
		float cosv = (float)Math.cos(anglev);
		float sinv = (float)Math.sin(anglev);
		
		Vector3D du = new Vector3D();
		Vector3D dv = new Vector3D();
		for(int i=1;i<mesh.uStepTotal-1;i++) {
			for(int j=0;j<mesh.vStepTotal;j++) {
				x=(float)center.x+rx*cosv;
				y=(float)center.y+ry*sinv;
				z=(float)center.z+angleu;
				mesh.coordinates[i][j] = new Vector3D(x, y, z);
				du.x = -rx*sinv;
				du.y = ry*cosv;
				du.z = 0;
				
				dv.x = 0;
				dv.y = 0;
				dv.z = 1;
				
				dv.crossProduct(du, mesh.normal[i][j]);
				mesh.normal[i][j].normalize();
				anglev += anglevStep;
				cosv = (float)Math.cos(anglev);
				sinv = (float)Math.sin(anglev);
				
			}
			anglev = (float)-Math.PI;
			cosv = (float)Math.cos(anglev);
			sinv = (float)Math.sin(anglev);
			
			angleu += angleuStep;
		}
		
		anglev = (float)-Math.PI;
		cosv = (float)Math.cos(anglev);
		sinv = (float)Math.sin(anglev);
		
		x=(float)center.x;
		y=(float)center.y;
		for(int j=0;j<mesh.vStepTotal;j++) {
			int i = 0;
			z=(float)center.z+umin;
			mesh.normal[i][j] = new Vector3D(0, 0, -1);
			mesh.coordinates[i][j] = new Vector3D(x,y,z);
			
			i = mesh.uStepTotal-1;
			z=(float)center.z+umax;
			mesh.normal[i][j] = new Vector3D(0, 0, 1);
			mesh.coordinates[i][j] = new Vector3D(x,y,z);
		}
	}

}
