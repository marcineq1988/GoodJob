package mpikula.goodjob;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Marcin on 2015-05-10.
 */
public class Settings {

    private static final String PREFS_NAME = "moje ustawienia";
    private static final String KEY_TEXT1 = "pref.job";
    private static final String KEY_TEXT2 = "pref.location";
    private static final String KEY_FAVORITES = "pref.favorites";
    private String mText1; // Preferencja TEXT JOB
    private String mText2; // Preferencja TEXT LOCATION

    private ArrayList<String> mFavorites = new ArrayList<String>();

    private SharedPreferences mPreferences;

    public Settings(Context mContext){
        mPreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        refresh();
    }

    public Settings refresh(){
        this.mText1 = mPreferences.getString(KEY_TEXT1, "");
        this.mText2 = mPreferences.getString(KEY_TEXT2, "");
        return this;
    }

    public String getmText1() {
        return mText1;
    }

    public Settings setmText1(String mText1) {
        this.mText1 = mText1;
        return this;
    }

    public String getmText2() {
        return mText2;
    }

    public Settings setmText2(String mText2) {
        this.mText2 = mText2;
        return this;
    }

    public void save(){
        mPreferences.edit()
                .putString(KEY_TEXT1, mText1)
                .putString(KEY_TEXT2, mText2)
                .commit();
    }
}