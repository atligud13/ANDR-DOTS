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
    private final String SIX_NAMES = "names_file";
    private final String SIX_SCORES = "scores_file";
    private final String EIGHT_NAMES = "names_eight_file";
    private final String EIGHT_SCORES = "scores_eight_file";

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
        File namesSix = new File(dir + "/scores/", SIX_NAMES);
        File scoresSix = new File(dir + "/scores/", SIX_SCORES);
        File namesEight = new File(dir + "/scores/", EIGHT_NAMES);
        File scoresEight = new File(dir + "/scores/", EIGHT_SCORES);
        namesSix.delete();
        scoresSix.delete();
        namesEight.delete();
        return scoresEight.delete();
    }

    public void setClickListeners() {
        // Why does this have to be so dirty looking?
        resetHighScorePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                resetScoreList();
                Toast.makeText( getApplicationContext(), "High scores deleted", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }
}
