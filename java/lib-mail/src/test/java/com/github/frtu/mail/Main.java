package com.github.frtu.mail;

public class Main {
    public static void main(String[] args) {
        MailMain.main(new String[]{"rndfred@163.com", "Test subject with attachment", "Testing body",
                "file:./src/test/resources/mail-servers.pdf"});
    }
}
