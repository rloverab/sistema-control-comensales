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

import java.awt.Component;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Clase con métodos de manipulación de Componentes Swing. Hasta ahora sólo
 * manipula JComboBox y JList.
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public final class Controls {

    public static enum Verificar {
        ES_TEXTO_NO_VACIO,
        ES_LISTA_NO_VACIA,
        ELEMENTO_SELECCIONADO,
        ES_ALFABETICO,
        ES_ALFABETICO_CON_ESPACIOS,
        NO_ES_NUMERICO,
        ES_NUMERO_ENTERO,
        ES_NUMERO_REAL,
        ES_NULO
    }

    /**
     * Llena un JComboBox con datos de un objeto List de String.
     *
     * @param comboBox JComboBox a llenar.
     * @param list Lista de elemento a ingresar en la JComboBox.
     * @param itemInicial Primer ítem de la lista. Asigne null para omitir este
     * ítem.
     */
    public static void fillComboBoxString(
            JComboBox comboBox,
            List<String> list,
            String itemInicial) {

        comboBox.removeAllItems();

        if (itemInicial != null) {
            comboBox.addItem(itemInicial);
        }

        list.forEach(e -> {
            comboBox.addItem(e);
        });
    }
    
    public static void fillComboBoxItem(
            JComboBox comboBox,
            List<Item> list,
            String itemInicial,
            int selectedIndex){
        fillComboBoxItem(comboBox, list, itemInicial);
        if(selectedIndex >= -1 && selectedIndex < comboBox.getItemCount()){
            comboBox.setSelectedIndex(selectedIndex);
        }else{
            comboBox.setSelectedIndex(-1);
        }
    }
    
    public static void fillComboBoxItem(
            JComboBox comboBox,
            List<Item> list,
            String itemInicial){
        comboBox.removeAllItems();

        if (itemInicial != null) {
            comboBox.addItem(new Item(null,itemInicial));
        }
        
        list.forEach(e -> {
            comboBox.addItem(e);
        });
    }

    /**
     * Asigna el valor seleccionado del JComboBox al ToolTip del mismo
     * JComboBox.
     *
     * @param comboBox
     */
    public static void updateComboBoxTooltip(JComboBox comboBox) {
        if (comboBox.getSelectedIndex() > -1) {
            comboBox.setToolTipText(comboBox.getSelectedItem().toString());
        } else {
            comboBox.setToolTipText(null);

        }
    }

    /**
     *
     * @param jList
     * @param list
     */
    public static void fillList(
            JList jList,
            List<String> list) {        
        DefaultListModel dlm = new DefaultListModel();
        jList.setModel(dlm);
        
        list.forEach(e -> {
            dlm.addElement(e);
        });        
    }

    /**
     * Elimina todos los elementos de una JList
     *
     * @param list
     */
    public static void removeAllItemsList(JList list) {
        DefaultListModel dfm = (DefaultListModel) list.getModel();

        if (!dfm.isEmpty()) {
            dfm.removeAllElements();
        }
    }

    /**
     * Mueve los elementos seleccionados en la lista de origen a otra lista.
     *
     * @param originList Lista de origen.
     * @param destinationList Lista destino.
     */
    public static void moveElementJList(
            JList originList,
            JList destinationList) {
        Object item;
        DefaultListModel originModel;
        DefaultListModel destinationModel;
        
        originModel = (DefaultListModel) originList.getModel();
        destinationModel = (DefaultListModel) destinationList.getModel();
        
        for (Iterator selected = originList.getSelectedValuesList().iterator(); selected.hasNext();) {
            item = selected.next();

            destinationModel.addElement(item);
            originModel.removeElement(item);
        }
    }
    
    public static void prepareTable(JTable table, String[] header, int[] widths){
        DefaultTableModel dtm;

        dtm = new DefaultTableModel(header, 0);                
        
        table.setModel(dtm);        

        for (int i = 0; i < widths.length; i++) {
            if (widths[i] >= 0) {
                table.getColumnModel().getColumn(i).setMaxWidth(widths[i]);
                table.getColumnModel().getColumn(i).setMinWidth(widths[i]);
                table.getTableHeader().getColumnModel().getColumn(i).setMaxWidth(widths[i]);
                table.getTableHeader().getColumnModel().getColumn(i).setMinWidth(widths[i]);
            }
        }
        
        dtm.setRowCount(0);                
    }

    public static void fillTable(JTable table, List<Object[]> rows){
        DefaultTableModel dtm;
        
        removeAllRowsTable(table);
        
        dtm = (DefaultTableModel) table.getModel();
        
        if (rows != null) {
            rows.forEach(e -> {
                dtm.addRow(e);
            });
        }
    }
    
    public static void fillTable(JTable table, String[] header, int[] widths, List<Object[]> rows) {
        DefaultTableModel dtm;        
        
        prepareTable(table, header, widths);
        
        dtm = (DefaultTableModel) table.getModel();
        
        if (rows != null) {
            rows.forEach(e -> {
                dtm.addRow(e);
            });
        }
    }

    public static void removeAllRowsTable(JTable table){
        DefaultTableModel dtm;

        dtm = (DefaultTableModel) table.getModel();
                
        dtm.setRowCount(0);
    }
    
    public static boolean isValid(Object control, Verificar... verificar) {
        String s;
        char c;
        int size;
        boolean allowSpace;
        int index;

        s = "";
        size = 0;
        allowSpace = false;
        index = -1;

        switch (control.getClass().getTypeName()) {
            case "javax.swing.JTextField":
                s = ((javax.swing.JTextField) control).getText();
                break;
            case "javax.swing.JTextArea":
                s = ((javax.swing.JTextArea) control).getText();
                break;
            case "javax.swing.JComboBox":
                size = ((javax.swing.JComboBox) control).getModel().getSize();
                if (size > 0) {
                    index = ((javax.swing.JComboBox) control).getSelectedIndex();
                }
                break;
            case "javax.swing.JList":
                size = ((javax.swing.JList) control).getModel().getSize();
                if (size > 0) {
                    index = ((javax.swing.JList) control).getSelectedIndex();
                }
                break;
        }

        for (Verificar tipo : verificar) {
            switch (tipo) {
                case ES_LISTA_NO_VACIA:
                    switch (control.getClass().getTypeName()) {
                        case "javax.swing.JComboBox":
                        case "javax.swing.JList":
                            if (size == 0) {
                                return false;
                            }
                            break;
                    }
                    break;
                case ELEMENTO_SELECCIONADO:
                    if (index < 0) {
                        return false;
                    }
                    break;
                case ES_ALFABETICO_CON_ESPACIOS:
                    allowSpace = true;
                case ES_ALFABETICO:
                    for (int i = 0; i < s.length(); i++) {
                        c = s.charAt(i);
                        if (c < 'A' || c > 'z') {
                            if (allowSpace) {
                                if (c != ' ') {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                    }
                    break;
                case ES_NUMERO_ENTERO:
                    if (s.isBlank()) {
                        return false;
                    } else {
                        try {
                            Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    break;
                case ES_NUMERO_REAL:
                    if (s.isBlank()) {
                        return false;
                    } else {
                        try {
                            Double.parseDouble(s);
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    break;
                case ES_TEXTO_NO_VACIO:
                    switch (control.getClass().getTypeName()) {
                        case "javax.swing.JTextField":
                        case "javax.swing.JTextArea":
                            if (s.isBlank()) {
                                return false;
                            }
                            break;
                    }
                    break;
            }
        }
        return true;
    }

    public static boolean isValidFields(List<Object[]> fields, boolean withMessage) {
        String message;
        String fieldName;
        Component component;
        Component parent;
        JTabbedPane tabbedPane;
        JPanel panel;

        message = "";

        for (Object[] field : fields) {
            for (Verificar verificar : ((Verificar[]) field[3])) {
                if (withMessage) {
                    switch (verificar) {
                        case ELEMENTO_SELECCIONADO:
                        case ES_TEXTO_NO_VACIO:
                            message = "El campo %s es obligatorio.";
                            break;
                        case ES_ALFABETICO:
                            message = "El campo %s sólo admite caracteres alfabéticos. No se admite dejar espacios";
                            break;
                        case ES_ALFABETICO_CON_ESPACIOS:
                            message = "El campo %s sólo admite caracteres alfabéticos y espacios.";
                            break;
                    }
                }

                if (!isValid(
                        field[1],
                        verificar)) {
                    component = field[2] == null ? (Component) field[1] : (Component) field[2];
                    fieldName = field[0].toString();                    

                    parent = component.getParent();

                    do {
                        if ("javax.swing.JPanel".equals(parent.getClass().getTypeName())) {
                            panel = ((javax.swing.JPanel) parent);
                            if (panel.getParent() != null && "javax.swing.JTabbedPane".equals(panel.getParent().getClass().getTypeName())) {
                                tabbedPane = (javax.swing.JTabbedPane) panel.getParent();
                                tabbedPane.setSelectedComponent(panel);
                            }

                        }
                        parent = parent.getParent();
                    } while (parent.getParent() != null);

                    component.requestFocus();
                    
                    if (withMessage) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showInternalMessageDialog(
                                component,
                                String.format(message, fieldName.toUpperCase()),
                                fieldName.toUpperCase(),
                                JOptionPane.WARNING_MESSAGE);
                    }

                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param jList
     * @param list
     */
    public static void fillListItems(JList jList, List<Item> list){
        DefaultListModel model;
        
        model = new DefaultListModel();        
        
        list.forEach(e -> {
            model.addElement(e.getObject());
        });
        
        jList.setModel(model);        
    }
    
    public static boolean isCorrectFormatTextField(
            javax.swing.JTextField jTextField,
            int charLimit,
            boolean allowSpaces,
            char caracter) {
        String s = jTextField.getText();

        if (s.length() >= charLimit) {
            return false;
        } else if (allowSpaces && !s.isBlank() && caracter == ' ') {
            if (s.charAt(s.length() - 1) == ' ') {
                return false;
            }
        } else if (caracter < 'A' || caracter > 'z') {
            return false;
        }

        return true;
    }
}
