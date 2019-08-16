package ontologia;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Patologia
* @author ontology bean generator
* @version 2019/08/16, 09:20:08
*/
public class Patologia implements Concept {

   /**
* Protege name: sintoma2
   */
   private String sintoma2;
   public void setSintoma2(String value) { 
    this.sintoma2=value;
   }
   public String getSintoma2() {
     return this.sintoma2;
   }

   /**
* Protege name: sintoma1
   */
   private String sintoma1;
   public void setSintoma1(String value) { 
    this.sintoma1=value;
   }
   public String getSintoma1() {
     return this.sintoma1;
   }

   /**
* Protege name: nombre
   */
   private String nombre;
   public void setNombre(String value) { 
    this.nombre=value;
   }
   public String getNombre() {
     return this.nombre;
   }

   /**
* Protege name: id
   */
   private int id;
   public void setId(int value) { 
    this.id=value;
   }
   public int getId() {
     return this.id;
   }

   /**
* Protege name: sintoma3
   */
   private String sintoma3;
   public void setSintoma3(String value) { 
    this.sintoma3=value;
   }
   public String getSintoma3() {
     return this.sintoma3;
   }

   /**
* Protege name: tratamiento
   */
   private String tratamiento;
   public void setTratamiento(String value) { 
    this.tratamiento=value;
   }
   public String getTratamiento() {
     return this.tratamiento;
   }

}
