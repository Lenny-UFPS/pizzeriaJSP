/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dao.Conexion;
import dao.IngredienteAdicionalJpaController;
import dao.PizzaJpaController;
import dao.SaborJpaController;
import dao.TipoJpaController;
import dao.exceptions.PreexistingEntityException;
import dto.IngredienteAdicional;
import dto.Pizza;
import dto.Sabor;
import dto.Tipo;
import java.util.List;

/**
 *
 * @author Lenny
 */
public class SistemaPizza {
    
    public List<Pizza> getPizzas(){
        Conexion con = Conexion.getConexion();
        PizzaJpaController pizzaDAO = new PizzaJpaController(con.getBd());
        return pizzaDAO.findPizzaEntities();
    }
    
    public List<IngredienteAdicional> getAdicionales(){
        Conexion conn = Conexion.getConexion();
        IngredienteAdicionalJpaController ingredienteDAO = new  IngredienteAdicionalJpaController(conn.getBd());
        return ingredienteDAO.findIngredienteAdicionalEntities();
    }
    
    public List<Sabor> getSabores(){
        Conexion conn = Conexion.getConexion();
        SaborJpaController saborDAO = new SaborJpaController(conn.getBd());
        return saborDAO.findSaborEntities();
    }
    
    public List<Tipo> getTipos(){
        Conexion conn = Conexion.getConexion();
        TipoJpaController tipoDAO = new TipoJpaController(conn.getBd());
        return tipoDAO.findTipoEntities();
    }
    
    public void agregarIngredienteAdicional(int id, String descripcion, double valor) throws Exception{
        try{
            Conexion conn = Conexion.getConexion();
            IngredienteAdicionalJpaController ingredienteDAO = new IngredienteAdicionalJpaController(conn.getBd());
            IngredienteAdicional ingrediente = new IngredienteAdicional(id, descripcion, valor);
            ingredienteDAO.create(ingrediente);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void agregarSabor(int id, String descripcion) throws PreexistingEntityException, Exception{
        try{
            Conexion conn = Conexion.getConexion();
            SaborJpaController saborDAO = new SaborJpaController(conn.getBd());
            Sabor sabor = new Sabor(id, descripcion);
            saborDAO.create(sabor);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void agregarTipo(int id, String descripcion) throws PreexistingEntityException, Exception{
        try{
            Conexion conn = Conexion.getConexion();
            TipoJpaController tipoDAO = new TipoJpaController(conn.getBd());
            Tipo tipo = new Tipo(id, descripcion);
            tipoDAO.create(tipo);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void agregarPizza(Double valor, int idTipo, int idSabor) throws Exception{
        Conexion con = Conexion.getConexion();
        PizzaJpaController pizzaDAO = new PizzaJpaController(con.getBd());
        Pizza nuevaPizza = new Pizza();
        nuevaPizza.setValor(valor);

        nuevaPizza.setIdTipo(getIdTipo(idTipo));
        nuevaPizza.setIdSabor(getIdSabor(idSabor));

        pizzaDAO.create(nuevaPizza);
    }
    
    public int getIdSabor(int id){
        Conexion conn = Conexion.getConexion();
        SaborJpaController sabor = new SaborJpaController(conn.getBd());
        return sabor.findSabor(id).getIdSabor();
    }
    
    public int getIdTipo(int id){
        Conexion conn = Conexion.getConexion();
        TipoJpaController tipo = new TipoJpaController(conn.getBd());
        return tipo.findTipo(id).getIdTipo();
    }
}
