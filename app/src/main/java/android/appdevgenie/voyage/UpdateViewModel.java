package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.appdevgenie.voyage.database.NewEntry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class UpdateViewModel extends ViewModel {

    private LiveData<NewEntry> newEntry;

    public UpdateViewModel(AppDatabase appDatabase, int itemId) {

        newEntry = appDatabase.entryDao().loadEntryById(itemId);
    }

    public LiveData<NewEntry> getNewEntry(){
        return newEntry;
    }
}
