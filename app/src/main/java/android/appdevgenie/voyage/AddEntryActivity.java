package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.NewEntry;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEntryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MOOD_SAD = R.drawable.ic_action_sad;
    private static final int MOOD_NEUTRAL = R.drawable.ic_action_neutral;
    private static final int MOOD_HAPPY = R.drawable.ic_action_happy;
    private static final String SAVE_MOOD = "saveMood";

    private EditText etEntry;
    private ImageView ivEntryMood;

    private Context context;
    private String dateString;
    private String timeString;
    private String username;
    private int mood = MOOD_NEUTRAL;

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_entry);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            username = bundle.getString("username");
        }

        setupVariables();

        if (savedInstanceState != null) {
            ivEntryMood.setImageResource(savedInstanceState.getInt(SAVE_MOOD));
        }
    }

    private void setupVariables() {

        context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(username);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ivEntryMood = findViewById(R.id.ivEntryMood);
        ivEntryMood.setImageResource(MOOD_NEUTRAL);
        ImageButton ibSad = findViewById(R.id.ibNewSad);
        ImageButton ibNeutral = findViewById(R.id.ibNewNeutral);
        ImageButton ibHappy = findViewById(R.id.ibNewHappy);
        ibSad.setOnClickListener(this);
        ibNeutral.setOnClickListener(this);
        ibHappy.setOnClickListener(this);

        appDatabase = AppDatabase.getInstance(context);

        TextView tvDate = findViewById(R.id.tvEntryDate);
        TextView tvTime = findViewById(R.id.tvEntryTime);
        etEntry = findViewById(R.id.etEntryInfo);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("cccc, dd MMMM, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        dateString = dateFormat.format(calendar.getTime());
        tvDate.setText(dateString);
        timeString = timeFormat.format(calendar.getTime());
        tvTime.setText(timeString);

        FloatingActionButton fabAdd = findViewById(R.id.fabAddNewEntry);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String entryInfo = etEntry.getText().toString();
                Date date = new Date();

                if(!entryInfo.equals("")) {
                    final NewEntry newEntry = new NewEntry(username, entryInfo, timeString, dateString, mood, date);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            appDatabase.entryDao().insertEntry(newEntry);
                            finish();
                        }
                    });

                } else {

                    Toast.makeText(context, context.getString(R.string.add_thoughts), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the MainActivity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SAVE_MOOD, mood);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ibNewSad:

                ivEntryMood.setImageResource(MOOD_SAD);
                mood = MOOD_SAD;

                break;

            case R.id.ibNewNeutral:

                ivEntryMood.setImageResource(MOOD_NEUTRAL);
                mood = MOOD_NEUTRAL;

                break;

            case R.id.ibNewHappy:

                ivEntryMood.setImageResource(MOOD_HAPPY);
                mood = MOOD_HAPPY;

                break;
        }
    }
}
