package nett.arnar.atli.dots.Shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by arnar on 9/10/15.
 */
public class Circle {
    RectF circle;
    int color;
    int radius;

    public Circle(int color, int radius) {
        this.circle = new RectF();
        this.color = color;
        this.radius = radius;
    }

    public void draw(Canvas canvas, Paint paint, int x, int y) {
        circle.set(x - radius, y - radius, x + 2*radius, y + 2*radius);
        paint.setColor(color);
        canvas.drawOval(circle, paint);
    }

    public RectF getCircle() {
        return circle;
    }

    public float getX() {
        return circle.left + ((circle.right - circle.left) / 2);
    }

    public float getY() {
        return circle.top + ((circle.bottom - circle.top) / 2);
    }

    public int getColor() {
        return color;
    }
}
