package android.appdevgenie.voyage;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private static final String TAG = MainViewModelFactory.class.getSimpleName();

    private Application application;
    private String username;

    public MainViewModelFactory(Application application, String user) {
        this.application = application;
        this.username = user;

        Log.d(TAG, "MainViewModelFactory: user: " + username);

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d(TAG, "MainViewModelFactory: modelClass");
        return (T) new MainViewModel(application, username);
    }

}
