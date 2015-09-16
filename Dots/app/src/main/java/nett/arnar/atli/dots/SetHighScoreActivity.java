package nett.arnar.atli.dots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class SetHighScoreActivity extends AppCompatActivity {
    private final String SIX_NAMES = "names_file";
    private final String SIX_SCORES = "scores_file";
    private final String EIGHT_NAMES = "names_eight_file";
    private final String EIGHT_SCORES = "scores_eight_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_high_score);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_high_score, menu);
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

    public void onSubmitClick(View view) throws IOException{
        // Getting the name and score from the user input
        EditText nameEditText = (EditText) findViewById(R.id.set_high_score_name);
        EditText scoreEditText = (EditText) findViewById(R.id.set_high_score_score);
        CheckBox six = (CheckBox) findViewById(R.id.six);
        String name = nameEditText.getText().toString();
        String score = scoreEditText.getText().toString();

        // Saving the score to the internal storage
        try {
            if(six.isChecked()) {
                saveScore(name, score, "SIX");
            }
            else {
                saveScore(name, score, "EIGHT");
            }
        }
        catch(IOException e) {
            createInternalStorageFiles();
            if(six.isChecked()) {
                saveScore(name, score, "SIX");
            }
            else {
                saveScore(name, score, "EIGHT");
            }
        }

        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
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

    // Saves the score to the internal storage
    public void saveScore(String name, String score, String mode) throws IOException{
        String names, scores;
        if(mode.equals("SIX")) {
            names = this.getFilesDir() + "/scores/" + SIX_NAMES;
            scores = this.getFilesDir() + "/scores/" + SIX_SCORES;
        }
        else {
            names = this.getFilesDir() + "/scores/" + EIGHT_NAMES;
            scores = this.getFilesDir() + "/scores/" + EIGHT_SCORES;
        }
        FileOutputStream nFos = new FileOutputStream(names, true);
        FileOutputStream sFos = new FileOutputStream(scores, true);

        nFos.write(name.getBytes());
        nFos.write(System.getProperty("line.separator").getBytes());
        sFos.write(score.getBytes());
        sFos.write(System.getProperty("line.separator").getBytes());
        nFos.close();
        sFos.close();
    }
}
