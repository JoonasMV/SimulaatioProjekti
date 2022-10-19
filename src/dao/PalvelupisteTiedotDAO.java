package dao;

import java.util.ArrayList;
import java.util.List;

import entity.PalvelupisteTiedot;

import jakarta.persistence.*;

/**
 * DAO-luokka PalvelupisteTiedot-olioiden hallitsemiseen
 */

public class PalvelupisteTiedotDAO {

	EntityManager em = datasource.DatabaseConnection.getEntityManager();
	
	/**
	 * Hakee PalvelupisteTiedot id:n perusteella
	 * 
	 * @param id tunnus
	 * @return   tiedot, null jos ei löytynyt
	 */
	
	
	public PalvelupisteTiedot hae(int id) {
		em.getTransaction().begin();
		PalvelupisteTiedot p = em.find(PalvelupisteTiedot.class, id);
		em.getTransaction().commit();
		return p;
	}
	
	/**
	 * Hakee kaikki palvelupistetiedot ajon id:n perusteella
	 * 
	 * @param ajoId ajon id
	 * @return		tulokset listassa
	 */

	public List<PalvelupisteTiedot> haeKaikkiAjosta(int ajoId) {
		List<PalvelupisteTiedot> tulokset = new ArrayList<PalvelupisteTiedot>();
		List<PalvelupisteTiedot> kaikki = haeKaikki();
		for(PalvelupisteTiedot p : kaikki) {
			if(p.getAjo().getId() == ajoId) {
				tulokset.add(p);
			}
		}
		return tulokset;
	}
	
	/**
	 * Tallentaa palvelupisteen tiedot
	 * 
	 * @param t tiedot
	 */

	public void tallenna(PalvelupisteTiedot t) {
		EntityManager em = datasource.DatabaseConnection.getEntityManager();
		em.getTransaction().begin();
		em.persist(t);
		em.getTransaction().commit();
	}
	
	/**
	 * Päivittää palvelupisteen tiedot
	 * 
	 * @param t tiedot
	 */

	public void paivita(PalvelupisteTiedot t) {
		EntityManager em = datasource.DatabaseConnection.getEntityManager();
		em.getTransaction().begin();
		PalvelupisteTiedot d = em.find(PalvelupisteTiedot.class, t.getId());
		if(d != null) {
			em.remove(d);
			em.persist(t);
		}
		em.getTransaction().commit();
	}

	/**
	 * Poistaa palvelupisteen tiedot
	 * 
	 * @param t poistettava
	 */
	
	public void poista(PalvelupisteTiedot t) {
		EntityManager em = datasource.DatabaseConnection.getEntityManager();
		em.getTransaction().begin();
		em.remove(t);
		em.getTransaction().commit();
	}
	
	/**
	 * Hakee kaikki tiedot
	 * 
	 * @return tiedot listassa
	 */

	public List<PalvelupisteTiedot> haeKaikki() {
		EntityManager em = datasource.DatabaseConnection.getEntityManager();
		EntityTransaction tr = em.getTransaction();
		if(tr.isActive())
			tr.commit();
		tr.begin();
		List<PalvelupisteTiedot> tulokset = em.createNamedQuery("Palvelupiste.getAll", PalvelupisteTiedot.class).getResultList();
		tr.commit();
		return tulokset;
	}

}
