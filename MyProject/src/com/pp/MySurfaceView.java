package com.pp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

/**
 * 
 * @author Himi
 * 
 */
@SuppressLint("NewApi")
public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	// 备份3.1 地图滚动，单双指功能,重力和加速度传感器,加上轨迹移动
	// 3.2 修改人物和轨迹
	//3.3修改双指
	private SurfaceHolder surfhold;
	private Paint paint;
	private Thread thread;
	private boolean flag;
	private Canvas canvas;
	private int width, height;

	private Bitmap mhero = null;
	private Bitmap map = null;
	// 尝试自己的地图
	private Bitmap you = null;
	private Bitmap zuo = null;
	Boolean isMove = false;
	private int moveMapX = 0;
	private int moveMapY = 0;
	// npc
	private Bitmap npc = null;
	private Bitmap heroac[][] = new Bitmap[4][4];
	private Bitmap mMapAct[][] = new Bitmap[25][25];

	// 英雄的方向常量
	private final int DIRECTION_UP = 1;
	private final int DIRECTION_DOWN = 2;
	private final int DIRECTION_LEFT = 3;
	private final int DIRECTION_RIGHT = 4;

	// 英雄当前的方向(默认朝右方向)
	private int direction = DIRECTION_RIGHT;
	// 动作帧下标
	private int currentFrame = 0;
	// 单点按键位置
	public int mPosX = 320;
	public int mPosY = 480;
	//
	public int backPosX = 320;
	public int backPosY = 480;
	// 触屏点与当前的的差
	public int sx = 0;
	public int sy = 0;
	// 离目标点的距离（绝对值）
	public int dx = 0, dy = 0;

	public final static int TILE_WIDTH = 31;
	public final static int TILE_HEIGHT = 31;

	int mWidthTileCount = 0;
	int mHeightTileCount = 0;

	// 横向纵向tile块的数量
	int mBitMapWidth = 0;
	int mBitMapHeight = 0;

	// 英雄在地图中的坐标
	int mHeroMapX = 320;
	int mHeroMapY = 480;
	// 备份英雄发生碰撞以前的坐标点
	int mBackHeroMapX = 320;
	int mBackHeroMapY = 480;

	// 英雄在行走中的坐标
	int robot_x = 320;
	int robot_y = 480;
	// 备份英雄发生碰撞以前的屏幕显示坐标点
	int mBackRobotX = 320;
	int mBackRobotY = 480;

	// 英雄在地图中绘制坐标
	int mHeroImageX = 0;
	int mHeroImageY = 0;

	// 英雄在地图二位数组中的索引
	int mHeroIndexX = 0;
	int mHeroIndexY = 0;

	// 屏幕左上角在地图中的位置
	int mMapPosX = 0;
	int mMapPosY = 0;

	// 双指
	// long firstclick = 0;
	// int clickcount = 0;
	Boolean doubleclick = false;
	// 尝试路线
	private ArrayList<PointF> pointgra = new ArrayList<PointF>();
	private Path mPath;
	private Boolean path = false;
	Boolean over = false;
	Boolean get = false;
	// int mMapPathX = 0;
	// int mMapPathY = 0;
	private static final float TOUCH_TOLERANCE = 4;
	private Paint lPaint;
	int of = 0;
	int mX = robot_x;
	int mY = robot_y;
	// 场景块的宽高的数量
	public final static int TILE_WIDTH_COUNT = 25;
	public final static int TILE_HEIGHT_COUNT = 25;

	/** 与实体层发生碰撞 **/
	private boolean isAcotrCollision = false;
	/** 与边界层发生碰撞 **/
	private boolean isBorderCollision = false;
	// 控制步速
	boolean isFast = false;
	// 步长
	public int Hero_Step = 4;
	private float beforeLenght, afterLenght, gapLenght, changesize = 1;
	// 处理按键卡现象
	public boolean isUp, isDown, isLeft, isRight;

	public int[][] mCollision = {

			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1,
					-1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					-1, -1, -1, -1, -1 },
			{ -1, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					-1, -1, -1, -1 },
			{ 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, -1, -1, -1 },
			{ 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, -1, -1, 0 },
			{ 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, -1, -1, 0 },
			{ 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, -1, -1, 0 },
			{ 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, -1, -1, 0 },
			{ 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1,
					0, -1, -1, -1, 0 },
			{ 0, 0, 0, 0, -1, -1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1,
					0, -1, -1, -1, 0 },
			{ 0, 0, -1, -1, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1,
					-1, 0, 0, 0, -1 },
			{ -1, -1, -1, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1,
					-1, 0, 0, 0, -1, -1 },
			{ -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1,
					0, 0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0,
					0, 0, 0, -1 },
			{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					-1, -1, -1, -1 },
			{ -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					-1, -1, -1, -1 },
			{ -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, -1, -1, -1 },
			{ -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1,
					-1, -1, -1, -1, -1 } };

	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 },
	// { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0 } };
	/**
	 * SurfaceView初始化函数
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				new AlertDialog.Builder(getContext())
						.setTitle("操作提示")
						.setMessage("单指移动，双指缩放，轨迹移动，重力移动，加速度变速，地图预览！")
						.setIcon(R.drawable.icon)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// AnimView.this.init();

									}
								})
						.setNegativeButton("ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {

									}
								}).show();
				// 获取传递的数据
				// Bundle data = msg.getData();
				// int count = data.getInt("COUNT");
				// 处理UI更新等操作
			}
		};
	};

	public MySurfaceView(Context context) {
		super(context);
		surfhold = this.getHolder();
		surfhold.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		lPaint.setColor(Color.BLACK);
		lPaint.setStyle(Paint.Style.STROKE);// 空心
		lPaint.setStrokeJoin(Paint.Join.ROUND);
		lPaint.setStrokeCap(Paint.Cap.ROUND);
		lPaint.setStrokeWidth(5);

		setFocusable(true);
		createImage();
		mPath = new Path();

	}

	/**
	 * SurfaceView视图创建，响应此函数
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		width = this.getWidth();
		height = this.getHeight();
		flag = true;
		// 实例线程
		thread = new Thread(this);
		// 启动线程
		thread.start();
	}

	/**
	 * 
	 * @param currentFrame
	 *            绘制帧
	 * @param frameW
	 *            每帧的高
	 * @param frameH
	 *            每帧的高
	 * @param canvas
	 *            画布实例
	 * @param paint
	 *            画笔实例
	 */
	public void drawFrame(Canvas canvas, Paint paint) {
		int frameW = 32;
		int frameH = 48;
		canvas.save();
		// 设置一个宽高与机器人每帧相同大小的可视区域
		canvas.clipRect(robot_x - frameW / 2, robot_y - frameH, robot_x
				+ frameW / 2, robot_y);

		short sd = 0;
		if (direction == DIRECTION_UP) {
			sd = 3;
		} else if (direction == DIRECTION_LEFT) {
			sd = 1;
		} else if (direction == DIRECTION_RIGHT) {
			sd = 2;
		} else if (direction == DIRECTION_DOWN) {
			sd = 0;
		}
		// canvas.scale(changesize, changesize, robot_x - frameW / 2, robot_y
		// - frameH / 2);
		canvas.drawBitmap(heroac[sd][currentFrame], robot_x - frameW / 2,
				robot_y - frameH, paint);
		canvas.restore();
	}

	private void DrawMap(Canvas canvas, Paint paint) {
		canvas.save();
		int i, j;
		for (i = 0; i < TILE_HEIGHT_COUNT; i++) {
			for (j = 0; j < TILE_WIDTH_COUNT; j++) {
				canvas.drawBitmap(mMapAct[i][j], mMapPosX + (j * TILE_WIDTH),
						mMapPosY + (i * TILE_HEIGHT), paint);
			}
		}
		if (-320 <= mMapPosX && mMapPosX <= 0) {

			canvas.drawBitmap(npc, mMapPosX + 160, mMapPosY + 320, paint);
		}
		canvas.drawBitmap(you, 288, 240, paint);
		canvas.drawBitmap(zuo, 0, 240, paint);
		canvas.restore();
	}

	private void DrawmoveMap(Canvas canvas, Paint paint) {
		canvas.save();
		int i, j;
		for (i = 0; i < TILE_HEIGHT_COUNT; i++) {
			for (j = 0; j < TILE_WIDTH_COUNT; j++) {
				canvas.drawBitmap(mMapAct[i][j], moveMapX + (j * TILE_WIDTH),
						moveMapY + (i * TILE_HEIGHT), paint);
			}
		}
		canvas.drawBitmap(you, 288, 240, paint);
		canvas.drawBitmap(zuo, 0, 240, paint);
		canvas.restore();
	}

	/**
	 * 根据ID绘制一个tile块
	 * 
	 * @param id
	 * @param canvas
	 * @param paint
	 * @param bitmap
	 */

	/**
	 * 游戏绘图
	 */
	public void myDraw() {
		try {
			canvas = surfhold.lockCanvas();
			if (canvas != null) {
				if (path == false) {
					canvas.drawColor(Color.BLUE);
					canvas.scale(changesize, changesize, robot_x, robot_y);
					if (isMove == true) {
						DrawmoveMap(canvas, paint);
					} else {
						DrawMap(canvas, paint);
						// paint.setColor(Color.BLACK);
						drawFrame(canvas, paint);
					}
				} else if (path == true) {
					canvas.drawPath(mPath, lPaint);
					if (over && pointgra.size() > 0) {
						// move();
						canvas.drawPoint(pointgra.get(of).x,
								pointgra.get(of).y, paint);
						// of += 1;
						if (of < pointgra.size()) {
							if (of == pointgra.size() - 1) {
								mPath.reset();// 移动完成后移除线条
								of = 0;
							}
							invalidate();
						}
					}
				}
				if (isBorderCollision) {
					DrawCollision(canvas, "边界");
				}
				if (isAcotrCollision) {
					DrawCollision(canvas, "碰撞");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				surfhold.unlockCanvasAndPost(canvas);
		}
	}

	private void DrawCollision(Canvas canvas, String string) {
		// TODO Auto-generated method stub

		drawRimString(canvas, string, Color.BLACK, width >> 1, height >> 1);
	}

	public final void drawRimString(Canvas canvas, String str, int color,
			int x, int y) {
		int backColor = paint.getColor();
		paint.setColor(~color);
		canvas.drawText(str, x + 1, y, paint);
		canvas.drawText(str, x, y + 1, paint);
		canvas.drawText(str, x - 1, y, paint);
		canvas.drawText(str, x, y - 1, paint);
		paint.setColor(color);
		canvas.drawText(str, x, y, paint);
		paint.setColor(backColor);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		Log.e("", "aaaaaaa");
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	/**
	 * 触屏事件监听
	 */
	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// if (firstclick != 0 && System.currentTimeMillis() - firstclick > 100)
		// {
		// clickcount = 0;
		// }
		// clickcount++;
		// if (clickcount == 2) {
		// path = false;
		// scaleWithFinger(event);
		// } else if (clickcount == 1) {

		if (event.getPointerCount() == 2) {
			path = false;
			doubleclick = true;
			scaleWithFinger(event);

		} else {
			int action = event.getAction();
			mPosX = (int) event.getX();
			mPosY = (int) event.getY();
			backPosX = mPosX;
			backPosY = mPosY;
			// System.out.println(mPosX / TILE_WIDTH + "    " + mPosY /
			// TILE_HEIGHT);
			if (mPosX / TILE_WIDTH == 9 && mPosY / TILE_HEIGHT == 8
					|| mPosX / TILE_WIDTH == 10 && mPosY / TILE_HEIGHT == 8
					|| mPosX / TILE_WIDTH == 0 && mPosY / TILE_HEIGHT == 8
					|| mPosX / TILE_WIDTH == 1 && mPosY / TILE_HEIGHT == 8) {
				isMove = true;
			} else {
				sy = mPosY - robot_y;
				sx = mPosX - robot_x;
				dx = Math.abs(sx);
				dy = Math.abs(sy);
				isMove = false;
				/** 控制当触摸抬起时清屏 **/
				path = false;

				switch (action) {
				// 触摸按下的事件
				case MotionEvent.ACTION_DOWN: {
					path = false;
					if (sy >= 0) {// xia
						direction = DIRECTION_DOWN;
						isDown = true;
						// moveDirection =DIRECTION_DOWN ;
					}
					if (sy < 0) {// shang
						direction = DIRECTION_UP;
						isUp = true;
						// moveDirection = DIRECTION_UP;
					}
					move();
					pointgra.clear();
					of = 0;
					pointgra.add(new PointF(mPosX, mPosY));
					touch_start(mPosX, mPosY);
					invalidate();

					break;
				}
				// 触摸移动的事件
				case MotionEvent.ACTION_MOVE: {
					path = true;
					over = false;
					pointgra.add(new PointF(mPosX, mPosY));
					touch_move(mPosX, mPosY);
					invalidate();
					break;

				}
				// 触摸抬起的事件
				case MotionEvent.ACTION_UP: {
					over = true;
					doubleclick = false;
					touch_up();
					invalidate();
					break;
				}
				}
			}
		}
		return true;
	}

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = (int) x;
		mY = (int) y;
	}

	private void touch_move(float x, float y) {
		float xx = Math.abs(x - mX);
		float yy = Math.abs(y - mY);
		if (xx >= TOUCH_TOLERANCE || yy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = (int) x;
			mY = (int) y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
	}

	@SuppressLint("NewApi")
	public void scaleWithFinger(MotionEvent event) {
		float moveX = event.getX(1) - event.getX(0);
		float moveY = event.getY(1) - event.getY(0);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			beforeLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			afterLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));
			gapLenght = afterLenght - beforeLenght;
			if (Math.abs(gapLenght) > 5) {
				// 如果当前时间两点距离大于前一时间两点距离，则传0，否则传1
				if (gapLenght > 0) {
					changesize = changesize + 0.05f;
					if (changesize > 1.5)
						changesize = 1.5f;
				} else {
					changesize = changesize - 0.05f;
					if (changesize < 1)
						changesize = 1;
				}
				beforeLenght = afterLenght;
			}
		}
	}

	public void createImage() {
		map = new BitmapDrawable(this.getContext().getResources()
				.openRawResource(R.drawable.map)).getBitmap();// 创建于屏幕大小一致的图片

		for (short i = 0; i < 25; i++) {
			for (short f = 0; f < 25; f++) {
				mMapAct[i][f] = Bitmap.createBitmap(map, f * TILE_WIDTH, i
						* TILE_HEIGHT, 32, 32);
			}
		}
		mhero = new BitmapDrawable(this.getContext().getResources()
				.openRawResource(R.drawable.r01)).getBitmap(); // 获得人物
		// 分解人物动作
		System.out.println("开始切图");
		for (short i = 0; i < 4; i++) {
			for (short f = 0; f < 4; f++) {
				heroac[i][f] = Bitmap.createBitmap(mhero, f * 32, i * 48, 32,
						48);
			}
		}
		you = new BitmapDrawable(this.getContext().getResources()
				.openRawResource(R.drawable.you)).getBitmap(); // 获得人物
		zuo = new BitmapDrawable(this.getContext().getResources()
				.openRawResource(R.drawable.zuo)).getBitmap(); // 获得人物
		npc = new BitmapDrawable(this.getContext().getResources()
				.openRawResource(R.drawable.npc)).getBitmap(); // 获得人物
	}

	/**
	 * 按键事件监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			isUp = true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			isDown = true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			isLeft = true;
			direction = DIRECTION_LEFT;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			isRight = true;
			direction = DIRECTION_RIGHT;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			isUp = false;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			isDown = false;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			isLeft = false;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			isRight = false;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 游戏逻辑
	 */
	public void move() {
		// System.out.println("pos" + mPosX + "    " + mPosY);

		if (isMove == true) {
			if (mPosX / TILE_WIDTH == 9 && mPosY / TILE_HEIGHT == 8
					|| mPosX / TILE_WIDTH == 10 && mPosY / TILE_HEIGHT == 8) {
				moveMapX -= 10;
			}
			if (mPosX / TILE_WIDTH == 0 && mPosY / TILE_HEIGHT == 8
					|| mPosX / TILE_WIDTH == 1 && mPosY / TILE_HEIGHT == 8) {
				moveMapX += 10;
			}
		} else {
			if (backPosX == mPosX && backPosY == mPosY) {
				// System.out.println("womeibiano " + dx + "     " + dy);
			} else {
				sy = mPosY - robot_y;
				sx = mPosX - robot_x;
				dx = Math.abs(sx);
				dy = Math.abs(sy);
				backPosX = mPosX;
				backPosY = mPosY;
				if (sy >= 0) {// xia
					direction = DIRECTION_DOWN;
					isDown = true;
					// moveDirection =DIRECTION_DOWN ;
				}
				if (sy < 0) {// shang
					direction = DIRECTION_UP;
					isUp = true;
					// moveDirection = DIRECTION_UP;
				}
			}
			if (doubleclick == true) {
				dx = 0;
				dy = 0;
				sy = 0;
				sx = 0;
			}
		}

	}

	public void logic() {

		// System.out.println("d" + dy + "    " + dx);
		if ((dy / Hero_Step) == 0 || (dx / Hero_Step) == 0)
			changeDirection();
		// System.out.println(mHeroIndexX + "    " + mHeroIndexY);
		int mx = 160 - (mHeroMapX - robot_x);
		int my = 320 - (mHeroMapY - robot_y);
		int indexx = mx / TILE_WIDTH;
		int indexy = my / TILE_WIDTH;
		// System.out.println(mPosX + "    " + mPosY + "   " + mx + "   " + my);

		if (isDown == true || isLeft == true || isRight == true || isUp == true) {
			/** 根据按键更新显示动画 **/
			/** 在碰撞数组中寻找看自己是否与地图物理层发生碰撞 **/
			if (isDown) {
				// mAnimationState = ANIM_DOWN;
				mHeroMapY += Hero_Step;
				dy -= Hero_Step;
				if (robot_y >= 384) {
					if (mHeroIndexY >= 12 && mHeroIndexY <= 19) {
						mMapPosY -= Hero_Step;
					} else {
						robot_y += Hero_Step;
					}
				} else {
					robot_y += Hero_Step;
				}
			} else if (isLeft == true) {
				mHeroMapX -= Hero_Step;
				dx -= Hero_Step;
				if (robot_x <= 96) {
					if (mHeroIndexX >= 3 && mHeroIndexX <= 17) {
						mMapPosX += Hero_Step;
					} else {
						robot_x -= Hero_Step;
					}
				} else {
					robot_x -= Hero_Step;
				}
			} else if (isRight == true) {
				// mAnimationState = ANIM_RIGHT;
				mHeroMapX += Hero_Step;
				dx -= Hero_Step;
				if (robot_x >= 224) {
					if (mHeroIndexX >= 7 && mHeroIndexX <= 21) {
						mMapPosX -= Hero_Step;
					} else {
						robot_x += Hero_Step;
					}
				} else {
					robot_x += Hero_Step;
				}
			} else if (isUp == true) {
				// mAnimationState = ANIM_UP;
				mHeroMapY -= Hero_Step;
				dy -= Hero_Step;
				if (robot_y <= 160) {
					if (mHeroIndexY >= 5 && mHeroIndexY <= 15) {

						mMapPosY += Hero_Step;
					} else {
						robot_y -= Hero_Step;
					}
				} else {
					robot_y -= Hero_Step;
				}
			}
			moveMapX = mMapPosX;
			moveMapY = mMapPosY;
			/** 算出英雄移动后在地图二位数组中的索引 **/
			mHeroIndexX = mHeroMapX / TILE_WIDTH;
			mHeroIndexY = mHeroMapY / TILE_HEIGHT;
		}
		if (isRight == true || isUp == true || isDown == true || isLeft == true)
			currentFrame++;
		if (currentFrame >= 4) {
			currentFrame = 0;
		}
	}

	private void changeDirection() {
		// TODO Auto-generated method stub
		if ((dy / Hero_Step) == 0 && (dx / Hero_Step) != 0) {
			if (sx >= 0) {
				isUp = false;
				isDown = false;
				isRight = true;
				direction = DIRECTION_RIGHT;
			} else if (sx < 0) {
				isUp = false;
				isDown = false;
				isLeft = true;
				direction = DIRECTION_LEFT;
			}

		} else if ((dy / Hero_Step) != 0 && (dx / Hero_Step) == 0) {
			if (sy >= 0) {
				isDown = true;
				isLeft = false;
				isRight = false;
				direction = DIRECTION_DOWN;
			} else if (sy < 0) {
				isUp = true;
				isLeft = false;
				isRight = false;
				direction = DIRECTION_UP;
			}
		} else if ((dy / Hero_Step) == 0 && (dx / Hero_Step) == 0) {
			isLeft = false;
			isRight = false;
			isUp = false;
			isDown = false;
			get = true;
			backPosX = mPosX;
			backPosY = mPosY;
		}

	}

	public void UpdateHero() {
		// TODO
		// /** 检测人物是否出屏 **/

		isBorderCollision = false;
		if (mHeroMapX <= 32) {
			mHeroMapX = 34;
			robot_x = 34;
			isBorderCollision = true;
			isLeft = false;
			isRight = false;
			isUp = false;
			isDown = false;
		} else if (mHeroMapX >= TILE_WIDTH_COUNT * TILE_WIDTH) {
			mHeroMapX = TILE_WIDTH_COUNT * TILE_WIDTH - 32;
			robot_x = width - 32;
			isBorderCollision = true;
			isLeft = false;
			isRight = false;
			isUp = false;
			isDown = false;
		}
		if (mHeroMapY <= 0) {
			mHeroMapY = 32;
			robot_y = 32;
			isBorderCollision = true;
			isLeft = false;
			isRight = false;
			isUp = false;
			isDown = false;
		} else if (mHeroMapY >= TILE_HEIGHT_COUNT * TILE_HEIGHT) {
			mHeroMapY = TILE_HEIGHT_COUNT * TILE_HEIGHT - 32;
			robot_y = height - 32;
			isBorderCollision = true;
			isLeft = false;
			isRight = false;
			isUp = false;
			isDown = false;
		}
		// 防止地图越界
		if (mMapPosX >= 0) {
			mMapPosX = 0;
		} else if (mMapPosX <= -(800 - 352)) {// /352
			mMapPosX = -(800 - 352);
		}
		if (mMapPosY >= 0) {
			mMapPosY = 0;
		} else if (mMapPosY <= -(800 - 598)) {// 598
			mMapPosY = -(800 - 598);
		}
		// moveMap防止越界
		if (moveMapX >= 0) {
			moveMapX = 0;
		} else if (moveMapX <= -(800 - 352)) {
			moveMapX = -(800 - 352);
		}
		if (moveMapY >= 0) {
			moveMapY = 0;
		} else if (moveMapY <= -(800 - 598)) {
			moveMapY = -(800 - 598);
		}
		/** 越界检测 **/
		int cowidth = mCollision[0].length - 1;
		int coheight = mCollision.length - 1;

		if (mHeroIndexX <= 0) {
			mHeroIndexX = 0;
		} else if (mHeroIndexX >= cowidth) {
			mHeroIndexX = cowidth;
		}
		if (mHeroIndexY <= 0) {
			mHeroIndexY = 0;
		} else if (mHeroIndexY >= coheight) {
			mHeroIndexY = coheight;
		}
		// 碰撞检测
		if (mCollision[mHeroIndexY][mHeroIndexX] == -1) {
			mHeroMapX = mBackHeroMapX;
			mHeroMapY = mBackHeroMapY;
			robot_y = mBackRobotY;
			robot_x = mBackRobotX;
			isAcotrCollision = true;
			isLeft = false;
			isRight = false;
			isUp = false;
			isDown = false;
		} else {
			mBackHeroMapX = mHeroMapX;
			mBackHeroMapY = mHeroMapY;
			mBackRobotX = robot_x;
			mBackRobotY = robot_y;
			isAcotrCollision = false;
		}
		mHeroImageX = robot_x;
		mHeroImageY = robot_y;
	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			// logic();
			// UpdateHero();
			if (of < pointgra.size()) {

				mPosX = (int) pointgra.get(of).x;
				mPosY = (int) pointgra.get(of).y;
				System.out.println(mPosX + "   " + mPosY);
				if (of == 0) {
					backPosX = mPosX;
					backPosY = mPosY;
				}
				// if (over == true && get == true) {
				of += 1;
				// get = false;
				// }
			}
			move();
			logic();
			UpdateHero();
			myDraw();

			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * SurfaceView视图状态发生改变，响应此函数
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * SurfaceView视图消亡时，响应此函数
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}

}
