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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class UpdateEntryActivity extends AppCompatActivity {

    public static final String INSTANCE_ID = "instanceID";
    private static final int DEFAULT_ID = -1;
    public static final String EXTRA_ITEM_ID = "extraItemId";
    public static final String EXTRA_USERNAME = "username";
    private static final String DATE_STRING = "dateString";
    private static final String TIME_STRING = "timeString";
    private static final String UPDATED_ON = "updatedOn";

    private TextView tvEntryDate;
    private TextView tvEntryTime;
    private EditText etEntryInfo;

    private int itemId = DEFAULT_ID;
    private String username;
    private Date date;

    private AppDatabase appDatabase;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);

        context = getApplicationContext();

        appDatabase = AppDatabase.getInstance(context);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvEntryDate = findViewById(R.id.tvEntryDate);
        tvEntryTime = findViewById(R.id.tvEntryTime);
        etEntryInfo = findViewById(R.id.etEntryInfo);

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_ID)) {
            itemId = savedInstanceState.getInt(INSTANCE_ID, DEFAULT_ID);
            tvEntryDate.setText(savedInstanceState.getString(DATE_STRING, ""));
            tvEntryTime.setText(savedInstanceState.getString(TIME_STRING, ""));
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
                            }
                            populateSheet(newEntries);
                        }
                    });

                }
            }
            if (intent.hasExtra(EXTRA_USERNAME)) {
                username = intent.getStringExtra("username");
            }
        }

        FloatingActionButton fabUpdate = findViewById(R.id.fabUpdate);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dateString = tvEntryDate.getText().toString();
                String timeSting = tvEntryTime.getText().toString();
                String entryInfo = etEntryInfo.getText().toString();

                if (!entryInfo.equals("")) {
                    final NewEntry newEntry = new NewEntry(username, entryInfo, timeSting, dateString, date);
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
        etEntryInfo.setText(newEntry.getThoughts());
        etEntryInfo.setSelection(etEntryInfo.getText().length());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_ID, itemId);
        outState.putString(DATE_STRING, tvEntryDate.getText().toString());
        outState.putString(TIME_STRING, tvEntryTime.getText().toString());
        outState.putLong(UPDATED_ON, DateConverter.toTimestamp(date));
        super.onSaveInstanceState(outState);

    }
}
