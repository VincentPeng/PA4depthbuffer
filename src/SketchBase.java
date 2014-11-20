//****************************************************************************
// SketchBase.  
//****************************************************************************
// Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)

import java.awt.image.BufferedImage;


public class SketchBase 
{
	public static final float PI = 3.1415926f;
	
	public SketchBase()
	{
		// deliberately left blank
	}
	
	// draw a point
	public static void drawPoint(BufferedImage buff, Point2D p)
	{
		buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getBRGUint8());
	}
	
	//////////////////////////////////////////////////
	//	Implement the following two functions
	//////////////////////////////////////////////////
	
	// draw a line segment

	/**
	 * <b>Description:</b></br>
	 *  Draw a line from p1 to p2, if the color of p1 and p2 are not the same,
	 *  the color will change in smooth color interpolation.
	 * </br></br>
	 * 
	 * <b>Output:</b></br>
	 *  A line from p1 to p2
	 * </br></br>
	 * 
	 * <b>Input:</b></br>
	 *   @param buff The destination image buffer to draw on.
	 *   @param p1 Start point
	 *   @param p2 Ending point
	 */
	public static void drawLine(BufferedImage buff, Point2D p1, Point2D p2) {

		// get the x and y coordinate of vertex
		int x0 = p1.x;
		int y0 = p1.y;
		int x1 = p2.x;
		int y1 = p2.y;

		// delta x and y
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		
		// define each step, considering position of p1 and p2, step may be minus.
		int sx = (x0 < x1) ? 1 : -1;
		int sy = (y0 < y1) ? 1 : -1;
		int sr = (p1.c.r < p2.c.r) ? 1 : -1;
		int sg = (p1.c.g < p2.c.g) ? 1 : -1;
		int sb = (p1.c.b < p2.c.b) ? 1 : -1;
		
		int clDiffR = (int) ((p2.c.r - p1.c.r) * 255);
		int clDiffG = (int) ((p2.c.g - p1.c.g) * 255);
		int clDiffB = (int) ((p2.c.b - p1.c.b) * 255);

		if (dx >= dy) {
			if (dx == 0)
				return;
			// integer part for color increment
			int clrIncR = clDiffR / dx;
			int clrIncG = clDiffG / dx;
			int clrIncB = clDiffB / dx;

			// decimal part for color
			int dr = Math.abs(clDiffR - clrIncR * dx);
			int dg = Math.abs(clDiffG - clrIncG * dx);
			int db = Math.abs(clDiffB - clrIncB * dx);
			
			int D = 2 * dy - dx;
			int Dr = 2 * dr - dx;
			int Dg = 2 * dg - dx;
			int Db = 2 * db - dx;

			int deltaColor = p1.c.getBRGUint8();

			for (int x = x0, y = y0; x != x1 || y != y1; x += sx) {

				// add the integer part of color into delta color
				deltaColor += ((clrIncR << 16) + (clrIncG << 8) + clrIncB);
				if (D < 0) {
					// choose lower
					D += 2 * dy;
				} else {
					y += sy;
					// deltaColor += ((1<<16) | (1<<8) | 1);
					D += 2 * dy - 2 * dx;
				}
				if (Dr < 0) {
					Dr += 2 * dr;
				} else {
					deltaColor += sr << 16;
					Dr += 2 * dr - 2 * dx;
				}
				if (Dg < 0) {
					Dg += 2 * dg;
				} else {
					deltaColor += sg << 8;
					Dg += 2 * dg - 2 * dx;
				}

				if (Db < 0) {
					Db += 2 * db;
				} else {
					deltaColor += sb;
					Db += 2 * db - 2 * dx;
				}
				buff.setRGB(x, buff.getHeight() - y - 1, deltaColor);
			}
		} else {
			if (dy == 0)
				return;
			// integer part for color
			int clrIncR = clDiffR / dy;
			int clrIncG = clDiffG / dy;
			int clrIncB = clDiffB / dy;
			// decimal part for color
			int dr = Math.abs(clDiffR - clrIncR * dy);
			int dg = Math.abs(clDiffG - clrIncG * dy);
			int db = Math.abs(clDiffB - clrIncB * dy);

			int D = 2 * dx - dy;
			int Dr = 2 * dr - dy;
			int Dg = 2 * dg - dy;
			int Db = 2 * db - dy;
			int deltaColor = p1.c.getBRGUint8();

			for (int x = x0, y = y0; x != x1 || y != y1; y += sy) {
				// add the integer part of color into delta color
				deltaColor += ((clrIncR << 16) + (clrIncG << 8) + clrIncB);
				if (D < 0) {
					// choose lower
					D += 2 * dx;
				} else {
					x += sx;
					D += 2 * dx - 2 * dy;
				}

				if (Dr < 0) {
					Dr += 2 * dr;
				} else {
					deltaColor += sr << 16;
					Dr += 2 * dr - 2 * dy;
				}
				if (Dg < 0) {
					Dg += 2 * dg;
				} else {
					deltaColor += sg << 8;
					Dg += 2 * dg - 2 * dy;
				}

				if (Db < 0) {
					Db += 2 * db;
				} else {
					deltaColor += sb;
					Db += 2 * db - 2 * dy;
				}

				buff.setRGB(x, buff.getHeight() - y - 1, deltaColor);
			}
		}
	}
	
	// draw a triangle


	/**
	 * <b>Description:</b></br>
	 *  Using three vertex coordinate to draw a triangle. If the color of vertex is not the same,
	 *  and smooth bilinear interpolation flag is on, then it will do the smooth bilinear interpolation.
	 *
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 *  A triangle
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param buff  The destination image buffer to draw on.
	 *   @param p1   vertex 1.
	 *   @param p2   vertex 2.
	 *   @param p3   vertex 3.
	 *   @param do_smooth   The flag of doing the smooth interpolation.
	 */
	public static void drawTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3, boolean do_smooth)
	{
		// Create edge array to store the information of three line segment of the triangle.
		Edge edges[] = new Edge[3];
		if (do_smooth) {
			edges[0] = new Edge(p1.x, p1.y, p1.c, p2.x, p2.y, p2.c);
			edges[1] = new Edge(p2.x, p2.y, p2.c, p3.x, p3.y, p3.c);
			edges[2] = new Edge(p3.x, p3.y, p3.c, p1.x, p1.y, p1.c);
		} else {
			edges[0] = new Edge(p1.x, p1.y, p1.c, p2.x, p2.y, p1.c);
			edges[1] = new Edge(p2.x, p2.y, p1.c, p3.x, p3.y, p1.c);
			edges[2] = new Edge(p3.x, p3.y, p1.c, p1.x, p1.y, p1.c);
		}
		
		// There must be one long edge, whose delta y-coordinate will be the sum of other two edges.
		int longEdge=0, shortEdge1, shortEdge2;
		// Variable to store the max delta y-coordinate.
		int maxDy = 0;
		for(int i=0;i<3;i++)
		{
			int dy = edges[i].Y2-edges[i].Y1;
			if(dy>maxDy) {
				longEdge = i;
				maxDy = dy;
			}
		}
		shortEdge1 = (longEdge+1) % 3;
		shortEdge2 = (longEdge+2) % 3;
		
		drawBetweenEdge(buff, edges[longEdge], edges[shortEdge1]);
		drawBetweenEdge(buff, edges[longEdge], edges[shortEdge2]);
	}
	
	/**
	 * <b>Description:</b></br>
	 *  Draw horizontal lines between two edges to fill out the pixel between two edges.
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 *  The pixels between two edges will be filled out.
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param buff   The destination image buffer to draw on.  
	 *   @param edge   Edge 1. Should be the longest edge.
	 *   @param edge2  Edge 2. Short edge.
	 */
	private static void drawBetweenEdge(BufferedImage buff, Edge edge1, Edge edge2) {
		
		
		//
		float e1xdiff = (float)(edge1.X2 - edge1.X1);
		float e2xdiff= (float)(edge2.X2 - edge2.X1);
		
		float e1ydiff = (float)(edge1.Y2 - edge1.Y1);
		if(e1ydiff == 0.0) return;
		float e2ydiff = (float)(edge2.Y2 - edge2.Y1);
		if(e2ydiff == 0.0) return;
			
		float factor1 = (float)(edge2.Y1-edge1.Y1)/e1ydiff;
		float factorStep1 = 1.0f/e1ydiff;
		float factor2 = 0.0f;
		float factorStep2 = 1.0f/e2ydiff;
		ColorType colorDiff1 = edge1.C2.colorDiff(edge1.C1);
		ColorType colorDiff2 = edge2.C2.colorDiff(edge2.C1);
		
		for(int y=edge2.Y1;y<edge2.Y2;y++) {
			Span span = new Span(edge1.C1.colorAdd(colorDiff1.colorTimes(factor1)),
					edge1.X1 + Math.round(factor1 * e1xdiff), 
					edge2.C1.colorAdd(colorDiff2.colorTimes(factor2)),
					edge2.X1 + Math.round(factor2 * e2xdiff));
			drawSpan(buff, span, y);
			
			factor1 += factorStep1;
			factor2 += factorStep2;
		}
	}

	/////////////////////////////////////////////////
	// for texture mapping (Extra Credit for CS680)
	/////////////////////////////////////////////////
	/**
	 * <b>Description:</b></br>
	 *  Draw the texture on the destination buffer image.
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 *  The texture be drew on the destination Image buffer.
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param buff  The destination image buffer to draw on. 
	 *   @param texture  The source texture to be drew
	 *   @param p1 vertex of triangle
	 *   @param p2 vertex of triangle
	 *   @param p3 vertex of triangle
	 */
	public static void triangleTextureMap(BufferedImage buff, BufferedImage texture, Point2D p1, Point2D p2, Point2D p3)
	{
		// definition of the coordinate of the vertex on the texture.
		p1.u = 0.5f;
		p1.v = 1.0f;
		p2.u = 1.0f;
		p2.v = 0.0f;
		p3.u = 0.0f;
		p3.v = 0.0f;
		
		Edge[] edges = {new Edge(p1.x, p1.y, p1.u, p1.v, p2.x, p2.y, p2.u, p2.v),
				new Edge(p2.x, p2.y, p2.u, p2.v, p3.x, p3.y, p3.u, p3.v),
				new Edge(p1.x, p1.y, p1.u, p1.v, p3.x, p3.y, p3.u, p3.v)};
		
		// There must be one long edge, whose delta y-coordinate will be the sum
		// of other two edges.
		int longEdge = 0, shortEdge1, shortEdge2;
		// Variable to store the max delta y-coordinate.
		int maxDy = 0;
		for (int i = 0; i < 3; i++) {
			int dy = edges[i].Y2 - edges[i].Y1;
			if (dy > maxDy) {
				longEdge = i;
				maxDy = dy;
			}
		}
		shortEdge1 = (longEdge + 1) % 3;
		shortEdge2 = (longEdge + 2) % 3;
		
		drawTexBetweenEdge(buff, texture, edges[longEdge], edges[shortEdge1]);
		drawTexBetweenEdge(buff, texture, edges[longEdge], edges[shortEdge2]);
		
	}
	
	/**
	 * <b>Description:</b></br>
	 *  Draw horizontal line between two edges. edge1 should be the longer line that has the bigger
	 *  delta y-coordinate.
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 *  fill the pixel out between two edges.
	 *
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param buff 
	 *   @param texture
	 *   @param edge1
	 *   @param edge2
	 */
	private static void drawTexBetweenEdge(BufferedImage buff, BufferedImage texture, Edge edge1, Edge edge2) {
		
		
		float e1xdiff = (float)(edge1.X2 - edge1.X1);
		float e2xdiff= (float)(edge2.X2 - edge2.X1);
		float e1udiff = edge1.U2 - edge1.U1;
		float e2udiff = edge2.U2 - edge2.U1;
		
		float e1ydiff = (float)(edge1.Y2 - edge1.Y1);
		if(e1ydiff == 0.0) return;
		float e2ydiff = (float)(edge2.Y2 - edge2.Y1);
		if(e2ydiff == 0.0) return;
		float e1vdiff = edge1.V2 - edge1.V1;
		float e2vdiff = edge2.V2 - edge2.V1;
		
		// The start ratio of the position of edge1 y coordinate
		float factor1 = (float)(edge2.Y1-edge1.Y1)/e1ydiff;
		// Growing step of edge1
		float factorStep1 = 1.0f/e1ydiff;
		// The start ratio of the position of edge2 y coordinate
		float factor2 = 0.0f;
		// Growing step of edge2
		float factorStep2 = 1.0f/e2ydiff;
		
		for(int y=edge2.Y1;y<edge2.Y2;y++) {
			Span span = new Span(edge1.X1 + Math.round(factor1 * e1xdiff),
					edge1.U1 + factor1 * e1udiff,
					edge1.V1 + factor1 * e1vdiff,
					edge2.X1 + Math.round(factor2 * e2xdiff),
					edge2.U1 + factor2 * e2udiff,
					edge2.V1 + factor2 * e2vdiff);		
			drawTexSpan(buff, texture, span, y);
			
			factor1 += factorStep1;
			factor2 += factorStep2;
		}
	}
	
	/**
	 * <b>Description:</b></br>
	 *  Separate the origin triangle into many span, and convert each point in the span's 
	 *  coordinate into the texture coordinate system. Then draw them.
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 *  Get the corresponding color from the texture and draw them as a horizontal line on the 
	 *  Image buffer.
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param buff  The destination image buffer to draw on.  
	 *   @param texture   The texture need to be drew.
	 *   @param span  The horizontal line to draw on.
	 *   @param y   y-coordinate.
	 */
	private static void drawTexSpan(BufferedImage buff, BufferedImage texture,
			Span span, int y) {
		// The x-coordinate difference between two vertex
				int xdiff = span.X2-span.X1;
				if(xdiff == 0) return;
				
				float factor = 0.0f;
				float factorStep = 1.0f/(float)xdiff;
				
				float udiff = span.U2 - span.U1;
				float vdiff = span.V2 - span.V1;
				
				for(int x=span.X1; x<=span.X2;x++) {
					float u = span.U1 + factor * udiff;
					float v = span.V1 + factor * vdiff;
					int color = textureMapLookup(u, v, texture);
					buff.setRGB(x, y, color);
					factor += factorStep;
				}
	}

	/**
	 * <b>Description:</b></br>
	 *  Using the u,v coordinate to get the RGB from the texture. Bilinear interpolation will 
	 *  be used to mix the neighbor color to get the return color.
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 * The color from the mixture of neighbor point
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param u  u-coordinate
	 *   @param v  v-coordinate
	 *   @param texture  The texture to get color from
	 *   @return The mixed color from the neighbor of (u,v)
	 */
	private static int textureMapLookup(float u, float v, BufferedImage texture) {
		int color = 0;
		// The width and height of texture
		int height = texture.getHeight();
		int width = texture.getWidth();
		
		// The left bottom neighbor of point(u,v)
		int intU = (int) (u*(width-1));
		int intV = (int) (v*(height-1));
		// Get the coordinate of other neighbors.
		int intUaddOne = intU < width-1 ? intU+1 : intU;
		int intVaddOne = intV < height-1 ? intV+1 : intV;
		
		// Neighbor point array
		TexPoint [] neigPoint = {new TexPoint(intU, intV, texture.getRGB(intU, intV)),
				new TexPoint(intUaddOne, intV, texture.getRGB(intUaddOne, intV)),
				new TexPoint(intU, intVaddOne, texture.getRGB(intU, intVaddOne)),
				new TexPoint(intUaddOne, intVaddOne, texture.getRGB(intUaddOne, intVaddOne)),
		};
		
		int r0 = neigPoint[0].color>>16 & 0xFF;
		int g0 = neigPoint[0].color>>8 & 0xFF;
		int b0  = neigPoint[0].color & 0xFF;
		
		int r1 = neigPoint[1].color>>16 & 0xFF;
		int g1 = neigPoint[1].color>>8 & 0xFF;
		int b1  = neigPoint[1].color & 0xFF;
		
		int r2 = neigPoint[2].color>>16 & 0xFF;
		int g2 = neigPoint[2].color>>8 & 0xFF;
		int b2  = neigPoint[2].color & 0xFF;
		
		int r3 = neigPoint[3].color>>16 & 0xFF;
		int g3 = neigPoint[3].color>>8 & 0xFF;
		int b3  = neigPoint[3].color & 0xFF;
		
		int colorLeftR = (int)(r0*(1-v) + v*r2);
		int colorLeftG = (int)(g0*(1-v) + v*g2);
		int colorLeftB = (int)(b0*(1-v) + v*b2);
		
		int colorRightR = (int)(r1*(1-v) + v*r3);
		int colorRightG = (int)(g1*(1-v) + v*g3);
		int colorRightB = (int)(b1*(1-v) + v*b3);
		
		int colorR = (int)(colorLeftR*(1-u) + colorRightR*u);
		int colorG = (int)(colorLeftG*(1-u) + colorRightG*u);
		int colorB = (int)(colorLeftB*(1-u) + colorRightB*u);
		
		color = (colorR << 16) | (colorG << 8) | colorB;
		return color;
	}
	

	/**
	 * <b>Description:</b></br>
	 * Draw a span at the specific y-coordinate. 
	 *
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 * Draw a span at the Image buffer.
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param The destination image buffer to draw on.  
	 *   @param span   A horizontal line between two edges
	 *   @param y   The y-coordinate to draw the span.
	 */
	public static void drawSpan(BufferedImage buff, Span span, int y) {
		
		// The x-coordinate difference between two vertex
		int xdiff = span.X2-span.X1;
		if(xdiff == 0) return;
		
		float factor = 0.0f;
		float factorStep = 1.0f/(float)xdiff;
		ColorType colorDiff = span.C2.colorDiff(span.C1);
		
		for(int x=span.X1; x<=span.X2;x++) {
			drawPoint(buff, new Point2D(x, y, span.C1.colorAdd(colorDiff.colorTimes(factor))));
			factor += factorStep;
		}
	}
	
	
	/**
	 * <b>Description:</b></br>
	 *
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 *
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param buff
	 *   @param p1
	 *   @param p2
	 */
	public static void drawAntialiaedLine(BufferedImage buff, Point2D p1, Point2D p2) {
		
		int x1 = p1.x;
		int x2 = p2.x;
		int y1 = p1.y;
		int y2 = p2.y;
		ColorType c1 = p1.c;
		ColorType c2 = p2.c;
		int dx = x2 - x1;
		int dy = y2-y1;
		if (Math.abs(dx) < Math.abs(dy) ){
			int tmp = x1;
			x1 = y1;
			y1 = tmp;
			
			tmp = x2;
			x2 = y2;
			y2 = tmp;
			
			tmp = dx;
			dx= dy;
			dy = tmp;
			
			
		}
		
		if(x2<x1) {
			int tmp = x1;
			x1 = x2;
			x2 = tmp;
			
			tmp = y1;
			y1 = y2;
			y2 = tmp;
			
			ColorType tmpColorType = c1;
			c1 = c2;
			c2 = tmpColorType;
		}
		float gradient = (float)dy/(float)dx;
			
		int xend = Math.round(x1);
		int yend = (int)(y1 + gradient * (xend - x1));
		float xgap = rfpart(x1 + 0.5f);
		int xpxl1 = xend;
//		int 
		float intery = yend + gradient;
		
//		plot(buff, new Point2D(xpxl1, ypxl1,c1), rfpart(yend) * xgap);
//	    plot(xpxl1, ypxl1 + 1, fpart(yend) * xgap)
		
		xend = Math.round(x2);
		yend = (int)(y2 + gradient*(xend - x2));
		int xpxl2 = xend;
		
		
		for (int x = xpxl1+1; x < xpxl2; x++) {
			ColorType c =c1.colorAdd(c2.colorDiff(c1).colorTimes((float)(x-xpxl1)/(float)(xpxl2-xpxl1)));
			if(Math.abs(p1.x-p2.x) < Math.abs(p1.y-p2.y)){
				plot(buff, new Point2D( (int)intery, x, c), rfpart(intery));
				plot(buff, new Point2D( (int)intery + 1, x, c), fpart(intery));
			} else {
				plot(buff, new Point2D(x, (int)intery, c), rfpart(intery));
				plot(buff, new Point2D(x, (int)intery + 1, c), fpart(intery));
			}
			intery += gradient;
		}
	}
	
	public static void plot(BufferedImage buff, Point2D p,float brightness) {
		ColorType c = new ColorType(p.c.r*brightness,p.c.r*brightness,p.c.r*brightness);
		
		drawPoint(buff, new Point2D(p.x, p.y, c));
	}
	
	public static float fpart(float x) {
		return x-(int)x;
	}
	
	public static float rfpart(float x) {
		return 1-fpart(x);
	}
	
	
	/**
	 * Edge definition class. It represents the edge of the triangle.
	 *
	 */
	static class Edge
	{
		// The color of the vertex 
		public ColorType C1, C2;
		// The coordinate 
		public int X1, Y1, X2, Y2;
		// The Texture coordinate
		public float U1, V1, U2, V2;
		public Edge(int x1,int y1, ColorType c1, int x2, int y2, ColorType c2)
		{
			if (y1 >= y2) {
				X1 = x2;
				Y1 = y2;
				X2 = x1;
				Y2 = y1;
				C1 = c2;
				C2 = c1;
			} else {
				X1 = x1;
				Y1 = y1;
				X2 = x2;
				Y2 = y2;
				C1 = c1;
				C2 = c2;
			}
		}
		
		public Edge(int x1, int y1, float u1, float v1, int x2, int y2, float u2, float v2) {
			if(y1>=y2) {
				X1 = x2;
				Y1 = y2;
				X2 = x1;
				Y2 = y1; 
				U1 = u2;
				V1 = v2;
				U2 = u1;
				V2 = v1;
			} else {
				X1 = x1;
				Y1 = y1;
				X2 = x2;
				Y2 = y2;
				U1 = u1;
				V1 = v1;
				U2 = u2;
				V2 = v2;
			}
		}
	}
	
	/**
	 * 
	 * The definition of Span class, representing a horizontal line.
	 *
	 */
	static class Span
	{
		// The color of the span's color of two vertex
		public ColorType C1, C2;
		// The start and end x-coordinate of the span
		public int X1, X2;
		// The texture coordinate
		public float U1, V1, U2, V2;
		/**
		 * 
		 * @param c1  start point color 
		 * @param x1  start point x-coordinate
		 * @param c2  end point color
		 * @param x2  end point color
		 */
		public Span(ColorType c1, int x1, ColorType c2, int x2) 
		{
			if(x1<=x2)
			{
				X1 = x1;
				X2 = x2;
				C1 = c1;
				C2 = c2;
			}
			else 
			{
				X1 = x2;
				X2 = x1;
				C1 = c2;
				C2 = c1;
			}
		}
		
		public Span(int x1, float u1, float v1, int x2, float u2, float v2) {
			if(x1<=x2)
			{
				X1 = x1;
				X2 = x2;
				U1 = u1;
				V1 = v1;
				U2 = u2;
				V2 = v2;
			}
			else 
			{
				X1 = x2;
				X2 = x1;
				U1 = u2;
				V1 = v2;
				U2 = u1;
				V2 = v1;
			}
		}
	}
	
	/**
	 * 
	 * Point definition for Texture coordinate
	 *
	 */
	static class TexPoint {
		// u and v coordinate
		// RGB value for the point
		public int u,v,color;
		public TexPoint(int u, int v, int color) {
			this.u = u;
			this.v = v;
			this.color = color;
		}
	}
}
