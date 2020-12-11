/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lenny
 */
@Entity
@Table(name = "sabor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sabor.findAll", query = "SELECT s FROM Sabor s")
    , @NamedQuery(name = "Sabor.findByIdSabor", query = "SELECT s FROM Sabor s WHERE s.idSabor = :idSabor")
    , @NamedQuery(name = "Sabor.findByDescripcion", query = "SELECT s FROM Sabor s WHERE s.descripcion = :descripcion")})
public class Sabor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_sabor")
    private Integer idSabor;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "id_sabor", referencedColumnName = "id_sabor", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pizza pizza;

    public Sabor() {
    }

    public Sabor(Integer idSabor) {
        this.idSabor = idSabor;
    }

    public Sabor(Integer idSabor, String descripcion) {
        this.idSabor = idSabor;
        this.descripcion = descripcion;
    }

    public Integer getIdSabor() {
        return idSabor;
    }

    public void setIdSabor(Integer idSabor) {
        this.idSabor = idSabor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSabor != null ? idSabor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sabor)) {
            return false;
        }
        Sabor other = (Sabor) object;
        if ((this.idSabor == null && other.idSabor != null) || (this.idSabor != null && !this.idSabor.equals(other.idSabor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Sabor[ idSabor=" + idSabor + " ]";
    }
    
}
