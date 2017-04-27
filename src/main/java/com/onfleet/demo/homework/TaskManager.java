package com.onfleet.demo.homework;


import com.onfleet.demo.homework.cli.AppLogger;
import com.onfleet.demo.homework.collection.Tasklist;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Task;
import com.onfleet.demo.homework.util.SampleDataset;
import org.jetbrains.annotations.Nullable;

import java.util.*;


/**
 * Mediates between {@link Tasklist} instances of {@link Task} records, associated with
 * {@link Driver} records, where each task has a {@link com.onfleet.demo.homework.struct.Location}.
 * Keep track of the current "cost" for each driver's workload, and allow that to be efficiently
 * queried when assigning new tasks.
 */
@SuppressWarnings("WeakerAccess")
public final class TaskManager {
  // -- internals -- //
  /**
   * Master tasklist assignment set.
   */
  HashMap<Driver, Tasklist> taskboard;

  // -- constructor -- //
  /**
   * Build a new task manager, given a set of drivers we will be distributing tasks over.
   */
  TaskManager(final Collection<Driver> drivers) {
    // setup initial driver tasklists
    final HashMap<Driver, Tasklist> tasklistMap = new HashMap<>(drivers.size());

    for (final Driver driver : drivers) {
      final Tasklist tasklist = new Tasklist(driver);
      tasklistMap.put(driver, tasklist);
    }

    this.taskboard = tasklistMap;
  }

  // -- static API -- //
  /**
   * Setup a new {@link TaskManager} with a {@link SampleDataset}.
   *
   * @param dataset Sample dataset to build a {@link TaskManager} from.
   * @return Newly-minted {@link TaskManager}.
   */
  public static TaskManager setupWithDataset(final SampleDataset dataset) {
    AppLogger.say("TaskManager", "Initializing sample dataset with "
                                     + dataset.getGeneratedTasks().size() + " tasks and "
                                     + dataset.getGeneratedDrivers().size() + " drivers...");

    // initialize from dataset data
    final Collection<Driver> drivers = dataset.getGeneratedDrivers();
    final TaskManager manager = new TaskManager(drivers);

    Driver lowestCostDriver;
    for (final Task task : dataset.getGeneratedTasks()) {
      lowestCostDriver = manager.resolveLowestCostAssignment(task);
      manager.assignToDriver(lowestCostDriver, task);
    }
    return manager;
  }

  // -- public API -- //
  /**
   * Assign a {@link Task} to a {@link Driver}.
   *
   * @param driver Driver we are assigning to.
   * @param task Task we are assigning.
   */
  public void assignToDriver(final Driver driver, final Task task) {
    AppLogger.say("TaskManager", "Assigning task '"
                                     + task.getUuid() + "' to " + driver.getName() + "...");
    final Tasklist tasklist = this.taskboard.get(driver);
    if (tasklist == null)
      taskboard.put(driver, new Tasklist(driver, Collections.singletonList(task)));
    else
      tasklist.assignTask(task);
  }

  /**
   * Assign a collection of {@link Task} records to a {@link Driver}.
   *
   * @param driver Driver we are assigning to.
   * @param tasks Collection of tasks we are assigning.
   */
  public void assignToDriver(final Driver driver, final Iterable<Task> tasks) {
    for (final Task task : tasks) {
      assignToDriver(driver, task);
    }
  }

  /**
   * Given a {@link Task}, figure out the cheapest cost for who to assign it to in the set of
   * active {@link Driver} {@link Tasklist} records, given their current workload.
   *
   * @param task Task that we wish to assign to someone.
   * @return {@link Driver} that should be assigned the task.
   */
  public Driver resolveLowestCostAssignment(final Task task) {
    double estimatedCost;
    double lowestCostSoFar = 0.0;
    Tasklist resolvedList = null;

    for (final Map.Entry<Driver, Tasklist> entry : taskboard.entrySet()) {
      final Tasklist list = entry.getValue();

      estimatedCost = list.costToAssignTask(task);
      if (estimatedCost == 0.0)
        return list.getDriver();  // driver's first task
      if (lowestCostSoFar == 0.0) {
        // we don't have a 'lowest cost' yet
        lowestCostSoFar = estimatedCost;
        resolvedList = list;
      }
      if (lowestCostSoFar > estimatedCost) {
        // this list beat the last lowest one
        lowestCostSoFar = estimatedCost;
        resolvedList = list;
      }
    }

    if (resolvedList == null)
      throw new IllegalStateException("There should always be a lowest-cost list.");
    return resolvedList.getDriver();
  }

  /**
   * Retrieve the current tasklist for a {@link Driver}.
   *
   * @param driver Driver to retrieve a tasklist for.
   * @return Driver's tasklist, if we've seen them before, or <pre>null</pre>.
   */
  public @Nullable Tasklist tasklistForDriver(final Driver driver) {
    return this.taskboard.get(driver);
  }

  /**
   * Build a report of drivers and their task loads. The list should be
   * sorted by task load.
   *
   * @return {@link StringBuilder}, pre-filled with a report to be printed.
   */
  public StringBuilder report() {
    // not really worried about performance at the end of our run...
    final StringBuilder sb = new StringBuilder();
    final List<Tasklist> tasklistSet = new ArrayList<>(this.taskboard.values());

    // sort in descending order
    Collections.sort(tasklistSet);
    Collections.reverse(tasklistSet);

    sb.append("");
    sb.append("------------- Task Report -------------\n");

    for (final Tasklist list : tasklistSet) {
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
