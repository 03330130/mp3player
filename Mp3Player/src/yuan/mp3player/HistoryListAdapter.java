package yuan.mp3player;

import java.util.List;

import yuan.database.SearchHistoryDBManager;
import yuan.model.SearchHistoryInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class HistoryListAdapter extends BaseAdapter{
	private List<SearchHistoryInfo> searchHistoryInfos = null;	
	private LayoutInflater inflater =null;
	private SearchHistoryDBManager dbManager = null;
	
	public HistoryListAdapter(Context context, List<SearchHistoryInfo> searchHistoryInfos,
			SearchHistoryDBManager dbManager){		
		this.searchHistoryInfos = searchHistoryInfos;
		this.dbManager = dbManager;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return searchHistoryInfos.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return searchHistoryInfos.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {  
			convertView = inflater.inflate(R.layout.search_history_item, null);  
		}
		ImageButton history_button_flag = (ImageButton) convertView.findViewById(R.id.search_history_flag);
		TextView history_search_word = (TextView)convertView.findViewById(R.id.search_history_word);
		ImageButton history_cancelBtn_item = (ImageButton) convertView.findViewById(R.id.clear_search_history_item);
		
		history_button_flag.setFocusable(false);
		history_search_word.setText(searchHistoryInfos.get(position).getSearchKey());
		history_cancelBtn_item.setFocusable(false);//��һ��ǳ���Ҫ�������ſ������б����ý���
		
		//Ϊɾ����ť ��Ӽ�����
		history_cancelBtn_item.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {								
				cancelItem(position);
			}			
		});
		
		return convertView;
	}
	
	/**
	 * ɾȥ�б����е���
	 * ͬʱҲɾȥ���ݿ��еı�Ȼ�����½�һ������һ���ı�
	 * �������б�
	 */
	public void canceAllItems(){
		dbManager.drop();
		searchHistoryInfos.clear();
		notifyDataSetChanged();
	}
	
	/**
	 * ɾȥ�б���λ����positionһ��
	 * ͬʱҲɾȥ���ݿ��и���ļ�¼
	 * �������б�
	 * @param position
	 */
	public void cancelItem(int position){
		dbManager.cancel(searchHistoryInfos.get(position).getSearchKey());
		searchHistoryInfos.remove(position);
		notifyDataSetChanged();
	}	
}
