package dataobject;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class Mail {

    private static final String mailSAE = "procapybarasae@gmail.com";

    private static Session session;

    public Mail() throws Exception {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailSAE, "ltkdmeyaoigyhfay");
            }
        });
    }




    /**
     * Envoie un mail notifiant la fin d'un vote.
     * @param email L'adresse email à laquelle le courrier éléctronique doit être envoyé.
     * @param vote Le vote qui vient de se terminer.
     */
    public void envoyerMailFinVote(String email, Vote vote) throws Exception {

        String messageBody =
                "Bonjour,\n" +
                        "Un vote auquel vous avez participé vient de se terminer.\n" +
                        "L'intitulé du référundum était : « " + vote.getIntitule() + " »\n" +
                        "Vous aviez le choix entre les options : « " + vote.getOption1() + " » et «" + vote.getOption2() + " »\n";

        if (vote.getResultat() < 0.5)

            messageBody += "L'option « " + vote.getOption1() + " » gagne avec "
                    + ((double) (int) ((1 - vote.getResultat()) * 10000)) / 100 + "% des voix";

        else if (vote.getResultat() > 0.5)

            messageBody += "L'option « " + vote.getOption2() + " » gagne avec "
                    + ((double) (int) (vote.getResultat() * 10000)) / 100 + "% des voix";

        else

            messageBody += "Les options « " + vote.getOption1() +
                    " » et « " + vote.getOption2() + " » sont à égalité";

        envoyerMail2(email, "E-Scrutin : Fin d'un vote auquel vous avez participé.", messageBody);
    }

    /**
     * Envoie un mail notifiant la participation à un vote.
     * @param email L'adresse email à laquelle le courrier éléctronique doit être envoyé.
     * @param vote Le vote pour lequel le bulletin a été dépose.
     */
    public void envoyerMailDepotBulletin(String email, Vote vote) throws Exception {
        envoyerMail2(
                email,
                "E-Scrutin : Participation à un vote.",
                "Bonjour,\n" +
                        "Nous confirmons le dépôt de votre bulletin pour le vote suivant : \n" +
                        vote.getIntitule() + "\n" +
                        "Si la participation à ce référundum ne vient pas de vous, merci de vous rendre auprès de l'administrateur système."
        );
    }


    /**
     * Envoie un mail notifiant la création d'un compte dans le système.
     * @param email L'adresse email à laquelle le courrier éléctronique doit être envoyé.
     * @param utilisateur L'{@link Utilisateur} qui vient d'être créé (mot de passe en clair).
     */
    public void envoyerMailCreationCompte(String email, Utilisateur utilisateur,String motdepasse) throws Exception {
        envoyerMail2(
                email,
                "E-scrutin : Création d'un compte.",
                "Bonjour,\n" +
                        "Un compte vous a été attribué au sein de l'entreprise, voici vos identifiants de connexion : \n" +
                        "Nom d'utilisateur : " + utilisateur.getLogin() + "\n" +
                        "Mot de passe : " + motdepasse + "\n\n" +
                        "Merci de changer de mot de passe le plus tôt possible sur un des postes de l'entreprise."
        );
    }

    /**
     * Envoie un mail quelconque.
     * @param destemail L'adresse email à laquelle le courrier éléctronique doit être envoyé.
     * @param messageSubject Le champs <strong>sujet</strong> du courrier éléctronique envoyé.
     * @param messageBody Le corps du mail envoyé.
     */
    private void envoyerMail2(String destemail, String messageSubject, String messageBody)  throws Exception{

        MimeMessage message= new MimeMessage(session);
        message.setFrom(new InternetAddress(mailSAE));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(destemail));
        message.setSubject(messageSubject);
        message.setText(messageBody);

        // Mime -> Email


        try {
            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}