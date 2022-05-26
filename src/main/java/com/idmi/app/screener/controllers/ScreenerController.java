package com.idmi.app.screener.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idmi.app.screener.logic.BinanceApi;
import com.idmi.app.screener.models.Coin;

@RestController
@CrossOrigin("*")
public class ScreenerController
{
	@GetMapping("/stocks")
	public List<Coin> getStockCoins()
	{
		return BinanceApi.getCoins(false);
	}

	@GetMapping("/futures")
	public List<Coin> getFutureCoins()
	{
		return BinanceApi.getCoins(true);
	}

	@PutMapping("/")
	public void changeMinQuontityValue(@RequestParam("value") int newValue)
	{
		if (newValue >= 0)
		{
			BinanceApi.setMinQuontityValue(newValue);
		}
	}
}
