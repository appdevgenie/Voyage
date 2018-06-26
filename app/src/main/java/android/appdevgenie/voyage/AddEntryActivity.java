package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.NewEntry;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEntryActivity extends AppCompatActivity {

    private TextView tvDate;
    private TextView tvTime;
    private EditText etEntry;
    private FloatingActionButton fabAdd;

    private Context context;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
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

        appDatabase = AppDatabase.getInstance(context);

        Toast.makeText(context, username, Toast.LENGTH_SHORT).show();

        tvDate = findViewById(R.id.tvEntryDate);
        tvTime = findViewById(R.id.tvEntryTime);
        etEntry = findViewById(R.id.etEntryInfo);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("cccc, dd MMMM, yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        dateString = dateFormat.format(calendar.getTime());
        tvDate.setText(dateString);
        timeString = timeFormat.format(calendar.getTime());
        tvTime.setText(timeString);

        fabAdd = findViewById(R.id.fabAddNewEntry);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String entryInfo = etEntry.getText().toString();
                Date date = new Date();

                if(!entryInfo.equals("")) {
                    NewEntry newEntry = new NewEntry(username, entryInfo, timeString, dateString, date);
                    appDatabase.entryDao().insertEntry(newEntry);
                    finish();
                }
            }
        });
    }
}
