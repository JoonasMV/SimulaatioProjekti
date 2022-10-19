package dao;

import java.util.List;

import entity.Ajo;

import jakarta.persistence.*;

/**
 * DAO-luokka ajo-olioiden hallitsemiseen
 */

public class AjoDAO {

	EntityManager em = datasource.DatabaseConnection.getEntityManager();
	
	/**
	 * Hakee ajon id:n perusteella
	 * 
	 * @param id ajon tunnus
	 * @return   ajo, null jos ei löytynyt
	 */
	
	public Ajo hae(int id) {
		EntityTransaction tr = em.getTransaction();
		if(tr.isActive())
			tr.commit();
		
		tr.begin();
		Ajo a = em.find(Ajo.class, id);
		tr.commit();
		return a;
	}
	
	/**
	 * Hakee kaikki ajot
	 * 
	 * @return kaikki ajot listassa
	 */

	public List<Ajo> haeKaikki() {
		EntityTransaction tr = em.getTransaction();
		if(tr.isActive())
			tr.commit();
		
		tr.begin();
		List<Ajo> tulokset = em.createNamedQuery("Ajot.getAllAjot", Ajo.class).getResultList();
		tr.commit();
		return tulokset;
	}
	
	/**
	 * Tallentaa ajon
	 * 
	 * @param t tallennettava ajo
	 */

	public void tallenna(Ajo t) {
		EntityTransaction tr = em.getTransaction();
		if(tr.isActive())
			tr.commit();
		
		tr.begin();
		em.persist(t);
		tr.commit();
	}
	
	/**
	 * Päivittää ajon
	 * 
	 * @param t päivitettävä ajo
	 */

	public void paivita(Ajo t) {
		EntityTransaction tr = em.getTransaction();
		if(tr.isActive())
			tr.commit();
		
		tr.begin();
		Ajo a = em.find(Ajo.class, t.getId());
		if(a != null) {
			em.remove(a);
			em.persist(t);
		}
		tr.commit();
	}
	
	/**
	 * Poistaa ajon
	 * 
	 * @param t poistettava ajo
	 */

	public void poista(Ajo t) {
		EntityTransaction tr = em.getTransaction();
		if(tr.isActive())
			tr.commit();
		
		tr.begin();
		em.remove(t);
		tr.commit();
	}

}
