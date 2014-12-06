public class AmbientLight implements LightSource {

	private ColorType color;

	public AmbientLight(ColorType color) {
		this.color = color;
	}

	@Override
	public void applyLight(Material mat, Vector3D viewVec, Vector3D normal,
			Point3D p) {
		ColorType res = new ColorType(0, 0, 0);
		
		if (mat.isAmbient()) {
			res.r = mat.getKa().r*color.r;
			res.g = mat.getKa().g*color.g;
			res.b = mat.getKa().b*color.b;
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
		return "ambient light";
	}
	

}
