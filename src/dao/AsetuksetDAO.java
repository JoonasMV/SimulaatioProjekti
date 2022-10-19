package dao;

import java.util.List;

import entity.Ajo;
import entity.Asetukset;
import jakarta.persistence.*;

/**
 * DAO-luokka Asetukset-olioiden hallitsemiseen.
 */

public class AsetuksetDAO {
	
	EntityManager em = datasource.DatabaseConnection.getEntityManager();
	
	/**
	 * Hakee asetukset id:n perusteella
	 * 
	 * @param id asetusten tunnus
	 * @return   asetukset, null jos ei löytynyt
	 */
	
	public Asetukset hae(int id) {
		em.getTransaction().begin();
		Asetukset a = em.find(Asetukset.class, id);
		em.getTransaction().commit();
		return a;
	}
	
	/**
	 * Hakee asetukset ajon perusteella
	 * 
	 * @param ajo ajo jonka perusteella haetaan
	 * @return	  asetukset, null jos ei löytynyt
	 */
	
	public Asetukset haeAjonPerusteella(Ajo ajo) {
		EntityTransaction tr = em.getTransaction();
		if(tr.isActive())
			tr.commit();
		tr.begin();
		Query q = em.createNamedQuery("Asetukset.getAjonPerusteella", Asetukset.class);
		q.setParameter(1, ajo);
		@SuppressWarnings("unchecked")
		List<Asetukset> results = q.getResultList();
		tr.commit();
		return results.get(0);
	}
	
	/**
	 * Hakee kaikki ajot
	 * 
	 * @return kaikki ajot listassa
	 */
	
	public List<Asetukset> haeKaikki(){
		throw new IllegalCallerException("Ei implementoitu vielä");
	}
	
	
	/**
	 * Tallentaa asetukset
	 * 
	 * @param a tallennettavat asetukset
	 */
	
	public void tallenna(Asetukset a) {
		EntityManager em = datasource.DatabaseConnection.getEntityManager();
		em.getTransaction().begin();
		em.persist(a);
		em.getTransaction().commit();
	}
	
	/**
	 * Päivittää asetukset
	 * 
	 * @param a päivitettävät asetukset
	 */
	
	public void paivita(Asetukset a) {
		EntityManager em = datasource.DatabaseConnection.getEntityManager();
		em.getTransaction().begin();
		Asetukset as = em.find(Asetukset.class, a.getId());
		if(as != null) {
			em.remove(as);
			em.persist(a);
		}
		em.getTransaction().commit();
	}
	
	/**
	 * Poistaa asetukset id:n perusteella
	 * 
	 * @param id poistettavien asetuksien id
	 */
	
	public void poista(int id) {
		em.getTransaction().begin();
		Asetukset a = em.find(Asetukset.class, id);
		if(a != null) {
			em.remove(a);
		}
		em.getTransaction().commit();
	}

}
