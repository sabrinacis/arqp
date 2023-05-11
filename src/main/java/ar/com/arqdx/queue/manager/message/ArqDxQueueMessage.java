package ar.com.arqdx.queue.manager.message;

public abstract class ArqDxQueueMessage<T> implements IArqDxMessage {
        public abstract <T> Object getQueueMessage(T v1);
}
