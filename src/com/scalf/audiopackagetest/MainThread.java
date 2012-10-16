package com.scalf.audiopackagetest;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	 
	private SurfaceHolder surfaceHolder;
	private AudioVisualTest gamePanel;
	
	private static final String TAG = MainThread.class.getSimpleName();
	
	public MainThread(SurfaceHolder surfaceHolder, AudioVisualTest gamePanel)
	{
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
		
	}
	
	 // flag to hold game state
	 private boolean running;
	 public void setRunning(boolean running) {
	  this.running = running;
	 }
	 
	 @Override
	 public void run() {
		 Canvas canvas;
		 Log.d(TAG, "Starting game loop");
	  while (running) {
		  canvas=null;
		  try
		  {
			  canvas = this.surfaceHolder.lockCanvas();
			  synchronized (surfaceHolder)
			  {
				  this.gamePanel.onDraw(canvas);
				  this.gamePanel.update();
			  }
		  }
		  finally
		  {
			  if(canvas != null)
			  {
				  surfaceHolder.unlockCanvasAndPost(canvas);
			  }
		  }
	  }
	 }
	}