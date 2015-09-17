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
    private BoardView view;
    private ValueAnimator locationAnimator = new ValueAnimator();
    private ValueAnimator radiusAnimator = new ValueAnimator();

    private AnimatorUpdateListener locationListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int newY = (int) animation.getAnimatedValue();
            moveTo((int) getX(), newY);
            view.invalidate();
        }
    };

    private AnimatorUpdateListener radiusListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int newRadius = (int) animation.getAnimatedValue();
            setRadius(newRadius);
            view.invalidate();
        }
    };

    public Circle(BoardView view, int color, int radius, int x, int y) {
        this.circle = new RectF();
        this.circle.set(x - radius, y - radius, x + radius, y + radius);
        this.color = color;
        this.radius = radius;
        this.view = view;

        this.radiusAnimator.setDuration(200);
        this.radiusAnimator.setIntValues(1, radius);
        this.radiusAnimator.addUpdateListener(radiusListener);
        this.radiusAnimator.start();
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        canvas.drawOval(circle, paint);
    }

    public void moveTo(int x, int y) {
        this.circle.set(x - radius, y - radius, x + radius, y + radius);
    }

    public void animateY(int yTo) {
        locationAnimator.removeAllUpdateListeners();
        locationAnimator.setDuration(200);
        locationAnimator.setIntValues((int)getY(), yTo);
        locationAnimator.addUpdateListener(locationListener);
        locationAnimator.start();
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

    private void setRadius(int rad) {
        int x = (int)getX();
        int y = (int)getY();
        this.radius = rad;
        circle.set(x - rad, y - rad, x + rad, y + rad);
    }
}
