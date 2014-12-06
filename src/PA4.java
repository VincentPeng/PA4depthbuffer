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
import java.util.ArrayList;

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

	public static final int DEFAULT_WINDOW_WIDTH = 1024;
	public static final int DEFAULT_WINDOW_HEIGHT = 640;
	private GLCapabilities capabilities;
	private GLCanvas canvas;
	private FPSAnimator animator;
	public static BufferedImage buffer;
	private int last_x = 0;
	private int last_y = 0;
	boolean rotate_world;
	private boolean toggleSpec = true;
	private boolean toggleDiff = true;
	
	
	// exponent for specular light
	private int expNs = 5;
	private Vector3D viewing_center = new Vector3D(
			(float) (DEFAULT_WINDOW_WIDTH / 2),
			(float) (DEFAULT_WINDOW_HEIGHT / 2), (float) 0.0);
	private static final Vector3D viewingVec = new Vector3D(0, 0, 1);
	private Quaternion viewing_quaternion = new Quaternion(); // world rotation
																// controlled by
																// mouse
	public static DepthBuffer depthBuffer;

	
	
	private char renderMethod = 'F';
	private boolean toggleCameraOrOb = true;
	private int sceneNum = 0;
	private Scene scene;

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

		depthBuffer = new DepthBuffer(DEFAULT_WINDOW_WIDTH,
				DEFAULT_WINDOW_HEIGHT);

		ColorType lightColor = new ColorType(1, 1, 1);

		scene = new Scene();
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
				Q.normalize();
				if (toggleCameraOrOb) {
					scene.rotateCamera(Q, viewing_center);
				} else {
					scene.rotateObj(Q);
				}
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
		case 'c':
		case 'C':
			toggleCameraOrOb = !toggleCameraOrOb;
			System.out.println("choose camera to translate? "
					+ toggleCameraOrOb);
			break;
		// Choose which objecht to be rotated
		case '[':
		case '{':
			scene.changeSelectedObj();
			break;
		// change the 5 scenes which to be drawn
		case 't':
		case 'T':
			sceneNum = (sceneNum+1)%8;
			scene.turnOnScene(sceneNum);
			System.out.println("turn on scene num:" + sceneNum);
			break;
		case 'Q':
		case 'q':
			new Thread() {
				public void run() {
					animator.stop();
				}
			}.start();
			System.exit(0);
			break;
		// Toggle on Specular/Diffuse/Ambient
		case 'S':
		case 's':
			toggleSpec = !toggleSpec;
			scene.toggleSpec(toggleSpec);
			System.out.println("Specular=" + toggleSpec);
			break;
		case 'D':
		case 'd':
			toggleDiff = !toggleDiff;
			scene.toggleDiff(toggleDiff);
			System.out.println("Diffuse=" + toggleDiff);
			break;
		case 'A':
		case 'a':
			scene.toggleLight(Scene.AMB_LIGHT);
			break;
		// Change the render method to Flat/Gouraud/Phong
		case 'F':
		case 'f':
			renderMethod = key.getKeyChar();
			System.out.println("Render method : Flat");
			break;
		case 'G':
		case 'g':
			renderMethod = key.getKeyChar();
			System.out.println("Render method : Gouraud");
			break;
		case 'P':
		case 'p':
			renderMethod = key.getKeyChar();
			System.out.println("Render method : Phong");
			break;
		case '0':
			scene.toggleLight(Scene.INFI_LIGHT);
			break;
		case '1':
			scene.toggleLight(Scene.SPOT_LIGHT);
			break;
		case '2':
			scene.toggleLight(Scene.POINT_LIGHT);
			break;
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
			scene.scaleObj(1.1f);
			break;
		case '<':
			scene.scaleObj(0.9f);
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
		case KeyEvent.VK_UP:
			if (toggleCameraOrOb) {
				scene.transCamera(0, 5, 0);
			} else {
				scene.transObj(0, -5, 0);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (toggleCameraOrOb) {
				scene.transCamera(0, -5, 0);
			} else {
				scene.transObj(0, 5, 0);
			}
			break;
		case KeyEvent.VK_LEFT:
			if (toggleCameraOrOb) {
				scene.transCamera(5, 0, 0);
			} else {
				scene.transObj(-5, 0, 0);
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (toggleCameraOrOb) {
				scene.transCamera(-5, 0, 0);
			} else {
				scene.transObj(5, 0, 0);
			}
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
		scene.drawScene(renderMethod);
		gl.glDrawPixels(buffer.getWidth(), buffer.getHeight(), GL2.GL_BGR,
				GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(data));
	}

	// private void drawShade() {
	// clearPixelBuffer();
	// cylinder.draw(lightSources, viewingVec, renderMethod);
	// sphere.draw(lightSources, viewingVec, renderMethod);
	// ellipsoid.draw(lightSources, viewingVec, renderMethod);
	// torus.draw(lightSources, viewingVec, renderMethod);
	// box.draw(lightSources, viewingVec, renderMethod);
	// }

	public static void clearPixelBuffer() {
		Graphics2D g = buffer.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		g.dispose();
		depthBuffer.clearBuffer();
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
		scene = new Scene();
		scene.openScene0();
		clearPixelBuffer();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub

	}

}
