package android.appdevgenie.voyage;

import android.app.Application;
import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.NewEntry;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<NewEntry>> newEntries;

    public MainViewModel(@NonNull Application application, String user) {
        super(application);

        Log.d(TAG, "MainViewModel: user: " + user);
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        newEntries = appDatabase.entryDao().loadAllEntriesByUsername(user);

    }

    public LiveData<List<NewEntry>> getNewEntries(){
        return newEntries;
    }

}
