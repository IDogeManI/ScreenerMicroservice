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
	@GetMapping("/spot/asks")
	public List<Coin> getSpotAskCoins()
	{
		return BinanceApi.getCoins(false, true);
	}

	@GetMapping("/spot/bids")
	public List<Coin> getSpotBidsCoins()
	{
		return BinanceApi.getCoins(false, false);
	}

	@GetMapping("/futures/asks")
	public List<Coin> getFuturesAsksCoins()
	{
		return BinanceApi.getCoins(true, true);
	}

	@GetMapping("/futures/bids")
	public List<Coin> getFuturesBidsCoins()
	{
		return BinanceApi.getCoins(true, true);
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
