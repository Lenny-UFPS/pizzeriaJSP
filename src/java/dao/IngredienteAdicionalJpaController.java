/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.IngredienteAdicional;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Pizza;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lenny
 */
public class IngredienteAdicionalJpaController implements Serializable {

    public IngredienteAdicionalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(IngredienteAdicional ingredienteAdicional) throws PreexistingEntityException, Exception {
        if (ingredienteAdicional.getPizzaList() == null) {
            ingredienteAdicional.setPizzaList(new ArrayList<Pizza>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pizza> attachedPizzaList = new ArrayList<Pizza>();
            for (Pizza pizzaListPizzaToAttach : ingredienteAdicional.getPizzaList()) {
                pizzaListPizzaToAttach = em.getReference(pizzaListPizzaToAttach.getClass(), pizzaListPizzaToAttach.getIdPizza());
                attachedPizzaList.add(pizzaListPizzaToAttach);
            }
            ingredienteAdicional.setPizzaList(attachedPizzaList);
            em.persist(ingredienteAdicional);
            for (Pizza pizzaListPizza : ingredienteAdicional.getPizzaList()) {
                pizzaListPizza.getIngredienteAdicionalList().add(ingredienteAdicional);
                pizzaListPizza = em.merge(pizzaListPizza);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIngredienteAdicional(ingredienteAdicional.getIdIngrediente()) != null) {
                throw new PreexistingEntityException("IngredienteAdicional " + ingredienteAdicional + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(IngredienteAdicional ingredienteAdicional) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            IngredienteAdicional persistentIngredienteAdicional = em.find(IngredienteAdicional.class, ingredienteAdicional.getIdIngrediente());
            List<Pizza> pizzaListOld = persistentIngredienteAdicional.getPizzaList();
            List<Pizza> pizzaListNew = ingredienteAdicional.getPizzaList();
            List<Pizza> attachedPizzaListNew = new ArrayList<Pizza>();
            for (Pizza pizzaListNewPizzaToAttach : pizzaListNew) {
                pizzaListNewPizzaToAttach = em.getReference(pizzaListNewPizzaToAttach.getClass(), pizzaListNewPizzaToAttach.getIdPizza());
                attachedPizzaListNew.add(pizzaListNewPizzaToAttach);
            }
            pizzaListNew = attachedPizzaListNew;
            ingredienteAdicional.setPizzaList(pizzaListNew);
            ingredienteAdicional = em.merge(ingredienteAdicional);
            for (Pizza pizzaListOldPizza : pizzaListOld) {
                if (!pizzaListNew.contains(pizzaListOldPizza)) {
                    pizzaListOldPizza.getIngredienteAdicionalList().remove(ingredienteAdicional);
                    pizzaListOldPizza = em.merge(pizzaListOldPizza);
                }
            }
            for (Pizza pizzaListNewPizza : pizzaListNew) {
                if (!pizzaListOld.contains(pizzaListNewPizza)) {
                    pizzaListNewPizza.getIngredienteAdicionalList().add(ingredienteAdicional);
                    pizzaListNewPizza = em.merge(pizzaListNewPizza);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ingredienteAdicional.getIdIngrediente();
                if (findIngredienteAdicional(id) == null) {
                    throw new NonexistentEntityException("The ingredienteAdicional with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            IngredienteAdicional ingredienteAdicional;
            try {
                ingredienteAdicional = em.getReference(IngredienteAdicional.class, id);
                ingredienteAdicional.getIdIngrediente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingredienteAdicional with id " + id + " no longer exists.", enfe);
            }
            List<Pizza> pizzaList = ingredienteAdicional.getPizzaList();
            for (Pizza pizzaListPizza : pizzaList) {
                pizzaListPizza.getIngredienteAdicionalList().remove(ingredienteAdicional);
                pizzaListPizza = em.merge(pizzaListPizza);
            }
            em.remove(ingredienteAdicional);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<IngredienteAdicional> findIngredienteAdicionalEntities() {
        return findIngredienteAdicionalEntities(true, -1, -1);
    }

    public List<IngredienteAdicional> findIngredienteAdicionalEntities(int maxResults, int firstResult) {
        return findIngredienteAdicionalEntities(false, maxResults, firstResult);
    }

    private List<IngredienteAdicional> findIngredienteAdicionalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(IngredienteAdicional.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public IngredienteAdicional findIngredienteAdicional(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(IngredienteAdicional.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngredienteAdicionalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<IngredienteAdicional> rt = cq.from(IngredienteAdicional.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
