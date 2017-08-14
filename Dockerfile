FROM clojure
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY ./target/pancakes-standalone.jar /usr/src/app
EXPOSE 8080
CMD ["java", "-jar", "pancakes-standalone.jar"]