
public class Object3D {
	public Mesh3D mesh;
	protected Material material;
	protected Point3D center;
	
	
	public Object3D(Point3D center, Material mat) {
		this.center = center;
		this.material = mat;
	}
	
	public Material getMaterial() {
		return material;
	} 
}
