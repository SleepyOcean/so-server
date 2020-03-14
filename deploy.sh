mvn clean install -DskipTests
docker start mysql
#docker start es

# 构建启动 blog-server
mv ./so-blog-service/target/blog-server.jar ./
docker stop blogserver
docker rm blogserver
docker rmi blogserver
docker build -f ./so-blog-service/Dockerfile -t blogserver .
docker run -d -p 9010:9010 --privileged=true --name blogserver -v /ocean/server_data/blogserver/ImageServer:/ImageServer -e "TZ=Asia/Shanghai" blogserver

# 构建启动 file-server
mv ./so-file-system-service/target/file-server.jar ./
docker stop fileserver
docker rm fileserver
docker rmi fileserver
docker build -f ./so-file-system-service/Dockerfile -t fileserver .
docker run -d -p 9020:9020 --privileged=true --name fileserver -v /ocean/server_data/fileserver:/ocean/server_data -e "TZ=Asia/Shanghai" fileserver
