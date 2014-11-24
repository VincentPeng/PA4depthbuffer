import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

public class PA4 extends JFrame implements GLEventListener, KeyListener,
		MouseListener, MouseMotionListener {

	private static final int DEFAULT_WINDOW_WIDTH = 1024;
	private static final int DEFAULT_WINDOW_HEIGHT = 640;
	private GLCapabilities capabilities;
	private GLCanvas canvas;
	private FPSAnimator animator;
	BufferedImage buffer;
	private int last_x = 0;
	private int last_y = 0;
	boolean rotate_world;
	private int nstep = 64;
	private Light light;
	// exponent for specular light
	private int expNs = 10;
	private Vector3D viewing_center = new Vector3D(
			(float) (DEFAULT_WINDOW_WIDTH / 2),
			(float) (DEFAULT_WINDOW_HEIGHT / 2), (float) 0.0);
	private static final Vector3D viewingVec = new Vector3D(0, 0, 1);
	private Quaternion viewing_quaternion = new Quaternion(); // world rotation
																// controlled by
																// mouse

	private boolean doSmoothShading = true;

	public PA4() {
		capabilities = new GLCapabilities(null);
		capabilities.setDoubleBuffered(true);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
		canvas.setAutoSwapBufferMode(true); // true by default. Just to be
											// explicit
		getContentPane().add(canvas);
		rotate_world = false;
		animator = new FPSAnimator(canvas, 60);

		setTitle("CS480/CS680 : PA4 Shaded Rendering and Depth-Buffering");
		setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);

		light = new InfiniteLight(0, 1, 1, new ColorType(1, 1, 1));
	}

	public void run() {
		animator.start();
	}

	public static void main(String[] args) {
		PA4 pa4 = new PA4();
		pa4.run();
	}

	@Override
	public void mouseDragged(final MouseEvent mouse) {
		if (this.rotate_world) {
			// get the current position of the mouse
			final int x = mouse.getX();
			final int y = mouse.getY();

			// get the change in position from the previous one
			final int dx = x - this.last_x;
			final int dy = y - this.last_y;

			// create a unit vector in the direction of the vector (dy, dx, 0)
			final float magnitude = (float) Math.sqrt(dx * dx + dy * dy);
			if (magnitude > 0.0001) {
				// define axis perpendicular to (dx,-dy,0)
				// use -y because origin is in upper lefthand corner of the
				// window
				final float[] axis = new float[] { -(float) (dy / magnitude),
						(float) (dx / magnitude), 0 };

				// calculate appropriate quaternion
				final float viewing_delta = 3.1415927f / 180.0f;
				final float s = (float) Math.sin(0.5f * viewing_delta);
				final float c = (float) Math.cos(0.5f * viewing_delta);
				final Quaternion Q = new Quaternion(c, s * axis[0],
						s * axis[1], s * axis[2]);
				this.viewing_quaternion = Q.multiply(this.viewing_quaternion);

				// normalize to counteract acccumulating round-off error
				this.viewing_quaternion.normalize();

				// save x, y as last x, y
				this.last_x = x;
				this.last_y = y;
				drawShade();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	@Override
	public void mousePressed(MouseEvent mouse) {
		int button = mouse.getButton();
		if (button == MouseEvent.BUTTON1) {
			last_x = mouse.getX();
			last_y = mouse.getY();
			rotate_world = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent mouse) {
		int button = mouse.getButton();
		if (button == MouseEvent.BUTTON1) {
			rotate_world = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	@Override
	public void keyTyped(KeyEvent key) {
		switch (key.getKeyChar()) {
		case 'Q':
		case 'q':
			new Thread() {
				public void run() {
					animator.stop();
				}
			}.start();
			System.exit(0);
			break;
		case 'S':
		case 's':
			doSmoothShading = !doSmoothShading;
		case '+':
		case '=':
			expNs++;
			System.out.println("expNs=" + expNs);
			break;
		case '-':
		case '_':
			if (expNs > 0) {
				expNs--;
			}
			System.out.println("expNs=" + expNs);
			break;
		case '>':
			if (nstep < 128)
				nstep *= 2;
			System.out.println("nstep=" + nstep);
			break;
		case '<':
			if (nstep > 8)
				nstep /= 2;
			System.out.println("nstep=" + nstep);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent key) {
		switch (key.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			new Thread() {
				public void run() {
					animator.stop();
				}
			}.start();
			System.exit(0);
			break;

		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		WritableRaster wr = buffer.getRaster();
		DataBufferByte dbb = (DataBufferByte) wr.getDataBuffer();
		byte[] data = dbb.getData();

		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);

		drawShade();
		gl.glDrawPixels(buffer.getWidth(), buffer.getHeight(), GL2.GL_BGR,
				GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(data));
	}

	void clearPixelBuffer() {
		Graphics2D g = buffer.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		g.dispose();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = (GL2) drawable.getGL();

		Dimension dimension = this.getContentPane().getSize();
		buffer = new BufferedImage(dimension.width, dimension.height,
				BufferedImage.TYPE_3BYTE_BGR);

		clearPixelBuffer();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub

	}

	private void drawShade() {

		clearPixelBuffer();
		ColorType torusKa = new ColorType(0.0, 0.0, 0.0);
		ColorType torusKd = new ColorType(0.0, 0.5, 0.9);
		ColorType torusKs = new ColorType(1.0, 1.0, 1.0);

		ColorType sphereKa = new ColorType(0.1, 0.1, 0.1);
		ColorType sphereKd = new ColorType(0.5, 0.0, 0.5);
		ColorType sphereKs = new ColorType(1.0, 1.0, 1.0);

		ColorType ellipKa = new ColorType(0.1, 0.1, 0.1);
		ColorType ellipKd = new ColorType(0.5, 0.0, 0.5);
		ColorType ellipKs = new ColorType(1.0, 1.0, 1.0);

		ColorType cyinderKa = new ColorType(0.1, 0.1, 0.1);
		ColorType cyinderKd = new ColorType(0.5, 0.0, 0.5);
		ColorType cyinderKs = new ColorType(1.0, 1.0, 1.0);

		ColorType boxKa = new ColorType(0.1, 0.1, 0.1);
		ColorType boxKd = new ColorType(0.5, 0.0, 0.5);
		ColorType boxKs = new ColorType(1.0, 1.0, 1.0);

		int radius = 50;
		Material sphereMat = new Material(sphereKa, sphereKd, sphereKs, expNs);
		Material torusMat = new Material(torusKa, torusKd, torusKs, expNs);
		Material ellipMat = new Material(ellipKa, ellipKd, ellipKs, expNs);
		Material cylinderMat = new Material(cyinderKa, cyinderKd, cyinderKs,
				expNs);
		Material boxMat = new Material(boxKa, boxKd, boxKs, expNs);
		drawSphere(new Point3D(128, 128, 128), sphereMat, 1.5f * radius);
		drawTorus(new Point3D(256, 384, 128), torusMat, radius);
		drawEllipsoid(new Point3D(512, 128, 128), ellipMat, radius * 2, radius,
				radius);
		drawCylinder(new Point3D(700, 384, 128), cylinderMat, radius * 1.5f,
				radius, radius * 2.5f);
		drawBox(new Point3D(900,128, 128),boxMat,radius*2);

	}


	// helper method that computes the unit normal to the plane of the triangle
	// degenerate triangles yield normal that is numerically zero
	private Vector3D computeTriangleNormal(Vector3D v0, Vector3D v1, Vector3D v2) {
		Vector3D e0 = v1.minus(v2);
		Vector3D e1 = v0.minus(v2);
		Vector3D norm = e0.crossProduct(e1);

		if (norm.magnitude() > 0.000001)
			norm.normalize();
		else
			// detect degenerate triangle and set its normal to zero
			norm.set((float) 0.0, (float) 0.0, (float) 0.0);

		return norm;
	}

	private void drawSphere(Point3D center, Material mat, float radius) {
		Sphere sphere = new Sphere(center, radius, mat, nstep, nstep);
		sphere.mesh.rotateMesh(viewing_quaternion, viewing_center);
		drawMesh(sphere);
	}

	private void drawTorus(Point3D center, Material mat, float radius) {
		Torus torus = new Torus(center, mat, radius * 0.8f, radius * 1.25f,
				nstep, nstep);
		torus.mesh.rotateMesh(viewing_quaternion, viewing_center);
		drawMesh(torus);
	}

	private void drawEllipsoid(Point3D center, Material mat, float rx,
			float ry, float rz) {
		Ellipsoid ellipsoid = new Ellipsoid(center, mat, rx, ry, rz, nstep,
				nstep);
		ellipsoid.mesh.rotateMesh(viewing_quaternion, viewing_center);
		drawMesh(ellipsoid);
	}

	private void drawCylinder(Point3D center, Material mat, float rx, float ry,
			float umax) {
		Cylinder cylinder = new Cylinder(center, mat, rx, ry, umax, nstep,
				nstep);
		cylinder.mesh.rotateMesh(viewing_quaternion, viewing_center);
		drawMesh(cylinder);
	}
	

	private void drawBox(Point3D center, Material mat, float edge) {
		Box box = new Box(center, mat, edge);
		box.mesh.rotateMesh(viewing_quaternion, viewing_center);
		drawMesh(box);
	}

	private void drawMesh(Object3D ob) {
		Vector3D triangle_normal = new Vector3D();
		Mesh3D mesh = ob.mesh;
		int stepu = mesh.uStepTotal;
		int stepv = mesh.vStepTotal;
		Point2D[] tri = { new Point2D(), new Point2D(), new Point2D() };
		Vector3D v0, v1, v2, n0, n1, n2;
		Material mat = ob.getMaterial();
		for (int i = 0; i < stepv - 1; i++) {
			for (int j = 0; j < stepu - 1; j++) {
				// TODO: add depthBuffer
				v0 = mesh.coordinates[i][j];
				v1 = mesh.coordinates[i][j + 1];
				v2 = mesh.coordinates[i + 1][j + 1];

				triangle_normal = computeTriangleNormal(v0, v1, v2);
				if (viewingVec.dotProduct(triangle_normal) > 0.0) {
					if (doSmoothShading && !(ob instanceof Box) ) {
						n0 = mesh.normal[i][j];
						n1 = mesh.normal[i][j + 1];
						n2 = mesh.normal[i + 1][j + 1];
						tri[0].c = light.applyLight(mat, viewingVec, n0);
						tri[1].c = light.applyLight(mat, viewingVec, n1);
						tri[2].c = light.applyLight(mat, viewingVec, n2);
					} else {
						n0 = n1 = n2 = triangle_normal;
						tri[0].c = tri[1].c = tri[2].c = light.applyLight(mat,
								viewingVec, triangle_normal);
					}
					tri[0].x = (int) v0.x;
					tri[0].y = (int) v0.y;
					tri[1].x = (int) v1.x;
					tri[1].y = (int) v1.y;
					tri[2].x = (int) v2.x;
					tri[2].y = (int) v2.y;

					SketchBase.drawTriangle(buffer, tri[0], tri[1], tri[2],
							doSmoothShading);
				}

				v0 = mesh.coordinates[i][j];
				v1 = mesh.coordinates[i + 1][j + 1];
				v2 = mesh.coordinates[i + 1][j];

				triangle_normal = computeTriangleNormal(v0, v1, v2);
				if (viewingVec.dotProduct(triangle_normal) > 0.0) {
					if (doSmoothShading && !(ob instanceof Box)) {
						n0 = mesh.normal[i][j];
						n1 = mesh.normal[i + 1][j + 1];
						n2 = mesh.normal[i + 1][j];
						tri[0].c = light.applyLight(mat, viewingVec, n0);
						tri[1].c = light.applyLight(mat, viewingVec, n1);
						tri[2].c = light.applyLight(mat, viewingVec, n2);
					} else {
						n0 = n1 = n2 = triangle_normal;
						tri[0].c = tri[1].c = tri[2].c = light.applyLight(mat,
								viewingVec, triangle_normal);
					}

					tri[0].x = (int) v0.x;
					tri[0].y = (int) v0.y;
					tri[1].x = (int) v1.x;
					tri[1].y = (int) v1.y;
					tri[2].x = (int) v2.x;
					tri[2].y = (int) v2.y;

					SketchBase.drawTriangle(buffer, tri[0], tri[1], tri[2],
							doSmoothShading);
				}
			}
		}
	}
}
