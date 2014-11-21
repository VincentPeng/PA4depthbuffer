
public class InfiniteLight implements Light {

	final Vector3D dirVec;
	final ColorType lightColor;

	public InfiniteLight(float x, float y, float z, ColorType colorType) {
		dirVec = new Vector3D(x, y, z);
		dirVec.normalize();

		lightColor = colorType;
	}
	static int a=0;
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
	public ColorType applyLight(Material material, Vector3D viewVec, Vector3D normal) {
		ColorType retColorType = new ColorType(0, 0, 0);
		// The angle between the viewing Vector3D and the normal of the point
		double dotVN = viewVec.dotProduct(normal);
		double dotLN = dirVec.dotProduct(normal);
//		if(++a < 300)
//		System.out.println(normal.x+" "+normal.y+" "+normal.z+"");
		// If the point is facing opposite to the viewer, it will not be considered
		if(dotLN>0.0) {
			if(material.isAmbient()) {
				retColorType.r += material.getKa().r*lightColor.r;
				retColorType.g += material.getKa().g*lightColor.g;
				retColorType.b += material.getKa().b*lightColor.b;
			}
			if(material.isDiffuse()) {
					retColorType.r += material.getKd().r*lightColor.r*dotLN;
					retColorType.g += material.getKd().g*lightColor.g*dotLN;
					retColorType.b += material.getKd().b*lightColor.b*dotLN;
			}
			if(material.isSpecular()) {
				Vector3D reflect = dirVec.reflect(normal);
				double dotRV = reflect.dotProduct(viewVec);
				if(dotLN >0 && dotRV>0.0) {
					retColorType.r += (float)Math.pow(material.getKs().r*lightColor.r*dotLN, material.getNs());
					if(++a < 64*64) {
					//System.out.println(dotLN + " "+material.getKs().r+ " "+ lightColor.r+" "+ material.getNs()+" ");
					//System.out.println(Math.pow(material.getKs().g*lightColor.g*dotLN, material.getNs()));
					System.out.println(dotLN+" "+material.getNs()+" "+Math.pow(material.getKs().g*lightColor.g*dotLN, material.getNs()));
					}
					retColorType.g += (float)Math.pow(material.getKs().g*lightColor.g*dotLN, material.getNs());
					retColorType.b += (float)Math.pow(material.getKs().b*lightColor.b*dotLN, material.getNs());
				}
			}
				
				
			retColorType.r = Math.min(retColorType.r, 1);
			retColorType.g = Math.min(retColorType.g, 1);
			retColorType.b = Math.min(retColorType.b, 1);
		}
		
		return retColorType;
	}
}
