package android.appdevgenie.voyage;

import android.appdevgenie.voyage.database.AppDatabase;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private static final String TAG = MainViewModelFactory.class.getSimpleName();

    private AppDatabase appDatabase;
    private String username;

    public MainViewModelFactory(AppDatabase appDatabase, String user) {
        this.appDatabase = appDatabase;
        this.username = user;

        Log.d(TAG, "MainViewModelFactory: user: " + username);

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d(TAG, "MainViewModelFactory: modelClass");
        //noinspection unchecked
        return (T) new MainViewModel(appDatabase, username);
    }

}
