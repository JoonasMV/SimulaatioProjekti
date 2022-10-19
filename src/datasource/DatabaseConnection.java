package datasource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Tietokantayhteys-luokka (non-instantiable)
 */

public class DatabaseConnection {

	private static EntityManagerFactory emf = null;
	private static EntityManager em = null;
	
	//Tunnukset käytettävä database määritelty persistence.xml
	//Käyttäjätunnus
	private static final String USER = Objects.requireNonNull(System.getenv("USER"));
	//Salasana
	private static final String PW = Objects.requireNonNull(System.getenv("PW"));
	
	private static final String USER_KEY = "jakarta.persistence.jdbc.user";
	private static final String PW_KEY = "jakarta.persistence.jdbc.password";
	
	private DatabaseConnection() {
	}
	
	/**
	 * Luo yhteyden tietokantaan 
	 * <p>
	 * Tunnukset haetaan ympäristömuuttujista USER, ja PW
	 * <p>
	 * 
	 * @return entitymanager-olio
	 */
	
	public static EntityManager getEntityManager() {
		if(em == null) {
			if(emf == null) {
				Map<String, Object> overrides = new HashMap<String, Object>();
				overrides.put(USER_KEY, USER);
				overrides.put(PW_KEY, PW);
				emf = Persistence.createEntityManagerFactory("DevPU", overrides);
			}
			em = emf.createEntityManager();
			System.out.println("Yhteys databaseen muodostettu");
		}
		return em;
	}
	
	/**
	 * Luo yhteyden tietokantaan jos sitä ei ole olemassa
	 * <p>
	 * Tarkoitus luoda yhteys silloin kun ohjelma käynnistetään, niin ensimmäinen simulointikerta
	 * käynnistyy nopeammin
	 * <p>
	 */
	
	public static void createConnection() {
		if(em == null) {
			if(emf == null) {
				Map<String, Object> overrides = new HashMap<String, Object>();
				overrides.put(USER_KEY, USER);
				overrides.put(PW_KEY, PW);
				emf = Persistence.createEntityManagerFactory("DevPU", overrides);
			}
			em = emf.createEntityManager();
			System.out.println("Yhteys databaseen muodostettu");
		}
	}
}
