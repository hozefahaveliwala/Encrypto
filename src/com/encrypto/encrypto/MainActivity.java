package com.encrypto.encrypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends ActionBarActivity {
	Button encrypt, decrypt, key;
	VideoView video;
	DBHelper DBHelper = new DBHelper(this);
	ViewPager viewPager;
	PagerAdapter adapter;
	PageIndicator mIndicator;

	int[] flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		copyDB();

		flag = new int[] { R.drawable.en, R.drawable.de, };

		// Locate the ViewPager in viewpager_main.xml
		viewPager = (ViewPager) findViewById(R.id.pager);
		// // Pass results to ViewPagerAdapter Class\
		adapter = new ViewPagerAdapter(MainActivity.this, flag);
		// // Binds the Adapter to the ViewPager
		viewPager.setAdapter(adapter);

		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator = indicator;
		indicator.setViewPager(viewPager);

		final float density = getResources().getDisplayMetrics().density;
		// indicator.setBackgroundColor(0xFFCCCCCC);
		indicator.setRadius(5 * density);
		indicator.setPageColor(0xFF888888);
		indicator.setFillColor(0x880000FF);
		indicator.setStrokeColor(0xFF000000);
		indicator.setStrokeWidth(2 * density);

		encrypt = (Button) findViewById(R.id.btn_encrypt);
		decrypt = (Button) findViewById(R.id.btn_decrypt);
		key = (Button) findViewById(R.id.btn_key);

		encrypt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (checkKey() == true) {
					Intent i = new Intent(MainActivity.this,
							File_Operation.class);
					i.putExtra("optype", "E");
					startActivity(i);
				} else {
					Toast.makeText(MainActivity.this, "Key Not set!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		decrypt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (checkKey() == true) {
					Intent i = new Intent(MainActivity.this,
							File_Operation.class);
					i.putExtra("optype", "D");
					startActivity(i);
				} else {
					Toast.makeText(MainActivity.this, "Key Not set!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		key.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (showNote() == true)
					showDialogNote();
				else
					showDialogKey();
			}

		});

	}

	private void showDialogNote() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.note);
		dialog.setTitle("Note");
		dialog.setCancelable(true);
		TextView text = (TextView) dialog.findViewById(R.id.txt_note);
		final CheckBox chckshow = (CheckBox) dialog.findViewById(R.id.chckshow);
		text.setText("If you are setting the key for the first time press OK & continue.\nChanging the key will unable you to decrypt the previously encrypted files.\nPlease decrypt those files and then change the key. ");

		Button bok = (Button) dialog.findViewById(R.id.noteok);
		bok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (chckshow.isChecked() == true) {
					setShowNote(0);
				}
				showDialogKey();
				dialog.dismiss();
			}
		});

		Button bcancel = (Button) dialog.findViewById(R.id.notecancel);
		bcancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void showDialogKey() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.key);
		dialog.setTitle("Encryption/Decryption Key");
		dialog.setCancelable(true);

		final EditText editkey = (EditText) dialog.findViewById(R.id.edit_key);

		CheckBox chckkey = (CheckBox) dialog.findViewById(R.id.chck_key);
		chckkey.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked == false) {
					editkey.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				} else {
					editkey.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				}
			}
		});

		Button bok = (Button) dialog.findViewById(R.id.keyok);
		bok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String tempkey = editkey.getText().toString();
				if (tempkey.equalsIgnoreCase("") == false) {
					tempkey = sha1Hash(tempkey);
					Log.d("hash", tempkey);
					tempkey = tempkey.substring(0, 16);
					setKey(tempkey);
					Toast.makeText(MainActivity.this, "Key Changed!",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				} else {
					Toast.makeText(MainActivity.this, "Invalid Input!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		dialog.show();

		Button bcancel = (Button) dialog.findViewById(R.id.keycancel);
		bcancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void copyDB() {
		File makedir = new File("/data/data/" + this.getPackageName()
				+ "/databases/");

		File dbfound = new File("/data/data/" + this.getPackageName()
				+ "/databases/Encrypto.db");
		try {
			if (makedir.exists() == false)
				makedir.mkdir();
			if (dbfound.exists() == false) {
				AssetManager assetManager = this.getAssets();
				InputStream in = null;
				OutputStream out = null;

				in = assetManager.open("Encrypto.db");
				out = new FileOutputStream(dbfound);
				byte[] buffer = new byte[1024];
				int read;
				while ((read = in.read(buffer)) != -1) {
					out.write(buffer, 0, read);
				}
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			}
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}
	}

	private boolean checkKey() {
		String key;
		DBHelper.myDBopen();
		key = DBHelper.getKey();
		DBHelper.myDBclose();
		if (key == "" || key == null) {
			return false;
		} else {
			return true;
		}
	}

	private void setKey(String key) {
		DBHelper.myDBopen();
		DBHelper.setKey(key);
		DBHelper.myDBclose();
	}

	private boolean showNote() {
		DBHelper.myDBopen();
		if (DBHelper.getShowNote() == 1) {
			DBHelper.myDBclose();
			return true;
		} else {
			DBHelper.myDBclose();
			return false;
		}
	}

	private void setShowNote(int n) {
		DBHelper.myDBopen();
		DBHelper.setShowNote(n);
		DBHelper.myDBclose();
	}

	private String sha1Hash(String toHash) {
		String hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = toHash.getBytes("UTF-8");
			digest.update(bytes, 0, bytes.length);
			bytes = digest.digest();

			hash = bytesToHex(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hash;
	}

	private String bytesToHex(byte[] bytes) {
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
