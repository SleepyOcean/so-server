docker start mysql
docker start es
mvn clean install -DskipTests

# 构建启动 blog-server
mv ./so-blog-service/target/blog-server.jar ./so-blog-service/
docker stop blogserver
docker rm blogserver
docker rmi blogserver
docker build -f ./so-blog-service/Dockerfile -t blogserver .
docker run -d -p 9999:9999 --privileged=true --name blogserver -v /ocean/server_data/blogserver/ImageServer:/ImageServer -e "TZ=Asia/Shanghai" blogserver

# 构建启动 file-server
mv ./so-file-system-service/target/file-server.jar ./so-file-system-service/
docker stop fileserver
docker rm fileserver
docker rmi fileserver
docker build -f ./so-file-system-service/Dockerfile -t fileserver .
docker run -d -p 9000:9000 --privileged=true --name fileserver -v /ocean/server_data/fileserver:/ocean/server_data -e "TZ=Asia/Shanghai" fileserver