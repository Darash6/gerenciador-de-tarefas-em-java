FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gerenciador-tarefas.jar /app/gerenciador-tarefas.jar

CMD ["java", "-jar", "gerenciador-tarefas.jar"]
