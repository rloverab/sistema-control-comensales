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
import java.util.Objects;

/**
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public class Usuario extends Persona implements Cloneable{
    private int usuarioId;
    private TipoUsuario tipoUsuario;
    private boolean activo;

    public Usuario(TipoUsuario tipoUsuario, boolean activo, int id, String documentoIdentidad, String nombre1, String nombre2, String apellido1, String apellido2, Sexo sexo, Date fechaNacimiento, String telefonoMovil, String correoElectronico) {
        super(id, documentoIdentidad, nombre1, nombre2, apellido1, apellido2, sexo, fechaNacimiento, telefonoMovil, correoElectronico);
        this.tipoUsuario = tipoUsuario;
        this.activo = activo;
    }
    
    public Usuario(){
        super();
        usuarioId = 0;
        tipoUsuario = null;
        activo = true;        
    }

    public int getPersonaId() {
        return super.getId();
    }
    
    public void setPersonaId(int personaId){
        super.setId(personaId);
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;          
    }

    @Override
    public Object clone() throws CloneNotSupportedException {        
        return super.clone(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.usuarioId;
        hash = 37 * hash + Objects.hashCode(this.tipoUsuario);
        hash = 37 * hash + (this.activo ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (this.usuarioId != other.usuarioId) {
            return false;
        }
        if (this.activo != other.activo) {
            return false;
        }
        return Objects.equals(this.tipoUsuario, other.tipoUsuario);
    }

    
    
    
}
