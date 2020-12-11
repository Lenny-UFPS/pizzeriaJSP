/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Lenny
 */
@Entity
@Table(name = "ingrediente_adicional")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IngredienteAdicional.findAll", query = "SELECT i FROM IngredienteAdicional i")
    , @NamedQuery(name = "IngredienteAdicional.findByIdIngrediente", query = "SELECT i FROM IngredienteAdicional i WHERE i.idIngrediente = :idIngrediente")
    , @NamedQuery(name = "IngredienteAdicional.findByDescripcion", query = "SELECT i FROM IngredienteAdicional i WHERE i.descripcion = :descripcion")
    , @NamedQuery(name = "IngredienteAdicional.findByValor", query = "SELECT i FROM IngredienteAdicional i WHERE i.valor = :valor")})
public class IngredienteAdicional implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_ingrediente")
    private Integer idIngrediente;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "valor")
    private double valor;
    @ManyToMany(mappedBy = "ingredienteAdicionalList")
    private List<Pizza> pizzaList;

    public IngredienteAdicional() {
    }

    public IngredienteAdicional(Integer idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public IngredienteAdicional(Integer idIngrediente, String descripcion, double valor) {
        this.idIngrediente = idIngrediente;
        this.descripcion = descripcion;
        this.valor = valor;
    }

    public Integer getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Integer idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @XmlTransient
    public List<Pizza> getPizzaList() {
        return pizzaList;
    }

    public void setPizzaList(List<Pizza> pizzaList) {
        this.pizzaList = pizzaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIngrediente != null ? idIngrediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IngredienteAdicional)) {
            return false;
        }
        IngredienteAdicional other = (IngredienteAdicional) object;
        if ((this.idIngrediente == null && other.idIngrediente != null) || (this.idIngrediente != null && !this.idIngrediente.equals(other.idIngrediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.IngredienteAdicional[ idIngrediente=" + idIngrediente + " ]";
    }
    
}
