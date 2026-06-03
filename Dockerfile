FROM mcr.microsoft.com/playwright/java:v1.54.0

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

CMD ["mvn","test"]