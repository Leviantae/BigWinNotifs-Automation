import com.sun.mail.imap.IMAPFolder;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.event.MessageCountListener;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

public class Main {
    private static final String EMAIL_USERNAME = ".";
    private static final String EMAIL_PASSWORD = ".";


    public static void main(String[] args) {


        BigWinOperatorsList operatorsList = loadOperators();
        BigWinProvidersList providersList = loadProviders();

        createJiraTicket();


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

    private static BigWinOperatorsList loadOperators() {
        try {
            ObjectMapper operatorMapper = new ObjectMapper();
            BigWinOperatorsList operatorConfig = operatorMapper.readValue(new File("opConfig.json"), BigWinOperatorsList.class);
            return operatorConfig;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BigWinProvidersList loadProviders() {
        try {
            ObjectMapper providerMapper = new ObjectMapper();
            BigWinProvidersList providerConfig = providerMapper.readValue(new File("providerConfig.json"), BigWinProvidersList.class);
            return providerConfig;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }





    public static void createJiraTicket()
    {
            try (CloseableHttpClient httpClient = HttpClients.custom().build()) {
                String jiraUrl = "https://leviantae.atlassian.net/rest/api/latest/issue/";

                String projectKey = "LEV";
                String issueType = "Task";
                String summary = "Test for now";

                String username = ".";
                String password = ".";

                String jsonPayload = String.format(
                        "{\"fields\":{\"project\":{\"key\":\"%s\"},\"issuetype\":{\"name\":\"%s\"},\"summary\":\"%s\"}}",
                        projectKey, issueType, summary);

                HttpPost request = new HttpPost(jiraUrl);
                request.setEntity(new StringEntity(jsonPayload, "UTF-8"));
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Authorization", "Basic " + getBase64Credentials(username, password));

                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    if (response.getStatusLine().getStatusCode() == 201) {
                        String responseBody = EntityUtils.toString(response.getEntity());
                        String issueKey = parseIssueKey(responseBody);
                        System.out.println("Jira issue created successfully. Issue Key: " + issueKey);
                    } else {
                        System.err.println("Failed to create Jira issue. HTTP Status Code: " + response.getStatusLine().getStatusCode());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static String getBase64Credentials(String username, String password) {
            String credentials = username + ":" + password;
            return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
        }

        private static String parseIssueKey(String responseBody) {
            return responseBody.split("\"key\":\"")[1].split("\"")[0];
        }

}
