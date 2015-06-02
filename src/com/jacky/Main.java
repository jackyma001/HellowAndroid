package com.jacky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


public class Main extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btnStudy = (Button) findViewById(R.id.btnStudy);
        Button btnPlan = (Button) findViewById(R.id.btnPlan);
        Button btnNewWord = (Button) findViewById(R.id.btnNewWrod);
        Button btnWordList = (Button) findViewById(R.id.btnWordList);
        
        btnWordList.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent(Main.this,com.jacky.WordList.class);
        		startActivity(intent);
        	}
        });
        
        btnStudy.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent(Main.this, com.jacky.Dic.class); 		
        		startActivity(intent); 
        	}
        });
        
        btnPlan.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent(Main.this,com.jacky.Plan.class);
        		startActivity(intent);
        	}
        });
        
        btnNewWord.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
      
        	}
        });        
	}
	
}
