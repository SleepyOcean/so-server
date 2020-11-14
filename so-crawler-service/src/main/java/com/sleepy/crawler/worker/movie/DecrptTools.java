package com.sleepy.crawler.worker.movie;

import com.google.common.hash.Hashing;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import static javax.crypto.Cipher.DECRYPT_MODE;

/**
 * @author gehoubao
 * @create 2020-11-13 20:32
 **/
public class DecrptTools {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String EMPTY_STR = "";
    private static final int AES_KEY_SIZE = 16;//256/192/128~32/24/16

    public static void main(String[] args) {
        String tempkey = "!@#$%^&*()_+";
        String ming = "at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_77]";

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String en = tripleDesEncrypt(ming, tempkey);
            String den = tripleDesDecrypt(en, tempkey);
        }
        long end = System.currentTimeMillis();
        System.out.println("TripleDES: " + (end - begin));
        System.out.println("TripleDES: " + (end - begin) / 100000.0);

        begin = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String en = desEncrypt(ming, tempkey);
            String den = desDecrypt(en, tempkey);
        }
        end = System.currentTimeMillis();
        System.out.println("DES: " + (end - begin));
        System.out.println("DES: " + (end - begin) / 100000.0);

        begin = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String en = aesEncrypt(ming, tempkey);
            String den = aesDecrypt(en, tempkey);
        }
        end = System.currentTimeMillis();
        System.out.println("AES: " + (end - begin));
        System.out.println("AES: " + (end - begin) / 100000.0);
        /*
        100000次
        key: !@#$%^&*()_+
        src: at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_77]
        TripleDES: 3831
        TripleDES: 0.03831
        DES: 845
        DES: 0.00845
        AES: 888
        AES: 0.00888
        */
    }

    private static final String ENCRYPT = "AES";
    private static final String CIPHER = "AES/CBC/PKCS5Padding";

    /**
     * AES加密
     *
     * @param key 加密密钥
     * @param src 加密内容
     * @return 返回BASE64密文
     */
    public static final String aesEncrypt(String src, String key) {
        if (key == null || src == null) {
            return EMPTY_STR;
        }
        try {
            byte[] bs = getAESResult(key, src.getBytes(DEFAULT_CHARSET), Cipher.ENCRYPT_MODE);
            if (bs != null) {
                return Base64.getEncoder().encodeToString(bs);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return src;
    }

    /**
     * AES解密
     *
     * @param key 解密密钥
     * @param src 解密内容
     * @return 明文
     */
    public static final String aesDecrypt(String src, String key) {
        if (key == null || src == null) {
            return EMPTY_STR;
        }
        try {
            byte[] bs = getAESResult(key, Base64.getDecoder().decode(src.getBytes(DEFAULT_CHARSET)), DECRYPT_MODE);
            if (bs != null) {
                return new String(bs, DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return src;
    }

    /**
     * AES加解密结果
     *
     * @param key         密钥
     * @param textBytes   明文 密文 字节数组
     * @param encryptMode 加密 解密
     * @return
     */
    private static byte[] getAESResult(String key, byte[] textBytes, final int encryptMode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        Key newKey = new SecretKeySpec(buildCLenKey(key, AES_KEY_SIZE), ENCRYPT);
        Cipher cipher = Cipher.getInstance(ENCRYPT);
        cipher.init(encryptMode, newKey, new SecureRandom());
        return cipher.doFinal(textBytes);
    }


    //定义加密算法，有DES、DESede(3DES)
    private static final String ALGORITHM = "TripleDES";
    // 算法名称/加密模式/填充方式
//    private static final String CIPHER_ALGORITHM_ECB = "DESede/ECB/PKCS5Padding";
    private static final String CIPHER_ALGORITHM_ECB = "DESede/CBC/PKCS7Padding";

    /**
     * TripleDES加密方法
     *
     * @param src
     * @param key
     * @return BASE64
     */
    public static final String tripleDesEncrypt(String src, String key) {
        if (key == null || src == null) {
            return EMPTY_STR;
        }
        try {
            byte[] des = getTripleDESResult(key, src.getBytes(), Cipher.ENCRYPT_MODE);
            if (des != null) {
                return Base64.getEncoder().encodeToString(des);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return src;
    }

    /**
     * TripleDES解密函数
     *
     * @param src 密文的字节数组
     * @param key 密钥
     * @return String 明文
     */
    public static final String tripleDesDecrypt(String src, String key) {
        if (key == null || src == null) {
            return EMPTY_STR;
        }
        try {
            byte[] srcb = Base64.getDecoder().decode(src);
            byte[] des = getTripleDESResult(key, srcb, DECRYPT_MODE);
            return new String(des, DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return src;
    }

    /**
     * TripleDES加解密结果
     *
     * @param key         密钥
     * @param textBytes   明文 密文 字节数组
     * @param encryptMode 加密 解密
     * @return
     */
    private static byte[] getTripleDESResult(String key, byte[] textBytes, final int encryptMode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        Security.addProvider(new BouncyCastleProvider());
        Key newKey = new SecretKeySpec(buildCLenKey(Hashing.md5().hashBytes(key.getBytes("UTF-8")).toString(), 24), ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        byte[] iv = "www.pianku.tv".getBytes();
        AlgorithmParameterSpec desKeySpec = new IvParameterSpec(Hashing.md5().hashBytes("www.pianku.tv".getBytes("UTF-8")).toString().substring(0, 8).getBytes("UTF8"));

        cipher.init(encryptMode, newKey, desKeySpec);
        return cipher.doFinal(textBytes);
    }

    /**
     * 根据字符串生成密钥字节数组
     *
     * @param keyStr 密钥字符串
     * @param lgn    密钥长度
     * @return 长度密钥字节数组
     * @throws UnsupportedEncodingException
     */
    private static byte[] buildCLenKey(String keyStr, int lgn) throws UnsupportedEncodingException {
        byte[] key = new byte[lgn];    //声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组

        ///执行数组拷贝
        if (key.length > temp.length) {
            //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }


    private static final String DES_ALGORITHM = "DES";
    public static final String DES_CIPHER_ALGORITHM = "DES";

    /**
     * DES 加密方法
     *
     * @param src
     * @param key
     * @return BASE64
     */
    public static final String desEncrypt(String src, String key) {
        if (key == null || src == null) {
            return EMPTY_STR;
        }
        try {
            byte[] des = getDESResult(key, src.getBytes(), Cipher.ENCRYPT_MODE);
            if (des != null) {
                return Base64.getEncoder().encodeToString(des);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return src;
    }

    /**
     * DES解密函数
     *
     * @param src 密文的字节数组
     * @param key 密钥
     * @return String 明文
     */
    public static final String desDecrypt(String src, String key) {
        if (key == null || src == null) {
            return EMPTY_STR;
        }
        try {
            byte[] srcb = Base64.getDecoder().decode(src);
            byte[] des = getDESResult(key, srcb, DECRYPT_MODE);
            return new String(des, DEFAULT_CHARSET);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return src;
    }

    private static byte[] getDESResult(String key, byte[] textBytes, final int encryptMode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        Key newKey = new SecretKeySpec(buildCLenKey(key, 8), DES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
        cipher.init(encryptMode, newKey, new SecureRandom());
        return cipher.doFinal(textBytes);
    }
}