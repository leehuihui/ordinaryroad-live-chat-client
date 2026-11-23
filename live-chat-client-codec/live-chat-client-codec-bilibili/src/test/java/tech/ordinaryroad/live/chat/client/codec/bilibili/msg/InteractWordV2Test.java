package tech.ordinaryroad.live.chat.client.codec.bilibili.msg;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliCmdEnum;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * INTERACT_WORD_V2：2025-09 起 B站以 protobuf（data.pb，base64）下发进场 / 关注 / 分享
 */
class InteractWordV2Test {

    private static byte[] message(Writer writer) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CodedOutputStream out = CodedOutputStream.newInstance(bos);
        writer.write(out);
        out.flush();
        return bos.toByteArray();
    }

    interface Writer {
        void write(CodedOutputStream out) throws IOException;
    }

    @Test
    void parseEnterRoom() throws IOException {
        byte[] base = message(out -> {
            out.writeString(1, "ignored");
            out.writeString(2, "https://i0.hdslb.com/face.jpg");
        });
        byte[] uinfo = message(out -> {
            out.writeInt64(1, 12345L);
            out.writeBytes(2, ByteString.copyFrom(base));
        });
        byte[] pb = message(out -> {
            out.writeInt64(1, 12345L);
            out.writeString(2, "张三");
            out.writeString(3, "未知字段，应被跳过");
            out.writeInt64(5, 1L);
            out.writeInt64(7, 1795431604L);
            out.writeBytes(22, ByteString.copyFrom(uinfo));
            out.writeInt64(99, 7L);
        });

        InteractWordMsg.Data data = InteractWordMsg.parseV2Data(Base64.getEncoder().encodeToString(pb));
        InteractWordMsg msg = new InteractWordMsg();
        msg.setData(data);

        assertEquals(12345L, data.getUid());
        assertEquals("12345", msg.getUid());
        assertEquals("张三", msg.getUsername());
        assertEquals(1, data.getMsg_type());
        assertEquals(1795431604L, data.getTimestamp());
        assertEquals("https://i0.hdslb.com/face.jpg", msg.getUserAvatar());
        assertNull(msg.getBadgeName());
        assertEquals(0, msg.getBadgeLevel());
    }

    @Test
    void parseMinimalMessage() throws IOException {
        byte[] pb = message(out -> {
            out.writeInt64(1, 1L);
            out.writeInt64(5, 2L);
        });
        InteractWordMsg.Data data = InteractWordMsg.parseV2Data(Base64.getEncoder().encodeToString(pb));
        assertEquals(1L, data.getUid());
        assertEquals(2, data.getMsg_type());
        assertNull(data.getUname());
        assertNull(data.getUinfo());
    }

    @Test
    void cmdEnumRecognized() {
        assertEquals(BilibiliCmdEnum.INTERACT_WORD_V2, BilibiliCmdEnum.getByString("INTERACT_WORD_V2"));
        assertEquals(BilibiliCmdEnum.INTERACT_WORD, BilibiliCmdEnum.getByString("INTERACT_WORD"));
    }
}
