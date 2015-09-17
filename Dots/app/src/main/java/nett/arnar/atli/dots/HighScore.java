package nett.arnar.atli.dots;

/**
 * Created by Atli Guðlaugsson on 9/10/2015.
 */
public class HighScore implements Comparable {
    public String name;
    public String score;

    public HighScore(String name, String score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(Object o) {
        HighScore h = (HighScore) o;
        int thisScore = Integer.parseInt(this.score);
        int thatScore = Integer.parseInt(h.score);
        if(thisScore < thatScore) return 0;
        return 1;
    }
}
