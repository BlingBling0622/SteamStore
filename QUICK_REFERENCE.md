# Quick Reference Guide

## 🚀 Quick Start

```bash
# 1. Get your Steam API Key
Visit: https://steamcommunity.com/dev/apikey

# 2. Configure API Key
Edit: src/main/resources/application.properties
Replace: YOUR_STEAM_API_KEY_HERE with your actual key

# 3. Run the application
Windows: start.bat
Linux/Mac: ./start.sh

# 4. Access the app
Open: http://localhost:8080
```

## 📁 Project Structure

```
steam-library-java/
├── src/main/java/com/steamlibrary/
│   ├── controller/          # Web controllers
│   ├── service/             # Business logic
│   ├── repository/          # Database access
│   ├── model/              # JPA entities
│   ├── dto/                # Data transfer objects
│   └── config/             # Spring configuration
├── src/main/resources/
│   ├── templates/          # HTML templates
│   └── application.properties
└── pom.xml                 # Maven dependencies
```

## 🔑 Key Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Home page |
| `/register` | GET/POST | User registration |
| `/login` | GET/POST | User login |
| `/dashboard` | GET | User dashboard (auth required) |
| `/steam/link` | GET | Link Steam account |
| `/steam/callback` | GET | Steam OAuth callback |
| `/steam/sync` | GET | Sync game library |

## 💾 Database Tables

### users
- id, username, password, email, created_at, updated_at

### steam_accounts  
- id, steam_id, persona_name, profile_url, avatar_url, linked_at, last_synced, user_id

### steam_games
- id, app_id, steam_id, game_name, playtime_forever, playtime_2weeks, img_icon_url, img_logo_url, synced_at

## ⚙️ Configuration

### Required Settings
```properties
steam.api.key=YOUR_API_KEY
```

### Optional Settings
```properties
server.port=8080
app.base.url=http://localhost:8080
spring.datasource.url=jdbc:h2:mem:steamlibdb  # or MySQL
```

## 🔧 Maven Commands

```bash
# Compile
mvn compile

# Run tests
mvn test

# Package JAR
mvn package

# Run application
mvn spring-boot:run

# Clean build
mvn clean install
```

## 🎮 Steam API Integration

### APIs Used
1. **GetPlayerSummaries** - User profile info
2. **GetOwnedGames** - Game library
3. **OpenID** - Authentication

### API Key Requirements
- Free from Steam
- No rate limit for reasonable use
- Required for all API calls

## 🔒 Security Features

- ✅ BCrypt password hashing
- ✅ Spring Security authentication
- ✅ CSRF protection
- ✅ Session management (30 min)
- ✅ OpenID authentication
- ✅ Input validation

## 📊 Features Checklist

- [x] User registration & login
- [x] Steam OAuth integration
- [x] Profile display
- [x] Game library sync
- [x] Playtime statistics
- [x] Responsive design
- [x] Session management

## 🐛 Common Issues & Solutions

### Port Already in Use
```properties
# Change port in application.properties
server.port=8081
```

### Steam API Key Error
```
Check: application.properties has correct API key
Get key from: https://steamcommunity.com/dev/apikey
```

### No Games Showing
```
1. Click "Sync Library" button
2. Verify Steam profile is public
3. Check Steam API key is valid
```

### Build Fails
```bash
# Clear cache and rebuild
mvn clean install -U
```

## 📝 User Flow

```
1. Register → Create account
2. Login → Enter credentials
3. Dashboard → See welcome message
4. Link Steam → Authorize with Steam
5. View Library → See all your games
6. Sync → Update game list
```

## 🌐 Steam Profile Settings

For the app to access your library:
1. Go to Steam Privacy Settings
2. Set "My Profile" to **Public**
3. Set "Game Details" to **Public**

## 📦 Dependencies

**Key Libraries:**
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- Thymeleaf
- OpenID4Java
- H2 Database
- MySQL Connector
- Lombok
- Gson

## 🎨 Design System

**Colors:**
- Background: `#F6F1E8` (warm paper)
- Primary: `#C8853F` (amber)
- Text: `#1F2421` (dark)
- Muted: `#8A8A80` (gray)
- Dark block: `#2A2723` (charcoal)

**Fonts:**
- Headings: DM Serif Display
- Body: Inter

## 🚦 Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 302 | Redirect (normal) |
| 401 | Unauthorized (not logged in) |
| 404 | Page not found |
| 500 | Server error |

## 💡 Tips

1. **Development:** Use H2 database (default)
2. **Production:** Switch to MySQL
3. **Testing:** Use H2 console at `/h2-console`
4. **Debugging:** Enable debug logs in application.properties
5. **Hot Reload:** Spring DevTools included

## 📞 Support Resources

- [README.md](README.md) - Full documentation
- [SETUP.md](SETUP.md) - Detailed setup guide  
- [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - Technical overview
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Steam Web API](https://steamcommunity.com/dev)

## ⏱️ Estimated Times

- **Setup:** 10-15 minutes
- **First build:** 2-5 minutes
- **Startup:** 10-20 seconds
- **Steam linking:** 30 seconds

## 🎯 Testing Checklist

- [ ] Application starts successfully
- [ ] Home page loads
- [ ] User registration works
- [ ] User login works
- [ ] Dashboard displays
- [ ] Steam linking works
- [ ] Games display in library
- [ ] Sync updates games
- [ ] Logout works

---

**Need Help?** Check SETUP.md for detailed troubleshooting!
