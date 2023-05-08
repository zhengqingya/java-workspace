FROM openjdk:8-jre-alpine

ENV SLEEP 0
ENV PROFILE dev

CMD echo "===========================开始了。。。=============================="

ADD *.jar /app.jar

RUN sh -c 'touch /app.war'
VOLUME /tmp
EXPOSE 88
CMD echo "The application will start in ${SLEEP}s..." && \
    sleep ${SLEEP} && \
    java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar -Ddefault.client.encoding="UTF-8" -Dfile.encoding="UTF-8" -Duser.language="Zh" -Duser.region="CN" app.jar --spring.profiles.active=${PROFILE}
