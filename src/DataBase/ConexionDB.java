package DataBase;

import jade.util.leap.ArrayList;
import jade.util.leap.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologia.*;

/**
 *
 * @author EQUIPO
 */
public class ConexionDB {

    Connection connect;
    Statement consulta;
    String url = "base_de_datos/salud.db";

    public void connect() {
        try {
            connect = DriverManager.getConnection("jdbc:sqlite:" + url);
            consulta = connect.createStatement();
        } catch (SQLException ex) {
            System.err.println("No se ha podido conectar a la base de datos\n" + ex.getMessage());
        }
    }

    public void close() {
        try {
            connect.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveUsuario(Usuario usuario) {
        try {
            connect();
            String sql = "insert into usuarios (identificacion, nombre) "
                    + "values ( " + usuario.getIdentificacion() + ",'" + usuario.getNombre()
                    + "')";
            consulta.execute(sql);
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public Object buscarUsuario(int identificacion) {
        Usuario usuario = null;
        try {
            connect();
            String sql = "SELECT * FROM usuarios WHERE identificacion = " + identificacion;
            ResultSet result = consulta.executeQuery(sql);
            while (result.next()) {
                usuario = new Usuario();
                usuario.setIdentificacion(identificacion);
                usuario.setNombre(result.getString("nombre"));
            }
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return usuario;
    }

    public void savePatologia(Patologia patologia) {
        try {
            connect();
            String sql = "INSERT INTO patologias VALUES(NULL, '" + patologia.getNombre()
                    + "','" + patologia.getSintoma1() + "','" + patologia.getSintoma2()
                    + "','" + patologia.getSintoma3() + "','" + patologia.getTratamiento() + "')";
            consulta.execute(sql);
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void editPatologia(Patologia patologia) {
        try {
            connect();
            PreparedStatement st = connect.prepareStatement("UPDATE patologias SET sintoma1 = ? ,"
                    + " sintoma2 = ? ,"
                    + " sintoma3 = ?, "
                    + " nombre = ?,"
                    + " tratamiento = ?"
                    + "WHERE id = ?");
            st.setString(1, patologia.getSintoma1());
            st.setString(2, patologia.getSintoma2());
            st.setString(3, patologia.getSintoma3());
            st.setString(4, patologia.getNombre());
            st.setString(5, patologia.getTratamiento());
            st.setInt(6, patologia.getId());
            st.execute();
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public List listaPatologias() {
        List lista = new ArrayList();
        try {
            connect();
            String sql = "SELECT * FROM patologias";
            ResultSet result = consulta.executeQuery(sql);
            while (result.next()) {
                Patologia patologia = new Patologia();
                patologia.setId(Integer.parseInt(result.getString("id")));
                patologia.setNombre(result.getString("nombre"));
                patologia.setSintoma1(result.getString("sintoma1"));
                patologia.setSintoma2(result.getString("sintoma2"));
                patologia.setSintoma3(result.getString("sintoma3"));
                patologia.setTratamiento(result.getString("tratamiento"));
                lista.add(patologia);
            }
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return lista;
    }

    public Object buscarPatologia(Diagnostico diagnostico) {
        Diagnostico diagnostico1 = null;
        try {
            connect();
            String sql = "SELECT * FROM patologias WHERE sintoma1 IN ('" + diagnostico.getSintoma1() + "','"
                    + diagnostico.getSintoma2() + "','" + diagnostico.getSintoma3() + "') and "
                    + "sintoma2 IN ('" + diagnostico.getSintoma1() + "','"
                    + diagnostico.getSintoma2() + "','" + diagnostico.getSintoma3() + "') and "
                    + "sintoma3 IN ('" + diagnostico.getSintoma1() + "','"
                    + diagnostico.getSintoma2() + "','" + diagnostico.getSintoma3() + "')";
            ResultSet result = consulta.executeQuery(sql);
            while (result.next()) {
                diagnostico1 = new Diagnostico();
                diagnostico1.setNombrePatologia(result.getString("nombre"));
                diagnostico1.setTratamiento(result.getString("tratamiento"));
            }
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return diagnostico1;
    }
}
