# Project - lib-mail

## About

Library to send an email through command line or as a library using java.

### Java lib

Import JAR using :

```
<dependency>
    <groupId>com.github.frtu.libs</groupId>
    <artifactId>lib-mail</artifactId>
    <version>${frtu-libs.version}</version>
</dependency>
```

To send an email use :

```java
final MailComposer mailComposer = mailService.createMimeMessage();
mailComposer.to(to)
        .subject(subject)
        .text(body)
        .attachment(attachments)
        .send();
```

Note : method ```to()``` can be called multiple times each time per recipient.

### Command line

Have a file configured with all SMTP mail server named ```application.properties```

Run the command passing :

* Target emails (separated with comma ```,```)
* Subject
* Body
* Attachments.. : append any attachment using file: prefixes

```
java -cp "/Users/fred/git/platform/lib-toolbox-frtu/lib-mail/src/test/resources:target/lib-mail-1.1.0-SNAPSHOT.jar" com.github.frtu.mail.MailMain "rndfred@163.com" "Test subject" "Testing body" "file:./src/test/resources/mail-servers.pdf"
```

## Release notes

### 1.1.0 - Send email & attachement using command line

Sending SMTP mail thru command line and java API :

* API : Provide a fluent API to send email (see docs)
* Cmd line : send email with HTML content
* Cmd line : send email with Attachments
