package dto;

import dto.IngredienteAdicional;
import dto.Sabor;
import dto.Tipo;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-12-10T21:51:34")
@StaticMetamodel(Pizza.class)
public class Pizza_ { 

    public static volatile SingularAttribute<Pizza, Tipo> tipo;
    public static volatile ListAttribute<Pizza, IngredienteAdicional> ingredienteAdicionalList;
    public static volatile SingularAttribute<Pizza, Sabor> sabor;
    public static volatile SingularAttribute<Pizza, Integer> idPizza;
    public static volatile SingularAttribute<Pizza, Double> valor;
    public static volatile SingularAttribute<Pizza, Integer> idTipo;
    public static volatile SingularAttribute<Pizza, Integer> idSabor;

}