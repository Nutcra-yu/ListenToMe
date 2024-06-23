package android.ListenToMe;

import android.content.Context;
import android.content.SharedPreferences;

public class MySPManager {

    //sp名字
    public static final String SP_NAME = "userinfo";

    public static final String INFO_PHONE = "phone";
    public static final String INFO_PASSWORD = "passWord";
    public static final String INFO_NAME = "name";
    public static final String INFO_GENDER = "gender";
    public static final String INFO_BIRTH = "birthDay";

    //单例
    private static MySPManager instance;

    //私有构造器
    private MySPManager() {
    }

    /**
     * 开放获取单例
     */
    public static MySPManager getSPInstance(){
        if (instance == null)
            instance = new MySPManager();
        return instance;
    }

    /**
     * 私有获取SP 工具类
     */
    private SharedPreferences getSP(Context context){
        return context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }

    /**
     * 保存SP中的信息
     */
    public void saveInt(Context context,String KEY,int num){
        SharedPreferences.Editor editor = getSPInstance().getSP(context).edit();

        editor.putInt(KEY,num);

        editor.apply();
    }

    public void saveString(Context context,String KEY,String str){
        SharedPreferences.Editor editor = getSPInstance().getSP(context).edit();

        editor.putString(KEY,str);

        editor.apply();
    }

    public void saveLong(Context context,String KEY,long num){
        SharedPreferences.Editor editor = getSPInstance().getSP(context).edit();

        editor.putLong(KEY,num);

        editor.apply();
    }

    /**
     * 获取SP中的信息
     */
    public String getString(Context context,String KEY){
        return getSP(context).getString(KEY,"none");
    }

    public int getInt(Context context,String KEY){
        return getSP(context).getInt(KEY,0);
    }

    public long getLong(Context context,String KEY){
        return getSP(context).getLong(KEY,0);
    }




}
