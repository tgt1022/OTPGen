package net.tushar.util.otp;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import net.tushar.util.otp.PasscodeGenerator.Signer;

/**
 * Class containing implementation of HOTP/TOTP.
 * Generates OTP codes for one or more accounts.
 * @author Tushar Roy (http://www.tushroy.com)
 * @author Steve Weis (sweis@google.com)
 * @author Cem Paya (cemp@google.com)
 */
public class OtpProvider implements OtpSource {

  private static final int PIN_LENGTH = 6; // HOTP or TOTP
  private static final int REFLECTIVE_PIN_LENGTH = 9; // ROTP
  
  @Override
  public String getNextCode(String secret,OtpType type,Integer currentCounter) throws OtpSourceException {
    return getCurrentCode(secret, null,type,currentCounter);
  }

  // This variant is used when an additional challenge, such as URL or
  // transaction details, are included in the OTP request.
  // The additional string is appended to standard HOTP/TOTP state before
  // applying the MAC function.
  @Override
  public String respondToChallenge(String secret, String challenge,OtpType type,Integer currentCounter) throws OtpSourceException {
    if (challenge == null) {
      return getCurrentCode(secret, null,type,currentCounter);
    }
    try {
      byte[] challengeBytes = challenge.getBytes("UTF-8");
      return getCurrentCode(secret, challengeBytes,type,currentCounter);
    } catch (UnsupportedEncodingException e) {
      return "";
    }
  }

  @Override
  public TotpCounter getTotpCounter() {
    return mTotpCounter;
  }

  @Override
  public TotpClock getTotpClock() {
    return mTotpClock;
  }

  private String getCurrentCode(String secret, byte[] challenge,OtpType type,Integer currentCounter) throws OtpSourceException {
    // Secret is required.
    if (secret == null) {
      throw new OtpSourceException("No secret defined");
    }

    long otp_state = 0;

    if (type == OtpType.TOTP) {
      // For time-based OTP, the state is derived from clock.
      otp_state =
          mTotpCounter.getValueAtTime(Utilities.millisToSeconds(mTotpClock.currentTimeMillis()));
    } else if (type == OtpType.HOTP){
      // For counter-based OTP, the state is obtained by incrementing stored counter.
      Integer counter = currentCounter+1;
      otp_state = counter.longValue();
    }

    return computePin(secret, otp_state, challenge);
  }

  public OtpProvider(TotpClock totpClock) {
    this(totpClock,DEFAULT_INTERVAL);
  }

  public OtpProvider(TotpClock totpClock,int interval) {
    mTotpCounter = new TotpCounter(interval);
    mTotpClock = totpClock;
  }

  /**
   * Computes the one-time PIN given the secret key.
   *
   * @param secret the secret key
   * @param otp_state current token state (counter or time-interval)
   * @param challenge optional challenge bytes to include when computing passcode.
   * @return the PIN
   */
  private String computePin(String secret, long otp_state, byte[] challenge)
      throws OtpSourceException {
    if (secret == null || secret.length() == 0) {
      throw new OtpSourceException("Null or empty secret");
    }

    try {
      Signer signer = Utilities.getSigningOracle(secret);
      PasscodeGenerator pcg = new PasscodeGenerator(signer,
        (challenge == null) ? PIN_LENGTH : REFLECTIVE_PIN_LENGTH);

      return (challenge == null) ?
             pcg.generateResponseCode(otp_state) :
             pcg.generateResponseCode(otp_state, challenge);
    } catch (GeneralSecurityException e) {
      throw new OtpSourceException("Crypto failure", e);
    }
  }

  /** Default passcode timeout period (in seconds) */
  public static final int DEFAULT_INTERVAL = 30;

  /** Counter for time-based OTPs (TOTP). */
  private final TotpCounter mTotpCounter;

  /** Clock input for time-based OTPs (TOTP). */
  private final TotpClock mTotpClock;
  
}
