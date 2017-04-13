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
								.setTitle("操作提示")
								.setMessage("点击屏幕人物移动到该位置。  双指可以缩放哦。       滑动一条轨迹人物随着移动，摇摆手机人物移动，晃动手机人物变速，地图预览！")
								.setIcon(R.drawable.icon)
								.setPositiveButton("确定",
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
