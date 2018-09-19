package HTTPTEST;

import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.ObjectInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;  
  
import javax.crypto.Cipher;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;  
  
public class RSAUtil {  
      
    /** 指定加密算法为RSA */  
    private static final String ALGORITHM = "RSA";  
    /** 指定公钥存放文件 */  
    private static String PUBLIC_KEY_FILE = "PublicKey";  
    /** 指定私钥存放文件 */  
    private static String PRIVATE_KEY_FILE = "PrivateKey";

    private static String key = "od3J6Efns2ZNQvN0dskwKFe6T6l8FKisFbj9THZ%2Bh%2BpnwoxKvkhC6bHW%2F6kMpHr%2BBCfaKW3%2FaTNu%0D%0A%2FLQhSaHkBC4iJH3Dj%2BZUALvJ5P%2BPEMyFCpmWa0TA%2BIFubyZ0oXOr5ZG7j4zsEwf867k0xd5CpX8o%0D%0AcjC%2FSwE0F10FroQpfVc%3D";
  
    public static void main(String[] args) throws Exception {  
          
        String source = "ThinkWin813";// 要加密的字符串
        System.out.println("准备用公钥加密的字符串为：" + source);  
          
        String cryptograph = encrypt(source);// 生成的密文  
        System.out.println("用公钥加密后的结果为:" + cryptograph);

        String str=URLEncoder.encode(cryptograph);
         System.out.println(str);

      //   System.out.println(URLDecoder.decode(str));


        String target = decrypt(cryptograph);// 解密密文

      //  String target = decrypt(URLDecoder.decode(key));// 解密密文
       System.out.println("用私钥解密后的字符串为：" + target);
    }
      
    /** 
     * 加密方法 
     * @param source 源数据 
     * @return 
     * @throws Exception 
     */  
    public static String encrypt(String source) throws Exception {  
          
        Key publicKey = getKey(PUBLIC_KEY_FILE);  
  
        /** 得到Cipher对象来实现对源数据的RSA加密 */  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        byte[] b = source.getBytes();  
        /** 执行加密操作 */  
        byte[] b1 = cipher.doFinal(b);  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(b1);  
    }  
  
    /** 
     * 解密算法 
     * @param cryptograph    密文 
     * @return 
     * @throws Exception 
     */  
    public static String decrypt(String cryptograph) throws Exception {  
          
        Key privateKey = getKey(PRIVATE_KEY_FILE);  
  
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        BASE64Decoder decoder = new BASE64Decoder();  
        byte[] b1 = decoder.decodeBuffer(cryptograph);  
  
        /** 执行解密操作 */  
        byte[] b = cipher.doFinal(b1);  
        return new String(b);  
    }  
      
    private static Key getKey(String fileName) throws Exception, IOException {  
        Key key;  
        ObjectInputStream ois = null;  
        try {  
            /** 将文件中的私钥对象读出 */  
//            ois = new ObjectInputStream(new FileInputStream(fileName));  
            ois = new ObjectInputStream(new FileInputStream("E:\\项目文件\\中油瑞飞\\"+fileName));
            key = (Key) ois.readObject();  
        } catch (Exception e) {  
            throw e;  
        } finally {  
            ois.close();  
        }  
        return key;  
    }  
}  
