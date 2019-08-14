package ontologisalud;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Solicitar_tratamiento
* @author ontology bean generator
* @version 2019/08/9, 02:09:47
*/
public class Solicitar_tratamiento implements Predicate {

   /**
* Protege name: Profilaxis
   */
   private Pulpitis_reversible profilaxis;
   public void setProfilaxis(Pulpitis_reversible value) { 
    this.profilaxis=value;
   }
   public Pulpitis_reversible getProfilaxis() {
     return this.profilaxis;
   }

   /**
* Protege name: Topicacion_fluor
   */
   private Caries_incipiente topicacion_fluor;
   public void setTopicacion_fluor(Caries_incipiente value) { 
    this.topicacion_fluor=value;
   }
   public Caries_incipiente getTopicacion_fluor() {
     return this.topicacion_fluor;
   }

   /**
* Protege name: Desbridamiento
   */
   private Pulpitis_irreversible desbridamiento;
   public void setDesbridamiento(Pulpitis_irreversible value) { 
    this.desbridamiento=value;
   }
   public Pulpitis_irreversible getDesbridamiento() {
     return this.desbridamiento;
   }

}
