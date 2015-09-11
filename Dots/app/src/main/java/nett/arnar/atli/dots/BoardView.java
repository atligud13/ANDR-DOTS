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

import nett.arnar.atli.dots.Shapes.Circle;

/**
 * Created by Atli Guðlaugsson on 9/9/2015.
 */
public class BoardView extends View {
    private int m_cellWidth;
    private int m_cellHeight;
    private int m_circleMargin;
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

    public BoardView(Context context, int numCells, Circle[][] circles) {
        super(context);
        this.circles = circles;
        this.numCells = numCells;

        // Setting default values
        m_paintCell.setColor(Color.BLACK);
        m_paintCell.setStyle(Paint.Style.STROKE);
        m_paintCell.setStrokeWidth(2);
        m_paintCell.setAntiAlias(true);

        m_paintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        m_paintCircle.setAntiAlias(true);

        m_paintPath.setStrokeWidth(10);
        m_paintPath.setStyle(Paint.Style.STROKE);
        m_paintPath.setAntiAlias(true);
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
        m_circleMargin = boardWidth / (numCells + 1);
    }

    @Override
    public void onDraw(Canvas canvas) {
        m_rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRect(m_rect, m_paintCell);

        for (int i = 0; i < numCells; ++i) {
            int x = m_circleMargin + (i * m_circleMargin);
            for (int j = 0; j < numCells; ++j) {
                int y = m_circleMargin + (j * m_circleMargin);
                circles[i][j].draw(canvas, m_paintCircle, x, y);
            }
        }

        if (!m_cellPath.isEmpty()) {
            m_path.reset();
            Point point = m_cellPath.get(0);
            m_path.moveTo(point.x, point.y);
            for(int i = 1; i < m_cellPath.size(); i++) {
                point = m_cellPath.get(i);
                m_path.lineTo(point.x, point.y);
            }
            //m_path.lineTo(m_currentPoint.x, m_currentPoint.y);
            canvas.drawPath(m_path, m_paintPath);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        Circle overlapping = getOverlappingCircle(x, y);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (overlapping != null) {
                m_paintPath.setColor(overlapping.getColor());
                m_cellPath.add(new Point((int) overlapping.getX(), (int) overlapping.getY()));
                m_currentPoint = new Point(x, y);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (overlapping != null && overlapping.getColor() == m_paintPath.getColor()) {
                m_cellPath.add(new Point((int)overlapping.getX(), (int)overlapping.getY()));
                m_currentPoint.set(x, y);
                invalidate();
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            m_cellPath.clear();
            m_currentPoint = null;
            invalidate();
        }

        return true;
    }

    private Circle getOverlappingCircle(int x, int y) {
        int col = -1, row = 0;

        for (int i = 0; i < numCells; ++i) {
            Circle c = circles[i][row];
            if (c.getCircle().contains(x, c.getY())) {
                col = i;
                break;
            }
        }

        if (col == -1) return null;

        for (int i = 0; i < numCells; ++i) {
            Circle c = circles[col][i];
            if (c.getCircle().contains(x, y)) {
                row = i;
                break;
            }
        }

        return circles[col][row];
    }
}
