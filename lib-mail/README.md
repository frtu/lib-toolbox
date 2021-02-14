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

Run the command passing :

* Target email
* Subject
* Body

```
java -jar target/lib-mail-1.1.0-SNAPSHOT.jar "xxx@yyy.com" "Test subject" "Testing body"
```

## Release notes

### 0.0.1-SNAPSHOT - Current version

* Feature list