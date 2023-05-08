/*
 * Copyright (C) 2022 Roger Lovera <roger.lovera>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import servicios.ConnectionDB;

/**
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public class Queries {

    private final ConnectionDB conn;
    public static final int OK = 0;
    public static final int ERROR = -1;
    public static final int USUARIO_EXISTENTE = 1;
    public static final int USUARIO_ATENTIDO = 2;

    /**
     * Constructor
     *
     * @param conn Datos de la conexión a la base de datos
     */
    public Queries(ConnectionDB conn) {
        this.conn = conn;
    }

    public int insertServicio(String documento_identidad, int comida_id) {
        ResultSet rs;

        rs = conn.executeStoredProcedureWithResultSet(
                "insert_servicio",
                documento_identidad,
                comida_id);

        try {
            if (rs != null && rs.next()) {
                return rs.getInt("status");
            } else {
                return ERROR;
            }
        } catch (SQLException ex) {
            return ERROR;
        }
    }

    public Comida getComidaActual() {
        ResultSet rs;
        Comida comida;

        comida = null;

        rs = conn.executeStoredProcedureWithResultSet(
                "select_comida");

        try {
            if (rs != null && rs.next()) {
                comida = new Comida();

                comida.setComidaId(rs.getInt("id"));
                comida.setComida(rs.getString("comida"));
                comida.setInicio(rs.getTime("inicio"));
                comida.setFin(rs.getTime("fin"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return comida;
    }

    public List<UsuarioAtendido> getUsuariosAtendidos(
            String documentoIdentidad,
            String nombres,
            String apellidos,
            Integer tipoUsuarioId,
            Integer comidaId,
            Timestamp fechaInicial,
            Timestamp fechaFinal) {
        ResultSet rs;
        List<UsuarioAtendido> usuariosAtendidos;

        usuariosAtendidos = new ArrayList();

        rs = conn.executeStoredProcedureWithResultSet(
                "select_usuarios_atendidos",
                documentoIdentidad,
                nombres,
                apellidos,
                tipoUsuarioId,
                comidaId,
                fechaInicial,
                fechaFinal,
                0);

        try {
            while (rs.next()) {
                UsuarioAtendido usuarioAtendido;
                Comida comida;
                TipoUsuario tipoUsuario;
                Pais pais;
                
                usuarioAtendido = new UsuarioAtendido();
                comida = new Comida();
                tipoUsuario = new TipoUsuario();
                pais = new Pais();

                usuarioAtendido.setId(rs.getInt("usuario_id"));
                usuarioAtendido.setDocumentoIdentidad(rs.getString("documento_identidad"));
                usuarioAtendido.setNombre1(rs.getString("nombre1"));
                usuarioAtendido.setNombre2(rs.getString("nombre2"));
                usuarioAtendido.setApellido1(rs.getString("apellido1"));
                usuarioAtendido.setApellido2(rs.getString("apellido2"));
                tipoUsuario.setId(rs.getInt("tipo_usuario_id"));
                tipoUsuario.setTipoUsuario(rs.getString("tipo_usuario"));
                usuarioAtendido.setTipoUsuario(tipoUsuario);
                usuarioAtendido.setFechaHoraAtencion(rs.getTimestamp("fecha_hora_atencion"));
                comida.setComidaId(rs.getInt("comida_id"));
                comida.setComida(rs.getString("comida"));
                comida.setInicio(rs.getTime("inicio"));
                comida.setFin(rs.getTime("fin"));
                usuarioAtendido.setComida(comida);
                pais.setId(rs.getInt("pais_id"));
                pais.setPais(rs.getString("pais"));
                usuarioAtendido.setPais(pais);
                usuariosAtendidos.add(usuarioAtendido);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuariosAtendidos;
    }

    public List<TipoUsuario> getTiposUsuario() {
        ResultSet rs;
        List<TipoUsuario> tipoUsuarios;

        tipoUsuarios = new ArrayList<>();

        rs = conn.executeStoredProcedureWithResultSet("select_tipos_usuario");

        try {
            while (rs != null && rs.next()) {
                TipoUsuario tipoUsuario;
                tipoUsuario = new TipoUsuario();
                tipoUsuario.setId(rs.getInt("id"));
                tipoUsuario.setTipoUsuario(rs.getString("tipo_usuario"));
                tipoUsuarios.add(tipoUsuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tipoUsuarios;
    }

    public List<Sexo> getSexos() {
        ResultSet rs;
        List<Sexo> sexos;

        sexos = new ArrayList<>();

        rs = conn.executeStoredProcedureWithResultSet("select_sexos");

        try {
            while (rs != null && rs.next()) {
                Sexo sexo;
                sexo = new Sexo(
                        rs.getInt("id"),
                        rs.getString("sexo"));
                sexos.add(sexo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sexos;
    }

    public List<Comida> getComidas() {
        ResultSet rs;
        List<Comida> comidas;

        comidas = new ArrayList<>();

        rs = conn.executeStoredProcedureWithResultSet("select_comidas");

        try {
            while (rs != null && rs.next()) {
                Comida comida;
                comida = new Comida();
                comida.setComidaId(rs.getInt("id"));
                comida.setComida(rs.getString("comida"));
                comida.setInicio(rs.getTime("inicio"));
                comida.setFin(rs.getTime("fin"));
                comidas.add(comida);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return comidas;
    }

    public List<Pais> getPaises(){
        ResultSet rs;
        List<Pais> paises;

        paises = new ArrayList<>();

        rs = conn.executeStoredProcedureWithResultSet("select_paises");

        try {
            while (rs != null && rs.next()) {
                Pais pais;
                pais = new Pais();
                pais.setId(rs.getInt("id"));
                pais.setPais(rs.getString("pais"));
                paises.add(pais);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Collections.sort(paises.subList(1, paises.size()), (p1, p2) -> p1.getPais().compareTo(p2.getPais()));
        return paises;
    }
    
    public Persona getPersona(String documentoIdentidad) {
        ResultSet rs;
        Persona persona;

        persona = null;
        rs = conn.executeStoredProcedureWithResultSet("select_persona", documentoIdentidad);

        try {
            if (rs != null && rs.next()) {
                persona = new Persona();
                persona.setId(rs.getInt("persona_id"));
                persona.setDocumentoIdentidad(rs.getString("documento_identidad"));
                persona.setNombre1(rs.getString("nombre1"));
                persona.setNombre2(rs.getString("nombre2"));
                persona.setApellido1(rs.getString("apellido1"));
                persona.setApellido2(rs.getString("apellido2"));
                persona.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                persona.setSexo(new Sexo(
                        rs.getInt("sexo_id"),
                        rs.getString("sexo")));
                persona.setTelefono(rs.getString("telefono"));
                persona.setCorreoElectronico(rs.getString("correo_electronico"));
            }
        } catch (SQLException ex) {
            persona = null;
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return persona;
    }

    public Usuario getUsuario(String documentoIdentidad) {
        ResultSet rs;
        Usuario usuario;

        usuario = null;
        rs = conn.executeStoredProcedureWithResultSet("select_usuario", documentoIdentidad);

        try {
            if (rs != null && rs.next()) {                
                usuario = new Usuario();
                usuario.setPersonaId(rs.getInt("persona_id"));
                usuario.setUsuarioId(rs.getInt("usuario_id"));
                usuario.setDocumentoIdentidad(rs.getString("documento_identidad"));
                usuario.setNombre1(rs.getString("nombre1"));
                usuario.setNombre2(rs.getString("nombre2"));
                usuario.setApellido1(rs.getString("apellido1"));
                usuario.setApellido2(rs.getString("apellido2"));
                usuario.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                usuario.setSexo(new Sexo(
                        rs.getInt("sexo_id"),
                        rs.getString("sexo")));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setCorreoElectronico(rs.getString("correo_electronico"));
                usuario.setTipoUsuario(new TipoUsuario(
                        rs.getInt("tipos_usuario_id"),
                        rs.getString("tipo_usuario")));
                usuario.setPais(new Pais(
                        rs.getInt("pais_id"), 
                        rs.getString("pais")));
                usuario.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException ex) {
            usuario = null;
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuario;
    }

    public int updatePersona(Persona persona) {
        ConnectionDB.Status status;

        status = conn.executeStoredProcedure(
                "update_persona",
                persona.getId(),
                persona.getDocumentoIdentidad(),
                persona.getNombre1(),
                persona.getNombre2(),
                persona.getApellido1(),
                persona.getApellido2(),
                persona.getSexo().getId(),
                persona.getFechaNacimiento(),
                persona.getPais().getPais(),
                persona.getTelefono(),
                persona.getCorreoElectronico());

        return status != ConnectionDB.Status.ERROR ? OK : ERROR;
    }

    public int insertPersona(Persona persona) {
        ConnectionDB.Status status;

        status = conn.executeStoredProcedure(
                "insert_persona",
                persona.getDocumentoIdentidad(),
                persona.getNombre1(),
                persona.getNombre2(),
                persona.getApellido1(),
                persona.getApellido2(),
                persona.getSexo().getId(),
                persona.getFechaNacimiento(),
                persona.getPais().getPais(),
                persona.getTelefono(),
                persona.getCorreoElectronico());

        return status != ConnectionDB.Status.ERROR ? OK : ERROR;
    }

    public int updateUsuario(Usuario usuario) {
        int updatePersona;

        updatePersona = updatePersona(usuario);

        if (updatePersona == OK) {
            ConnectionDB.Status status = conn.executeStoredProcedure(
                    "update_usuario",
                    usuario.getUsuarioId(),
                    usuario.getTipoUsuario().getId(),
                    usuario.isActivo());

            return status != ConnectionDB.Status.OK ? ERROR : OK;
        } else {
            return ERROR;
        }
    }

    public int insertUsuario(Usuario usuario) {
        Persona persona;
        ConnectionDB.Status statusInsertUsuario;

        persona = getPersona(usuario.getDocumentoIdentidad());

        if (persona == null) {
            insertPersona(usuario);
            persona = getPersona(usuario.getDocumentoIdentidad());
        }

        System.out.println(persona);

        statusInsertUsuario = conn.executeStoredProcedure(
                "insert_usuario",
                persona.getId(),
                usuario.getTipoUsuario().getId(),
                usuario.isActivo());

        return ConnectionDB.Status.ERROR != statusInsertUsuario ? OK : ERROR;
    }

    public int updateComida(Comida comida) {
        ConnectionDB.Status status;

        status = conn.executeStoredProcedure(
                "update_comida",
                comida.getId(),
                comida.getInicio(),
                comida.getFin());

        return status != ConnectionDB.Status.OK ? ERROR : OK;
    }

    public int updateComidas(List<Comida> comidas) {
        int updateComida;

        updateComida = OK;

        if (!comidas.isEmpty()) {
            try {
                conn.startTransaction();

                for (Comida comida : comidas) {
                    updateComida = updateComida(comida);
                    System.out.println(comida);
                    System.out.println(comida.getInicio());
                    System.out.println(comida.getFin());

                    if (updateComida != OK) {
                        break;
                    }
                }

                if (updateComida == OK) {
                    conn.commit();
                }else{
                    conn.rollback();
                }
            } catch (SQLException ex) {
                updateComida = ERROR;
                Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }            

        return updateComida;
    }

    public Timestamp getTimestamp() {
        ResultSet rs;
        Timestamp timestamp = null;

        rs = conn.executeStoredProcedureWithResultSet("select_current_timestamp");

        try {
            if (rs.next()) {
                timestamp = rs.getTimestamp("cur_timestamp");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return timestamp;
    }
}
