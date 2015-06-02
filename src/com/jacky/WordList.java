package com.jacky;
import android.app.ListActivity;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WordList  extends ListActivity {
	private SQLiteDatabase mSQLiteDatabase = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String[] wordLists = new String[this.getListNumber()];
        for (int i = 0; i < wordLists.length; i++)
        {
        	wordLists[i]= "List"+ i;
        }
        setListAdapter(new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, wordLists));   
        
        ListView lv = getListView();  
        lv.setTextFilterEnabled(true);  
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent,
					android.view.View view, int posiation, long id) {
				Intent intent = new Intent(WordList.this, com.jacky.Dic.class);
				Bundle bundle = new Bundle(); 
        		bundle.putInt("LIST_ID", posiation+1);
        		bundle.putString("ALERT_MESSAGE", "1");
        		bundle.putInt("WORD_PER_DAY", 50);
        		intent.putExtras(bundle);
        		startActivity(intent);
			}
        });
        
	}

	private int getListNumber()
	{
		mSQLiteDatabase = new DatabaseManager(this).openDatabase();
		long totalWords= DatabaseUtils.queryNumEntries(mSQLiteDatabase, "dic");       
        int wordPerDays = 50;
        int listNums =  (int) Math.ceil((double)totalWords/wordPerDays);
        return listNums;
	}
}
