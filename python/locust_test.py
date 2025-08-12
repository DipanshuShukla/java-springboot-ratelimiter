from locust import HttpUser, task, between
import random

class RateLimiterUser(HttpUser):
    # Simulates "think time" between requests
    wait_time = between(0.5, 1.5)

    @task
    def hit_ping_endpoint(self):
        # Simulate unique IPs via header
        fake_ip = f"192.168.0.{random.randint(1, 50)}"
        self.client.get(
            "/ping",
            headers={"X-Forwarded-For": fake_ip}
        )
