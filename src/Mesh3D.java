
public class Mesh3D {
	
	public int uStepTotal,vStepTotal;
	public Vector3D[][] coordinates;
	public Vector3D[][] normal;
	public Mesh3D(int u, int v) {
		this.uStepTotal =u;
		this.vStepTotal=v;
		coordinates = new Vector3D[u][v];
		normal = new Vector3D[u][v];
	}
	
	public void rotateMesh(Quaternion q, Vector3D center)
	{
		Quaternion q_inv = q.conjugate();
		Vector3D vec;
		
		Quaternion p;
		
		for(int i=0;i<uStepTotal;++i)
			for(int j=0;j<vStepTotal;++j)
			{
				// apply pivot rotation to vertices, given center point
				p = new Quaternion((float)0.0,coordinates[i][j].minus(center)); 
				p=q.multiply(p);
				p=p.multiply(q_inv);
				vec = p.get_v();
				coordinates[i][j]=vec.plus(center);
				
				// rotate the normals
				p = new Quaternion((float)0.0,normal[i][j]);
				p=q.multiply(p);
				p=p.multiply(q_inv);
				normal[i][j] = p.get_v();
			}
		
		
	}
}
