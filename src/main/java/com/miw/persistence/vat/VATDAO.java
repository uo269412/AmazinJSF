package com.miw.persistence.vat;

import java.util.List;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.*;

import com.miw.model.VAT;
import com.miw.persistence.Dba;

@Named("vatDataService")
@RequestScoped
public class VATDAO implements VATDataService {

	protected Logger logger = LogManager.getLogger(getClass());

	public List<VAT> getVATs() throws Exception {

		List<VAT> resultList = null;

		Dba dba = new Dba();
		try {
			EntityManager em = dba.getActiveEm();

			resultList = em.createQuery("Select a From VAT a", VAT.class).getResultList();

			logger.debug("Result list: " + resultList.toString());

		} finally {
			// 100% sure that the transaction and entity manager will be closed
			dba.closeEm();
		}

		// We return the result
		return resultList;
	}

	public VAT getVAT(int id) throws Exception {
		Dba dba = new Dba();
		VAT result;
		try {
			EntityManager em = dba.getActiveEm();

			TypedQuery<VAT> vatquery = em.createQuery("Select a From VAT a where id = :id", VAT.class);
			vatquery.setParameter("id", id);
			try {
				result = vatquery.getSingleResult();

			} catch (NoResultException ex) {
				result = null;
			}

		} finally {
			// 100% sure that the transaction and entity manager will be closed
			dba.closeEm();
		}
		return result;
	}
}