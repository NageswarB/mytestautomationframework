package com.webmailhostopia.common.utils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This utility class provides a functionality to send an HTML e-mail message
 * with embedded images.
 *
 */
public class EmailUtil {

	/**
	 * Sends an HTML e-mail with inline images.
	 * @param host SMTP host
	 * @param port SMTP port
	 * @param userName e-mail address of the sender's account 
	 * @param password password of the sender's account
	 * @param toAddress e-mail address of the recipient
	 * @param subject e-mail subject
	 * @param htmlBody e-mail content with HTML tags
	 * @param mapInlineImages 
	 * 			key: Content-ID
	 * 			value: path of the image file
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void send(String host, String port,
			final String userName, final String password, String toAddress, String ccAddress,
			String fromAddress, String subject, String htmlBody, 
			Map<String, String> mapInlineImages)
				throws Exception {
		
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "false");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);
		

		// creates a new e-mail message
		Message msg = new MimeMessage(session);
		//msg.setFrom(new InternetAddress(userName));
        msg.setFrom(new InternetAddress(fromAddress));
    
       String[] recipients = toAddress.split(";");
       InternetAddress[] addressTo = new InternetAddress[recipients.length];
       for (int i = 0; i < recipients.length; i++) {
           addressTo[i] = new InternetAddress(recipients[i]);
       }
       msg.addRecipients(Message.RecipientType.TO, addressTo); 
       
       if (!StringUtils.isEmpty(ccAddress)) {
	       String[] recipientsCc = ccAddress.split(";");
	       InternetAddress[] addressCc = new InternetAddress[recipientsCc.length];
	       for (int i = 0; i < recipientsCc.length; i++) {
	    	   addressCc[i] = new InternetAddress(recipientsCc[i]);
	       }
	       msg.addRecipients(Message.RecipientType.CC, addressCc); 
       }
       
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		
		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(htmlBody, "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// adds inline image attachments
		if (mapInlineImages != null && mapInlineImages.size() > 0) {
			Set<String> setImageID = mapInlineImages.keySet();
			
			for (String contentId : setImageID) {
				MimeBodyPart imagePart = new MimeBodyPart();
				imagePart.setHeader("Content-ID", "<" + contentId + ">");
				imagePart.setDisposition(Part.INLINE);
				
				String imageFilePath = mapInlineImages.get(contentId);
				try {
					imagePart.attachFile(imageFilePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(imagePart);
			}
		}

		msg.setContent(multipart);
		Transport.send(msg);
	}
}