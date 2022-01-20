package com.imooc.broker;

import com.google.common.collect.Lists;
import com.imooc.Message;
import java.util.List;

/** @author afu */
public class MessageHolder {
  private final List<Message> messages = Lists.newArrayList();

  public static final ThreadLocal<MessageHolder> HOLDER =
      /* 线程初始化演进
            new ThreadLocal() {
              @Override
              protected Object initialValue() {
                return new MessageHolder();
              }
            }；

            ThreadLocal.withInitial(() -> new MessageHolder());
      */
      ThreadLocal.withInitial(MessageHolder::new);

  public static void add(Message message) {
    HOLDER.get().messages.add(message);
  }

  public static List<Message> clear() {
    List<Message> tmp = Lists.newArrayList(HOLDER.get().messages);
    HOLDER.remove();
    return tmp;
  }
}
