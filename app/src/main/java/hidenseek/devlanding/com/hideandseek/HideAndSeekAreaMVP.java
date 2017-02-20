package hidenseek.devlanding.com.hideandseek;

/**
 * Created by ted on 2/18/17.
 */

public interface HideAndSeekAreaMVP {
    interface view{
       void getSeekBarProgressInMeters(int progressInMeters);

        void sendProgressInMetersToMapsActivity(int progressInMeters);
    }
    interface presenter{
        void showSeekBarProgress(int progressInMeters);

        void placeCircleRadiusAroundCurrentPositionOnMap(int progressInMeters);
    }
}
