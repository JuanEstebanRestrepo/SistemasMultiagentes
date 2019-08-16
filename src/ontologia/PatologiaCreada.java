package ontologia;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PatologiaCreada
* @author ontology bean generator
* @version 2019/08/16, 09:20:08
*/
public class PatologiaCreada implements Predicate {

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
