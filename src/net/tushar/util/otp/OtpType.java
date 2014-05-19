package net.tushar.util.otp;
/***
 * 
 * @author Tushar (http://www.tushroy.com)
 *
 */

public enum OtpType { // must be the same as in res/values/strings.xml:type
	TOTP(0), // time based
	HOTP(1); // counter based

	public final Integer value; 

	OtpType(Integer value) {
		this.value = value;
	}

	public static OtpType getEnum(Integer i) {
		for (OtpType type : OtpType.values()) {
			if (type.value.equals(i)) {
				return type;
			}
		}

		return null;
	}

}
