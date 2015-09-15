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
        return this.score.compareTo(h.score);
    }
}
