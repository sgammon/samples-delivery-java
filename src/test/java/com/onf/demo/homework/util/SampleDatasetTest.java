package com.onf.demo.homework.util;


import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;


/**
 * Small testsuite for the {@link SampleDataset} class, which is used throughout this codebase as a container for
 * generated sample data.
 */
public final class SampleDatasetTest {
  @Test
  public void testGenerate() {
    final SampleDataset dataset = SampleDataset.generateDataset();
    assertNotNull("randomly-generating a dataset should work", dataset);
    assertNotNull("randomly-generated dataset should provide underlying tasks", dataset.getGeneratedTasks());
    assertNotNull("randomly-generated dataset should provide underlying drivers", dataset.getGeneratedDrivers());
  }

  @Test
  public void testLoadKnown() throws IOException {
    final SampleDataset dataset = SampleDataset.loadKnownDataset(KnownDataset.TEST_SAMPLE);
    assertNotNull("loading a known dataset should work", dataset);
  }
}
