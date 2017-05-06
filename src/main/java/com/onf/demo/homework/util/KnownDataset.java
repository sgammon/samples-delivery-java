package com.onf.demo.homework.util;


/**
 * Enumerates sample datasets that can be loaded directly from the JAR.
 */
public enum KnownDataset {
  /**
   * Small sample dataset, that is quick to load, for testing.
   */
  TEST_SAMPLE,

  /**
   * Test-case state.
   */
  UNKNOWN;

  /**
   * @return Data file backing the known dataset.
   */
  public String dataFile() {
    switch (this) {
      case TEST_SAMPLE: return "testsuite.json";
      default: throw new IllegalStateException();  // not reachable
    }
  }

  /**
   * @return Return a specification for a known dataset.
   */
  public DatasetSpec toSpec() {
    switch (this) {
      case TEST_SAMPLE: return DatasetSpec.defaultSpec();
      default: throw new IllegalStateException();  // not reachable
    }
  }

  /**
   * @return Use the testsuite's enum name in reporting output.
   */
  public String toString() {
    return this.name();
  }
}
