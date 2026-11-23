package tech.ordinaryroad.live.chat.client.douyin.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 抖音 WebSocket 握手 Cookie 拼接（2026-04 起抖音只向登录用户推送礼物消息）
 */
class DouyinWebSocketCookieTest {

    @Test
    void noCookieUsesGuestTtwid() {
        assertEquals("ttwid=T", DouyinLiveChatClient.buildWebSocketCookie(null, "T"));
        assertEquals("ttwid=T", DouyinLiveChatClient.buildWebSocketCookie("", "T"));
        assertEquals("ttwid=T", DouyinLiveChatClient.buildWebSocketCookie("   ", "T"));
    }

    @Test
    void loginCookieWithoutTtwidAppendsGuestTtwid() {
        assertEquals("sessionid=abc; uid_tt=1; ttwid=T",
                DouyinLiveChatClient.buildWebSocketCookie("sessionid=abc; uid_tt=1", "T"));
        assertEquals("sessionid=abc; ttwid=T",
                DouyinLiveChatClient.buildWebSocketCookie("sessionid=abc;", "T"));
    }

    @Test
    void loginCookieWithTtwidKeptAsIs() {
        assertEquals("sessionid=abc; ttwid=REAL; x=1",
                DouyinLiveChatClient.buildWebSocketCookie("sessionid=abc; ttwid=REAL; x=1", "T"));
        assertEquals("sessionid=abc; ttwid=REAL",
                DouyinLiveChatClient.buildWebSocketCookie("  sessionid=abc; ttwid=REAL  ", "T"));
    }
}
