# Usa imagem com JDK 17
FROM eclipse-temurin:17-jdk

# Define o diretório de trabalho no container
WORKDIR /app

# Copia todos os arquivos do projeto
COPY . .

# Dá permissão de execução ao Maven wrapper
RUN chmod +x mvnw

# Faz build da aplicação ignorando os testes
RUN ./mvnw clean package -DskipTests

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "target/simplifica-contabil-0.0.1-SNAPSHOT.jar"]
