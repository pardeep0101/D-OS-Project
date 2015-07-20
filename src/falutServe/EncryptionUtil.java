package falutServe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import sun.security.*;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import javax.crypto.Cipher;

/**
 * @author Pardeep (template from JavaDigest)
 * 
 */
public class EncryptionUtil {
	private EncryptionUtil(){
		
	}
	/**
	 * String to hold name of the encryption algorithm.
	 */
	public static final String ALGORITHM = "RSA";

	/**
	 * String to hold the name of the private key file.
	 */
	//public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";
	public static final String PRIVATE_KEY_FILE = "private.key";
	/**
	 * String to hold name of the public key file.
	 */
	//public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";
	public static final String PUBLIC_KEY_FILE = "public.key";
	/**
	 * Generate key which contains a pair of private and public key using 1024
	 * bytes. Store the set of keys in Prvate.key and Public.key files.
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void generateKey() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator
					.getInstance(ALGORITHM);
			keyGen.initialize(1024);
			final KeyPair key = keyGen.generateKeyPair();

			File privateKeyFile = new File(PRIVATE_KEY_FILE);
			File publicKeyFile = new File(PUBLIC_KEY_FILE);

			// Create files to store public and private key
			if (privateKeyFile.getParentFile() != null) {
				privateKeyFile.getParentFile().mkdirs();
			}
			privateKeyFile.createNewFile();

			if (publicKeyFile.getParentFile() != null) {
				publicKeyFile.getParentFile().mkdirs();
			}
			publicKeyFile.createNewFile();

			// Saving the Public key in a file
			ObjectOutputStream publicKeyOS = new ObjectOutputStream(
					new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(key.getPublic());
			publicKeyOS.close();

			// Saving the Private key in a file
			ObjectOutputStream privateKeyOS = new ObjectOutputStream(
					new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(key.getPrivate());
			privateKeyOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * The method checks if the pair of public and private key has been
	 * generated.
	 * 
	 * @return flag indicating if the pair of keys were generated.
	 */
	public static boolean areKeysPresent() {

		File privateKey = new File(PRIVATE_KEY_FILE);
		File publicKey = new File(PUBLIC_KEY_FILE);

		if (privateKey.exists() && publicKey.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * Encrypt the plain text using public key.
	 * 
	 * @param text
	 *            : original plain text
	 * @param key
	 *            :The public key
	 * @return Encrypted text
	 * @throws java.lang.Exception
	 */
	public static byte[] encrypt(String text, PublicKey key) {
		byte[] cipherText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}

	/**
	 * Decrypt text using private key.
	 * 
	 * @param text
	 *            :encrypted text
	 * @param key
	 *            :The private key
	 * @return plain text
	 * @throws java.lang.Exception
	 */
	public static String decrypt(byte[] text, PrivateKey key) {
		byte[] dectyptedText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(ALGORITHM);

			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = cipher.doFinal(text);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new String(dectyptedText);
	}

	/**
	 * Test the EncryptionUtil
	 */
	ObjectInputStream inputStream = null;
	public static void main(String[] args) {

		try {

			// Check if the pair of keys are present else generate those.
			if (!areKeysPresent()) {
				// Method generates a pair of keys using the RSA algorithm and
				// stores it
				// in their respective files
				generateKey();
			}

			final String originalText = "Pardeep Kumar huhaha ";
			
			
			// Encrypt the string using the public key
//			inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
//			final PublicKey publicKey = (PublicKey) inputStream.readObject();
//			final byte[] cipherText = encrypt(originalText, publicKey);
			EncryptionUtil eu = getInstanceEncDec();
			byte[] encText = eu.encText(originalText);
			
			// Decrypt the cipher text using the private key.
//			inputStream = new ObjectInputStream(new FileInputStream(
//					PRIVATE_KEY_FILE));
//			final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
//			final String plainText = decrypt(cipherText, privateKey);
			
			String plainText = eu.decText(encText);
			// Printing the Original, Encrypted and Decrypted Text
			System.out.println("Original: " + originalText);
			System.out.println("Encrypted: " + encText.toString());
			System.out.println("Decrypted: " + plainText);
			
			System.out.println("converting array to string");;
			System.out.println(eu.decodeArrayToString(encText));
			String newEnc = eu.decodeArrayToString(encText);
			System.out.println("String to arrya");
			byte[] newencTest = eu.decodeStringToArrya(newEnc);
			System.out.println(newencTest.toString());
			String plainText2 = eu.decText(newencTest);
			System.out.println("Decrypted: " + plainText2);
			
			
//			String aex = eu.getPublicKey();
//			ArrayList<String>  aa = new ArrayList<String> ();
//			String newaa = "";
//			for(String ss : aex.split("\\n")){
//				aa.add(ss);
//			}
//			StringBuilder result = new StringBuilder();
//			for(int i=0;i<aa.size();i++){
//				result = result.append(aa.get(i).trim());
//			}
//			System.out.println("final ");
//			//System.out.println(result.toString());
//			
//			eu.decodeStringPubKey(result.toString());
//			byte[] encText2 = eu.encText(originalText, eu.decodeStringPubKey(result.toString()));
//			
//			String plainText2 = eu.decText(encText);
//			// Printing the Original, Encrypted and Decrypted Text
//			System.out.println("Original: " + originalText);
//			System.out.println("Encrypted: " + encText2.toString());
//			System.out.println("Decrypted: " + plainText2);
//			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void genKey(){
		if (!areKeysPresent()) {
			// Method generates a pair of keys using the RSA algorithm and
			// stores it
			// in their respective files
			generateKey();
		}
	}
	public final byte[] cipherText = null;
	public byte[] encText(String originalText){
		genKey();
		try {
			inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
			final PublicKey publicKey = (PublicKey) inputStream.readObject();
			//System.out.println("public key used1: "+publicKey.toString());
			//final PublicKey publicKey = getPublicKey();
			//System.out.println(publicKey.toString());
			final byte[] cipherText = encrypt(originalText, publicKey);
			return cipherText;
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		

	}
	public byte[] encText(String originalText, PublicKey publicKey2){
		genKey();
		//inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
		final PublicKey publicKey = publicKey2;
		//System.out.println("public key used : "+ publicKey.toString());
		//final PublicKey publicKey = getPublicKey();
		//System.out.println(publicKey.toString());
		final byte[] cipherText = encrypt(originalText, publicKey);
		return cipherText;
		
		

	}
	
	public String decText(byte[] cipherText){
		
		try {
			inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
			final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
			final String plainText = decrypt(cipherText, privateKey);
			return plainText;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public static EncryptionUtil  getInstanceEncDec(){
		EncryptionUtil eu = new EncryptionUtil();
		
		return eu;
		
	}
	public String getPublicKey(){
		genKey();
		try {
			inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
			final PublicKey publicKey = (PublicKey) inputStream.readObject();
		    // Send the public key bytes to the other party...
		    byte[] publicKeyBytes = publicKey.getEncoded();

		    //Convert Public key to String
		    BASE64Encoder encoder = new BASE64Encoder();
		    String pubKeyStr = encoder.encode(publicKeyBytes);
		    
		    //System.out.println(pubKeyStr);
		    String aex = pubKeyStr;
		    ArrayList<String>  aa = new ArrayList<String> ();
			String newaa = "";
			for(String ss : aex.split("\\n")){
				aa.add(ss);
			}
			StringBuilder result = new StringBuilder();
			for(int i=0;i<aa.size();i++){
				result = result.append(aa.get(i).trim());
			}
			//System.out.println("final ");
			//System.out.println(result.toString());
		    return result.toString();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	public PublicKey decodeStringPubKey(String pubKeyStr){
		
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] sigBytes2 = decoder.decodeBuffer(pubKeyStr);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes2);
			KeyFactory keyFact = KeyFactory.getInstance(ALGORITHM);
			PublicKey pubKey2 = keyFact.generatePublic(x509KeySpec);

			return pubKey2;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	public String decodeArrayToString(byte[] arr){
		BASE64Encoder encoder = new BASE64Encoder();
	    String arrToStr = encoder.encode(arr);
	    String aex = arrToStr;
	    ArrayList<String>  aa = new ArrayList<String> ();
		String newaa = "";
		for(String ss : aex.split("\\n")){
			aa.add(ss);
		}
		StringBuilder result = new StringBuilder();
		for(int i=0;i<aa.size();i++){
			result = result.append(aa.get(i).trim());
		}
//		System.out.println("Converted bytes : ");
//		System.out.println(result.toString());
		
		
		return result.toString();
		
	}
	
	public byte[] decodeStringToArrya(String str){
		
		byte[] sigBytes2;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			sigBytes2 = decoder.decodeBuffer(str);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes2);
			return sigBytes2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
		
		
	}
	
	
	
	
	
	
	
	
}