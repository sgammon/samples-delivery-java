
## Managers

Classes in this package are compliant with `TaskAssigner`, which presents an interface for a management object
that manages `Driver` records and their assigned `Task` records in some way.

There are two implementations of `TaskAssigner`:
- `TaskManager`: works in conjunction with `Tasklist` to manage pre-computed load stats for each `Driver`, making it
  easy to resolve the cheapest-assignment for a given `Task`
- `BlindTaskManager`: takes the output of `TaskManager` and strips it away so we can simulate similar algorithms, but
  on-read, with no awareness or control over the code that produced it
