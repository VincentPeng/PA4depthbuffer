import java.util.ArrayList;

public interface Surface {
	public void drawFlatShading(ArrayList<LightSource> lightSources, Vector3D viewVec);

	public void drawGouraudShading(ArrayList<LightSource> lightSources, Vector3D viewVec);

	public void drawPhongShading(ArrayList<LightSource> lightSources, Vector3D viewVec);
}
