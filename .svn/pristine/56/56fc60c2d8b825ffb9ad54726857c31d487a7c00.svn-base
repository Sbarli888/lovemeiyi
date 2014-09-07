package com.huawei.ptn.db.dao;

import com.huawei.ptn.db.DBConstant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperImpl extends SQLiteOpenHelper implements IDBHelper {
	
	private Context mContext = null;
	
	private SQLiteDatabase mDb;

	public DBHelperImpl(Context context) {
		super(context, DBConstant.DATABASE_NAME, null, DBConstant.DATABASE_VERSION);
		this.mContext = mContext;
		
		//close the existing database
		SQLiteDatabase database = getReadableDatabase();
		if (database != null) {
			database.close();
		}
	}
	
	private void createTable(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstant.TABLE_NAME_CACHE + " (objectKey VARCHAR, value VARCHAR);");
	}

	public void closeDB() {
		if (mDb != null) {
			mDb.close();
		}
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		if (mDb == null || !mDb.isOpen()) {
			openW();
		}
		return mDb.delete(table, whereClause, whereArgs);
	}

	public void deleteAll(String table) {
		delete(table, null, null);
	}

	public void execSQL(String sql) {
		if (mDb == null || !mDb.isOpen()) {
			openW();
		}
		mDb.execSQL(sql);
	}
	
	public void execSQL(String sql, Object[] bindArgs) {
		if (mDb == null || !mDb.isOpen()) {
			openW();
		}
		mDb.execSQL(sql, bindArgs);
	}

	public long insert(String table, String nullColumnHack, ContentValues values) {
		if (mDb == null || !mDb.isOpen()) {
			openW();
		}
		return mDb.insert(table, nullColumnHack, values);
	}

	public long insertOrThrow(String table, String nullColumnHack,
			ContentValues values) {
		if (mDb == null || !mDb.isOpen()) {
			openW();
		}
		return mDb.insertOrThrow(table, nullColumnHack, values);
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		if (mDb == null || !mDb.isOpen()) {
			openR();
		} 
		return mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
	
	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		if (mDb == null || !mDb.isOpen()) {
			openR();
		}
		return mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}

	public Cursor query(boolean distinct, String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		if (mDb == null || !mDb.isOpen()) {
			openR();
		} 
		return mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		if (mDb == null || !mDb.isOpen()) {
			openR();
		} 
		return mDb.rawQuery(sql, selectionArgs);
	}

	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		if (mDb == null || !mDb.isOpen()) {
			openW();
		}
		return mDb.update(table, values, whereClause, whereArgs);
	}

	public void openR() {
		mDb = getReadableDatabase();
	}

	public void openW() {
		mDb = getWritableDatabase();
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return mDb;
	}

	public void initDatabase(SQLiteDatabase database) {
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
























