package com.example.pedarkharj_edit2.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MydbHelper {
    private static SQLiteDatabase mydb;
    private static MyDatabase mdbInstance;
    private static MydbHelper mdbHelper;
    private Context mContext;

    private MydbHelper(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized MydbHelper getInstance(Context mContext) {
        if (mdbInstance == null) {
            mdbInstance = new MyDatabase(mContext);
        }
        if (mdbHelper == null) {
            mdbHelper = new MydbHelper(mContext);
        }
        mydb = mdbInstance.getWritableDatabase();
        return mdbHelper;
    }

    public void insertToTable(int id, String name, String imgString){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabase.ID, id);
        contentValues.put(MyDatabase.NAME, name);
        contentValues.put(MyDatabase.IMG_STRING, imgString);
        mydb.insert(MyDatabase.MYTB, null, contentValues);
        mydb.close();
    }

    void updateImg (int id, String newImgString){
        ContentValues values = new ContentValues();
        values.put(MyDatabase.IMG_STRING, newImgString);
        mydb.update(MyDatabase.MYTB,    values,      MyDatabase.ID +" = ?",       new String[]{String.valueOf(id)} );
        mydb.close();
    }

    void updateName (int id, String newName){
        ContentValues values = new ContentValues();
        values.put("name", newName);
        mydb.update(MyDatabase.MYTB,    values,      MyDatabase.ID +" = ?",       new String[]{String.valueOf(id)} );
        mydb.close();
    }

    public void delRow (int id){
       mydb.delete(MyDatabase.MYTB, MyDatabase.ID+ " =?", new String[]{String.valueOf(id)} );
    }

    public void delAllData (){
        mydb.delete(MyDatabase.MYTB, null, null);
    }

    public int getRowsCount(){
        Cursor cursor = mydb.rawQuery("SELECT * FROM "+ MyDatabase.MYTB, null);
        int rowCount = cursor.getCount();
        cursor.close();
        return rowCount;
    }

    public TableData[] getAllTable() {
        Cursor cursor = mydb.rawQuery("SELECT * FROM "+ MyDatabase.MYTB, null);
        int rowCount = cursor.getCount();

        TableData[] tableData = new TableData[rowCount] ;
        int i = 0;
        while (cursor.moveToNext()){
            tableData[i] = new TableData();
            tableData[i].id = cursor.getInt(cursor.getColumnIndex(MyDatabase.ID));
            tableData[i].name = cursor.getString(cursor.getColumnIndex(MyDatabase.NAME));
            tableData[i].imgString = cursor.getString(cursor.getColumnIndex(MyDatabase.IMG_STRING));
            i++;
        }
        cursor.close();
        mydb.close();
        return tableData;
    }



    public class TableData{
        private  int id;
        private String name, imgString;

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getImgString() {
            return imgString;
        }
        public void setImgString(String imgString) {
            this.imgString = imgString;
        }
    }

    public static class MyDatabase extends SQLiteOpenHelper {
        final static String DB_NAME = "databaseName";
         final static int DB_VERSION = 1;
         static final String MYTB = "contacts";
         static final String ID = "id";
         static final String NAME = "name";
         static final String IMG_STRING = "imgString";

        //create DB
        MyDatabase(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }


        //create table with attrs if not exists
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+
                    MYTB+ " ("+
                    ID+ " INTEGER PRIMARY KEY, "+
                    NAME+ " TEXT, "+
                    IMG_STRING+ " TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        }
    }

}
