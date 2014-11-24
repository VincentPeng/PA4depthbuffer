
public class Box extends Object3D {

	private float halfEdge;
	public Box(Point3D center, Material mat, float edge) {
		
		super(center, mat);
		halfEdge = 0.5f * edge;
		mesh = new Mesh3D(5, 4);
		fillMesh();
	}
	
	public void fillMesh() {
		
		
		mesh.coordinates[0][0] = new Vector3D((float)center.x-halfEdge, (float)center.y+halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[0][1] = new Vector3D((float)center.x-halfEdge, (float)center.y+halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[0][2] = new Vector3D((float)center.x+halfEdge,(float)center.y+ halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[0][3] = new Vector3D((float)center.x+halfEdge, (float)center.y+halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[1][0] = new Vector3D((float)center.x-halfEdge,(float)center.y +halfEdge, (float)center.z-halfEdge);
		mesh.coordinates[1][1] = new Vector3D((float)center.x-halfEdge, (float)center.y+halfEdge, (float)center.z-halfEdge);
		mesh.coordinates[1][2] = new Vector3D((float)center.x+halfEdge, (float)center.y+halfEdge, (float)center.z-halfEdge);
		mesh.coordinates[1][3] = new Vector3D((float)center.x+halfEdge, (float)center.y+halfEdge, (float)center.z-halfEdge);
		
		mesh.coordinates[2][0] = new Vector3D((float)center.x-halfEdge,(float)center.y +halfEdge, (float)center.z-halfEdge);
		mesh.coordinates[2][1] = new Vector3D((float)center.x-halfEdge, (float)center.y-halfEdge, (float)center.z-halfEdge);
		mesh.coordinates[2][2] = new Vector3D((float)center.x+halfEdge, (float)center.y-halfEdge, (float)center.z-halfEdge);
		mesh.coordinates[2][3] = new Vector3D((float)center.x+halfEdge, (float)center.y+halfEdge, (float)center.z-halfEdge);
		
		mesh.coordinates[3][0] = new Vector3D((float)center.x-halfEdge,(float)center.y +halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[3][1] = new Vector3D((float)center.x-halfEdge, (float)center.y-halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[3][2] = new Vector3D((float)center.x+halfEdge, (float)center.y-halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[3][3] = new Vector3D((float)center.x+halfEdge, (float)center.y+halfEdge, (float)center.z+halfEdge);
		
		mesh.coordinates[4][0] = new Vector3D((float)center.x-halfEdge,(float)center.y +halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[4][1] = new Vector3D((float)center.x-halfEdge,(float)center.y +halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[4][2] = new Vector3D((float)center.x+halfEdge, (float)center.y+halfEdge, (float)center.z+halfEdge);
		mesh.coordinates[4][3] = new Vector3D((float)center.x+halfEdge, (float)center.y+halfEdge, (float)center.z+halfEdge);
		
	}
	

}
