package ar.com.arqdx.queue.manager.message;

public class QueueMessageTX extends ArqDxQueueMessage {
    private String id;
    private String value;

    public QueueMessageTX(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public QueueMessageTX getQueueMessage(String value) {
        return new QueueMessageTX("1", value);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MQMessageTX{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public Object getQueueMessage(Object v1) {
        return null;
    }
}

