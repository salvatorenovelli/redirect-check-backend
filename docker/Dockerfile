FROM openjdk:8u121-jre-alpine
VOLUME /tmp
ADD *.jar /home/app.jar
RUN apk add --update bash openssl fontconfig ttf-dejavu
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
EXPOSE 8080 5005
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /home/app.jar" ]