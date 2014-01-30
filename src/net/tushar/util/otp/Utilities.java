package net.tushar.util.otp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.tushar.util.otp.Base32String.DecodingException;
import net.tushar.util.otp.PasscodeGenerator.Signer;

/**
 * A class for handling a variety of utility things.  This was mostly made
 * because I needed to centralize dialog related constants. I foresee this class
 * being used for other code sharing across Activities in the future, however.
 * @author Tushar Roy (http://www.tushroy.com)
 */
public class Utilities {
	
  public static final long SECOND_IN_MILLIS = 1000;
  public static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;

  // Constructor -- Does nothing yet
  private Utilities() { }

  public static final long millisToSeconds(long timeMillis) {
    return timeMillis / 1000;
  }

  public static final long secondsToMillis(long timeSeconds) {
    return timeSeconds * 1000;
  }
  //added extra
  static Signer getSigningOracle(String secret) {
	    try {
	      byte[] keyBytes = decodeKey(secret);
	      final Mac mac = Mac.getInstance("HMACSHA1");
	      mac.init(new SecretKeySpec(keyBytes, ""));

	      // Create a signer object out of the standard Java MAC implementation.
	      return new Signer() {
	        @Override
	        public byte[] sign(byte[] data) {
	          return mac.doFinal(data);
	        }
	      };
	    } catch (DecodingException error) {
	      //Log.e(LOCAL_TAG, error.getMessage());
	    } catch (NoSuchAlgorithmException error) {
	      //Log.e(LOCAL_TAG, error.getMessage());
	    } catch (InvalidKeyException error) {
	      //Log.e(LOCAL_TAG, error.getMessage());
	    }

	    return null;
	  }

	  private static byte[] decodeKey(String secret) throws DecodingException {
	    return Base32String.decode(secret);
	  }
}
