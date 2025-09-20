# Prerequisites
- Docker and Docker Compose
- JDK 17+ (for local development)
- Maven 3.5+ (for local development)

## Running with Docker (Recommended)
1) Clone the repository:
```bash
git clone https://github.com/nikhil3972/event-notification-system.git
cd event-notification-system
```
2) Start the application:
```bash
docker-compose up --build
```
3) Verify the application is running:
```bash
curl http://localhost:8080/actuator/health
```
4) The API will be available at: 
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "EMAIL",
    "payload": {
      "recipient": "test@example.com", 
      "message": "Hello!"
    },
    "callbackUrl": "http://httpbin.org/post"
  }'
```

# Running Locally
1) Build the application: 
```bash
mvn clean package
```
2) Run the application: 
```bash
java -jar target/eventnotification-0.0.1-SNAPSHOT.jar
```
or use Maven directly:                                          
```bash
mvn spring-boot:run
```

# Postman Collection
Import the collection from [event-notification-system](src/main/resources/postman/event-notification-system.postman_collection.json)