package main.java.consultaCEP.infra;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class Cache {

	private static net.sf.ehcache.Cache cache;

	private static net.sf.ehcache.Cache getCache() {
		cache = CacheManager.getInstance().getCache("app");
		if (cache == null) {
			CacheManager.create();
			CacheManager.getInstance().addCache("app");
			cache = CacheManager.getInstance().getCache("app");
		}

		return cache;
	}

	public static void put(String key, Object obj) {
		Element elm = new Element(key, obj);
		elm.setTimeToLive(86400);
		
		getCache().put(elm);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T get(Class<T> tclass, String key) {
		Element elm = getCache().get(key);
		if(elm == null)
			return null;
		Object obj = elm.getObjectValue();
		if (obj == null)
			return null;

		if (obj.getClass() == tclass)
			return (T) obj;

		return null;
	}
}
