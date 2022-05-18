package com.idmi.app.screener.models;

public class Coin
{
	private final String name;
	private final float price;
	private final float quontity;
	private final float dollarValue;

	public Coin(String name, float price, float quontity, float dollarValue)
	{
		this.name = name;
		this.price = price;
		this.quontity = quontity;
		this.dollarValue = dollarValue;
	}

	public float getPrice()
	{
		return price;
	}

	public float getQuontity()
	{
		return quontity;
	}

	public float getDollarValue()
	{
		return dollarValue;
	}

	public String getName()
	{
		return name;
	}

}
