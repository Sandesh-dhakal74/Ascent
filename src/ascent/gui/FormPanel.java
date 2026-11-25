package ascent.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A simple form made of rows, each with a label on the left and an
 * input field on the right. The workout, goal, and add-exercise
 * screens all build their forms with it so they line up the same way.
 */
public class FormPanel extends JPanel
{
   /** The grid row the next field will go in. */
   private int nextRow;

   /**
      Constructs an empty form.
   */
   public FormPanel()
   {
      super(new GridBagLayout());
      nextRow = 0;
   }

   /**
      Adds a row with a label and any input component.
      @param labelText the text of the label
      @param field the component to put next to the label
   */
   public void addRow(String labelText, JComponent field)
   {
      GridBagConstraints labelPlace = new GridBagConstraints();
      labelPlace.gridx = 0;
      labelPlace.gridy = nextRow;
      labelPlace.anchor = GridBagConstraints.EAST;
      labelPlace.insets = new Insets(4, 4, 4, 8);
      add(new JLabel(labelText), labelPlace);

      GridBagConstraints fieldPlace = new GridBagConstraints();
      fieldPlace.gridx = 1;
      fieldPlace.gridy = nextRow;
      fieldPlace.anchor = GridBagConstraints.WEST;
      fieldPlace.weightx = 1.0;
      fieldPlace.insets = new Insets(4, 0, 4, 4);
      add(field, fieldPlace);

      nextRow++;
   }

   /**
      Adds a row with a label and a new text field.
      @param labelText the text of the label
      @param columns the width of the text field in characters
      @return the text field that was added
   */
   public JTextField addTextField(String labelText, int columns)
   {
      JTextField field = new JTextField(columns);
      addRow(labelText, field);
      return field;
   }
}