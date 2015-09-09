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

/**
 * Created by Atli Guðlaugsson on 9/9/2015.
 */
public class BoardView extends View {
    private int m_cellWidth;
    private int m_cellHeight;
    private int numCells;
    private Rect m_rect = new Rect();
    private RectF m_circle = new RectF();
    private Path m_path = new Path();
    private Paint m_paintCell = new Paint();
    private Paint m_paintCircle = new Paint();
    private Paint m_paintPath = new Paint();
    private boolean m_moving = false;
    private List<Point> m_cellPath = new ArrayList<Point>();

    public BoardView(Context context, int numCells) {
        super(context);
        this.numCells = numCells;

        // Setting default values
        m_paintCell.setColor(Color.BLACK);
        m_paintCell.setStyle(Paint.Style.STROKE);
        m_paintCell.setStrokeWidth(2);
        m_paintCell.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        m_rect.set(0, 0, 100, 100);
        canvas.drawRect(m_rect, m_paintCell);
    }
}
