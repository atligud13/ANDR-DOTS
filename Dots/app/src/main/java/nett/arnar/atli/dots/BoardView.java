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
    }
}
