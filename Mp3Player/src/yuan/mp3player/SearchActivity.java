package yuan.mp3player;

import java.util.Iterator;
import java.util.List;

import yuan.constant.AppConstant;
import yuan.constant.MusicPlayer;
import yuan.factory.CommonSearchFactory;
import yuan.factory.OnlineFactory;
import yuan.model.CopyMp3Infos;
import yuan.model.Mp3Info;
import yuan.mp3player.service.PlayerService;
import yuan.utils.Network;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends ListActivity implements OnScrollListener {

	private final static int SEARCH_CODE = 1; // ��һ������ʱ��ʶ����
	private final static int SEARCH_MORE_CODE = 2; // �ڶ��λ�ڶ����Ժ�������ʶ����

	private List<Mp3Info> mp3Infos = null; // ��ŵ�һ���Ժ���������
	private List<Mp3Info> more_mp3Infos = null; // ��ŵڶ��λ�ڶ����Ժ���������
	private ListView searchListVive = null;
	private SearchListAdapter searchListAdapter = null;
	private SearchBroadcastReceiver searchReceiver = null;
	private View selectView = null;
	private EditText editView = null;
	private TextView mp3NumbersTextView = null;
	private TextView search_more_text = null;
	private ImageButton searchButton = null;
	private ImageButton searchMoreButton = null;
	private View searchMoreView = null;
	private View searchResultStateView = null;

	private int visibleLastIndex = 0; // ���Ŀ���������
	private int visibleItemCount; // ��ǰ���ڿɼ�������

	private int page_no = 1;// ��ѯMP3���صĵ�һҳxml�ļ�
	private int page_size = 30;// һ��xml�ļ�����30�׸���
	private String keyWord = null;// �����ؼ���

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		selectView = getLayoutInflater().inflate(
				R.layout.search_result_item_operate, null);
		// ������
		editView = (EditText) findViewById(R.id.search_text);
		editView.setOnClickListener(new SearchEditListener());
		editView.setFocusable(false);

		// ������ť
		searchButton = (ImageButton) findViewById(R.id.search_button);
		searchButton.setOnClickListener(new SearchButtonListener());

		// Listview�ĵײ�����������ͼ
		searchMoreView = getLayoutInflater().inflate(R.layout.search_more_item,
				null);
		searchMoreView.setClickable(true);

		// Listview��ͷ����ͼ��������ʾ�����������
		searchResultStateView = getLayoutInflater().inflate(
				R.layout.search_result_info, null);

		// ��listviewͷ����ͼ��ʾ�����������
		mp3NumbersTextView = (TextView) searchResultStateView
				.findViewById(R.id.search_result_total);
		mp3NumbersTextView.setVisibility(View.INVISIBLE);

		// listview�ĵײ���ͼ�İ�ť�Ͱ�ť���������
		searchMoreButton = (ImageButton) searchMoreView
				.findViewById(R.id.search_more_button);
		searchMoreButton.setOnClickListener(new SearchMoreListener());
		search_more_text = (TextView) searchMoreView
				.findViewById(R.id.search_more_text);

		// Ϊlistview���ͷ���͵ײ���ͼ
		searchListVive = getListView();
		searchListVive.addHeaderView(searchResultStateView, null, false);
		searchListVive.setOnScrollListener(this);

		// ע���һ�������㲥������
		searchReceiver = new SearchBroadcastReceiver();
		registerReceiver(searchReceiver, searchReceiver.getIntentFilter());
	}

	/**
	 * ���EditView��ת��SearchActivity
	 */
	private class SearchEditListener implements OnClickListener {
		public void onClick(View v) {
			if (Network.isAccessNetwork(SearchActivity.this)) {
				Intent intent = new Intent();
				intent.setClass(SearchActivity.this,
						SearchHistoryActivity.class);
				SearchActivity.this.startActivity(intent);

			} else {
				Toast.makeText(SearchActivity.this, "��ǰ��û����",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * ������ť�������������������������Ĺؼ���
	 * 
	 * @author Administrator
	 */
	private class SearchButtonListener implements OnClickListener {
		public void onClick(View v) {

		}
	}

	/**
	 * ��SearchActivity�з��������㲥 ���е�һ�������Ĺ㲥������
	 */
	private class SearchBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getStringExtra("keyWord") != null) {
				// ��������
				page_no = 1;
				keyWord = intent.getStringExtra("keyWord");
				// ��ʼ����
				Toast.makeText(SearchActivity.this, "��������...",
						Toast.LENGTH_SHORT).show();
				new SearchAsyncTask(SEARCH_CODE).execute("��һ��");
			}
		}

		/** �㲥�������������ض��Ĺ㲥 */
		public IntentFilter getIntentFilter() {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(AppConstant.SEARCH_KEY_WORD_ACTION);
			return intentFilter;
		}
	}

	/**
	 * ��ʼ��һ���������֣������������Ľ�����б���ʽ��ʾ����
	 * 
	 * @throws Exception
	 */
	private void search(String page_no, String page_size, String keyWord) {
		OnlineFactory factory = new CommonSearchFactory(page_no, page_size,
				keyWord);
		mp3Infos = factory.execute();
	}

	private void updateList() {
		if (mp3Infos != null && !mp3Infos.isEmpty()) {
			initAdapter(mp3Infos); // ��ʼ��SearchListAdapter,��Mp3Info�е����������List��

			// ��ʾ�������
			mp3NumbersTextView.setVisibility(View.VISIBLE);
			mp3NumbersTextView.setText("�ҵ���ؽ�� " + mp3Infos.get(0).getMp3Sum()
					+ " ƪ");

			// �ж��Ƿ��и���������������listview�ײ���ͼ�Ƿ���ʾ
			if (searchListAdapter.getCount() < Integer.parseInt(mp3Infos.get(0)
					.getMp3Sum())) {
				searchListVive.removeFooterView(searchMoreView);
				searchListVive.addFooterView(searchMoreView);
			} else {
				searchListVive.removeFooterView(searchMoreView);
			}
		} else {
			Toast.makeText(this, "û���ҵ���ص�����", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * ��һ�������󣬳�ʼ��SearchListAdapter,��Mp3Info�е����������List��
	 * 
	 * @param mp3Infos
	 */
	private void initAdapter(List<Mp3Info> mp3Infos) {
		searchListAdapter = new SearchListAdapter(this, mp3Infos, selectView);
		this.setListAdapter(searchListAdapter);
	}

	/**
	 * �ڶ������ϵ�����
	 */
	private void searchMore() {
		OnlineFactory factory = new CommonSearchFactory(
				Integer.toString(++page_no), Integer.toString(page_size),
				keyWord);
		more_mp3Infos = factory.execute();
	}

	/**
	 * �ڶ����������������� ������
	 */
	public class SearchMoreListener implements OnClickListener {

		public void onClick(View v) {
			search_more_text.setText("������,���Ժ�...");
			new SearchAsyncTask(SEARCH_MORE_CODE).execute("��������");
		}
	}

	private void updateMoreList() {
		// ��searchlistAdapter����������
		for (Iterator<Mp3Info> iterator = more_mp3Infos.iterator(); iterator
				.hasNext();) {
			Mp3Info mp3Info = iterator.next();
			searchListAdapter.addItem(mp3Info);
		}
		search_more_text.setText("����");
		searchListAdapter.notifyDataSetChanged(); // ���ݼ��仯��,֪ͨearchlistAdapterˢ���б���
		searchListVive.setSelection(visibleLastIndex - visibleItemCount + 1); // ����ѡ����

		mp3Infos.addAll(more_mp3Infos);
		CopyMp3Infos.setMP3INFOS(mp3Infos);

		if (searchListAdapter.getCount() == Integer.parseInt(mp3Infos.get(0)
				.getMp3Sum())) {
			searchListVive.removeFooterView(searchMoreView);
		}
	}

	/**
	 * �б�״̬�ı�ʱ����
	 */
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = searchListAdapter.getCount() - 1; // ���ݼ����һ�������
		int lastIndex = itemsLastIndex + 1; // ���ϵײ���searchMoreView��
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// ������Զ�����,��������������첽�������ݵĴ���
			System.out.println("------------>�Զ�����");
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN); 
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// ע��  
	        intent.addCategory(Intent.CATEGORY_HOME); 
	        startActivity(intent); 
	        return true; 
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * �����б�ʱ����
	 */
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	/**
	 * ����б������ز���
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// �����û�������б��е�λ�����õ�mp3Info����
		CopyMp3Infos.setMP3INFOS(mp3Infos);// Ϊ���ڲ�ͬActivity֮�䴫���������ݶ���
		MusicPlayer.isFirstPlaying = true;
		Intent intent = new Intent();
		intent.setClass(this, PlayerService.class);
		intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
		intent.putExtra("mp3Info", mp3Infos.get(position - 1));
		intent.putExtra("position", position - 1);
		this.startService(intent);
	}

	private class SearchAsyncTask extends AsyncTask<String, String, String> {
		int code = 0;

		public SearchAsyncTask(int code) {
			super();
			this.code = code;
		}

		@Override
		protected String doInBackground(String... params) {
			if (code == SEARCH_CODE) {
				// ��һ������
				search(Integer.toString(page_no), Integer.toString(page_size),
						keyWord);
			} else if (code == SEARCH_MORE_CODE) {
				// �ڶ�����������
				searchMore();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (code == SEARCH_CODE) {
				// ��һ�θ����б�
				updateList();
			} else if (code == SEARCH_MORE_CODE) {
				// �ڶ������ϸ����б�
				updateMoreList();
			}
		}
	}
}
