FROM debian:bullseye

MAINTAINER steven@nevets.tech

RUN apt update && apt upgrade -y
RUN apt install wget git tesseract-ocr -y

RUN wget https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.4.1%2B1/OpenJDK17U-jdk_x64_linux_hotspot_17.0.4.1_1.tar.gz
RUN tar -xzvf OpenJDK17U-jdk_x64_linux_hotspot_17.0.4.1_1.tar.gz
RUN mv jdk-17.0.4.1+1/ /java/
RUN rm OpenJDK17U-jdk_x64_linux_hotspot_17.0.4.1_1.tar.gz

RUN git clone https://github.com/tesseract-ocr/tessdata.git /tessdata

RUN mkdir /bot
#RUN wget https://ci.nevets.tech/view/Bots/job/SupportBot/lastSuccessfulBuild/artifact/target/SupportBot-Latest.jar
#RUN mv SupportBot-Latest.jar /bot/SupportBot.jar
COPY ./target/SupportBot-Latest.jar /bot/SupportBot.jar

WORKDIR /bot

ENV PATH=$PATH:/java/bin/
ENV JAVA_HOME=/java/
ENV TESSDATA_PATH=/tessdata/
ENV TESSDATA_PREFIX=/tessdata/

CMD ["java", "-Xmx2G", "-Xms2G", "-jar", "SupportBot.jar"]