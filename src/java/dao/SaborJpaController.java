/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Pizza;
import dto.Sabor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lenny
 */
public class SaborJpaController implements Serializable {

    public SaborJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sabor sabor) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pizza pizzaOrphanCheck = sabor.getPizza();
        if (pizzaOrphanCheck != null) {
            Sabor oldSaborOfPizza = pizzaOrphanCheck.getSabor();
            if (oldSaborOfPizza != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pizza " + pizzaOrphanCheck + " already has an item of type Sabor whose pizza column cannot be null. Please make another selection for the pizza field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pizza pizza = sabor.getPizza();
            if (pizza != null) {
                pizza = em.getReference(pizza.getClass(), pizza.getIdPizza());
                sabor.setPizza(pizza);
            }
            em.persist(sabor);
            if (pizza != null) {
                pizza.setSabor(sabor);
                pizza = em.merge(pizza);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSabor(sabor.getIdSabor()) != null) {
                throw new PreexistingEntityException("Sabor " + sabor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sabor sabor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sabor persistentSabor = em.find(Sabor.class, sabor.getIdSabor());
            Pizza pizzaOld = persistentSabor.getPizza();
            Pizza pizzaNew = sabor.getPizza();
            List<String> illegalOrphanMessages = null;
            if (pizzaNew != null && !pizzaNew.equals(pizzaOld)) {
                Sabor oldSaborOfPizza = pizzaNew.getSabor();
                if (oldSaborOfPizza != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pizza " + pizzaNew + " already has an item of type Sabor whose pizza column cannot be null. Please make another selection for the pizza field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pizzaNew != null) {
                pizzaNew = em.getReference(pizzaNew.getClass(), pizzaNew.getIdPizza());
                sabor.setPizza(pizzaNew);
            }
            sabor = em.merge(sabor);
            if (pizzaOld != null && !pizzaOld.equals(pizzaNew)) {
                pizzaOld.setSabor(null);
                pizzaOld = em.merge(pizzaOld);
            }
            if (pizzaNew != null && !pizzaNew.equals(pizzaOld)) {
                pizzaNew.setSabor(sabor);
                pizzaNew = em.merge(pizzaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sabor.getIdSabor();
                if (findSabor(id) == null) {
                    throw new NonexistentEntityException("The sabor with id " + id + " no longer exists.");
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
            Sabor sabor;
            try {
                sabor = em.getReference(Sabor.class, id);
                sabor.getIdSabor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sabor with id " + id + " no longer exists.", enfe);
            }
            Pizza pizza = sabor.getPizza();
            if (pizza != null) {
                pizza.setSabor(null);
                pizza = em.merge(pizza);
            }
            em.remove(sabor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sabor> findSaborEntities() {
        return findSaborEntities(true, -1, -1);
    }

    public List<Sabor> findSaborEntities(int maxResults, int firstResult) {
        return findSaborEntities(false, maxResults, firstResult);
    }

    private List<Sabor> findSaborEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sabor.class));
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

    public Sabor findSabor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sabor.class, id);
        } finally {
            em.close();
        }
    }

    public int getSaborCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sabor> rt = cq.from(Sabor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
