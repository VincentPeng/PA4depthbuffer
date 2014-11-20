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

	private static final int DEFAULT_WINDOW_WIDTH = 800;
	private static final int DEFAULT_WINDOW_HEIGHT = 640;
	private GLCapabilities capabilities;
	private GLCanvas canvas;
	private FPSAnimator animator;
	BufferedImage buffer;
	private GLU glu;
	private GLUT glut;
	private int last_x = 0;
	private int last_y = 0;
	boolean rotate_world;
	
	private Light light;
	
	private Vector3D viewing_center = new Vector3D((float)(DEFAULT_WINDOW_WIDTH/2),(float)(DEFAULT_WINDOW_HEIGHT/2),(float)0.0);
	private static final Vector3D viewingVec = new Vector3D(0, 0, 1);

	private Quaternion viewing_quaternion = new Quaternion(); // world rotation controlled by mouse

	
	Sphere sphere;
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

		glu = new GLU();
		glut = new GLUT();

		setTitle("CS480/CS680 : PA4 Shaded Rendering and Depth-Buffering");
		setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
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
	public void mouseDragged(MouseEvent mouse) {
		if (rotate_world) {
			// Vector3D in the direction of mouse motion
			int x = mouse.getX();
			int y = mouse.getY();
			float dx = x - last_x;
			float dy = y - last_y;

			// spin around axis by small delta
			float mag = Math.max((float) Math.sqrt(dx * dx + dy * dy),
					0.000001f);
			float[] axis = new float[3];
			axis[0] = dy / mag;
			axis[1] = dx / mag;
			axis[2] = 0.0f;

			// calculate appropriate quaternion
			float viewing_delta = 3.1415927f / 180.0f; // 1 degree
			float s = (float) Math.sin(0.5f * viewing_delta);
			float c = (float) Math.cos(0.5f * viewing_delta);

			Quaternion Q = new Quaternion(c, s * axis[0], s * axis[1], s
					* axis[2]);
			viewing_quaternion = Q.multiply(viewing_quaternion);

			// normalize to counteract acccumulating round-off error
			viewing_quaternion.normalize();

			// Save x, y as last x, y
			last_x = x;
			last_y = y;
			
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
//		GL2 gl = (GL2)drawable.getGL();
//		clearPixelBuffer();
//		drawShade();
//		
//		WritableRaster rs = buffer.getRaster();
//		DataBufferByte db = (DataBufferByte)rs.getDataBuffer();
//		byte[] data = db.getData();
//		gl.glDrawPixels(buffer.getWidth(), buffer.getHeight(), GL2.GL_BGR, 
//				GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(data));
		GL2 gl = drawable.getGL().getGL2();
	    WritableRaster wr = buffer.getRaster();
	    DataBufferByte dbb = (DataBufferByte) wr.getDataBuffer();
	    byte[] data = dbb.getData();

	    gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
	    clearPixelBuffer();
	    drawShade();
	    gl.glDrawPixels (buffer.getWidth(), buffer.getHeight(),
      GL2.GL_BGR, GL2.GL_UNSIGNED_BYTE,
      ByteBuffer.wrap(data));
	}
	
	void clearPixelBuffer()
	{
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
		
		createSolid();
	}

	private void createSolid() {
		ColorType torusKa = new ColorType(0.0,0.0,0.0);
        ColorType sphereKa = new ColorType(0.0,0.0,0.0);
        ColorType torusKd = new ColorType(0.0,0.5,0.9);
        ColorType sphereKd = new ColorType(0.5,0.0,0.5);
        ColorType torusKs = new ColorType(1.0,1.0,1.0);
        ColorType sphereKs = new ColorType(1.0,1.0,1.0);
        int sphereNs = 5;
        int torusNs = 5;
		Material sphereMaterial = new Material(sphereKa, sphereKd, sphereKs, sphereNs);
		sphere = new Sphere(new Point3D(500, 200, 0), 100, sphereMaterial, 40, 40);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub

	}
	
	private void drawShade() {
		ColorType torusKa = new ColorType(0.0,0.0,0.0);
        ColorType sphereKa = new ColorType(0.0,0.0,0.0);
        ColorType torusKd = new ColorType(0.0,0.5,0.9);
        ColorType sphereKd = new ColorType(0.5,0.0,0.5);
        ColorType torusKs = new ColorType(1.0,1.0,1.0);
        ColorType sphereKs = new ColorType(1.0,1.0,1.0);
        int sphereNs = 1;
        int torusNs = 5;
		Material sphereMaterial = new Material(sphereKa, sphereKd, sphereKs, sphereNs);
		sphere = new Sphere(new Point3D(500, 100, 0), 80, sphereMaterial, 200, 200);
		Mesh3D mesh = sphere.mesh;
		
		int stepu = mesh.uStepTotal;
		int stepv = mesh.vStepTotal;
		Point2D[] tri = {new Point2D(), new Point2D(), new Point2D()};

		sphere.mesh.rotateMesh(viewing_quaternion, viewing_center);
		for(int i=0;i<stepu-1;i++) {
			for(int j=0;j<stepv-1;j++) {
				//TODO: add depthBuffer
				
				tri[0].c = light.applyLight(sphere.getMaterial(), viewingVec, mesh.normal[i][j]);
				tri[1].c = light.applyLight(sphere.getMaterial(), viewingVec, mesh.normal[i+1][j]);
				tri[2].c = light.applyLight(sphere.getMaterial(), viewingVec, mesh.normal[i][j+1]);
				
				tri[0].x = (int)mesh.coordinates[i][j].x;
				tri[0].y = (int)mesh.coordinates[i][j].y;
				tri[1].x = (int)mesh.coordinates[i+1][j].x;
				tri[1].y = (int)mesh.coordinates[i+1][j].y;
				tri[2].x = (int)mesh.coordinates[i][j+1].x;
				tri[2].y = (int)mesh.coordinates[i][j+1].y;
				
				SketchBase.drawTriangle(buffer, tri[0], tri[1], tri[2], true);
				
				tri[0].c = light.applyLight(sphere.getMaterial(), viewingVec, mesh.normal[i+1][j+1]);
				
				tri[0].x = (int)mesh.coordinates[i+1][j+1].x;
				tri[0].y = (int)mesh.coordinates[i+1][j+1].y;
				
				SketchBase.drawTriangle(buffer, tri[2], tri[1], tri[0], true);
			}
		}
				
	}

}
