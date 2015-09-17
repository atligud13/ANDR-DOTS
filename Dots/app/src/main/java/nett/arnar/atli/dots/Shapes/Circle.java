package nett.arnar.atli.dots.Shapes;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import nett.arnar.atli.dots.BoardView;

/**
 * Created by arnar on 9/10/15.
 */
public class Circle {
    private RectF circle;
    private int color;
    private int radius;

    public Circle(BoardView view, int color, int radius, int x, int y) {
        this.circle = new RectF();
        this.circle.set(x - radius, y - radius, x + radius, y + radius);
        this.color = color;
        this.radius = radius;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        canvas.drawOval(circle, paint);
    }

    public void moveTo(int x, int y) {
        this.circle.set(x - radius, y - radius, x + radius, y + radius);
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

    public void setRadius(int rad) {
        int x = (int)getX();
        int y = (int)getY();
        this.radius = rad;
        circle.set(x - rad, y - rad, x + rad, y + rad);
    }
}
