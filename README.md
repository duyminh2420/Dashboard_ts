# Dashboard - Full Stack Application

A complete full-stack dashboard application with React/TypeScript frontend and Scala/Akka HTTP backend.

## 📁 Project Structure

```
Dashboard_typescript/
├── react-dashboard/              # React Frontend (TypeScript + Vite)
│   ├── src/
│   │   ├── components/          # Reusable UI components
│   │   ├── pages/               # Page components
│   │   ├── styles/              # Global styles
│   │   ├── utils/               # Utility functions
│   │   └── data.ts              # Mock data (to be replaced with API calls)
│   ├── package.json
│   ├── tsconfig.json
│   └── vite.config.ts
│
├── scala-backend/                # Scala Backend (Akka HTTP)
│   ├── src/main/scala/
│   │   └── com/dashboard/
│   │       ├── models/          # Domain models
│   │       ├── routes/          # API route handlers
│   │       ├── services/        # Business logic layer
│   │       ├── repositories/    # Data access layer
│   │       ├── utils/           # Utilities
│   │       └── Main.scala       # Entry point
│   ├── build.sbt
│   ├── Dockerfile
│   └── docker-compose.yml
│
├── INTEGRATION_GUIDE.md          # Frontend-Backend integration guide
├── DEPLOYMENT_GUIDE.md           # Production deployment guide
└── README.md                     # This file
```

## 🚀 Quick Start

### Frontend Only

```bash
cd react-dashboard
npm install
npm start
```

Runs on: `http://localhost:3000` (uses mock data from `data.ts`)

### Backend + Frontend

#### Option 1: Development (In-Memory Data)

**Terminal 1 - Backend:**
```bash
cd scala-backend
sbt run
```

**Terminal 2 - Frontend:**
```bash
cd react-dashboard
npm start
```

#### Option 2: Docker (Complete Stack)

```bash
# From root directory
docker-compose up --build
```

Runs:
- Frontend: `http://localhost:3000`
- Backend: `http://localhost:8080/api`
- PostgreSQL: `localhost:5432`

## 🎯 Features

### Frontend
- ✅ Dashboard with analytics charts
- ✅ User management with data grid
- ✅ Product management
- ✅ Activity tracking
- ✅ Responsive design with SCSS
- ✅ TypeScript for type safety
- ✅ React Router for navigation
- ✅ Material-UI components
- ✅ Recharts visualizations

### Backend
- ✅ RESTful API with Akka HTTP
- ✅ Type-safe models with Circe JSON
- ✅ Service layer for business logic
- ✅ Repository pattern for data access
- ✅ CORS support
- ✅ Error handling with consistent responses
- ✅ In-memory repositories (development)
- ✅ PostgreSQL support (production)
- ✅ Docker & Docker Compose ready
- ✅ Health check endpoint

## 📡 API Endpoints

### Users
```
GET    /api/users                    # List all users
GET    /api/users/:id               # Get user by ID
POST   /api/users                   # Create user
PUT    /api/users/:id               # Update user
DELETE /api/users/:id               # Delete user
GET    /api/users/stats/overview    # User statistics
```

### Products
```
GET    /api/products                    # List all products
GET    /api/products/:id               # Get product by ID
POST   /api/products                   # Create product
PUT    /api/products/:id               # Update product
DELETE /api/products/:id               # Delete product
GET    /api/products/stats/overview    # Product statistics
```

### Activities
```
GET    /api/activities                    # Get recent activities
GET    /api/activities/:id               # Get activity by ID
POST   /api/activities                   # Create activity
DELETE /api/activities/:id               # Delete activity
GET    /api/activities/user/:userId      # Get user activities
```

### Health
```
GET    /api/health                   # Server health status
```

## 🏗️ Architecture

### Frontend Architecture
```
Vite (Build Tool)
├── React 18 (UI Framework)
├── TypeScript (Type Safety)
├── React Router (Navigation)
├── Material-UI (Components)
├── Recharts (Charts)
└── SCSS (Styling)
```

### Backend Architecture
```
Scala 2.13.12
├── Akka HTTP (Web Framework)
├── Akka Streams (Async)
├── Circe (JSON)
├── ScalikeJDBC (Database)
├── PostgreSQL (Data)
└── Docker (Containerization)
```

## 🔌 Integration

See [INTEGRATION_GUIDE.md](./INTEGRATION_GUIDE.md) for detailed instructions on:
- Connecting frontend to backend
- Creating API hooks
- Error handling
- Environment configuration
- Testing the connection

## 📦 Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Frontend | React | 18.2.0 |
| Frontend | TypeScript | 5.1.6 |
| Frontend | Vite | 4.4.9 |
| Frontend | Material-UI | 5.14.5 |
| Backend | Scala | 2.13.12 |
| Backend | Akka HTTP | 10.5.3 |
| Backend | SBT | 1.9.8 |
| Database | PostgreSQL | 16 |
| Containerization | Docker | Latest |

## 🚢 Deployment

See [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md) for:
- Local development setup
- Docker deployment
- Production deployment (AWS, Heroku, DigitalOcean)
- Monitoring & scaling
- Backup & recovery
- CI/CD integration

## 📝 Project Evolution

### Phase 1: Frontend Foundation ✅
- React dashboard with mock data
- Component library
- Routing structure
- Styling system

### Phase 2: Backend API ✅ (Current)
- Scala/Akka HTTP server
- RESTful endpoints
- Service & repository layers
- In-memory data storage

### Phase 3: Database Integration 🔄
- PostgreSQL integration
- Migration system
- Connection pooling
- Query optimization

### Phase 4: Advanced Features 📋
- User authentication (JWT)
- File uploads
- Advanced search/filtering
- Real-time updates (WebSockets)
- Analytics dashboard

### Phase 5: Production Ready 📋
- CI/CD pipeline
- Monitoring & logging
- Performance optimization
- Security hardening
- Infrastructure as Code

## 💾 Data Models

### User
```typescript
{
  id: number
  firstName: string
  lastName: string
  email: string
  phone: string
  img: string
  createdAt: string
  verified: boolean
}
```

### Product
```typescript
{
  id: number
  title: string
  color: string
  producer: string
  price: string
  img: string
  createdAt: string
  inStock: boolean
}
```

### Activity
```typescript
{
  id: number
  userId: number
  text: string
  time: string
}
```

## 🧪 Testing

### Frontend Tests
```bash
cd react-dashboard
npm run test
```

### Backend Tests
```bash
cd scala-backend
sbt test
```

## 📊 Performance Tips

1. **Frontend**:
   - Use React.memo for expensive components
   - Implement virtualization for large lists
   - Lazy load routes
   - Optimize images with CDN

2. **Backend**:
   - Add database indexes
   - Implement caching (Redis)
   - Use pagination for large datasets
   - Connection pooling
   - Query optimization

## 🔒 Security Best Practices

- ✅ Input validation on both frontend and backend
- ✅ CORS configuration
- 🔒 TODO: JWT authentication
- 🔒 TODO: Rate limiting
- 🔒 TODO: SQL injection prevention
- 🔒 TODO: HTTPS/TLS
- 🔒 TODO: XSS protection
- 🔒 TODO: CSRF protection

## 📚 Resources

### Frontend
- [React Documentation](https://react.dev)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Vite Guide](https://vitejs.dev)
- [Material-UI](https://mui.com)
- [Recharts](https://recharts.org)

### Backend
- [Scala Documentation](https://docs.scala-lang.org)
- [Akka HTTP Guide](https://doc.akka.io/docs/akka-http/current/)
- [Circe](https://circe.github.io/circe/)
- [ScalikeJDBC](https://scalikejdbc.org/)

### DevOps
- [Docker Documentation](https://docs.docker.com)
- [Docker Compose](https://docs.docker.com/compose/)
- [PostgreSQL](https://www.postgresql.org/docs/)

## 🤝 Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Make changes and commit: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Create Pull Request

## 📄 License

MIT License - See LICENSE file for details

## 👥 Author

Created as a foundation for scalable full-stack web applications.

## ❓ Troubleshooting

### Frontend Issues
- See `react-dashboard/README.md`
- Check browser console for errors
- Verify backend is running on port 8080

### Backend Issues
- Check SBT compilation errors
- Verify Java/Scala installation
- Review server logs

### Integration Issues
- See `INTEGRATION_GUIDE.md`
- Check CORS configuration
- Verify API endpoints match frontend calls

## 🎯 Next Steps

1. ✅ Complete this setup
2. ⏳ Implement database integration
3. ⏳ Add authentication
4. ⏳ Deploy to production
5. ⏳ Set up CI/CD

## 📞 Support

For issues or questions:
1. Check documentation files
2. Review relevant code comments
3. Check server/console logs
4. Create GitHub issue

---

**Status**: Full stack foundation with mock data ready for production development
**Last Updated**: January 2026
