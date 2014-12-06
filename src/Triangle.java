import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Triangle {
	private Point3D p0,p1,p2;
	private Edge[] edges;
	private int tallestEdgeNo;

	public Triangle(Point3D p0, Point3D p1, Point3D p2) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		
		edges = new Edge[3];
		
		edges[0] = new Edge(p0, p1);
		edges[1] = new Edge(p1, p2);
		edges[2] = new Edge(p2, p0);
		
		tallestEdgeNo = tallestEdge();
	}
	
	private int tallestEdge() {
		float maxY = 0;
		int edgeIndex = 0;
		for(int i=0;i<3;i++) {
			if(maxY < edges[i].getDeltaY()) {
				edgeIndex = i;
				maxY = edges[i].getDeltaY();
			}
		}
		return edgeIndex;
		
	}

	public void drawFlatShading() {
		drawGouraudShading();
	}
	
	public void drawGouraudShading() {
		drawPoint( p0);
		drawPoint( p1);
		drawPoint( p2);
		
		for (int i = 1; i < edges.length; i++) {
			Edge e1 = edges[tallestEdgeNo];
			Edge e2 = edges[(tallestEdgeNo +i) % 3];
			int startY = e2.getStartY();
			int endY = e2.getEndY();
			
			if(startY == endY) {
				continue;
			}
			
			for(int j = startY ; j<= endY;j++) {
				int x1 = e1.getXfromY(j);
				int x2 = e2.getXfromY(j);
				
				int z1 = e1.getZfromY(j);
				int z2 = e2.getZfromY(j);
				
				Point3D drawPoint1 = new Point3D(x1, j, z1);
				Point3D drawPoint2 = new Point3D(x2, j, z2);
				
				drawPoint1.c = e1.getColorFromY(j);
				drawPoint2.c = e2.getColorFromY(j);
				
				drawSpan( drawPoint1,drawPoint2);
			}
		}
	}
	
	
	
	private void drawSpan(Point3D p0,
			Point3D p1) {
		Edge spanEdge = new Edge(p0, p1);
		int startX = p0.x;
		int endX = p1.x;
		
		float startZ = p0.z;
		float endZ = p1.z;
		
		if(startX == endX) {
			drawPoint( spanEdge.p0);
			drawPoint( spanEdge.p1);
			return;
		}
		
		if(startX > endX) {
			// swap
			int temp = startX;
			startX = endX;
			endX = temp;
			
			float tempf = startZ;
			startZ = endZ;
			endZ = tempf;
		}
		
		for(int i = startX; i <= endX; i++) {
			ColorType drawColor = spanEdge.getColorFromX(i);
			int z = spanEdge.getZfromX(i);
			Point3D drawPoint3d = new Point3D(i, p0.y, z);
			drawPoint3d.c = drawColor;
			drawPoint( drawPoint3d);
		}
	}
	
	public void drawPhongShading(Material mat , ArrayList<LightSource> lightSources, Vector3D viewVector)
	{
		for(int i = 0 ; i < lightSources.size(); i++)
		{
			LightSource light = lightSources.get(i);
			
			light.applyLight(mat, viewVector, p0.normal , p0);
			light.applyLight(mat, viewVector, p1.normal , p1);
			light.applyLight(mat, viewVector, p2.normal , p2);
		}
		
		
		
		drawPoint(p0);
		drawPoint(p1);
		drawPoint(p2);
		
		for(int i = 1; i < 3 ; i++)
		{
			Edge e1 = edges[tallestEdgeNo];
			Edge e2 = edges[(tallestEdgeNo + i) % 3];
			
			int startY = e2.getStartY();
			int endY = e2.getEndY();

			if(startY == endY)
			{
				continue;
			}
			
			for(int j = startY; j <= endY; j++)
			{
				int x1 = (int)e1.getXfromY(j);
				int x2 = (int)e2.getXfromY(j);
				
				int z1 = e1.getZfromY(j);
				int z2 = e2.getZfromY(j);
				

				Point3D drawPoint1 = new Point3D(x1 , j , z1);
				Point3D drawPoint2 = new Point3D(x2 , j , z2);
				
				Vector3D normal1 = e1.getNormalFromY(j);
				Vector3D normal2 = e2.getNormalFromY(j);
				
				drawPoint1.normal = normal1;
				drawPoint2.normal = normal2;
				
				drawPhongSpan(drawPoint1, drawPoint2 , mat ,  lightSources , viewVector);
				
			}
			
		}

	}
	
	private void drawPhongSpan(Point3D p0 , Point3D p1, Material mat , ArrayList<LightSource> lightSources ,Vector3D viewVector)
	{
		Edge spanEdge = new Edge(p0 , p1);
		
		int startX = (int)p0.x; 
		int endX = (int)p1.x;
		
		float startZ = p0.z;
		float endZ = p1.z;
		
		if(startX == endX)
		{
			for(int i = 0 ; i < lightSources.size(); i++)
			{
				LightSource light = lightSources.get(i);	
				light.applyLight(mat, viewVector, spanEdge.p0.normal , spanEdge.p0);
				light.applyLight(mat, viewVector, spanEdge.p1.normal , spanEdge.p1);

			}
			
			drawPoint( spanEdge.p0);
			drawPoint( spanEdge.p1);	
			return;
		}

		if(startX > endX)
		{
			//swap 
			int temp = startX;
			startX = endX;
			endX = temp;
			
			float tempf = startZ;
			startZ = endZ;
			endZ = tempf;
		}
		
		
		for(int i = startX ; i <= endX ; i++)
		{
			Vector3D drawNormal = spanEdge.getNormalFromX(i);
			
			int z = spanEdge.getZfromX(i);
			
			Point3D drawpoint = new Point3D(i , p0.y , z);
			
			drawpoint.normal = drawNormal;
			
			for(int j = 0 ; j < lightSources.size(); j++)
			{
				LightSource light = lightSources.get(j);
				
				light.applyLight(mat, viewVector, drawpoint.normal , drawpoint);
				
			}
			
			drawPoint(drawpoint);
		}
		
	}

	public void drawPoint(Point3D point) {
		BufferedImage fb = PA4.buffer;
		int x = point.x;
		int y = point.y;
		if (x>fb.getWidth()-1 || x<0) {
			return;
		} 
		
		if (y>fb.getHeight()-1 || y<0) {
			return;
		}
		
		if (point.z > PA4.depthBuffer.getDepth(x, y)) {
			PA4.depthBuffer.setDepth(x, y, point.z);
			fb.setRGB(point.x, fb.getHeight()-point.y-1, point.c.getBRGUint8());
		}
	}
	
}
