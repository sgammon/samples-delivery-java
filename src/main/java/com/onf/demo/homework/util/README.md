
## Utilities

Static tools used for various things, like generating data, reading files, and so on.
Tools are contained under `com.onf.demo.homework.util`:

- `DatasetSpec`: specifies the parameters of a `SampleDataset`, used during dataset generation and serialized in/out
  with the persistable JSON data format
- `FileUtil`: routines for reading JAR-embedded files
- `KnownDataset`: enumeration of JAR-embedded datasets, runnable from the CLI
- `NameHelper`: tool that loads random name data and provides it to `ObjectGenerator`
- `ObjectGenerator`: generates random `Driver`, `Location`, and `Task` records
- `SampleDataset`: contains a set of generated sample data, ready for processing via `TaskManager`
