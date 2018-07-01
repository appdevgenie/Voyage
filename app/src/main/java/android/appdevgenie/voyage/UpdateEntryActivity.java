package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.DateConverter;
import android.appdevgenie.voyage.database.NewEntry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

public class UpdateEntryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = UpdateEntryActivity.class.getSimpleName();
    public static final String INSTANCE_ID = "instanceID";
    private static final int DEFAULT_ID = -1;
    public static final String EXTRA_ITEM_ID = "extraItemId";
    public static final String EXTRA_USERNAME = "username";
    private static final String DATE_STRING = "dateString";
    private static final String TIME_STRING = "timeString";
    private static final String SAVE_MOOD = "saveMood";
    private static final String UPDATED_ON = "updatedOn";
    private static final int MOOD_SAD = R.drawable.ic_action_sad;
    private static final int MOOD_NEUTRAL = R.drawable.ic_action_neutral;
    private static final int MOOD_HAPPY = R.drawable.ic_action_happy;

    private TextView tvEntryDate;
    private TextView tvEntryTime;
    private EditText etEntryInfo;
    private ImageView ivEntryMood;

    private int itemId = DEFAULT_ID;
    private int mood = MOOD_NEUTRAL;
    private String username;
    private Date date;

    private AppDatabase appDatabase;
    private Context context;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);

        setupVariables();

        if (savedInstanceState != null) {
            itemId = savedInstanceState.getInt(INSTANCE_ID, DEFAULT_ID);
            tvEntryDate.setText(savedInstanceState.getString(DATE_STRING, ""));
            tvEntryTime.setText(savedInstanceState.getString(TIME_STRING, ""));
            ivEntryMood.setImageResource(savedInstanceState.getInt(SAVE_MOOD, MOOD_NEUTRAL));
            date = DateConverter.toDate(savedInstanceState.getLong(UPDATED_ON));
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_ITEM_ID)) {
                if (itemId == DEFAULT_ID) {
                    itemId = intent.getIntExtra(EXTRA_ITEM_ID, DEFAULT_ID);

                    UpdateViewModelFactory updateViewModelFactory = new UpdateViewModelFactory(appDatabase, itemId);
                    final UpdateViewModel updateViewModel = ViewModelProviders.of(this, updateViewModelFactory).get(UpdateViewModel.class);
                    updateViewModel.getNewEntry().observe(this, new Observer<NewEntry>() {
                        @Override
                        public void onChanged(@Nullable NewEntry newEntries) {
                            updateViewModel.getNewEntry().removeObserver(this);

                            if (newEntries != null) {
                                date = newEntries.getUpdatedOn();
                                mood = newEntries.getMood();
                            }
                            populateSheet(newEntries);
                        }
                    });

                }
            }
            if (intent.hasExtra(EXTRA_USERNAME)) {
                username = intent.getStringExtra("username");
                toolbar.setSubtitle(username);
            }
        }

    }

    private void setupVariables() {

        context = getApplicationContext();

        appDatabase = AppDatabase.getInstance(context);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ivEntryMood = findViewById(R.id.ivEntryMood);
        ImageButton ibSad = findViewById(R.id.ibNewSad);
        ImageButton ibNeutral = findViewById(R.id.ibNewNeutral);
        ImageButton ibHappy = findViewById(R.id.ibNewHappy);
        ibSad.setOnClickListener(this);
        ibNeutral.setOnClickListener(this);
        ibHappy.setOnClickListener(this);

        tvEntryDate = findViewById(R.id.tvEntryDate);
        tvEntryTime = findViewById(R.id.tvEntryTime);
        etEntryInfo = findViewById(R.id.etEntryInfo);

        FloatingActionButton fabUpdate = findViewById(R.id.fabUpdate);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dateString = tvEntryDate.getText().toString();
                String timeSting = tvEntryTime.getText().toString();
                String entryInfo = etEntryInfo.getText().toString();

                if (!entryInfo.equals("")) {
                    final NewEntry newEntry = new NewEntry(username, entryInfo, timeSting, dateString, mood, date);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            newEntry.setId(itemId);
                            appDatabase.entryDao().updateEntry(newEntry);
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(context, context.getString(R.string.add_thoughts), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateSheet(NewEntry newEntry) {

        if (newEntry == null) {
            return;
        }

        tvEntryDate.setText(newEntry.getEntryDate());
        tvEntryTime.setText(newEntry.getEntryTime());
        ivEntryMood.setImageResource(newEntry.getMood());
        etEntryInfo.setText(newEntry.getThoughts());
        etEntryInfo.setSelection(etEntryInfo.getText().length());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(INSTANCE_ID, itemId);
        outState.putString(DATE_STRING, tvEntryDate.getText().toString());
        outState.putString(TIME_STRING, tvEntryTime.getText().toString());
        outState.putInt(SAVE_MOOD, mood);
        Log.d(TAG, "onSaveInstanceState: mood: " + String.valueOf(mood));
        outState.putLong(UPDATED_ON, DateConverter.toTimestamp(date));

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mood = savedInstanceState.getInt(SAVE_MOOD, MOOD_NEUTRAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.update_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();

        switch (itemID) {

            case R.id.action_update:

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("cc, dd/MM/yy, HH:mm", Locale.getDefault());
                String dateString = dateFormat.format(calendar.getTime());
                dateString = "[" + dateString + "] : ";
                etEntryInfo.setSelection(etEntryInfo.getText().length());
                etEntryInfo.append("\n\n" + dateString);

                return true;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

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
