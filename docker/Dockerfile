FROM oracle/graalvm-ce:1.0.0-rc12
ENV BOT_TOKEN=UNSET

RUN gu install python
RUN gu install ruby

RUN yum install -y wget
RUN wget https://www-us.apache.org/dist/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz
RUN tar -xzvf apache-maven-3.6.0-bin.tar.gz

COPY /pom.xml /pom.xml
COPY /src /src
COPY /scripts /scripts
 
RUN ./apache-maven-3.6.0/bin/mvn clean package -f /pom.xml -T100
RUN mkdir /config/
RUN cp /target/amethyst-1.0.0-jar-with-dependencies.jar /amethyst.jar
CMD java -XX:-UseJVMCIClassLoader -jar amethyst.jar $BOT_TOKEN
