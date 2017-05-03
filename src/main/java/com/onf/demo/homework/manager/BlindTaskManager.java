package com.onf.demo.homework.manager;


import com.onf.demo.homework.TaskAssigner;
import com.onf.demo.homework.collection.Tasklist;
import com.onf.demo.homework.struct.Driver;
import com.onf.demo.homework.struct.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;


/**
 * Holds data to simulate different algorithms resolving which {@link Driver}
 * to which a task must be assigned.
 */
@SuppressWarnings("WeakerAccess")
public final class BlindTaskManager extends BaseTaskManager implements TaskAssigner {
  // -- internals -- //
  /**
   * Holds the global set of tasks for each driver, ordered by the order in which
   * they were originally inserted - so, however {@link TaskManager} inserted them.
   */
  Map<Driver, LinkedHashSet<Task>> taskboard;

  // -- constructor --//
  /**
   * Construct a new task manager that is blind to the algorithms that assembled it
   * from a sample dataset.
   *
   * @param manager Original task manager.
   */
  public BlindTaskManager(final @NotNull TaskAssigner manager) {
    taskboard = manager.export();
  }

  // -- private API -- //
  /**
   * Compute the cost for a {@link Driver}'s current tasklist, based on the number of
   * tasks assigned to them, and the summed distance they must travel.
   *
   * @param tasklist Current tasklist for this candidate {@link Driver}.
   * @param candidateTask Cost difference for list with task assigned.
   * @return "Cost" estimate for this tasklist, given the additional candidate {@link Task}.
   */
  private double costForTasklist(final Iterable<Task> tasklist, final Task candidateTask) {
    // calculate the summed distance of all tasks...
    double summedDistance = 0.0;
    Task lastTaskSeen = null;
    for (final Task taskItem : tasklist) {
      summedDistance += Tasklist.calculateDistanceForPoints(lastTaskSeen, taskItem);
      lastTaskSeen = taskItem;
    }

    // simple distance, for now, added to the end of the list
    return summedDistance + Tasklist.calculateDistanceForPoints(lastTaskSeen, candidateTask);
  }

  // -- public API -- //
  /**
   * Assign a {@link Task} to a {@link Driver}.
   *
   * @param driver Driver to assign the task to.
   * @param task Task to assign.
   */
  @Override
  public void assignToDriver(final @NotNull Driver driver, final @NotNull Task task) {
    final LinkedHashSet<Task> taskset = this.taskboard.get(driver);
    if (taskset == null)
      this.taskboard.put(driver, new LinkedHashSet<>(Collections.singleton(task)));
    else
      taskset.add(task);
  }

  /**
   * Resolve the lowest-cost {@link Driver} assignment possible for a given {@link Task}.
   *
   * @param task Task to be considered.
   * @return Driver to be assigned the task.
   * @throws IllegalStateException If any task sets tracked by this object end up being
   *         empty, which is not allowed, since this class depends on data being
   *         generated or loaded beforehand.
   */
  @NotNull @Override
  public Driver resolveLowestCostAssignment(final @NotNull Task task) {
    double lowestSeenLoad = 0.0;
    Driver candidateDriver = null;

    // for each driver's tasklist...
    for (final Map.Entry<Driver, LinkedHashSet<Task>> tasksetEntry : this.taskboard.entrySet()) {
      if (tasksetEntry.getValue().isEmpty())
        throw new IllegalStateException("Cannot run `BlindTaskManager` without pre-loaded data.");

      // task list cost with added load
      double taskListCost = this.costForTasklist(tasksetEntry.getValue(),
                                                 task);

      if (candidateDriver == null) {
        candidateDriver = tasksetEntry.getKey();
        lowestSeenLoad = taskListCost;
      } else {
        // is the current task list less costly than our previous candidate?
        if (lowestSeenLoad > taskListCost) {
          // if so it's our new candidate
          candidateDriver = tasksetEntry.getKey();
          lowestSeenLoad = taskListCost;
        }
        // otherwise we can keep going
      }
    }
    if (candidateDriver == null)
      throw new IllegalStateException("There should always be a candidate driver");
    return candidateDriver;
  }

  /**
   * Fetch the {@link Tasklist} for a given {@link Driver}, assuming we have one. Returns
   * <pre>null</pre> if we don't.
   *
   * @param driver Driver to fetch a {@link Tasklist} for.
   * @return Tasklist for the specified Driver, if any, otherwise, <pre>null</pre>.
   */
  @Nullable @Override
  public Tasklist tasklistForDriver(final @NotNull Driver driver) {
    if (!this.taskboard.containsKey(driver))
      return null;
    return new Tasklist(driver, this.taskboard.get(driver));
  }

  /**
   * Export the current task board for use in reporting, etc.
   *
   * @return The current state of {@link BlindTaskManager}.
   */
  @NotNull @Override
  public Map<Driver, LinkedHashSet<Task>> export() {
    return Collections.unmodifiableMap(this.taskboard);
  }
}
