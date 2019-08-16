package ontologia;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: UsuarioEncontrado
* @author ontology bean generator
* @version 2019/08/16, 09:20:08
*/
public class UsuarioEncontrado implements Predicate {

   /**
* Protege name: usuario
   */
   private Usuario usuario;
   public void setUsuario(Usuario value) { 
    this.usuario=value;
   }
   public Usuario getUsuario() {
     return this.usuario;
   }

}
