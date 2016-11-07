package com.encrypto.encrypto;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.coderplus.filepicker.FilePickerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.coderplus.filepicker.FilePickerActivity;

public class File_Operation extends ActionBarActivity {
	static final int REQUEST_PICK_FILE = 777;
	Button b_file1, b_file2, start;
	EditText source, dest;
	CheckBox deletebox;
	private File File1, File2;
	private String key;
	private String s = "", d = "";
	DBHelper DBHelper = new DBHelper(this);
	private String operation_type = "E";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_operation);

		operation_type = getIntent().getStringExtra("optype");

		b_file1 = (Button) findViewById(R.id.btn_1);
		b_file2 = (Button) findViewById(R.id.btn_2);
		start = (Button) findViewById(R.id.btn_start);
		source = (EditText) findViewById(R.id.edit_source);
		dest = (EditText) findViewById(R.id.edit_dest);
		deletebox = (CheckBox) findViewById(R.id.chkdelete);

		b_file1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(File_Operation.this,
						FilePickerActivity.class);
				intent.putExtra(FilePickerActivity.EXTRA_SELECT_FILES_ONLY,
						true);
				intent.putExtra(FilePickerActivity.EXTRA_FILE_PATH, Environment
						.getExternalStorageDirectory().getAbsolutePath());
				startActivityForResult(intent, REQUEST_PICK_FILE);

			}

		});

		b_file2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(File_Operation.this,
						FilePickerActivity.class);
				intent.putExtra(
						FilePickerActivity.EXTRA_SELECT_DIRECTORIES_ONLY, true);
				intent.putExtra(FilePickerActivity.EXTRA_FILE_PATH, Environment
						.getExternalStorageDirectory().getAbsolutePath());
				startActivityForResult(intent, REQUEST_PICK_FILE);
			}

		});

		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if ((s == "") || (d == "")) {
					source.setText("");
					dest.setText("");
					deletebox.setChecked(false);
					Toast.makeText(File_Operation.this,
							"Please choose the paths correctly!",
							Toast.LENGTH_SHORT).show();
				} else {

					key = getKey();
					Log.d("s", s);
					int endIndex = s.lastIndexOf("/");
					if (endIndex != -1) {
						d = d + s.substring(endIndex, s.length());
						Log.d("d", d);
					}

					File1 = new File(s);
					File2 = new File(d);

					crypt(operation_type);
					if (deletebox.isChecked() == true) {
						File1.delete();
					}
					source.setText("");
					dest.setText("");
					deletebox.setChecked(false);
				}
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_PICK_FILE:
				if (data.hasExtra(FilePickerActivity.EXTRA_FILE_PATH)) {
					// Get the file path
					@SuppressWarnings("unchecked")
					List<File> files = (List<File>) data
							.getSerializableExtra(FilePickerActivity.EXTRA_FILE_PATH);
					// Print the File/Folder names
					for (File file : files) {
						if (file.isDirectory()) {
							d = file.getAbsolutePath().toString();
							dest.setText(d);
						} else {
							s = file.getAbsolutePath().toString();
							source.setText(s);
						}
					}
				}
			}
		}
	}

	private String getKey() {
		String tkey;
		DBHelper.myDBopen();
		tkey = DBHelper.getKey();
		DBHelper.myDBclose();
		return tkey;
	}

	private void crypt(String type) {
		if (type.equalsIgnoreCase("E")) {
			try {
				Encrypt.encrypt(key, File1, File2);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 1",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 2",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 3",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 4",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 5",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 6",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			Toast.makeText(File_Operation.this, "Encryption Successful!",
					Toast.LENGTH_SHORT).show();

		} else if (type.equalsIgnoreCase("D")) {
			try {
				Encrypt.decrypt(key, File1, File2);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 1",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 2",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 3",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 4",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 5",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error 6",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			Toast.makeText(File_Operation.this, "Decryption Successful!",
					Toast.LENGTH_SHORT).show();
		}
	}
}
