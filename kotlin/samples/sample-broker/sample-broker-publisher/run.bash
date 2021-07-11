echo "-> Run bash individually - Type 'sample-brokerbuild' to build the project"
sample-brokerbuild() {
  echo "mvn clean package"
  mvn clean package
}
echo "-> Run bash individually - Type 'sample-brokerrun' to run the executable"
sample-brokerrun() {
  echo "Running executable > java -jar target/sample-broker-0.0.1-SNAPSHOT.jar"
  java -jar target/sample-broker-0.0.1-SNAPSHOT.jar
}

sample-brokerbuild
sample-brokerrun