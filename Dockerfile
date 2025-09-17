# Use Tomcat 11 with JDK 21 (closest available official image)
FROM tomcat:11.0.0-jdk21

# Remove default ROOT webapp
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR to Tomcat ROOT so app serves at /
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
