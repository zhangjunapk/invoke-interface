package org.zj.interfaceinvoke.base.util;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class AESUtil {

    private static final String AES = "AES";
    private static final String SHA1PRNG = "SHA1PRNG";
    private static final String UTF8 = "UTF-8";
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    /**
     * 加密
     *
     * @param content
     *            需要加密的内容
     * @param password
     *            加密密码
     */
    public static String encrypt(String content, String password) {
        try {
            if(content==null|| "".equals(content) || password==null){
                return null;
            }
            KeyGenerator kgen = KeyGenerator.getInstance(AES);
            SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
            random.setSeed(password.getBytes(UTF8));
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES);
            byte[] byteContent = content.getBytes(UTF8);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            // 加密
            return parseByte2HexStr(result) ;
        } catch (Exception e) {
            throw new SecurityException("AES加密失败",e);
        }
    }

    /**
     * 解密
     *
     * @param content
     *            待解密内容
     * @param password
     *            解密密钥
     * @return
     */
    public static String decrypt(String content, String password) {
        try {
            if(content==null|| "".equals(content) || password==null){
                return null;
            }

            KeyGenerator kgen = KeyGenerator.getInstance(AES);
            SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
            random.setSeed(password.getBytes(UTF8));
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(parseHexStr2Byte(content));
            // 解密
            return new String(result,"UTF-8") ;
        } catch (Exception e) {
            throw new SecurityException("AES解密失败",e);
        }
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if(hexStr.length() < 1){
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     *
     * @param data
     * @param key
     * @param salt
     */
    public static byte[] encrypt(byte[] data, byte[] key, String salt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k, initIvParameterSpec(salt));
        return cipher.doFinal(data);
    }

    /**
     *
     * @param key
     * @return
     */
    private static Key toKey(byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    /**
     *
     * @param salt
     * @return
     */
    public static IvParameterSpec initIvParameterSpec(String salt) {
        return new IvParameterSpec(String.format("%16s", salt).getBytes());
    }
}
