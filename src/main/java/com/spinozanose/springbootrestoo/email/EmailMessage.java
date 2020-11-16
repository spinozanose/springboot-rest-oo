package com.spinozanose.springbootrestoo.email;

/**
 * Notice that in some ways this looks like a DTO. The data, however, is scoped inside the
 * package, and there is every reason to believe that some behaviors would be coded here if
 * this were part of a real email sending system.
 *
 * Right now this can be thought of as an extension to the EmailSendingService interface. It
 * is a concrete class here, but it could just as easily be an interface and have a factory
 * to set it up (or share a factory with the EmailSendingService).
 *
 */
public class EmailMessage {
    final String sender;
    final String[] recipients;
    final String subject;
    final String message;

    public EmailMessage(final String sender, final String[] recipients, final String subject, final String message) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.message = message;
    }
}
