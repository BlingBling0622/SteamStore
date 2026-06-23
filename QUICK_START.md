# 🚀 Quick Start Guide - Steam Library Integration App

## ✅ Application is Ready to Use!

Your application has been **verified and is fully functional**. Follow these simple steps to run it.

---

## 📋 What You Have

✅ **All code files** - Complete and working  
✅ **All templates** - Beautiful UI ready  
✅ **Database setup** - H2 configured  
✅ **Security** - Spring Security with BCrypt  
✅ **Build scripts** - Automated startup  
✅ **Documentation** - README.md, STATUS.md  

---

## 🎯 Two Ways to Start

### Method 1: Simple Start (Recommended)

**For Windows:**
```bash
start.bat
```

**For Linux/Mac/Git Bash:**
```bash
./start.sh
```

This will:
- Check Java and Maven
- Build the project
- Start the application
- Show you the URL to access

### Method 2: Background Mode

**Start in background:**
```bash
# Windows
nohup java -jar target\steam-library-app-1.0.0.jar > application.log 2>&1 &

# Linux/Mac
nohup java -jar target/steam-library-app-1.0.0.jar > application.log 2>&1 &
```

**Stop the application:**
```bash
# Windows
stop.bat

# Linux/Mac
./stop.sh
```

---

## 🌐 Access Your Application

Once started, open your browser:

| Page | URL | Description |
|------|-----|-------------|
| **Home** | http://localhost:8080 | Landing page with features |
| **Register** | http://localhost:8080/register | Create new account |
| **Login** | http://localhost:8080/login | Sign in |
| **Dashboard** | http://localhost:8080/dashboard | User dashboard (requires login) |
| **H2 Console** | http://localhost:8080/h2-console | Database viewer |

### H2 Database Console Access:
```
JDBC URL:  jdbc:h2:mem:steamlibdb
Username:  sa
Password:  (leave empty)
```

---

## 🎮 How to Use the Application

### 1️⃣ Create an Account
1. Go to http://localhost:8080/register
2. Enter username, email, and password
3. Click "Create Account"

### 2️⃣ Login
1. Go to http://localhost:8080/login
2. Enter your credentials
3. You'll be redirected to dashboard

### 3️⃣ Link Steam Account (Optional)
1. On dashboard, click "Link Steam Account"
2. You'll be redirected to Steam
3. Login with Steam credentials
4. Your library will sync automatically

**⚠️ Note:** Steam integration requires an API key (see Configuration below)

---

## ⚙️ Configuration

### 🔑 Steam API Key (Required for Steam Features)

To enable Steam integration:

1. **Get API Key:**
   - Visit: https://steamcommunity.com/dev/apikey
   - Sign in with your Steam account
   - Enter domain: `localhost` (for development)
   - Copy the generated key

2. **Update Configuration:**
   - Open: `src/main/resources/application.properties`
   - Find: `steam.api.key=YOUR_STEAM_API_KEY_HERE`
   - Replace with your actual key
   - Restart the application

### 🔄 Without Steam API Key

The app still works! You can:
- ✅ Register and login
- ✅ Access dashboard
- ✅ Use H2 console
- ❌ Cannot link Steam account
- ❌ Cannot sync game library

---

## 🛠️ Troubleshooting

### Problem: Port 8080 already in use
```
Error: Web server failed to start. Port 8080 was already in use.
```

**Solution:**
```bash
# Find and kill the process
netstat -ano | grep :8080
taskkill //PID <PID_NUMBER> //F

# Or run stop.bat
stop.bat
```

### Problem: Build fails
```
Error: BUILD FAILURE
```

**Solution:**
```bash
# Clean and rebuild
mvn clean install

# Or delete target folder and rebuild
rm -rf target
mvn clean package
```

### Problem: Can't access the application
```
curl: (7) Failed to connect to localhost port 8080
```

**Solution:**
1. Check if application is running:
   ```bash
   netstat -ano | grep :8080
   ```
2. Check logs:
   ```bash
   tail -50 application.log
   ```
3. Restart:
   ```bash
   start.bat
   ```

### Problem: Steam API errors
```
Error: Failed to fetch Steam profile
```

**Solution:**
1. Verify API key in `application.properties`
2. Ensure your Steam profile is **public**
3. Check Steam API status at https://steamstat.us/

---

## 📁 Project Files

```
steam-library-java/
├── start.bat              # ⭐ Start script (Windows)
├── start.sh               # ⭐ Start script (Linux/Mac)
├── stop.bat               # Stop script (Windows)
├── stop.sh                # Stop script (Linux/Mac)
├── QUICK_START.md         # This file
├── README.md              # Full documentation
├── STATUS.md              # Current status report
├── application.log        # Runtime logs (created when running)
├── pom.xml                # Maven configuration
└── src/                   # Source code
    ├── main/
    │   ├── java/          # Java source files
    │   └── resources/
    │       ├── templates/ # HTML pages
    │       └── application.properties  # Configuration
    └── test/              # Test files
```

---

## 🎯 Quick Test Checklist

After starting the application, verify everything works:

- [ ] Home page loads at http://localhost:8080
- [ ] Can access registration page
- [ ] Can create a new account
- [ ] Can login with created account
- [ ] Dashboard shows after login
- [ ] Can logout
- [ ] H2 console is accessible

---

## 🚀 Next Steps

1. **Basic Usage:**
   - Register a test account
   - Login and explore dashboard
   - Check H2 console to see data

2. **Enable Steam Integration:**
   - Get Steam API key
   - Update application.properties
   - Restart application
   - Link your Steam account

3. **Customize:**
   - Modify templates in `src/main/resources/templates/`
   - Update styling (inline CSS in HTML files)
   - Add new features

4. **Production:**
   - Switch to MySQL (see README.md)
   - Set up environment variables
   - Configure proper domain
   - Enable HTTPS

---

## 📞 Need Help?

1. **Check logs:** `tail -f application.log`
2. **Read full docs:** `README.md`
3. **Check status:** `STATUS.md`
4. **Steam API docs:** https://steamcommunity.com/dev
5. **Spring Boot docs:** https://spring.io/projects/spring-boot

---

## ✨ Features Summary

| Feature | Status | Notes |
|---------|--------|-------|
| User Registration | ✅ Working | BCrypt password encryption |
| User Login | ✅ Working | Spring Security |
| Dashboard | ✅ Working | Shows user info |
| Steam OpenID | ⚠️ Needs API key | Get from Steam |
| Library Sync | ⚠️ Needs API key | Fetches game data |
| H2 Database | ✅ Working | In-memory (dev) |
| Beautiful UI | ✅ Working | Warm, modern design |
| Security | ✅ Working | CSRF, sessions, auth |

---

## 🎉 You're All Set!

Your application is **fully functional** and ready to use. Start it with:

```bash
start.bat    # or start.sh
```

Then open: **http://localhost:8080**

Enjoy! 🚀
