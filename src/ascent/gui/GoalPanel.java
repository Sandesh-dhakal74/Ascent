package ascent.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import ascent.controller.AscentController;
import ascent.model.Goal;
import ascent.model.WorkoutLog;

/**
 * The Goal tab. The user sets a calorie target here, and a progress
 * bar shows how much of it the finished workouts have reached.
 */
public class GoalPanel extends JPanel
{
   /** Supplies the goal and the workout log. */
   private AscentController controller;
   /** Called after the goal changes so every tab can redraw. */
   private Runnable onChange;

   /** What the goal is for. */
   private JTextField descriptionField;
   /** The calorie target. */
   private JTextField targetField;
   /** Shows the goal's description. */
   private JLabel goalLabel;
   /** Shows progress toward the target as a percent. */
   private JProgressBar progressBar;
   /** Shows calories logged and calories remaining. */
   private JLabel detailLabel;
   /** Shows what just happened, or what went wrong. */
   private MessageLabel message;

   /**
      Constructs the Goal tab.
      @param controller the controller that holds the goal and the log
      @param onChange called after the goal changes so all tabs refresh
   */
   public GoalPanel(AscentController controller, Runnable onChange)
   {
      this.controller = controller;
      this.onChange = onChange;

      setLayout(new BorderLayout(10, 10));
      setBorder(new EmptyBorder(12, 12, 12, 12));
      add(buildGoalForm(), BorderLayout.NORTH);
      add(buildProgressBox(), BorderLayout.CENTER);
      refresh();
   }

   /**
      Redraws the tab to match the current goal and workout log.
   */
   public void refresh()
   {
      Goal goal = controller.getGoal();
      if (goal == null)
      {
         goalLabel.setText("No goal set yet.");
         progressBar.setValue(0);
         progressBar.setString("0%");
         detailLabel.setText("Set a goal above to track your progress.");
         return;
      }

      WorkoutLog log = controller.getLog();
      double percent = goal.getProgressPercent(log);
      double achieved = log.getTotalCaloriesAllTime();

      goalLabel.setText("Goal: " + goal.getDescription());
      progressBar.setValue((int) Math.min(100, Math.round(percent)));
      progressBar.setString(String.format("%.0f%%", percent));
      if (goal.isAchieved(log))
      {
         detailLabel.setText(String.format("Goal achieved! ~%.0f / %.0f calories",
               achieved, goal.getTargetCalories()));
      }
      else
      {
         detailLabel.setText(String.format("~%.0f / %.0f calories, ~%.0f remaining",
               achieved, goal.getTargetCalories(), goal.getTargetCalories() - achieved));
      }
   }

   /**
      Builds the box with the description and target fields.
      @return the goal form box
   */
   private JPanel buildGoalForm()
   {
      FormPanel form = new FormPanel();
      descriptionField = form.addTextField("Goal description", 20);
      targetField = form.addTextField("Target calories", 20);
      descriptionField.addActionListener(e -> setGoal());
      targetField.addActionListener(e -> setGoal());

      JButton setButton = new JButton("Set goal");
      setButton.addActionListener(e -> setGoal());
      message = new MessageLabel();

      JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
      buttonRow.add(setButton);
      buttonRow.add(message);

      JPanel box = new JPanel(new BorderLayout());
      box.setBorder(BorderFactory.createTitledBorder("Calorie goal"));
      box.add(form, BorderLayout.CENTER);
      box.add(buttonRow, BorderLayout.SOUTH);
      return box;
   }

   /**
      Builds the box with the goal name, progress bar, and details.
      @return the progress box
   */
   private JPanel buildProgressBox()
   {
      goalLabel = new JLabel(" ");
      progressBar = new JProgressBar(0, 100);
      progressBar.setStringPainted(true);
      detailLabel = new JLabel(" ");

      JPanel rows = new JPanel(new GridLayout(0, 1, 6, 6));
      rows.add(goalLabel);
      rows.add(progressBar);
      rows.add(detailLabel);

      JPanel box = new JPanel(new BorderLayout());
      box.setBorder(BorderFactory.createTitledBorder("Progress"));
      box.add(rows, BorderLayout.NORTH);
      return box;
   }

   /** Reads the form and sets the goal. */
   private void setGoal()
   {
      try
      {
         String description = FormInput.requiredText("Goal description", descriptionField.getText());
         double target = FormInput.number("Target calories", targetField.getText());
         controller.setGoal(description, target);
         message.showInfo("Goal set.");
         onChange.run();
      }
      catch (IllegalArgumentException e)
      {
         message.showError(e.getMessage());
      }
   }
}
