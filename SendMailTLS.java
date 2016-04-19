package project1;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.Multipart;


public class SendMailTLS {
	
	public static void main(String[] args) throws IOException {

		final String username = "lawareh@gmail.com";
		final String password = "BB36sec1kol3004";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("lawareh@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse("karunesh@bu.edu"));
			message.setSubject("Testing Subject");
			message.setText("Dear QA,"
			+ "\n\n This email is to inform you that the Build that you were"
			+" running on TeamCity has been built successfully."
			+"The attachment along with this email has all the details of the build"
			+"\n\n Thank you, \n ARN Team ");
			MimeBodyPart messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart();
	        messageBodyPart = new MimeBodyPart();

	        String attachmentPath = "C:/";
	        String attachmentName = "file.txt";

	        File att = new File(new File(attachmentPath), attachmentName);
	        messageBodyPart.attachFile(att);

	        DataSource source = new FileDataSource(att);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(attachmentName);
	        multipart.addBodyPart(messageBodyPart);
	        message.setContent(multipart);
			System.out.println("Done");
			Transport.send(message);
			System.out.println("Sent message successfully....");
		}   
		catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}

