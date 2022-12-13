package dataobject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;


public class Mail {

    private static final String mailSAE = "ProcapybaraSAE@gmail.com";
    private final  Gmail service;

    public Mail() throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT,jsonFactory))
                .setApplicationName("SAEvote")
                .build();

    }
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, GsonFactory jsonFactory)
            throws IOException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(Mail.class.getResourceAsStream("/code_secret_client_762233691442-ka8uci3j2nusuqf2del3pejap8i64ea4.apps.googleusercontent.com.json")));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, jsonFactory, clientSecrets, Set.of(GmailScopes.GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void envoyerMail(String subject,String toEmailAddress,Vote vote) throws GeneralSecurityException, IOException, MessagingException {
        String messageSubject = null;
        String messageBody= null;
        if (subject.equals("Vote")) {
            messageSubject = "Confirmation de vote";
            messageBody = "Votre vote pour La question : \""+vote.getIntitule()+"\" a bien été pris en compte";
        }
        if (subject.equals("FinVote")) {
            messageSubject = "Fin d'un vote";
            messageBody = "Le vote pour \""+vote.getIntitule()+"\" vient de se terminer";
        }

        // Encoder comme MIME message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(mailSAE));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(toEmailAddress));
        email.setSubject(messageSubject);
        email.setText(messageBody);

        // Mime -> Email
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        try {
            //envoi message
            message = service.users().messages().send("me", message).execute();

        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                throw new RuntimeException("Erreur : "+e.getDetails());
            } else {
                throw e;
            }
        }
    }

}