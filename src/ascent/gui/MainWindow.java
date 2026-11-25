package ascent.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import ascent.controller.AscentController;

/**
 * The main window of Ascent. It holds three tabs (Workout, History,
 * and Goal) that all read from the same controller. Whenever one tab
 * changes something, refreshAll() redraws every tab so the display
 * always matches the application state.
 */
public class MainWindow extends JFrame
{
   /** The Workout tab. */
   private WorkoutPanel workoutPanel;
   /** The History tab. */
   private HistoryPanel historyPanel;
   /** The Goal tab. */
   private GoalPanel goalPanel;

   /**
      Constructs the main window with its three tabs.
      @param controller the controller shared by every tab
   */
   public MainWindow(AscentController controller)
   {
      super("Ascent: Fitness Workout Planner");

      workoutPanel = new WorkoutPanel(controller, this::refreshAll);
      historyPanel = new HistoryPanel(controller);
      goalPanel = new GoalPanel(controller, this::refreshAll);

      JTabbedPane tabs = new JTabbedPane();
      tabs.addTab("Workout", workoutPanel);
      tabs.addTab("History", historyPanel);
      tabs.addTab("Goal", goalPanel);
      add(tabs);

      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(820, 560);
      setLocationRelativeTo(null);
   }

   /**
      Redraws every tab from the controller's current state.
   */
   public void refreshAll()
   {
      workoutPanel.refresh();
      historyPanel.refresh();
      goalPanel.refresh();
   }
}
