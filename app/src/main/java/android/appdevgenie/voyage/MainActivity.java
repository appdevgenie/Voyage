package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.NewEntry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VoyageAdapter.ItemClickListener {

    private static final int SIGN_IN = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    private String username;
    private Context context;

    private Toolbar toolbar;

    private VoyageAdapter voyageAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        initAuthStateListener();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        voyageAdapter = new VoyageAdapter(this, this);
        recyclerView.setAdapter(voyageAdapter);

        //used to delete entry on swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        int position = viewHolder.getAdapterPosition();
                        List<NewEntry> entries = voyageAdapter.getEntries();
                        appDatabase.entryDao().deleteEntry(entries.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);


        FloatingActionButton fabNewEntry = findViewById(R.id.fabAddEntry);
        fabNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        appDatabase = AppDatabase.getInstance(context);

        //populateList();
    }

    private void initAuthStateListener() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {

                    String signedIn = firebaseUser.getDisplayName();

                    onSignedInInitialized(signedIn);

                    toolbar.setSubtitle(signedIn);

                } else {

                    username = "";

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.login_logo)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            SIGN_IN);
                }
            }
        };
    }

    private void onSignedInInitialized(String displayName) {

        username = displayName;

        populateList();

    }

    private void populateList() {
        //final LiveData<List<NewEntry>> entries = appDatabase.entryDao().loadAllEntriesByUsername(username);
        MainViewModel mainViewModel = ViewModelProviders.of(this, new MainViewModelFactory(appDatabase, username)).get(MainViewModel.class);
        mainViewModel.getNewEntries().observe(this, new Observer<List<NewEntry>>() {
            @Override
            public void onChanged(@Nullable List<NewEntry> newEntries) {
                Log.d(TAG, "onChanged: update from LiveData");
                voyageAdapter.setEntries(newEntries);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(context, context.getString(R.string.signed_in), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, context.getString(R.string.sign_in_cancelled), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();

        switch (itemID) {

            case R.id.menu_sign_out:

                AuthUI.getInstance().signOut(this);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onItemClickListener(int itemId) {

        Intent intent = new Intent(MainActivity.this, UpdateEntryActivity.class);
        intent.putExtra(UpdateEntryActivity.EXTRA_ITEM_ID, itemId);
        intent.putExtra(UpdateEntryActivity.EXTRA_USERNAME, username);
        startActivity(intent);

    }
}
