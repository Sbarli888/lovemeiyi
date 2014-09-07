package com.huawei.ptn.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface IDBHelper {
	
	public void closeDB();
	
	public int delete(String table, String whereClause, String[] whereArgs);
	
	public void deleteAll(String table);
	
	public void execSQL(String sql);
	
	public void execSQL(String sql, Object[] bindArgs);
	
	public long insert(String table, String nullColumnHack, ContentValues values);
	
	public long insertOrThrow(String table, String nullColumnHack, ContentValues values);	
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, 
			String groupBy, String having, String orderBy);
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, 
			String groupBy, String having, String orderBy, String limit);
	
	public Cursor query(boolean distinct, String table, String[] columns, String selection, 
			String[] selectionArgs, String groupBy, String having, String orderBy, String limit);
	
	public Cursor rawQuery(String sql, String[] selectionArgs);
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs);
	
	public void openR();
	
	public void openW();
	
	public SQLiteDatabase getSQLiteDatabase();
	
	public void initDatabase(SQLiteDatabase database);
	
}

























