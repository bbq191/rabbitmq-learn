package com.imooc.broker;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步消息处理
 *
 * @author afu
 */
@Slf4j
public class AsyncBaseQueue {
  private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();
  private static final int QUEUE_SIZE = 10000;

  private static final ExecutorService SENDER_ASYNC =
      new ThreadPoolExecutor(
          THREAD_SIZE,
          THREAD_SIZE,
          60L,
          TimeUnit.SECONDS,
          new ArrayBlockingQueue<>(QUEUE_SIZE),
          // lambada 闭包
          /* public Thread newThread(Runnable r) {
             Thread t = new Thread(r);
             t.setName("rabbitmq_client_async_sender");
             return t;
          }*/
          r -> {
            Thread t = new Thread(r);
            t.setName("rabbitmq_client_async_sender");
            return t;
          },
          /* new java.util.concurrent.RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
              log.error("async sender is error rejected, runnable: {}, executor: {}", r, executor);
            }
          } */
          (r, executor) ->
              log.error("async sender is error rejected, runnable: {}, executor: {}", r, executor));

  public static void submit(Runnable runnable) {
    SENDER_ASYNC.submit(runnable);
  }
}
