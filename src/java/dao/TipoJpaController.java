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
import dto.Tipo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lenny
 */
public class TipoJpaController implements Serializable {

    public TipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipo tipo) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pizza pizzaOrphanCheck = tipo.getPizza();
        if (pizzaOrphanCheck != null) {
            Tipo oldTipoOfPizza = pizzaOrphanCheck.getTipo();
            if (oldTipoOfPizza != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pizza " + pizzaOrphanCheck + " already has an item of type Tipo whose pizza column cannot be null. Please make another selection for the pizza field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pizza pizza = tipo.getPizza();
            if (pizza != null) {
                pizza = em.getReference(pizza.getClass(), pizza.getIdPizza());
                tipo.setPizza(pizza);
            }
            em.persist(tipo);
            if (pizza != null) {
                pizza.setTipo(tipo);
                pizza = em.merge(pizza);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipo(tipo.getIdTipo()) != null) {
                throw new PreexistingEntityException("Tipo " + tipo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipo tipo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipo persistentTipo = em.find(Tipo.class, tipo.getIdTipo());
            Pizza pizzaOld = persistentTipo.getPizza();
            Pizza pizzaNew = tipo.getPizza();
            List<String> illegalOrphanMessages = null;
            if (pizzaNew != null && !pizzaNew.equals(pizzaOld)) {
                Tipo oldTipoOfPizza = pizzaNew.getTipo();
                if (oldTipoOfPizza != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pizza " + pizzaNew + " already has an item of type Tipo whose pizza column cannot be null. Please make another selection for the pizza field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pizzaNew != null) {
                pizzaNew = em.getReference(pizzaNew.getClass(), pizzaNew.getIdPizza());
                tipo.setPizza(pizzaNew);
            }
            tipo = em.merge(tipo);
            if (pizzaOld != null && !pizzaOld.equals(pizzaNew)) {
                pizzaOld.setTipo(null);
                pizzaOld = em.merge(pizzaOld);
            }
            if (pizzaNew != null && !pizzaNew.equals(pizzaOld)) {
                pizzaNew.setTipo(tipo);
                pizzaNew = em.merge(pizzaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipo.getIdTipo();
                if (findTipo(id) == null) {
                    throw new NonexistentEntityException("The tipo with id " + id + " no longer exists.");
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
            Tipo tipo;
            try {
                tipo = em.getReference(Tipo.class, id);
                tipo.getIdTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipo with id " + id + " no longer exists.", enfe);
            }
            Pizza pizza = tipo.getPizza();
            if (pizza != null) {
                pizza.setTipo(null);
                pizza = em.merge(pizza);
            }
            em.remove(tipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipo> findTipoEntities() {
        return findTipoEntities(true, -1, -1);
    }

    public List<Tipo> findTipoEntities(int maxResults, int firstResult) {
        return findTipoEntities(false, maxResults, firstResult);
    }

    private List<Tipo> findTipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipo.class));
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

    public Tipo findTipo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipo> rt = cq.from(Tipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
