package com.onfleet.demo.homework.manager;


import com.onfleet.demo.homework.TaskAssigner;
import com.onfleet.demo.homework.cli.AppLogger;
import com.onfleet.demo.homework.collection.Tasklist;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Task;
import com.onfleet.demo.homework.util.SampleDataset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


/**
 * Mediates between {@link Tasklist} instances of {@link Task} records, associated with
 * {@link Driver} records, where each task has a {@link com.onfleet.demo.homework.struct.Location}.
 * Keep track of the current "cost" for each driver's workload, and allow that to be efficiently
 * queried when assigning new tasks.
 */
@SuppressWarnings("WeakerAccess")
public final class TaskManager extends BaseTaskManager implements TaskAssigner {
  // -- internals -- //
  /**
   * Master tasklist assignment set.
   */
  Map<Driver, Tasklist> taskboard;

  // -- constructor -- //
  /**
   * Build a new task manager, given a set of drivers we will be distributing tasks over.
   */
  private TaskManager(final Collection<Driver> drivers) {
    // setup initial driver tasklists
    final Map<Driver, Tasklist> tasklistMap = new HashMap<>(drivers.size());

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
   * Export the current state of {@link TaskManager} into a sorted map.
   *
   * @return Current task-board, sans pre-computed weighting data.
   */
  public @NotNull Map<Driver, LinkedHashSet<Task>> export() {
    final Map<Driver, LinkedHashSet<Task>> payload = new HashMap<>();
    for (final Map.Entry<Driver, Tasklist> entry : taskboard.entrySet()) {
      final LinkedHashSet<Task> targetSet = new LinkedHashSet<>(entry.getValue().getAssignedTasks().size());
      targetSet.addAll(entry.getValue().getAssignedTasks());
      payload.put(entry.getKey(), targetSet);
    }
    return payload;
  }

  /**
   * Assign a {@link Task} to a {@link Driver}.
   *
   * @param driver Driver we are assigning to.
   * @param task Task we are assigning.
   */
  public void assignToDriver(final @NotNull Driver driver, final @NotNull Task task) {
    AppLogger.say("TaskManager", "Assigning task '"
                                     + task.getUuid() + "' to " + driver.getName() + "...");
    final Tasklist tasklist = this.taskboard.get(driver);
    if (tasklist == null)
      taskboard.put(driver, new Tasklist(driver, Collections.singletonList(task)));
    else
      tasklist.assignTask(task);
  }

  /**
   * Given a {@link Task}, figure out the cheapest cost for who to assign it to in the set of
   * active {@link Driver} {@link Tasklist} records, given their current workload.
   *
   * @param task Task that we wish to assign to someone.
   * @return {@link Driver} that should be assigned the task.
   */
  public @NotNull Driver resolveLowestCostAssignment(final @NotNull Task task) {
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
  public @Nullable Tasklist tasklistForDriver(final @NotNull Driver driver) {
    return this.taskboard.get(driver);
  }
}
