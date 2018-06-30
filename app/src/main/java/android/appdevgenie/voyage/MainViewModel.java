package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.NewEntry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<NewEntry>> newEntries;

    public MainViewModel(@NonNull AppDatabase appDatabase, String user) {

        Log.d(TAG, "MainViewModel: user: " + user);
        newEntries = appDatabase.entryDao().loadAllEntriesByUsername(user);

    }

    public LiveData<List<NewEntry>> getNewEntries(){
        return newEntries;
    }

}
