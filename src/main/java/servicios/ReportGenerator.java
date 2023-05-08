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
package servicios;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author roger
 */
public class ReportGenerator {
    public static void generateReportFromPrecompiled(
            ConnectionDB conn,
            String reportSource,
            Map<String, Object> params) {
        JasperReport jasperReport;
        File jasperFile;

        jasperFile = new File(reportSource);

        try {
            jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
            generateReport(conn, jasperReport, params);
        } catch (JRException ex) {
            Logger.getLogger(ReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void generateReportFromTemplate(
            ConnectionDB conn,
            String reportSource,
            Map<String, Object> params) {
        JasperReport jasperReport;

        try {
            jasperReport = JasperCompileManager.compileReport(reportSource);
            generateReport(conn, jasperReport, params);
        } catch (JRException ex) {
            Logger.getLogger(ReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void generateReport(
            ConnectionDB conn,
            JasperReport jasperReport,
            Map<String, Object> params) throws JRException {
        JasperPrint jasperPrint;
        
        if (jasperReport != null) {
            jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    params,
                    conn.getConnection());

            JasperViewer.viewReport(jasperPrint, false);            
        }
    }
}
