package com.music.mp3player;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.music.R;

import com.music.constant.MusicContant;
import com.music.database.SearchHistoryDBManager;
import com.music.factory.model.SearchHistoryInfo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class SearchHistoryActivity extends ListActivity{
	private AutoCompleteTextView AutoCompleteView = null;
	private ImageButton searchButton = null;
	private ImageButton clearButton = null;
	private View clearHistoryView = null;
	private ListView listView = null;
	
	private SearchHistoryDBManager dbManager = null;
	private HistoryListAdapter historyListAdapter = null;
	private List<SearchHistoryInfo> searchHistoryInfos = null;
	public List<String> suggest;   
	private ArrayAdapter<String> autoCompleteAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
		
		setContentView(R.layout.search_history);
		AutoCompleteView = (AutoCompleteTextView)findViewById(R.id.search_text);
		AutoCompleteView.addTextChangedListener(new AutoCompleteTextListener());
		AutoCompleteView.setOnItemClickListener(new AutoSuggestionItemListener());
		AutoCompleteView.requestFocus();
		//������ť
		searchButton = (ImageButton)findViewById(R.id.search_button);
		searchButton.setOnClickListener(new SearchButtonListener());
		
		//listview�ײ����������ʷ��¼����ͼ
		clearHistoryView = getLayoutInflater().inflate(R.layout.search_history_clear_item, null);
		clearHistoryView.setClickable(true);
		
		//���������ʷ��¼�İ�ť
		clearButton = (ImageButton) clearHistoryView.findViewById(R.id.clear_history_button);
		clearButton.setOnClickListener(new ClearAllListener());
		
		//listview��ӵײ���ͼ
		listView = getListView();
		listView.addFooterView(clearHistoryView);
		
		//�����ݿ⣬�����ز�ѯ���
		dbManager = new SearchHistoryDBManager(this);		
		searchHistoryInfos = dbManager.query();	
		
		//��ʼ��BaseAdapter�����б���ʽ��ʾ��ѯ���
		initAdapter(searchHistoryInfos,dbManager);
		
		
	}
	
	/**
	 * ��Activity���ɼ�ʱ���򱻴ݻ�ʱ���ã��Թر����ݿ�ʹݻ�Activity��һ�ص��������Activity
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dbManager.DBClose();
		finish();
	}

	/**
	 * ��ʼ��BaseAdapter
	 * @param searchHistoryInfos
	 * @param dbManager
	 */
	private void initAdapter(List<SearchHistoryInfo> searchHistoryInfos, SearchHistoryDBManager dbManager) {
		historyListAdapter = new HistoryListAdapter(this, searchHistoryInfos, dbManager);		
		this.setListAdapter(historyListAdapter);
	}
	
	/**
	 * ɾ������������¼
	 */
	private class ClearAllListener implements OnClickListener {
		public void onClick(View v) {		
			historyListAdapter.canceAllItems();			
		}		
	}
	
	/**
	 * ���������㲥
	 * @param keyWord
	 */
	private void sendSearchBroadcast(String keyWord) {
		Intent intent = new Intent();							
		intent.putExtra("keyWord", keyWord);
		intent.setAction(MusicContant.SEARCH_KEY_WORD_ACTION);
		sendBroadcast(intent);
	}
	
	/**
	 * ������ť�������������������������Ĺؼ���
	 * @author Administrator
	 */
	private class SearchButtonListener implements OnClickListener {
		public void onClick(View v) {
			String keyWord = AutoCompleteView.getText().toString();//�õ������ؼ���			
			//��Ϊ��ʱ���㲥��SearchActivit��������,���ݻٵ�ǰSearchHistoryActivity���Ա�ص�ԭ������SearchActivity
			if(!keyWord.equals("")) {								
				sendSearchBroadcast(keyWord);
				dbManager.add(keyWord);
				SearchHistoryActivity.this.finish();
			} else {
				Toast.makeText(SearchHistoryActivity.this, "����Ϊ��", Toast.LENGTH_SHORT).show();
			}
		}		
	}
	
	/**
	 * ����б�����жԸ�����ʷ��¼���в�ѯ
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		sendSearchBroadcast(searchHistoryInfos.get(position).getSearchKey());
		finish();
	}
	
	/**
	 * �Զ���ʾ�����ļ�����
	 * @author Administrator
	 */
	private class AutoCompleteTextListener implements TextWatcher {

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String newText = s.toString();  
			new getAutoCompleteText().execute(newText);			
		}

		public void afterTextChanged(Editable s) {

		}		
	}
	
	private class getAutoCompleteText extends AsyncTask<String,String,String>{  
		  
		@Override  
		protected void onPostExecute(String result) {  
		    super.onPostExecute(result);  
		    autoCompleteAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.search_keyword_autocomplete_item,suggest);  
		    AutoCompleteView.setAdapter(autoCompleteAdapter);  
		    autoCompleteAdapter.notifyDataSetChanged();
		}

		@Override
		protected String doInBackground(String... key) {		 
			 try{  
			     HttpClient hClient = new DefaultHttpClient();  
			     HttpGet hGet = new HttpGet("http://tingapi.ting.baidu.com/v1/restserver/" +
			     		"ting?method=baidu.ting.search.suggestion&format=json&query=" + key[0].trim());  
			     ResponseHandler<String> rHandler = new BasicResponseHandler();  
			     String data = hClient.execute(hGet, rHandler);
			     
			     suggest = new ArrayList<String>();
			     JSONObject jo = new JSONObject(data);            
		         JSONArray jsonArray = (JSONArray) jo.get("suggestion_list");            
		         for (int i = 0; i < jsonArray.length(); ++i) {
		        	 suggest.add((String) jsonArray.get(i));          
		         }	         			    
			     } catch (Exception e) {  
			      Log.w("SuggestionError", e.getMessage());  
			   }  
			 return null;			   
		}		
	}
	
	private class AutoSuggestionItemListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			sendSearchBroadcast(suggest.get(position));
			dbManager.add(suggest.get(position));
			SearchHistoryActivity.this.finish();		
		}		
	}
}
