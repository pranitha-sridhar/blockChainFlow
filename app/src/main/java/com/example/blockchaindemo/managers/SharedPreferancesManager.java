package com.example.blockchaindemo.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferancesManager {
    private  SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREFERENCES_DATA="com.example.blockchaindemo";
    private static final String ENCRYPTION_STATUS="encryption_status";
    private static final String DARK_THEME="dark_theme";
    private static final String PROOF_OF_WORK="proof_of_work";
    private static final int DEFAULT_POW=2;

    public SharedPreferancesManager(Context context){
        sharedPreferences=context.getSharedPreferences(PREFERENCES_DATA,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.apply();
    }

    public void setPowValue(int powValue){
        editor.putInt(PROOF_OF_WORK,powValue);
        editor.commit();
    }

    public int getPowValue(){
        return sharedPreferences.getInt(PROOF_OF_WORK,DEFAULT_POW);
    }

    public void setEncryptionStatus(boolean isActivated){
        editor.putBoolean(ENCRYPTION_STATUS,isActivated);
        editor.commit();
    }

    public boolean getEncryptionStatus(){
        return sharedPreferences.getBoolean(ENCRYPTION_STATUS,false);
    }


}
