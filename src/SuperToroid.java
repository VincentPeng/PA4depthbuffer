import java.util.ArrayList;


public class SuperToroid extends Object3D {

	private MeshSurface body;
	private float radius;
	private float raxial;
	
	private float scale1,scale2;
	
	public SuperToroid(Point3D center, float r , float raxial , Material mat , int uStepTotal , int vStepTotal) {
		super(center, mat);
		scale1 = 2f;
		scale2 = 2f;

		this.radius = r;
		this.raxial = raxial;
		
		body = new MeshSurface(center, mat, uStepTotal, vStepTotal);
		
		fillMesh();
	}

	private void fillMesh() {
		Mesh3D mesh3d = body.mesh;
		int i,j;		
		float theta, phi;
		float d_theta=(float)(2.0*Math.PI)/ ((float)(mesh3d.vStepTotal-1));
		float d_phi=(float)(2.0 * Math.PI) / ((float)mesh3d.uStepTotal-1);
		float c_theta,s_theta;
		float c_phi, s_phi;
		
		Vector3D du = new Vector3D();
		Vector3D dv = new Vector3D();
		
		for(i=0,theta=(float) - Math.PI; i < mesh3d.vStepTotal; ++i,theta += d_theta)
	    {
			c_theta=(float)Math.cos(theta);
			s_theta=(float)Math.sin(theta);
			
			for(j=0,phi=(float) - (Math.PI);j < mesh3d.uStepTotal; ++j , phi += d_phi)
			{
				// vertex location
				c_phi = (float)Math.cos(phi);
				s_phi = (float)Math.sin(phi);
				
				mesh3d.coordinates[i][j].x = (float) ( (float)center.x + (raxial + radius * c_phi * Math.pow(Math.abs(c_phi), scale1 - 1)) * c_theta * Math.pow(Math.abs(c_theta), scale2 - 1));
				
				mesh3d.coordinates[i][j].y = (float) ( (float)center.y + s_theta * Math.pow(Math.abs(s_theta), scale2 - 1) * (raxial + radius * c_phi * Math.pow(Math.abs(c_phi), scale1 - 1)));

				mesh3d.coordinates[i][j].z = (float) ( (float)center.z + (raxial * s_phi * Math.pow(Math.abs(s_phi), scale1 - 1)));
				
				// compute partial derivatives
				// then use cross-product to get the normal
				// and normalize to produce a unit vector for the normal
				
				du.x = (float) (-(float) scale1 * radius * Math.pow(Math.abs(c_phi) , scale1 - 1) * s_phi * Math.pow(Math.abs(c_theta), scale2 - 1) *c_theta);
				du.y = (float) (-(float) scale1 * radius * Math.pow(Math.abs(c_phi) , scale1 - 1) * s_phi * Math.pow(Math.abs(s_theta), scale2 - 1) *s_theta);
				du.z = (float) ((float) scale1 * raxial * Math.pow(Math.abs(s_phi) , scale1 - 1) * c_phi);
				
				dv.x = -(float)(scale2 * radius * c_phi * Math.pow(Math.abs(c_phi), scale1 - 1) * Math.pow(Math.abs(c_theta), scale2 - 1) * s_theta + 
						 scale2 * raxial * Math.pow(Math.abs(c_theta), scale2 - 1) * s_theta);
				dv.y = (float)(scale2 * radius * c_phi * Math.pow(Math.abs(c_phi), scale1 - 1) * Math.pow(Math.abs(s_theta) , scale2 - 1) * c_theta +
						 scale2 * raxial * Math.pow(Math.abs(s_theta), scale2 - 1) * c_theta);
				dv.z = 0;
				
				
				
				dv.crossProduct(du, mesh3d.normal[i][j]);
				mesh3d.normal[i][j].normalize();		
			}
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

	@Override
	public void toggleDiff(boolean isDiff) {
		material.setDiffuse(isDiff);
	}

	@Override
	public void toggleSpec(boolean isSpec) {
		material.setSpecular(isSpec);
	}

	@Override
	public void rotate(Quaternion q, Vector3D rotate_center) {
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
		return "super toroid";
	}

}
