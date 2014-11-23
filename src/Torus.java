
public class Torus extends Object3D{

	private float r,r_axial;
	private static final float UMIN = (float)-Math.PI;
	private static final float UMAX = (float)Math.PI;
	private static final float VMIN = (float)-Math.PI;
	private static final float VMAX = (float)Math.PI;
	
	public Torus(Point3D center, Material mat, float r, float r_axial, int uStepTotal,
			int vStepTotal) {
		super(center, mat);
		this.r = r;
		this.r_axial = r_axial;
		mesh = new Mesh3D( vStepTotal, uStepTotal);
		fillMesh();
	}

	private void fillMesh() {
		float x,y,z;
		float angleu = (float)-Math.PI;
		float anglev = (float)-Math.PI;
		float angleuStep = (UMAX-UMIN)/(mesh.uStepTotal-1);
		float anglevStep = (VMAX-VMIN)/(mesh.vStepTotal-1);
		float cosu = (float)Math.cos(angleu);
		float sinu = (float)Math.sin(angleu);
		float cosv = (float)Math.cos(anglev);
		float sinv = (float)Math.sin(anglev);
		
		Vector3D du = new Vector3D();
		Vector3D dv = new Vector3D();
		for(int i=0;i<mesh.uStepTotal;i++) {
			for(int j=0;j<mesh.vStepTotal;j++) {
				x=(float)center.x+(r_axial+r*cosu)*cosv;
				y=(float)center.y+(r_axial+r*cosu)*sinv;
				z=(float)center.z+r*sinu;
				mesh.coordinates[i][j] = new Vector3D(x, y, z);
				du.x = -(r_axial+r*cosu)*sinv;
				du.y = (r_axial+r*cosu)*cosv;
				du.z = 0;
				
				dv.x = -r*sinu*cosv;
				dv.y = -r*sinu*sinv;
				dv.z = r*cosu;
				
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
			cosu = (float)Math.cos(angleu);
			sinu = (float)Math.sin(angleu);
		}
	}
	
	
}
