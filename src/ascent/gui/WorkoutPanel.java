package ascent.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import ascent.controller.AscentController;
import ascent.model.Exercise;
import ascent.model.Workout;

/**
 * The Workout tab. The user starts a workout here, adds exercises to
 * it, and then finishes it to save it to the history. Buttons are
 * enabled only when they make sense: the start fields while no workout
 * is open, and Add, Finish, and Discard while one is.
 */
public class WorkoutPanel extends JPanel
{
   /** Carries out every action on this tab. */
   private AscentController controller;
   /** Called after anything changes so every tab can redraw. */
   private Runnable onChange;

   /** Name of the workout to start. */
   private JTextField nameField;
   /** Date of the workout to start. */
   private JTextField dateField;
   /** Starts a workout. */
   private JButton startButton;
   /** Opens the add-exercise dialog. */
   private JButton addButton;
   /** Saves the open workout to the history. */
   private JButton finishButton;
   /** Throws away the open workout. */
   private JButton discardButton;
   /** The exercises in the open workout, one summary line each. */
   private DefaultListModel<String> exerciseModel;
   /** The border around the exercise list, retitled with the workout name. */
   private TitledBorder listBorder;
   /** Shows the running calorie total of the open workout. */
   private JLabel totalLabel;
   /** Shows what just happened, or what went wrong. */
   private MessageLabel message;

   /**
      Constructs the Workout tab.
      @param controller the controller that carries out the actions
      @param onChange called after any change so all tabs refresh
   */
   public WorkoutPanel(AscentController controller, Runnable onChange)
   {
      this.controller = controller;
      this.onChange = onChange;

      setLayout(new BorderLayout(10, 10));
      setBorder(new EmptyBorder(12, 12, 12, 12));
      add(buildStartBox(), BorderLayout.NORTH);
      add(buildExerciseList(), BorderLayout.CENTER);
      add(buildBottom(), BorderLayout.SOUTH);
      refresh();
   }

   /**
      Redraws the tab to match the controller's current state.
   */
   public void refresh()
   {
      Workout workout = controller.getCurrentWorkout();
      boolean open = (workout != null);

      nameField.setEnabled(!open);
      dateField.setEnabled(!open);
      startButton.setEnabled(!open);
      addButton.setEnabled(open);
      finishButton.setEnabled(open);
      discardButton.setEnabled(open);

      exerciseModel.clear();
      if (open)
      {
         for (Exercise exercise : workout.getExercises())
         {
            exerciseModel.addElement(exercise.getSummary());
         }
         listBorder.setTitle("Current workout: " + workout.getWorkoutName()
               + " (" + workout.getDate() + ")");
         totalLabel.setText(String.format("Total so far: ~%.0f calories across %d exercise(s)",
               workout.getTotalCalories(), workout.getExerciseCount()));
      }
      else
      {
         listBorder.setTitle("No workout in progress");
         totalLabel.setText(" ");
      }
      repaint();
   }

   /**
      Builds the box with the name and date fields and the Start button.
      @return the start box
   */
   private JPanel buildStartBox()
   {
      FormPanel form = new FormPanel();
      nameField = form.addTextField("Workout name", 20);
      dateField = form.addTextField("Date (YYYY-MM-DD)", 20);
      dateField.setText(LocalDate.now().toString());
      nameField.addActionListener(e -> startWorkout());
      dateField.addActionListener(e -> startWorkout());

      startButton = new JButton("Start workout");
      startButton.addActionListener(e -> startWorkout());
      JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
      buttonRow.add(startButton);

      JPanel box = new JPanel(new BorderLayout());
      box.setBorder(BorderFactory.createTitledBorder("New workout"));
      box.add(form, BorderLayout.CENTER);
      box.add(buttonRow, BorderLayout.SOUTH);
      return box;
   }

   /**
      Builds the scrolling list of the open workout's exercises.
      @return the list in a scroll pane
   */
   private JScrollPane buildExerciseList()
   {
      exerciseModel = new DefaultListModel<String>();
      JScrollPane scroll = new JScrollPane(new JList<String>(exerciseModel));
      listBorder = BorderFactory.createTitledBorder("Current workout");
      scroll.setBorder(listBorder);
      return scroll;
   }

   /**
      Builds the calorie total, the action buttons, and the message line.
      @return the bottom panel
   */
   private JPanel buildBottom()
   {
      totalLabel = new JLabel(" ");

      addButton = new JButton("Add exercise...");
      addButton.addActionListener(e -> addExercise());
      finishButton = new JButton("Finish workout");
      finishButton.addActionListener(e -> finishWorkout());
      discardButton = new JButton("Discard workout");
      discardButton.addActionListener(e -> discardWorkout());
      JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
      buttonRow.add(addButton);
      buttonRow.add(finishButton);
      buttonRow.add(discardButton);

      message = new MessageLabel();

      JPanel bottom = new JPanel(new GridLayout(0, 1));
      bottom.add(totalLabel);
      bottom.add(buttonRow);
      bottom.add(message);
      return bottom;
   }

   /** Reads the name and date fields and starts a workout. */
   private void startWorkout()
   {
      try
      {
         String name = FormInput.requiredText("Workout name", nameField.getText());
         String date = FormInput.requiredText("Date", dateField.getText());
         controller.startWorkout(name, date);
         nameField.setText("");
         message.showInfo("Started workout: " + name);
         onChange.run();
      }
      catch (IllegalArgumentException | IllegalStateException e)
      {
         message.showError(e.getMessage());
      }
   }

   /** Opens the add-exercise dialog and reports what was added. */
   private void addExercise()
   {
      Window owner = SwingUtilities.getWindowAncestor(this);
      AddExerciseDialog dialog = new AddExerciseDialog(owner, controller);
      dialog.setVisible(true);

      Exercise added = dialog.getAddedExercise();
      if (added != null)
      {
         message.showInfo("Added: " + added.getSummary());
         onChange.run();
      }
   }

   /** Saves the open workout to the history. */
   private void finishWorkout()
   {
      try
      {
         Workout saved = controller.finishWorkout();
         message.showInfo(String.format("Workout saved: %s, ~%.0f calories",
               saved.getWorkoutName(), saved.getTotalCalories()));
         onChange.run();
      }
      catch (IllegalStateException e)
      {
         message.showError(e.getMessage());
      }
   }

   /** Asks for confirmation, then throws away the open workout. */
   private void discardWorkout()
   {
      int answer = JOptionPane.showConfirmDialog(this,
            "Discard this workout and everything in it?", "Discard workout",
            JOptionPane.YES_NO_OPTION);
      if (answer != JOptionPane.YES_OPTION)
      {
         return;
      }
      try
      {
         controller.discardWorkout();
         message.showInfo("Workout discarded.");
         onChange.run();
      }
      catch (IllegalStateException e)
      {
         message.showError(e.getMessage());
      }
   }
}