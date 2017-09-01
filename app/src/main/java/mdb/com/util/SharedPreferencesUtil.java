package mdb.com.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author james on 8/7/17.
 */

public class SharedPreferencesUtil {

    private static final String NEWM = "newm_pref";

    public static final String MOVIE_DETAILS_MAIN_COLOR = "movie_details_color";

    public static void saveInt(String key, int value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEWM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defaultVal, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEWM, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultVal);
    }
}
