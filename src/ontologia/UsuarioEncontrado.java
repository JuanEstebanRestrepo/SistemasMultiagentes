package ontologia;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: UsuarioEncontrado
* @author ontology bean generator
* @version 2019/08/14, 21:41:54
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
