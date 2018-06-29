package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.NewEntry;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEntryActivity extends AppCompatActivity {

    private EditText etEntry;

    private Context context;
    private String dateString;
    private String timeString;
    private String username;

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
    }

    private void setupVariables() {

        context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    final NewEntry newEntry = new NewEntry(username, entryInfo, timeString, dateString, date);
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
}
