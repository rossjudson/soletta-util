package com.soletta.seek.util.args;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyListener;
import java.beans.EventHandler;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import com.soletta.seek.util.args.LibArgs.Parameter;

public class LibArgsGUI {

    private List<Parameter> parameters;
    private Object config;
    private JDialog dialog;

    
    public LibArgsGUI(Object config) throws ArgException {

        this.config = config;
        try {
            parameters = LibArgs.introspect(config);
            
        } catch (IntrospectionException e) {
            throw new ArgException("Unable to introspect " + config.getClass().getName());
        }
        
    }
    
    public JDialog show(boolean modal) {
        dialog = new JDialog();
        dialog.setModal(modal);
        
        dialog.setTitle("Configure");
        
        JPanel optionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2, 2, 2, 2);
        
        for (Parameter p: parameters) {

            gc.gridwidth = 1;
            gc.weightx = 0;
            gc.fill = GridBagConstraints.NONE;
            final JCheckBox set = new JCheckBox(p.longName);
            gc.anchor = GridBagConstraints.BASELINE_LEADING;
            optionPanel.add(set, gc);
            
            Class<?> ptype = p.pd.getPropertyType();
            final JComponent comp;
            if (Enum.class.isAssignableFrom(ptype)) {
                JComboBox combo = new JComboBox(ptype.getEnumConstants());
                comp = combo;
            } else if (Color.class.isAssignableFrom(ptype)) {
                JColorChooser cc = new JColorChooser();
                cc.getSelectionModel().addChangeListener(EventHandler.create(ChangeListener.class, config, p.pd.getWriteMethod().getName(), "source.selectedColor"));
                comp = cc;
            } else if (Boolean.TYPE.isAssignableFrom(ptype) || Boolean.class.isAssignableFrom(ptype)) {
                JCheckBox x = new JCheckBox(p.longName);
                x.addChangeListener(EventHandler.create(ChangeListener.class, config, p.pd.getWriteMethod().getName(), "source.selected"));
                comp = x;
            } else if (Integer.TYPE.isAssignableFrom(ptype) || Integer.class.isAssignableFrom(ptype)) {
                SpinnerNumberModel model = new SpinnerNumberModel(p.intDefault(), p.intMin(), p.intMax(), 1);
                JSpinner x = new JSpinner(model);
                x.addChangeListener(EventHandler.create(ChangeListener.class, config, p.pd.getWriteMethod().getName(), "source.value"));
                comp = x;
            } else {
                gc.fill = GridBagConstraints.HORIZONTAL;
                JTextField tf = new JTextField();
                tf.addKeyListener(EventHandler.create(KeyListener.class, config, p.pd.getWriteMethod().getName(), "source.text"));
                comp = tf;
            }
            
            comp.setEnabled(false);
            
            comp.addPropertyChangeListener(new PropertyChangeListener() {
                
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    System.out.println(evt.getPropertyName() + "\t" + evt.getOldValue() + "\t" + evt.getNewValue());
                }
            });
            ChangeListener handler = EventHandler.create(ChangeListener.class, comp, "setEnabled", "source.selected");
            set.addChangeListener(handler);
            
//            set.addChangeListener(new ChangeListener() {
//                
//                @Override
//                public void stateChanged(ChangeEvent e) {
//                    // TODO Auto-generated method stub
//                    
//                }
//            });
            
            comp.setToolTipText(p.description());
            gc.gridwidth = GridBagConstraints.REMAINDER;
            optionPanel.add(comp, gc);
            PropertyEditor editor = PropertyEditorManager.findEditor(p.pd.getPropertyType());
            if (editor != null) {
                Object val = editor.getValue();
            }
            
            gc.gridy++;
            
        }

        dialog.getContentPane().add(optionPanel);
        dialog.pack();
        dialog.setVisible(true);
        if (modal) {
            dialog.dispose();
        }
        return dialog;
    }
    
}
