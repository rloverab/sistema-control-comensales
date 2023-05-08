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
package aplicacion;

import clases.Comida;
import clases.Controls;
import clases.Item;
import clases.Queries;
import clases.Reports;
import clases.StringTools;
import clases.TipoUsuario;
import clases.UsuarioAtendido;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Component;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public class IFrameConsulta extends javax.swing.JInternalFrame {

    private final Queries queries;
    private final Reports reports;
    private List<UsuarioAtendido> usuariosAtendidos;

    /**
     * Creates new form UsuariosAtendidos
     *
     * @param queries
     * @param reports
     */
    public IFrameConsulta(Queries queries, Reports reports) {
        initComponents();
        
        Component[] components;

        this.queries = queries;
        this.reports = reports;
        usuariosAtendidos = new ArrayList<>();
        
        components = dateInicial.getComponents();
        
        for(Component component : components){
            if (component.getClass().toString().contains("JTextFieldDateEditor")) {
                JTextFieldDateEditor textField;

                textField = (JTextFieldDateEditor) component;
                textField.setEditable(false);

                break;
            }
        }
        
        components = dateFinal.getComponents();
        
        for(Component component : components){
            if (component.getClass().toString().contains("JTextFieldDateEditor")) {
                JTextFieldDateEditor textField;

                textField = (JTextFieldDateEditor) component;
                textField.setEditable(false);

                break;
            }
        }
        
        prepareWindow();
    }

    private void prepareWindow() {
        txtDocumentoIdentidad.setText(null);
        txtNombres.setText(null);
        txtApellidos.setText(null);
        fillComboBoxUsuarios();
        fillComboBoxComidas();
        prepareTableResultado();
    }

    private void fillComboBoxUsuarios() {
        List<TipoUsuario> tiposUsuario;
        List<Item> items;

        tiposUsuario = queries.getTiposUsuario();
        items = new ArrayList<>();

        tiposUsuario.forEach(tipoUsuario -> {
            items.add(new Item(tipoUsuario));
        });

        Controls.fillComboBoxItem(cbxUsuarios, items, "Todos");
        cbxUsuarios.setSelectedIndex(0);
    }

    private void fillComboBoxComidas() {
        List<Comida> comidas;
        List<Item> items;

        comidas = queries.getComidas();
        items = new ArrayList<>();

        comidas.forEach(comida -> {
            items.add(new Item(comida));
        });

        Controls.fillComboBoxItem(cbxComidas, items, "Todos");
        cbxComidas.setSelectedIndex(0);
    }

    private void fillTableResultado() {
        DefaultTableModel dtm;

        dtm = (DefaultTableModel) tblResultado.getModel();
        dtm.setRowCount(0);

        usuariosAtendidos.forEach(usuarioAtendido -> {
            Object[] objects;

            objects = new Object[]{
                usuarioAtendido.getDocumentoIdentidad(),
                StringTools
                        .proper(String
                                .format(
                                        "%s%s, %s%s", 
                                        usuarioAtendido.getApellido1(),
                                        usuarioAtendido.getApellido2() != null ? " " + usuarioAtendido.getApellido2() : "",
                                        usuarioAtendido.getNombre1(),
                                        usuarioAtendido.getNombre2() != null ? " " + usuarioAtendido.getNombre2() : "")),
                usuarioAtendido.getTipoUsuario(),
                usuarioAtendido.getComida().getComida(),
                usuarioAtendido.getFechaHoraAtencion("dd/MM/yyyy hh:mm:ss a")
            };

            dtm.addRow(objects);
        });
    }

    private void prepareTableResultado() {
        int[] columnWidths = new int[]{120, -1, 80, 80, 160};
        int rowHeight = 21;

        TableColumnModel tcm = tblResultado.getColumnModel();
        TableColumnModel tcmh = tblResultado.getTableHeader().getColumnModel();
        DefaultTableModel dtm = (DefaultTableModel) tblResultado.getModel();
        tblResultado.setRowHeight(rowHeight);

        for (int i = 0; i < columnWidths.length; i++) {
            if (columnWidths[i] >= 0) {
                tcm.getColumn(i).setPreferredWidth(columnWidths[i]);
                tcm.getColumn(i).setMaxWidth(columnWidths[i]);
                tcm.getColumn(i).setMinWidth(columnWidths[i]);
                tcmh.getColumn(i).setPreferredWidth(columnWidths[i]);
                tcmh.getColumn(i).setMaxWidth(columnWidths[i]);
                tcmh.getColumn(i).setMinWidth(columnWidths[i]);
            }
        }

        dtm.setRowCount(0);
    }

    private void search() {
        String documentoIdentidad;
        String nombres;
        String apellidos;
        Integer tipoUsuarioId;
        Integer comidaId;
        Timestamp fechaInicial = null;
        Timestamp fechaFinal = null;

        documentoIdentidad = txtDocumentoIdentidad.getText().isBlank() ? null : txtDocumentoIdentidad.getText().trim();
        nombres = txtNombres.getText().isBlank() ? null : txtNombres.getText().trim();
        apellidos = txtApellidos.getText().isBlank() ? null : txtApellidos.getText().trim();
        tipoUsuarioId = cbxUsuarios.getSelectedIndex() > 0 ? ((TipoUsuario) ((Item) cbxUsuarios.getSelectedItem()).getObject()).getId() : null;
        comidaId = cbxComidas.getSelectedIndex() > 0 ? ((Comida) ((Item) cbxComidas.getSelectedItem()).getObject()).getId() : null;

        if (dateInicial.getDate() != null && dateFinal.getDate() != null) {
            if (dateInicial.getDate().after(dateFinal.getDate())){
                java.util.Date aux;

                aux = dateInicial.getDate();
                dateInicial.setDate(dateFinal.getDate());
                dateFinal.setDate(aux);
            }
        }
        
        if(dateInicial.getCalendar() != null){   
            Calendar calendarInicio;
            calendarInicio = Calendar.getInstance();
            calendarInicio.set(
                    dateInicial.getCalendar().get(Calendar.YEAR), 
                    dateInicial.getCalendar().get(Calendar.MONTH), 
                    dateInicial.getCalendar().get(Calendar.DAY_OF_MONTH), 
                    0, 
                    0, 
                    0);
            calendarInicio.set(Calendar.MILLISECOND, 0);
            fechaInicial = new Timestamp(calendarInicio.getTimeInMillis());
        }
        
        if(dateFinal.getCalendar() != null){   
            Calendar calendarFinal;
            calendarFinal = Calendar.getInstance();
            calendarFinal.set(
                    dateFinal.getCalendar().get(Calendar.YEAR), 
                    dateFinal.getCalendar().get(Calendar.MONTH), 
                    dateFinal.getCalendar().get(Calendar.DAY_OF_MONTH), 
                    23, 
                    59, 
                    59);
            calendarFinal.set(Calendar.MILLISECOND, 999);
            fechaFinal = new Timestamp(calendarFinal.getTimeInMillis());
        }
        
        if(dateFinal.getCalendar() != null){
            dateFinal.getCalendar().set(Calendar.HOUR, 23);
            dateFinal.getCalendar().set(Calendar.MINUTE, 59);
            dateFinal.getCalendar().set(Calendar.SECOND, 59);
            dateFinal.getCalendar().set(Calendar.MILLISECOND, 999);
        }

        usuariosAtendidos = queries
                .getUsuariosAtendidos(
                        documentoIdentidad,
                        nombres,
                        apellidos,
                        tipoUsuarioId,
                        comidaId,
                        fechaInicial,
                        fechaFinal);
        fillTableResultado();
    }
    
    private void export(){
        String documentoIdentidad;
        String nombres;
        String apellidos;
        Integer tipoUsuarioId;
        Integer comidaId;
        Timestamp fechaInicial = null;
        Timestamp fechaFinal = null;

        documentoIdentidad = txtDocumentoIdentidad.getText().isBlank() ? null : txtDocumentoIdentidad.getText().trim();
        nombres = txtNombres.getText().isBlank() ? null : txtNombres.getText().trim();
        apellidos = txtApellidos.getText().isBlank() ? null : txtApellidos.getText().trim();
        tipoUsuarioId = cbxUsuarios.getSelectedIndex() > 0 ? ((TipoUsuario) ((Item) cbxUsuarios.getSelectedItem()).getObject()).getId() : null;
        comidaId = cbxComidas.getSelectedIndex() > 0 ? ((Comida) ((Item) cbxComidas.getSelectedItem()).getObject()).getId() : null;

        if (dateInicial.getDate() != null && dateFinal.getDate() != null) {
            if (dateInicial.getDate().after(dateFinal.getDate())){
                java.util.Date aux;

                aux = dateInicial.getDate();
                dateInicial.setDate(dateFinal.getDate());
                dateFinal.setDate(aux);
            }
        }
        
        if(dateInicial.getCalendar()!= null){   
            Calendar calendarInicio;
            calendarInicio = Calendar.getInstance();
            calendarInicio.set(
                    dateInicial.getCalendar().get(Calendar.YEAR), 
                    dateInicial.getCalendar().get(Calendar.MONTH), 
                    dateInicial.getCalendar().get(Calendar.DAY_OF_MONTH), 
                    0, 
                    0, 
                    0);
            calendarInicio.set(Calendar.MILLISECOND, 0);
            fechaInicial = new Timestamp(calendarInicio.getTimeInMillis());
        }
        
        if(dateFinal.getCalendar()!= null){   
            Calendar calendarFinal;
            calendarFinal = Calendar.getInstance();
            calendarFinal.set(
                    dateFinal.getCalendar().get(Calendar.YEAR), 
                    dateFinal.getCalendar().get(Calendar.MONTH), 
                    dateFinal.getCalendar().get(Calendar.DAY_OF_MONTH), 
                    23, 
                    59, 
                    59);
            calendarFinal.set(Calendar.MILLISECOND, 999);
            fechaFinal = new Timestamp(calendarFinal.getTimeInMillis());
        }
        
        if(dateFinal.getCalendar() != null){
            dateFinal.getCalendar().set(Calendar.HOUR, 23);
            dateFinal.getCalendar().set(Calendar.MINUTE, 59);
            dateFinal.getCalendar().set(Calendar.SECOND, 59);
            dateFinal.getCalendar().set(Calendar.MILLISECOND, 999);
        }
        
        reports.generateReportUsuariosAtendidos(documentoIdentidad, nombres, apellidos, tipoUsuarioId, comidaId, fechaInicial, fechaFinal);
    }

    private void reset(){
        txtDocumentoIdentidad.setText(null);
        txtNombres.setText(null);
        txtApellidos.setText(null);
        cbxUsuarios.setSelectedIndex(0);
        cbxComidas.setSelectedIndex(0);
        dateInicial.setDate(null);
        dateFinal.setDate(null);
        prepareTableResultado();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbxUsuarios = new javax.swing.JComboBox<>();
        txtApellidos = new javax.swing.JTextField();
        txtNombres = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDocumentoIdentidad = new javax.swing.JTextField();
        dateInicial = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnReiniciar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cbxComidas = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        dateFinal = new com.toedter.calendar.JDateChooser();
        btnExportar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResultado = new javax.swing.JTable();
        btnCerrar = new javax.swing.JButton();

        setTitle("Consulta");
        setMaximumSize(new java.awt.Dimension(800, 590));
        setMinimumSize(new java.awt.Dimension(800, 590));
        setPreferredSize(new java.awt.Dimension(800, 590));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros de búsqueda"));

        jLabel1.setText("Nombre");

        jLabel2.setText("Apellido");

        jLabel3.setText("Usuario");

        cbxUsuarios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtApellidos.setText("jTextField1");
        txtApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidosKeyTyped(evt);
            }
        });

        txtNombres.setText("jTextField2");
        txtNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombresKeyTyped(evt);
            }
        });

        jLabel4.setText("Documento de identidad");

        txtDocumentoIdentidad.setText("jTextField3");
        txtDocumentoIdentidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDocumentoIdentidadKeyTyped(evt);
            }
        });

        dateInicial.setDateFormatString("dd/MM/yyyy");

        jLabel5.setText("Desde");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnReiniciar.setText("Reiniciar");
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });

        jLabel8.setText("Servicio");

        cbxComidas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Hasta");

        dateFinal.setDateFormatString("dd/MM/yyyy");

        btnExportar.setText("Exportar");
        btnExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDocumentoIdentidad, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombres)
                            .addComponent(txtApellidos))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(btnExportar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnReiniciar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxUsuarios, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbxComidas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateInicial, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(dateFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtDocumentoIdentidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dateInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(cbxUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbxComidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBuscar)
                            .addComponent(btnReiniciar)
                            .addComponent(btnExportar))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblResultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "DI", "Usuario", "Tipo de usuario", "Servicio", "Fecha/Hora"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblResultado);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCerrar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        search();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        reset();
    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void btnExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarActionPerformed
        export();
    }//GEN-LAST:event_btnExportarActionPerformed

    private void txtDocumentoIdentidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocumentoIdentidadKeyTyped
        char key;
        
        key = evt.getKeyChar();
        
        if(!StringTools.isAlphanumeric(key) || txtDocumentoIdentidad.getText().length() >= 15){
            evt.consume();            
        }
    }//GEN-LAST:event_txtDocumentoIdentidadKeyTyped

    private void txtNombresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombresKeyTyped
        char key;
        
        key = evt.getKeyChar();
        
        if(!StringTools.isAlphabetic(key, true) && key != ' '){
            evt.consume();            
        }
    }//GEN-LAST:event_txtNombresKeyTyped

    private void txtApellidosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidosKeyTyped
        char key;
        
        key = evt.getKeyChar();
        
        if(!StringTools.isAlphabetic(key, true) && key != ' '){
            evt.consume();            
        }
    }//GEN-LAST:event_txtApellidosKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnExportar;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JComboBox<String> cbxComidas;
    private javax.swing.JComboBox<String> cbxUsuarios;
    private com.toedter.calendar.JDateChooser dateFinal;
    private com.toedter.calendar.JDateChooser dateInicial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblResultado;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtDocumentoIdentidad;
    private javax.swing.JTextField txtNombres;
    // End of variables declaration//GEN-END:variables
}
