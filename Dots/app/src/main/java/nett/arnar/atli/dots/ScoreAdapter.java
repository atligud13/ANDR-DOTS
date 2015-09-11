package nett.arnar.atli.dots;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
/**
 * Created by Atli Guðlaugsson on 9/11/2015.
 */

public class ScoreAdapter extends ArrayAdapter<HighScore> {

    private final Context context;
    private final List<HighScore> values;

    public ScoreAdapter(Context context, List<HighScore> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.high_score_list_row, parent,false);

        // Setting the name of the player
        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        nameView.setText( values.get(position).name );

        // Setting the score of the player
        TextView scoreView = (TextView) rowView.findViewById(R.id.score);
        scoreView.setText(values.get(position).score);

        return rowView;
    }
}