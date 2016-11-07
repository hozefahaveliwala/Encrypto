package com.encrypto.encrypto;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static String DB_PATH = "/data/data/com.encrypto.encrypto/databases/";
	private static String DB_NAME = "Encrypto.db";
	private static String TB_NAME = "parameters";

	private final Context myContext;
	private SQLiteDatabase myDatabase;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	// public void setDBname(String db) {
	// this.DB_NAME = db;
	// }

	public void myDBopen() {

		myDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public void myDBclose() {

		myDatabase.close();
		myDatabase.releaseReference();
	}

	public String getKey() {
		String key;
		Cursor c = myDatabase.rawQuery("Select key from " + TB_NAME, null);
		c.moveToFirst();
		key = c.getString(0);
		c.close();
		return key;
	}

	public void setKey(String name) {
		myDatabase.execSQL("Update " + TB_NAME + " set key = '" + name + "'");
	}

	public void setShowNote(int n) {
		myDatabase.execSQL("Update " + TB_NAME + " set shownote = " + n);
	}

	public int getShowNote() {
		int shownote;
		Cursor c = myDatabase.rawQuery("Select shownote from " + TB_NAME, null);
		c.moveToFirst();
		shownote = c.getInt(0);
		c.close();
		return shownote;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
