import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class FlatSurface implements Surface {

	public TriangleFan fan;
	private Vector3D normal;
	private Material mat;
	private Vector3D viewVec;
	private int stepTotal;

	public Material getMat() {
		return mat;
	}

	public void setMat(Material mat) {
		this.mat = mat;
	}

	public FlatSurface(Vector3D center, Vector3D normal, Vector3D viewVec,
			Material material, int stepTotal, float rx, float ry) {
		this.normal = normal;
		this.mat = material;
		this.stepTotal = stepTotal;
		this.viewVec = viewVec;
		this.fan = new TriangleFan(center, normal, stepTotal, rx, ry);
	}

	@Override
	public void drawFlatShading(ArrayList<LightSource> lightSources,
			Vector3D viewVec) {
		Vector3D trangleNormal = fan.getNormal();
		Point3D p0, p1, p2;
		for (int i = 0; i < this.stepTotal - 1; i++) {

			p0 = new Point3D((int) fan.center.x, (int) fan.center.y,
					(int) fan.center.z);
			p1 = new Point3D((int) fan.border[i].x, (int) fan.border[i].y,
					(int) fan.border[i].z);
			p2 = new Point3D((int) fan.border[i + 1].x,
					(int) fan.border[i + 1].y, (int) fan.border[i + 1].z);

			for (Iterator<LightSource> iterator = lightSources.iterator(); iterator
					.hasNext();) {
				LightSource lightSource = iterator.next();
				// The color should be the same because the normal is the same
				lightSource.applyLight(mat, viewVec, trangleNormal, p0);
				lightSource.applyLight(mat, viewVec, trangleNormal, p1);
				lightSource.applyLight(mat, viewVec, trangleNormal, p2);
			}

			Triangle tri = new Triangle(p0, p1, p2);
			tri.drawFlatShading();

		}
	}

	@Override
	public void drawGouraudShading(ArrayList<LightSource> lightSources,
			Vector3D viewVec) {
		Vector3D trangleNormal = fan.getNormal();
		Point3D p0, p1, p2;
		for (int i = 0; i < this.stepTotal - 1; i++) {

			p0 = new Point3D((int) fan.center.x, (int) fan.center.y,
					(int) fan.center.z);
			p1 = new Point3D((int) fan.border[i].x, (int) fan.border[i].y,
					(int) fan.border[i].z);
			p2 = new Point3D((int) fan.border[i + 1].x,
					(int) fan.border[i + 1].y, (int) fan.border[i + 1].z);

			for (Iterator<LightSource> iterator = lightSources.iterator(); iterator
					.hasNext();) {
				LightSource lightSource = iterator.next();
				// The color should be the same because the normal is the same
				lightSource.applyLight(mat, viewVec, trangleNormal, p0);
				lightSource.applyLight(mat, viewVec, trangleNormal, p1);
				lightSource.applyLight(mat, viewVec, trangleNormal, p2);
			}

			Triangle tri = new Triangle(p0, p1, p2);
			tri.drawGouraudShading();

		}
	}

	@Override
	public void drawPhongShading(ArrayList<LightSource> lightSources,
			Vector3D viewVec) {
		Point3D p0, p1, p2;
		for (int i = 0; i < this.stepTotal - 1; i++) {

			p0 = new Point3D((int) fan.center.x, (int) fan.center.y,
					(int) fan.center.z);
			p1 = new Point3D((int) fan.border[i].x, (int) fan.border[i].y,
					(int) fan.border[i].z);
			p2 = new Point3D((int) fan.border[i + 1].x,
					(int) fan.border[i + 1].y, (int) fan.border[i + 1].z);

			p0.normal = p1.normal = p2.normal = normal;
			Triangle tri = new Triangle(p0, p1, p2);
			tri.drawPhongShading(mat, lightSources, viewVec);

		}
	}

}
