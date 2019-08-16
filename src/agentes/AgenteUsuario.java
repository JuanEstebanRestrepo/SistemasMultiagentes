/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologia.*;

public class AgenteUsuario extends Agent {

    private final Scanner entrada = new Scanner(System.in);
    private final BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
    private final Codec codec = new SLCodec();
    private final Ontology ontologia = SaludOntology.getInstance();

    DFAgentDescription[] resultados;
    DFAgentDescription[] resultado_usuario;
    static int menu = 0;
    static String cc = "0";
    static String option = "";

    @Override
    public void setup() {

        System.out.println("El agente: " + getAID().getName() + " está corriendo.");

        // Registro el servicio que presta este agente
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType("usuario");
            sd.setName("Interactuar con el usuario");
            dfd.addServices(sd);
            DFService.register(this, dfd);
        } catch (FIPAException e) {
        }
        try {
            buscarServicio();
        } catch (FIPAException ex) {
            Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.addBehaviour(new Menu());
        this.addBehaviour(new ProtocoloOdontologo());
        this.addBehaviour(new ProtocoloExperto());

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);

    }

    public void buscarServicio() throws FIPAException {
        DFAgentDescription descripcion = new DFAgentDescription();
        // Todas las descripciones que encajan con la plantilla proporcionada en el DF
        resultados = DFService.search(this, descripcion);
        if (resultados.length == 0) {
            System.out.println("Ningun agente ofrece el servicio deseado");
        } else {
            for (int i = 0; i < resultados.length; ++i) {
                System.out.println("El agente " + resultados[i].getName().getLocalName() + " ofrece los siguientes servicios:");
                Iterator servicios = resultados[i].getAllServices();
                int j = 1;
                while (servicios.hasNext()) {
                    ServiceDescription servicio = (ServiceDescription) servicios.next();
                    System.out.println(j + "- " + servicio.getName());
                    j++;
                }
            }
        }
    }

    private class NoDiagnostico extends CyclicBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("AgenteOdontologo");
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(id),
                    MessageTemplate.MatchContent("no diagnostico"));
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                System.out.println("No se encontro un diagnostico con esos sintomas");
                this.myAgent.addBehaviour(new Menu());
            } else {
                block();
            }
        }
    }

    private class RespuestaEdicionPatologia extends CyclicBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("AgenteExperto");
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(id),
                    MessageTemplate.MatchContent("patologia editada"));
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                System.out.println(msg.getContent());
                this.myAgent.addBehaviour(new Menu());
            } else {
                block();
            }
        }
    }

    private class EditarPatologia extends OneShotBehaviour {

        private final Patologias patologias;

        private EditarPatologia(Patologias patologias) {
            this.patologias = patologias;
        }

        @Override
        public void action() {
            try {
                System.out.println("Lista de patologias");
                System.out.println("Seleccione una opcion");
                List listaPatologias = patologias.getListaPatologias();
                for (int i = 0; i < listaPatologias.size(); i++) {
                    Patologia get = (Patologia) listaPatologias.get(i);
                    System.out.println((i + 1) + ". " + get.getNombre());
                }
                int opcion = entrada.nextInt();
                Patologia patologia = (Patologia) listaPatologias.get(opcion - 1);
                System.out.println("Selecciono la patologia: " + patologia.getNombre());
                System.out.println("Con los siguientes sintomas");
                System.out.println(patologia.getSintoma1());
                System.out.println(patologia.getSintoma2());
                System.out.println(patologia.getSintoma3());
                System.out.println("Tratamiento");
                System.out.println(patologia.getTratamiento());
                System.out.println("");
                System.out.println("Que quiere editar?");
                System.out.println("1. nombre");
                System.out.println("2. sintoma 1");
                System.out.println("3. sintoma 2");
                System.out.println("4. sintoma 3");
                System.out.println("5. tratamiento");
                opcion = entrada.nextInt();
                String nuevo;
                switch (opcion) {
                    case 1:
                        System.out.println("Ingrese el nuevo nombre");
                        nuevo = buff.readLine();
                        patologia.setNombre(nuevo);
                        break;
                    case 2:
                        System.out.println("Ingrese el nuevo sintoma 1");
                        nuevo = buff.readLine();
                        patologia.setSintoma1(nuevo);
                        break;
                    case 3:
                        System.out.println("Ingrese el nuevo sintoma 2");
                        nuevo = buff.readLine();
                        patologia.setSintoma2(nuevo);
                        break;
                    case 4:
                        System.out.println("Ingrese el nuevo sintoma 3");
                        nuevo = buff.readLine();
                        patologia.setSintoma3(nuevo);
                        break;
                    case 5:
                        System.out.println("Ingrese el nuevo tratamiento");
                        nuevo = buff.readLine();
                        patologia.setTratamiento(nuevo);
                        break;
                    default:
                        break;
                }
                PatologiaModificada patologiaModificada = new PatologiaModificada();
                patologiaModificada.setPatologia(patologia);
                ACLMessage mensaje = new ACLMessage();
                AID id = new AID();
                id.setLocalName("AgenteExperto");
                mensaje.addReceiver(id);
                mensaje.setLanguage(codec.getName());
                mensaje.setOntology(ontologia.getName());
                mensaje.setPerformative(ACLMessage.INFORM);
                getContentManager().fillContent(mensaje, patologiaModificada);
                this.myAgent.send(mensaje);
                this.myAgent.addBehaviour(new RespuestaEdicionPatologia());

            } catch (IOException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Codec.CodecException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static class SolicitarPatologias extends OneShotBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("AgenteExperto");
            ACLMessage mensaje = new ACLMessage();
            mensaje.addReceiver(id);
            mensaje.setPerformative(ACLMessage.INFORM);
            mensaje.setContent("patologias");
            this.myAgent.send(mensaje);
        }
    }

    private class ProtocoloExperto extends CyclicBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("AgenteExperto");
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(id),
                    MessageTemplate.MatchOntology(ontologia.getName()));
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                try {
                    ContentElement ce = getContentManager().extractContent(msg);
                    if (ce instanceof PatologiasCreadas) {
                        PatologiasCreadas patologiasCreadas = (PatologiasCreadas) ce;
                        Patologias patologias = patologiasCreadas.getPatologias();
                        this.myAgent.addBehaviour(new EditarPatologia(patologias));
                    }
                } catch (Codec.CodecException ex) {
                    Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (OntologyException ex) {
                    Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private class RespuestaCreacionPatologia extends CyclicBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("AgenteExperto");
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(id),
                    MessageTemplate.MatchContent("patologia creada"));
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                System.out.println(msg.getContent());
                this.myAgent.addBehaviour(new Menu());
            } else {
                block();
            }
        }
    }

    private class CrearPatologia extends OneShotBehaviour {

        @Override
        public void action() {
            try {
                Patologia patologia = new Patologia();
                System.out.println("Vas a crer una nueva patologia");
                System.out.println("Ingrese nombre");
                String entrada = buff.readLine();
                patologia.setNombre(entrada);
                System.out.println("Ingrese primer sintoma");
                entrada = buff.readLine();
                patologia.setSintoma1(entrada);
                System.out.println("Ingrese segundo sintoma");
                entrada = buff.readLine();
                patologia.setSintoma2(entrada);
                System.out.println("Ingrese tercer sintoma");
                entrada = buff.readLine();
                patologia.setSintoma3(entrada);
                System.out.println("Ingrese tratamiento");
                entrada = buff.readLine();
                patologia.setTratamiento(entrada);
                PatologiaCreada patologiaCreada = new PatologiaCreada();
                patologiaCreada.setPatologia(patologia);
                ACLMessage mensaje = new ACLMessage();
                AID id = new AID();
                id.setLocalName("AgenteExperto");
                mensaje.addReceiver(id);
                mensaje.setLanguage(codec.getName());
                mensaje.setOntology(ontologia.getName());
                mensaje.setPerformative(ACLMessage.INFORM);
                getContentManager().fillContent(mensaje, patologiaCreada);
                this.myAgent.send(mensaje);
                this.myAgent.addBehaviour(new RespuestaCreacionPatologia());
            } catch (IOException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Codec.CodecException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private class SolicitarDiagnostico extends OneShotBehaviour {

        Usuario usuario;

        public SolicitarDiagnostico(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public void action() {
            try {
                Diagnostico diagnostico = new Diagnostico();
                System.out.println("Paciente: " + usuario.getNombre());
                System.out.println("\n-Ingrese sintoma 1");
                String sintoma = buff.readLine();
                diagnostico.setSintoma1(sintoma);
                System.out.println("\n-Ingrese sintoma 2");
                sintoma = buff.readLine();
                diagnostico.setSintoma2(sintoma);
                System.out.println("\n-Ingrese sintoma 3");
                sintoma = buff.readLine();
                diagnostico.setSintoma3(sintoma);
                DiagnosticoCreado diagnosticoCreado = new DiagnosticoCreado();
                diagnosticoCreado.setDiagnostico(diagnostico);
                ACLMessage mensaje = new ACLMessage();
                AID id = new AID();
                id.setLocalName("AgenteOdontologo");
                mensaje.addReceiver(id);
                mensaje.setLanguage(codec.getName());
                mensaje.setOntology(ontologia.getName());
                mensaje.setPerformative(ACLMessage.INFORM);
                getContentManager().fillContent(mensaje, diagnosticoCreado);
                this.myAgent.send(mensaje);
                this.myAgent.addBehaviour(new NoDiagnostico());
            } catch (IOException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Codec.CodecException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class ProtocoloOdontologo extends CyclicBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("AgenteOdontologo");
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(id),
                    MessageTemplate.MatchOntology(ontologia.getName()));
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                try {
                    ContentElement ce = getContentManager().extractContent(msg);
                    if (ce instanceof UsuarioEncontrado) {
                        UsuarioEncontrado usuarioEncontrado = (UsuarioEncontrado) ce;
                        Usuario usuario = usuarioEncontrado.getUsuario();
                        this.myAgent.addBehaviour(new SolicitarDiagnostico(usuario));
                    } else if (ce instanceof DiagnosticoDado) {
                        DiagnosticoDado diagnosticoDado = (DiagnosticoDado) ce;
                        Diagnostico diagnostico = diagnosticoDado.getDiagnostico();
                        System.out.println("El diagnóstico según los sintomas ingresados es: "
                                + diagnostico.getNombrePatologia());
                        System.out.println("Tiene un tratamiento: " + diagnostico.getTratamiento());
                        this.myAgent.addBehaviour(new Menu());
                    }
                } catch (Codec.CodecException ex) {
                    Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
                } catch (OntologyException ex) {
                    Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private class PedirUsuario extends OneShotBehaviour {

        @Override
        public void action() {
            try {
                System.out.println("-Ingrese identificacion del paciente:");
                int identificacion = entrada.nextInt();
                Usuario usuario = new Usuario();
                usuario.setIdentificacion(identificacion);
                UsuarioABuscar usuarioABuscar = new UsuarioABuscar();
                usuarioABuscar.setUsuario(usuario);
                ACLMessage mensaje = new ACLMessage();
                AID id = new AID();
                id.setLocalName("AgenteOdontologo");
                mensaje.addReceiver(id);
                mensaje.setLanguage(codec.getName());
                mensaje.setOntology(ontologia.getName());
                mensaje.setPerformative(ACLMessage.INFORM);
                getContentManager().fillContent(mensaje, usuarioABuscar);
                this.myAgent.send(mensaje);
            } catch (Codec.CodecException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private class RespuestaCreacionUsuario extends CyclicBehaviour {

        @Override
        public void action() {
            AID id = new AID();
            id.setLocalName("AgenteOdontologo");
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(id),
                    MessageTemplate.MatchContent("usuario creado"));
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                System.out.println(msg.getContent());
                this.myAgent.addBehaviour(new Menu());
            } else {
                block();
            }
        }
    }

    private class RegistarUsuario extends OneShotBehaviour {

        @Override
        public void action() {
            try {
                Usuario usuario = new Usuario();
                System.out.println("Ingrese los siguientes datos");
                System.out.println("Identificacion");
                int identificacion = entrada.nextInt();
                System.out.println("Nombre");
                String nombre = buff.readLine();
                usuario.setIdentificacion(identificacion);
                usuario.setNombre(nombre);
                UsuarioCreado usuarioCreado = new UsuarioCreado();
                usuarioCreado.setUsuario(usuario);
                ACLMessage mensaje = new ACLMessage();
                AID id = new AID();
                id.setLocalName("AgenteOdontologo");
                mensaje.addReceiver(id);
                mensaje.setLanguage(codec.getName());
                mensaje.setOntology(ontologia.getName());
                mensaje.setPerformative(ACLMessage.INFORM);
                getContentManager().fillContent(mensaje, usuarioCreado);
                this.myAgent.send(mensaje);
                this.myAgent.addBehaviour(new RespuestaCreacionUsuario());

            } catch (IOException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Codec.CodecException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(AgenteUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class Menu extends OneShotBehaviour {

        @Override
        public void action() {
            int k;
            System.out.println("------------------------------");
            System.out.println("|--------  Salud  -----------|");
            System.out.println("|---------  Menú  -----------|");
            System.out.println("|  1. Registrar usuario      |");
            System.out.println("|  2. Solicitar diagnóstico  |");
            System.out.println("|  3. Agregar Patología      |");
            System.out.println("|  4. Editar Patología       |");
            System.out.println("------------------------------");
            k = entrada.nextInt();

            switch (k) {
                case 1:
                    this.myAgent.addBehaviour(new RegistarUsuario());
                    break;
                case 2:
                    this.myAgent.addBehaviour(new PedirUsuario());
                    break;
                case 3:
                    this.myAgent.addBehaviour(new CrearPatologia());
                    break;
                case 4:
                    this.myAgent.addBehaviour(new SolicitarPatologias());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("El agente: " + getAID().getName() + " ha terminado.");
    }
}
