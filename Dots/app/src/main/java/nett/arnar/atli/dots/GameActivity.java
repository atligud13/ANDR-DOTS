package nett.arnar.atli.dots;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import nett.arnar.atli.dots.Shapes.Circle;

public class GameActivity extends AppCompatActivity {

    BoardView boardView;
    Circle[][] circles;
    int[] color_pool = new int[] {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN};
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        //Get the message from intent
        Intent intent = getIntent();
        int numCells = intent.getIntExtra(BoardSelector.GAME_MODE, 6);

        circles = new Circle[numCells][numCells];
        initCircles(numCells);

        // Creating the board view
        boardView = new BoardView(this, numCells, circles);
        boardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Adding the board view the the parent layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.gameLayout);
        layout.addView(boardView);
    }

    private void initCircles(int cells) {
        for (int i = 0; i < cells; ++i) {
            for (int j = 0; j < cells; ++j) {
                circles[i][j] = new Circle(color_pool[rand.nextInt(4)], 15);
            }
        }
    }
}
