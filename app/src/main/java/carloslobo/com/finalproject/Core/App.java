package carloslobo.com.finalproject.Core;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

/**
 * Created by camilo on 11/7/15.
 */
public class App extends Application{
    private final static String TAG = App.class.getName();

    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ewrrzF25zpTLoeWUq76hoHuK0YnxgNi6wHr97CoT", "7mQG0iJbKabtsFfxza1PTTQusfwvi6FOYOQChnyF");

        Log.d(TAG,"Parse is ready");
    }
}