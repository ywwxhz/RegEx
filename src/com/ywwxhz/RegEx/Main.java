package com.ywwxhz.RegEx;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.ywwxhz.regexmatcher.R;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

public class Main extends Activity implements OnClickListener {
	private EditText Regx_express;
	private EditText text_source;
	private TextView Matchs;
	private Button Next;
	private Matcher matcher;
	InputMethodManager imm;
	private int count;
	private boolean isdisable = true;

	private boolean findNext() {
		if (this.matcher.find()) {
			this.text_source.requestFocus();
			this.text_source.setSelection(this.matcher.start(),
					this.matcher.end());
			return true;
		} else {
			return false;
		}
	}

	private void doMatch(Boolean showdetail) {
		try {
			String regx, text;
			regx = Regx_express.getText().toString();
			text = text_source.getText().toString();
			if (!text.isEmpty() || !regx.isEmpty()) {
				matcher = Pattern.compile(regx).matcher(text);
				if (!isdisable && showdetail) {
					if (count > 0) {
						imm.hideSoftInputFromWindow(
								Regx_express.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(
								text_source.getWindowToken(), 0);
						Intent intent = new Intent();
						intent.setClass(this, Matchslist.class);
						intent.putExtra("regx", regx);
						intent.putExtra("text", text);
						startActivity(intent);
					} else {
						Toast.makeText(this, R.string.No_Match,
								Toast.LENGTH_LONG).show();
						Next.setEnabled(false);
					}
					System.gc();
				} else {
					int i = 0;
					while (matcher.find()) {
						i++;
					}
					count = i;
					if (count == 0) {
						Matchs.setText(getString(R.string.No_Match));
						Next.setEnabled(false);
					} else {
						Matchs.setText(String.format(
								getString(R.string.Matchs), count));
						Next.setEnabled(true);
					}
					isdisable = false;
				}
			} else {
				Matchs.setText("");
				if (showdetail) {
					Toast.makeText(this, R.string.empty, Toast.LENGTH_LONG)
							.show();
				}
				Next.setEnabled(false);
				System.gc();
			}
		} catch (PatternSyntaxException localPatternSyntaxException) {
			String title = getString(R.string.Error);
			this.Matchs.setText(title);
			if (showdetail) {
				alert(title, localPatternSyntaxException.getLocalizedMessage());
			}
			isdisable = true;
			matcher = null;
			System.gc();
		}
	}

	private void alert(String title, String message) {
		imm.hideSoftInputFromWindow(Regx_express.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(text_source.getWindowToken(), 0);
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(R.string.OK, null).create().show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.Regx_express = (EditText) findViewById(R.id.Regx_express);
		Regx_express.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				doMatch(false);
			}
		});
		this.Regx_express.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		this.text_source = (EditText) findViewById(R.id.Text_source);
		this.text_source.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				doMatch(false);
			}
		});
		this.text_source.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		this.Matchs = (TextView) findViewById(R.id.Matchs);
		this.Next = (Button) findViewById(R.id.Next_match);
		this.Next.setOnClickListener(this);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			System.exit(0);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if (!findNext()) {
			this.matcher.reset();
			findNext();
		}
		System.gc();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			alert(getString(R.string.about), getString(R.string.about_message));
			break;
		case R.id.reference:
			Intent intent = new Intent();
			intent.setClass(this, Reference.class);
			startActivity(intent);
			break;
		case R.id.exit:
			this.finish();
			System.exit(0);
			break;
		case R.id.allMach:
			doMatch(true);
			break;
		}
		return false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Nothing need to be done here

		} else {
			// Nothing need to be done here
		}
	}

}
