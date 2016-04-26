package com.pji.de.awareway.surfaceview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LigneMetroSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder mHolder = null;
	// Le Thread dans lequel se fera le dessin
	private DrawingThread mThread;

	public LigneMetroSurfaceView(Context context) {
		super(context);
		this.init();
	}

	public LigneMetroSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	public LigneMetroSurfaceView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.init();
	}

	public void init() {
		mHolder = getHolder();
		mHolder.addCallback(this);

		mThread = new DrawingThread();
	}

	@Override
	protected void onDraw(Canvas pCanvas) {
		Bitmap b = Bitmap.createBitmap(128, 128, Config.ARGB_8888);
		Paint p = new Paint();

		// Dessiner ses contours
		p.setStyle(Paint.Style.FILL_AND_STROKE);
		p.setColor(Color.parseColor("#000000"));
		pCanvas.drawLine(0, 0, 0, 128, p);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mThread.keepDrawing = true;
		mThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mThread.keepDrawing = false;

		boolean joined = false;
		while (!joined) {
			try {
				mThread.join();
				joined = true;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("WrongCall")
	private class DrawingThread extends Thread {
		// Utilise pour arreter le dessin quand il le faut
		boolean keepDrawing = true;

		@Override
		public void run() {

			while (keepDrawing) {
				Canvas canvas = null;

				try {
					// On recupere le canvas pour dessiner dessus
					canvas = mHolder.lockCanvas();
					// On s'assure qu'aucun autre thread n'accede au holder
					synchronized (mHolder) {
						// Et on dessine
						LigneMetroSurfaceView.this.onDraw(canvas);
					}
				} finally {
					// Notre dessin fini, on relache le Canvas pour que le
					// dessin s'affiche
					if (canvas != null)
						mHolder.unlockCanvasAndPost(canvas);
				}

				// Pour dessiner a 50 fps
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
				}
			}
		}
	}

}
