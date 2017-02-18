package hidenseek.devlanding.com.hideandseek;

import android.content.Context;

/**
 * Created by ted on 2/18/17.
 */

public class HideAndSeekAreaPresenter implements HideAndSeekAreaMVP.presenter{

    private final HideAndSeekAreaMVP.view view;

    public HideAndSeekAreaPresenter(HideAndSeekAreaMVP.view view){
        this.view = view;
    }

    @Override
    public void showSeekBarProgress(int progressInMeters) {
        view.getSeekBarProgressInMeters(progressInMeters);
    }
}
