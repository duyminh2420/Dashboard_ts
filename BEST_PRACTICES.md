# Best Practices & Development Guidelines

## 🏗️ Architecture Best Practices

### 1. Layer Separation
✅ **Do**: Keep routes, services, and repositories separate
```scala
// Good: Clear separation
class UserRoutes(service: UserService) { ... }
class UserService(repository: UserRepository) { ... }
trait UserRepository { ... }
```

❌ **Don't**: Mix business logic with HTTP handling
```scala
// Bad: Logic in routes
class UserRoutes {
  val routes = path("users") {
    get {
      // Complex business logic here - WRONG!
    }
  }
}
```

### 2. Dependency Injection
✅ **Do**: Inject dependencies through constructors
```scala
class UserService(userRepository: UserRepository)
class UserRoutes(userService: UserService)
```

❌ **Don't**: Create dependencies inside classes
```scala
class UserService {
  val repository = new InMemoryUserRepository() // WRONG!
}
```

### 3. Type Safety
✅ **Do**: Use explicit types and avoid `Any`
```scala
def getUser(id: Int): Future[Option[User]] = ???
```

❌ **Don't**: Use generic types without specifics
```scala
def getUser(id: Any): Future[Any] = ??? // WRONG!
```

## 🎯 Scala & Functional Programming

### 1. Immutability
✅ **Do**: Prefer immutable data structures
```scala
case class User(id: Int, name: String) // Immutable by default
val users = List(user1, user2) // Immutable list
```

❌ **Don't**: Use mutable collections
```scala
val users = scala.collection.mutable.ListBuffer() // Avoid when possible
users += newUser
```

### 2. Pattern Matching
✅ **Do**: Use pattern matching for comprehensive handling
```scala
onComplete(userService.getUser(id)) {
  case Success(Some(user)) => complete(user)
  case Success(None) => complete(StatusCodes.NotFound)
  case Failure(ex) => complete(StatusCodes.InternalServerError)
}
```

❌ **Don't**: Use if-else chains
```scala
// Avoid cascading if-else
val result = ...
if (result != null) {
  if (result.isDefined) {
    // ...
  }
}
```

### 3. Option Type
✅ **Do**: Use Option for nullable values
```scala
def findUser(id: Int): Future[Option[User]]
```

❌ **Don't**: Return null
```scala
def findUser(id: Int): Future[User] {
  // Returns null on not found - WRONG!
}
```

### 4. Future Handling
✅ **Do**: Chain futures with map, flatMap
```scala
userService.getUser(id)
  .map(_.map(_.email))
  .recover { case ex => None }
```

❌ **Don't**: Use Thread.sleep or blocking operations
```scala
Thread.sleep(1000) // BLOCKS THE THREAD!
```

## 📝 Code Organization

### 1. File Structure
✅ **Do**: Organize by feature/domain
```
repositories/
  UserRepository.scala
  ProductRepository.scala
  
services/
  UserService.scala
  ProductService.scala
```

❌ **Don't**: Organize by type
```
controllers/    // Don't mix layers
models/
services/
```

### 2. Naming Conventions
✅ **Do**: Use meaningful, descriptive names
```scala
def getUsersByAge(minAge: Int): Future[List[User]] = ???
def isValidEmail(email: String): Boolean = ???
```

❌ **Don't**: Use unclear abbreviations
```scala
def getU(a: Int) = ??? // What does 'a' mean?
def chk(s: String) = ??? // What is 'chk'?
```

### 3. Method Size
✅ **Do**: Keep methods small and focused
```scala
def createUser(user: User): Future[User] = {
  if (!isValidEmail(user.email)) {
    Future.failed(new IllegalArgumentException("Invalid email"))
  } else {
    repository.create(user)
  }
}
```

❌ **Don't**: Create massive methods
```scala
def complexOperation(params: Map[String, Any]): Any = {
  // 500 lines of code - WRONG!
}
```

## 🛡️ Error Handling

### 1. Explicit Error Cases
✅ **Do**: Handle all cases explicitly
```scala
onComplete(operation) {
  case Success(value) => complete(StatusCodes.OK -> value)
  case Failure(ex) => complete(StatusCodes.BadRequest -> error(ex.message))
}
```

❌ **Don't**: Silently fail or swallow errors
```scala
operation.recover { case _ => None } // What error? No logging!
```

### 2. Meaningful Error Messages
✅ **Do**: Provide clear error messages
```scala
if (!isValidEmail(email)) {
  throw new IllegalArgumentException("Email format must be user@domain.com")
}
```

❌ **Don't**: Generic or empty messages
```scala
if (!isValidEmail(email)) {
  throw new Exception("Error") // Unhelpful!
}
```

### 3. Error Responses
✅ **Do**: Use consistent error response format
```scala
ApiResponse.error[User]("User with ID 1 not found")
```

❌ **Don't**: Return different formats
```scala
// Sometimes this
StatusCodes.NotFound -> "Not found"

// Sometimes this
StatusCodes.NotFound -> Json.obj("error" -> "Not found")

// Inconsistent!
```

## 🧪 Testing

### 1. Unit Test Structure
✅ **Do**: Test service logic in isolation
```scala
class UserServiceTest extends AnyFunSuite {
  val mockRepository = mock[UserRepository]
  val service = new UserService(mockRepository)
  
  test("getUserById returns user when found") {
    when(mockRepository.getById(1)).thenReturn(Future.successful(Some(user)))
    val result = service.getUserById(1)
    assert(result.isDefined)
  }
}
```

### 2. Test Naming
✅ **Do**: Use descriptive test names
```scala
test("should return user when valid ID provided")
test("should throw exception when email is invalid")
test("should calculate statistics correctly")
```

❌ **Don't**: Use unclear names
```scala
test("test1")
test("works")
test("check")
```

## 📊 Performance

### 1. Async Operations
✅ **Do**: Always return Future for I/O operations
```scala
def getUser(id: Int): Future[Option[User]] = ???
```

❌ **Don't**: Block threads
```scala
def getUser(id: Int): Option[User] = {
  Thread.sleep(1000) // Blocking!
  // ...
}
```

### 2. Query Optimization
✅ **Do**: Add indexes and limit results
```scala
// Good: Limited result set
def getRecentActivities(limit: Int = 10): Future[List[Activity]]

// Database: CREATE INDEX idx_created_at ON activities(created_at DESC)
```

❌ **Don't**: Fetch everything
```scala
def getAllActivities(): Future[List[Activity]] // Could be millions!
```

### 3. Lazy Evaluation
✅ **Do**: Use lazy evaluation for expensive computations
```scala
lazy val statistics = calculateStatistics() // Computed only when accessed
```

❌ **Don't**: Eagerly compute everything
```scala
val stats = calculateStatistics() // Even if not used!
```

## 🔒 Security

### 1. Input Validation
✅ **Do**: Always validate user input
```scala
def createUser(user: User): Future[User] = {
  if (user.email.isEmpty || !isValidEmail(user.email)) {
    Future.failed(new IllegalArgumentException("Invalid email"))
  } else {
    repository.create(user)
  }
}
```

❌ **Don't**: Trust user input
```scala
def createUser(user: User): Future[User] = {
  repository.create(user) // What if email is invalid?
}
```

### 2. SQL Injection Prevention
✅ **Do**: Use prepared statements
```scala
// ScalikeJDBC does this automatically with parameters
SQL("SELECT * FROM users WHERE email = ?").bind(email).map(rs => ???)
```

❌ **Don't**: String concatenation
```scala
// VULNERABLE!
SQL(s"SELECT * FROM users WHERE email = '$email'")
```

### 3. Authentication
✅ **Do**: Always verify requests
```scala
def deleteUser(id: Int, token: String): Future[Boolean] = {
  if (!isValidToken(token)) {
    Future.failed(new SecurityException("Invalid token"))
  } else {
    repository.delete(id)
  }
}
```

❌ **Don't**: Skip authentication
```scala
def deleteUser(id: Int): Future[Boolean] = {
  repository.delete(id) // No auth check!
}
```

## 📚 Documentation

### 1. Method Documentation
✅ **Do**: Document public methods
```scala
/**
  * Retrieves a user by ID
  * @param id The user ID
  * @return Future containing Option[User]
  * @throws IllegalArgumentException if id <= 0
  */
def getUserById(id: Int): Future[Option[User]] = ???
```

❌ **Don't**: Leave code undocumented
```scala
def getUserById(id: Int): Future[Option[User]] = ??? // What does it do?
```

### 2. Type Hints
✅ **Do**: Always specify return types
```scala
def calculateTotal(prices: List[Double]): Double = ???
```

❌ **Don't**: Rely on type inference for public APIs
```scala
def calculateTotal(prices) = ??? // Type unclear
```

## 🚀 Production Readiness

### 1. Configuration
✅ **Do**: Externalize configuration
```scala
val port = sys.env.getOrElse("PORT", "8080").toInt
val dbHost = sys.env.getOrElse("DB_HOST", "localhost")
```

❌ **Don't**: Hardcode values
```scala
val port = 8080 // What if we need to change this?
val dbHost = "localhost"
```

### 2. Logging
✅ **Do**: Log important events
```scala
logger.info(s"User created with ID: ${user.id}")
logger.warn(s"Failed to fetch user: ${ex.getMessage}")
logger.error("Database connection failed", ex)
```

❌ **Don't**: Omit logging
```scala
// Silent failure
repository.create(user)
```

### 3. Monitoring
✅ **Do**: Include health checks
```scala
path("health") {
  get {
    complete("OK")
  }
}
```

❌ **Don't**: Deploy without monitoring
```scala
// No way to check if service is running
```

## 🔄 Maintenance

### 1. Version Control
✅ **Do**: Make meaningful commits
```
commit: "Add email validation to UserService"
```

❌ **Don't**: Vague commits
```
commit: "fix stuff"
commit: "update"
```

### 2. Code Reviews
✅ **Do**: Review others' code
- Check for logic errors
- Verify error handling
- Ensure consistency

### 3. Technical Debt
✅ **Do**: Refactor regularly
- Update dependencies
- Remove unused code
- Simplify complex logic

❌ **Don't**: Accumulate debt
- "We'll fix it later"
- Never refactoring

## 🎓 Learning Resources

### Code Quality
- [Scala Style Guide](https://docs.scala-lang.org/style/)
- [SonarQube](https://www.sonarqube.org/) for code analysis
- [Scalafmt](https://scalameta.org/scalafmt/) for formatting

### Akka Best Practices
- [Akka Best Practices](https://doc.akka.io/docs/akka/current/general/index.html)
- [Reactive Principles](https://www.reactivemanifesto.org/)

### Testing
- [ScalaTest](https://www.scalatest.org/)
- [Mockito for Scala](https://github.com/mockito/mockito-scala)

## ✅ Pre-Deployment Checklist

- [ ] All tests passing
- [ ] Code follows style guide
- [ ] No compiler warnings
- [ ] Error handling complete
- [ ] Input validation in place
- [ ] Logging configured
- [ ] No hardcoded values
- [ ] Dependencies up to date
- [ ] Docker builds successfully
- [ ] Environment variables documented
- [ ] Database migrations tested
- [ ] Performance acceptable
- [ ] Security review passed
- [ ] Documentation updated

---

**Remember**: Write code for humans first, machines second!

These guidelines help ensure:
- 🔒 **Security**: Input validation, error handling
- 🚀 **Performance**: Non-blocking, optimized queries
- 📖 **Maintainability**: Clear, organized code
- ✅ **Reliability**: Proper error handling, testing
- 🎯 **Scalability**: Horizontal scaling ready

Follow these patterns and your codebase will be professional and maintainable!
