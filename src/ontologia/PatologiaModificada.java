package ontologia;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PatologiaModificada
* @author ontology bean generator
* @version 2019/08/15, 22:51:46
*/
public class PatologiaModificada implements Predicate {

   /**
* Protege name: patologia
   */
   private Patologia patologia;
   public void setPatologia(Patologia value) { 
    this.patologia=value;
   }
   public Patologia getPatologia() {
     return this.patologia;
   }

}
