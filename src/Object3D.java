import java.util.ArrayList;


public abstract class Object3D {
	protected Material material;
	protected Point3D center;
	
	
	public Object3D(Point3D center, Material mat) {
		this.center = center;
		this.material = mat;
	}
	
	public Material getMaterial() {
		return material;
	}
	

	public void draw(ArrayList<LightSource> lightSources, Vector3D viewVec, char renderMethod) {
		if(renderMethod == 'F' || renderMethod == 'f') {
			drawFlat(lightSources,viewVec);
		} else if (renderMethod == 'G' || renderMethod == 'g') {
			drawGouraud(lightSources,viewVec);
		} else {
			drawPhong(lightSources,viewVec);
		}
	}

	
	public abstract void drawFlat(ArrayList<LightSource> lightSources,
			Vector3D viewVec);
	public abstract void drawGouraud(ArrayList<LightSource> lightSources,
			Vector3D viewVec);
	public abstract void drawPhong(ArrayList<LightSource> lightSources,
			Vector3D viewVec);
	
	public abstract void toggleDiff(boolean isDiff);
	public abstract void toggleSpec(boolean isSpec);
}
