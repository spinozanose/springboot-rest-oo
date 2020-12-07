package com.spinozanose.springbootrestoo.email;

/**
 * This is an example of an anti-corruption layer to a service. it is where the details
 * of sending an email message are removed from the domain implementation.
 *
 * Notice that the EmailSendingService is a module, and has no dependency on the domain.
 * It is also an Aggregate that manages the immutable EmailMessage object.
 *
 * The design of this service allows a service stub
 * (https://www.martinfowler.com/eaaCatalog/serviceStub.html).
 *
 * In addition here is the best place to implement translations between the EmailMessage
 * and EmailSendingService interfaces and a vendor's specific implementation. We also
 * could manage more complexity, like retries, multiple emails, caching, etc.
 *
 * In some way this class is implemented so simply because there are no dependencies to
 * be managed. We don't even need a constructor. If there were any more complexity,
 * however, it would be a good idea to consider a more typical interface-with-named-factory
 * pattern.
 */
public class EmailSendingService {

    // scoped package-private so it may be stubbed for testing.
    AVendorsEmailSendingService service = new AVendorsEmailSendingService();

    public void sendEmail(EmailMessage message) {
        service.sendEmail(message);
    }
}
