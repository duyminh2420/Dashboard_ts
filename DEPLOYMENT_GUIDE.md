# Deployment Guide

## Local Development

### Prerequisites
- JDK 11+
- Scala 2.13.12
- SBT 1.9.8
- PostgreSQL 12+ (optional, for production)
- Docker & Docker Compose (optional)

### Running Locally

1. **With in-memory data (development)**:
```bash
cd scala-backend
sbt run
```

2. **With PostgreSQL (production-like)**:
```bash
docker-compose up --build
```

## Docker Deployment

### Build Docker Image

```bash
cd scala-backend
docker build -t dashboard-backend:latest .
```

### Run Container

```bash
docker run -p 8080:8080 \
  -e DB_HOST=localhost \
  -e DB_PORT=5432 \
  -e DB_NAME=dashboard \
  dashboard-backend:latest
```

## Production Deployment

### AWS EC2

1. Create EC2 instance (Ubuntu 22.04)
2. Install Docker and Docker Compose
3. Clone repository
4. Update `.env` with production values
5. Run: `docker-compose -f docker-compose.prod.yml up -d`

### Heroku

```bash
heroku create dashboard-backend
git push heroku main
```

### DigitalOcean App Platform

1. Connect GitHub repository
2. Set environment variables
3. Deploy using Dockerfile

## Monitoring

### Health Check

```bash
curl http://localhost:8080/api/health
```

### Logs

```bash
# Docker logs
docker logs dashboard-backend

# Scala backend logs (development)
# Check console output
```

### Metrics

- Response times
- Error rates
- Database query performance
- Memory usage

## Scaling Considerations

1. **Load Balancing**: Use nginx or AWS Load Balancer
2. **Caching**: Implement Redis for frequently accessed data
3. **Database Replication**: Set up PostgreSQL replication
4. **Horizontal Scaling**: Deploy multiple backend instances
5. **CDN**: Use CloudFront or similar for static assets

## Backup & Recovery

### Database Backup

```bash
# Manual backup
docker exec dashboard-postgres pg_dump -U postgres dashboard > backup.sql

# Restore
docker exec -i dashboard-postgres psql -U postgres dashboard < backup.sql
```

### Automated Backups

Use AWS RDS, DigitalOcean Backups, or pg_dump with cron jobs.

## SSL/TLS Setup

Use Let's Encrypt with Nginx reverse proxy:

```nginx
server {
    listen 443 ssl;
    server_name api.yourdomain.com;

    ssl_certificate /etc/letsencrypt/live/api.yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.yourdomain.com/privkey.pem;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## Performance Optimization

1. **Connection Pooling**: Configure HikariCP
2. **Query Optimization**: Add database indexes
3. **Caching Headers**: Set appropriate Cache-Control headers
4. **Gzip Compression**: Enable for responses
5. **CDN**: Serve static files from CDN

## Troubleshooting

### High Memory Usage

- Check for memory leaks
- Tune JVM heap size: `-Xmx512m -Xms256m`
- Monitor with `jps` and `jconsole`

### Database Connection Issues

- Check connection pool settings
- Verify database is running
- Monitor active connections

### Slow Response Times

- Profile with JFR (Java Flight Recorder)
- Analyze database query performance
- Check for N+1 queries
- Implement caching

## Continuous Integration

### GitHub Actions Example

```yaml
name: Build and Deploy

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: olafurpg/setup-scala@v13
      - run: sbt test
      - run: sbt clean compile
      - run: docker build -t dashboard-backend:${{ github.sha }} .
      - run: docker push dashboard-backend:${{ github.sha }}
```

## Maintenance

- Regular security updates
- Database maintenance (VACUUM, ANALYZE)
- Log rotation
- Certificate renewal (SSL/TLS)
- Dependency updates

## Support

For deployment issues, check:
- Backend logs
- Database logs
- Application metrics
- Network connectivity
