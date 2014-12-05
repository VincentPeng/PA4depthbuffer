import java.util.ArrayList;
import java.util.Iterator;

public class Scene {
	
	private ArrayList<Object3D> objList;
	private ArrayList<LightSource> lightList;
	private Sphere obj0;
	private Torus obj1;
	private Ellipsoid obj2;
	private Cylinder obj3;
	private Box obj4;
	private Point3D position0,position1,position2;
	private InfiniteLight light0;
	private SpotLight light1;
	private PointLight light2;
	private AmbientLight light3;
	
	private int selectedObjIndex = 0;
	
	int radius = 50;
	float r = radius * 0.8f;
	float r_axial = radius * 1.5f;
	int nstep = 50;
	int ns = 5;
	Vector3D viewVec = new Vector3D(0.0f, 0.0f, 1.0f);
	ColorType lightColor = new ColorType(1, 1, 1);
	ColorType ambColorType = new ColorType(0, 1, 0);
	private Material mat0, mat1, mat2, mat3, mat4;
	Vector3D infiniteLightVec = new Vector3D(1.0f,1.0f,1.0f);
	Vector3D spotLightVec = new Vector3D(-1.0f,0.0f,1.0f);
	Point3D spotLightPos = new Point3D(PA4.DEFAULT_WINDOW_WIDTH/4, PA4.DEFAULT_WINDOW_HEIGHT/2, 200);
	double spotLightAngle = Math.PI/6;
	Point3D pointLightPos = new Point3D(PA4.DEFAULT_WINDOW_WIDTH*3/4, PA4.DEFAULT_WINDOW_HEIGHT/2, 200);
	
	public Scene() {
		
		position0 = new Point3D(320, 200, -100);
		position1 = new Point3D(640, 200, 100);
		position2 = new Point3D(480, 400, 0);
		

		ColorType Ka0 = new ColorType(0.2, 0.2, 0.2);
		ColorType Kd0 = new ColorType(0.6, 0.3, 0.0);
		ColorType Ks0 = new ColorType(1.0, 1.0, 1.0);
		
		ColorType Ka1 = new ColorType(0.2, 0.2, 0.2);
		ColorType Kd1 = new ColorType(0.0, 0.5, 0.9);
		ColorType Ks1 = new ColorType(1.0, 1.0, 1.0);

		ColorType Ka2 = new ColorType(0.1, 0.4, 0.0);
		ColorType Kd2 = new ColorType(0.5, 0.0, 0.5);
		ColorType Ks2 = new ColorType(1.0, 1.0, 1.0);

		ColorType Ka3 = new ColorType(0.0, 0.2, 0.2);
		ColorType Kd3 = new ColorType(0.4, 0.2, 0.2);
		ColorType Ks3 = new ColorType(1.0, 1.0, 1.0);

		ColorType Ka4 = new ColorType(0.2, 0.0, 0.2);
		ColorType Kd4 = new ColorType(0.1, 0.9, 0.1);
		ColorType Ks4 = new ColorType(1.0, 1.0, 1.0);

		mat0 = new Material(Ka0, Kd0, Ks0, ns);
		mat1 = new Material(Ka1, Kd1, Ks1, ns);
		mat2 = new Material(Ka2, Kd2, Ks2, ns);
		mat3 = new Material(Ka3, Kd3, Ks3, ns);
		mat4 = new Material(Ka4, Kd4, Ks4, ns);
		reset();
	}
	
	public void reset() {
		objList = new ArrayList<Object3D>();
		lightList = new ArrayList<LightSource>();
	}
	
	public void drawScene(char renderMethod) {
		PA4.clearPixelBuffer();
		for (int i = 0; i < objList.size(); i++) {
			Object3D obj = objList.get(i);
			obj.draw(lightList, viewVec, renderMethod);
		}
	}

	public void turnOnScene(int num) {
		if (num > 4)
			return;
		
		switch (num) {
		case 0:
			openScene0();
			break;
		case 1:
			openScene1();
			break;
		case 2:
			openScene2();
			break;
		case 3:
			openScene3();
			break;
		case 4:
			openScene4();
			break;

		default:
			openScene0();
			break;
		}
	}

	// init obj, light.
	public void openScene0() {
		reset();
		obj0 = new Sphere(position0, radius, mat1, nstep, nstep);
		obj1 = new Torus(position1, mat2, r, r_axial, nstep, nstep);
		obj2 = new Ellipsoid(position2, mat3, radius * 2, radius, radius, nstep, nstep);
		objList.add(obj0);
		objList.add(obj1);
		objList.add(obj2);
		
		light0 = new InfiniteLight(infiniteLightVec, lightColor);
		light1 = new SpotLight(spotLightPos, spotLightVec, lightColor, spotLightAngle);
		light2 = new PointLight(lightColor, pointLightPos);
		light3 = new AmbientLight(ambColorType);
		lightList.add(light0);
		lightList.add(light1);
		lightList.add(light2);
		lightList.add(light3);
		
		
	}

	public void openScene1() {
		reset();
		obj1 = new Torus(position0, mat2, r, r_axial, nstep, nstep);
		obj2 = new Ellipsoid(position1, mat3, radius * 2, radius, radius, nstep, nstep);
		obj3 = new Cylinder(position2, mat4, radius * 1.5f, radius, radius, nstep, nstep, viewVec);
		objList.add(obj1);
		objList.add(obj2);
		objList.add(obj3);

		light0 = new InfiniteLight(infiniteLightVec, lightColor);
		light2 = new PointLight(lightColor, pointLightPos);
		lightList.add(light0);
		lightList.add(light2);
	}

	public void openScene2() {
		reset();
		obj2 = new Ellipsoid(position0, mat3, radius * 2, radius, radius, nstep, nstep);
		obj3 = new Cylinder(position1, mat4, radius * 1.5f, radius, radius, nstep, nstep, viewVec);
		obj4 = new Box(position2, radius*2, mat0, nstep, nstep);
		objList.add(obj2);
		objList.add(obj3);
		objList.add(obj4);

		light0 = new InfiniteLight(infiniteLightVec, lightColor);
		light3 = new AmbientLight(ambColorType);
		lightList.add(light0);
		lightList.add(light3);
	}

	public void openScene3() {
		reset();
		obj0 = new Sphere(position0, radius, mat0, nstep, nstep);
		obj3 = new Cylinder(position1, mat2, radius * 1.5f, radius, radius, nstep, nstep, viewVec);
		obj4 = new Box(position2, radius, mat4, nstep, nstep);
		objList.add(obj0);
		objList.add(obj3);
		objList.add(obj4);

		light1 = new SpotLight(spotLightPos, spotLightVec, lightColor, spotLightAngle);
		light2 = new PointLight(lightColor, pointLightPos);
		lightList.add(light1);
		lightList.add(light2);
	}

	public void openScene4() {
		reset();
		obj0 = new Sphere(position0, radius, mat1, nstep, nstep);
		obj1 = new Torus(position1, mat3, r, r_axial, nstep, nstep);
		obj4 = new Box(position2, radius, mat0, nstep, nstep);
		objList.add(obj0);
		objList.add(obj1);
		objList.add(obj2);
		
		light1 = new SpotLight(spotLightPos, spotLightVec, lightColor, spotLightAngle);
		light3 = new AmbientLight(ambColorType);
		lightList.add(light1);
		lightList.add(light3);
	}
	
	public void changeSelectedObj() {
		selectedObjIndex = (selectedObjIndex+1)%objList.size();
	}

	public void translateScene(int x, int y, int z) {

	}

	public void transCamera(int i, int j, int k) {
		for (Iterator<Object3D> it = objList.iterator(); it.hasNext();) {
			Object3D obj = (Object3D) it.next();
			obj.translate(i, j, k);
		}
	}

	public void transObj(int i, int j, int k) {
		Object3D selectedObj = objList.get(selectedObjIndex);
		selectedObj.translate(i,j,k);
	}
	
	public void rotateCamera(Quaternion q, Vector3D viewCenter) {
		for (Iterator<Object3D> it = objList.iterator(); it.hasNext();) {
			Object3D obj = (Object3D) it.next();
			obj.rotate(q, viewCenter);
		}
	}
	
	public void rotateObj(Quaternion q) {
		Object3D selectedObj = objList.get(selectedObjIndex);
		selectedObj.rotate(q, selectedObj.getCenter());
	}

	public void toggleDiff(boolean toggleDiff) {
		for (Iterator<Object3D> it = objList.iterator(); it.hasNext();) {
			Object3D obj = (Object3D) it.next();
			obj.toggleDiff(toggleDiff);
		}
	}

	public void toggleSpec(boolean toggleSpec) {
		for (Iterator<Object3D> it = objList.iterator(); it.hasNext();) {
			Object3D obj = (Object3D) it.next();
			obj.toggleSpec(toggleSpec);
		}
	}

}
