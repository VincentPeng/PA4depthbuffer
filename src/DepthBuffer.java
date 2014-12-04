
public class DepthBuffer {
	private int width, height;
	public double[][] depthBuff;
	
	public DepthBuffer(int w, int h) {
		this.width = w;
		this.height = h;
		depthBuff = new double[w][h];
		clearBuffer();
	}
	
	public void clearBuffer() {
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				depthBuff[i][j] = -999999;
			}
		}
	}
	
	public double getDepth(int x, int y) {
		if(x<0 || x>width-1 || y<0 || y>height-1) {
			System.err.println("getDepth: coordinate is out of boundary!");
			return 9999999;
		}
		return depthBuff[x][y];
	}
	
	public void setDepth(int x, int y, double dep) {
		if(x<0 || x>width-1 || y<0 || y>height-1) {
			System.err.println("setDepth: coordinate is out of boundary!");
		}
		depthBuff[x][y] = dep;
	}
}
