import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.mail.event.MessageCountListener;

public class Main {
    private static final String EMAIL_USERNAME = "testertestamente@gmail.com";
    private static final String EMAIL_PASSWORD = ".";

    public static void main(String[] args) {
        String host = "imap.gmail.com";
        String protocol = "imaps";
        int port = 993;

        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", protocol);
        properties.setProperty("mail.imaps.host", host);
        properties.setProperty("mail.imaps.port", String.valueOf(port));

        try {
            Session session = Session.getDefaultInstance(properties);

            Store store = session.getStore(protocol);
            store.connect(host, EMAIL_USERNAME, EMAIL_PASSWORD);

            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_WRITE);

            MessageCountListener messageCountListener = new MessageCountAdapter() {
                public void messagesAdded(MessageCountEvent ev) {
                    Message[] messages = ev.getMessages();
                    for (Message message : messages) {
                        processBigWin(message);
                    }
                }
            };
            inbox.addMessageCountListener(messageCountListener);

            IDLEThread idleThread = new IDLEThread(inbox);
            idleThread.start();

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void processBigWin(Message message) {
        try {
            System.out.println("New Email Subject: " + message.getSubject());

            Address[] fromAddresses = message.getFrom();
            if (fromAddresses != null && fromAddresses.length > 0) {
                System.out.println("From: " + fromAddresses[0]);
            } else {
                System.out.println("From: Unknown");
            }

            Object content = message.getContent();
            if (content instanceof String) {
                System.out.println("Content: " + content);
            } else if (content instanceof MimeMultipart) {
                processMultipart((MimeMultipart) content);
            } else {
                System.out.println("Unsupported content type: " + content.getClass().getName());
                System.out.println("Content: " + content.toString());
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private static class IDLEThread extends Thread {
        private final Folder folder;

        public IDLEThread(Folder folder) {
            this.folder = folder;
        }

        public void run() {
            try {
                while (true) {
                    // Start the IDLE mode
                    ((IMAPFolder) folder).idle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void processMultipart(MimeMultipart multipart) throws MessagingException, IOException {

            BodyPart bodyPart = multipart.getBodyPart(0);
            Object partContent = bodyPart.getContent();

            if (partContent instanceof String) {
                System.out.println(" (Text): " + partContent);
            } else if (partContent instanceof MimeMultipart) {
                processMultipart((MimeMultipart) partContent);
            } else {
                System.out.println(" (Unsupported): " + partContent.getClass().getName());
                System.out.println("Content: " + partContent.toString());
            }
        }

}