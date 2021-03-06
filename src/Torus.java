import java.util.ArrayList;


public class Torus extends Object3D{

	private float radius,raxial;
	private static final float UMIN = (float)-Math.PI;
	private static final float UMAX = (float)Math.PI;
	private static final float VMIN = (float)-Math.PI;
	private static final float VMAX = (float)Math.PI;
	
	MeshSurface body;
	
	public Torus(Point3D center, Material mat, float r, float r_axial, int uStepTotal,
			int vStepTotal) {
		super(center, mat);
		this.radius = r;
		this.raxial = r_axial;
		body = new MeshSurface(center, mat, uStepTotal, vStepTotal);
		fillMesh();
	}

	private void fillMesh() {
		Mesh3D mesh = body.mesh;
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
		for(int i=0;i<mesh.vStepTotal;i++) {
			for(int j=0;j<mesh.uStepTotal;j++) {
				x=(float)center.x+(raxial+radius*cosu)*cosv;
				y=(float)center.y+(raxial+radius*cosu)*sinv;
				z=(float)center.z+radius*sinu;
				mesh.coordinates[i][j] = new Vector3D(x, y, z);
				du.x = -(raxial+radius*cosu)*sinv;
				du.y = (raxial+radius*cosu)*cosv;
				du.z = 0;
				
				dv.x = -radius*sinu*cosv;
				dv.y = -radius*sinu*sinv;
				dv.z = radius*cosu;
				
				du.crossProduct(dv, mesh.normal[i][j]);
				mesh.normal[i][j].normalize();
				angleu += angleuStep;
				cosu = (float)Math.cos(angleu);
				sinu = (float)Math.sin(angleu);
				
			}
			angleu = (float)-Math.PI;
			cosu = (float)Math.cos(angleu);
			sinu = (float)Math.sin(angleu);
			
			anglev += anglevStep;
			cosv = (float)Math.cos(anglev);
			sinv = (float)Math.sin(anglev);
		}
	}

	@Override
	public void drawFlat(ArrayList<LightSource> lightSources, Vector3D viewVec) {
		body.drawFlatShading(lightSources, viewVec);
	}

	@Override
	public void drawGouraud(ArrayList<LightSource> lightSources,
			Vector3D viewVec) {
		body.drawGouraudShading(lightSources, viewVec);
	}

	@Override
	public void drawPhong(ArrayList<LightSource> lightSources, Vector3D viewVec) {
		body.drawPhongShading(lightSources, viewVec);
	}
	
	public void rotate(Quaternion q , Vector3D rotate_center)
	{
		
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
		
		body.mesh.transformMesh(qMatrix , nTrans);
		
		this.center.x = Math.round(vCenter.x);
		this.center.y = Math.round(vCenter.y);
		this.center.z = Math.round(vCenter.z);
		
		body.center = center;

	}
	
	@Override
	public void toggleDiff(boolean isDiff) {
		body.getMat().setDiffuse(isDiff);
		
	}

	@Override
	public void toggleSpec(boolean isSpec) {
		body.getMat().setSpecular(isSpec);
	}

	@Override
	public void translate(float x, float y, float z) {
		center.x += (int)x;
		center.y += (int)y;
		center.z += (int)z;
		
		TransformMatrix translate = new TransformMatrix();
		translate.setMatrix(TransformMatrix.translate(x, y, z));
		TransformMatrix nTrans = new TransformMatrix();
		
		body.mesh.transformMesh(translate, nTrans);
	}

	@Override
	public void scale(float scaleFactor) {
		radius *= scaleFactor;
		raxial *= scaleFactor;

		TransformMatrix transIn = new TransformMatrix();
		transIn.setMatrix(TransformMatrix.translate(- this.center.x, - this.center.y,  - this.center.z));
		TransformMatrix transOut = new TransformMatrix();
		transOut.setMatrix(TransformMatrix.translate( this.center.x, this.center.y, this.center.z));
		
		TransformMatrix scale = new TransformMatrix();
		scale.setMatrix(TransformMatrix.scale(scaleFactor , scaleFactor , scaleFactor));
		
		TransformMatrix transform = new TransformMatrix();
		transform.setMatrix(transOut.getMatrix());
		transform.multiplyMatrix(scale.getMatrix());
		transform.multiplyMatrix(transIn.getMatrix());
		
		TransformMatrix nTrans = new TransformMatrix();
		
		Vector3D vCenter = new Vector3D(this.center.x , this.center.y , this.center.z);
		
		vCenter = transform.multiplyPoint(vCenter);
		
		body.mesh.transformMesh(transform , nTrans);
		
		this.center.x = Math.round(vCenter.x);
		this.center.y = Math.round(vCenter.y);
		this.center.z = Math.round(vCenter.z);
		
		body.center = center;
		
		body.mesh.transformMesh(transform, nTrans);
	}

	@Override
	public String getName() {
		return "torus";
	}
}
