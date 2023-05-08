/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public final class ConnectionDB {

    private Connection conn;
    private String host;
    private String port;
    private String user;
    private String password;
    private String database;

    public enum Status {
        OK,
        ERROR,
        EXIST
    }

    //Constructors
    public ConnectionDB() {
        this("localhost", "3306", "user", "password", "database");
    }

    public ConnectionDB(String host, String port, String user, String password, String database) {
        setHost(host);
        setPort(port);
        setUser(user);
        setPassword(password);
        setDatabase(database);
    }

    //Setters
    public void setHost(String host) {
        this.host = host.trim();
    }

    public void setPort(String port) {
        this.port = ":" + port.trim();
    }

    public void setUser(String user) {
        this.user = user.trim();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public void setDatabase(String database) {
        this.database = "/" + database.trim();
    }

    //Getters
    public Connection getConnection() {
        return conn;
    }

    //Actions
    public void open() {
        String url = String.format("jdbc:mysql://%s%s%s", host, port, database);
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Falla al abrir la conexión");
            }
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Falla al cerrar la conexión");
            }
        }
    }

    public Status executeStoredProcedure(String storeProcedure, Object... params) {
        String query;
        CallableStatement cst;
        Status status;

        query = String.format(
                "{CALL %s(%s%s)}",
                storeProcedure,
                params.length > 0 ? "?" : "",
                params.length > 1 ? ",?".repeat(params.length - 1) : ""
        );
        
        try {
            cst = conn.prepareCall(query);            
            
            for (int i = 0; i < params.length; i++) {

                if (params[i] != null) {
                    switch (params[i].getClass().getTypeName()) {
                        case "java.lang.String":
                            cst.setString(i + 1, (String) params[i]);
                            break;
                        case "java.lang.Integer":
                            cst.setInt(i + 1, (int) params[i]);
                            break;
                        case "java.lang.Double":
                            cst.setDouble(i + 1, (double) params[i]);
                            break;
                        case "java.lang.Float":
                            cst.setFloat(i + 1, (float) params[i]);
                            break;
                        case "java.lang.Boolean":
                            cst.setBoolean(i + 1, (boolean) params[i]);
                            break;
                        case "java.sql.Date":
                            cst.setDate(i + 1, (Date) params[i]);                        
                            break;
                        case "java.util.Date":
                            cst.setDate(i + 1, new Date(((java.util.Date) params[i]).getTime()));
                            break;
                        case "java.sql.Timestamp":
                            cst.setTimestamp(i + 1, (Timestamp) params[i]);
                            break;    
                        case "java.sql.Time":
                            cst.setTime(i + 1, (Time) params[i]);
                    }                    
                } else {
                    cst.setNull(i + 1, -1);
                }
            }
            
            if (cst.execute()) {
                status = Status.EXIST;                
            } else {
                status = Status.OK;
            }
        } catch (SQLException ex) {
            status = Status.ERROR;
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return status;
    }

    /**
     * Devuelve el resultado de una consulta de la base de datos mediante usando un procedimiento almacenado.
     * @param storeProcedure Nombre del procedimiento almacenado
     * @param params Parámetros del procedimiento almacenado (opcional)
     * @return Objeto ResultSet con el resultado de la consulta.
     */
    public ResultSet executeStoredProcedureWithResultSet(String storeProcedure, Object... params) {
        String query;
        CallableStatement cst;
        ResultSet rs = null;

        query = String.format(
                "{CALL %s(%s%s)}",
                storeProcedure,
                params.length > 0 ? "?" : "",
                params.length > 1 ? ",?".repeat(params.length - 1) : ""
        );

        try {
            cst = conn.prepareCall(query);

            for (int i = 0; i < params.length; i++) {

                if (params[i] != null) {
                    switch (params[i].getClass().getTypeName()) {
                        case "java.lang.String":
                            cst.setString(i + 1, (String) params[i]);
                            break;
                        case "java.lang.Integer":
                            cst.setInt(i + 1, (int) params[i]);
                            break;
                        case "java.lang.Double":
                            cst.setDouble(i + 1, (double) params[i]);
                            break;
                        case "java.lang.Float":
                            cst.setFloat(i + 1, (float) params[i]);
                            break;
                        case "java.lang.Boolean":
                            cst.setBoolean(i + 1, (boolean) params[i]);
                            break;
                        case "java.sql.Date":
                            cst.setDate(i + 1, (Date) params[i]);
                            break;
                        case "java.util.Date":
                            cst.setDate(i + 1, new Date(((java.util.Date) params[i]).getTime()));
                            break;
                        case "java.sql.Timestamp":
                            cst.setTimestamp(i + 1, (Timestamp) params[i]);
                            break;                        
                        case "java.sql.Time":
                            cst.setTime(i + 1, (Time) params[i]);
                    }
                } else {
                    cst.setNull(i + 1, -1);                                        
                }

            }

            if (cst.execute()) {
                rs = cst.getResultSet();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rs;
    }

    public void startTransaction() throws SQLException {
        conn.setAutoCommit(false);        
    }

    public void commit() throws SQLException {
        conn.commit();
        conn.setAutoCommit(true);
    }

    public void rollback() throws SQLException {
        conn.rollback();
        conn.setAutoCommit(true);
    }
}
