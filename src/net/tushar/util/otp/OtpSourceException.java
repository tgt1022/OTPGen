package net.tushar.util.otp;
/***
 * 
 * @author Tushar (http://www.tushroy.com)
 *
 */

@SuppressWarnings("serial")
public class OtpSourceException extends Exception {
  public OtpSourceException(String message) {
    super(message);
  }

  public OtpSourceException(String message, Throwable cause) {
    super(message, cause);
  }
}
