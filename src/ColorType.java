
public class ColorType {
	public float r, g, b;

	public ColorType(float _r, float _g, float _b) {
		r = _r;
		g = _g;
		b = _b;
	}
	
	public ColorType(double _r, double _g, double _b)
	{
		r = (float)_r;
		g = (float)_g;
		b = (float)_b;
	}

	public int getBRGUint8() {
		int _b = Math.round(b * 255.0f);
		int _g = Math.round(g * 255.0f);
		int _r = Math.round(r * 255.0f);

		return (_r << 16) | (_g << 8) | _b;
	}

	/**
	 * <b>Description:</b></br>
	 * Add the two colors together 
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 * the new color
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 * 
	 * @param otherColor the color to add
	 * @return  The new color add together
	 */
	public ColorType colorAdd(ColorType otherColor) {
		return new ColorType(this.r + otherColor.r, this.g + otherColor.g,
				this.b + otherColor.b);
	}

	/**
	 * <b>Description:</b></br>
	 *  Subtract the otherColor
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 *  the subtraction result
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 *   @param otherColor the color to subtract
	 *   @return The new color after subtraction
	 */
	public ColorType colorDiff(ColorType otherColor) {
		return new ColorType(this.r - otherColor.r, this.g - otherColor.g,
				this.b - otherColor.b);
	}

	/**
	 * <b>Description:</b></br>
	 *  Times two colors together
	 * </br></br>
	 *
	 * <b>Output:</b></br>
	 *  The result after multiplication
	 * </br></br>
	 *
	 * <b>Input:</b></br>
	 * 
	 * @param ratio the ratio to multiply
	 * @return multiplication result
	 */
	public ColorType colorTimes(float ratio) {
		return new ColorType(this.r * ratio, this.g * ratio, this.b * ratio);
	}
}
