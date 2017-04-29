package com.onfleet.demo.homework.manager;


import com.onfleet.demo.homework.TaskAssigner;
import com.onfleet.demo.homework.collection.Tasklist;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Task;
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
   */
  @NotNull @Override
  public Driver resolveLowestCostAssignment(final @NotNull Task task) {
    return null;
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
