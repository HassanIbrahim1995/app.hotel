# Shift Manager - Intelligent Shift Worker Management System

## Overview
Shift Manager is a comprehensive solution for intelligent shift worker management. The system provides advanced scheduling, employee interaction, and organizational optimization capabilities through a modular microservices architecture.

## Key Features
- **Smart Shift Scheduling**: AI-powered scheduling to optimize shifts based on employee skills, preferences, and availability
- **Shift Swapping**: Built-in intelligent shift swapping system to help employees find the best matches
- **Employee & Manager Dashboards**: Role-specific views with personalized information
- **Calendar Integration**: Individual and team calendars with integration capabilities
- **Vacation Request Management**: Automated system to request, approve, and track time off
- **Reporting & Analytics**: Powerful reporting tools with PDF export functionality
- **Real-time Notifications**: Email and in-app notifications for important events
- **Mobile Integration**: API designed for mobile application consumption

## Technical Architecture

### Backend (API Module)
- **Java 21** with Spring Boot 3.x
- **Microservices Architecture** for scalability and modularity
- **Spring Security** with JWT for authentication
- **Liquibase** for database migration management
- **PostgreSQL** for data persistence
- **MapStruct** for object mapping
- **Spring Problem Details** (RFC 7807) for error handling
- **JUnit 5** for testing

### Frontend (Web Client)
- **Angular 16+** with TypeScript
- **Bootstrap 5** for responsive UI components
- **FullCalendar** for calendar visualization
- **Chart.js** for analytics dashboard
- **Angular JWT** for token management

## Project Structure

### API Module
```
./api-module/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/shiftmanager/api/
│   │   │       ├── config/         # Spring configuration classes
│   │   │       ├── controller/     # REST API controllers
│   │   │       ├── dto/            # Data Transfer Objects
│   │   │       ├── exception/      # Exception handling
│   │   │       ├── mapper/         # Object mapping
│   │   │       ├── model/          # Domain entities
│   │   │       ├── repository/     # Data access layer
│   │   │       ├── security/       # Authentication & authorization
│   │   │       └── service/        # Business logic
│   │   └── resources/
│   │       ├── db/
│   │       │   ├── changelog/      # Liquibase change logs
│   │       │   └── migration/      # SQL migrations
│   │       └── application.yml     # Application configuration
│   └── test/                       # Unit and integration tests
└── pom.xml                         # Maven build file
```

### Web Client
```
./web-client/
├── src/
│   ├── app/
│   │   ├── components/             # Angular components
│   │   │   ├── admin/              # Admin-specific views
│   │   │   ├── auth/               # Authentication views
│   │   │   ├── employee/           # Employee views
│   │   │   ├── manager/            # Manager views
│   │   │   └── shared/             # Shared components
│   │   ├── core/                   # Core functionality
│   │   │   ├── guards/             # Route guards
│   │   │   └── interceptors/       # HTTP interceptors
│   │   ├── models/                 # Type definitions
│   │   ├── services/               # API services
│   │   │   └── algorithms/         # Scheduling algorithms
│   │   ├── app-routing.module.ts   # Routing configuration
│   │   └── app.module.ts           # Main module
│   ├── assets/                     # Static assets
│   └── environments/               # Environment configurations
└── package.json                    # NPM dependencies
```

## Getting Started

### Prerequisites
- Java 21 JDK
- Node.js 18+ and npm
- Docker and Docker Compose
- Kubernetes cluster for production deployment

### Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/shift-manager.git
   cd shift-manager
   ```

2. **Start the PostgreSQL database**
   ```bash
   docker-compose up -d postgres
   ```

3. **Run the API module**
   ```bash
   cd api-module
   ./mvnw spring-boot:run
   ```

4. **Run the Web Client**
   ```bash
   cd web-client
   npm install
   npm start
   ```

5. **Access the application**
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html

## Deployment

### Docker Deployment
```bash
# Build Docker images
docker-compose build

# Run all services
docker-compose up -d
```

### Kubernetes Deployment
The Kubernetes deployment files are located in the `k8s/` directory. You can deploy the application to your Kubernetes cluster using:

```bash
# Create namespace
kubectl create namespace shift-manager

# Apply database configuration
kubectl apply -f k8s/postgres.yaml

# Apply backend API configuration
kubectl apply -f k8s/api.yaml

# Apply frontend configuration
kubectl apply -f k8s/web-client.yaml
```

Refer to the Kubernetes deployment guide in `k8s/README.md` for more detailed instructions.

## Testing

### Backend Tests
```bash
cd api-module
./mvnw test
```

### Frontend Tests
```bash
cd web-client
npm test
```

## API Documentation
Once the application is running, you can access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Contributing
Please refer to our [Contributing Guidelines](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
