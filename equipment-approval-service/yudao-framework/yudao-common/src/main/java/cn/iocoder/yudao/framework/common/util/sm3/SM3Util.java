package cn.iocoder.yudao.framework.common.util.sm3;

import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;

public class SM3Util {

    public static String SM3(String content) throws UnsupportedEncodingException {
        byte[] md = new byte[32];
        byte[] msg1 = content.getBytes("UTF-8");
        SM3Digest sm3 = new SM3Digest();
        sm3.update(msg1, 0, msg1.length);
        sm3.doFinal(md, 0);
        String s = new String(Hex.encode(md));
        return s.toUpperCase();
    }

}
