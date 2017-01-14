package jim.util;

import com.twilio.exception.TwilioException;
import com.twilio.http.TwilioRestClient;

public class SMSClient {
    private SMSCredentials credentials;
    private SMSMessageCreator messageCreator;

    public SMSClient() {
        this.credentials = new SMSCredentials();
        System.out.println("Got the credentials");
        TwilioRestClient client = new TwilioRestClient.Builder( credentials.getAccountSid(), credentials.getAuthToken() ).build();
        System.out.println("Got the client");
        this.messageCreator = new SMSMessageCreator(client);
        System.out.println("Done with the constructor");
    }

    public SMSClient(SMSCredentials credentials, SMSMessageCreator messageCreator) {
        this.credentials = credentials;
        this.messageCreator = messageCreator;
    }

    public void sendMessage(String to, String message) {
        try {
        	System.out.println("In sendMessage");
            this.messageCreator.create(to, this.credentials.getPhoneNumber(), message);
            System.out.println("Leaving sendMessage");
        } catch (TwilioException exception) {
            exception.printStackTrace();
        }
    }
}