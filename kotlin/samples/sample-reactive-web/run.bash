echo "-> Run bash individually - Type 'sample-reactive-webbuild' to build the project"
sample-reactive-webbuild() {
  echo "mvn clean package"
  mvn clean package
}
echo "-> Run bash individually - Type 'sample-reactive-k8s' to run the executable"
sample-reactive-k8s() {
  echo "Running executable > mvn k8s:resource k8s:build"
  mvn k8s:resource k8s:build
}
echo "-> Run bash individually - Type '$sample-reactive-registryk8s' to run the executable"
sample-reactive-k8sregistry() {
  echo "Running executable > mvn k8s:resource k8s:build k8s:push -Pregistry-k8s"
  mvn k8s:resource k8s:build k8s:push -Pregistry-k8s
}
echo "-> Run bash individually - Type 'sample-reactive-webrun' to run the executable"
sample-reactive-webrun() {
  echo "Running executable > mvn spring-boot:run"
  mvn spring-boot:run
}
