
public class Material {
	private ColorType ka,kd,ks;
	private double ns;
	public double getNs() {
		return ns;
	}

	private boolean isDiffuse = false,isAmbient= false,isSpecular= false;
	
	public Material(ColorType ka, ColorType kd, ColorType ks, double ns) {
		this.ka = ka;
		this.kd = kd;
		this.ks = ks;
		this.ns = ns;
		
		if (ka.r>0.0 || ka.g >0.0 || ka.b>0.0) {
			isAmbient = true;
		}
		if (kd.r>0.0 || kd.g > 0.0 || kd.b >0.0) {
			isDiffuse = true;
		}
		if(ns>0 && (ks.r>0.0 || ks.g > 0.0 || ks.b>0.0))
			isSpecular = true;
		
	}

	public ColorType getKa() {
		return ka;
	}

	public ColorType getKd() {
		return kd;
	}

	public ColorType getKs() {
		return ks;
	}

	public boolean isDiffuse() {
		return isDiffuse;
	}

	public boolean isAmbient() {
		return isAmbient;
	}

	public boolean isSpecular() {
		return isSpecular;
	}

	public void setDiffuse(boolean isDiffuse) {
		this.isDiffuse = isDiffuse;
	}

	public void setAmbient(boolean isAmbient) {
		this.isAmbient = isAmbient;
	}

	public void setSpecular(boolean isSpecular) {
		this.isSpecular = isSpecular;
	}

}
