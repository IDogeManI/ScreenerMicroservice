package com.idmi.app.screener.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idmi.app.screener.logic.BinanceApi;
import com.idmi.app.screener.models.Coin;

@RestController
@CrossOrigin("*")
public class ScreenerController
{
	@GetMapping("/spot/asks")
	public List<Coin> getSpotAskCoins(HttpServletRequest request)
	{
		int minValue;
		if (request.getParameter("minValue") != null)
			minValue = Integer.valueOf(request.getParameter("minValue"));
		else
			minValue = 400000;
		return BinanceApi.getCoins(false, true, minValue);
	}

	@GetMapping("/spot/bids")
	public List<Coin> getSpotBidsCoins(HttpServletRequest request)
	{
		int minValue;
		if (request.getParameter("minValue") != null)
			minValue = Integer.valueOf(request.getParameter("minValue"));
		else
			minValue = 400000;
		return BinanceApi.getCoins(false, false, minValue);
	}

	@GetMapping("/futures/asks")
	public List<Coin> getFuturesAsksCoins(HttpServletRequest request)
	{
		int minValue;
		if (request.getParameter("minValue") != null)
			minValue = Integer.valueOf(request.getParameter("minValue"));
		else
			minValue = 200000;
		return BinanceApi.getCoins(true, true, minValue);
	}

	@GetMapping("/futures/bids")
	public List<Coin> getFuturesBidsCoins(HttpServletRequest request)
	{
		int minValue;
		if (request.getParameter("minValue") != null)
			minValue = Integer.valueOf(request.getParameter("minValue"));
		else
			minValue = 200000;
		return BinanceApi.getCoins(true, true, minValue);
	}

}
