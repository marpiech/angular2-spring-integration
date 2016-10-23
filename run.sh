mkdir pid
java -jar hello-service/build/libs/hello-service-0.1.jar &
echo $! > hello-service.pid 
