package main.java.consultaCEP.interfaces;

public interface ICache {

	static ICache getInstance() {
		return null;
	}
	static void setInstance(ICache cache) {
	}

	void put(String key, Object obj);

	<T extends Object> T get(Class<T> tclass, String key);
}
