FROM openjdk:17-jdk-slim

WORKDIR /src

COPY gerenciador-tarefas.jar /src/gerenciador-tarefas.jar

CMD ["java", "-jar", "gerenciador-tarefas.jar"]
