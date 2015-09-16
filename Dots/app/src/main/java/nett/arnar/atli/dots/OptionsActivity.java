package nett.arnar.atli.dots;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import nett.arnar.atli.dots.R;

public class OptionsActivity extends PreferenceActivity {

    Preference resetHighScorePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        resetHighScorePref = (Preference) findPreference("resetScoreList");
        setClickListeners();
    }

    public boolean resetScoreList() {
        File dir = getFilesDir();
        File names = new File(dir + "/scores/", "names_file");
        File scores = new File(dir + "/scores/", "scores_file");
        names.delete();
        return scores.delete();
    }

    public void setClickListeners() {
        // Why does this have to be so dirty looking?
        resetHighScorePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(resetScoreList()) {
                    Toast.makeText( getApplicationContext(), "High scores deleted", Toast.LENGTH_LONG).show();
                    return true;
                }
                Toast.makeText( getApplicationContext(), "You've gotta make some high scores first brah", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
}
