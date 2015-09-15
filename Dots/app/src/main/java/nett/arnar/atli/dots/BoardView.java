package nett.arnar.atli.dots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nett.arnar.atli.dots.Shapes.Circle;

/**
 * Created by Atli Guðlaugsson on 9/9/2015.
 */
public class BoardView extends View {
    private int m_circleGap;

    private int numCells;
    private Rect m_rect = new Rect();
    private RectF m_circle = new RectF();
    private Path m_path = new Path();
    private Paint m_paintCell = new Paint();
    private Paint m_paintCircle = new Paint();
    private Paint m_paintPath = new Paint();
    private boolean m_moving = false;
    private List<Point> m_cellPath = new ArrayList<Point>();
    private Point m_currentPoint;
    private Circle[][] circles;

    public BoardView(Context context, int cells) {
        super(context);
        numCells = cells;

        setPadding(100, 100, 100, 100);

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

    @Override
    protected void onLayout (boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initCircles();
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
        m_circleGap = boardWidth / (numCells - 1);
    }

    @Override
    public void onDraw(Canvas canvas) {
        m_rect.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(m_rect, m_paintCell);

        for (int i = 0; i < numCells; ++i) {
            int x = colToX(i);
            for (int j = 0; j < numCells; ++j) {
                int y = rowToY(j);
                circles[i][j].draw(canvas, m_paintCircle, x, y);
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
            }
            m_cellPath.clear();
            invalidate();
        }

        return true;
    }

    private void addToPath(Circle c) {
        Point point = new Point(xToCol((int)c.getX()), yToRow((int)c.getY()));

        // TODO: check if adjacent

        for (Point p : m_cellPath) {
            if (p.x == point.x && p.y == point.y) {
                // Point is already in the path
                return;
            }
        }

        m_cellPath.add(point);
    }

    private int xToCol(int x) {
        return (x - getPaddingLeft()) / m_circleGap;
    }
    private int yToRow(int y) {
        return (y - getPaddingTop()) / m_circleGap;
    }
    private int colToX(int col) {
        return getPaddingLeft() + (col * m_circleGap);
    }
    private int rowToY(int row) {
        return getPaddingTop() + (row * m_circleGap);
    }

    private Circle getOverlappingCircle(int x, int y) {
        int col = -1;

        for (int i = 0; i < numCells; ++i) {
            Circle c = circles[i][0];
            if (c.getCircle().contains(x, c.getY())) {
                col = i;
                break;
            }
        }

        if (col == -1) return null;

        for (int i = 0; i < numCells; ++i) {
            Circle c = circles[col][i];
            if (c.getCircle().contains(x, y)) {
                return c;
            }
        }

        return null;
    }

    private void removeCircles() {
        Random rand = new Random();
        int radius = getMeasuredWidth() / (numCells * 4);
        for (Point p : m_cellPath) {
            circles[p.x][p.y] = new Circle(COLOR_POOL[rand.nextInt(4)], radius);
        }
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
            for (int j = 0; j < numCells; ++j) {
                circles[i][j] = new Circle(COLOR_POOL[rand.nextInt(4)], radius);
            }
        }
    }
}
