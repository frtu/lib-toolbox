echo "-> Run bash individually - Type 'sample-r2dbcbuild' to build the project"
sample-r2dbcbuild() {
  echo "mvn clean package"
  mvn clean package
}
echo "-> Run bash individually - Type 'sample-r2dbcrun' to run the executable"
sample-r2dbcrun() {
  echo "Running executable > java -jar target/persistence-r2dbc-1.1.1-SNAPSHOT.jar"
  java -jar target/sample-r2dbc-1.1.1-SNAPSHOT.jar
}

sample-r2dbcbuild
sample-r2dbcrun