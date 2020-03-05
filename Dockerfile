FROM java:8
COPY blog-server.jar /sleepy/so-blog-server.jar
EXPOSE 9999
CMD ["java", "-jar", "/sleepy/so-blog-server.jar"] 