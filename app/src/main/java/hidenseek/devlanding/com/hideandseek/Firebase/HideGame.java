package hidenseek.devlanding.com.hideandseek.Firebase;

/**
 * Created by ted on 4/7/17.
 */

public class HideGame {
    public final String gameCodeOfGamePlaying;
    public final boolean isSeeker;

    public double startLat;
    public double startLng;
    public double startRadius;

    public HideGame(String gameCodeOfGamePlaying, boolean isSeeker, double startLat, double startLng, double startRadius) {
        this.gameCodeOfGamePlaying = gameCodeOfGamePlaying;
        this.isSeeker = isSeeker;
        this.startLat = startLat;
        this.startLng = startLng;
        this.startRadius = startRadius;
    }

    public HideGame(String gameCodeOfGamePlaying, boolean isSeeker) {
       this.gameCodeOfGamePlaying = gameCodeOfGamePlaying;
        this.isSeeker = isSeeker;
    }
}
