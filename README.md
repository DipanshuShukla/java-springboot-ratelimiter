# Rate Limiter (Spring Boot)

A lightweight request-rate limiter built with Spring Boot. It uses a Spring Security filter to intercept incoming requests, tracks request frequency per IP, and stores counters in Redis. The limiter blocks clients that exceed a configured threshold within a one-minute window.

## Features

* Intercepts requests via a Spring Security filter
* Tracks request counts per IP
* Uses Redis for fast in-memory storage
* Simple per-minute rate limiting
* Maven-based build
* Easy to configure through application properties

## How It Works

1. Each request passes through a custom security filter.
2. The filter identifies the client IP and increments its request counter in Redis.
3. If the IP exceeds the `request-limit-per-minute` value, the request is rejected.
4. Counters expire automatically after one minute.

## Configuration

`application.yml` example:

```yaml
server:
  port: 12345

spring:
  data:
    redis:
      host: localhost
      port: 6379

request-limit-per-minute: 5
```

## Requirements

* Java 17+ (or your project’s target)
* Maven
* Redis server

## Running the Service

1. Start Redis:

   ```bash
   redis-server
   ```
2. Build the project:

   ```bash
   mvn clean package
   ```
3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

## Project Structure

* **Filter layer:** Handles request interception and rate checks
* **Redis service:** Manages counters and TTL
* **Configuration:** Exposes the per-minute limit as a property

## Customization

You can adjust or extend:

* Different rate windows (e.g., per-second, per-hour)
* Keying strategies (API key, user ID, etc.)
* Enforcement behavior (headers, custom responses, logging)

## License

MIT or your chosen license.
