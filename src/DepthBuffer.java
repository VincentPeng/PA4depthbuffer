
public class DepthBuffer {
	public ColorType[][] colorBuff;
	public double[][] depthBuff;
	
	public DepthBuffer(int width, int height) {
		colorBuff = new ColorType[width][height];
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				colorBuff[i][j] = new ColorType(0, 0, 0);
				depthBuff[i][j] = 999999;
			}
		}
	}
}
