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
import nett.arnar.atli.dots.BoardView.GameListener;

public class GameActivity extends AppCompatActivity {
    private BoardView boardView;
    private TextView tv_score;
    private TextView tv_moves;

    private GameListener gameListener = new GameListener() {
        @Override
        public void onGameOver() {

        }

        @Override
        public void onMove(int score, int moves) {
            tv_score.setText(String.valueOf(score));
            tv_moves.setText(String.valueOf(moves));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_moves = (TextView) findViewById(R.id.tv_moves);

        //Get the message from intent
        Intent intent = getIntent();
        int numCells = intent.getIntExtra(BoardSelector.GAME_MODE, 6);

        boardView = (BoardView) findViewById(R.id.v_bv);
        boardView.setNumCells(numCells);
        boardView.setGameListener(gameListener);
    }
}
