# Use uma imagem oficial do JDK 17 como imagem base para construir o projeto
FROM gradle:7.5.1-jdk17 AS build

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o conteúdo do diretório de trabalho local para o contêiner
COPY . .

# Execute o build do projeto usando Gradle
RUN gradle build --no-daemon

# Use uma imagem oficial do JDK 17 como imagem base para o contêiner final
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR construído do estágio anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Exponha a porta definida no .env
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
