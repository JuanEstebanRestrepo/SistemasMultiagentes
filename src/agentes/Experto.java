/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.Hashtable;
import java.util.Scanner;

/**
 *
 * @author EQUIPO
 */
public class Experto extends Agent{

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

        //Agregar comportamientos 
        this.addBehaviour(new EsperarSolicitudPatologia());
    }
    
    private class EsperarSolicitudPatologia extends CyclicBehaviour {

        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String[] sintomas = msg.getContent().split(",");
                ConexionDB holi = new ConexionDB();
                holi.connect();
                holi.savePatologia(sintomas[0],sintomas[1],sintomas[2],sintomas[3]);
                System.out.println("Patología agregada");
                holi.close();
                System.out.println("Conexión cerrada \n");
                holi.connect();
                holi.mostrarPatologia();
                holi.close();
                 ACLMessage reply = msg.createReply();
                 myAgent.send(reply);
            } else {
                block();
            }
        }
    }
}