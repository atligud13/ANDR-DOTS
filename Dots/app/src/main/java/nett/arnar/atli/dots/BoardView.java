package nett.arnar.atli.dots;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import nett.arnar.atli.dots.Shapes.Circle;

/**
 * Created by Atli Guðlaugsson on 9/9/2015.
 */
public class BoardView extends View {
    private int m_cellWidth;
    private int numCells;
    private Path m_path = new Path();
    private Paint m_paintCell = new Paint();
    private Paint m_paintCircle = new Paint();
    private Paint m_paintPath = new Paint();
    private List<Point> m_cellPath = new ArrayList<Point>();
    private Point m_currentPoint;
    private int m_score = 0;
    private int m_moves = 30;
    private Circle[][] circles;
    private GameListener listener;

    private HashMap<Circle, Integer> animatingCircles = new HashMap<>();
    private ArrayList<Circle> newCircles = new ArrayList<>();

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        numCells = 6;

        m_currentPoint = new Point(0, 0);

        // Setting default values
        m_paintCell.setColor(Color.BLACK);
        m_paintCell.setStyle(Paint.Style.STROKE);
        m_paintCell.setStrokeWidth(2);
        m_paintCell.setAntiAlias(true);

        m_paintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        m_paintCircle.setAntiAlias(true);

        m_paintPath.setStrokeWidth(12);
        m_paintPath.setStyle(Paint.Style.STROKE);
        m_paintPath.setAntiAlias(true);
    }

    public void setGameListener(GameListener l) {
        listener = l;
        listener.onMove(m_score, m_moves);
    }

    public void setNumCells(int cells) {
        numCells = cells;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // Only initialize one time
        if (circles == null) initCircles();
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        int boardWidth = (xNew - getPaddingLeft() - getPaddingRight());
        m_cellWidth = boardWidth / numCells;
    }

    @Override
    public void onDraw(Canvas canvas) {

        for (int i = 0; i < numCells; ++i) {
            for (int j = 0; j < numCells; ++j) {
                circles[i][j].draw(canvas, m_paintCircle);
            }
        }

        if (!m_cellPath.isEmpty()) {
            m_path.reset();
            Point point = m_cellPath.get(0);
            m_path.moveTo(colToX(point.x), rowToY(point.y));
            for (int i = 1; i < m_cellPath.size(); i++) {
                point = m_cellPath.get(i);
                m_path.lineTo(colToX(point.x), rowToY(point.y));
            }
            m_path.lineTo(m_currentPoint.x, m_currentPoint.y);
            canvas.drawPath(m_path, m_paintPath);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        m_currentPoint.set(x, y);

        Circle overlapping = getOverlappingCircle(x, y);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (overlapping != null) {
                // A circle was hit
                m_paintPath.setColor(overlapping.getColor());
                addToPath(overlapping);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (overlapping != null) {
                if (m_cellPath.isEmpty()) {
                    m_paintPath.setColor(overlapping.getColor());
                }
                if (overlapping.getColor() == m_paintPath.getColor()) {
                    addToPath(overlapping);
                }
            }
            invalidate();
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (m_cellPath.size() > 1) {
                removeCircles();
                m_moves--;
                m_score += m_cellPath.size();
                listener.onMove(m_score, m_moves);
                if (m_moves < 1) listener.onGameOver(m_score);
            }
            m_cellPath.clear();
            invalidate();
        }

        return true;
    }

    private void addToPath(Circle c) {
        Point point = new Point(xToCol((int)c.getX()), yToRow((int)c.getY()));

        // Check if it's the first point
        if (!m_cellPath.isEmpty()) {
            Point lastP = m_cellPath.get(m_cellPath.size() - 1);

            // Check if point is adjacent
            if (!(lastP.x - 1 == point.x && lastP.y == point.y || // Left of
                  lastP.x + 1 == point.x && lastP.y == point.y || // Right of
                  lastP.y - 1 == point.y && lastP.x == point.x || // Above
                  lastP.y + 1 == point.y && lastP.x == point.x))  // Below
                return;
        }

        m_cellPath.add(point);
    }

    private int xToCol(int x) {
        return (x - getPaddingLeft()) / m_cellWidth;
    }
    private int yToRow(int y) {
        return (y - getPaddingTop()) / m_cellWidth;
    }
    private int colToX(int col) {
        return (getPaddingLeft() + (col * m_cellWidth)) + (m_cellWidth / 2);
    }
    private int rowToY(int row) {
        return (getPaddingTop() + (row * m_cellWidth)) + (m_cellWidth / 2);
    }

    private Circle getOverlappingCircle(int x, int y) {
        int col = xToCol(x);
        int row = yToRow(y);

        // Make sure col and row is within range
        if (col < numCells && row < numCells && col >= 0 && row >= 0)
            return circles[col][row];

        return null;
    }

    private void removeCircles() {
        Random rand = new Random();
        int distance[] = new int[numCells];

        // Sorting the points by their y value so our gravity works correctly
        Collections.sort(m_cellPath, new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return Integer.compare(lhs.y, rhs.y);
            }
        });

        for (Point p : m_cellPath) {
            // Applying simple gravity
            for (int i = p.y; i > 0; --i) {
                circles[p.x][i] = circles[p.x][i - 1];
                circles[p.x][i - 1] = null;
                if (circles[p.x][i] != null) {
                    // The circle and its destination y is stored
                    animatingCircles.put(circles[p.x][i], rowToY(i));
                }
            }
            circles[p.x][0] = null;
        }

        // Creating new circles where needed
        for (int i = 0; i < numCells; ++i) {
            int x = colToX(i);
            for (int j = 0; circles[i][j] == null && j < numCells; ++j) {
                int y = rowToY(j);
                circles[i][j] = new Circle(this, COLOR_POOL[rand.nextInt(5)], 0, x, y);
                // Storing new circles so we can animate their radius
                newCircles.add(circles[i][j]);
                distance[i] += m_cellWidth;
            }
        }

        animateGravity(distance);
    }

    private ValueAnimator gravityAnimator = new ValueAnimator();

    private void animateGravity(final int distances[]) {
        gravityAnimator.removeAllUpdateListeners();
        gravityAnimator.setDuration(200);
        gravityAnimator.setFloatValues(0.0f, 1.0f);
        gravityAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                for (Map.Entry<Circle, Integer> e : animatingCircles.entrySet()) {
                    Circle c = e.getKey();

                    // Calculating the circle's original y position
                    int x = (int)c.getX();
                    int yTo = e.getValue();
                    int yFrom = yTo - distances[xToCol(x)];

                    int y = (int)((1.0-value) * yFrom + value * yTo);
                    c.moveTo(x, y);
                }
                invalidate();

                if (value == 1.0f) {
                    animatingCircles.clear();
                    animateRadius();
                }
            }
        });
        gravityAnimator.start();
    }

    private ValueAnimator radiusAnimator = new ValueAnimator();

    private void animateRadius() {
        // Find a good radius for each circle
        final float radius = getMeasuredWidth() / (numCells * 4);
        radiusAnimator.removeAllUpdateListeners();
        radiusAnimator.setDuration(200);
        radiusAnimator.setFloatValues(0.0f, radius);
        radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                for (Circle c : newCircles) {
                    c.setRadius((int)value);
                }
                invalidate();

                if (value == radius) {
                    newCircles.clear();
                }
            }
        });
        radiusAnimator.start();
    }

    private static int[] COLOR_POOL = new int[] {
            Color.argb(255, 122, 215, 222), // blue
            Color.argb(255, 222, 129, 122), // red
            Color.argb(255, 165, 222, 122), // green
            Color.argb(255, 179, 122, 222), // purple
            Color.argb(255, 219, 207, 110)  // yellow
    };

    private void initCircles() {
        Random rand = new Random();
        // Initialize array of circles
        circles = new Circle[numCells][numCells];
        // Find a good radius for each circle
        int radius = getMeasuredWidth() / (numCells * 4);
        for (int i = 0; i < numCells; ++i) {
            int x = colToX(i);
            for (int j = 0; j < numCells; ++j) {
                int y = rowToY(j);
                circles[i][j] = new Circle(this, COLOR_POOL[rand.nextInt(5)], radius, x, y);
            }
        }
    }

    public interface GameListener {
        public void onGameOver(int score);
        public void onMove(int score, int moves);
    }
}
