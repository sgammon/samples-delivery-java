package com.onfleet.demo.homework;


import com.onfleet.demo.homework.collection.Tasklist;
import com.onfleet.demo.homework.manager.BlindTaskManager;
import com.onfleet.demo.homework.manager.TaskManager;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Map;


/**
 * Specifies a common interface between {@link TaskManager} and {@link BlindTaskManager},
 * where {@link Task} records may be assigned to {@link Driver} {@link Tasklist}s.
 */
public interface TaskAssigner {
  /**
   * Assign a {@link Task} record to a {@link Driver}, in the underlying task management
   * implementation.
   *
   * @param driver Driver to assign the task to.
   * @param task Task to assign.
   */
  void assignToDriver(final @NotNull Driver driver, final @NotNull Task task);

  /**
   * Assign an {@link Iterable} of {@link Task} records to a {@link Driver}. Same effect
   * as {@link #assignToDriver(Driver, Task)} but for multiple tasks.
   *
   * @param driver Driver to assign the tasks to.
   * @param tasks Tasks to assign.
   */
  void assignToDriver(final @NotNull Driver driver, final @NotNull Iterable<Task> tasks);

  /**
   * Resolve the lowest-cost {@link Driver} assignment possible for a given {@link Task}.
   *
   * @param task Task to be considered.
   * @return Driver to be assigned the task.
   */
  @NotNull Driver resolveLowestCostAssignment(final @NotNull Task task);

  /**
   * Fetch the {@link Tasklist} for a given {@link Driver}, assuming we have one. Returns
   * <pre>null</pre> if we don't.
   *
   * @param driver Driver to fetch a {@link Tasklist} for.
   * @return Tasklist for the specified Driver, if any, otherwise, <pre>null</pre>.
   */
  @Nullable Tasklist tasklistForDriver(final @NotNull Driver driver);

  /**
   * Export the current state of {@link TaskManager} into a sorted map.
   *
   * @return Current task-board, sans pre-computed weighting data.
   */
  @NotNull Map<Driver, LinkedHashSet<Task>> export();
}
