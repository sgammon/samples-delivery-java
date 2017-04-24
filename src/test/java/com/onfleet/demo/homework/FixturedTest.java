package com.onfleet.demo.homework;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.onfleet.demo.homework.util.SampleDataset;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Before;


/**
 * Adds functionality to generate/read fixture data during testsuite execution.
 * Used as an ABC (Abstract Base Class) for other tests.
 */
public abstract class FixturedTest {
  // -- internals -- //
  /**
   * Sample dataset object.
   */
  private @Nullable SampleDataset sampleDataset;

  /**
   * Setup a randomly-generated dataset, for use in tests.
   */
  @Before
  public void setUpFixtures() {
    this.sampleDataset = SampleDataset.generateDataset();
  }

  /**
   * Tear-down the randomly-generated dataset built previously for tests.
   */
  @After
  public void tearDownFixtures() {
    this.sampleDataset = null;
  }

  // -- subclass API -- //
  /**
   * @return Retrieve the locally-generated sample dataset.
   */
  protected SampleDataset getSampleDataset() {
    if (this.sampleDataset == null)
      throw new IllegalStateException("Cannot retrieve sample dataset until after, well, @Before, is dispatched.");
    return this.sampleDataset;
  }

  /**
   * @return Retrieve a prepared Jackson {@link ObjectMapper} for decoding JSON.
   */
  protected ObjectMapper objectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    return mapper;
  }
}