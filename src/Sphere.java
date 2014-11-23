public class Sphere extends Object3D {
	private float radius;
	private static final float UMIN = (float)-Math.PI / 2;
	private static final float UMAX = (float)Math.PI / 2;
	private static final float VMIN = (float)-Math.PI;
	private static final float VMAX = (float)Math.PI;

	public Sphere(Point3D center, float radius, Material material, int totalStepu, int totalStepv) {
		super(center, material);
		this.radius = radius;
		mesh = new Mesh3D(totalStepv, totalStepu);
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
				x = (float)center.x + radius*cosu*cosv;
				y = (float)center.y + radius*cosu*sinv;
				z = (float)center.z + radius*sinu;
				mesh.coordinates[i][j] = new Vector3D(x, y, z);
				mesh.normal[i][j] = new Vector3D(cosu*cosv, cosu*sinv, sinu);
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
