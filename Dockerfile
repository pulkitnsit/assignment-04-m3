FROM dgroup/java8-gradle
USER root
ADD . .
RUN apt-get update
RUN apt-get -y install --upgrade python-pip python-setuptools python-dev build-essential
RUN pip install --upgrade --user awscli
RUN gradle build
EXPOSE 8080
CMD bash run.sh

FROM dgroup/java8-gradle 
ADD . . 
RUN apt-get update 
RUN apt-get -y install python-pip  
RUN pip install awscli 
RUN gradle build  
RUN aws s3 cp s3://cnu-2k17/nsinghal/application.properties application.properties 
CMD java -jar build/libs/gs-accessing-data-mysql-0.1.0.jar --spring.config.location=application.properties