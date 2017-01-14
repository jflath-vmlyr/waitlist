package jim.util;


import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

public class SMSMessageCreator {
    private final TwilioRestClient client;

    public SMSMessageCreator(TwilioRestClient client) {
        this.client = client;
    }

    public Message create(String to, String from, String body) {
    	System.out.println("In the create, sending this message [" + body + "]");
        MessageCreator messageCreator = new MessageCreator( new PhoneNumber(to), new PhoneNumber(from), body);
        System.out.println("leaving the create");
        return messageCreator.create(this.client);
    }
}