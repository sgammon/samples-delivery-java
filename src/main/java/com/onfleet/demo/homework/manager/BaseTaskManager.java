package com.onfleet.demo.homework.manager;


import com.onfleet.demo.homework.TaskAssigner;
import com.onfleet.demo.homework.collection.Tasklist;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Task;
import org.jetbrains.annotations.NotNull;

import java.util.*;


/**
 * Abstract base class that provides default functionality for {@link TaskAssigner} implementors.
 */
abstract class BaseTaskManager implements TaskAssigner {
  /**
   * Assign a collection of {@link Task} records to a {@link Driver}.
   *
   * @param driver Driver we are assigning to.
   * @param tasks Collection of tasks we are assigning.
   */
  public void assignToDriver(final @NotNull Driver driver, final @NotNull Iterable<Task> tasks) {
    for (final Task task : tasks) {
      assignToDriver(driver, task);
    }
  }

  /**
   * Build a report of drivers and their task loads. The list should be sorted by task load.
   *
   * @return {@link StringBuilder}, pre-filled with a report to be printed.
   */
  public @NotNull StringBuilder report() {
    // not really worried about performance at the end of our run...
    final StringBuilder sb = new StringBuilder();
    final List<Map.Entry<Driver, LinkedHashSet<Task>>> rawTasklistCollection = (
        new ArrayList<>(this.export().entrySet()));

    // rebuild into full Tasklist objects, with stats
    final List<Tasklist> tasklistCollection = new ArrayList<>(rawTasklistCollection.size());
    for (final Map.Entry<Driver, LinkedHashSet<Task>> tasklistForDriver : rawTasklistCollection) {
      Tasklist tasklistObj = new Tasklist(tasklistForDriver.getKey(),
                                          tasklistForDriver.getValue());
      tasklistCollection.add(tasklistObj);
    }

    // sort in descending order
    Collections.sort(tasklistCollection);
    Collections.reverse(tasklistCollection);

    sb.append("");
    sb.append("------------- Task Report -------------\n");

    for (final Tasklist list : tasklistCollection) {
      sb.append(list.getDriver().getName());
      sb.append(": estimate(");
      sb.append(list.getLoadEstimate());
      sb.append("), ");
      sb.append("assigned(");
      sb.append(list.getAssignedTasks().size());
      sb.append("), ");

      // get first and last task, calculate total distance they have to go
      final Task firstTask = list.getAssignedTasks().iterator().next();
      final Task lastTask = list.getLastAssignedTask();
      final Double totalDistanceForDriver = Tasklist.calculateDistanceForPoints(firstTask, lastTask);

      sb.append("total(");
      sb.append(totalDistanceForDriver);
      sb.append(")\n");
    }
    sb.append("");
    return sb;
  }
}
