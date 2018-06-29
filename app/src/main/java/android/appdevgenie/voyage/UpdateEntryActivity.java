package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.NewEntry;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateEntryActivity extends AppCompatActivity {

    public static final String INSTANCE_ID = "instanceID";
    private static final int DEFAULT_ID = -1;
    public static final String EXTRA_ITEM_ID = "extraItemId";
    private static final String DATE_STRING = "dateString";
    private static final String TIME_STRING = "timeString";

    private TextView tvEntryDate;
    private TextView tvEntryTime;
    private EditText etEntryInfo;

    private int itemId = DEFAULT_ID;

    private AppDatabase appDatabase;
    private Context context;
    private NewEntry newEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);

        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("itemId")) {
            if (itemId == DEFAULT_ID) {
                itemId = bundle.getInt("itemId");
            }
        }*/

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
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_ITEM_ID)) {
            if (itemId == DEFAULT_ID) {
                itemId = intent.getIntExtra(EXTRA_ITEM_ID, DEFAULT_ID);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        newEntry = appDatabase.entryDao().loadEntryById(itemId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateSheet(newEntry);
                            }
                        });
                    }
                });
            }
        }

        FloatingActionButton fabUpdate = findViewById(R.id.fabUpdate);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String entryInfo = etEntryInfo.getText().toString();

                if (!entryInfo.equals("")) {

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            newEntry = appDatabase.entryDao().loadEntryById(itemId);
                            newEntry.setId(itemId);
                            newEntry.setThoughts(entryInfo);
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
        super.onSaveInstanceState(outState);

    }
}
