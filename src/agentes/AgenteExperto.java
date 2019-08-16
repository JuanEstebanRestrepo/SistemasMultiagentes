/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import jade.util.leap.List;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologia.*;

/**
 *
 * @author EQUIPO
 */
public class AgenteExperto extends Agent {

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
            sd.setType("experto");
            sd.setName("Agregar patología");
            dfd.addServices(sd);
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);
        //Agregar comportamientos 
        this.addBehaviour(new ProtocoloUsuario());
        this.addBehaviour(new EnviarPatologias());
    }

    private class EnviarPatologias extends CyclicBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("AgenteUsuario");
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(id),
                    MessageTemplate.MatchContent("patologias"));
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                try {
                    List listaPatologias = baseDatos.listaPatologias();
                    Patologias patologias = new Patologias();
                    patologias.setListaPatologias(listaPatologias);
                    PatologiasCreadas patologiasCreadas = new PatologiasCreadas();
                    patologiasCreadas.setPatologias(patologias);
                    ACLMessage mensaje = new ACLMessage();
                    mensaje.addReceiver(id);
                    mensaje.setLanguage(codec.getName());
                    mensaje.setOntology(ontologia.getName());
                    mensaje.setPerformative(ACLMessage.INFORM);
                    getContentManager().fillContent(mensaje, patologiasCreadas);
                    this.myAgent.send(mensaje);
                } catch (Codec.CodecException ex) {
                    Logger.getLogger(AgenteExperto.class.getName()).log(Level.SEVERE, null, ex);
                } catch (OntologyException ex) {
                    Logger.getLogger(AgenteExperto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private class GuardarPatologia extends OneShotBehaviour {

        private Patologia patologia;

        public GuardarPatologia(Patologia patologia) {
            this.patologia = patologia;
        }

        @Override
        public void action() {
            baseDatos.savePatologia(patologia);
            AID id = new AID();
            id.setLocalName("AgenteUsuario");
            ACLMessage mensaje = new ACLMessage();
            mensaje.addReceiver(id);
            mensaje.setContent("patologia creada");
            this.myAgent.send(mensaje);
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
                    if (ce instanceof PatologiaCreada) {
                        PatologiaCreada patologiaCreada = (PatologiaCreada) ce;
                        Patologia patologia = patologiaCreada.getPatologia();
                        this.myAgent.addBehaviour(new GuardarPatologia(patologia));
                    } else if(ce instanceof PatologiaCreada) {
                        PatologiaCreada patologiaCreada = (PatologiaCreada) ce;
                        Patologia patologia = patologiaCreada.getPatologia();
                    }
                } catch (Codec.CodecException ex) {
                    Logger.getLogger(AgenteExperto.class.getName()).log(Level.SEVERE, null, ex);
                } catch (OntologyException ex) {
                    Logger.getLogger(AgenteExperto.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                block();
            }
        }
    }
}
