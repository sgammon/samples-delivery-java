package com.onfleet.demo.homework.util;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onfleet.demo.homework.struct.Driver;
import com.onfleet.demo.homework.struct.Location;
import com.onfleet.demo.homework.struct.Task;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;


/**
 * Represents a generated (or loaded) sample dataset, comprised of {@link com.onfleet.demo.homework.struct.Location},
 * {@link Driver}, and {@link Task} records. This dataset structure is used before tasks are assigned.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public final class SampleDataset implements Serializable {
  // -- internals -- //
  final static long serialVersionUID = 1L;

  /**
   * Generated {@link Task} objects.
   */
  final @NotNull Collection<Task> generatedTasks;

  /**
   * Generated {@link Driver} objects.
   */
  final @NotNull Collection<Driver> generatedDrivers;

  /**
   * Loaded or generated person names.
   */
  final @NotNull NameHelper names;

  // -- constructor -- //
  /**
   * JSON creator and raw constructor for a {@link SampleDataset} object.
   *
   * @param generatedTasks Tasks to store in this dataset.
   * @param generatedDrivers Drivers to store in this dataset.
   * @param nameHelper Name dataset to generate additional names from.
   */
  @JsonCreator
  SampleDataset(final @NotNull @JsonProperty("tasks") Collection<Task> generatedTasks,
                final @NotNull @JsonProperty("drivers") Collection<Driver> generatedDrivers,
                final @NotNull @JsonProperty("names") NameHelper nameHelper) {
    this.generatedTasks = Collections.unmodifiableCollection(generatedTasks);
    this.generatedDrivers = Collections.unmodifiableCollection(generatedDrivers);
    this.names = nameHelper;
  }

  // -- static API -- //
  /**
   * Entrypoint function for generating a full dataset of sample {@link Task} and
   * {@link Driver} records.
   *
   * @return Newly-generated {@link SampleDataset}.
   */
  public static SampleDataset generateDataset() {
    return generateDataset(DatasetSpec.defaultSpec());
  }

  /**
   * Entrypoint function for generating a full dataset of sample {@link Task} and
   * {@link Driver} records.
   *
   * @param spec Dataset spec to use when generating this dataset.
   * @return Newly-generated {@link SampleDataset}.
   */
  public static SampleDataset generateDataset(final @NotNull DatasetSpec spec) {
    try {
      // read naming data
      final NameHelper nameHelper = new NameHelper();

      // allocate lists
      final int estimatedTasks = spec.getNumberOfDrivers() * (spec.getTasksPerDriver() - spec.getVarianceInTasksPerDriver());
      final List<Driver> generatedDrivers = new ArrayList<>(spec.getNumberOfDrivers());
      final List<Task> generatedTasks = new ArrayList<>(estimatedTasks);

      // generate some drivers
      int driverIndex = 0;
      int tasksToGenerate = 0;
      final int variance = (spec.getTasksPerDriver() - spec.getVarianceInTasksPerDriver());
      while (driverIndex <= spec.getNumberOfDrivers()) {
        // decide how many tasks, roughly, this driver will have, and add it to the total
        final int tasks = spec.getTasksPerDriver() - RandomNumberUtil.getRandomGenerator().nextInt(variance);
        tasksToGenerate += tasks;

        // generate a driver
        final Driver generatedDriver = Driver.factory(nameHelper.generateName());
        generatedDrivers.add(generatedDriver);
        driverIndex++;
      }

      // generate a location per task
      int driverTaskIndex = 0;
      while (driverTaskIndex <= tasksToGenerate) {
        // generate a random location
        final Location generatedLocation = ObjectGenerator.generateLocation();
        final Task generatedTask = Task.factory(generatedLocation);
        generatedTasks.add(generatedTask);

        driverTaskIndex++;
      }

      return new SampleDataset(generatedTasks,
                               generatedDrivers,
                               nameHelper);
    } catch (final IOException e) {
      // throw as runtime exception (would NOT do this in production)
      throw new RuntimeException(e);
    }
  }

  /**
   * Load a known dataset directly from JAR resources.
   *
   * @param dataset Known dataset to load.
   * @return Loaded {@link SampleDataset} from the target JSON file.
   * @throws IOException if the underlying dataset cannot be loaded.
   */
  public static SampleDataset loadKnownDataset(final KnownDataset dataset) throws IOException {
    final String dataFile = dataset.dataFile();

    // read the data file
    final String fileContents = new FileUtil().fileContentsFromJARAsString(dataFile);

    // load the data file via JSON
    return new ObjectMapper().readerFor(SampleDataset.class).readValue(fileContents);
  }

  // -- getters -- //
  /**
   * @return Collection of {@link Task} objects.
   */
  @NotNull @JsonProperty("tasks")
  public Collection<Task> getGeneratedTasks() {
    return generatedTasks;
  }

  /**
   * @return Collection of {@link Driver} objects.
   */
  @NotNull @JsonProperty("drivers")
  public Collection<Driver> getGeneratedDrivers() {
    return generatedDrivers;
  }

  /**
   * @return Collection of names and name tools with {@link NameHelper}.
   */
  @NotNull @JsonProperty("names")
  public NameHelper getNames() {
    return names;
  }
}
