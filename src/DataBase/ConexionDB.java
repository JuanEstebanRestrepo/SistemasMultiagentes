/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
                    + "values ( " + usuario.getIdentificacion()+",'" + usuario.getNombre()
                    + "')";
            consulta.execute(sql);
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void mostrarUsuario() {
        ResultSet result = null;
        try {
            PreparedStatement st = connect.prepareStatement("select * from usuarios");
            result = st.executeQuery();
            while (result.next()) {
                System.out.print("ID: ");
                System.out.println(result.getInt("id"));

                System.out.print("identificacion: ");
                System.out.println(result.getString("identificacion"));

                System.out.print("Nombre: ");
                System.out.println(result.getString("nombre"));
                System.out.println("=======================");
            }
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
            String sql = "INSERT INTO patologias VALUES(NULL, '"+patologia.getNombre()
                    + "','"+ patologia.getSintoma1() + "','" + patologia.getSintoma2()
                    + "','" + patologia.getSintoma3()+"')";
            consulta.execute(sql);
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void editPatologia(String nombre, String x, String y, String z) {
        try {
            PreparedStatement st = connect.prepareStatement("UPDATE patologias SET sintoma1 = ? ,"
                    + " sintoma2 = ? ,"
                    + " sintoma3 = ? "
                    + " WHERE nombre =?");
            st.setString(1, x);
            st.setString(2, y);
            st.setString(3, z);
            st.setString(4, nombre);
            st.execute();
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
                lista.add(patologia);
            }
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return lista;
    }

    public Object buscarPatologia(Diagnostico diagnostico) {
        try {
            connect();
            String sql = "SELECT * FROM patologias WHERE sintoma1 IN ('"+diagnostico.getSintoma1()+"','"
                    + diagnostico.getSintoma2()+ "','"+diagnostico.getSintoma3()+"') and "
                    + "sintoma2 IN ('"+diagnostico.getSintoma1()+"','"
                    + diagnostico.getSintoma2()+ "','"+diagnostico.getSintoma3()+"') and "
                    + "sintoma3 IN ('"+diagnostico.getSintoma1()+"','"
                    + diagnostico.getSintoma2()+ "','"+diagnostico.getSintoma3()+"')";
            ResultSet result = consulta.executeQuery(sql);
            while (result.next()) {
                diagnostico.setNombrePatologia(result.getString("nombre"));
                return diagnostico;
/*
                if ((result.getString("sintoma1").equals(x) || result.getString("sintoma2").equals(x) || result.getString("sintoma3").equals(x))
                        && (result.getString("sintoma1").equals(y) || result.getString("sintoma2").equals(y) || result.getString("sintoma3").equals(y))
                        && (result.getString("sintoma1").equals(z) || result.getString("sintoma2").equals(z) || result.getString("sintoma3").equals(z))) {
                    return (result.getString("nombre"));
                }
*/
            }
            close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
}
