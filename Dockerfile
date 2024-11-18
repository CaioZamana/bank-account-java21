FROM amazoncorretto:21-alpine
WORKDIR /app

# Copiar o JAR gerado para o container
COPY build/libs/*.jar app.jar

# Expor a porta padrão do Spring Boot
EXPOSE 8080

# Definir o comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
