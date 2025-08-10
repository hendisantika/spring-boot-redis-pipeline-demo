# Spring Boot Redis Pipeline Demo

A comprehensive demonstration of Redis pipeline operations in Spring Boot, showcasing performance improvements when
handling bulk operations with 10,000 data entries.

## What is Redis?

**Redis** (Remote Dictionary Server) is an open-source, in-memory data structure store that can be used as a database,
cache, message broker, and streaming engine. It supports various data structures such as strings, hashes, lists, sets,
sorted sets, bitmaps, hyperloglogs, and geospatial indexes.

### Key Features of Redis:

- **In-Memory Storage**: All data is stored in RAM, providing extremely fast read and write operations
- **Persistence**: Optional disk persistence to survive system restarts
- **Data Structures**: Rich set of data types beyond simple key-value pairs
- **High Performance**: Can handle millions of requests per second
- **Atomic Operations**: All Redis operations are atomic, ensuring data consistency
- **Replication & Clustering**: Built-in support for high availability and scalability

### Common Use Cases:

- **Caching**: Store frequently accessed data to reduce database load
- **Session Storage**: Manage user sessions in web applications
- **Real-time Analytics**: Count page views, user interactions, etc.
- **Message Queues**: Implement pub/sub messaging patterns
- **Rate Limiting**: Control API request rates
- **Leaderboards**: Gaming and social application rankings

## What is Redis Pipeline?

**Redis Pipelining** is a technique to send multiple commands to the Redis server without waiting for the replies of
previous commands. Instead of the traditional request-response model where each command waits for a response, pipelining
allows you to send a batch of commands and then read all responses at once.

### How Redis Pipeline Works:

1. **Batch Commands**: Multiple Redis commands are queued up locally
2. **Single Network Trip**: All commands are sent to Redis server in one network round trip
3. **Batch Processing**: Redis server processes all commands sequentially
4. **Bulk Response**: All responses are returned together in the same order

### Performance Benefits:

- **Reduced Network Latency**: Fewer network round trips between client and server
- **Higher Throughput**: Process more operations per second
- **Lower Resource Usage**: Reduced TCP overhead and context switching
- **Improved Efficiency**: Particularly beneficial for bulk operations

### When to Use Redis Pipeline:

- **Bulk Insert Operations**: Adding thousands of records at once
- **Batch Updates**: Updating multiple keys simultaneously
- **Mass Deletions**: Removing large sets of data
- **Analytics Queries**: Reading multiple metrics in one go
- **Data Migration**: Moving large datasets efficiently

### Performance Comparison:

- **Without Pipeline**: Each command = 1 network round trip
    - 1000 commands = 1000 round trips
    - Total time ≈ 1000 × (network latency + processing time)
- **With Pipeline**: All commands = 1 network round trip
    - 1000 commands = 1 round trip
    - Total time ≈ 1 × network latency + (1000 × processing time)
    - **Result**: 5-10x performance improvement for bulk operations

## Features

- **Redis Pipeline Operations**: Efficient bulk insert, read, and delete operations using Redis pipelines
- **Performance Comparison**: Side-by-side comparison between pipeline and normal Redis operations
- **Docker Compose Integration**: Easy Redis setup using Docker Compose
- **RESTful API**: Complete REST API for testing pipeline operations
- **Comprehensive Monitoring**: Database info and health check endpoints

## Technology Stack

- **Spring Boot 3.5.4**
- **Spring Data Redis**
- **Redis 7 (Alpine)**
- **Docker Compose**
- **Lombok**
- **Java 21**

## Quick Start

### Prerequisites

- Java 21 or higher
- Docker and Docker Compose
- Maven 3.6+

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd spring-boot-redis-pipeline-demo
   ```

2. **Start Redis using Docker Compose**
   ```bash
   docker-compose up -d
   ```

3. **Access Redis UI (Redis Commander)**
    - URL: http://localhost:8081
    - Username: `admin`
    - Password: `admin`
    - This provides a web-based interface to browse and manage Redis data

4. **Build and run the application**
   ```bash
   ./mvnw clean compile spring-boot:run
   ```

5. **Verify the application is running**
   ```bash
   curl http://localhost:8080/api/redis/health
   ```

## API Endpoints

### Health Check

- **GET** `/api/redis/health` - Application health check

### Redis Operations

#### Pipeline Operations (High Performance)

- **POST** `/api/redis/pipeline/insert` - Insert 10K records using pipeline
- **GET** `/api/redis/pipeline/read` - Read 10K records using pipeline
- **DELETE** `/api/redis/pipeline/delete` - Delete 10K records using pipeline

#### Normal Operations (Standard)

- **POST** `/api/redis/normal/insert` - Insert 10K records using normal operations

#### Performance & Monitoring

- **POST** `/api/redis/performance/compare` - Compare pipeline vs normal performance
- **GET** `/api/redis/info` - Get Redis database information
- **GET** `/api/redis/user/{userId}` - Get specific user data

## Sample HTTP Requests & cURL Commands

### 1. Health Check

```bash
# cURL
curl -X GET http://localhost:8080/api/redis/health

# HTTP
GET http://localhost:8080/api/redis/health
```

**Expected Response:**

```json
{
  "status": "UP",
  "service": "Redis Pipeline Demo",
  "timestamp": "1691683440000"
}
```

### 2. Insert 10K Records using Pipeline

```bash
# cURL
curl -X POST http://localhost:8080/api/redis/pipeline/insert \
  -H "Content-Type: application/json"

# HTTP
POST http://localhost:8080/api/redis/pipeline/insert
Content-Type: application/json
```

**Expected Response:**

```json
{
  "operation": "INSERT_10K_PIPELINE",
  "records": 10000,
  "duration_ms": 245,
  "status": "SUCCESS"
}
```

### 3. Insert 10K Records using Normal Operations

```bash
# cURL
curl -X POST http://localhost:8080/api/redis/normal/insert \
  -H "Content-Type: application/json"

# HTTP  
POST http://localhost:8080/api/redis/normal/insert
Content-Type: application/json
```

**Expected Response:**

```json
{
  "operation": "INSERT_10K_NORMAL",
  "records": 10000,
  "duration_ms": 1850,
  "status": "SUCCESS"
}
```

### 4. Performance Comparison

```bash
# cURL
curl -X POST http://localhost:8080/api/redis/performance/compare \
  -H "Content-Type: application/json"

# HTTP
POST http://localhost:8080/api/redis/performance/compare
Content-Type: application/json
```

**Expected Response:**

```json
{
  "pipeline_duration_ms": 245,
  "normal_duration_ms": 1850,
  "performance_improvement": "7.55x faster",
  "time_saved_ms": 1605,
  "status": "SUCCESS"
}
```

### 5. Read 10K Records using Pipeline

```bash
# cURL
curl -X GET http://localhost:8080/api/redis/pipeline/read

# HTTP
GET http://localhost:8080/api/redis/pipeline/read
```

**Expected Response:**

```json
{
  "operation": "READ_10K_PIPELINE",
  "records": 10000,
  "duration_ms": 180,
  "status": "SUCCESS",
  "results_count": 10000
}
```

### 6. Get Redis Database Information

```bash
# cURL
curl -X GET http://localhost:8080/api/redis/info

# HTTP
GET http://localhost:8080/api/redis/info
```

**Expected Response:**

```json
{
  "database_size": 10000,
  "pipeline_user_exists": true,
  "normal_user_exists": true,
  "status": "SUCCESS"
}
```

### 7. Get Specific User Data

```bash
# cURL
curl -X GET http://localhost:8080/api/redis/user/1

# HTTP
GET http://localhost:8080/api/redis/user/1
```

**Expected Response:**

```json
{
  "key": "user:1",
  "data": {
    "id": "1",
    "name": "User 1",
    "email": "user1@example.com",
    "age": "21"
  },
  "exists": true,
  "status": "SUCCESS"
}
```

### 8. Delete 10K Records using Pipeline

```bash
# cURL
curl -X DELETE http://localhost:8080/api/redis/pipeline/delete

# HTTP
DELETE http://localhost:8080/api/redis/pipeline/delete
```

**Expected Response:**

```json
{
  "operation": "DELETE_10K_PIPELINE",
  "records": 10000,
  "duration_ms": 95,
  "status": "SUCCESS"
}
```

## Performance Benefits

Redis pipelining provides significant performance improvements:

- **7-8x faster** than individual operations
- **Reduced network overhead** - Multiple commands sent in single request
- **Better throughput** - Higher operations per second
- **Lower latency** - Fewer round trips to Redis server

### Typical Performance Results

- **Pipeline Insert**: ~200-300ms for 10K records
- **Normal Insert**: ~1500-2000ms for 10K records
- **Performance Gain**: 6-8x improvement

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── id/my/hendisantika/redispipelinedemo/
│   │       ├── SpringBootRedisPipelineDemoApplication.java
│   │       ├── config/
│   │       │   └── RedisConfig.java
│   │       ├── controller/
│   │       │   └── RedisPipelineController.java
│   │       └── service/
│   │           └── RedisPipelineService.java
│   └── resources/
│       └── application.properties
├── compose.yaml
└── README.md
```

## Configuration

### Redis Configuration (application.properties)

```properties
spring.application.name=spring-boot-redis-pipeline-demo
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.timeout=60000ms
spring.data.redis.database=0
server.port=8080
```

### Docker Compose (compose.yaml)

```yaml
services:
  redis:
    image: redis:7-alpine
    container_name: redis-pipeline-demo
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    command: redis-server --appendonly yes
    healthcheck:
      test: [ "CMD", "redis-cli", "ping" ]
      interval: 30s
      timeout: 10s
      retries: 3

volumes:
  redis-data:
```

## Testing Workflow

1. **Start the application and Redis**
2. **Check health**: `GET /api/redis/health`
3. **Run performance comparison**: `POST /api/redis/performance/compare`
4. **Insert data with pipeline**: `POST /api/redis/pipeline/insert`
5. **Read data with pipeline**: `GET /api/redis/pipeline/read`
6. **Check database info**: `GET /api/redis/info`
7. **Get sample user**: `GET /api/redis/user/1`
8. **Clean up**: `DELETE /api/redis/pipeline/delete`

## Error Handling

All endpoints return structured error responses:

```json
{
  "status": "ERROR",
  "error": "Connection refused to Redis server"
}
```

## Logging

The application provides detailed logging for all operations:

- Pipeline operation timings
- Error details
- Request/response logging

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.