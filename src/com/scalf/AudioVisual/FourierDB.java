package com.scalf.AudioVisual;

public class FourierDB {
	private int DB;
	private int oDB;
	
	public int getDB() {
		return DB;
	}

	public void setMagnitude(int DB) {
		this.oDB = this.DB;
		this.DB = DB;
	}

	public int getODB() {
		//Hey, Dirty. Baby I got your int.
		return oDB;
	}
	
	public FourierDB(int DB)
	{
		this.DB = DB;
		oDB = 0;
	}
	
	public FourierDB()
	{
		this(0);
	}	
}
