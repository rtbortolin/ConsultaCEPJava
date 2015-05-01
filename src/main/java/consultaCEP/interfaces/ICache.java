package main.java.consultaCEP.interfaces;

public interface ICache {

	void put(String key, Object obj);

	<T extends Object> T get(Class<T> tclass, String key);
}
