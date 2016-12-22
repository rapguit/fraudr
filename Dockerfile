FROM tomcat:8-jre8
RUN apt-get update && apt-get upgrade -y

RUN rm -rf webapps/ROOT
COPY target/classes/collisions.txt /tmp/collisions.txt
COPY target/fraudr-*.war webapps/ROOT.war

#ENV config
ENV TZ=America/Sao_Paulo
ENV JAVA_OPTS="-Dfile.collisions.path=/tmp/collisions.txt"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

CMD ["catalina.sh", "run"]