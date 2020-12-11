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
import dto.Tipo;
import dto.Sabor;
import dto.IngredienteAdicional;
import dto.Pizza;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lenny
 */
public class PizzaJpaController implements Serializable {

    public PizzaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pizza pizza) throws PreexistingEntityException, Exception {
        if (pizza.getIngredienteAdicionalList() == null) {
            pizza.setIngredienteAdicionalList(new ArrayList<IngredienteAdicional>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipo tipo = pizza.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getIdTipo());
                pizza.setTipo(tipo);
            }
            Sabor sabor = pizza.getSabor();
            if (sabor != null) {
                sabor = em.getReference(sabor.getClass(), sabor.getIdSabor());
                pizza.setSabor(sabor);
            }
            List<IngredienteAdicional> attachedIngredienteAdicionalList = new ArrayList<IngredienteAdicional>();
            for (IngredienteAdicional ingredienteAdicionalListIngredienteAdicionalToAttach : pizza.getIngredienteAdicionalList()) {
                ingredienteAdicionalListIngredienteAdicionalToAttach = em.getReference(ingredienteAdicionalListIngredienteAdicionalToAttach.getClass(), ingredienteAdicionalListIngredienteAdicionalToAttach.getIdIngrediente());
                attachedIngredienteAdicionalList.add(ingredienteAdicionalListIngredienteAdicionalToAttach);
            }
            pizza.setIngredienteAdicionalList(attachedIngredienteAdicionalList);
            em.persist(pizza);
            if (tipo != null) {
                Pizza oldPizzaOfTipo = tipo.getPizza();
                if (oldPizzaOfTipo != null) {
                    oldPizzaOfTipo.setTipo(null);
                    oldPizzaOfTipo = em.merge(oldPizzaOfTipo);
                }
                tipo.setPizza(pizza);
                tipo = em.merge(tipo);
            }
            if (sabor != null) {
                Pizza oldPizzaOfSabor = sabor.getPizza();
                if (oldPizzaOfSabor != null) {
                    oldPizzaOfSabor.setSabor(null);
                    oldPizzaOfSabor = em.merge(oldPizzaOfSabor);
                }
                sabor.setPizza(pizza);
                sabor = em.merge(sabor);
            }
            for (IngredienteAdicional ingredienteAdicionalListIngredienteAdicional : pizza.getIngredienteAdicionalList()) {
                ingredienteAdicionalListIngredienteAdicional.getPizzaList().add(pizza);
                ingredienteAdicionalListIngredienteAdicional = em.merge(ingredienteAdicionalListIngredienteAdicional);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPizza(pizza.getIdPizza()) != null) {
                throw new PreexistingEntityException("Pizza " + pizza + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pizza pizza) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pizza persistentPizza = em.find(Pizza.class, pizza.getIdPizza());
            Tipo tipoOld = persistentPizza.getTipo();
            Tipo tipoNew = pizza.getTipo();
            Sabor saborOld = persistentPizza.getSabor();
            Sabor saborNew = pizza.getSabor();
            List<IngredienteAdicional> ingredienteAdicionalListOld = persistentPizza.getIngredienteAdicionalList();
            List<IngredienteAdicional> ingredienteAdicionalListNew = pizza.getIngredienteAdicionalList();
            List<String> illegalOrphanMessages = null;
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Tipo " + tipoOld + " since its pizza field is not nullable.");
            }
            if (saborOld != null && !saborOld.equals(saborNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Sabor " + saborOld + " since its pizza field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getIdTipo());
                pizza.setTipo(tipoNew);
            }
            if (saborNew != null) {
                saborNew = em.getReference(saborNew.getClass(), saborNew.getIdSabor());
                pizza.setSabor(saborNew);
            }
            List<IngredienteAdicional> attachedIngredienteAdicionalListNew = new ArrayList<IngredienteAdicional>();
            for (IngredienteAdicional ingredienteAdicionalListNewIngredienteAdicionalToAttach : ingredienteAdicionalListNew) {
                ingredienteAdicionalListNewIngredienteAdicionalToAttach = em.getReference(ingredienteAdicionalListNewIngredienteAdicionalToAttach.getClass(), ingredienteAdicionalListNewIngredienteAdicionalToAttach.getIdIngrediente());
                attachedIngredienteAdicionalListNew.add(ingredienteAdicionalListNewIngredienteAdicionalToAttach);
            }
            ingredienteAdicionalListNew = attachedIngredienteAdicionalListNew;
            pizza.setIngredienteAdicionalList(ingredienteAdicionalListNew);
            pizza = em.merge(pizza);
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                Pizza oldPizzaOfTipo = tipoNew.getPizza();
                if (oldPizzaOfTipo != null) {
                    oldPizzaOfTipo.setTipo(null);
                    oldPizzaOfTipo = em.merge(oldPizzaOfTipo);
                }
                tipoNew.setPizza(pizza);
                tipoNew = em.merge(tipoNew);
            }
            if (saborNew != null && !saborNew.equals(saborOld)) {
                Pizza oldPizzaOfSabor = saborNew.getPizza();
                if (oldPizzaOfSabor != null) {
                    oldPizzaOfSabor.setSabor(null);
                    oldPizzaOfSabor = em.merge(oldPizzaOfSabor);
                }
                saborNew.setPizza(pizza);
                saborNew = em.merge(saborNew);
            }
            for (IngredienteAdicional ingredienteAdicionalListOldIngredienteAdicional : ingredienteAdicionalListOld) {
                if (!ingredienteAdicionalListNew.contains(ingredienteAdicionalListOldIngredienteAdicional)) {
                    ingredienteAdicionalListOldIngredienteAdicional.getPizzaList().remove(pizza);
                    ingredienteAdicionalListOldIngredienteAdicional = em.merge(ingredienteAdicionalListOldIngredienteAdicional);
                }
            }
            for (IngredienteAdicional ingredienteAdicionalListNewIngredienteAdicional : ingredienteAdicionalListNew) {
                if (!ingredienteAdicionalListOld.contains(ingredienteAdicionalListNewIngredienteAdicional)) {
                    ingredienteAdicionalListNewIngredienteAdicional.getPizzaList().add(pizza);
                    ingredienteAdicionalListNewIngredienteAdicional = em.merge(ingredienteAdicionalListNewIngredienteAdicional);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pizza.getIdPizza();
                if (findPizza(id) == null) {
                    throw new NonexistentEntityException("The pizza with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pizza pizza;
            try {
                pizza = em.getReference(Pizza.class, id);
                pizza.getIdPizza();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pizza with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Tipo tipoOrphanCheck = pizza.getTipo();
            if (tipoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pizza (" + pizza + ") cannot be destroyed since the Tipo " + tipoOrphanCheck + " in its tipo field has a non-nullable pizza field.");
            }
            Sabor saborOrphanCheck = pizza.getSabor();
            if (saborOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pizza (" + pizza + ") cannot be destroyed since the Sabor " + saborOrphanCheck + " in its sabor field has a non-nullable pizza field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<IngredienteAdicional> ingredienteAdicionalList = pizza.getIngredienteAdicionalList();
            for (IngredienteAdicional ingredienteAdicionalListIngredienteAdicional : ingredienteAdicionalList) {
                ingredienteAdicionalListIngredienteAdicional.getPizzaList().remove(pizza);
                ingredienteAdicionalListIngredienteAdicional = em.merge(ingredienteAdicionalListIngredienteAdicional);
            }
            em.remove(pizza);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pizza> findPizzaEntities() {
        return findPizzaEntities(true, -1, -1);
    }

    public List<Pizza> findPizzaEntities(int maxResults, int firstResult) {
        return findPizzaEntities(false, maxResults, firstResult);
    }

    private List<Pizza> findPizzaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pizza.class));
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

    public Pizza findPizza(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pizza.class, id);
        } finally {
            em.close();
        }
    }

    public int getPizzaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pizza> rt = cq.from(Pizza.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
