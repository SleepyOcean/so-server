FROM java:8
COPY file-server.jar /ocean/file-server.jar
EXPOSE 9020
CMD ["java", "-jar", "/ocean/file-server.jar", "--rootDir=/ocean/server_data/FileStorage/", "--imgDir=/ocean/server_data/GalleryStorage/"]