import java.awt.Color;
import java.awt.image.RescaleOp;


public class SpotLight implements LightSource {
	
	Point3D position;
	Vector3D dirVector;
	ColorType lightColor;
	private double cosAngle;
	
	private static final float A0 = 1;
	private static final float A1 = 0.00005f;
	private static final float A2 = 0.0000001f;
	private static final float n_angular = 300;

	
	public SpotLight(Point3D position, Vector3D dirVector,
			ColorType lightColor, double angle) {
		this.position = position;
		this.dirVector = dirVector;
		this.dirVector.normalize();
		this.lightColor = lightColor;
		cosAngle = Math.cos(angle);
	}


	@Override
	public void applyLight(Material mat, Vector3D viewVec, Vector3D normal,
			Point3D p) {
		
		Vector3D incidentDir = new Vector3D(position.x - p.x, position.y - p.y,
				position.z - p.z);
		float distance = (float)incidentDir.magnitude();
		float radialAtten = 1/(A0+A1*distance+A2*distance*distance);
		incidentDir.normalize();
		
		double dotAtten = dirVector.dotProduct(incidentDir);
		float angularAtten = (float) Math.pow(dotAtten, n_angular);
		ColorType res = new ColorType(0,0,0);
		double dot = incidentDir.dotProduct(normal);
		if (dot > 0.0 && dotAtten > cosAngle) {
			if (mat.isDiffuse()) {
				res.r += (float) (dot*mat.getKd().r*lightColor.r) * radialAtten * angularAtten;
				res.g += (float) (dot*mat.getKd().g*lightColor.g) * radialAtten * angularAtten;
				res.r += (float) (dot*mat.getKd().b*lightColor.b) * radialAtten * angularAtten;
			}
			
			if(mat.isSpecular()) {
				Vector3D r = incidentDir.reflect(normal);
				dot = r.dotProduct(viewVec);
				if(dot > 0.0) {
					res.r += (float) Math.pow(dot*mat.getKs().r*lightColor.r, mat.getNs()) * radialAtten *angularAtten;
					res.g += (float) Math.pow(dot*mat.getKs().r*lightColor.g, mat.getNs()) * radialAtten *angularAtten;
					res.b += (float) Math.pow(dot*mat.getKs().b*lightColor.b, mat.getNs()) * radialAtten *angularAtten;
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

}
