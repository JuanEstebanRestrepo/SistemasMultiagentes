package ontologisalud;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Caries_incipiente
* @author ontology bean generator
* @version 2019/08/9, 02:09:47
*/
public class Caries_incipiente implements Concept {

   /**
* Protege name: Bordes_definidos
   */
   private String bordes_definidos="bordes_definidos";
   public void setBordes_definidos(String value) { 
    this.bordes_definidos=value;
   }
   public String getBordes_definidos() {
     return this.bordes_definidos;
   }

   /**
* Protege name: Color_blanquecino
   */
   private String color_blanquecino="color_blanquecino";
   public void setColor_blanquecino(String value) { 
    this.color_blanquecino=value;
   }
   public String getColor_blanquecino() {
     return this.color_blanquecino;
   }

   /**
* Protege name: Opacidad_esmalte
   */
   private String opacidad_esmalte="opacidad_esmalte";
   public void setOpacidad_esmalte(String value) { 
    this.opacidad_esmalte=value;
   }
   public String getOpacidad_esmalte() {
     return this.opacidad_esmalte;
   }

}
