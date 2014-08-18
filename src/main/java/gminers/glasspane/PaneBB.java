package gminers.glasspane;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


/**
 * Pane <b>B</b>ounding <b>B</b>ox. Defines a portion of the screen, including x and y coordinates, as well as a width and height.
 * 
 * @author Aesen Vismea
 * 
 */
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PaneBB
		implements Cloneable {
	/**
	 * The X coordinate of this bounding box, in 'big' pixels.
	 */
	int x = 0;
	/**
	 * The Y coordinate of this bounding box, in 'big' pixels.
	 */
	int y = 0;
	/**
	 * The width of this bounding box, in 'big' pixels.
	 */
	int width = 0;
	/**
	 * The height of this bounding box, in 'big' pixels.
	 */
	int height = 0;
	
	/**
	 * Clones the passed PaneBB into a new object.
	 */
	public PaneBB(final PaneBB bb) {
		mimic(bb);
	}
	
	/**
	 * Gets the 'edge' Y of this bounding box.
	 * 
	 * @return y + height
	 */
	public int getEdgeY() {
		return y + height;
	}
	
	/**
	 * Gets the 'edge' X of this bounding box.
	 * 
	 * @return x + width
	 */
	public int getEdgeX() {
		return x + width;
	}
	
	/**
	 * Gets the X coordinate of the center of this bounding box.
	 * 
	 * @return x + (width / 2)
	 */
	public int getMidpointX() {
		return x + (width / 2);
	}
	
	/**
	 * Gets the Y coordinate of the center of this bounding box.
	 * 
	 * @return y + (height / 2)
	 */
	public int getMidpointY() {
		return y + (height / 2);
	}
	
	/**
	 * Returns <code>true</code> if cX and cY are within this bounding box.
	 * 
	 * @param cX
	 *            the X coordinate to check
	 * @param cY
	 *            the Y coordinate to check
	 */
	public boolean withinBounds(final int cX, final int cY) {
		return (cX >= x && cX <= getEdgeX()) && (cY >= y && cY <= getEdgeY());
	}
	
	/**
	 * Returns <code>true</code> if the given PaneBB intersects (overlaps) this one.
	 * 
	 * @param bb
	 *            The bounding box to check
	 */
	public boolean intersects(final PaneBB bb) {
		if (bb.getEdgeY() < y) {
			return false;
		}
		if (bb.getEdgeX() < x) {
			return false;
		}
		if (bb.getX() > getEdgeX()) {
			return false;
		}
		if (bb.getY() > getEdgeY()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Translates this PaneBB by the given amounts.
	 * 
	 * @param x
	 *            The X translation amount
	 * @param y
	 *            The Y translation amount
	 * @return This PaneBB, for call chaining
	 */
	public PaneBB translate(final int x, final int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * Changes the bounds of this PaneBB to match the passed one.
	 * 
	 * @param bb
	 *            The PaneBB to mimic
	 * @return <code>this</code>, for chaining
	 */
	public PaneBB mimic(final PaneBB bb) {
		x = bb.getX();
		y = bb.getY();
		width = bb.getWidth();
		height = bb.getHeight();
		return this;
	}
}
