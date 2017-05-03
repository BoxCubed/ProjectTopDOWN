package com.boxcubed.utils;

public class Timer {
	public float rate = 0;
	public float last = 0;
	
	public Timer(float r)
	{
		last = System.currentTimeMillis();
		rate = r;
	}
	
	public boolean check()
	{
		if(last+rate<System.currentTimeMillis())
		{
			reboot();
			return true;
		}
		return false;
	}
	
	public int multicheck()
	{
		float total = System.currentTimeMillis()-last;
		int c = 0;
		while(rate<total)
		{
			total-=rate;
			reboot();
			c++;
		}
		return c;
	}
	
	public boolean check(float r)
	{
		if(last+r<System.currentTimeMillis())
		{
			reboot();
			return true;
		}
		return false;
	}
	
	public boolean checkNoReboot()
	{
		if(last+rate<System.currentTimeMillis())
		{
			return true;
		}
		return false;
	}
	
	public void reboot()
	{
		last = System.currentTimeMillis();
	}
}
