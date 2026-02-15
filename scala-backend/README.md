# Dashboard Backend - Scala/Akka HTTP

A scalable backend for the React Dashboard built with Scala and Akka HTTP framework.

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    HTTP Routes Layer                     │
│  (UserRoutes, ProductRoutes, ActivityRoutes)            │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                  Service Layer                           │
│  (UserService, ProductService, ActivityService)        │
│  - Business logic validation                            │
│  - Data transformation                                  │
│  - Statistics calculation                               │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│              Repository Layer                            │
│  (UserRepository, ProductRepository, ActivityRepository) │
│  - Data access abstraction                              │
│  - Can switch between in-memory and database            │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│              Data Models                                │
│  (User, Product, Activity, ChartBox, etc.)             │
└─────────────────────────────────────────────────────────┘
```

## Project Structure

```
scala-backend/
├── src/
│   ├── main/scala/com/dashboard/
│   │   ├── models/           # Domain models
│   │   ├── routes/           # API route handlers
│   │   ├── services/         # Business logic layer
│   │   ├── repositories/     # Data access layer
│   │   ├── utils/            # Utility functions
│   │   └── Main.scala        # Application entry point
│   └── test/scala/           # Unit tests
├── conf/                      # Configuration files
├── project/                   # SBT build configuration
├── build.sbt                  # SBT dependencies
└── README.md
```

## API Endpoints

### Users
- `GET /api/users` - Get all users (with optional search)
- `GET /api/users/:id` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/:id` - Update user
- `DELETE /api/users/:id` - Delete user
- `GET /api/users/stats/overview` - Get user statistics

### Products
- `GET /api/products` - Get all products (with optional search)
- `GET /api/products/:id` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/:id` - Update product
- `DELETE /api/products/:id` - Delete product
- `GET /api/products/stats/overview` - Get product statistics

### Activities
- `GET /api/activities` - Get recent activities
- `GET /api/activities/:id` - Get activity by ID
- `POST /api/activities` - Create new activity
- `DELETE /api/activities/:id` - Delete activity
- `GET /api/activities/user/:userId` - Get activities for specific user

### Health Check
- `GET /api/health` - Server health status

## Getting Started

### Prerequisites
- Scala 2.13.12
- SBT 1.9.8
- JDK 11 or higher

### Installation

1. Navigate to the backend directory:
```bash
cd scala-backend
```

2. Run the application:
```bash
sbt run
```

The server will start on `http://localhost:8080`

### Building for Production

```bash
sbt clean compile
sbt dist
```

## Key Features

### ✅ Clean Architecture
- Separation of concerns with layers: Routes → Services → Repositories
- Easy to test and maintain
- Clear dependency injection

### ✅ Type Safety
- Strongly typed models with Circe JSON serialization
- Scala's type system prevents common bugs
- Compile-time safety for JSON handling

### ✅ CORS Support
- Built-in CORS handling for frontend integration
- Configurable origins and methods

### ✅ Error Handling
- Consistent API response format
- Proper HTTP status codes
- Detailed error messages

### ✅ Scalability
- Non-blocking I/O with Akka HTTP
- Async/Future-based operations
- Ready for database integration

### ✅ Repository Pattern
- In-memory repository for development
- Abstract repository trait for easy DB switching
- Prepared PostgreSQL repository skeleton

## Configuration

### Environment Variables
Create a `.env` file:
```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=dashboard
DB_USER=postgres
DB_PASSWORD=password
SERVER_HOST=0.0.0.0
SERVER_PORT=8080
```

### Application Config
Edit `conf/application.conf` for Akka HTTP settings

## Testing

Run tests with:
```bash
sbt test
```

## Future Enhancements

1. **Database Integration**
   - Implement PostgresUserRepository
   - Add database schema migrations
   - Use Slick ORM for type-safe queries

2. **Authentication**
   - JWT token support
   - User login/register endpoints
   - Role-based access control (RBAC)

3. **Caching**
   - Redis integration
   - Query result caching
   - Distributed caching

4. **Advanced Features**
   - File uploads
   - Advanced search/filtering
   - Real-time updates with WebSockets
   - Analytics and reporting

5. **DevOps**
   - Docker containerization
   - Kubernetes deployment
   - CI/CD pipeline setup

## Connecting Frontend to Backend

Update your React dashboard to fetch from the backend:

```typescript
// src/utils/fetch.tsx
const API_BASE_URL = "http://localhost:8080/api";

export const fetchUsers = async () => {
  const response = await fetch(`${API_BASE_URL}/users`);
  return response.json();
};
```

## Performance Considerations

- **Non-blocking I/O**: All operations use Future/async patterns
- **Streaming**: Large responses use streaming where applicable
- **Caching**: Implement caching for frequently accessed data
- **Database Indexing**: Create indexes on frequently queried fields

## Security Best Practices

- ✅ Input validation in services
- ✅ SQL injection prevention (prepared statements ready)
- ✅ CORS origin validation
- ✅ HTTP status codes follow REST standards
- 🔒 TODO: Add authentication middleware
- 🔒 TODO: Implement rate limiting
- 🔒 TODO: Add request logging and monitoring

## Troubleshooting

### Port already in use
```bash
# Find process using port 8080
lsof -i :8080
# Kill the process
kill -9 <PID>
```

### SBT build issues
```bash
sbt clean
sbt update
sbt compile
```

## Contributing

1. Follow Scala naming conventions
2. Write unit tests for new features
3. Keep services focused on business logic
4. Use meaningful commit messages

## License

MIT

## Support

For issues or questions, please open an issue on GitHub.
