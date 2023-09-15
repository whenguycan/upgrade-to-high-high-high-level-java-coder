 /**
 * *********************** 版权声明 ***********************************
 *
 * 版权所有：浙江大华技术股份有限公司
 * ©CopyRight DahuaTech 2016
 *
 * *******************************************************************
*/
package com.ruoyi.project.parking.dahua.icc;

 import com.ruoyi.common.utils.StringUtils;
 import org.apache.commons.codec.binary.Base64;

 import javax.crypto.Cipher;
 import javax.crypto.KeyGenerator;
 import javax.crypto.SecretKey;
 import javax.crypto.spec.SecretKeySpec;
 import java.security.SecureRandom;

/**
 * AES加密工具
 * @author   张黎8 27477
 * @date     2016年6月7日
 */
public class AESUtil {

	private static final String ENCODEING = "UTF-8";

	private static final String ALGORITHM = "AES";//加密算法

	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//算法/工作模式/填充方式


    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncryptToHexString(String content, String encryptKey) throws Exception {
    	return byteArrayToHexString(aesEncryptToBytes(content, encryptKey));
    }


  /**
   * 将base 64 code AES解密
   * @param encryptStr 待解密的base 64 code
   * @param decryptKey 解密密钥
   * @return 解密后的string
   * @throws Exception
   */
  public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
      return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
  }

  /**
   * 将base 64 code AES解密
   * @param encryptStr 待解密的base 64 code
   * @param decryptKey 解密密钥
   * @return 解密后的string
   * @throws Exception
   */
  public static String aesDecryptHexString(String encryptStr, String decryptKey) throws Exception {
	  return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(hexStringToByteArray(encryptStr), decryptKey);
  }

	/**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {

		SecretKeySpec skeySpec = getSecretKeySpec(encryptKey);

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);// 创建密码器
		byte[] byteContent = content.getBytes(ENCODEING);

		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);// 初始化
		return cipher.doFinal(byteContent);
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
    	SecretKeySpec skeySpec = getSecretKeySpec(decryptKey);

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);// 创建密码器

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes, ENCODEING);
    }

    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : Base64.decodeBase64(base64Code.getBytes());
    }

	/**
	 *
	 * @param secureKey
	 * @return
	 * @throws Exception
	 */
	private static SecretKeySpec getSecretKeySpec(String secureKey) throws Exception{
		if(secureKey == null || secureKey.trim().equals("") || secureKey.length() != 16){
			throw new Exception("密钥不能为空或密钥长度不对");
		}
		byte[] raw = secureKey.getBytes(ENCODEING);
		SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
		return skeySpec;
	}

	public static String encryptByRandomSeed(String plaintext, String keyRandomSeed) throws Exception{
		SecretKeySpec sks = getSecretKeySpecByRandomSeed(keyRandomSeed);
		Cipher encryptCipher = getCipher(Cipher.ENCRYPT_MODE, sks);
		byte[] result = encryptCipher.doFinal(plaintext.getBytes(ENCODEING));
		return  base64Encode(result);
	}

	public static String decryptByRandomSeed(String ciphertext, String keyRandomSeed) throws Exception {
		SecretKeySpec sks = getSecretKeySpecByRandomSeed(keyRandomSeed);
		Cipher decryptCiphe = getCipher(Cipher.DECRYPT_MODE, sks);//initDecryptCipher(secureKey);
		byte[] result =  decryptCiphe.doFinal(base64Decode(ciphertext));
		return new String(result, ENCODEING);
	}

	private static SecretKeySpec getSecretKeySpecByRandomSeed(String randomSeed){
		SecretKeySpec sks = null;
		try {
			KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
			//安全随机数生成器
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");//使用默认的SHA1PRNG算法
			secureRandom.setSeed(randomSeed.getBytes(ENCODEING));
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] secretKeyEncoded = secretKey.getEncoded();
			sks = new SecretKeySpec(secretKeyEncoded, ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sks;
	}

	/**
	 *
	 * @param cipherMode
	 * @param sks
	 * @return
	 * @throws Exception
	 */
	private static Cipher getCipher(int cipherMode, SecretKeySpec sks) throws Exception{
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(cipherMode, sks);
		return cipher;
	}

    /**
     * 字节数组转换成十六进制字符串
     * @param array
     * @return
     */
    private static String byteArrayToHexString(byte[] array) {
    	if(array == null){
    		return null;
    	}
    	StringBuffer hexString = new StringBuffer();
    	// 字节数组转换为 十六进制 数
		for (int i = 0; i < array.length; i++) {
			String shaHex = Integer.toHexString(array[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}
		return hexString.toString();
    }

    /**
     * 十六进制字符串转换成字节数组
     * @param array
     * @return
     */
    private static byte[] hexStringToByteArray(String hexStr) {
    	if(StringUtils.isBlank(hexStr)){
    		return null;
    	}
    	int length = hexStr.length()/2;
    	byte[] result = new byte[length];
    	for(int i=0; i<length; i++){
    		int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
    		int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
    		result[i] = (byte)(high*16 + low);
    	}
    	return result;
    }
}
