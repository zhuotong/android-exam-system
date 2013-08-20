package com.dream.eexam.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseUtil{

	private static final String TAG = "DatabaseUtil";

	//Database Name
	private static final String DATABASE_NAME = "db_eExam";
	//Database Version
	private static final int DATABASE_VERSION = 1;
	//Table Name
	private static final String DATABASE_TABLE = "answer_tbl";

	/**
	 * Table columns
	 */
	public static final String CATALOG_ID = "cid";
	public static final String QUESTION_ID = "qid";
	public static final String QUESTION_ID_STRING = "qid_str";
	public static final String ANSWERS = "answers";

	/**
	 * Database creation sql statement
	 */
	private static final String CREATE_ANSWER_TABLE = "create table if not exists "
			+ DATABASE_TABLE + "(" + CATALOG_ID + " integer not null,"
			+ QUESTION_ID + " integer not null," + QUESTION_ID_STRING + " text," + ANSWERS + " text," + "primary key("
			+ CATALOG_ID + "," + QUESTION_ID + ")" + ");";

	/**
	 * Context
	 */
	private final Context mCtx;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Inner private class. Database Helper class for creating and updating database.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		/**
		 * onCreate method is called for the 1st time when database doesn't exists.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating DataBase: " + CREATE_ANSWER_TABLE);
			db.execSQL(CREATE_ANSWER_TABLE);
		}
		/**
		 * onUpgrade method is called when database version changes.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "+ newVersion);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 *
	 * @param ctx the Context within which to work
	 */
	public DatabaseUtil(Context ctx) {
		this.mCtx = ctx;
	}
	/**
	 * This method is used for creating/opening connection
	 * @return instance of DatabaseUtil
	 * @throws SQLException
	 */
	public DatabaseUtil open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	/**
	 * This method is used for closing the connection.
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * This method is used to create/insert new record.
	 * @param name
	 * @param value
	 * @return long
	 */
	public long createAnswer(long cid, long qid,String qidStr,String answers) {
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(CATALOG_ID, cid);
		initialValues.put(QUESTION_ID, qid);
		initialValues.put(QUESTION_ID_STRING, qidStr);
		initialValues.put(ANSWERS, answers);
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	/**
	 * This method will delete record.
	 * @param rowId
	 * @return boolean
	 */
	public boolean deleteAnswer(long cid, long qid) {
		return mDb.delete(DATABASE_TABLE, CATALOG_ID + "=" + cid + " AND " + QUESTION_ID + "=" + qid, null) > 0;
	}
	
	public boolean deleteAllAnswers() {
		return mDb.delete(DATABASE_TABLE, null, null) > 0;
	}

	/**
	 * This method will return Cursor holding all  records.
	 * @return Cursor
	 */
	public Cursor fetchAllAnswers() {
		return mDb.query(DATABASE_TABLE, new String[] {CATALOG_ID,QUESTION_ID,QUESTION_ID_STRING,ANSWERS}, null, null, null, null, null);
	}
	
	public int fetchAllAnswersCount() {
		int sum=0;
		Cursor cursor = mDb.query(DATABASE_TABLE, new String[] {CATALOG_ID,QUESTION_ID,QUESTION_ID_STRING,ANSWERS}, null, null, null, null, null);
		while(cursor.moveToNext()){
			sum++;
		}
		return sum;
	}

	public Cursor fetchAnswer(long cid) throws SQLException {
		Log.i(TAG, "fetchAnswer()... ");
		String con = CATALOG_ID + "=" + cid ;
		Log.i(TAG, "con: " + con);
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {CATALOG_ID,QUESTION_ID,QUESTION_ID_STRING,ANSWERS}, con, null,null, null, null, null);
		return mCursor;
	}
	
	/**
	 * This method will return Cursor holding the specific record.
	 * @param id
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor fetchAnswer(long cid, long qid) throws SQLException {
		Log.i(TAG, "fetchAnswer()... ");
		String con = CATALOG_ID + "=" + cid + " AND " + QUESTION_ID + "=" + qid;
		Log.i(TAG, "con: " + con);
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {CATALOG_ID,QUESTION_ID,QUESTION_ID_STRING,ANSWERS}, con, null,null, null, null, null);
		/*if (mCursor != null) {
			mCursor.moveToFirst();
		}*/
		return mCursor;
	}

	/**
	 * This method will update record.
	 * @param id
	 * @param name
	 * @param value
	 * @return boolean
	 */
	public boolean updateAnswer(long cid, long qid,String qidStr, String answers) {
		ContentValues args = new ContentValues();
		args.put(CATALOG_ID, cid);
		args.put(QUESTION_ID, qid);
		args.put(QUESTION_ID_STRING, qidStr);
		args.put(ANSWERS, answers);
		return mDb.update(DATABASE_TABLE,args,CATALOG_ID + "=" + cid + " AND " + QUESTION_ID + "=" + qid, null) > 0;
	}
	
	public void printStoredDataInDB(){
		Log.i(TAG,"----------------data in SQLLite-----------------");
    	Cursor cursor = fetchAllAnswers() ;
    	while(cursor.moveToNext()){
    		int cid = cursor.getInt(0);
    		int qid = cursor.getInt(1);
			String qidStr = cursor.getString(2);
			String answer = cursor.getString(3);
			Log.i(TAG, "cid="+String.valueOf(cid)+" qid="+String.valueOf(qid)+" qidStr="+qidStr+" answer="+answer);
		}
    	cursor.close();
	}
	
	public void saveAnswer(DatabaseUtil dbUtil,Integer cid,Integer qid,String qidStr,String answers){
    	Cursor cursor = dbUtil.fetchAnswer(cid,qid);
    	if(cursor != null && cursor.moveToNext()){
    		dbUtil.updateAnswer(cid,qid,qidStr,answers);
    	}else{
    		dbUtil.createAnswer(cid,qid,qidStr,answers);
    	}
    }
}
