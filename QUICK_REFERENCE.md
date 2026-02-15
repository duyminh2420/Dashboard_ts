# Quick Reference Guide

## 📋 Common Commands

### Frontend

```bash
# Navigate to frontend
cd react-dashboard

# Install dependencies
npm install

# Start development server (port 3000)
npm start

# Build for production
npm build

# Run linting
npm run lint

# Run tests
npm test

# Preview production build
npm run preview
```

### Backend

```bash
# Navigate to backend
cd scala-backend

# Install dependencies (automatic with SBT)
sbt update

# Run in development (port 8080)
sbt run

# Run tests
sbt test

# Compile
sbt compile

# Build assembly JAR
sbt assembly

# Clean build artifacts
sbt clean

# Interactive shell
sbt
```

### Docker

```bash
# From root directory

# Build and start all services
docker-compose up --build

# Start services (no rebuild)
docker-compose up

# Stop services
docker-compose down

# View logs
docker-compose logs -f backend
docker-compose logs -f postgres
docker-compose logs -f frontend

# Access backend container
docker exec -it dashboard-backend bash

# Access database
docker exec -it dashboard-postgres psql -U postgres -d dashboard
```

## 🌐 Endpoints Reference

### Health & Status
- `GET http://localhost:8080/api/health` - Server status

### User Endpoints
```
GET    http://localhost:8080/api/users
GET    http://localhost:8080/api/users/1
GET    http://localhost:8080/api/users?search=john
GET    http://localhost:8080/api/users/stats/overview
POST   http://localhost:8080/api/users
PUT    http://localhost:8080/api/users/1
DELETE http://localhost:8080/api/users/1
```

### Product Endpoints
```
GET    http://localhost:8080/api/products
GET    http://localhost:8080/api/products/1
GET    http://localhost:8080/api/products?inStock=true
GET    http://localhost:8080/api/products/stats/overview
POST   http://localhost:8080/api/products
PUT    http://localhost:8080/api/products/1
DELETE http://localhost:8080/api/products/1
```

### Activity Endpoints
```
GET    http://localhost:8080/api/activities
GET    http://localhost:8080/api/activities/1
GET    http://localhost:8080/api/activities?recent=10
GET    http://localhost:8080/api/activities/user/1
POST   http://localhost:8080/api/activities
DELETE http://localhost:8080/api/activities/1
```

## 📂 File Structure Quick Reference

### Frontend
```
react-dashboard/
├── src/
│   ├── components/           # Reusable components
│   │   ├── navbar/
│   │   ├── menu/
│   │   ├── footer/
│   │   ├── chartBox/
│   │   ├── dataTable/
│   │   └── ...
│   ├── pages/                # Page components
│   │   ├── home/
│   │   ├── users/
│   │   ├── products/
│   │   ├── login/
│   │   └── ...
│   ├── styles/               # Global styles
│   │   ├── global.scss
│   │   └── variables.scss
│   ├── utils/                # Utilities
│   ├── App.tsx               # Main app & router
│   ├── main.tsx              # Entry point
│   └── data.ts               # Mock data
├── public/                   # Static assets
├── package.json
├── tsconfig.json
├── vite.config.ts
└── index.html
```

### Backend
```
scala-backend/
├── src/main/scala/com/dashboard/
│   ├── models/               # Data models
│   │   └── Models.scala
│   ├── routes/               # API routes
│   │   ├── UserRoutes.scala
│   │   ├── ProductRoutes.scala
│   │   └── ActivityRoutes.scala
│   ├── services/             # Business logic
│   │   ├── UserService.scala
│   │   ├── ProductService.scala
│   │   └── ActivityService.scala
│   ├── repositories/         # Data access
│   │   ├── UserRepository.scala
│   │   ├── ProductRepository.scala
│   │   └── ActivityRepository.scala
│   ├── utils/                # Utilities
│   │   └── Utils.scala
│   └── Main.scala            # Entry point
├── src/test/scala/           # Tests
├── project/
│   ├── build.properties
│   └── plugins.sbt
├── conf/                     # Configuration
├── build.sbt                 # Dependencies
├── Dockerfile                # Container config
├── docker-compose.yml        # Multi-container
├── init.sql                  # Database schema
└── README.md
```

## 🔄 Common Workflows

### Start Development Environment

```bash
# Terminal 1: Backend
cd scala-backend
sbt run

# Terminal 2: Frontend
cd react-dashboard
npm start

# Now visit http://localhost:3000
```

### Modify a User API Endpoint

1. **Frontend**: Update call in `react-dashboard/src/utils/api.ts`
2. **Backend Routes**: Modify `scala-backend/src/main/scala/com/dashboard/routes/UserRoutes.scala`
3. **Backend Service**: Update logic in `scala-backend/src/main/scala/com/dashboard/services/UserService.scala`
4. **Backend Repository**: Change data access in `scala-backend/src/main/scala/com/dashboard/repositories/UserRepository.scala`

### Add New Data Model

1. Add model to `scala-backend/src/main/scala/com/dashboard/models/Models.scala`
2. Create repository trait and implementation
3. Create service with business logic
4. Create route handlers
5. Export from `Main.scala`
6. Update frontend types if needed

### Connect Frontend to API

1. Create API functions in `react-dashboard/src/utils/api.ts`
2. Create custom hook (e.g., `useUsers.ts`)
3. Update component to use hook instead of mock data
4. Test endpoint in browser DevTools

### Deploy with Docker

```bash
# From root directory
docker-compose up --build

# Access services:
# Frontend: http://localhost:3000
# Backend: http://localhost:8080/api
# Database: localhost:5432
```

## 🆘 Troubleshooting

### Frontend won't start
```bash
cd react-dashboard
rm -rf node_modules package-lock.json
npm install
npm start
```

### Backend compilation error
```bash
cd scala-backend
sbt clean
sbt compile
```

### Port already in use
```bash
# Check what's using port 8080
netstat -ano | findstr :8080

# Or for Mac/Linux
lsof -i :8080
```

### Database connection error
```bash
# Check if PostgreSQL is running
docker-compose up postgres

# Verify credentials in docker-compose.yml
# Default: postgres/password
```

### CORS errors
- Ensure backend CORS is enabled (it is by default)
- Check frontend API URL matches backend origin
- Verify backend is running on 8080

## 📊 Testing API Quickly

### Using curl

```bash
# Get all users
curl http://localhost:8080/api/users

# Create user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"id":0,"firstName":"John","lastName":"Doe","email":"john@example.com","phone":"123","img":"","createdAt":"","verified":false}'

# Update user
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"id":1,"firstName":"Jane","lastName":"Doe","email":"jane@example.com","phone":"456","img":"","createdAt":"","verified":true}'

# Delete user
curl -X DELETE http://localhost:8080/api/users/1
```

### Using VS Code REST Client Extension

Create `.rest` file:

```
### Get all users
GET http://localhost:8080/api/users

### Get user by ID
GET http://localhost:8080/api/users/1

### Create user
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "id": 0,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "123",
  "img": "",
  "createdAt": "2024-01-25",
  "verified": false
}
```

## 🔑 Environment Variables

### Frontend (.env)
```
REACT_APP_API_URL=http://localhost:8080/api
```

### Backend (.env / docker-compose.yml)
```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=dashboard
DB_USER=postgres
DB_PASSWORD=password
SERVER_HOST=0.0.0.0
SERVER_PORT=8080
```

## 📱 Frontend Routes

```
/                    → Home (Dashboard)
/users               → Users List
/users/:userId       → User Detail
/products            → Products List
/products/:id        → Product Detail
/login               → Login Page
/register            → Register Page
```

## 💻 IDE/Editor Setup

### VS Code Extensions Recommended

- ESLint
- Prettier - Code formatter
- REST Client
- Scala (Metals)
- Docker
- PostgreSQL
- Thunder Client (API testing)

### IntelliJ IDEA

- Scala plugin
- Database Navigator
- REST Client
- Docker support

## 🎓 Learning Resources

### Getting Started
1. Read `README.md` (root)
2. Read `INTEGRATION_GUIDE.md` for API integration
3. Check `scala-backend/README.md` for backend details

### Deep Dive
1. Review component code in `react-dashboard/src/components`
2. Study service layer in `scala-backend/src/main/scala/com/dashboard/services`
3. Understand routing in `App.tsx` (frontend) and `*Routes.scala` (backend)

## ⚡ Performance Checklist

- [ ] Enable React.StrictMode in development
- [ ] Use React DevTools Profiler
- [ ] Check network tab for large requests
- [ ] Implement pagination for lists
- [ ] Cache API responses
- [ ] Use debouncing for search
- [ ] Optimize database queries
- [ ] Add indexes for frequently queried columns
- [ ] Enable gzip compression
- [ ] Use CDN for static assets

## 🔐 Security Checklist

- [ ] Input validation on both frontend and backend
- [ ] HTTPS in production
- [ ] CORS properly configured
- [ ] SQL injection prevention
- [ ] XSS prevention
- [ ] CSRF protection
- [ ] Rate limiting
- [ ] Authentication/Authorization
- [ ] Secrets management
- [ ] Dependency vulnerability checks

---

**Last Updated**: January 2026  
**Maintainer**: Development Team
