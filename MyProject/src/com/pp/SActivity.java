package com.pp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SActivity extends Activity {
	Context mContext = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = this;

		Button botton0 = (Button) findViewById(R.id.start);
		botton0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
			}
		});
		Button botton1 = (Button) findViewById(R.id.button1);
		botton1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Handler mHandler = new Handler() {
					public void handleMessage(Message msg) {
						new AlertDialog.Builder(mContext)
								.setTitle("������ʾ")
								.setMessage("�����Ļ�����ƶ�����λ�á�  ˫ָ��������Ŷ��       ����һ���켣���������ƶ���ҡ���ֻ������ƶ����ζ��ֻ�������٣���ͼԤ����")
								.setIcon(R.drawable.icon)
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												// AnimView.this.init();

											}
										})
								.setNegativeButton("ok",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {

											}
										}).show();

					}
				};
				Message msg = new Message();
				mHandler.sendMessage(msg);
			};

		});

	}
}
