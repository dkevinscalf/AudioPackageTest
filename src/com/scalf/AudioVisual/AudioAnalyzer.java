package com.scalf.AudioVisual;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.Log;

@TargetApi(9)
public class AudioAnalyzer {
	private Visualizer mVisualizer;
	private byte[] mFFTBytes;	
	private static int FDivisions = 4;
	
	public FourierDBCollection FourierData;
	
	public AudioAnalyzer()
	{
		FourierData = new FourierDBCollection(FDivisions);
	}
	
	public void link(MediaPlayer player)
	  {
	    if(player == null)
	    {
	      throw new NullPointerException("Cannot link to null MediaPlayer");
	    }

	    // Create the Visualizer object and attach it to our media player.
	    Log.d("test", "1");
	    mVisualizer = new Visualizer(player.getAudioSessionId());
	    Log.d("test", "2");
	    mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

	    // Pass through Visualizer data to VisualizerView
	    Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener()
	    {
	      @Override
	      public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
	          int samplingRate)
	      {
	        //Do Nothing, we want FFT
	      }

	      @Override
	      public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
	          int samplingRate)
	      {
	        updateVisualizerFFT(bytes);
	      }
	    };

	    mVisualizer.setDataCaptureListener(captureListener,
	        Visualizer.getMaxCaptureRate() / 2, true, true);

	    // Enabled Visualizer and disable when we're done with the stream
	    mVisualizer.setEnabled(true);
	    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
	    {
	      @Override
	      public void onCompletion(MediaPlayer mediaPlayer)
	      {
	        mVisualizer.setEnabled(false);
	      }
	    });
	  }
	
	public void release()
	{
		mVisualizer.release();
	}

	/**
	* Pass FFT data to the visualizer. Typically this will be obtained from the
	   * Android Visualizer.OnDataCaptureListener call back. See
	   * {@link Visualizer.OnDataCaptureListener#onFftDataCapture }
	   * @param bytes
	   */
	public void updateVisualizerFFT(byte[] bytes) {
	    mFFTBytes = bytes;
	    FourierData.ProcessFourierData(mFFTBytes);
	}

	public void DrawFFT(Canvas canvas, Rect targetRect, Paint flashPaint, Paint fadePaint, Boolean mTop)
	{
		int divWidth = Math.round(targetRect.width()/FourierData.Items.length);
		int left = 0;
		int right = 0;
		int top1 = 0;
		int top2 = 0;
		int bottom1 = 0;
		int bottom2 = 0;
		int dbValue = 0;
		int oldDBValue = 0;

		for(int i=0;i<FourierData.Items.length;i++)
		{
			dbValue = FourierData.Items[i].getDB();
			oldDBValue = FourierData.Items[i].getODB();
			
			left = targetRect.left + (i*divWidth);
			right = targetRect.left + ((i+1)*divWidth);
			
			if(mTop)
			{
				top1 = targetRect.top;
				top2 = targetRect.top;
				bottom1 = targetRect.top + ((dbValue * 2) -10);
				bottom2 = targetRect.top + ((oldDBValue * 2) -10);
			}
			else
			{
				top1 = targetRect.bottom - ((dbValue * 2) -10);
				top2 = targetRect.bottom - ((oldDBValue * 2) -10);
				bottom1 = targetRect.bottom;
				bottom2 = targetRect.bottom;
			}
			Rect flashRect = new Rect(left, top1, right, bottom1);
			Rect fadeRect = new Rect(left, top2, right, bottom2);
			
			canvas.drawRect(fadeRect, fadePaint);
			canvas.drawRect(flashRect, flashPaint);
		}
	}
}
