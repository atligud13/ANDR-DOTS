package nett.arnar.atli.dots;

import android.content.Context;
import android.content.ContextWrapper;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoresActivity extends AppCompatActivity {
    ArrayList<HighScore> highScores = new ArrayList<>();
    ArrayList<HighScore> highScoresEight = new ArrayList<>();
    private ListView list;
    private ListView listEight;
    private ScoreAdapter adapter;
    private ScoreAdapter adapterEight;
    private final String SIX_NAMES = "names_file";
    private final String SIX_SCORES = "scores_file";
    private final String EIGHT_NAMES = "names_eight_file";
    private final String EIGHT_SCORES = "scores_eight_file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        // Setting the input streams to the files
        // Doing this in each separate try/catch because
        // if either one is empty, an exception will be thrown
        // and the other one won't be printed out.
        try {
            populateSixHighScoresList();
        }
        catch(IOException e) {
            createInternalStorageFiles();
        }

        try {
            populateEightHighScoresList();
        }
        catch(IOException e) {
            createInternalStorageFiles();
        }

        // Finding the list and filling it with the score data
        list = (ListView) findViewById(R.id.scoreTable);
        listEight = (ListView) findViewById(R.id.scoreTableEight);
        adapter = new ScoreAdapter(this, highScores);
        adapterEight = new ScoreAdapter(this, highScoresEight);
        list.setAdapter(adapter);
        listEight.setAdapter(adapterEight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Creates a directory with two files, names and scores
    public void createInternalStorageFiles() {
        File scores = new File(this.getFilesDir(), "scores");
        scores.mkdir();
        File names_file = new File(this.getFilesDir() + "/scores/", SIX_NAMES);
        File scores_file = new File(this.getFilesDir() + "/scores/", SIX_SCORES);
        File names_eight_file = new File(this.getFilesDir() + "/scores/", EIGHT_NAMES);
        File scores_eight_file = new File(this.getFilesDir() + "/scores/", EIGHT_SCORES);
    }

    public void populateSixHighScoresList() throws IOException {
        BufferedReader nameReader = new BufferedReader( new FileReader(this.getFilesDir() + "/scores/" + SIX_NAMES) );
        BufferedReader scoreReader = new BufferedReader( new FileReader(this.getFilesDir() + "/scores/" + SIX_SCORES));
        String         name, score;

        // Constructing the lists
        while( ( name = nameReader.readLine() ) != null ) {
            if( (score = scoreReader.readLine() ) != null) {
                highScores.add(new HighScore(name, score));
            }
        }

        Collections.sort(highScores);
    }

    public void populateEightHighScoresList() throws IOException {
        BufferedReader nameEightReader = new BufferedReader( new FileReader(this.getFilesDir() + "/scores/" + EIGHT_NAMES) );
        BufferedReader scoreEightReader = new BufferedReader( new FileReader(this.getFilesDir() + "/scores/" + EIGHT_SCORES));
        String         name, score;

        while( ( name = nameEightReader.readLine() ) != null ) {
            if( (score = scoreEightReader.readLine() ) != null) {
                highScoresEight.add(new HighScore(name, score));
            }
        }

        // Sorting the high scores
        Collections.sort(highScoresEight);
    }
}
