package dto;

import dto.Pizza;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-12-10T21:51:34")
@StaticMetamodel(IngredienteAdicional.class)
public class IngredienteAdicional_ { 

    public static volatile SingularAttribute<IngredienteAdicional, String> descripcion;
    public static volatile SingularAttribute<IngredienteAdicional, Double> valor;
    public static volatile ListAttribute<IngredienteAdicional, Pizza> pizzaList;
    public static volatile SingularAttribute<IngredienteAdicional, Integer> idIngrediente;

}