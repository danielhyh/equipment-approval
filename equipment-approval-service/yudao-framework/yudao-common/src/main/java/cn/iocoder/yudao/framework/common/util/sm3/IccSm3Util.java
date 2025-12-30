package cn.iocoder.yudao.framework.common.util.sm3;

/**
 * sm3签名工具类
 */
public class IccSm3Util {

    /**
     * 验证签名是否正确
     * @param str 待签名的数据
     * @param sign 签名
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(String str, String sign) throws Exception {
        return generateSignature(str).equals(sign);
    }

    /**
     * 根据内容生成签名
     * @param str 待签名的数据
     * @return 签名
     */
    public static String generateSignature(String str) throws Exception {
        // 去掉换行符，避免跨平台问题
        String normalizedContent = str.replaceAll("\n", "").replaceAll("\r", "");

        // 去除首尾空白字符并将内部空白字符标准化
        normalizedContent = normalizedContent.trim().replaceAll("\\s+", " ");

        // 对字符串计算SM3哈希值
        return SM3Util.SM3(normalizedContent);
    }

}
