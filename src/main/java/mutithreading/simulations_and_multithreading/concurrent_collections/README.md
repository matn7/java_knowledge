# ConcurrentHashMap

- Supports full concurrency during retrieval. Retrieval operations do not block even if adds are running concurrently.
-  Reads can happen fast, while writes require a lock. Write lock is acquired at granular level whole table is not
  locked only segments are locked. So very rare chance of read waiting on write to complete.
- Iterations do not throw concurrent modification exception (fail safe).
- Can be used in cases where a lot of concurrent addition happened followed by concurrent reads later on.
- Null key not allowed. (not sure if null mapped or not to null value).
- Operations are atomic.