FROM openjdk:22-jdk
COPY SmartJobPortalSystem/target/smart-job-portal.jar /smart-job-portal
EXPOSE 8080
ENTRYPOINT ["java","-jar","/smart-job-portal"]