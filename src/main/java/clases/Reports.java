/*
 * Copyright (C) 2021 Roger Lovera <roger.lovera>
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

import servicios.ReportGenerator;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import servicios.ConnectionDB;

/**
 *
 * @author Roger Lovera <roger.lovera>
 */
public class Reports {

    private final ConnectionDB conn;

    public Reports(ConnectionDB conn) {
        this.conn = conn;
    }    
    
    public void generateReportUsuariosAtendidos(
            String documentoIdentidad,
            String nombres,
            String apellidos,
            Integer tipoUsuarioId,
            Integer comidaId,
            Timestamp fechaInicial,
            Timestamp fechaFinal) {
        String reportSource;
        Map<String, Object> params;

        reportSource = "./reports/record.jasper";
        
        System.out.println(reportSource);
        params = new HashMap<>();
        params.put("DOCUMENTO_IDENTIDAD", documentoIdentidad);
        params.put("NOMBRES", nombres);
        params.put("APELLIDOS", apellidos);
        params.put("TIPO_USUARIO_ID", tipoUsuarioId);
        params.put("COMIDA_ID", comidaId);
        params.put("FECHA_INICIO", fechaInicial);
        params.put("FECHA_FINAL", fechaFinal);
                
        ReportGenerator.generateReportFromPrecompiled(conn, reportSource, params);
    }
    
    public void generateReportStatistics(            
            Timestamp fechaInicial,
            Timestamp fechaFinal) {
        String pathSubreport = "./reports/";
        String reportSource;
        Map<String, Object> params;

        reportSource = "./reports/statistics.jasper";
        
        System.out.println(reportSource);
        params = new HashMap<>();        
        params.put("DOCUMENTO_IDENTIDAD", null);
        params.put("NOMBRES", null);
        params.put("APELLIDOS", null);
        params.put("TIPO_USUARIO_ID", null);
        params.put("COMIDA_ID", null);
        params.put("FECHA_INICIAL", fechaInicial);
        params.put("FECHA_FINAL", fechaFinal);
        params.put("PATH_SUBREPORT", pathSubreport);
                
        ReportGenerator.generateReportFromPrecompiled(conn, reportSource, params);
    }
}
