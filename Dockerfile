# Etapa 1: front
FROM node:18-alpine AS frontbuilder

WORKDIR /frontend
COPY an-front/package*.json /frontend/
COPY an-front/proxy.conf.json /frontend/
COPY an-front/angular.json /frontend/

RUN npm install
COPY an-front/ /frontend/
RUN npm run build

# Verificar que la carpeta de salida existe
RUN ls -la /frontend/dist/an-front/browser

# Etapa 2: Construcción
FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app
COPY webchat_agc/pom.xml /app/
COPY webchat_agc/src /app/src/

# Copiar los archivos estáticos de Angular
COPY --from=frontbuilder /frontend/dist/an-front/browser/ /app/src/main/resources/static

RUN mvn clean package -DskipTests

# Etapa 3: Ejecución
FROM openjdk:17-jdk

COPY --from=builder /app/target/webchat_agc-0.0.1-SNAPSHOT.jar backend-spring.jar

# Copiar el truststore al contenedor
# COPY webchat_agc/src/main/resources/rds-truststore.jks /app/rds-truststore.jks

# Configurar las opciones JVM para usar el truststore
#ENV JAVA_TOOL_OPTIONS="-Djavax.net.ssl.trustStore=/app/rds-truststore.jks -Djavax.net.ssl.trustStorePassword=amazonCApass"

ENTRYPOINT [ "java", "-jar", "backend-spring.jar" ]
