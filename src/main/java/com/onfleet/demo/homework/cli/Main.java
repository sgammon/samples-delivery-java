
package com.onfleet.demo.homework.cli;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.onfleet.demo.homework.manager.TaskManager;
import com.onfleet.demo.homework.util.FileUtil;
import com.onfleet.demo.homework.util.KnownDataset;
import com.onfleet.demo.homework.util.SampleDataset;
import org.apache.commons.cli.*;

import java.io.IOException;


/**
 * Main runner class for the {@link TaskManager}. Loads up fixture data via a local file specified via the command
 * line, or uses a default file supplied via the <pre>resources/</pre> directory and included in the JAR.
 */
@SuppressWarnings({"UtilityClassCanBeEnum", "UtilityClass"})
public final class Main {
  /**
   * Name of the CLI tool, for the help text.
   */
  private static final String clitoolName = "deliverytool";

  static {
    AppLogger._enableLogging();  // in a CLI context, we want to emit logs
  }

  /**
   * Main entrypoint function for the command-line tool.
   *
   * @param args Command line arguments.
   */
  public static void main(final String[] args) throws ParseException, IOException {
    // create Options object
    Options options = new Options();

    // add help option
    Option help = new Option( "h", "help", false,
                              "print this message");
    options.addOption(help);

    // add quiet option
    Option quiet = new Option( "q", "quiet", false,
                               "squelch most logging messages");
    options.addOption(quiet);

    // add quiet option
    Option verbose = new Option( "v", "verbose", false,
                                 "add more logging messages, -q wins if both are passed");
    options.addOption(verbose);

    // add dataset option
    Option dataset = Option.builder("d")
                         .hasArg(false)
                         .longOpt("dataset")
                         .desc("named dataset to use")
                         .optionalArg(true)
                         .type(KnownDataset.class)
                         .build();
    options.addOption(dataset);

    // add datafile option
    Option datafile = Option.builder("f")
                           .hasArg(false)
                           .longOpt("datafile")
                           .desc("data file to use, either -d or -f may be passed")
                           .optionalArg(true)
                           .build();
    options.addOption(datafile);

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, args);

    // enforce quietness
    if (cmd.hasOption("quiet"))
      AppLogger._disableLogging();

    // enforce verbosity
    else if (cmd.hasOption("verbose"))
      AppLogger._enableLogging(true);

    // apply help text
    if (cmd.hasOption("help")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(clitoolName, options);
    } else {
      // otherwise, execute according to options
      if (cmd.hasOption("dataset") && cmd.hasOption("datafile")) {
        AppLogger.exit("Cannot pass both a dataset and datafile.");
      } else {
        SampleDataset subject;
        if (cmd.hasOption("dataset")) {
          // load a known/embedded dataset
          AppLogger.say("CLI", "Loading known dataset '" + cmd.getOptionValue("dataset").toUpperCase() + "'...");
          final KnownDataset knownDataset = KnownDataset.valueOf(cmd.getOptionValue("dataset"));
          subject = SampleDataset.loadKnownDataset(knownDataset);
        } else if (cmd.hasOption("datafile")) {
          // load an arbitrary data file
          AppLogger.say("CLI", "Loading dataset from file '" + cmd.getOptionValue("datafile") + "'...");
          final String fileContents = new FileUtil().fileContentsFromJARAsString(cmd.getOptionValue("datafile"));
          subject = new ObjectMapper().readerFor(SampleDataset.class).readValue(fileContents);
        } else {
          // default behavior
          AppLogger.say("CLI", "Generating sample dataset...");
          subject = SampleDataset.generateDataset();
        }

        if (subject == null)
          AppLogger.exit("Failed to load dataset.");

        // run the route calculator
        final TaskManager manager = TaskManager.setupWithDataset(subject);
        AppLogger.say("CLI", "Ready to run simulation.");
        System.out.print(manager.report().toString());
        AppLogger.say("CLI", "Finished.");
      }
    }
  }
}
