package main.java.consultaCEP.infra;

import main.java.consultaCEP.interfaces.ICache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class Cache implements ICache {
	private static ICache singletonCache;
	private net.sf.ehcache.Cache cache;

	private net.sf.ehcache.Cache getCache() {
		cache = CacheManager.getInstance().getCache("app");
		if (cache == null) {
			CacheManager.create();
			CacheManager.getInstance().addCache("app");
			cache = CacheManager.getInstance().getCache("app");
		}

		return cache;
	}

	@Override
	public void put(String key, Object obj) {
		Element elm = new Element(key, obj);
		elm.setTimeToLive(86400);

		getCache().put(elm);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Object> T get(Class<T> tclass, String key) {
		Element elm = getCache().get(key);
		if (elm == null)
			return null;
		Object obj = elm.getObjectValue();
		if (obj == null)
			return null;

		if (obj.getClass() == tclass)
			return (T) obj;

		return null;
	}

	public static ICache getInstance() {
		if (singletonCache == null)
			singletonCache = new Cache();
		return singletonCache;
	}

	public static void setInstance(ICache cache) {
		singletonCache = cache;
	}
}
