package nett.arnar.atli.dots;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HighScoresActivity extends AppCompatActivity {
    ArrayList<HighScore> highScores = new ArrayList<HighScore>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        LinearLayout layout = (LinearLayout) findViewById(R.id.scoreLayout);

        highScores.add(new HighScore("Atli", "9999999"));
        highScores.add(new HighScore("asdf", "23"));

        drawHighScoreList(this);
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

    public void drawHighScoreList(Context context) {
        ListView list = (ListView) findViewById(R.id.scoreTable);
        ArrayAdapter<View> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        list.setAdapter(arrayAdapter);

        for(int i = 0; i < highScores.size(); i++) {
            HighScore entry = highScores.get(i);

            View view = LayoutInflater.from(context).inflate(R.layout.high_score_list_row, null);

            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(entry.name);

            TextView score = (TextView) view.findViewById(R.id.score);
            score.setText(entry.score);

            arrayAdapter.add(view);
        }

    }
}
