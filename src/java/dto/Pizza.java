/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Lenny
 */
@Entity
@Table(name = "pizza")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pizza.findAll", query = "SELECT p FROM Pizza p")
    , @NamedQuery(name = "Pizza.findByIdPizza", query = "SELECT p FROM Pizza p WHERE p.idPizza = :idPizza")
    , @NamedQuery(name = "Pizza.findByIdTipo", query = "SELECT p FROM Pizza p WHERE p.idTipo = :idTipo")
    , @NamedQuery(name = "Pizza.findByIdSabor", query = "SELECT p FROM Pizza p WHERE p.idSabor = :idSabor")
    , @NamedQuery(name = "Pizza.findByValor", query = "SELECT p FROM Pizza p WHERE p.valor = :valor")})
public class Pizza implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_pizza")
    private Integer idPizza;
    @Basic(optional = false)
    @Column(name = "id_tipo")
    private int idTipo;
    @Basic(optional = false)
    @Column(name = "id_sabor")
    private int idSabor;
    @Basic(optional = false)
    @Column(name = "valor")
    private double valor;
    @JoinTable(name = "pizza_adicional", joinColumns = {
        @JoinColumn(name = "id_pizza", referencedColumnName = "id_pizza")}, inverseJoinColumns = {
        @JoinColumn(name = "id_ingrediente", referencedColumnName = "id_ingrediente")})
    @ManyToMany
    private List<IngredienteAdicional> ingredienteAdicionalList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pizza")
    private Tipo tipo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pizza")
    private Sabor sabor;

    public Pizza() {
    }

    public Pizza(Integer idPizza) {
        this.idPizza = idPizza;
    }

    public Pizza(Integer idPizza, int idTipo, int idSabor, double valor) {
        this.idPizza = idPizza;
        this.idTipo = idTipo;
        this.idSabor = idSabor;
        this.valor = valor;
    }

    public Integer getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(Integer idPizza) {
        this.idPizza = idPizza;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getIdSabor() {
        return idSabor;
    }

    public void setIdSabor(int idSabor) {
        this.idSabor = idSabor;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @XmlTransient
    public List<IngredienteAdicional> getIngredienteAdicionalList() {
        return ingredienteAdicionalList;
    }

    public void setIngredienteAdicionalList(List<IngredienteAdicional> ingredienteAdicionalList) {
        this.ingredienteAdicionalList = ingredienteAdicionalList;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Sabor getSabor() {
        return sabor;
    }

    public void setSabor(Sabor sabor) {
        this.sabor = sabor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPizza != null ? idPizza.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pizza)) {
            return false;
        }
        Pizza other = (Pizza) object;
        if ((this.idPizza == null && other.idPizza != null) || (this.idPizza != null && !this.idPizza.equals(other.idPizza))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Pizza[ idPizza=" + idPizza + " ]";
    }
    
}
