# Threads-practice

## ThreadLocal context

ThreadContext is a map that is scoped to every thread. Casually speaking, every thread has its own instance of a map, and the values are not visible to the other threads. Java application servers/web servers use thread pooling and reuse threads. This means once a thread finished it's web request, it is reused to process the next request. The data within the ThreadContext remains and is valid for the next request.
One of the problems I came across lately was preserving context across different execution threads in Java. It should seem simple enough but depending on your use case it may be a little more than that.

One way is to use **InheritableThreadLocal** to pass ThreadLocal data to child threads as they are created. If you haven’t looked at 
InheritableThreadLocal please do so because it is an AWESOME core Java feature. Essentially, InheritableThreadLocal eliminates writing the relevant code to pass contextual information to child threads AND as with any ThreadLocal, each thread sees a different copy of the data so there are no concurrency concerns.

This pattern does break down though when threads are reused, as is generally the case with Java’s ThreadPools, if instead of creating one off thread I decide to use a cached thread pool (to minimize thread creation overhead), reused threads will maintain the previous value they inherited when they were created. In this case we have to be careful with clearing the context of the thread before we reusing to execute another task by using the existing methods like `clear()`, `remove(Object key)` in InheritableThreadLocal.

In this project we have a runnable task, in that we have the copy of parent thread context, we set the parent context and new 
values to the current running thread and printed the current thread context then we cleared it by calling `clearContext()`.
We created the **thread pool** having 2 threads using **executor service** `ExecutorService es = Executors.newFixedThreadPool(2)`, 
executed the task using `execute()` method of executor service.