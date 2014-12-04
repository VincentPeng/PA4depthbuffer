

public class TransformMatrix {
	public float[] matrix;
	
	public TransformMatrix()
	{
		matrix = new float[16];
		
		//first row
		matrix[0] = 1.0f;
		matrix[1] = 0f;
		matrix[2] = 0f;
		matrix[3] = 0f;
		
		//second row
		matrix[4] = 0f;
		matrix[5] = 1.0f;
		matrix[6] = 0f;
		matrix[7] = 0f;
		
		//third row
		matrix[8] = 0f;
		matrix[9] = 0f;
		matrix[10] = 1.0f;
		matrix[11] = 0f;

		//fourth row
		matrix[12] = 0f;
		matrix[13] = 0f;
		matrix[14] = 0f;
		matrix[15] = 1.0f;

	}
	
	public static float[] scale(float sx , float sy , float sz)
	{
		float[] scaleMat = new float[16];
		
		scaleMat[0] = sx;
		scaleMat[1] = 0;
		scaleMat[2] = 0;
		scaleMat[3] = 0;
		
		
		scaleMat[4] = 0;
		scaleMat[5] = sy;
		scaleMat[6] = 0;
		scaleMat[7] = 0;
		
		scaleMat[8] = 0;
		scaleMat[9] = 0;
		scaleMat[10] = sz;
		scaleMat[11] = 0;
		
		scaleMat[12] = 0;
		scaleMat[13] = 0;
		scaleMat[14] = 0;
		scaleMat[15] = 1;
		
		return scaleMat;
	}
	
	public static float[] translate(float x , float y , float z)
	{
		float [] translateMat = new float[16];
		
		translateMat[0] = 1;
		translateMat[1] = 0;
		translateMat[2] = 0;
		translateMat[3] = x;
		
		
		translateMat[4] = 0;
		translateMat[5] = 1;
		translateMat[6] = 0;
		translateMat[7] = y;
		
		translateMat[8] = 0;
		translateMat[9] = 0;
		translateMat[10] = 1;
		translateMat[11] = z;
		
		translateMat[12] = 0;
		translateMat[13] = 0;
		translateMat[14] = 0;
		translateMat[15] = 1;
		
		return translateMat;
	}
	
	public void setMatrix(float[] that)
	{
		if(that.length == 16)
		{
			this.matrix = that;
		}
		else
		{
			System.out.println("not a right matrix");
		}
	}
	
	public float [] getMatrix()
	{
		return matrix;
	}
	
	public float [] multiplyMatrix(float [] that)
	{
		float [] result = new float[16];
		for(int i = 0; i < 16 ; i++)
		{
			int row = i / 4;
			int m = row * 4;
			int n = i % 4;

			result[i] = matrix[ m] * that[n] + matrix[m + 1] * that[n + 4] + matrix[m + 2] * that[n + 8] + matrix[m + 3] * that[n + 12]; 
		}
		
		matrix = result;
		
		return result;
	}
	
	public Vector3D multiplyPoint(Vector3D point)
	{
		Vector3D pResult = new Vector3D(0 , 0 , 0);
		
		float [] homo = new float[4];
		
		homo[0] = point.x;
		homo[1] = point.y;
		homo[2] = point.z;
		homo[3] = 1.0f;
		
		for(int i = 0; i < 4; i++)
		{
			homo[i] = homo[0] * matrix[i * 4] + homo[1] * matrix[i * 4 + 1] + homo[2] * matrix[i * 4 + 2] + homo[3] * matrix[i * 4 + 3];
		}
		
		pResult.x = homo[0] / homo[3];
		pResult.y = homo[1] / homo[3];
		pResult.z = homo[2] / homo[3];
		
		return pResult;
	}
	
	public float [] getTranspose()
	{
		float [] transpose = new float[16];
		
		for(int i = 0; i < 16 ; i++)
		{
			int j = i % 4 * 4 + i / 4;
			
			transpose[i] = matrix[j];
		}
		
		
		return transpose;
		
	}
	
	public void printMatrix()
	{
		for(int i = 0; i < 16; i++)
		{
			System.out.printf("%.2f ," , matrix[i]);
			if(i % 4 == 3)
			{
				System.out.println();
			}
		}
	}
	
}
