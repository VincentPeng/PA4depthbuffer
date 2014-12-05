
public class TriangleFan {
	public Vector3D center;
	private Vector3D normal;
	private int stepTotal;
	private float rx;
	private float ry;
	public Vector3D[] border;
	
	public TriangleFan(Vector3D center, Vector3D normal, int stepTotal, float rx, float ry) {
		this.center = center;
		this.normal = normal;
		this.stepTotal = stepTotal;
		this.rx = rx;
		this.ry = ry;
		this.border = new Vector3D[stepTotal];
		for (int i = 0; i < stepTotal; i++) {
			border[i] = new Vector3D();
		}
		fillFan();
	}

	private void fillFan() {
		float theta = (float)-Math.PI;
		float c_theta = (float)Math.cos(theta);
		float s_theta = (float)Math.sin(theta);
		float thetaStep = (float)(2.0*Math.PI)/(stepTotal-1);
		for(int i=0;i<stepTotal;i++) {
			border[i].x = (float)center.x + rx*c_theta;
			border[i].y = (float)center.y + ry*s_theta;
			border[i].z = (float)center.z;
			
			
			theta += thetaStep;
			c_theta = (float)Math.cos(theta);
			s_theta = (float)Math.sin(theta);
		}
	}

	public Vector3D getNormal() {
		return normal;
	}
	
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
		transformFan(qMatrix, nTrans);

	}
	
	public void transformFan(TransformMatrix vTrans, TransformMatrix nTrans)
	{
		for(int i = 0; i < stepTotal; i++)
		{
			border[i] = vTrans.multiplyPoint(border[i]);
		}
		this.center = vTrans.multiplyPoint(center);
		normal = nTrans.multiplyPoint(normal);
		normal.normalize();
	}

	
}
