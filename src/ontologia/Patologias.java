package ontologia;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Patologias
* @author ontology bean generator
* @version 2019/08/15, 22:51:46
*/
public class Patologias implements Concept {

   /**
* Protege name: listaPatologias
   */
   private List listaPatologias = new ArrayList();
   public void addListaPatologias(Patologia elem) { 
     List oldList = this.listaPatologias;
     listaPatologias.add(elem);
   }
   public boolean removeListaPatologias(Patologia elem) {
     List oldList = this.listaPatologias;
     boolean result = listaPatologias.remove(elem);
     return result;
   }
   public void clearAllListaPatologias() {
     List oldList = this.listaPatologias;
     listaPatologias.clear();
   }
   public Iterator getAllListaPatologias() {return listaPatologias.iterator(); }
   public List getListaPatologias() {return listaPatologias; }
   public void setListaPatologias(List l) {listaPatologias = l; }

}
