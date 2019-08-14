package ontologisalud;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Analizar_patologias
* @author ontology bean generator
* @version 2019/08/9, 02:09:47
*/
public class Analizar_patologias implements Predicate {

   /**
* Protege name: Informacion_caries_incipiente
   */
   private Caries_incipiente informacion_caries_incipiente;
   public void setInformacion_caries_incipiente(Caries_incipiente value) { 
    this.informacion_caries_incipiente=value;
   }
   public Caries_incipiente getInformacion_caries_incipiente() {
     return this.informacion_caries_incipiente;
   }

   /**
* Protege name: Informacion_pulpitis_reversible
   */
   private Pulpitis_reversible informacion_pulpitis_reversible;
   public void setInformacion_pulpitis_reversible(Pulpitis_reversible value) { 
    this.informacion_pulpitis_reversible=value;
   }
   public Pulpitis_reversible getInformacion_pulpitis_reversible() {
     return this.informacion_pulpitis_reversible;
   }

   /**
* Protege name: Informacion_pulpitis_ireversible
   */
   private Pulpitis_irreversible informacion_pulpitis_ireversible;
   public void setInformacion_pulpitis_ireversible(Pulpitis_irreversible value) { 
    this.informacion_pulpitis_ireversible=value;
   }
   public Pulpitis_irreversible getInformacion_pulpitis_ireversible() {
     return this.informacion_pulpitis_ireversible;
   }

}
