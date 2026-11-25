package ascent.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import ascent.controller.AscentController;
import ascent.model.Exercise;

/**
 * A pop-up form for adding one exercise to the open workout. The user
 * picks Strength, Cardio, or Flexibility, and the form switches to the
 * fields that type needs. If something typed is invalid the dialog
 * stays open, shows what is wrong, and keeps everything already typed.
 */
public class AddExerciseDialog extends JDialog
{
   /** Name of the strength choice and its form. */
   private static final String STRENGTH = "Strength";
   /** Name of the cardio choice and its form. */
   private static final String CARDIO = "Cardio";
   /** Name of the flexibility choice and its form. */
   private static final String FLEXIBILITY = "Flexibility";

   /** Carries out the add once the input has been read. */
   private AscentController controller;
   /** The exercise that was added, or null if the user cancelled. */
   private Exercise addedExercise;

   /** Drop-down for the exercise type. */
   private JComboBox<String> typeBox;
   /** The exercise name. */
   private JTextField nameField;
   /** Optional notes. */
   private JTextField notesField;
   /** Strength: number of sets. */
   private JTextField setsField;
   /** Strength: reps per set. */
   private JTextField repsField;
   /** Strength: weight in pounds. */
   private JTextField weightField;
   /** Cardio: distance in miles. */
   private JTextField distanceField;
   /** Cardio: duration in minutes. */
   private JTextField cardioDurationField;
   /** Flexibility: duration in minutes. */
   private JTextField flexDurationField;
   /** Flexibility: intensity from 1 to 5. */
   private JTextField intensityField;

   /** Switches between the three type-specific forms. */
   private CardLayout cardLayout;
   /** Holds the three type-specific forms. */
   private JPanel cards;
   /** Shows what is wrong with the input. */
   private MessageLabel message;

   /**
      Constructs the dialog. It is modal, so the rest of the program
      waits until the user adds an exercise or cancels.
      @param owner the window this dialog appears over
      @param controller the controller that will add the exercise
   */
   public AddExerciseDialog(Window owner, AscentController controller)
   {
      super(owner, "Add an exercise", Dialog.ModalityType.APPLICATION_MODAL);
      this.controller = controller;
      addedExercise = null;

      FormPanel common = new FormPanel();
      typeBox = new JComboBox<String>(new String[] { STRENGTH, CARDIO, FLEXIBILITY });
      common.addRow("Type", typeBox);
      nameField = common.addTextField("Exercise name", 20);
      notesField = common.addTextField("Notes (optional)", 20);

      cardLayout = new CardLayout();
      cards = new JPanel(cardLayout);
      cards.add(buildStrengthForm(), STRENGTH);
      cards.add(buildCardioForm(), CARDIO);
      cards.add(buildFlexibilityForm(), FLEXIBILITY);
      typeBox.addActionListener(e -> cardLayout.show(cards, (String) typeBox.getSelectedItem()));

      JPanel content = new JPanel(new BorderLayout(8, 8));
      content.setBorder(new EmptyBorder(12, 12, 12, 12));
      content.add(common, BorderLayout.NORTH);
      content.add(cards, BorderLayout.CENTER);
      content.add(buildBottom(), BorderLayout.SOUTH);
      setContentPane(content);

      pack();
      setSize(Math.max(getWidth(), 460), getHeight());
      setLocationRelativeTo(owner);
   }

   /**
      Gets the exercise that was added.
      @return the new exercise, or null if the user cancelled
   */
   public Exercise getAddedExercise()
   {
      return addedExercise;
   }

   /**
      Builds the fields for a strength exercise.
      @return the strength form
   */
   private FormPanel buildStrengthForm()
   {
      FormPanel form = new FormPanel();
      setsField = form.addTextField("Sets", 8);
      repsField = form.addTextField("Reps per set", 8);
      weightField = form.addTextField("Weight (lbs)", 8);
      return form;
   }

   /**
      Builds the fields for a cardio exercise.
      @return the cardio form
   */
   private FormPanel buildCardioForm()
   {
      FormPanel form = new FormPanel();
      distanceField = form.addTextField("Distance (miles)", 8);
      cardioDurationField = form.addTextField("Duration (minutes)", 8);
      return form;
   }

   /**
      Builds the fields for a flexibility exercise.
      @return the flexibility form
   */
   private FormPanel buildFlexibilityForm()
   {
      FormPanel form = new FormPanel();
      flexDurationField = form.addTextField("Duration (minutes)", 8);
      intensityField = form.addTextField("Intensity (1 to 5)", 8);
      return form;
   }

   /**
      Builds the message line and the Add and Cancel buttons.
      @return the bottom panel
   */
   private JPanel buildBottom()
   {
      message = new MessageLabel();

      JButton addButton = new JButton("Add");
      addButton.addActionListener(e -> addExercise());
      JButton cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(e -> dispose());
      getRootPane().setDefaultButton(addButton);

      JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttonRow.add(addButton);
      buttonRow.add(cancelButton);

      JPanel bottom = new JPanel(new GridLayout(0, 1));
      bottom.add(message);
      bottom.add(buttonRow);
      return bottom;
   }

   /**
      Reads the form for the chosen type and asks the controller to add
      the exercise. Closes the dialog on success. Shows the problem and
      stays open if anything is invalid.
   */
   private void addExercise()
   {
      try
      {
         String name = FormInput.requiredText("Exercise name", nameField.getText());
         String notes = FormInput.optionalText(notesField.getText());
         String type = (String) typeBox.getSelectedItem();

         if (type.equals(STRENGTH))
         {
            int sets = FormInput.wholeNumber("Sets", setsField.getText());
            int reps = FormInput.wholeNumber("Reps", repsField.getText());
            double weight = FormInput.number("Weight", weightField.getText());
            addedExercise = controller.addStrength(name, notes, sets, reps, weight);
         }
         else if (type.equals(CARDIO))
         {
            double distance = FormInput.number("Distance", distanceField.getText());
            double duration = FormInput.number("Duration", cardioDurationField.getText());
            addedExercise = controller.addCardio(name, notes, distance, duration);
         }
         else
         {
            double duration = FormInput.number("Duration", flexDurationField.getText());
            int intensity = FormInput.wholeNumber("Intensity", intensityField.getText());
            addedExercise = controller.addFlexibility(name, notes, duration, intensity);
         }
         dispose();
      }
      catch (IllegalArgumentException | IllegalStateException e)
      {
         message.showError(e.getMessage());
      }
   }
}