package com.test;

public class Action
{
	//action이 가지고 있는 변수값. 멤버변수
	public String label, temp, time;

	public Action(String label, String temp, String time)
	{
		super();
		this.label = label;
		this.temp = temp;
		this.time = time;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getTemp()
	{
		return temp;
	}

	public void setTemp(String temp)
	{
		this.temp = temp;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}
	
	
}
