# Usa uma imagem base do Maven com JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto para dentro do contêiner
COPY . .

# Compila o projeto (gera o JAR dentro do contêiner)
RUN mvn clean package -DskipTests

# Segunda etapa: Criando a imagem final
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR gerado na fase anterior
COPY --from=build /app/target/slaivideos-backend-1.0.0.jar app.jar

# Expõe a porta do serviço
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
