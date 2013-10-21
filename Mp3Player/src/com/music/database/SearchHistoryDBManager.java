package com.music.database;

import java.util.ArrayList;
import java.util.List;

import com.music.factory.model.SearchHistoryInfo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SearchHistoryDBManager {

	private SearchHistoryDBHelper dbHelper;
	private SQLiteDatabase db;
	public SearchHistoryDBManager(Context context) {
		dbHelper = new SearchHistoryDBHelper(context);		
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * ����������ʷ��¼
	 * @param searchHistoryInfos
	 */
	public void add(String keyWord) {			
		ContentValues values = new ContentValues();
		values.put("key", keyWord);
		db.insert("search_history_word", null, values);
	}
	
	/**
	 * ����������ʷ��¼
	 * @param searchHistoryInfos
	 */
	public void update(SearchHistoryInfo searchHistoryInfo){
		db.execSQL("undate search_history_word set key=" + "'"+searchHistoryInfo.getSearchKey()+",");
	}
	
	/**
	 * ɾ��������ʷ��¼
	 * @param searchHistoryInfos
	 */
	public void cancel(String key) {		
		db.execSQL("delete from search_history_word where key like " + "'"+key+"'");
	}
	
	/**
	 * ȫ��ɾ����ʷ��¼����ɾ��������ʷ��¼��
	 * @param searchHistoryInfos
	 */
	public void drop() {
		db.execSQL("drop table if exists search_history_word");
		db.execSQL("CREATE TABLE IF NOT EXISTS search_history_word" +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, key VARCHAR)");
	}
	
	/**
	 * ��ѯ������ʷ��¼��
	 * ����һ��װ����ʷ��¼��List<SearchHistoryInfo>����
	 * @return
	 */
	public List<SearchHistoryInfo> query(){
		List<SearchHistoryInfo> searchHistoryInfos = new ArrayList<SearchHistoryInfo>();
		Cursor cursor = db.query("search_history_word", null, null, null, null, null, null);
		if(cursor != null){
			while(cursor.moveToNext()) {
				SearchHistoryInfo info = new SearchHistoryInfo();
				info.setSearchKey(cursor.getString(cursor.getColumnIndex("key")));
				info.set_id(cursor.getColumnIndex("_id"));			
				searchHistoryInfos.add(info);
			}
			cursor.close();
		}		
		return searchHistoryInfos;	
	}
	
	public void DBClose(){
		db.close();
	}
}
