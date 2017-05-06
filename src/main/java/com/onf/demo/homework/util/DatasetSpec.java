package com.onf.demo.homework.util;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Specifies the boundaries for a sample dataset.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public final class DatasetSpec {
  // -- defaults -- //
  /**
   * Default number of drivers.
   */
  public final static int defaultNumberOfDrivers = 50;

  /**
   * Default average number of tasks per driver.
   */
  public final static int defaultTasksPerDriver = 25;

  /**
   * Default variance in tasks per driver.
   */
  public final static int defaultVarianceInTasksPerDriver = 5;

  // -- internals -- //
  /**
   * Name of the dataset, if any.
   */
  final @Nullable String name;

  /**
   * Number of drivers we should auto-generate.
   */
  final int numberOfDrivers;

  /**
   * Number of tasks, on average, we should generate per-driver.
   */
  final int tasksPerDriver;

  /**
   * Maximum deviation in initial task counts for drivers.
   */
  final int varianceInTasksPerDriver;

  // -- constructor -- //
  /**
   * Create a specification object for a {@link SampleDataset}.
   *
   * @param name Label for this dataset, if it is from a {@link KnownDataset}.
   * @param numberOfDrivers Number of drivers to generate.
   * @param tasksPerDriver Number of tasks to generate per-driver, on average.
   * @param varianceInTasksPerDriver Maximum variance in number of tasks per-driver.
   */
  @JsonCreator
  DatasetSpec(final @JsonProperty("name") @Nullable String name,
              final @JsonProperty("numberOfDrivers") int numberOfDrivers,
              final @JsonProperty("tasksPerDriver") int tasksPerDriver,
              final @JsonProperty("varianceInTasksPerDriver") int varianceInTasksPerDriver) {
    this.name = name;
    this.numberOfDrivers = numberOfDrivers;
    this.tasksPerDriver = tasksPerDriver;
    this.varianceInTasksPerDriver = varianceInTasksPerDriver;
  }

  // -- equality -- //
  /**
   * Recognize identical datasets, if named.
   *
   * @param obj Other dataset.
   * @return Whether the other object matches or not.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DatasetSpec) {
      final DatasetSpec other = (DatasetSpec) obj;

      // if either name is null, one is a generated dataset, and they're not equal
      return !(other.getName() == null || this.getName() == null)
               && other.getName().equals(this.getName());
    }
    return false;  // not equal: not even the same types
  }

  /**
   * Return a dependable hashcode for known datasets.
   *
   * @return Hash code for the dataset's name, if known.
   */
  @Override
  public int hashCode() {
    if (this.getName() != null)
      return this.getName().hashCode();
    return super.hashCode();
  }

  // -- static API -- //
  /**
   * Generate a specification set for a known dataset.
   *
   * @param dataset Dataset to generate a spec for.
   * @return Prepared {@link DatasetSpec}.
   */
  public static DatasetSpec forKnownDataset(final KnownDataset dataset) {
    return dataset.toSpec();
  }

  /**
   * @return Default set of spec parameters.
   */
  public static DatasetSpec defaultSpec() {
    return new DatasetSpec(
        "Default",
        defaultNumberOfDrivers,
        defaultTasksPerDriver,
        defaultVarianceInTasksPerDriver);
  }

  // -- static getters -- //
  /**
   * @return Default number of drivers to generate.
   */
  public static Integer getDefaultNumberOfDrivers() {
    return defaultNumberOfDrivers;
  }

  /**
   * @return Default average number of tasks to generate per driver.
   */
  public static Integer getDefaultTasksPerDriver() {
    return defaultTasksPerDriver;
  }

  /**
   * @return Default variance in average number of tasks to generate per driver.
   */
  public static Integer getDefaultVarianceInTasksPerDriver() {
    return defaultVarianceInTasksPerDriver;
  }

  // -- getters -- //
  /**
   * @return Retrieve the name for this dataset, if known.
   */
  @Nullable @JsonProperty("name")
  public String getName() {
    return name;
  }

  /**
   * @return Number of drivers to generate.
   */
  @NotNull @JsonProperty("numberOfDrivers")
  public Integer getNumberOfDrivers() {
    return numberOfDrivers;
  }

  /**
   * @return Average number of tasks to generate per driver.
   */
  @NotNull @JsonProperty("tasksPerDriver")
  public Integer getTasksPerDriver() {
    return tasksPerDriver;
  }

  /**
   * @return Maximum variance in the number of tasks to generate per driver.
   */
  @NotNull @JsonProperty("varianceInTasksPerDriver")
  public Integer getVarianceInTasksPerDriver() {
    return varianceInTasksPerDriver;
  }

  /**
   * @return Indicate whether the underlying dataset is randomly generated, or not.
   */
  @JsonIgnore
  public boolean isRandom() {
    return this.getName() == null;
  }
}
