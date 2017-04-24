package com.onfleet.demo.homework.util;


import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Test the utility {@link DatasetSpec} class, which is used to specify the parameters of a
 * randomly-generated or loaded {@link SampleDataset}.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class DatasetSpecTest {
  @Test
  public void testGenerateDefaultSpec() {
    final DatasetSpec datasetSpec = DatasetSpec.defaultSpec();
    assertNotNull("default dataset spec should work", datasetSpec);
    assertEquals("default dataset spec should use default value for defaultNumberOfDrivers", DatasetSpec.getDefaultNumberOfDrivers(), datasetSpec.getNumberOfDrivers());
    assertEquals("default dataset spec should use default value for defaultTasksPerDriver", DatasetSpec.getDefaultTasksPerDriver(), datasetSpec.getTasksPerDriver());
    assertEquals("default dataset spec should use default value for defaultVarianceInTasksPerDriver", DatasetSpec.getDefaultVarianceInTasksPerDriver(), datasetSpec.getVarianceInTasksPerDriver());
  }

  @Test
  public void testGenerateForNamedDataset() {
    final DatasetSpec datasetSpec = KnownDataset.TEST_SAMPLE.toSpec();
    final DatasetSpec datasetSpec2 = DatasetSpec.forKnownDataset(KnownDataset.TEST_SAMPLE);
    assertNotNull("testsuite dataset spec should work", datasetSpec);
    assertEquals("testsuite dataset spec name should work", "Default", datasetSpec.getName());
    assertEquals("testsuite dataset spec should use default value for defaultNumberOfDrivers", DatasetSpec.getDefaultNumberOfDrivers(), datasetSpec.getNumberOfDrivers());
    assertEquals("testsuite dataset spec should use default value for defaultTasksPerDriver", DatasetSpec.getDefaultTasksPerDriver(), datasetSpec.getTasksPerDriver());
    assertEquals("testsuite dataset spec should use default value for defaultVarianceInTasksPerDriver", DatasetSpec.getDefaultVarianceInTasksPerDriver(), datasetSpec.getVarianceInTasksPerDriver());
  }

  @Test
  public void testNamedDatasetEquality() {
    final DatasetSpec randomDataset = new DatasetSpec(null, 50, 100, 75);
    final DatasetSpec datasetSpec = KnownDataset.TEST_SAMPLE.toSpec();
    final DatasetSpec datasetSpec2 = DatasetSpec.forKnownDataset(KnownDataset.TEST_SAMPLE);
    assertEquals("testsuite dataset name should be consistent", datasetSpec.getName(), datasetSpec2.getName());
    assertTrue("testsuite dataset equality should work via equals()", datasetSpec.equals(datasetSpec2));
    assertEquals("testsuite dataset equality should work via hashCode()", datasetSpec.hashCode(), datasetSpec2.hashCode());
    assertTrue("testsuite dataset should not match a random one via equals()", !datasetSpec.equals(randomDataset));
    assertNotEquals("testsuite dataset should not match a random one via hashCode()", datasetSpec.hashCode(), randomDataset.hashCode());
    assertNotEquals("testsuite dataset should not match a random object", datasetSpec, 1);
  }

  @Test
  public void testKnownDatasetFormatting() {
    final KnownDataset dataset = KnownDataset.TEST_SAMPLE;
    assertEquals("dataset name() should match toString()", dataset.name(), dataset.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testKnownCompletionForDatafile() {
    final KnownDataset dataset = KnownDataset.UNKNOWN;
    dataset.dataFile();
  }

  @Test(expected = IllegalStateException.class)
  public void testKnownCompletionForSpec() {
    final KnownDataset dataset = KnownDataset.UNKNOWN;
    dataset.toSpec();
  }
}
