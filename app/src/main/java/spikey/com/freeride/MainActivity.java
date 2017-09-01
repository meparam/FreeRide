package spikey.com.freeride;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity{

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView resultsTextView;
    private Context context;
    private ProgressBar progressCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPlayServices();
        final FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.subscribeToTopic("test");

        progressCircle = findViewById(R.id.progress_circle);

        final Button buttonDbTest = findViewById(R.id.button_db_connect);
        buttonDbTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseOperations.databaseMessageTest();
            }
        });

        context = this;
        final Button buttonGetTasks = findViewById(R.id.button_get_tasks);
        resultsTextView = findViewById(R.id.text_results);
        buttonGetTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressCircle.setVisibility(View.VISIBLE);
                DatabaseOperations.getAvailableTasks(resultsTextView, context);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressCircle.setVisibility(View.INVISIBLE);
        checkPlayServices();
        DatabaseOperations.connectedToDatabase();
        DatabaseReference.goOnline();////////////////
        //DatabaseOperations.listen();
        //DatabaseOperations.databaseMessageTest();
    }

    //still useful?
    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }
}
