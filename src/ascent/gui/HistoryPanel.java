package ascent.gui;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import ascent.controller.AscentController;
import ascent.model.Exercise;
import ascent.model.Workout;
import ascent.model.WorkoutLog;

/**
 * The History tab. The left list shows every finished workout with its
 * calories. Picking one shows its exercises on the right. The lifetime
 * calorie total sits at the bottom.
 */
public class HistoryPanel extends JPanel
{
   /** Supplies the workout log. */
   private AscentController controller;
   /** One line per finished workout. */
   private DefaultListModel<String> workoutModel;
   /** The list of finished workouts. */
   private JList<String> workoutList;
   /** One line per exercise in the selected workout. */
   private DefaultListModel<String> exerciseModel;
   /** Shows the lifetime calorie total. */
   private JLabel totalLabel;

   /**
      Constructs the History tab.
      @param controller the controller that holds the workout log
   */
   public HistoryPanel(AscentController controller)
   {
      this.controller = controller;

      workoutModel = new DefaultListModel<String>();
      workoutList = new JList<String>(workoutModel);
      workoutList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      workoutList.addListSelectionListener(e ->
      {
         if (!e.getValueIsAdjusting())
         {
            showSelectedWorkout();
         }
      });
      JScrollPane workoutScroll = new JScrollPane(workoutList);
      workoutScroll.setBorder(BorderFactory.createTitledBorder("Finished workouts"));

      exerciseModel = new DefaultListModel<String>();
      JScrollPane exerciseScroll = new JScrollPane(new JList<String>(exerciseModel));
      exerciseScroll.setBorder(BorderFactory.createTitledBorder("Exercises in the selected workout"));

      JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, workoutScroll, exerciseScroll);
      split.setResizeWeight(0.4);

      totalLabel = new JLabel(" ");

      setLayout(new BorderLayout(10, 10));
      setBorder(new EmptyBorder(12, 12, 12, 12));
      add(split, BorderLayout.CENTER);
      add(totalLabel, BorderLayout.SOUTH);
      refresh();
   }

   /**
      Redraws the tab to match the workout log. Keeps the same workout
      selected if there was one, otherwise selects the most recent.
   */
   public void refresh()
   {
      WorkoutLog log = controller.getLog();
      int selected = workoutList.getSelectedIndex();

      workoutModel.clear();
      for (Workout workout : log.getWorkouts())
      {
         workoutModel.addElement(String.format("%s (%s)  ~%.0f cal",
               workout.getWorkoutName(), workout.getDate(), workout.getTotalCalories()));
      }

      if (log.getWorkoutCount() == 0)
      {
         totalLabel.setText("No workouts logged yet.");
      }
      else
      {
         totalLabel.setText(String.format("Lifetime total: ~%.0f calories across %d workout(s)",
               log.getTotalCaloriesAllTime(), log.getWorkoutCount()));
         workoutList.setSelectedIndex(selected >= 0 ? selected : log.getWorkoutCount() - 1);
      }
      showSelectedWorkout();
   }

   /**
      Fills the right-hand list with the exercises of the selected
      workout, or empties it if nothing is selected.
   */
   private void showSelectedWorkout()
   {
      exerciseModel.clear();
      List<Workout> workouts = controller.getLog().getWorkouts();
      int index = workoutList.getSelectedIndex();
      if (index >= 0 && index < workouts.size())
      {
         for (Exercise exercise : workouts.get(index).getExercises())
         {
            exerciseModel.addElement(exercise.getSummary());
         }
      }
   }
}