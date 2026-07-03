## 2024-10-24 - Missing Cache Eviction in Spring Service
**Learning:** Found a performance optimization (adding `@Transactional(readOnly = true)`) but discovered that caching (`@Cacheable`) was implemented for reads without corresponding `@CachePut` or `@CacheEvict` on updates/deletes.
**Action:** Always verify that performance optimizations (like adding caching or transactions) don't mask or ignore correctness issues (like stale data).
