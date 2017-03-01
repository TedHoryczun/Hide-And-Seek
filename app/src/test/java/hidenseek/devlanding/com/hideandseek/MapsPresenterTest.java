package hidenseek.devlanding.com.hideandseek;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import hidenseek.devlanding.com.hideandseek.Maps.MapsMVP;
import hidenseek.devlanding.com.hideandseek.Maps.MapsPresenter;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MapsPresenterTest {
    @Rule
    public MockitoRule mockitoRule =
            MockitoJUnit.rule();

    @InjectMocks
    MapsPresenter presenter, spyPresenter;
    @Mock
    MapsMVP.Interactor interactor;
    @Mock
    MapsMVP.view view;
    @Mock
    MapsMVP.presenter presenterinterface;
    @Mock
    Context context;
    @Mock
    Location location;
    @Before
    public void setup(){
        spyPresenter = Mockito.spy(presenter);
    }
    @Test
    public void doesDisplayErrorMsgIfOutOfHideAndSeekArea() throws Exception {

        Mockito.when(interactor.isUserOutSideHideAndSeekArea(location)).thenReturn(true);

        presenter.showErrorMsgIfOutOfHideAndSeekAreaBounds(location);

        Mockito.verify(interactor).isUserOutSideHideAndSeekArea(location);
        Mockito.verify(view).displayErrorMsgIfOutOfHideAndSeekAreaBounds(location);
    }

    @Test
    public void updatesMetersAllowedToPlayCircleOnMapWhenUpdatingPlayAreaSize(){
        LatLng latLng = new LatLng(1.2,1.2);
        int meters = 2;
        Mockito.when(spyPresenter.isMetersAllowedToPlayInAlreadyDrawn()).thenReturn(true);
        spyPresenter.addMetersAllowedToPlay(latLng, meters);

        Mockito.verify(view).updateCircleAroundCurrentLocationWhereAllowedToPlay(latLng, meters);
    }
    @Test
    public void createsMetersAllowedToPlayCircleWhenFirstCreatePlayAreaSize(){
        LatLng latLng = new LatLng(1.2,1.2);
        int meters = 2;
        Mockito.when((spyPresenter.isMetersAllowedToPlayInAlreadyDrawn())).thenReturn(false);
        spyPresenter.addMetersAllowedToPlay(latLng, meters);

        Mockito.verify(view).addCircleAroundCurrentLocationWhereAllowedToPlay(latLng, meters);

    }


}