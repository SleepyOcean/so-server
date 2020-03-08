docker start mysql
mvn clean install -DskipTests
mv ./so-blog-service/target/blog-server.jar ./
docker stop blogserver
docker rm blogserver
docker rmi blogserver
docker build -t blogserver .
docker run -d -p 9999:9999 --name blogserver -v /home/sleepy/res/ImageServer:/ImageServer blogserver