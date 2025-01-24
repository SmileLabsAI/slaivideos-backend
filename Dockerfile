# Usa uma imagem base do OpenJDK 17
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o JAR para dentro do contêiner
COPY target/slaivideos-backend-1.0.0.jar app.jar

# Expõe a porta 8080 (a mesma do application.properties)
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]

server.port=8080

# Usa variável de ambiente no Render
mercadopago.APP_USR-2558136151487812-012311-08dd4e15416784773f715f16a694e92f-220300097=${MERCADOPAGO_APP_USR-2558136151487812-012311-08dd4e15416784773f715f16a694e92f-220300097}

logging.level.org.springframework=INFO
