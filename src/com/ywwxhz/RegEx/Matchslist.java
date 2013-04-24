package com.ywwxhz.RegEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ywwxhz.regexmatcher.R;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.SimpleAdapter;
import android.app.ListActivity;

public class Matchslist extends ListActivity {

	private int to[] = new int[] { R.id.id, R.id.start, R.id.end, R.id.value };
	private String from[] = new String[] { "id", "start", "end", "value" };
	private Matcher matcher;
	HashMap<String, Object> match;
	List<HashMap<String, Object>> maches;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matchslist);
		Bundle bundle = getIntent().getExtras();
		String regx = bundle.getString("regx");
		String text = bundle.getString("text");
		matcher = Pattern.compile(regx).matcher(text);
		maches = new ArrayList<HashMap<String, Object>>();
		for (int i = 1; matcher.find(); i++) {
			match = new HashMap<String, Object>();
			match.put("id", i);
			match.put("start", matcher.start());
			match.put("end", matcher.end());
			match.put("value", matcher.group());
			maches.add(match);
		}
		setTitle(String.format(getString(R.string.all_title), maches.size()));
		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), maches,
				R.layout.mach_item, from, to);
		getListView().setAdapter(adapter);
		System.gc();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.gc();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
