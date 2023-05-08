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

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public class UsuarioAtendido extends Usuario{
    private Comida comida;
    private Timestamp fechaHoraAtencion;

    public UsuarioAtendido(
            Comida comida, 
            Timestamp fechaHoraAtencion, 
            TipoUsuario tipoUsuario, 
            boolean activo, 
            int id, 
            String documentoIdentidad, 
            String nombre1, 
            String nombre2, 
            String apellido1, 
            String apellido2, 
            Sexo sexo, 
            Date fechaNacimiento, 
            String telefonoMovil, 
            String correoElectronico) {
        super(
                tipoUsuario, 
                activo, 
                id, 
                documentoIdentidad, 
                nombre1, 
                nombre2, 
                apellido1, 
                apellido2, 
                sexo, 
                fechaNacimiento, 
                telefonoMovil, 
                correoElectronico);
        this.comida = comida;
        this.fechaHoraAtencion = fechaHoraAtencion;
    }
    
    public UsuarioAtendido(){
        super();
        comida = null;
        fechaHoraAtencion = null;
    }

    public Comida getComida() {
        return comida;
    }

    public void setComida(Comida comida) {
        this.comida = comida;
    }

    public Timestamp getFechaHoraAtencion() {
        return fechaHoraAtencion;
    }
    
    public String getFechaHoraAtencion(String pattern){        
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        
        return sdf.format(fechaHoraAtencion);                
    }

    public void setFechaHoraAtencion(Timestamp fechaHoraAtencion) {
        this.fechaHoraAtencion = fechaHoraAtencion;
    }
    
    
}
