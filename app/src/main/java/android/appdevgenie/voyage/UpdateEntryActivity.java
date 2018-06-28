package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.NewEntry;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class UpdateEntryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabUpdate;
    private TextView tvEntryDate;
    private TextView tvEntryTime;
    private EditText etEntryInfo;

    private int itemId;

    private AppDatabase appDatabase;

    private Context context;

    private NewEntry newEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);

        context = getApplicationContext();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            itemId = bundle.getInt("itemId");
        }

        appDatabase = AppDatabase.getInstance(context);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvEntryDate = findViewById(R.id.tvEntryDate);
        tvEntryTime = findViewById(R.id.tvEntryTime);
        etEntryInfo = findViewById(R.id.etEntryInfo);

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

        fabUpdate = findViewById(R.id.fabUpdate);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String entryInfo = etEntryInfo.getText().toString();

                if(!entryInfo.equals("")) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
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

        if(newEntry == null) {
            return;
        }

        tvEntryDate.setText(newEntry.getEntryDate());
        tvEntryTime.setText(newEntry.getEntryTime());
        etEntryInfo.setText(newEntry.getThoughts());
        etEntryInfo.setSelection(etEntryInfo.getText().length());
    }

}
