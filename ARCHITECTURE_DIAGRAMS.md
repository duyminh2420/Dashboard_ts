# System Architecture Diagrams

## Full Stack Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                     USER BROWSER                                │
│                  (http://localhost:3000)                        │
└────────────────────────────┬────────────────────────────────────┘
                             │
                    ┌────────▼────────┐
                    │                 │
                    │   React App     │
                    │  (TypeScript)   │
                    │    + Vite       │
                    │                 │
                    └────────┬────────┘
                             │
                    (REST API Calls)
                             │
                    ┌────────▼────────────────────┐
                    │   CORS Policy Check         │
                    │ (Allow localhost:3000)      │
                    └────────┬────────────────────┘
                             │
      ┌──────────────────────┼──────────────────────┐
      │                      │                      │
   ┌──▼──┐            ┌──────▼──────┐       ┌──────▼────────┐
   │Users│            │  Products   │       │  Activities   │
   │Route│            │   Route     │       │   Route       │
   └──┬──┘            └──────┬──────┘       └──────┬────────┘
      │                      │                      │
      └──────────────────────┼──────────────────────┘
                             │
                    ┌────────▼────────────────────┐
                    │   Service Layer             │
                    │ ┌──────────────────────────┐│
                    │ │ UserService              ││
                    │ │ - Validation             ││
                    │ │ - Statistics             ││
                    │ │ - Business Logic         ││
                    │ └──────────────────────────┘│
                    │ ┌──────────────────────────┐│
                    │ │ ProductService           ││
                    │ │ - Price calculations     ││
                    │ │ - Inventory checks       ││
                    │ └──────────────────────────┘│
                    │ ┌──────────────────────────┐│
                    │ │ ActivityService          ││
                    │ │ - Logging                ││
                    │ │ - Tracking               ││
                    │ └──────────────────────────┘│
                    └────────┬────────────────────┘
                             │
                    ┌────────▼────────────────────┐
                    │  Repository Layer           │
                    │ ┌──────────────────────────┐│
                    │ │ UserRepository           ││
                    │ │ ProductRepository        ││
                    │ │ ActivityRepository       ││
                    │ └──────────────────────────┘│
                    └────────┬────────────────────┘
                             │
      ┌──────────────────────┴──────────────────────┐
      │                                              │
 ┌────▼─────────┐              ┌────────────────────▼────┐
 │In-Memory DB  │              │   PostgreSQL Database   │
 │(Development) │              │   (Production)          │
 │              │              │                        │
 │• Users       │              │• Users Table           │
 │• Products    │              │• Products Table        │
 │• Activities  │              │• Activities Table      │
 └──────────────┘              │• Indexes               │
                               │• Relationships         │
                               └────────────────────────┘
```

## Request Flow

```
┌──────────────────────────────────────────────────────────────┐
│ 1. Client Request                                            │
│    GET /api/users                                            │
└──────────┬───────────────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│ 2. Akka HTTP Router                                          │
│    Match route pattern                                       │
│    Extract parameters                                        │
└──────────┬───────────────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│ 3. Route Handler (UserRoutes)                                │
│    Validate input                                            │
│    Call Service                                              │
└──────────┬───────────────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│ 4. Service Layer (UserService)                              │
│    Business logic                                            │
│    Validation                                                │
│    Transformation                                            │
└──────────┬───────────────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│ 5. Repository Layer (UserRepository)                        │
│    Data access                                               │
│    Query execution                                           │
└──────────┬───────────────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│ 6. Data Source                                               │
│    Fetch data                                                │
│    Return result                                             │
└──────────┬───────────────────────────────────────────────────┘
           │
           ▼ (Future[List[User]])
┌──────────────────────────────────────────────────────────────┐
│ 7. Response Transformation                                   │
│    Wrap in ApiResponse                                       │
│    Serialize to JSON                                         │
│    Set status code                                           │
└──────────┬───────────────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│ 8. HTTP Response                                             │
│    Status: 200 OK                                            │
│    Content-Type: application/json                           │
│    Body: {"success": true, "data": [...], ...}             │
└──────────────────────────────────────────────────────────────┘
```

## Component Interaction

```
┌─────────────────────────────────────────────────────────────────┐
│                      React Frontend                             │
│ ┌────────────────────────────────────────────────────────────┐ │
│ │ Pages (Home, Users, Products)                             │ │
│ └─────────────┬──────────────────────────────────────────────┘ │
│               │                                                 │
│ ┌─────────────▼──────────────────────────────────────────────┐ │
│ │ Components (ChartBox, DataTable, TopBox)                  │ │
│ └─────────────┬──────────────────────────────────────────────┘ │
│               │                                                 │
│ ┌─────────────▼──────────────────────────────────────────────┐ │
│ │ API Utilities (fetch functions)                           │ │
│ │ - userApi.getAll()                                        │ │
│ │ - productApi.getAll()                                     │ │
│ │ - activityApi.getAll()                                    │ │
│ └─────────────┬──────────────────────────────────────────────┘ │
└────────────────┼──────────────────────────────────────────────┘
                 │
        ┌────────▼────────┐
        │   HTTP Request  │
        │   (Fetch API)   │
        └────────┬────────┘
                 │
      ┌──────────▼──────────┐
      │   Akka HTTP Server  │
      │   :8080             │
      │ ┌──────────────────┐│
      │ │ UserRoutes       ││
      │ │ ProductRoutes    ││
      │ │ ActivityRoutes   ││
      │ └──────────────────┘│
      │ ┌──────────────────┐│
      │ │ Services         ││
      │ │ Repositories     ││
      │ │ Data             ││
      │ └──────────────────┘│
      └──────────┬──────────┘
                 │
        ┌────────▼────────┐
        │   JSON Response │
        └────────┬────────┘
                 │
      ┌──────────▼──────────┐
      │  React parses JSON  │
      │  Updates State      │
      │  Re-renders UI      │
      └─────────────────────┘
```

## Service Layer Responsibilities

```
┌────────────────────────────────────────────────────────────┐
│                    USER REQUEST                            │
└────────────┬───────────────────────────────────────────────┘
             │
             ▼
┌────────────────────────────────────────────────────────────┐
│ INPUT VALIDATION                                           │
│ • Check if email is valid                                 │
│ • Verify required fields present                          │
│ • Validate data types                                     │
└────────────┬───────────────────────────────────────────────┘
             │
             ▼
┌────────────────────────────────────────────────────────────┐
│ BUSINESS LOGIC                                             │
│ • Calculate statistics                                    │
│ • Apply business rules                                    │
│ • Transform data                                          │
└────────────┬───────────────────────────────────────────────┘
             │
             ▼
┌────────────────────────────────────────────────────────────┐
│ REPOSITORY CALL                                            │
│ • Call appropriate repository method                      │
│ • Handle async operations                                 │
└────────────┬───────────────────────────────────────────────┘
             │
             ▼
┌────────────────────────────────────────────────────────────┐
│ RESULT PROCESSING                                          │
│ • Process repository response                             │
│ • Apply additional logic if needed                        │
│ • Format final response                                   │
└────────────┬───────────────────────────────────────────────┘
             │
             ▼
┌────────────────────────────────────────────────────────────┐
│ RETURN RESULT                                              │
│ Future[Either[Error, Result]]                             │
└────────────────────────────────────────────────────────────┘
```

## Data Flow for User List Request

```
Frontend: userApi.getAll()
    │
    ▼
GET /api/users
    │
    ▼
UserRoutes.routes
    ├─ Matches: GET /api/users
    ├─ Extracts parameters (search, page)
    └─ Calls userService.searchUsers(query)
    │
    ▼
UserService.searchUsers(query)
    ├─ Validates query
    ├─ Calls userRepository.search(query)
    └─ Returns Future[List[User]]
    │
    ▼
UserRepository.search(query)
    ├─ Filters in-memory list OR queries database
    ├─ Returns matching users
    └─ Returns Future[List[User]]
    │
    ▼
Response Handling
    ├─ Wraps in ApiResponse[List[User]]
    ├─ Sets status 200 OK
    ├─ Serializes to JSON with Circe
    └─ Sends to client
    │
    ▼
Frontend: response.json()
    ├─ Parses JSON
    ├─ Updates state
    └─ Re-renders component
```

## Error Handling Flow

```
Error Occurs (e.g., Invalid Email)
    │
    ▼
Service Layer
    ├─ Catches exception
    ├─ Validates it's expected error
    └─ Returns Future.failed(new IllegalArgumentException(...))
    │
    ▼
Route Handler
    ├─ onComplete { case Failure(ex) => ... }
    ├─ Status: 400 Bad Request
    └─ Body: ApiResponse(success=false, message="Error text")
    │
    ▼
HTTP Response
    ├─ Status: 400
    ├─ Content-Type: application/json
    └─ Body: {"success": false, "data": null, "message": "Invalid email"}
    │
    ▼
Frontend
    ├─ Checks response.success
    ├─ Displays error message
    └─ Provides feedback to user
```

## Service Layer Architecture

```
┌─────────────────────────────────────────────────────┐
│             Routes (HTTP Layer)                     │
│  UserRoutes | ProductRoutes | ActivityRoutes        │
└────────────────┬────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────┐
│             Services (Business Layer)               │
│  ┌─────────────────────────────────────────────┐   │
│  │ UserService                                 │   │
│  │ • getAllUsers()                             │   │
│  │ • getUserById(id)                           │   │
│  │ • createUser(user)                          │   │
│  │ • updateUser(id, user)                      │   │
│  │ • deleteUser(id)                            │   │
│  │ • searchUsers(query)                        │   │
│  │ • getUserStatistics()                       │   │
│  └─────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────┐   │
│  │ ProductService                              │   │
│  │ • getAllProducts()                          │   │
│  │ • getProductById(id)                        │   │
│  │ • createProduct(product)                    │   │
│  │ • updateProduct(id, product)                │   │
│  │ • deleteProduct(id)                         │   │
│  │ • getInStockProducts()                      │   │
│  │ • getProductStatistics()                    │   │
│  └─────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────┐   │
│  │ ActivityService                             │   │
│  │ • getAllActivities()                        │   │
│  │ • getActivityById(id)                       │   │
│  │ • getUserActivities(userId)                 │   │
│  │ • createActivity(activity)                  │   │
│  │ • deleteActivity(id)                        │   │
│  │ • getRecentActivities(limit)                │   │
│  └─────────────────────────────────────────────┘   │
└────────────────┬────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────┐
│          Repositories (Data Access Layer)            │
│  UserRepository | ProductRepository | ActivityRepo  │
└────────────────┬────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────┐
│              Data Sources                           │
│        In-Memory OR PostgreSQL                      │
└─────────────────────────────────────────────────────┘
```

## Deployment Architecture

```
┌────────────────────────────────────────────────────────────┐
│                  Docker Compose                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ docker-compose.yml                                   │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                            │
│  ┌────────────────┐  ┌─────────────────┐ ┌────────────┐  │
│  │   Frontend     │  │  Backend        │ │ Database   │  │
│  │ Service        │  │  Service        │ │ Service    │  │
│  │                │  │                 │ │            │  │
│  │ Port: 3000     │  │ Port: 8080      │ │ Port: 5432 │  │
│  │ Image: React   │  │ Image: Scala    │ │ Image: PG  │  │
│  └────┬───────────┘  └────────┬────────┘ └──────┬─────┘  │
│       │                       │                  │        │
│       └─────────┬─────────────┴──────────────────┘        │
│               Network: dashboard-network                 │
└────────────────────────────────────────────────────────────┘
```

---

These diagrams show:
1. Complete full-stack architecture
2. Request/response flow
3. Component interactions
4. Service responsibilities
5. Error handling
6. Deployment setup

Use these for understanding system behavior and documentation!
