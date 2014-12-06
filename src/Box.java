import java.util.ArrayList;


public class Box extends Object3D {

	
	Point3D topCenter;
	Point3D downCenter;
	Point3D leftCenter;
	Point3D rightCenter;
	Point3D frontCenter;
	Point3D backCenter;
	
	public MeshSurface topSurface;
	public MeshSurface downSurface;
	public MeshSurface leftSurface;
	public MeshSurface rightSurface;
	public MeshSurface frontSurface;
	public MeshSurface backSurface;
	
	Vector3D topNormal;
	Vector3D downNormal;
	Vector3D leftNormal;
	Vector3D rightNormal;
	Vector3D frontNormal;
	Vector3D backNormal;
	
	
	private float edge;
	
	public Box(Point3D center , float edge , Material mat , int vStepTotal , int uStepTotal )
	{
		super(center, mat);
		this.edge = edge;
		
		topCenter = new Point3D (center.x , center.y + (int)(edge / 2) , center.z);
		downCenter = new Point3D (center.x , center.y - (int)(edge / 2) , center.z);
		leftCenter = new Point3D (center.x - (int)(edge / 2) , center.y , center.z );
		rightCenter = new Point3D (center.x + (int)(edge / 2) , center.y , center.z);
		frontCenter = new Point3D (center.x , center.y , center.z + (int)(edge / 2));
		backCenter = new Point3D (center.x , center.y , center.z - (int)(edge / 2));
		
		topSurface = new MeshSurface(topCenter , mat , vStepTotal , uStepTotal);
		downSurface = new MeshSurface(downCenter , mat , vStepTotal , uStepTotal);
		leftSurface = new MeshSurface(leftCenter , mat , vStepTotal , uStepTotal);
		rightSurface = new MeshSurface(rightCenter , mat , vStepTotal , uStepTotal);
		frontSurface = new MeshSurface(frontCenter , mat , vStepTotal , uStepTotal);
		backSurface = new MeshSurface(backCenter , mat , vStepTotal , uStepTotal);
	
		topNormal = new Vector3D(0 , 1 , 0);
		downNormal = new Vector3D(0 , -1 , 0);
		leftNormal = new Vector3D(-1 , 0 , 0);
		rightNormal = new Vector3D(1 , 0 , 0);
		frontNormal = new Vector3D(0 , 0 , 1);
		backNormal = new Vector3D(0 , 0 , -1);
		
		fillMesh();
		
	}

	@Override
	public void drawFlat(ArrayList<LightSource> lightSources, Vector3D viewVec) {
		topSurface.drawFlatShading(lightSources, viewVec);
		downSurface.drawFlatShading(lightSources, viewVec);
		leftSurface.drawFlatShading(lightSources, viewVec);
		rightSurface.drawFlatShading(lightSources, viewVec);
		frontSurface.drawFlatShading(lightSources, viewVec);
		backSurface.drawFlatShading(lightSources, viewVec);
	}

	@Override
	public void drawGouraud(ArrayList<LightSource> lightSources,
			Vector3D viewVec) {
		topSurface.drawGouraudShading(lightSources, viewVec);
		downSurface.drawGouraudShading(lightSources, viewVec);
		leftSurface.drawGouraudShading(lightSources, viewVec);
		rightSurface.drawGouraudShading(lightSources, viewVec);
		frontSurface.drawGouraudShading(lightSources, viewVec);
		backSurface.drawGouraudShading(lightSources, viewVec);
	}

	@Override
	public void drawPhong(ArrayList<LightSource> lightSources, Vector3D viewVec) {
		topSurface.drawPhongShading(lightSources, viewVec);
		downSurface.drawPhongShading(lightSources, viewVec);
		leftSurface.drawPhongShading(lightSources, viewVec);
		rightSurface.drawPhongShading(lightSources, viewVec);
		frontSurface.drawPhongShading(lightSources, viewVec);
		backSurface.drawPhongShading(lightSources, viewVec);
	}

	@Override
	public void toggleDiff(boolean isDiff) {
		topSurface.getMat().setDiffuse(isDiff);
		downSurface.getMat().setDiffuse(isDiff);
		leftSurface.getMat().setDiffuse(isDiff);
		rightSurface.getMat().setDiffuse(isDiff);
		frontSurface.getMat().setDiffuse(isDiff);
		backSurface.getMat().setDiffuse(isDiff);
	}

	@Override
	public void toggleSpec(boolean isSpec) {
		topSurface.getMat().setSpecular(isSpec);
		downSurface.getMat().setSpecular(isSpec);
		leftSurface.getMat().setSpecular(isSpec);
		rightSurface.getMat().setSpecular(isSpec);
		frontSurface.getMat().setSpecular(isSpec);
		backSurface.getMat().setSpecular(isSpec);
	}
	
	private void fillMesh()
	{
		fillFront();
		fillRear();
		fillTop();
		fillDown();
		fillLeft();
		fillRight();
	}
	
	private void fillFront()
	{
		int i , j;
		
		float h , w;
		float dh = edge / (float)( frontSurface.mesh.vStepTotal - 1);
		float dw = edge / (float)( frontSurface.mesh.uStepTotal - 1);
		
		for(i = 0 , h =   (edge / 2); i < frontSurface.mesh.vStepTotal ; i++ , h -= dh)
		{
			for(j = 0 , w = -( edge / 2); j < frontSurface.mesh.uStepTotal ; j++ , w += dw)
			{
				frontSurface.mesh.coordinates[i][j].x = (float)((float)(frontCenter.x) + w );
				frontSurface.mesh.coordinates[i][j].y = (float)((float)(frontCenter.y) + h );
				frontSurface.mesh.coordinates[i][j].z = (float)frontCenter.z;
				
				frontSurface.mesh.normal[i][j] = frontNormal;
			}
		}
	}
	
	private void fillRear()
	{
		int i , j;
		
		float h , w;
		float dh = edge / (float)( backSurface.mesh.vStepTotal - 1);
		float dw = edge / (float)( backSurface.mesh.uStepTotal - 1);
		
		for(i = 0 , h =  - (edge / 2); i < backSurface.mesh.vStepTotal ; i++ , h += dh)
		{
			for(j = 0 , w = -( edge / 2); j < backSurface.mesh.uStepTotal ; j++ , w += dw)
			{
				backSurface.mesh.coordinates[i][j].x = (float)((float)(backCenter.x) + w );
				backSurface.mesh.coordinates[i][j].y = (float)((float)(backCenter.y) + h );
				backSurface.mesh.coordinates[i][j].z = (float)backCenter.z;
				
				backSurface.mesh.normal[i][j] = backNormal;
			}
		}
	}
	
	private void fillTop()
	{
		int i , j;
		
		float h , w;
		float dh = edge / (float)( topSurface.mesh.vStepTotal - 1);
		float dw = edge / (float)( topSurface.mesh.uStepTotal - 1);
		
		for(i = 0 , h =   (edge / 2); i < topSurface.mesh.vStepTotal ; i++ , h -= dh)
		{
			for(j = 0 , w = -( edge / 2); j < topSurface.mesh.uStepTotal ; j++ , w += dw)
			{
				topSurface.mesh.coordinates[i][j].x = (float)((float)(topCenter.x) + h );
				topSurface.mesh.coordinates[i][j].y = (float)(topCenter.y);
				topSurface.mesh.coordinates[i][j].z = (float)((float)topCenter.z + w);
				
				topSurface.mesh.normal[i][j] = topNormal;
			}
		}
	}
	
	private void fillDown()
	{
		int i , j;
		
		float h , w;
		float dh = edge / (float)( downSurface.mesh.vStepTotal - 1);
		float dw = edge / (float)( downSurface.mesh.uStepTotal - 1);
		
		for(i = 0 , h =  - (edge / 2); i < downSurface.mesh.vStepTotal ; i++ , h += dh)
		{
			for(j = 0 , w = -( edge / 2); j < downSurface.mesh.uStepTotal ; j++ , w += dw)
			{
				downSurface.mesh.coordinates[i][j].x = (float)((float)(downCenter.x) + h );
				downSurface.mesh.coordinates[i][j].y = (float)(downCenter.y);
				downSurface.mesh.coordinates[i][j].z = (float)((float)downCenter.z + w);
				
				downSurface.mesh.normal[i][j] = downNormal;
			}
		}
	}
	
	private void fillLeft()
	{
		int i , j;
		
		float h , w;
		float dh = edge / (float)( leftSurface.mesh.vStepTotal - 1);
		float dw = edge / (float)( leftSurface.mesh.uStepTotal - 1);
		
		for(i = 0 , h =  - (edge / 2); i < leftSurface.mesh.vStepTotal ; i++ , h += dh)
		{
			for(j = 0 , w = -( edge / 2); j < leftSurface.mesh.uStepTotal ; j++ , w += dw)
			{
				leftSurface.mesh.coordinates[i][j].x = (float)(leftCenter.x);
				leftSurface.mesh.coordinates[i][j].y = (float)((float)leftCenter.y + w);
				leftSurface.mesh.coordinates[i][j].z = (float)((float)leftCenter.z + h);
				
				leftSurface.mesh.normal[i][j] = leftNormal;
			}
		}
	}
	
	private void fillRight()
	{
		int i , j;
		
		float h , w;
		float dh = edge / (float)( rightSurface.mesh.vStepTotal - 1);
		float dw = edge / (float)( rightSurface.mesh.uStepTotal - 1);
		
		for(i = 0 , h =   (edge / 2); i < rightSurface.mesh.vStepTotal ; i++ , h -= dh)
		{
			for(j = 0 , w = -( edge / 2); j < rightSurface.mesh.uStepTotal ; j++ , w += dw)
			{
				rightSurface.mesh.coordinates[i][j].x = (float)(rightCenter.x);
				rightSurface.mesh.coordinates[i][j].y = (float)((float)rightCenter.y + w);
				rightSurface.mesh.coordinates[i][j].z = (float)((float)rightCenter.z + h);
				
				rightSurface.mesh.normal[i][j] = rightNormal;
			}
		}
	}

	public void rotate(Quaternion q, Vector3D rotate_center) {
		TransformMatrix qMatrix = new TransformMatrix();
		TransformMatrix transposeQua = new TransformMatrix();
		
		float [] transIn = TransformMatrix.translate(- rotate_center.x,  - rotate_center.y,  - rotate_center.z);
		float [] transOut = TransformMatrix.translate(rotate_center.x, rotate_center.y, rotate_center.z);
		
		transposeQua.setMatrix(q.toMatrix());
		
		qMatrix.setMatrix(transOut);
		
		qMatrix.multiplyMatrix(transposeQua.getTranspose());
	
		qMatrix.multiplyMatrix(transIn);
		
		TransformMatrix nTrans = new TransformMatrix();
		nTrans.setMatrix(transposeQua.getTranspose());
		
		topSurface.mesh.transformMesh(qMatrix , nTrans);
		downSurface.mesh.transformMesh(qMatrix , nTrans);
		leftSurface.mesh.transformMesh(qMatrix , nTrans);
		rightSurface.mesh.transformMesh(qMatrix , nTrans);
		frontSurface.mesh.transformMesh(qMatrix , nTrans);
		backSurface.mesh.transformMesh(qMatrix , nTrans);
		
		Vector3D tmp = new Vector3D(center.x , center.y , center.z);
		tmp = qMatrix.multiplyPoint(tmp);
		
		center.x = Math.round(tmp.x);
		center.y = Math.round(tmp.y);
		center.z = Math.round(tmp.z);
	}

	@Override
	public void translate(float x , float y , float z)
	{
		
		center.x += x;
		center.y += y;
		center.z += z;
		
		TransformMatrix translate = new TransformMatrix();
		translate.setMatrix(TransformMatrix.translate(x, y, z));
		TransformMatrix nTrans = new TransformMatrix();
		
		topSurface.mesh.transformMesh(translate, nTrans);
		downSurface.mesh.transformMesh(translate, nTrans);
		leftSurface.mesh.transformMesh(translate, nTrans);
		rightSurface.mesh.transformMesh(translate, nTrans);
		frontSurface.mesh.transformMesh(translate, nTrans);
		backSurface.mesh.transformMesh(translate, nTrans);
		
		
	}

	@Override
	public void scale(float scaleFactor) {
		edge *= scaleFactor;
		TransformMatrix transIn = new TransformMatrix();
		transIn.setMatrix(TransformMatrix.translate(- this.center.x, - this.center.y,  - this.center.z));
		TransformMatrix transOut = new TransformMatrix();
		transOut.setMatrix(TransformMatrix.translate( this.center.x, this.center.y, this.center.z));
		
		TransformMatrix scale = new TransformMatrix();
		scale.setMatrix(TransformMatrix.scale(scaleFactor , scaleFactor , scaleFactor));
		
		TransformMatrix transform = new TransformMatrix();
		transform.setMatrix(transOut.getMatrix());
		transform.multiplyMatrix(scale.getMatrix());
		transform.multiplyMatrix(transIn.getMatrix());
		
		TransformMatrix nTrans = new TransformMatrix();
		
		//top surface
		topSurface.mesh.transformMesh(transform, nTrans);
		
		//down surface
		downSurface.mesh.transformMesh(transform, nTrans);
		
		//left surface
		leftSurface.mesh.transformMesh(transform, nTrans);
		
		//right surface
		rightSurface.mesh.transformMesh(transform, nTrans);
		
		//front surface			
		frontSurface.mesh.transformMesh(transform, nTrans);
				
		//rear surface		
		backSurface.mesh.transformMesh(transform, nTrans);
	}

	@Override
	public String getName() {
		return "box";
	}

}
