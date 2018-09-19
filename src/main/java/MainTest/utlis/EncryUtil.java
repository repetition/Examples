package MainTest.utlis;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * @author Kerwin
 * @Version V1.0
 * @Date 2018/5/2
 * <p>加解密工具类</p>
 */
public class EncryUtil {
    private final static String KEY = "@ThinkWin_IPM";

    /**
     * 加密
     *
     * @param str 要加密的字符串
     * @return 加密后结果
     */
    public static String encrypt(String str) {
        return encrypt(KEY, str);
    }

    /**
     * 加密
     *
     * @param strKey 加密秘钥KEY
     * @param str    需加密字符串
     * @return 加密后结果
     */
    public static String encrypt(String strKey, String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        try {
            DESKeySpec keySpec = new DESKeySpec(strKey.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = Base64.encodeBase64(cipher.doFinal(str.getBytes("UTF-8")));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 解密
     *
     * @param str 需解密字符串
     * @return 解密后结果
     */
    public static String decrypt(String str) {
        return decrypt(KEY, str);
    }

    /**
     * 解密
     *
     * @param strKey 加密秘钥KEY
     * @param str    需解密字符串
     * @return 解密后结果
     */
    public static String decrypt(String strKey, String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec keySpec = new DESKeySpec(strKey.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] base64 = Base64.decodeBase64(str);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key,sr);
            byte[] resultBytes = cipher.doFinal(base64);
            return new String(resultBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

        System.out.println(encrypt("a41043c5cc558822"));
    }
}
