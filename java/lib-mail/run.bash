echo "-> Run bash individually - Type 'library-mailbuild' to build the project"
library-mailbuild() {
  echo "mvn clean package"
  mvn clean package
}
echo "-> Run bash individually - Type 'library-mailrun' to run the executable"
library-mailrun() {
  echo "Running executable > java -jar target/lib-mail-1.1.0-SNAPSHOT.jar"
  java -jar target/lib-mail-1.1.0-SNAPSHOT.jar
}

library-mailbuild
library-mailrun