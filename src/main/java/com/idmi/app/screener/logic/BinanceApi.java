package com.idmi.app.screener.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idmi.app.screener.models.Coin;

public final class BinanceApi
{
	private static final String[] ALLSYMBOLS =
	{
			"BCHUSDT", "XRPUSDT", "EOSUSDT", "LTCUSDT", "TRXUSDT", "ETCUSDT", "LINKUSDT", "XLMUSDT", "ADAUSDT",
			"XMRUSDT", "DASHUSDT", "ZECUSDT", "XTZUSDT", "BNBUSDT", "ATOMUSDT", "ONTUSDT", "IOTAUSDT", "BATUSDT",
			"VETUSDT", "NEOUSDT", "QTUMUSDT", "IOSTUSDT", "THETAUSDT", "ALGOUSDT", "ZILUSDT", "KNCUSDT", "ZRXUSDT",
			"COMPUSDT", "OMGUSDT", "DOGEUSDT", "SXPUSDT", "KAVAUSDT", "BANDUSDT", "RLCUSDT", "WAVESUSDT", "MKRUSDT",
			"SNXUSDT", "DOTUSDT", "YFIUSDT", "BALUSDT", "CRVUSDT", "TRBUSDT", "YFIIUSDT", "RUNEUSDT", "SUSHIUSDT",
			"SRMUSDT", "EGLDUSDT", "SOLUSDT", "ICXUSDT", "STORJUSDT", "BLZUSDT", "UNIUSDT", "AVAXUSDT", "FTMUSDT",
			"HNTUSDT", "ENJUSDT", "FLMUSDT", "TOMOUSDT", "RENUSDT", "KSMUSDT", "NEARUSDT", "AAVEUSDT", "FILUSDT",
			"RSRUSDT", "LRCUSDT", "MATICUSDT", "OCEANUSDT", "CVCUSDT", "BELUSDT", "CTKUSDT", "AXSUSDT", "ALPHAUSDT",
			"ZENUSDT", "SKLUSDT", "GRTUSDT", "1INCHUSDT", "AKROUSDT", "CHZUSDT", "SANDUSDT", "ANKRUSDT", "LUNAUSDT",
			"BTSUSDT", "LITUSDT", "UNFIUSDT", "DODOUSDT", "REEFUSDT", "RVNUSDT", "SFPUSDT", "XEMUSDT", "COTIUSDT",
			"CHRUSDT", "MANAUSDT", "ALICEUSDT", "HBARUSDT", "ONEUSDT", "LINAUSDT", "STMXUSDT", "DENTUSDT", "CELRUSDT",
			"HOTUSDT", "MTLUSDT", "OGNUSDT", "NKNUSDT", "SCUSDT", "DGBUSDT", "1000SHIBUSDT", "ICPUSDT", "BAKEUSDT",
			"GTCUSDT", "TLMUSDT", "IOTXUSDT", "RAYUSDT", "MASKUSDT", "ATAUSDT", "DYDXUSDT", "1000XECUSDT", "CELOUSDT",
			"ARUSDT", "KLAYUSDT", "ARPAUSDT", "CTSIUSDT", "LPTUSDT", "ENSUSDT", "ANTUSDT", "ROSEUSDT", "DUSKUSDT",
			"FLOWUSDT", "IMXUSDT"
	};

	private static final String stockURL = "https://api.binance.com/api/v3/depth?symbol=";
	private static final String futureURL = "https://fapi.binance.com/fapi/v1/depth?symbol=";
	private static int minQuontityValue = 500000;

	public static void setMinQuontityValue(int minQuontityValue)
	{
		BinanceApi.minQuontityValue = minQuontityValue;
	}

	public static List<Coin> getCoins(boolean isFuture)
	{
		List<Coin> outputCoins = new ArrayList<Coin>();
		List<CompletableFuture<List<Coin>>> completableFutures = new ArrayList<CompletableFuture<List<Coin>>>();
		for (int i = 0; i < ALLSYMBOLS.length; i++)
		{
			completableFutures.add(getCoinAsync(i, isFuture));
		}
		for (CompletableFuture<List<Coin>> completableFuture : completableFutures)
		{
			try
			{
				outputCoins.addAll(completableFuture.get());
			}
			catch (InterruptedException | ExecutionException e)
			{
				continue;
			}
		}
		return outputCoins;
	}

	private static CompletableFuture<List<Coin>> getCoinAsync(int i, boolean isFuture)
	{
		CompletableFuture<List<Coin>> completableFuture = CompletableFuture.supplyAsync(() ->
		{
			List<Coin> outputCoins = new ArrayList<Coin>();
			StringBuilder result = new StringBuilder();
			URL url;
			try
			{
				url = new URL(isFuture ? futureURL : stockURL + ALLSYMBOLS[i] + "&limit=100");
			}
			catch (MalformedURLException e1)
			{
				return new ArrayList<Coin>();
			}
			HttpURLConnection conn;
			try
			{
				conn = (HttpURLConnection) url.openConnection();
			}
			catch (IOException e1)
			{
				return new ArrayList<Coin>();
			}
			try
			{
				conn.setRequestMethod("GET");
			}
			catch (ProtocolException e1)
			{
				return new ArrayList<Coin>();
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())))
			{
				for (String line; (line = reader.readLine()) != null;)
				{
					result.append(line);
				}
			}
			catch (Exception e)
			{
				return new ArrayList<Coin>();
			}
			Map<String, Object> outputMap;
			try
			{
				outputMap = new ObjectMapper().readValue(result.toString(), HashMap.class);
			}
			catch (JsonProcessingException e)
			{
				return new ArrayList<Coin>();
			}
			List<List<String>> asks = (List<List<String>>) outputMap.get("asks");
			for (List<String> ask : asks)
			{
				float price = Float.valueOf(ask.get(0));
				float quantity = Float.valueOf(ask.get(1));

				if (price * quantity >= minQuontityValue)
				{
					outputCoins.add(new Coin(ALLSYMBOLS[i], price, quantity, price * quantity));
				}
			}

			List<List<String>> bids = (List<List<String>>) outputMap.get("bids");
			for (List<String> bid : bids)
			{
				float price = Float.valueOf(bid.get(0));
				float quantity = Float.valueOf(bid.get(1));

				if (price * quantity >= minQuontityValue)
				{
					outputCoins.add(new Coin(ALLSYMBOLS[i], price, quantity, price * quantity));
				}
			}
			return outputCoins;
		});
		return completableFuture;
	}
}
