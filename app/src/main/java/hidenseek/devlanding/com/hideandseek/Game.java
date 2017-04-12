package hidenseek.devlanding.com.hideandseek;

/**
 * Created by ted on 4/11/17.
 */

public class Game {
    public String gameCode;

    public float centerLat;
    public float centerLng;
    public double radius;

    public Game() {
    }

    public Game(String gameCode, float startLat, float startLng, double startRadius) {
        this.gameCode = gameCode;
        this.centerLat = startLat;
        this.centerLng = startLng;
        this.radius = startRadius;
    }
}
