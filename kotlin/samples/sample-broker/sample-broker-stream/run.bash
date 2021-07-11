echo "-> Run bash individually - Type 'sample-brokerbuild' to build the project"
sample-brokerbuild() {
  echo "mvn clean package"
  mvn clean package
}
echo "-> Run bash individually - Type 'sample-brokerrun' to run the executable"
sample-brokerrun() {
  echo "Running executable > mvn spring-boot:run"
  mvn spring-boot:run
}

sample-brokerbuild
sample-brokerrun