package agentes;

import DataBase.*;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologia.*;

public class AgenteOdontologo extends Agent {

    ConexionDB baseDatos = new ConexionDB();
    private final Codec codec = new SLCodec();
    private final Ontology ontologia = SaludOntology.getInstance();

    @Override
    protected void setup() {

        System.out.println("El agente: " + getAID().getName() + " está corriendo.");

        // Registro el servicio que presta este agente
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType("odontologo");
            sd.setName("Registrar usuario");
            dfd.addServices(sd);
            ServiceDescription sd2 = new ServiceDescription();
            sd2.setType("odontologo");
            sd2.setName("Solicitar diagnóstico");
            dfd.addServices(sd2);
            DFService.register(this, dfd);
        } catch (FIPAException e) {
        }
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);

        //Agregar comportamientos 
        this.addBehaviour(new ProtocoloUsuario());
    }

    private class DarDiagnostico extends OneShotBehaviour {

        private Diagnostico diagnostico;

        public DarDiagnostico(Diagnostico diagnostico) {
            this.diagnostico = diagnostico;
        }

        @Override
        public void action() {
            diagnostico = (Diagnostico) baseDatos.buscarPatologia(diagnostico);
            try {
                ACLMessage mensaje = new ACLMessage();
                AID id = new AID();
                id.setLocalName("AgenteUsuario");
                mensaje.addReceiver(id);
                mensaje.setPerformative(ACLMessage.INFORM);
                if (diagnostico != null) {
                    mensaje.setLanguage(codec.getName());
                    mensaje.setOntology(ontologia.getName());
                    DiagnosticoDado diagnosticoDado = new DiagnosticoDado();
                    diagnosticoDado.setDiagnostico(diagnostico);
                    getContentManager().fillContent(mensaje, diagnosticoDado);
                } else {
                    mensaje.setContent("no diagnostico");
                }
                this.myAgent.send(mensaje);
            } catch (Codec.CodecException ex) {
                Logger.getLogger(AgenteOdontologo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(AgenteOdontologo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class BuscarUsuario extends OneShotBehaviour {

        private Usuario usuario;

        public BuscarUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public void action() {
            try {
                usuario = (Usuario) baseDatos.buscarUsuario(usuario.getIdentificacion());
                UsuarioEncontrado usuarioEncontrado = new UsuarioEncontrado();
                usuarioEncontrado.setUsuario(usuario);
                ACLMessage mensaje = new ACLMessage();
                AID id = new AID();
                id.setLocalName("AgenteUsuario");
                mensaje.addReceiver(id);
                mensaje.setLanguage(codec.getName());
                mensaje.setOntology(ontologia.getName());
                mensaje.setPerformative(ACLMessage.INFORM);
                getContentManager().fillContent(mensaje, usuarioEncontrado);
                this.myAgent.send(mensaje);
            } catch (Codec.CodecException ex) {
                Logger.getLogger(AgenteOdontologo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(AgenteOdontologo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private class ProtocoloUsuario extends CyclicBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("AgenteUsuario");
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(id),
                    MessageTemplate.MatchOntology(ontologia.getName()));
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                try {
                    ContentElement ce = getContentManager().extractContent(msg);
                    if (ce instanceof UsuarioCreado) {
                        UsuarioCreado usuarioCreado = (UsuarioCreado) ce;
                        Usuario usuario = usuarioCreado.getUsuario();
                        baseDatos.saveUsuario(usuario);
                        ACLMessage mensaje = new ACLMessage();
                        mensaje.addReceiver(id);
                        mensaje.setContent("usuario creado");
                        this.myAgent.send(mensaje);
                    } else if (ce instanceof UsuarioABuscar) {
                        UsuarioABuscar usuasrioABuscar = (UsuarioABuscar) ce;
                        Usuario usuario = usuasrioABuscar.getUsuario();
                        this.myAgent.addBehaviour(new BuscarUsuario(usuario));
                    } else if (ce instanceof DiagnosticoCreado) {
                        DiagnosticoCreado diagnosticoCreado = (DiagnosticoCreado) ce;
                        Diagnostico diagnostico = diagnosticoCreado.getDiagnostico();
                        this.myAgent.addBehaviour(new DarDiagnostico(diagnostico));
                    }

                } catch (Codec.CodecException ex) {
                    Logger.getLogger(AgenteOdontologo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (OntologyException ex) {
                    Logger.getLogger(AgenteOdontologo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
