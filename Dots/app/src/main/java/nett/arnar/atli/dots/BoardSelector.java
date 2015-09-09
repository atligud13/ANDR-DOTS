package nett.arnar.atli.dots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class BoardSelector extends AppCompatActivity {
    public final static String GAME_MODE = "nett.arnar.atli.dots.GAMEMODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_selector);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board_selector, menu);
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

    public void onClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        Button buttonView = (Button) view;
        switch(buttonView.getId()) {
            case R.id.btnSixTiles :
                intent.putExtra(GAME_MODE, 6);
                break;
            case R.id.btnEightTiles :
                intent.putExtra(GAME_MODE, 8);
                break;
        }
        startActivity(intent);
    }
}
