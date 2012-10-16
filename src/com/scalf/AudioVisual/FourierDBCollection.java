package com.scalf.AudioVisual;

public class FourierDBCollection {
	public FourierDB[] Items;
	private int mDivisions;
	
	public FourierDBCollection(int divisions)
	{
		mDivisions = divisions;
	}
	
	public FourierDBCollection()
	{
		this(1);
	}
	
	public void ProcessFourierData(byte[] FFTData)
	{
		if(FFTData!= null)
		{
			if(Items==null || Items.length < FFTData.length/mDivisions)
			{
				Items = new FourierDB[FFTData.length/mDivisions];
			}
			
			for(int i=0; i<FFTData.length/mDivisions; i++)
			{
	    	      byte rfk = FFTData[mDivisions * i];
	    	      byte ifk = FFTData[mDivisions * i + 1];
	    	      float magnitude = (rfk * rfk + ifk * ifk);	    	      
	    	      int dbValue = (int) (10 * Math.log10(magnitude));
	    	      if(Items[i] == null)
	    	      {
	    	    	  Items[i].setMagnitude(dbValue);
	    	      }
			}
		}
	}
}
