# Steam Library Integration App

A modern web application built with **Spring Boot** that allows users to authenticate, link their Steam accounts, and view their game library with detailed statistics.

## Features

- 🔐 **User Authentication** - Secure user registration and login with Spring Security
- 🎮 **Steam Integration** - Link Steam account using OpenID authentication
- 📊 **Library Dashboard** - View your complete game collection with statistics
- ⏱️ **Playtime Tracking** - See total hours played for each game
- 🔄 **Auto Sync** - Sync your Steam library on demand
- 🎨 **Modern UI** - Beautiful, responsive design with warm color palette

## Tech Stack

### Backend
- **Spring Boot 3.2.0** - Core framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **H2 Database** - In-memory database (development)
- **MySQL** - Production database support
- **OpenID4Java** - Steam OpenID authentication
- **Thymeleaf** - Server-side templating

### Frontend
- **HTML5 & CSS3** - Modern web standards
- **Thymeleaf Templates** - Dynamic content rendering
- **Responsive Design** - Mobile-friendly interface

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **Steam API Key** - Get one from [Steam Web API](https://steamcommunity.com/dev/apikey)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd steam-library-java
```

### 2. Configure Steam API Key

Open `src/main/resources/application.properties` and update:

```properties
steam.api.key=YOUR_STEAM_API_KEY_HERE
```

**How to get a Steam API Key:**
1. Visit https://steamcommunity.com/dev/apikey
2. Sign in with your Steam account
3. Enter a domain name (can be localhost for development)
4. Copy the generated API key

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR file directly:

```bash
java -jar target/steam-library-app-1.0.0.jar
```

### 5. Access the Application

Open your browser and navigate to:
```
http://localhost:8080
```

## Configuration

### Database Configuration

**Development (H2 - Default):**
The application uses H2 in-memory database by default. Access the H2 console at:
```
http://localhost:8080/h2-console
```

**Production (MySQL):**
To use MySQL, update `application.properties`:

```properties
# Comment out H2 configuration
# spring.datasource.url=jdbc:h2:mem:steamlibdb

# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/steam_library
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### Application Properties

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | Application port | 8080 |
| `steam.api.key` | Steam Web API key | Required |
| `steam.openid.url` | Steam OpenID endpoint | https://steamcommunity.com/openid/login |
| `app.base.url` | Application base URL | http://localhost:8080 |

## Project Structure

```
steam-library-java/
├── src/main/java/com/steamlibrary/
│   ├── config/              # Configuration classes
│   │   └── SecurityConfig.java
│   ├── controller/          # MVC controllers
│   │   ├── AuthController.java
│   │   ├── DashboardController.java
│   │   ├── HomeController.java
│   │   └── SteamController.java
│   ├── dto/                 # Data Transfer Objects
│   │   ├── RegisterRequest.java
│   │   ├── SteamGameDto.java
│   │   └── SteamProfileDto.java
│   ├── model/               # JPA entities
│   │   ├── User.java
│   │   ├── SteamAccount.java
│   │   └── SteamGame.java
│   ├── repository/          # Spring Data repositories
│   │   ├── UserRepository.java
│   │   ├── SteamAccountRepository.java
│   │   └── SteamGameRepository.java
│   ├── service/             # Business logic
│   │   ├── CustomUserDetailsService.java
│   │   ├── SteamOpenIdService.java
│   │   ├── SteamService.java
│   │   └── UserService.java
│   └── SteamLibraryApplication.java
├── src/main/resources/
│   ├── templates/           # Thymeleaf templates
│   │   ├── dashboard.html
│   │   ├── index.html
│   │   ├── login.html
│   │   └── register.html
│   └── application.properties
├── pom.xml
└── README.md
```

## Usage Guide

### 1. Create an Account

1. Navigate to the home page
2. Click "Get Started" or "Register"
3. Fill in username, email, and password
4. Click "Create Account"

### 2. Login

1. Click "Login" from the home page
2. Enter your username and password
3. Click "Sign In"

### 3. Link Steam Account

1. After logging in, you'll see the dashboard
2. Click "Link Steam Account"
3. You'll be redirected to Steam's login page
4. Sign in with your Steam credentials
5. Authorize the application
6. You'll be redirected back with your Steam account linked

### 4. View Your Library

Once linked, your dashboard will show:
- Total games count
- Total playtime across all games
- Steam profile information
- Complete game library with individual playtime

### 5. Sync Library

Click "Sync Library" to refresh your game collection from Steam.

## API Endpoints

### Public Endpoints
- `GET /` - Home page
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /register` - Registration page
- `POST /register` - Process registration

### Authenticated Endpoints
- `GET /dashboard` - User dashboard
- `GET /steam/link` - Initiate Steam linking
- `GET /steam/callback` - Steam OAuth callback
- `GET /steam/sync` - Sync Steam library
- `POST /logout` - Logout

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

### Steam Accounts Table
```sql
CREATE TABLE steam_accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    steam_id VARCHAR(255) UNIQUE NOT NULL,
    persona_name VARCHAR(255),
    profile_url VARCHAR(500),
    avatar_url VARCHAR(500),
    linked_at TIMESTAMP,
    last_synced TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Steam Games Table
```sql
CREATE TABLE steam_games (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    app_id BIGINT NOT NULL,
    steam_id VARCHAR(255) NOT NULL,
    game_name VARCHAR(500),
    playtime_forever INT,
    playtime_2weeks INT,
    img_icon_url VARCHAR(500),
    img_logo_url VARCHAR(500),
    synced_at TIMESTAMP
);
```

## Security Features

- **Password Encryption** - BCrypt password hashing
- **CSRF Protection** - Spring Security CSRF tokens
- **Session Management** - Secure session handling (30 minutes timeout)
- **OpenID Authentication** - Secure Steam account linking
- **Input Validation** - Jakarta Validation for all user inputs

## Troubleshooting

### Common Issues

**1. Steam API Key Error**
```
Error: Failed to fetch Steam profile
```
**Solution:** Verify your Steam API key in `application.properties`

**2. OpenID Verification Failed**
```
Error: Steam login verification failed
```
**Solution:** Check that `app.base.url` matches your actual application URL

**3. Port Already in Use**
```
Error: Port 8080 is already in use
```
**Solution:** Change the port in `application.properties`:
```properties
server.port=8081
```

**4. Database Connection Error**
```
Error: Cannot create PoolableConnectionFactory
```
**Solution:** For MySQL, ensure the database exists and credentials are correct

## Future Enhancements

- [ ] Game achievements tracking
- [ ] Friends list integration
- [ ] Game recommendations
- [ ] Wishlist management
- [ ] Playtime charts and analytics
- [ ] Export library to CSV/JSON
- [ ] Email notifications for new games
- [ ] Dark mode support

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Disclaimer

This application is not affiliated with, endorsed by, or sponsored by Valve Corporation or Steam. All trademarks belong to their respective owners.

## Support

For issues and questions:
- Create an issue on GitHub
- Check the [Steam Web API documentation](https://steamcommunity.com/dev)
- Review Spring Boot documentation

## Acknowledgments

- [Steam Web API](https://steamcommunity.com/dev) - Game data provider
- [Spring Boot](https://spring.io/projects/spring-boot) - Framework
- [OpenID4Java](https://github.com/jbufu/openid4java) - OpenID authentication

---

Built with ❤️ using Spring Boot
