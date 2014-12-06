
public class PointLight implements LightSource {

	private ColorType lightColor;
	private Point3D position;
	
	private static final float A0 = 1;
	private static final float A1 = 0.0005f;
	private static final float A2 = 0.00001f;
	
	public PointLight(ColorType lightColor, Point3D position) {
		this.lightColor = lightColor;
		this.position = position;
	}


	@Override
	public void applyLight(Material mat, Vector3D viewVec, Vector3D normal,
			Point3D p) {
		Vector3D direction = new Vector3D(position.x - p.x, position.y - p.y, position.z - p.z);
		float distance = (float)direction.magnitude();
		float radialAtten = 1/(A0+A1*distance + A2*distance*distance);
		direction.normalize();
		ColorType res = new ColorType(0, 0, 0);
		double dot = direction.dotProduct(normal);
		if (dot>0) {
			if(mat.isDiffuse()) {
				res.r += (float)(dot*mat.getKd().r*lightColor.r) * radialAtten;
				res.g += (float)(dot*mat.getKd().g*lightColor.g) * radialAtten;
				res.b += (float)(dot*mat.getKd().b*lightColor.b) * radialAtten;
				
			}
			
			if (mat.isSpecular()) {
				Vector3D r = direction.reflect(normal);
				dot = r.dotProduct(viewVec);
				if(dot>0.0) {
					res.r += (float) Math.pow(dot*mat.getKs().r*lightColor.r, mat.getNs()) * radialAtten;
					res.g += (float) Math.pow(dot*mat.getKs().r*lightColor.g, mat.getNs()) * radialAtten;
					res.b += (float) Math.pow(dot*mat.getKs().b*lightColor.b, mat.getNs()) * radialAtten;
				
				}
			}
		}
		
		p.c.r += res.r;
		p.c.g += res.g;
		p.c.b += res.b;
		
		p.c.r = Math.min(p.c.r, 1);
		p.c.g = Math.min(p.c.g, 1);
		p.c.b = Math.min(p.c.b, 1);
	}

	@Override
	public String getName() {
		return "point light";
	}


}
