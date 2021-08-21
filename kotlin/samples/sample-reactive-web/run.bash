echo "-> Run bash individually - Type 'sample-reactive-webbuild' to build the project"
sample-reactive-webbuild() {
  echo "mvn clean package"
  mvn clean package
}
echo "-> Run bash individually - Type 'sample-reactive-webrun' to run the executable"
sample-reactive-webrun() {
  echo "Running executable > mvn spring-boot:run"
  mvn spring-boot:run
}

sample-reactive-webbuild
sample-reactive-webrun