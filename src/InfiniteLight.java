
public class InfiniteLight implements LightSource {

	final Vector3D dirVec;
	final ColorType lightColor;

	public InfiniteLight(float x, float y, float z, ColorType colorType) {
		dirVec = new Vector3D(x, y, z);
		dirVec.normalize();

		lightColor = colorType;
	}
	
	public InfiniteLight(Vector3D vec, ColorType colorType) {
		dirVec = vec;
		dirVec.normalize();

		lightColor = colorType;
	}
//	static int a=0;
	public Vector3D getDirection() {
		return dirVec;
	}

	/**
	 * 
	 * @param material
	 *            The material light is on
	 * @param viewVec
	 *            The Vector3D point to the viewer
	 * @param normal
	 *            the normal Vector3D at the point being lighting on
	 * @return
	 */
	public void applyLight(Material material, Vector3D viewVec, Vector3D normal, Point3D p) {
		ColorType res = new ColorType(0, 0, 0);
		// The angle between the viewing Vector3D and the normal of the point
		double dotVN = viewVec.dotProduct(normal);
		double dotLN = dirVec.dotProduct(normal);
		if(dotLN>0.0) {
			
			if(material.isDiffuse()) {
					res.r += material.getKd().r*lightColor.r*dotLN;
					res.g += material.getKd().g*lightColor.g*dotLN;
					res.b += material.getKd().b*lightColor.b*dotLN;
			}
			if(material.isSpecular()) {
				Vector3D reflect = dirVec.reflect(normal);
				double dotRV = reflect.dotProduct(viewVec);
				if(dotLN >0 && dotRV>0.0) {
					res.r += (float)Math.pow(material.getKs().r*lightColor.r*dotRV, material.getNs());
					res.g += (float)Math.pow(material.getKs().g*lightColor.g*dotRV, material.getNs());
					res.b += (float)Math.pow(material.getKs().b*lightColor.b*dotRV, material.getNs());
				}
			}
				
				
			res.r = Math.min(res.r, 1);
			res.g = Math.min(res.g, 1);
			res.b = Math.min(res.b, 1);
		}
		
		p.c.r += res.r;
		p.c.g += res.g;
		p.c.b += res.b;
		
		p.c.r = Math.min(p.c.r, 1);
		p.c.g = Math.min(p.c.g, 1);
		p.c.b = Math.min(p.c.b, 1);
	}
}
