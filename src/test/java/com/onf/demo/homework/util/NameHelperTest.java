package com.onf.demo.homework.util;


import com.onf.demo.homework.FixturedTest;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Tests {@link NameHelper}, which parses random name data and generates random
 * names for {@link com.onf.demo.homework.struct.Driver} records.
 */
@SuppressWarnings("unused")
public class NameHelperTest extends FixturedTest {
  @Test
  public void testConstructNameHelperRaw() {
    final List<String> firstNames = Collections.singletonList("John");
    final List<String> lastNames = Collections.singletonList("Doe");
    final NameHelper helper = new NameHelper(firstNames, lastNames);
    assertNotNull("testsuite first names should work", helper.getFirstNames());
    assertNotNull("testsuite last names should work", helper.getLastNames());
  }

  @Test
  public void testConstructNameHelperFromData() throws IOException {
    final NameHelper helper = new NameHelper();
    assertNotNull("testsuite first names should work", helper.getFirstNames());
    assertNotNull("testsuite last names should work", helper.getLastNames());
    assertTrue("testsuite should have first names", !helper.getFirstNames().isEmpty());
    assertTrue("testsuite should have last names", !helper.getLastNames().isEmpty());
  }

  @Test
  public void testConstructNameHelperIterator() throws IOException {
    final SampleDataset dataset = this.getSampleDataset();
    final NameHelper nameHelper = dataset.getNames();

    assertTrue("NameHelper.hasNext should always be true", nameHelper.hasNext());
    assertNotNull("NameHelper.next should return a randomly-generated name", nameHelper.next());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testConstructNameHelperIteratorRemove() throws IOException {
    final SampleDataset dataset = this.getSampleDataset();
    final NameHelper nameHelper = dataset.getNames();
    nameHelper.remove();
  }
}
