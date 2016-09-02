package rs.pupin.custompolyline2;

/**
 * Created by Larissa on 30.08.2016.
 * 2dimensional vector of floats
 */
public class Vec2f {
    private float x;
    private float y;

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
