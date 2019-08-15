package ontologia;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Diagnostico
* @author ontology bean generator
* @version 2019/08/15, 09:58:39
*/
public class Diagnostico implements Concept {

   /**
* Protege name: patologia
   */
   private String patologia;
   public void setPatologia(String value) { 
    this.patologia=value;
   }
   public String getPatologia() {
     return this.patologia;
   }

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
* Protege name: sintoma3
   */
   private String sintoma3;
   public void setSintoma3(String value) { 
    this.sintoma3=value;
   }
   public String getSintoma3() {
     return this.sintoma3;
   }

}
