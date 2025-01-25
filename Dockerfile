# Baixar uma imagem base com Java
FROM openjdk:17-jdk-slim

# Criar um diretório no container para o app
WORKDIR /app

# Copiar o arquivo JAR gerado pelo seu programa para o container
COPY Main.jar /app/Main.jar

# Comando para executar o programa
CMD ["java", "-jar", "Main.jar"]
