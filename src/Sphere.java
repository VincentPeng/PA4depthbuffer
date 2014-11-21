public class Sphere extends SketchBase {
	private Point3D center;
	private float radius;
	private Material material;
	private int totalStepu, totalStepv;
	public Mesh3D mesh;
	private static final float UMIN = -PI / 2;
	private static final float UMAX = PI / 2;
	private static final float VMIN = -PI;
	private static final float VMAX = PI;

	public Sphere(Point3D center, float radius, Material material, int totalStepu, int totalStepv) {
		this.center = center;
		this.radius = radius;
		this.totalStepu = totalStepu;
		this.totalStepv = totalStepv;
		this.material = material;
		mesh = new Mesh3D(totalStepu, totalStepv);
		fillMesh();
	}

	public void fillMesh() {
		float x,y,z;
		float angleu = -PI/2;
		float anglev = -PI;
		float angleuStep = (UMAX-UMIN)/(totalStepu-1);
		float anglevStep = (VMAX-VMIN)/(totalStepv-1);
		float cosu = (float)Math.cos(angleu);
		float sinu = (float)Math.sin(angleu);
		float cosv = (float)Math.cos(anglev);
		float sinv = (float)Math.sin(anglev);
		for(int i = 0; i<totalStepu;i++){
			for(int j=0;j<totalStepv;j++) {
				x = (float)center.x + radius*cosu*cosv;
				y = (float)center.y + radius*cosu*sinv;
				z = (float)center.z + radius*sinu;
				mesh.coordinates[i][j] = new Vector3D(x, y, z);
				mesh.normal[i][j] = new Vector3D(cosu*cosv, cosu*sinv, sinu);
				mesh.normal[i][j].normalize();
				anglev += anglevStep;
				cosv = (float)Math.cos(anglev);
				sinv = (float)Math.sin(anglev);
			}
			angleu += angleuStep;
			cosu = (float)Math.cos(angleu);
			sinu = (float)Math.sin(angleu);
		}
	}

	public Material getMaterial() {
		return material;
	}
}
