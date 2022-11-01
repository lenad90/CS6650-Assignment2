import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ChannelBufferFactory extends BasePooledObjectFactory<Channel> {
  private final Connection connection;
  private int count;

  public ChannelBufferFactory(Connection connection) {
    this.connection = connection;
    this.count = 0;
  }

  @Override
  synchronized public Channel create() throws Exception {
    count ++;
    Channel chan = connection.createChannel();
    return chan;
  }

  @Override
  public PooledObject<Channel> wrap(Channel channel) {
    return new DefaultPooledObject<>(channel);
  }

  public int getChannelCount() {
    return count;
  }
}
