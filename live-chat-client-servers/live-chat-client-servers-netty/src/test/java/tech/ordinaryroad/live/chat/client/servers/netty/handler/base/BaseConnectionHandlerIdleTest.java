package tech.ordinaryroad.live.chat.client.servers.netty.handler.base;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.timeout.IdleStateEvent;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 读空闲检测：收到 READER_IDLE 时关闭连接并触发 onDisconnected（由上层自动重连）
 */
class BaseConnectionHandlerIdleTest {

    static class TestHandler extends BaseConnectionHandler<TestHandler> {
        TestHandler(IBaseConnectionListener<TestHandler> listener) {
            super(() -> null, listener);
        }

        @Override
        public long getHeartbeatPeriod() {
            return 0;
        }

        @Override
        public long getHeartbeatInitialDelay() {
            return 0;
        }
    }

    @Test
    void readerIdleClosesChannelAndNotifiesDisconnected() {
        AtomicInteger disconnected = new AtomicInteger();
        EmbeddedChannel channel = new EmbeddedChannel(new TestHandler(new IBaseConnectionListener<TestHandler>() {
            @Override
            public void onDisconnected(TestHandler handler) {
                disconnected.incrementAndGet();
            }
        }));
        assertTrue(channel.isActive());

        channel.pipeline().fireUserEventTriggered(IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT);
        channel.runPendingTasks();

        assertFalse(channel.isActive());
        assertEquals(1, disconnected.get());
    }

    @Test
    void writerIdleIsIgnored() {
        AtomicInteger disconnected = new AtomicInteger();
        EmbeddedChannel channel = new EmbeddedChannel(new TestHandler(new IBaseConnectionListener<TestHandler>() {
            @Override
            public void onDisconnected(TestHandler handler) {
                disconnected.incrementAndGet();
            }
        }));

        channel.pipeline().fireUserEventTriggered(IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT);
        channel.runPendingTasks();

        assertTrue(channel.isActive());
        assertEquals(0, disconnected.get());
        channel.finishAndReleaseAll();
    }
}
