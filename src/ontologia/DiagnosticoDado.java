package ontologia;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: DiagnosticoDado
* @author ontology bean generator
* @version 2019/08/16, 09:20:08
*/
public class DiagnosticoDado implements Predicate {

   /**
* Protege name: diagnostico
   */
   private Diagnostico diagnostico;
   public void setDiagnostico(Diagnostico value) { 
    this.diagnostico=value;
   }
   public Diagnostico getDiagnostico() {
     return this.diagnostico;
   }

}
