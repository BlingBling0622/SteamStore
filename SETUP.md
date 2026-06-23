# Setup Guide - Steam Library Integration App

This guide will walk you through setting up and running the Steam Library Integration application.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
3. [Configuration](#configuration)
4. [Running the Application](#running-the-application)
5. [First Time Setup](#first-time-setup)
6. [Troubleshooting](#troubleshooting)

## Prerequisites

### Required Software

#### 1. Java Development Kit (JDK) 17 or higher

**Check if Java is installed:**
```bash
java -version
```

**Download Java:**
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- [OpenJDK](https://openjdk.org/)
- [Amazon Corretto](https://aws.amazon.com/corretto/)

**Installation:**
- **Windows:** Download installer and run it
- **macOS:** `brew install openjdk@17`
- **Linux:** `sudo apt install openjdk-17-jdk` (Ubuntu/Debian)

#### 2. Apache Maven 3.6 or higher

**Check if Maven is installed:**
```bash
mvn -version
```

**Download Maven:**
- [Official Maven Download](https://maven.apache.org/download.cgi)

**Installation:**
- **Windows:** Download zip, extract, and add to PATH
- **macOS:** `brew install maven`
- **Linux:** `sudo apt install maven` (Ubuntu/Debian)

#### 3. Steam API Key

**Get your Steam API Key:**
1. Visit https://steamcommunity.com/dev/apikey
2. Sign in with your Steam account
3. Enter a domain name (use `localhost` for development)
4. Copy your API key

## Installation

### Step 1: Navigate to Project Directory

```bash
cd E:\Project\steam-library-java
```

### Step 2: Verify Project Structure

Ensure you have the following structure:
```
steam-library-java/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── README.md
└── start.bat (or start.sh)
```

## Configuration

### Step 1: Configure Steam API Key

**Option A: Direct Configuration (Quick)**

Open `src/main/resources/application.properties` and replace:
```properties
steam.api.key=YOUR_STEAM_API_KEY_HERE
```

With your actual API key:
```properties
steam.api.key=ABCDEF1234567890ABCDEF1234567890
```

**Option B: Local Configuration (Recommended)**

1. Copy the template:
```bash
cp src/main/resources/application-local.properties.template src/main/resources/application-local.properties
```

2. Edit `application-local.properties` and add your Steam API key

3. Add this to `application.properties`:
```properties
spring.profiles.include=local
```

### Step 2: Database Configuration (Optional)

**Development Mode (Default):**
The application uses H2 in-memory database. No additional setup needed.

**Production Mode (MySQL):**
If you want to use MySQL:

1. Create a MySQL database:
```sql
CREATE DATABASE steam_library;
```

2. Update `application.properties`:
```properties
# Comment out H2
#spring.datasource.url=jdbc:h2:mem:steamlibdb

# Add MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/steam_library
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### Step 3: Configure Application URL (Optional)

If deploying to production, update:
```properties
app.base.url=https://yourdomain.com
```

For local development, keep the default:
```properties
app.base.url=http://localhost:8080
```

## Running the Application

### Method 1: Using Startup Scripts (Easiest)

**Windows:**
```bash
start.bat
```

**Linux/macOS:**
```bash
./start.sh
```

### Method 2: Using Maven

**Build the project:**
```bash
mvn clean package
```

**Run the application:**
```bash
mvn spring-boot:run
```

### Method 3: Using JAR File

**Build the JAR:**
```bash
mvn clean package
```

**Run the JAR:**
```bash
java -jar target/steam-library-app-1.0.0.jar
```

### Verify Application Started

You should see output like:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

...
Tomcat started on port(s): 8080 (http)
Started SteamLibraryApplication in X.XXX seconds
```

### Access the Application

Open your web browser and go to:
```
http://localhost:8080
```

## First Time Setup

### 1. Create an Account

1. Click **"Get Started"** or **"Register"**
2. Fill in the form:
   - Username (3-20 characters)
   - Email address
   - Password (minimum 6 characters)
   - Confirm password
3. Click **"Create Account"**

### 2. Login

1. After registration, you'll be redirected to login
2. Enter your username and password
3. Click **"Sign In"**

### 3. Link Your Steam Account

1. On the dashboard, click **"Link Steam Account"**
2. You'll be redirected to Steam's website
3. Sign in with your Steam credentials
4. Click **"Allow"** to authorize the application
5. You'll be redirected back to the dashboard

### 4. View Your Library

Once linked, your dashboard will display:
- Total number of games
- Total playtime
- Steam profile information
- Complete game library with images and playtime

### 5. Sync Your Library

Click **"Sync Library"** to refresh your game collection from Steam.

## Troubleshooting

### Issue: Application Won't Start

**Error: Port 8080 already in use**
```
Solution: Change the port in application.properties:
server.port=8081
```

**Error: Could not find or load main class**
```
Solution: Clean and rebuild:
mvn clean package
```

### Issue: Steam Login Fails

**Error: Steam login verification failed**
```
Solution 1: Verify your Steam API key is correct
Solution 2: Check that app.base.url matches your actual URL
Solution 3: Make sure you're using HTTPS in production
```

### Issue: No Games Showing

**Problem: Library is empty after linking**
```
Solution 1: Click "Sync Library" button
Solution 2: Check Steam API key is valid
Solution 3: Verify your Steam profile is public
```

To make your Steam profile public:
1. Go to https://steamcommunity.com/
2. Click your username → Edit Profile
3. Privacy Settings → My Profile → Public
4. Game Details → Public

### Issue: Database Errors

**Error: Table doesn't exist**
```
Solution: Set this in application.properties:
spring.jpa.hibernate.ddl-auto=update
```

**Error: MySQL connection failed**
```
Solution: Verify MySQL is running and credentials are correct
```

### Issue: Maven Build Fails

**Error: Dependencies not downloaded**
```
Solution: Clear Maven cache and rebuild:
mvn clean install -U
```

### Issue: Steam Images Not Loading

**Problem: Game images show broken**
```
Solution: This is usually due to Steam CDN. The images should load after a few seconds.
If persistent, check your internet connection.
```

## Development Tips

### Hot Reload with Spring DevTools

Spring DevTools is already included. Changes to Java files will trigger automatic restart.

### Accessing H2 Console

When using H2 database, access the console at:
```
http://localhost:8080/h2-console
```

**Connection settings:**
- JDBC URL: `jdbc:h2:mem:steamlibdb`
- Username: `sa`
- Password: (leave empty)

### Logging

View detailed logs by setting in `application.properties`:
```properties
logging.level.com.steamlibrary=DEBUG
```

### Testing the Application

Run tests:
```bash
mvn test
```

## Next Steps

Once everything is running:

1. ✅ Create your account
2. ✅ Link your Steam account
3. ✅ Explore your game library
4. ✅ Check your playtime statistics

## Getting Help

If you encounter issues not covered here:

1. Check the main [README.md](README.md)
2. Review Spring Boot documentation
3. Check Steam Web API documentation
4. Create an issue on GitHub

## Security Notes

⚠️ **Important Security Reminders:**

- Never commit your Steam API key to version control
- Use environment variables for production
- Keep your Steam credentials secure
- The application never stores your Steam password
- Only link Steam accounts you own

---

Happy gaming! 🎮
