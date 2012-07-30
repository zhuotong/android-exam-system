package com.dream.eexam.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ExamListDB {

	private static final String TAG = "ExamListDB";

	//Database Name
	private static final String DATABASE_NAME = "db_eExam";
	//Database Version
	private static final int DATABASE_VERSION = 1;
	//Table Name
	private static final String DATABASE_TABLE = "exam_tbl";

	//Table columns
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESC = "desc";

	//Database creation sql statement
	private static final String CREATE_EXAM_TABLE = "create table if not exists "
			+ DATABASE_TABLE + "(" + ID + " text not null,"
			+ NAME + " text not null," + DESC + " text," + "primary key "+ ID + ");";

	private final Context mCtx;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	public ExamListDB(Context ctx) {
		this.mCtx = ctx;
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating DataBase: " + CREATE_EXAM_TABLE);
			db.execSQL(CREATE_EXAM_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "+ newVersion);
		}
	}

	public ExamListDB open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createExamBase(String id, String name, String desc) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ID, id);
		initialValues.put(NAME, name);
		initialValues.put(DESC, desc);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteExamBase(String id) {
		return mDb.delete(DATABASE_TABLE, ID + "=" + id , null) > 0;
	}

	public Cursor fetchAllExamBases() {
		return mDb.query(DATABASE_TABLE, new String[] {ID,NAME,DESC}, null, null, null, null, null);
	}

	public Cursor fetchExamBase(String id) throws SQLException {
		Log.i(TAG, "fetchAnswer()... ");
		String con = ID + "=" + id ;
		Log.i(TAG, "con: " + con);
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {ID,NAME,DESC}, con, null,null, null, null, null);
		return mCursor;
	}
	
	/*public Cursor fetchAnswer(long id, long name) throws SQLException {
		Log.i(TAG, "fetchAnswer()... ");
		String con = ID + "=" + id + " AND " + NAME + "=" + name;
		Log.i(TAG, "con: " + con);
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {ID,NAME,DESC}, con, null,null, null, null, null);
		return mCursor;
	}*/

	public boolean updateExamBase(String id, String name, String desc) {
		ContentValues args = new ContentValues();
		args.put(ID, id);
		args.put(NAME, name);
		args.put(DESC, desc);
		return mDb.update(DATABASE_TABLE,args,ID + "=" + id , null) > 0;
	}
	
}
