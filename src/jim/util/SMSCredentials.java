package jim.util;


public class SMSCredentials {
// Production
	
	private boolean test = true;
	
	public SMSCredentials(){
//		test=false;
	}
	
    public String getAccountSid() {
        return test ? "AC0265d97a991773ff8e562b7b18593db9" : "AC5324997ab13479ec75f3190681c28d72";
    }

    public String getAuthToken() {
        return test ? "77f50a10a56ef4d34e9ea1f2529ce1cf" : "b85cf43700d2a5fa3241b3ba1c18e271";
    }

    public String getPhoneNumber() {
        return test ? "15005550006" : "18166563912";
    }

}

