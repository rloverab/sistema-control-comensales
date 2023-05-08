/*
 * Copyright (C) 2022 roger
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

import java.util.Objects;

/**
 * Almacena datos de sexos.
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public class Sexo {
    private int id;
    private String sexo;

    public Sexo(int id, String sexo) {
        this.id = id;
        this.sexo = sexo;
    }

    public int getId() {
        return id;
    }

    public String getSexo() {
        return sexo;
    }

    @Override
    public String toString() {
        return sexo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.sexo);
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
        final Sexo other = (Sexo) obj;
        if (this.id != other.id) {
            return false;
        }
        return Objects.equals(this.sexo, other.sexo);
    }
    
    
}
