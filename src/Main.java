import net.tushar.util.otp.OtpProvider;
import net.tushar.util.otp.OtpSourceException;
import net.tushar.util.otp.OtpType;
import net.tushar.util.otp.TotpClock;

public class Main {
	public static void main(String[] args) throws OtpSourceException {
		String secret = "abci1defhkalsd";
		TotpClock mTotpclock = new TotpClock();
		OtpProvider mOtpProvider = new OtpProvider(mTotpclock);
		System.out.println(mOtpProvider.getSecondsTillNextCounterValue());
		System.out.println(mOtpProvider.getNextCode(secret, OtpType.TOTP, 1));
	}
}
