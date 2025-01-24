# Usa uma imagem do OpenJDK como base
FROM openjdk:17-jdk-slim AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto para o contêiner
COPY . .

# Compila o projeto (gera o JAR dentro do contêiner)
RUN ./mvnw clean package -DskipTests

# Segunda etapa: Criando a imagem final
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR gerado na fase anterior
COPY --from=build /app/target/slaivideos-backend-1.0.0.jar app.jar

# Expõe a porta do serviço
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
