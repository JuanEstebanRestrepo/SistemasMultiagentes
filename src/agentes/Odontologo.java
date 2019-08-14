package agentes;
import DataBase.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import ontologisalud.Caries_incipiente;
import ontologisalud.Pulpitis_irreversible;
import ontologisalud.Pulpitis_reversible;

public class Odontologo extends Agent {

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
            e.printStackTrace();
        }

        //Agregar comportamientos 
        this.addBehaviour(new RegistrarUsuario());
        this.addBehaviour(new EsperarSolicitudNotificacion());
        this.addBehaviour(new EsperarConfirmacion());
        this.addBehaviour(new EsperarSolicitudDiagnostico());
    }

    private class EsperarSolicitudNotificacion extends CyclicBehaviour {

        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                //Recuperación del nombre del paciente
                String ident = msg.getContent();
                System.out.println("\n EL AGENTE ESTA ENCARGADO DE SOLICITAR NOTIFICACIONES DEL PACIENTE CON IDENTIFICACIÓN: " + ident + "\n \n");
                ACLMessage reply = msg.createReply();
//                String ide = (String) listaPacientes.get(ident);
                ConexionDB holo = new ConexionDB();
                holo.connect();
                String ide =holo.buscarUsuario(ident);
                holo.close();
                if (ide != null) {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(ide);
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    System.out.println("paciente no encontrado");
                    reply.setContent("paciente no encontrado");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }
    
    private class RegistrarUsuario extends CyclicBehaviour{
        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String[] usuario = msg.getContent().split(",");
                ConexionDB holi = new ConexionDB();
                holi.connect();
                holi.saveUsuario(usuario[0],usuario[1]);
                holi.close();
                System.out.println("Conexión cerrada \n");
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.CFP);
                reply.setContent("Usuario registrado");
                myAgent.send(reply);
                myAgent.doDelete();
            } else {
                block();
            }
        }
    }
    

    private class EsperarConfirmacion extends CyclicBehaviour {

        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                System.out.println("NOTIFICACIÓN ENVIADA!!!");
                
                Usuario.menu();
                myAgent.addBehaviour(new EsperarSolicitudNotificacion());
            } else {
                block();
            }
        }
    }

    private class EsperarSolicitudDiagnostico extends CyclicBehaviour {

        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                //Recuperación del nombre de la patología
                String ident = msg.getContent();
            System.out.println("\n EL AGENTE ESTA ENCARGADO DE SOLICITAR DIAGNÓSTICO PARA LOS SINTOMAS: " + ident + "\n \n");
                ACLMessage reply = msg.createReply();
                
                String[] sintomas = ident.split(",");
                
                ConexionDB holo = new ConexionDB();
                holo.connect();
                    String nombre = holo.buscarPatologia(sintomas[0],sintomas[1],sintomas[2]);
                holo.close();
                
                if (nombre != null) {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(nombre);
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    System.out.println("Diagnóstico no hencontrado");
                    reply.setContent("Diagnóstico no encontrado");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }
}
