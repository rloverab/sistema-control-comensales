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
package aplicacion;

import clases.Controls;
import clases.Formatter;
import clases.Item;
import clases.Pais;
import clases.Queries;
import clases.Sexo;
import clases.StringTools;
import clases.TipoUsuario;
import clases.Usuario;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public class IFrameUsuariosServicio extends javax.swing.JInternalFrame {

    private final Queries queries;
    private Usuario usuario;
    private boolean isNuevo;

    /**
     * Creates new form IFrameUsuarios
     *
     * @param queries
     */
    public IFrameUsuariosServicio(Queries queries) {
        initComponents();
        this.queries = queries;
        usuario = null;
        prepareFields();
        fillFields();
        isNuevo = true;
    }

    private void prepareFields() {
        txtDocumentoIdentidad.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                String text;

                text = txtDocumentoIdentidad.getText();

                if (usuario != null) {
                    usuario.setDocumentoIdentidad(text);
                }
            }
        });

        txtNombre1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                String text;

                text = txtNombre1.getText();

                if (usuario != null) {
                    usuario.setNombre1(text);
                }
            }
        });

        txtNombre2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                String text;

                text = txtNombre2.getText();

                if (usuario != null) {
                    usuario.setNombre2(text);
                }
            }
        });

        txtApellido1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                String text;

                text = txtApellido1.getText();

                if (usuario != null) {
                    usuario.setApellido1(text);
                }
            }
        });

        txtApellido2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                String text;

                text = txtApellido2.getText();

                if (usuario != null) {
                    usuario.setApellido2(text);
                }
            }
        });

        txtTelefono.setFormatterFactory(new Formatter("(####) ###.##.##", '_'));
        txtTelefono.setFocusLostBehavior(JFormattedTextField.PERSIST);

        txtTelefono.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                String text;

                text = txtTelefono.getText().trim();
                text = text.replace("(", "");
                text = text.replace(")", "");
                text = text.replace(" ", "");
                text = text.replace("_", "");
                text = text.replace(".", "");

                if (usuario != null) {
                    usuario.setTelefono(text);
                }
            }
        });

        txtCorreoElectronico.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                changedUpdate(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                String text;

                text = txtCorreoElectronico.getText().trim();

                if (usuario != null) {
                    usuario.setCorreoElectronico(text);
                }
            }
        });

        cbxSexos.addActionListener((ActionEvent ae) -> {
            if (usuario != null) {
                if (cbxSexos.getSelectedIndex() >= 0) {
                    usuario.setSexo(((Sexo) ((Item) cbxSexos.getSelectedItem()).getObject()));
                } else {
                    usuario.setSexo(null);
                }

            }
        });

        cbxTiposUsuario.addActionListener((ActionEvent ae) -> {
            if (usuario != null) {
                if (cbxTiposUsuario.getSelectedIndex() >= 0) {
                    usuario.setTipoUsuario((TipoUsuario) ((Item) cbxTiposUsuario.getSelectedItem()).getObject());
                } else {
                    usuario.setTipoUsuario(null);
                }

            }
        });

        cbxPaises.addActionListener((ActionEvent ae) -> {
            if (usuario != null) {
                if (cbxPaises.getSelectedIndex() >= 0) {
                    usuario.setPais((Pais) ((Item) cbxPaises.getSelectedItem()).getObject());
                } else {
                    usuario.setPais(null);
                }

            }
        });

        chkUsuarioActivo.addActionListener((ActionEvent ae) -> {
            usuario.setActivo(chkUsuarioActivo.isSelected());
        });

        Component[] components = dateFechaNacimiento.getComponents();

        for (Component component : components) {
            if (component.getClass().toString().contains("JTextFieldDateEditor")) {
                JTextFieldDateEditor textField;

                textField = (JTextFieldDateEditor) component;
                textField.setEditable(false);
                textField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        changedUpdate(e);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        changedUpdate(e);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        if (usuario != null) {
                            if (dateFechaNacimiento.getDate() != null) {
                                usuario.setFechaNacimiento(new Date(dateFechaNacimiento.getCalendar().getTimeInMillis()));
                            } else {
                                usuario.setFechaNacimiento(null);
                            }
                        }
                    }
                });

                break;
            }
        }
    }

    private void fillFields() {
        boolean isNullUsuario;
        TipoUsuario tipoUsuario;
        Sexo sexo;
        Pais pais;

        isNullUsuario = usuario == null;
        txtDocumentoIdentidad.setText(!isNullUsuario ? usuario.getDocumentoIdentidad() : "");
        //txtDocumentoIdentidad.setEnabled(isNullUsuario);
        btnComprobar.setEnabled(isNullUsuario);
        txtNombre1.setText(!isNullUsuario ? usuario.getNombre1() : "");
        txtNombre1.setEnabled(!isNullUsuario);
        txtNombre2.setText(!isNullUsuario ? usuario.getNombre2() : "");
        txtNombre2.setEnabled(!isNullUsuario);
        txtApellido1.setText(!isNullUsuario ? usuario.getApellido1() : "");
        txtApellido1.setEnabled(!isNullUsuario);
        txtApellido2.setText(!isNullUsuario ? usuario.getApellido2() : "");
        txtApellido2.setEnabled(!isNullUsuario);
        dateFechaNacimiento.setDate(!isNullUsuario && usuario.getFechaNacimiento() != null ? (new java.util.Date(usuario.getFechaNacimiento().getTime())) : null);
        dateFechaNacimiento.setEnabled(!isNullUsuario);
        txtTelefono.setText(!isNullUsuario ? usuario.getTelefono() : "");
        txtTelefono.setEnabled(!isNullUsuario);
        txtCorreoElectronico.setText(!isNullUsuario ? usuario.getCorreoElectronico() : "");
        txtCorreoElectronico.setEnabled(!isNullUsuario);
        chkUsuarioActivo.setSelected(!isNullUsuario ? usuario.isActivo() : false);
        chkUsuarioActivo.setEnabled(!isNullUsuario);
        tipoUsuario = !isNullUsuario && usuario.getTipoUsuario() != null ? usuario.getTipoUsuario() : null;
        fillComboBoxTipoUsuario();
        //fillComboBoxPais();

        if (!isNullUsuario && tipoUsuario != null) {
            int itemCount;

            itemCount = cbxTiposUsuario.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                String text;

                cbxTiposUsuario.setSelectedIndex(i);
                text = ((Item) cbxTiposUsuario.getSelectedItem()).getLabel().trim();

                if (text.equalsIgnoreCase(tipoUsuario.getTipoUsuario().trim())) {
                    usuario.setTipoUsuario(tipoUsuario);
                    break;
                }

                cbxTiposUsuario.setSelectedIndex(-1);
            }
        }

        cbxTiposUsuario.setEnabled(!isNullUsuario);
        sexo = !isNullUsuario && usuario.getSexo() != null ? usuario.getSexo() : null;
        fillComboBoxSexo();

        if (!isNullUsuario && sexo != null) {
            int itemCount;

            itemCount = cbxSexos.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                String text;

                cbxSexos.setSelectedIndex(i);
                text = ((Item) cbxSexos.getSelectedItem()).getLabel();

                if (text.equalsIgnoreCase(sexo.getSexo().trim())) {
                    usuario.setSexo(sexo);
                    cbxSexos.setSelectedIndex(i);
                    break;
                }

                cbxSexos.setSelectedIndex(-1);
            }
        }

        cbxSexos.setEnabled(!isNullUsuario);
        pais = !isNullUsuario && usuario.getPais() != null ? usuario.getPais() : null;
        fillComboBoxPais();

        if (!isNullUsuario && pais != null) {
            int itemCount;

            itemCount = cbxPaises.getItemCount();
            System.out.println(itemCount);
            for (int i = 0; i < itemCount; i++) {
                //String text;
                Pais p;

                cbxPaises.setSelectedIndex(i);
                //text = ((Item) cbxPaises.getSelectedItem()).getLabel();
                p = (Pais) ((Item) cbxPaises.getSelectedItem()).getObject();

                //if (text.equalsIgnoreCase(pais.getPais().trim())) {
                //System.out.println(p.getPais());
                if (p.getId() == pais.getId()) {
                    usuario.setPais(pais);
                    cbxPaises.setSelectedIndex(i);
                    break;
                }

                cbxPaises.setSelectedIndex(-1);
            }
        }

        cbxPaises.setEnabled(!isNullUsuario);

        btnGuardar.setEnabled(!isNullUsuario);
    }

    private void fillComboBoxTipoUsuario() {
        List<Item> items;
        List<TipoUsuario> tiposUsuarios;

        items = new ArrayList<>();

        tiposUsuarios = queries.getTiposUsuario();

        tiposUsuarios.forEach(e -> {
            Item item;

            item = new Item(e, e.getTipoUsuario());

            items.add(item);
        });

        Controls.fillComboBoxItem(cbxTiposUsuario, items, null, -1);
    }

    private void fillComboBoxSexo() {
        List<Item> items;
        List<Sexo> sexos;

        items = new ArrayList<>();
        sexos = queries.getSexos();

        sexos.forEach(e -> {
            Item item;

            item = new Item(e, e.getSexo());
            items.add(item);
        });

        Controls.fillComboBoxItem(cbxSexos, items, null, -1);
    }

    private void fillComboBoxPais() {
        List<Item> items;
        List<Pais> paises;

        items = new ArrayList<>();
        paises = queries.getPaises();

        paises.forEach(e -> {
            Item item;

            item = new Item(e, e.getPais());
            items.add(item);
        });

        Controls.fillComboBoxItem(cbxPaises, items, null, 0);
    }

    private boolean isValidUsuario() {
        String message = "";

        if (usuario != null) {
            message += usuario.getDocumentoIdentidad().isBlank() ? "- El documento de identidad es obligatorio.\n" : "";
            message += !usuario.getDocumentoIdentidad().isBlank() && !StringTools.isAlphanumeric(usuario.getDocumentoIdentidad(), false) ? "- El documento de identidad debe contener sólo caracteres alfanuméricos" : "";
            message += usuario.getNombre1().isBlank() ? "- El primer nombre es obligatorio.\n" : "";
            message += !usuario.getNombre1().isBlank() && !StringTools.isAlphabetic(usuario.getNombre1(), true) ? "- El primer nombre debe contener sólo caracteres alfabéticos.\n" : "";
            message += !usuario.getNombre2().isBlank() && !StringTools.isAlphabetic(usuario.getNombre2(), true) ? "- El segundo nombre debe contener sólo caracteres alfabéticos.\n" : "";
            message += usuario.getApellido1().isBlank() ? "- El primer apellido es obligatorio.\n" : "";
            message += !usuario.getApellido1().isBlank() && !StringTools.isAlphabetic(usuario.getApellido1(), true) ? "- El primer apellido debe contener sólo caracteres alfabéticos.\n" : "";
            message += !usuario.getApellido2().isBlank() && !StringTools.isAlphabetic(usuario.getApellido2(), true) ? "- El segundo apellido debe contener sólo caracteres alfabéticos.\n" : "";
            message += usuario.getFechaNacimiento() == null ? "- La fecha de nacimiento es obligatoria.\n" : "";
            message += !usuario.getTelefono().isBlank() && !StringTools.isNumeric(usuario.getTelefono(), false) ? "- El teléfono debe contener sólo caracteres numéricos.\n" : "";
            message += !usuario.getTelefono().isBlank() && usuario.getTelefono().trim().length() < 11 ? "- El teléfono parece estar incompleto.\n" : "";
            message += !usuario.getCorreoElectronico().isBlank() && !StringTools.isEmail(usuario.getCorreoElectronico().trim()) ? "- El correo electrónico parace estar mal escrito.\n" : "";
            message += usuario.getSexo() == null ? "- El campo sexo es obligatorio.\n" : "";
            message += usuario.getTipoUsuario() == null ? "- El tipo de usuario es obligatorio.\n" : "";

            if (!message.isBlank()) {
                JOptionPane.showInternalMessageDialog(
                        this,
                        message,
                        "Campos con problemas",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    private void check() {
        if (txtDocumentoIdentidad.getText().isBlank()) {
            JOptionPane.showInternalMessageDialog(
                    this.txtDocumentoIdentidad,
                    "Este campo es obligatorio",
                    "Documento de identidad",
                    JOptionPane.ERROR_MESSAGE);
            txtDocumentoIdentidad.requestFocus();
            return;
        }

        usuario = queries.getUsuario(txtDocumentoIdentidad.getText().trim().toUpperCase());
        System.out.println(usuario);
        isNuevo = usuario == null;

        if (isNuevo) {
            int response;

            response = JOptionPane
                    .showInternalConfirmDialog(
                            this.txtDocumentoIdentidad,
                            "Usuario no registrado.\n¿Desea registrarlo como un nuevo usuario?",
                            "Usuario no registrado",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                usuario = new Usuario();

                txtDocumentoIdentidad.setText(txtDocumentoIdentidad.getText().trim().toUpperCase());
                txtDocumentoIdentidad.setEnabled(false);
                usuario.setDocumentoIdentidad(txtDocumentoIdentidad.getText());
                //originalUsuario = usuario;
                //originalUsuario = new Usuario();
                //fillFields();
                txtNombre1.requestFocus();                
            } else {
                txtDocumentoIdentidad.requestFocus();
            }
        }/* else {
            try {
                originalUsuario = (Usuario) usuario.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(IFrameUsuariosServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

        fillFields();
    }

    private void reset() {
        usuario = null;
        fillFields();
        txtDocumentoIdentidad.setEnabled(true);
        isNuevo = true;
        txtDocumentoIdentidad.requestFocus();
    }

    private void save() {
        Usuario aux;

        aux = queries.getUsuario(txtDocumentoIdentidad.getText().toUpperCase().trim());

        if (isNuevo) {
            int insertUsuario;

            if (aux != null) {
                JOptionPane.showInternalMessageDialog(
                        this,
                        "Existe un usuario con este documento de identidad asociado.",
                        "Documento de identidad",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            insertUsuario = queries.insertUsuario(usuario);

            if (insertUsuario == Queries.OK) {
                JOptionPane.showInternalMessageDialog(
                        this,
                        "Usuario registrado exitósamente.",
                        "Nuevo usuario",
                        JOptionPane.INFORMATION_MESSAGE);
                reset();
            } else {
                System.out.println("Inserción ERROR");
            }
        } else {
            int updateUsuario;

            if (aux != null) {
                if (usuario.getPersonaId() != aux.getPersonaId()) {
                    JOptionPane.showInternalMessageDialog(
                            this,
                            "Existe un usuario con este documento de identidad asociado.",
                            "Documento de identidad",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            updateUsuario = queries.updateUsuario(usuario);

            if (updateUsuario == Queries.OK) {
                JOptionPane.showInternalMessageDialog(
                        this,
                        "Datos actualizados exitósamente.",
                        "Actualización de datos de usuario",
                        JOptionPane.INFORMATION_MESSAGE);
                usuario = queries.getUsuario(usuario.getDocumentoIdentidad());
                fillFields();
            }
        }
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
        lblDocumentoIdentidad = new javax.swing.JLabel();
        txtDocumentoIdentidad = new javax.swing.JTextField();
        btnComprobar = new javax.swing.JButton();
        lblPrimero = new javax.swing.JLabel();
        lblSegundo = new javax.swing.JLabel();
        lblNombres = new javax.swing.JLabel();
        lblApellidos = new javax.swing.JLabel();
        txtNombre1 = new javax.swing.JTextField();
        txtApellido1 = new javax.swing.JTextField();
        txtNombre2 = new javax.swing.JTextField();
        txtApellido2 = new javax.swing.JTextField();
        lblSexo = new javax.swing.JLabel();
        cbxSexos = new javax.swing.JComboBox<>();
        lblFechaNacimiento = new javax.swing.JLabel();
        dateFechaNacimiento = new com.toedter.calendar.JDateChooser();
        lblTipoUsuario = new javax.swing.JLabel();
        cbxTiposUsuario = new javax.swing.JComboBox<>();
        lblTelefono = new javax.swing.JLabel();
        lblCorreoElectronico = new javax.swing.JLabel();
        chkUsuarioActivo = new javax.swing.JCheckBox();
        txtTelefono = new javax.swing.JFormattedTextField();
        txtCorreoElectronico = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        cbxPaises = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        btnReiniciar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setTitle("Usuarios");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblDocumentoIdentidad.setText("Documento de identidad");

        txtDocumentoIdentidad.setText("jTextField1");
        txtDocumentoIdentidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocumentoIdentidadActionPerformed(evt);
            }
        });
        txtDocumentoIdentidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDocumentoIdentidadKeyTyped(evt);
            }
        });

        btnComprobar.setText("Comprobar");
        btnComprobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprobarActionPerformed(evt);
            }
        });

        lblPrimero.setText("Primer");

        lblSegundo.setText("Segundo");

        lblNombres.setText("Nombres");

        lblApellidos.setText("Apellidos");

        txtNombre1.setText("jTextField2");
        txtNombre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombre1ActionPerformed(evt);
            }
        });
        txtNombre1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombre1KeyTyped(evt);
            }
        });

        txtApellido1.setText("jTextField4");
        txtApellido1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellido1ActionPerformed(evt);
            }
        });
        txtApellido1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellido1KeyTyped(evt);
            }
        });

        txtNombre2.setText("jTextField3");
        txtNombre2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombre2ActionPerformed(evt);
            }
        });
        txtNombre2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombre2KeyTyped(evt);
            }
        });

        txtApellido2.setText("jTextField4");
        txtApellido2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellido2ActionPerformed(evt);
            }
        });
        txtApellido2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellido2KeyTyped(evt);
            }
        });

        lblSexo.setText("Sexo");

        cbxSexos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblFechaNacimiento.setText("Fecha de nacimiento");

        dateFechaNacimiento.setDateFormatString("dd/MM/yyyy");

        lblTipoUsuario.setText("Tipo de usuario");

        cbxTiposUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblTelefono.setText("Teléfono");

        lblCorreoElectronico.setText("Correo electrónico");

        chkUsuarioActivo.setText("Usuario activo");

        txtTelefono.setText("jFormattedTextField1");

        txtCorreoElectronico.setText("jFormattedTextField2");

        jLabel1.setText("Pais");

        cbxPaises.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblSexo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbxSexos, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblFechaNacimiento))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblCorreoElectronico)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateFechaNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCorreoElectronico)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbxPaises, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblNombres, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                    .addComponent(lblApellidos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtApellido1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtApellido2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblPrimero))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblSegundo)
                                            .addComponent(txtNombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(236, 236, 236)
                                        .addComponent(chkUsuarioActivo))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblDocumentoIdentidad)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDocumentoIdentidad, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnComprobar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblTipoUsuario)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxTiposUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDocumentoIdentidad)
                    .addComponent(txtDocumentoIdentidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnComprobar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrimero)
                    .addComponent(lblSegundo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombres)
                    .addComponent(txtNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellido1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellido2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApellidos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSexo)
                        .addComponent(cbxSexos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFechaNacimiento))
                    .addComponent(dateFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbxPaises, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTelefono)
                    .addComponent(lblCorreoElectronico)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCorreoElectronico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTipoUsuario)
                    .addComponent(cbxTiposUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkUsuarioActivo))
                .addGap(12, 12, 12))
        );

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnReiniciar.setText("Reiniciar");
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });

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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnReiniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnReiniciar)
                    .addComponent(btnCerrar))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnComprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComprobarActionPerformed
        check();
    }//GEN-LAST:event_btnComprobarActionPerformed

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        reset();
    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void txtDocumentoIdentidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocumentoIdentidadKeyTyped
        if (!StringTools.isAlphanumeric(evt.getKeyChar()) || txtDocumentoIdentidad.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_txtDocumentoIdentidadKeyTyped

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (isValidUsuario()) {
            save();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtNombre1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombre1KeyTyped
        if (!StringTools.isAlphabetic(evt.getKeyChar(), " -") || txtNombre1.getText().length() >= 25) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNombre1KeyTyped

    private void txtNombre2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombre2KeyTyped
        if (!StringTools.isAlphabetic(evt.getKeyChar(), " -") || txtNombre2.getText().length() >= 25) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNombre2KeyTyped

    private void txtApellido1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellido1KeyTyped
        if (!StringTools.isAlphabetic(evt.getKeyChar(), " -") || txtApellido1.getText().length() >= 25) {
            evt.consume();
        }
    }//GEN-LAST:event_txtApellido1KeyTyped

    private void txtApellido2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellido2KeyTyped
        if (!StringTools.isAlphabetic(evt.getKeyChar(), " -") || txtApellido2.getText().length() >= 25) {
            evt.consume();
        }
    }//GEN-LAST:event_txtApellido2KeyTyped

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void txtNombre1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombre1ActionPerformed
        txtNombre2.requestFocus();
    }//GEN-LAST:event_txtNombre1ActionPerformed

    private void txtDocumentoIdentidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocumentoIdentidadActionPerformed
        check();
    }//GEN-LAST:event_txtDocumentoIdentidadActionPerformed

    private void txtNombre2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombre2ActionPerformed
        txtApellido1.requestFocus();
    }//GEN-LAST:event_txtNombre2ActionPerformed

    private void txtApellido2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellido2ActionPerformed
        cbxSexos.requestFocus();
    }//GEN-LAST:event_txtApellido2ActionPerformed

    private void txtApellido1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellido1ActionPerformed
        txtApellido2.requestFocus();
    }//GEN-LAST:event_txtApellido1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnComprobar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JComboBox<String> cbxPaises;
    private javax.swing.JComboBox<String> cbxSexos;
    private javax.swing.JComboBox<String> cbxTiposUsuario;
    private javax.swing.JCheckBox chkUsuarioActivo;
    private com.toedter.calendar.JDateChooser dateFechaNacimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblCorreoElectronico;
    private javax.swing.JLabel lblDocumentoIdentidad;
    private javax.swing.JLabel lblFechaNacimiento;
    private javax.swing.JLabel lblNombres;
    private javax.swing.JLabel lblPrimero;
    private javax.swing.JLabel lblSegundo;
    private javax.swing.JLabel lblSexo;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTipoUsuario;
    private javax.swing.JTextField txtApellido1;
    private javax.swing.JTextField txtApellido2;
    private javax.swing.JFormattedTextField txtCorreoElectronico;
    private javax.swing.JTextField txtDocumentoIdentidad;
    private javax.swing.JTextField txtNombre1;
    private javax.swing.JTextField txtNombre2;
    private javax.swing.JFormattedTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
