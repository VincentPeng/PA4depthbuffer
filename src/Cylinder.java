import java.util.ArrayList;


public class Cylinder extends Object3D {

	private float rx,ry,umax,umin;
	private static final float VMIN = (float)-Math.PI;
	private static final float VMAX = (float)Math.PI;
	private MeshSurface sideCurve;
	private FlatSurface topCap;
	private FlatSurface bottomCap;
	public Cylinder(Point3D center, Material mat, float rx, float ry, float umax, int uStepTotal,
			int vStepTotal, Vector3D viewVec) {
		super(center, mat);
		this.rx = rx;
		this.ry = ry;
		this.umax = umax;
		this.umin = -umax;
		sideCurve = new MeshSurface(center, mat, uStepTotal, vStepTotal);
		topCap = new FlatSurface(new Vector3D(center.x, center.y, (int)(center.z+umax)),
				new Vector3D(0, 0, 1), viewVec, mat, vStepTotal, rx, ry);
		bottomCap = new FlatSurface(new Vector3D(center.x, center.y, center.z-umax),
				new Vector3D(0, 0, -1), viewVec, mat, vStepTotal, rx, ry);
		fillMesh();
	}
	
	private void fillMesh() {
		float x,y,z;
		Mesh3D mesh = sideCurve.mesh;
		float angleu = (float)umin;
		float anglev = (float)-Math.PI;
		float angleuStep = (umax-umin)/(mesh.uStepTotal-1);
		float anglevStep = (VMAX-VMIN)/(mesh.vStepTotal-1);
		float cosv = (float)Math.cos(anglev);
		float sinv = (float)Math.sin(anglev);
		
		Vector3D du = new Vector3D();
		Vector3D dv = new Vector3D();
		for(int i=0;i<mesh.uStepTotal;i++) {
			for(int j=0;j<mesh.vStepTotal;j++) {
				x=(float)center.x+rx*cosv;
				y=(float)center.y+ry*sinv;
				z=(float)center.z+angleu;
				System.out.printf("%f,%f,%f\n", x,y,z);
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
	}

	@Override
	public void drawFlat(ArrayList<LightSource> lightSources, Vector3D viewVec) {
		sideCurve.drawFlatShading(lightSources, viewVec);
		topCap.drawFlatShading(lightSources, viewVec);
		bottomCap.drawFlatShading(lightSources, viewVec);
	}

	@Override
	public void drawGouraud(ArrayList<LightSource> lightSources,
			Vector3D viewVec) {
		sideCurve.drawGouraudShading(lightSources, viewVec);
		topCap.drawGouraudShading(lightSources, viewVec);
		bottomCap.drawGouraudShading(lightSources, viewVec);
	}

	@Override
	public void drawPhong(ArrayList<LightSource> lightSources, Vector3D viewVec) {
		sideCurve.drawPhongShading(lightSources, viewVec);
		topCap.drawPhongShading(lightSources, viewVec);
		bottomCap.drawPhongShading(lightSources, viewVec);
	}
	
	public void rotate(Quaternion q , Vector3D rotate_center)
	{
		topCap.fan.rotate(q, rotate_center);
		bottomCap.fan.rotate(q, rotate_center);
		
		TransformMatrix qMatrix = new TransformMatrix();
		TransformMatrix transposeQua = new TransformMatrix();
		
		float [] transIn = TransformMatrix.translate(- rotate_center.x,  - rotate_center.y,  - rotate_center.z);
		float [] transOut = TransformMatrix.translate(rotate_center.x, rotate_center.y, rotate_center.z);
		
		transposeQua.setMatrix(q.toMatrix());
		
		
		qMatrix.setMatrix(transOut);
		
		qMatrix.multiplyMatrix(transposeQua.getTranspose());
	
		qMatrix.multiplyMatrix(transIn);
		
		TransformMatrix nTrans = new TransformMatrix();
		nTrans.setMatrix(transposeQua.getTranspose());
		
		Vector3D vCenter = new Vector3D(this.center.x , this.center.y , this.center.z);
		
		vCenter = qMatrix.multiplyPoint(vCenter);
		
		sideCurve.mesh.transformMesh(qMatrix , nTrans);
		
		this.center.x = Math.round(vCenter.x);
		this.center.y = Math.round(vCenter.y);
		this.center.z = Math.round(vCenter.z);
		
		sideCurve.center = center;
	}

	@Override
	public void toggleDiff(boolean isDiff) {
		sideCurve.getMat().setDiffuse(isDiff);
		topCap.getMat().setDiffuse(isDiff);
		bottomCap.getMat().setDiffuse(isDiff);
	}

	@Override
	public void toggleSpec(boolean isSpec) {
		sideCurve.getMat().setSpecular(isSpec);
		topCap.getMat().setSpecular(isSpec);
		bottomCap.getMat().setSpecular(isSpec);
	}
	
	

}
