package test.java;

import java.util.HashMap;
import java.util.Map;

import main.java.consultaCEP.domain.entities.Address;

public class AddressesMock {

	private static Map<String, Address> addresses;

	private static void init() {
		addresses = new HashMap<String, Address>();
		addresses.put("14800-010", new Address("Rua Nova Europa",
				"Jardim Quitandinha", "Araraquara", "SP", "14800-010"));
		addresses.put("06010-020", new Address("Rua Melvin Jones", "Centro",
				"Osasco", "SP", "06010-020"));
		addresses.put("69005-070", new Address("Avenida Floriano Peixoto",
				"Centro", "Manaus", "AM", "69005-070"));
	}

	public static Address get(String cep) {
		init();
		return addresses.get(cep);
	}

}
