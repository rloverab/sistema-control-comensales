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

import java.sql.Time;
import java.util.Objects;

/**
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public class Comida {

    private int comidaId;
    private String comida;
    private Time inicio;
    private Time fin;

    public Comida() {
        comidaId = 0;
        comida = null;
        inicio = null;
        fin = null;
    }

    public int getId() {
        return comidaId;
    }

    public void setComidaId(int comidaId) {
        this.comidaId = comidaId;
    }

    public String getComida() {
        return comida;
    }

    public void setComida(String comida) {
        this.comida = comida;
    }

    public Time getInicio() {
        return inicio;
    }

    public void setInicio(Time inicio) {
        this.inicio = inicio;
    }

    public Time getFin() {
        return fin;
    }

    public void setFin(Time fin) {
        this.fin = fin;
    }

    @Override
    public String toString() {
        return comida;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.comidaId;
        hash = 53 * hash + Objects.hashCode(this.comida);
        hash = 53 * hash + Objects.hashCode(this.inicio);
        hash = 53 * hash + Objects.hashCode(this.fin);
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
        final Comida other = (Comida) obj;
        if (this.comidaId != other.comidaId) {
            return false;
        }
        if (!Objects.equals(this.comida, other.comida)) {
            return false;
        }
        if (!Objects.equals(this.inicio, other.inicio)) {
            return false;
        }
        return Objects.equals(this.fin, other.fin);
    }
}
