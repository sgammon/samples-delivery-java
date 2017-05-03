
## CLI Interface

This module provides classes that enable `delivertool`, a command-line interface to this library.
Basically, there are two classes under `com.onf.demo.homework.cli`:
- `AppLogger`: logs things to the console, if enabled, usually for use via the CLI
- `Main`: actual CLI runner, built atop Apache Commons CLI for parsing options
