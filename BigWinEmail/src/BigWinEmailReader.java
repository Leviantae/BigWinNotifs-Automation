import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class BigWinEmailReader {

    private static final String loginEmailUsername = "ok@gmail.com";
    private static final String loginEmailPassword = "ok123";
    public static void main(String[] args) {

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(() -> {
            readEmail(loginEmailUsername, loginEmailPassword);
        }, 0, 1, TimeUnit.MINUTES);

        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readEmail(String username, String password) {
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
            store.connect(host, username, password);

            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            if (messages.length > 0) {
                Message lastMessage = messages[messages.length - 1];

                System.out.println("Dummy text 01");
            } else {
                System.out.println("Dummy text 02");
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
