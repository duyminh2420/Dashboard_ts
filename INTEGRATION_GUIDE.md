# Integration Guide: React Frontend with Scala Backend

## Overview

This guide explains how to connect your React TypeScript Dashboard with the Scala/Akka HTTP backend.

## Architecture Diagram

```
┌─────────────────────────┐
│   React Frontend        │
│  (TypeScript/Vite)      │
└────────────┬────────────┘
             │ HTTP/JSON
             ▼
┌─────────────────────────────────┐
│   CORS Layer (akka-http-cors)   │
└────────────┬────────────────────┘
             │
┌─────────────────────────────────┐
│   Scala/Akka HTTP Routes        │
├─────────────────────────────────┤
│ • UserRoutes                    │
│ • ProductRoutes                 │
│ • ActivityRoutes                │
└────────────┬────────────────────┘
             │
┌─────────────────────────────────┐
│   Service Layer (Business Logic)│
├─────────────────────────────────┤
│ • UserService                   │
│ • ProductService                │
│ • ActivityService               │
└────────────┬────────────────────┘
             │
┌─────────────────────────────────┐
│   Repository Layer (Data Access)│
├─────────────────────────────────┤
│ • InMemoryUserRepository        │
│ • InMemoryProductRepository     │
│ • InMemoryActivityRepository    │
│ (or PostgreSQL variants)        │
└────────────┬────────────────────┘
             │
┌─────────────────────────────────┐
│   Database (PostgreSQL)         │
└─────────────────────────────────┘
```

## Step 1: Start the Backend

### Development Mode (In-Memory Data)

```bash
cd scala-backend
sbt run
```

Server runs at: `http://localhost:8080`

### Production Mode (with Docker & PostgreSQL)

```bash
docker-compose up --build
```

This starts:
- Backend on `http://localhost:8080`
- PostgreSQL on `localhost:5432`
- Frontend on `http://localhost:3000`

## Step 2: Update Frontend API Configuration

Create or update `src/utils/api.ts`:

```typescript
// src/utils/api.ts
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

export interface ApiResponse<T> {
  success: boolean;
  data: T | null;
  message?: string;
  timestamp: number;
}

// Users
export const userApi = {
  getAll: async (search?: string) => {
    const url = new URL(`${API_BASE_URL}/users`);
    if (search) url.searchParams.append('search', search);
    const response = await fetch(url);
    return response.json() as Promise<ApiResponse<any[]>>;
  },
  
  getById: async (id: number) => {
    const response = await fetch(`${API_BASE_URL}/users/${id}`);
    return response.json() as Promise<ApiResponse<any>>;
  },
  
  create: async (user: any) => {
    const response = await fetch(`${API_BASE_URL}/users`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user),
    });
    return response.json() as Promise<ApiResponse<any>>;
  },
  
  update: async (id: number, user: any) => {
    const response = await fetch(`${API_BASE_URL}/users/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user),
    });
    return response.json() as Promise<ApiResponse<any>>;
  },
  
  delete: async (id: number) => {
    const response = await fetch(`${API_BASE_URL}/users/${id}`, {
      method: 'DELETE',
    });
    return response.json() as Promise<ApiResponse<any>>;
  },
  
  getStats: async () => {
    const response = await fetch(`${API_BASE_URL}/users/stats/overview`);
    return response.json() as Promise<ApiResponse<any>>;
  },
};

// Products
export const productApi = {
  getAll: async (search?: string, inStock?: boolean) => {
    const url = new URL(`${API_BASE_URL}/products`);
    if (search) url.searchParams.append('search', search);
    if (inStock !== undefined) url.searchParams.append('inStock', String(inStock));
    const response = await fetch(url);
    return response.json() as Promise<ApiResponse<any[]>>;
  },
  
  getById: async (id: number) => {
    const response = await fetch(`${API_BASE_URL}/products/${id}`);
    return response.json() as Promise<ApiResponse<any>>;
  },
  
  create: async (product: any) => {
    const response = await fetch(`${API_BASE_URL}/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(product),
    });
    return response.json() as Promise<ApiResponse<any>>;
  },
  
  update: async (id: number, product: any) => {
    const response = await fetch(`${API_BASE_URL}/products/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(product),
    });
    return response.json() as Promise<ApiResponse<any>>;
  },
  
  delete: async (id: number) => {
    const response = await fetch(`${API_BASE_URL}/products/${id}`, {
      method: 'DELETE',
    });
    return response.json() as Promise<ApiResponse<any>>;
  },
  
  getStats: async () => {
    const response = await fetch(`${API_BASE_URL}/products/stats/overview`);
    return response.json() as Promise<ApiResponse<any>>;
  },
};

// Activities
export const activityApi = {
  getAll: async (recent?: number) => {
    const url = new URL(`${API_BASE_URL}/activities`);
    if (recent) url.searchParams.append('recent', String(recent));
    const response = await fetch(url);
    return response.json() as Promise<ApiResponse<any[]>>;
  },
  
  getByUserId: async (userId: number) => {
    const response = await fetch(`${API_BASE_URL}/activities/user/${userId}`);
    return response.json() as Promise<ApiResponse<any[]>>;
  },
  
  create: async (activity: any) => {
    const response = await fetch(`${API_BASE_URL}/activities`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(activity),
    });
    return response.json() as Promise<ApiResponse<any>>;
  },
  
  delete: async (id: number) => {
    const response = await fetch(`${API_BASE_URL}/activities/${id}`, {
      method: 'DELETE',
    });
    return response.json() as Promise<ApiResponse<any>>;
  },
};

// Health check
export const healthApi = {
  check: async () => {
    const response = await fetch(`${API_BASE_URL.replace('/api', '')}/api/health`);
    return response.text();
  },
};
```

## Step 3: Create React Hooks for Data Fetching

`src/hooks/useUsers.ts`:

```typescript
import { useEffect, useState } from 'react';
import { userApi } from '../utils/api';

export const useUsers = () => {
  const [users, setUsers] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await userApi.getAll();
        if (response.success) {
          setUsers(response.data || []);
        } else {
          setError(response.message || 'Failed to fetch users');
        }
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Unknown error');
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  return { users, loading, error };
};
```

Similarly for `useProducts.ts` and `useActivities.ts`

## Step 4: Replace Mock Data with API Calls

### Before (mock data):

```typescript
// src/pages/users/Users.tsx
import { users } from "../../data";

export const Users = () => {
  // Uses static mock data
};
```

### After (API calls):

```typescript
// src/pages/users/Users.tsx
import { useEffect, useState } from "react";
import DataTable from "../../components/dataTable/DataTable";
import { userApi } from "../../utils/api";

export const Users = () => {
  const [users, setUsers] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await userApi.getAll();
        if (response.success) {
          setUsers(response.data || []);
        }
      } catch (error) {
        console.error('Failed to fetch users:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div className="users">
      <DataTable columns={columns} rows={users} />
    </div>
  );
};
```

## Step 5: Environment Configuration

Create `.env` in react-dashboard:

```env
REACT_APP_API_URL=http://localhost:8080/api
```

For production:

```env
REACT_APP_API_URL=https://api.yourdomain.com/api
```

## Step 6: Add Error Handling Context

`src/context/ErrorContext.tsx`:

```typescript
import { createContext, useContext, useState } from 'react';

interface ApiError {
  message: string;
  timestamp: number;
}

interface ErrorContextType {
  errors: ApiError[];
  addError: (message: string) => void;
  clearErrors: () => void;
}

const ErrorContext = createContext<ErrorContextType | undefined>(undefined);

export const ErrorProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [errors, setErrors] = useState<ApiError[]>([]);

  const addError = (message: string) => {
    setErrors([...errors, { message, timestamp: Date.now() }]);
    setTimeout(() => {
      setErrors(prev => prev.slice(1));
    }, 5000);
  };

  const clearErrors = () => setErrors([]);

  return (
    <ErrorContext.Provider value={{ errors, addError, clearErrors }}>
      {children}
    </ErrorContext.Provider>
  );
};

export const useErrors = () => {
  const context = useContext(ErrorContext);
  if (!context) throw new Error('useErrors must be used within ErrorProvider');
  return context;
};
```

## API Response Format

All endpoints follow this response format:

```json
{
  "success": true,
  "data": { /* actual data */ },
  "message": null,
  "timestamp": 1704067200000
}
```

Error response:

```json
{
  "success": false,
  "data": null,
  "message": "Error description",
  "timestamp": 1704067200000
}
```

## Testing the Connection

1. Start backend: `sbt run` (or docker-compose up)
2. Start frontend: `npm start`
3. Open browser console and test:

```javascript
// Test endpoint
fetch('http://localhost:8080/api/health')
  .then(r => r.text())
  .then(console.log);

// Get users
fetch('http://localhost:8080/api/users')
  .then(r => r.json())
  .then(console.log);
```

## Troubleshooting

### CORS Error

Ensure backend has CORS enabled (it does by default). If still failing:

1. Check backend logs for errors
2. Verify frontend URL matches CORS configuration
3. Try accessing backend directly: `http://localhost:8080/api/health`

### Connection Refused

- Backend not running? Start with `sbt run`
- Wrong port? Check backend runs on 8080
- Firewall blocking? Check network settings

### Data Not Updating

- Use React DevTools to check state
- Check network tab for failed requests
- Verify response status codes

## Performance Tips

1. **Cache API responses**: Use React Query or SWR
2. **Pagination**: Add limit/offset to list endpoints
3. **Debounce searches**: Use debounce on search input
4. **Lazy load data**: Load charts/tables on demand

## Security Considerations

- ✅ CORS enabled for frontend origin
- 🔒 TODO: Add JWT authentication
- 🔒 TODO: Add request rate limiting
- 🔒 TODO: Validate input on both frontend and backend
- 🔒 TODO: Use HTTPS in production

## Next Steps

1. Implement database integration (PostgreSQL)
2. Add user authentication
3. Implement caching (Redis)
4. Add advanced search/filtering
5. Set up CI/CD pipeline
6. Deploy to production

## Resources

- [Akka HTTP Documentation](https://doc.akka.io/docs/akka-http/current/)
- [Scala Best Practices](https://docs.scala-lang.org/)
- [React Query](https://tanstack.com/query/latest)
- [REST API Best Practices](https://restfulapi.net/)
