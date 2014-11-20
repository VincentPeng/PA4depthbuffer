import java.time.chrono.MinguoChronology;

public class InfiniteLight implements Light {

	final Vector3D dirVec;
	final ColorType lightColor;

	public InfiniteLight(float x, float y, float z, ColorType colorType) {
		dirVec = new Vector3D(x, y, z);
		dirVec.normalize();

		lightColor = colorType;
	}

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
		// If the point is facing opposite to the viewer, it will not be considered
		if(dotVN>0.0) {
			if(material.isAmbient()) {
				retColorType.r += material.getKa().r*lightColor.r;
				retColorType.g += material.getKa().g*lightColor.g;
				retColorType.b += material.getKa().b*lightColor.b;
			}
			if(material.isDiffuse()) {
				
				if(dotLN > 0.0) {
					retColorType.r += material.getKd().r*lightColor.r*dotLN;
					retColorType.g += material.getKd().g*lightColor.g*dotLN;
					retColorType.b += material.getKd().b*lightColor.b*dotLN;
				}
			}
			if(material.isSpecular()) {
				Vector3D reflect = dirVec.reflect(normal);
				double dotRV = reflect.dotProduct(viewVec);
				if(dotLN >0 && dotRV>0.0) {
					retColorType.r += material.getKs().r*lightColor.r*Math.pow(dotLN, material.getNs());
					retColorType.g += material.getKs().g*lightColor.g*Math.pow(dotLN, material.getNs());
					retColorType.b += material.getKs().b*lightColor.b* Math.pow(dotLN, material.getNs());
				}
			}
				
				
			retColorType.r = Math.min(retColorType.r, 1);
			retColorType.g = Math.min(retColorType.g, 1);
			retColorType.b = Math.min(retColorType.b, 1);
		}
		
		return retColorType;
	}
}
