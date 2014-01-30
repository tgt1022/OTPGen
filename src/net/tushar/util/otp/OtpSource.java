
package net.tushar.util.otp;
/***
 * 
 * @author Tushar (http://www.tushroy.com)
 *
 */

public interface OtpSource {

  String getNextCode(String secret,OtpType type,Integer currentCounter) throws OtpSourceException;

  String respondToChallenge(String secret, String challenge,OtpType type,Integer currentCounter) throws OtpSourceException;
  
  TotpCounter getTotpCounter();
  
  TotpClock getTotpClock();
}
