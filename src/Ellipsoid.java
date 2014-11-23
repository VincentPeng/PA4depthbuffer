
public class Ellipsoid extends Object3D{

	private float rx,ry,rz;
	private static final float UMIN = (float)-Math.PI / 2;
	private static final float UMAX = (float)Math.PI / 2;
	private static final float VMIN = (float)-Math.PI;
	private static final float VMAX = (float)Math.PI;
	public Ellipsoid(Point3D center, Material mat, float rx, float ry, float rz, int uStepTotal,
			int vStepTotal) {
		super(center, mat);
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		mesh = new Mesh3D(vStepTotal, uStepTotal);
		fillMesh();
	}


	public void fillMesh() {
		float x,y,z;
		float angleu = (float)-Math.PI/2;
		float anglev = (float)-Math.PI;
		float angleuStep = (UMAX-UMIN)/(mesh.uStepTotal-1);
		float anglevStep = (VMAX-VMIN)/(mesh.vStepTotal-1);
		float cosu = (float)Math.cos(angleu);
		float sinu = (float)Math.sin(angleu);
		float cosv = (float)Math.cos(anglev);
		float sinv = (float)Math.sin(anglev);
		for(int i=0;i<mesh.vStepTotal;i++) {
			
			for(int j = 0; j<mesh.uStepTotal;j++){
				x = (float)center.x + rx*cosu*cosv;
				y = (float)center.y + ry*cosu*sinv;
				z = (float)center.z + rz*sinu;
				mesh.coordinates[i][j] = new Vector3D(x, y, z);
				mesh.normal[i][j] = new Vector3D(rx*cosu*cosv, ry*cosu*sinv, rz*sinu);
				mesh.normal[i][j].normalize();
				angleu += angleuStep;
				cosu = (float)Math.cos(angleu);
				sinu = (float)Math.sin(angleu);
			}
			angleu = (float)-Math.PI/2;
			cosu = (float)Math.cos(angleu);
			sinu = (float)Math.sin(angleu);
			anglev += anglevStep;
			cosv = (float)Math.cos(anglev);
			sinv = (float)Math.sin(anglev);
			angleu = (float)-Math.PI/2;
		}
	}

}
