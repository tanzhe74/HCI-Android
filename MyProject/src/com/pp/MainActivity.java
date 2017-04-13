package com.pp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
@SuppressLint("NewApi") public class MainActivity extends Activity implements OnTouchListener, SensorEventListener {
	MySurfaceView mSurfaceView = null;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	@SuppressLint("NewApi") @Override
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_GAME);
		//显示自定义的SurfaceView视图
//		setContentView(new MySurfaceView(this));
		mSurfaceView = new MySurfaceView(this);
		setContentView(mSurfaceView);
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub	
	}
	@SuppressLint("NewApi") @Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		 int x = (int) event.values[0];
		 int y = (int) event.values[1];
		 int z = (int) event.values[2];
		// left and right
		if (Math.abs(z) < 0.1) {
		}
		// yaoyiyaoshixian
		if (Math.abs(x) > 14 && Math.abs(y) > 14 || Math.abs(y) > 14
				&& Math.abs(z) > 14 || Math.abs(x) > 14 && Math.abs(z) > 14) {
			System.out.println("变换速度");
			if (mSurfaceView.isFast) {
				mSurfaceView.Hero_Step = 4;
				mSurfaceView.isFast = false;
			} else {
				// startService(intent);
				mSurfaceView.Hero_Step = 8;
				mSurfaceView.isFast = true;
			}
		}
		if (Math.abs(x) > Math.abs(y)) {
			if (x >= 5) {
				// left
				int herox = mSurfaceView.robot_x;
				int heroy = mSurfaceView.robot_y;
				mSurfaceView.mPosX = herox - 64;
				mSurfaceView.mPosY = heroy;
				mSurfaceView.sx = -64;
				mSurfaceView.sy = 0;
				mSurfaceView.dx = 64;
				mSurfaceView.dy = 0;
				mSurfaceView.logic();
				mSurfaceView.UpdateHero();
			} else if(x<=-5){
				int herox = mSurfaceView.robot_x;
				int heroy = mSurfaceView.robot_y;
				mSurfaceView.mPosX = herox + 64;
				mSurfaceView.mPosY = heroy;
				mSurfaceView.sx = 64;
				mSurfaceView.sy = 0;
				mSurfaceView.dx = 64;
				mSurfaceView.dy = 0;
				mSurfaceView.logic();
				mSurfaceView.UpdateHero();
			}
		}
		// up and down
		else if (Math.abs(y) > Math.abs(x)) {
			if (y >=5) {
				// down
				int herox = mSurfaceView.robot_x;
				int heroy = mSurfaceView.robot_y;
				mSurfaceView.mPosX = herox ;
				mSurfaceView.mPosY = heroy +64;
				mSurfaceView.sx = 0;
				mSurfaceView.sy = 64;
				mSurfaceView.dx = 0;
				mSurfaceView.dy = 64;
				mSurfaceView.logic();
				mSurfaceView.UpdateHero();
			} else if(y<=-5){
				int herox = mSurfaceView.robot_x;
				int heroy = mSurfaceView.robot_y;
				mSurfaceView.mPosX = herox ;
				mSurfaceView.mPosY = heroy -64;
				mSurfaceView.sx = 0;
				mSurfaceView.sy = -64;
				mSurfaceView.dx = 0;
				mSurfaceView.dy = 64;
				mSurfaceView.logic();
				mSurfaceView.UpdateHero();
			}
		}

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}