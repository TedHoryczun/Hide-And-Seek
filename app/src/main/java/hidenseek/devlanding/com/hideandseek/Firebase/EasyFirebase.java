package hidenseek.devlanding.com.hideandseek.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import hidenseek.devlanding.com.hideandseek.Game;
import hidenseek.devlanding.com.hideandseek.Maps.MapsMVP;
import hidenseek.devlanding.com.hideandseek.R;

/**
 * Created by ted on 3/16/17.
 */

public class EasyFirebase implements MapsMVP.EasyFirebase {
    private final Context context;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private String TAG = " EASYFIREBASE";
    private FirebaseUser user;
    private String gameCodeOfGamePlaying;

    public EasyFirebase(Context context) {
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        listenForAuthRequests();
    }

    @Override
    public void login() {
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                        }

                        // ...
                    }
                });
    }

    @Override
    public void listenForAuthRequests(){
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        });
    }
    @Override
    public void unListenForAuthRequests(){
        firebaseAuth.removeAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        });
    }

    @Override
    public String getUUID() {
        return null;
    }

    public void isGameCodeAlreadyUsed(String uniqueGameCode, final Firebaselistener firebaselistener) {
        firebaseDatabase.child(uniqueGameCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    firebaselistener.onSuccess();
                }else{
                    firebaselistener.onError();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getCode();

            }
        });
    }

    public HideGame createGame(String uniqueGameCode) {
        String userId = user.getUid();
        String seekerString = context.getResources().getString(R.string.seeker);
        String gamesString = context.getResources().getString(R.string.game);
        firebaseDatabase.child(gamesString).child(uniqueGameCode)
                .child(userId).setValue(seekerString);
        gameCodeOfGamePlaying = uniqueGameCode;
        return new HideGame(uniqueGameCode, true);
    }
    public void joinGame(final String gameCode, final Firebaselistener listener){

        String gameString = context.getResources().getString(R.string.game);
        firebaseDatabase.child(gameString).child(gameCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    gameCodeOfGamePlaying = gameCode;
                    insertUserIdIntoGame(gameCode);
                    listener.onSuccess();
                }else{
                    listener.onError();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getCode();

            }
        });

    }

    private void insertUserIdIntoGame(String gameCode) {
        String userId = user.getUid();
        String hiderString = context.getResources().getString(R.string.hider);
        String gameString = context.getResources().getString(R.string.game);
        firebaseDatabase.child(gameString).child(gameCode)
                .child(userId).setValue(hiderString);
    }
    public void leaveAllGames(){
        String gameString = context.getResources().getString(R.string.game);
        String userId = user.getUid();
        firebaseDatabase.child(gameString).child(gameCodeOfGamePlaying)
                .child(userId).removeValue();
    }

    public void updateCurrentLocation(double latitude, double longitude) {
        String userId = user.getUid();
        firebaseDatabase.child("players")
                .child(userId)
                .child("currentLocation")
                .child("latitude").setValue(latitude);

        firebaseDatabase.child("players")
                .child(userId)
                .child("currentLocation")
                .child("longitude").setValue(longitude);
    }

    public void gameHasEnoughPlayers(final Firebaselistener listener) {
        String gameString = context.getResources().getString(R.string.game);
        String userId = user.getUid();
        firebaseDatabase.child(gameString).child(gameCodeOfGamePlaying).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long children = dataSnapshot.getChildrenCount();
                if(children > 1){
                    listener.onSuccess();
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void insertGameRules(Circle metersAllowedToPlayIn) {
        LatLng center = metersAllowedToPlayIn.getCenter();
        double radius = metersAllowedToPlayIn.getRadius();
        firebaseDatabase.child("game").child(gameCodeOfGamePlaying).child("rules")
                .child("centerLat").setValue(center.latitude);
        firebaseDatabase.child("game").child(gameCodeOfGamePlaying).child("rules")
                .child("centerLng").setValue(center.longitude);
        firebaseDatabase.child("game").child(gameCodeOfGamePlaying).child("rules")
                .child("radius").setValue(radius);

    }

    public void getGameRules(String gameCode) {
       firebaseDatabase.child("game").child(gameCode).child("rules").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   Game game = dataSnapshot.getValue(Game.class);
                   EventBus.getDefault().post(game);
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
    }
}
