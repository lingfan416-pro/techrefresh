# Java Collections
| Collection      | Underlying structure                         |
| --------------- | -------------------------------------------- |
| `ArrayList`     | dynamic array                                |
| `LinkedList`    | doubly linked list                           |
| `HashMap`       | hash table + bucket array + linked list/tree |
| `HashSet`       | built on `HashMap`                           |
| `TreeMap`       | red-black tree                               |
| `TreeSet`       | red-black tree                               |
| `PriorityQueue` | binary heap                                  |
| `ArrayDeque`    | circular array                               |

## Time complexity 
| Structure       |    Access |          Search |                   Insert |            Delete |
| --------------- | --------: | --------------: | -----------------------: | ----------------: |
| Array           |      O(1) |            O(n) |                     O(n) |              O(n) |
| `ArrayList`     |      O(1) |            O(n) | append O(1), middle O(n) |              O(n) |
| `LinkedList`    |      O(n) |            O(n) |        O(1) at node/ends | O(1) at node/ends |
| `HashSet`       |         — |        O(1) avg |                 O(1) avg |          O(1) avg |
| `HashMap`       |         — | O(1) avg by key |                 O(1) avg |          O(1) avg |
| `TreeSet`       |         — |        O(log n) |                 O(log n) |          O(log n) |
| `TreeMap`       |         — |        O(log n) |                 O(log n) |          O(log n) |
| `PriorityQueue` | peek O(1) |               — |                 O(log n) |          O(log n) |

## Fail-fast iterator
A fail-fast iterator is an iterator that throws an exception as soon as it detects that the collection was structurally 
modified during iteration by some means other than the iterator itself. In Java, the typical exception is: `ConcurrentModificationException`

### What "structurally modified" means
Usually this means operations that change the collection's size or internal structure, such as:
- add
- remove
- clear
Not every value update counts as structural. For example, replacing a value in some cases may not be structural.

example:
```
List<String> list = new ArrayList();
list.add("A");
list.add("B");
list.add("C");

for(String s : list){
    if(s.equals("B")){
        list.remove(s); //may throw concurrentModificationException
    }
}
```
### Why it is called fail-fast
Because it does not silently continue with potentially inconsistent iteration state. Instead, it fails early and quickly when it 
detects unsafe modification.

### How it works internally
Most standard mutable collections such as ArrayList and HashMap maintain a modification counter, often called something like modCount.
When the iterator is created:
- it stores the current modification count.
During iteration:
- it compares the stored count with the collection's current count.
If they differ:
- it throws `ConcurrentModificationException`
So the iterator is basically saying: The collection changed unexpectedly after I started iterating.
Use the iterator's own remove() method.
```
List<String> list = new ArrayList();
list.add("A");
list.add("B");
list.add("C");

Iterator<String> it = list.iterator();
while(it.hasNext()){
    String s = it.next();
    if(s.equals("B")){
        it.remove(); //this is safe
    }
}

```
This is safe because the iterator knows about the change.

Fail-fast behavior is not a strict guarantee for thread safety. It is a best-effort mechanism to detect illegal concurrent or unexpected modification.
- it helps detect bugs
- it does not mean the collection is thread-safe
- it should not be used as a synchronization mechanism

### Fail-fast vs Fail-safe
#### fail-fast iterator
- iterates over the original collection
- detects structural modification
- throw ConcurrentModificationException
Examples:
- ArrayList
- HashMap
- HashSet

#### Fail-safe iterator
- iterates over a snapshot or concurrent structure
- does not throw ConcurrentModificationException in the same way
- may reflect old or partial data depending on implementation
Examples:
- CopyOnwriteArrayList
- some concurrent collections
```
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
```
Its iterator works on a snapshot, so modification during iteration does not fail in the same way.

Fail-fast behavior is mainly relevant to mutable collections. With immutable collections, structural modification
 is not allowed, so this class of problem is avoided.

## Iterator behavior summary
### Fail-fast
Examples:
- ArrayList
- HashMap
- HashSet
Behavior
- May throw ConcurrentModificationException

### Weakly consistent
The iterator does not lock the whole collection and does not capture the whole snapshot. It just traverses
 the underlying data structure in a way that is safe under concurrent updates
Examples:
- ConcurrentHashMap
- ConcurrentLinkedQueue
- ConcurrentSkipListMap
- ConcurrentSkipListSet
Behavior
- tolerate concurrent modification
- may reflect some but not necessarily all updates
- usually no `ConcurrentModificationException`

### Snapshot iterator
Examples:
- CopyOnWriteArrayList
- CopyOnWriteArraySet
Behavior:
- iterates over a stable snapshot
- does not see later modification during the iteration

Concurrent collection comparison

| Type                                | Category     | Thread-safe | Iterator style                                                                 | Best use case                                | Main trade-off                                  |
| ----------------------------------- | ------------ | ----------: | ------------------------------------------------------------------------------ | -------------------------------------------- | ----------------------------------------------- |
| `ArrayList`                         | List         |          No | fail-fast                                                                      | single-threaded ordered list                 | unsafe for concurrent access                    |
| `Collections.synchronizedList(...)` | List wrapper |         Yes | usually needs external synchronization during iteration                        | simple thread-safe wrapper for existing list | coarse-grained locking, weaker scalability      |
| `CopyOnWriteArrayList`              | List         |         Yes | snapshot                                                                       | read-heavy lists, listeners, configs         | every write copies whole array                  |
| `ConcurrentLinkedQueue`             | Queue        |         Yes | weakly consistent                                                              | high-concurrency non-blocking FIFO           | no blocking/backpressure                        |
| `BlockingQueue`                     | Queue        |         Yes | depends on implementation, generally safe for concurrent producer-consumer use | task queues, producer-consumer               | can block, semantics vary by implementation     |
| `ConcurrentHashMap`                 | Map          |         Yes | weakly consistent                                                              | shared maps, caches, registries, counters    | no global ordering, more complex than `HashMap` |



| Type              | Concurrent modification | Sees updates after iterator created | Throws CME |
| ----------------- | ----------------------- | ----------------------------------- | ---------- |
| Fail-fast         | Not tolerated           | No reliable behavior                | May throw  |
| Weakly consistent | Tolerated               | May see some updates                | Usually no |
| Snapshot          | Tolerated               | No, sees old snapshot only          | No         |


Easy memory trick
- Fail-fast = “changed? stop”
- Weakly consistent = “changed? keep going, maybe see some changes”
- Snapshot = “changed? I only see the old picture”