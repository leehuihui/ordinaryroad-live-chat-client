/*
 * MIT License
 *
 * Copyright (c) 2023 OrdinaryRoad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tech.ordinaryroad.live.chat.client.servers.netty.client.config;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.ordinaryroad.live.chat.client.commons.client.config.BaseLiveChatClientConfig;

import java.net.URI;

/**
 * @author mjz
 * @date 2023/8/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseNettyClientConfig extends BaseLiveChatClientConfig {

    /**
     * 聚合器允许的最大消息体长度，默认 64*1024 byte
     *
     * @see HttpObjectAggregator#HttpObjectAggregator(int)
     */
    @Builder.Default
    private int aggregatorMaxContentLength = 64 * 1024;

    /**
     * WebSocketClientHandshaker最大消息体长度，默认 64*1024 byte
     *
     * @see WebSocketClientHandshakerFactory#newHandshaker(URI, WebSocketVersion, String, boolean, HttpHeaders, int)
     */
    @Builder.Default
    private int maxFramePayloadLength = 64 * 1024;

    /**
     * 读空闲超时（秒）：超过该时间没有收到任何数据就主动关闭连接，交给自动重连处理。
     * <p>
     * 用于发现「网络已断但对端没有发送 RST」的假死连接（否则要等 TCP keepalive，约 2 小时）。
     * 0 表示关闭（默认，与旧版本行为一致）。开启时应大于平台心跳回复间隔，建议 60 以上。
     *
     * @see io.netty.handler.timeout.IdleStateHandler
     */
    @Builder.Default
    private int readerIdleTimeSeconds = 0;
}
