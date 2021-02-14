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
        .send();
```

### Command line

Have a file configured with all SMTP mail server named ```application.properties```

Run the command passing :

* Target email
* Subject
* Body

```
java -cp "./folder_having_application_properties/:target/lib-mail-1.1.0-SNAPSHOT.jar" com.github.frtu.mail.MailMain "xxx@yyy.com" "Test subject" "Testing body"
```

## Release notes

### 1.1.0-SNAPSHOT - Current version

* Sending SMTP mail thru command line and java API