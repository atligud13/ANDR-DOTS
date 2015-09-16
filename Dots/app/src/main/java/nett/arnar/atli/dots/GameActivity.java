package nett.arnar.atli.dots;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;
import android.preference.PreferenceManager;
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
    public String SCORE = "nett.arnar.atli.dots.GameActivity";
    private BoardView boardView;
    private TextView tv_score;
    private TextView tv_moves;
    private Vibrator m_vibrator;
    private SharedPreferences m_sp;
    private boolean m_useVibrator;

    private GameListener gameListener = new GameListener() {
        @Override
        public void onGameOver(int score) {
            Intent intent = new Intent(GameActivity.this, SetHighScoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra(SCORE, score);
            startActivity(intent);
        }

        @Override
        public void onMove(int score, int moves) {
            tv_score.setText(String.valueOf(score));
            tv_moves.setText(String.valueOf(moves));
            if (m_useVibrator) {
                m_vibrator.vibrate(500);
            }
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

        m_vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        m_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        m_useVibrator = m_sp.getBoolean("vibrate", false);
    }
}
