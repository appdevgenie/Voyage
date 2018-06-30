package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

public class UpdateViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static final String TAG = UpdateViewModelFactory.class.getSimpleName();

    private AppDatabase appDatabase;
    private int itmId;

    public UpdateViewModelFactory(AppDatabase appDatabase, int itmId) {
        this.appDatabase = appDatabase;
        this.itmId = itmId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d(TAG, "updateViewModelFactory: modelClass");
        //noinspection unchecked
        return (T) new UpdateViewModel(appDatabase, itmId);
    }
}
