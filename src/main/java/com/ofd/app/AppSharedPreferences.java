package com.ofd.app;



import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {
	
	private Context ctx;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    
    public AppSharedPreferences(Context context) {
    	this.ctx = context;
		settings = ctx.getSharedPreferences(AppConstants.NAME_SHARED_PREFERENCES, 0);
		editor = settings.edit();
		settings.registerOnSharedPreferenceChangeListener(listener);
	}

    
    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {	
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {		
			
		}
	};
	
	public  void setConversationAsDelivery(String key, String value,boolean isDelivery,String type) {
		try{
			editor.putString(key+"objId", value);
            editor.putString(key+"type",type);
            editor.putBoolean(key,isDelivery);
			editor.commit();			
		}
		catch(Exception e){
			e.printStackTrace();}
	}
	
	public boolean getConversationIsDelivery(String key){
		return settings.getBoolean(key, false);
	}

    public String getConversationObjId(String key){
        return settings.getString(key+"objId",null);
    }

    public String getUserType(String key){
        return settings.getString(key+"type","n");
    }

    public  void setBool(String key, boolean value) {
        try{
            editor.putBoolean(key,value);
            editor.commit();
        }
        catch(Exception e){
            e.printStackTrace();}
    }

    public boolean getBool(String key){
        return settings.getBoolean(key, false);
    }






}
