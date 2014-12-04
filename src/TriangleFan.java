
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
		Quaternion q_inv = q.conjugate();
		Vector3D vec;

		Quaternion p;

		for (int i = 0; i < stepTotal; ++i) {
			// apply pivot rotation to vertices, given center point
			p = new Quaternion((float) 0.0, border[i].minus(rotate_center));
			p = q.multiply(p);
			p = p.multiply(q_inv);
			vec = p.get_v();
			border[i] = vec.plus(rotate_center);

		}
		// rotate the normals
		p = new Quaternion((float) 0.0, normal);
		p = q.multiply(p);
		p = p.multiply(q_inv);
		normal = p.get_v();

		// rotate the center
		p = new Quaternion((float) 0.0, center.minus(rotate_center));
		p = q.multiply(p);
		p = p.multiply(q_inv);
		vec = p.get_v();
		center = vec.plus(rotate_center);

	}

	
}
