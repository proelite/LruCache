package sample;

import cache.LruCache;

public class SalesTaxRateChecker {
	private final LruCache<String, Double> lruCache = new LruCache<String, Double>(50000);

	public Double fastRateLookUp(String address) {
		if (this.lruCache.contains(address)) {
			return this.lruCache.get(address);
		} else {
			Double rate = salesTaxLookUp(address);
			this.lruCache.put(address, rate);
			return rate;
		}
	}

	public Double salesTaxLookUp(String address) {
		// Stub
		return 0d;
	}
}
