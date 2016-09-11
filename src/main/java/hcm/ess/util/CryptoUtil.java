package hcm.ess.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

@Service
public class CryptoUtil {

	private static final Logger logger = LoggerFactory.getLogger(CryptoUtil.class);

	private static final String SECRET_KEY = "DH#9I*wx0j2lW5vQ";
	private static final String IV = "c&oxPBxzW9pd+k8X";
	private static final String ALGORITHM = "AES";
	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

	public String encrypt(String source) {
		String result = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(StringUtils.getBytesUtf8(IV));
			SecretKey key = new SecretKeySpec(StringUtils.getBytesUtf8(SECRET_KEY), ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] encryptedBytes = cipher.doFinal(StringUtils.getBytesUtf8(source));
			result = Base64Utils.encodeToString(encryptedBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			logger.error("Fail to encrypt a string: " + e.getMessage());
		}
		return result;
	}

	public String decrypt(String encryptedString) {
		String result = null;
		byte[] encryptedBytes = Base64Utils.decodeFromString(encryptedString);
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(StringUtils.getBytesUtf8(IV));
			SecretKey key = new SecretKeySpec(StringUtils.getBytesUtf8(SECRET_KEY), ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] originalBytes = cipher.doFinal(encryptedBytes);
			result = StringUtils.newStringUtf8(originalBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			logger.error("Fail to decrypt a string: " + e.getMessage());
		}
		return result;
	}
}
