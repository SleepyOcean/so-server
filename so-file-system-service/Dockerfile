FROM java:8
COPY file-server.jar /ocean/file-server.jar
EXPOSE 9020
CMD ["java", "-jar", "/ocean/file-server.jar", "--storage=/ocean/ServerData/FileSystemServStorage/"]