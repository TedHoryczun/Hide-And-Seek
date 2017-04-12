package hidenseek.devlanding.com.hideandseek;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hidenseek.devlanding.com.hideandseek.Firebase.HideGame;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HideAndSeekArea.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HideAndSeekArea#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HideAndSeekArea extends Fragment implements SeekBar.OnSeekBarChangeListener, HideAndSeekAreaMVP.view{
    @BindView(R.id.seekBar) SeekBar seekBarMetersAllowedToPlay;

    @BindView(R.id.metersTextView) TextView textShowSeekBarProgress;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;
    private HideAndSeekAreaPresenter presenter;

    public HideAndSeekArea() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HideAndSeekArea.
     */
    // TODO: Rename and change types and number of parameters
    public static HideAndSeekArea newInstance(String param1, String param2) {
        HideAndSeekArea fragment = new HideAndSeekArea();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @OnClick(R.id.playGameButton)
    public void playGameButtonClick(){
        HideGame game = new HideGame(mParam1, true);
        EventBus.getDefault().post(game);
        sendProgressInMetersToMapsActivity(seekBarMetersAllowedToPlay.getProgress());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_hide_and_seek_area, container, false);
        ButterKnife.bind(this, view);
        presenter = new HideAndSeekAreaPresenter(this);
        seekBarMetersAllowedToPlay.setOnSeekBarChangeListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        presenter.showSeekBarProgress(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void getSeekBarProgressInMeters(int progressInMeters) {
        textShowSeekBarProgress.setText(String.valueOf(progressInMeters));
        presenter.placeCircleRadiusAroundCurrentPositionOnMap(progressInMeters);
    }

    @Override
    public void sendProgressInMetersToMapsActivity(int progressInMeters) {
        EventBus.getDefault().post(progressInMeters);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
