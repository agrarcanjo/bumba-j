# Railway Deployment Guide

## Quick Setup (Recommended)

### Step 1: Add PostgreSQL Database

1. In your Railway project, click **"New"** → **"Database"** → **"Add PostgreSQL"**
2. Railway will automatically create these variables:
   - `PGHOST`
   - `PGPORT`
   - `PGDATABASE`
   - `PGUSER`
   - `PGPASSWORD`

### Step 2: Configure Application Variables

In your **application service** (not the database service), add these environment variables:

#### Option A: Using Railway's PostgreSQL Variables (Recommended)

```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_LIQUIBASE_ENABLED=false
JPA_HIBERNATE_DDL_AUTO=update
SPRING_DATASOURCE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}
SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}
```

**Important**: Replace `Postgres` with your actual PostgreSQL service name in Railway. You can find it in the Railway dashboard.

#### Option B: Manual Configuration

If you're using an external PostgreSQL database:

```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_LIQUIBASE_ENABLED=false
JPA_HIBERNATE_DDL_AUTO=update
SPRING_DATASOURCE_URL=jdbc:postgresql://your-host:5432/your-database
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
```

### Step 3: Deploy

Railway will automatically rebuild and deploy your application with the new configuration.

---

## Viewing Logs in Railway

### Access Logs

1. Go to your Railway project
2. Click on your application service
3. Click on the **"Deployments"** tab
4. Click on the latest deployment
5. Logs will appear in real-time

### Enable Debug Logging

Add these environment variables to see more detailed logs:

```bash
# Application logs (your code)
LOG_LEVEL_APP=DEBUG

# Spring Framework logs
LOG_LEVEL_SPRING_WEB=DEBUG
LOG_LEVEL_SPRING_SECURITY=DEBUG

# Database/SQL logs
LOG_LEVEL_HIBERNATE_SQL=DEBUG
LOG_LEVEL_HIBERNATE_PARAMS=TRACE

# Root logger (all logs)
LOG_LEVEL_ROOT=INFO
```

**Recommended for troubleshooting:**

```bash
LOG_LEVEL_APP=DEBUG
LOG_LEVEL_SPRING_WEB=INFO
LOG_LEVEL_HIBERNATE_SQL=DEBUG
```

**Warning**: `DEBUG` and `TRACE` levels generate a lot of logs. Use them only when troubleshooting, then set back to `INFO` for normal operation.

---

## Configuration Options Explained

### Database Schema Management

Choose **ONE** of these approaches:

#### 1. Hibernate Auto-Update (Simplest - Recommended for Development)

```bash
SPRING_LIQUIBASE_ENABLED=false
JPA_HIBERNATE_DDL_AUTO=update
```

✅ Hibernate automatically creates/updates tables
❌ Not recommended for production (can cause data loss)

#### 2. Liquibase Migrations (Recommended for Production)

```bash
SPRING_LIQUIBASE_ENABLED=true
JPA_HIBERNATE_DDL_AUTO=none
```

✅ Full control over database changes
✅ Safe for production
❌ Requires valid Liquibase changelogs

#### 3. Manual Schema Management

```bash
SPRING_LIQUIBASE_ENABLED=false
JPA_HIBERNATE_DDL_AUTO=none
```

✅ You manage the database schema manually
❌ Requires manual SQL execution

---

## Troubleshooting

### Error: "URL must start with 'jdbc'"

**Problem**: The `SPRING_DATASOURCE_URL` variable is not set or incorrectly formatted.

**Solution**:

1. Check that you're using the correct service reference syntax in Railway
2. Verify the variable is set in your **application service**, not the database service
3. Make sure the URL starts with `jdbc:postgresql://`

Example of correct format:

```
jdbc:postgresql://postgres.railway.internal:5432/railway
```

### Error: "Connection refused"

**Problem**: The application can't connect to the database.

**Solutions**:

1. Verify the PostgreSQL service is running
2. Check that you're referencing the correct service name (e.g., `${{Postgres.PGHOST}}`)
3. Ensure both services are in the same Railway project
4. Try using the internal Railway hostname: `postgres.railway.internal`

### Error: Liquibase changelog errors

**Problem**: Liquibase can't find or execute changelogs.

**Solution**: Disable Liquibase and use Hibernate auto-update:

```bash
SPRING_LIQUIBASE_ENABLED=false
JPA_HIBERNATE_DDL_AUTO=update
```

### Logs not showing or incomplete

**Problem**: You can't see detailed logs in Railway.

**Solutions**:

1. Enable debug logging with environment variables (see "Enable Debug Logging" above)
2. Check the "Deployments" tab, not "Observability" (which requires paid plan)
3. Make sure your application is actually running (check deployment status)
4. Add this to see startup logs:
   ```bash
   LOG_LEVEL_ROOT=DEBUG
   ```

### How to find your PostgreSQL service name in Railway

1. Go to your Railway project
2. Click on your PostgreSQL service
3. The service name is shown at the top (e.g., "Postgres", "PostgreSQL", "database")
4. Use this name in your variable references: `${{ServiceName.PGHOST}}`

---

## Complete Variable Reference

### Required Variables

```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://host:port/database
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password
```

### Optional Variables - Database

```bash
SPRING_LIQUIBASE_ENABLED=false          # true or false
JPA_HIBERNATE_DDL_AUTO=update           # none, validate, update, create, create-drop
SERVER_PORT=8080                        # Railway sets this automatically
```

### Optional Variables - Logging

```bash
LOG_LEVEL_ROOT=INFO                     # ERROR, WARN, INFO, DEBUG, TRACE
LOG_LEVEL_APP=INFO                      # Your application logs
LOG_LEVEL_JHIPSTER=INFO                 # JHipster framework logs
LOG_LEVEL_SPRING_WEB=INFO               # Spring Web logs
LOG_LEVEL_SPRING_SECURITY=INFO          # Spring Security logs
LOG_LEVEL_HIBERNATE_SQL=INFO            # SQL queries (use DEBUG to see queries)
LOG_LEVEL_HIBERNATE_PARAMS=INFO         # SQL parameters (use TRACE to see values)
```

### Alternative Variable Names (Supported)

```bash
DATABASE_URL=jdbc:postgresql://...      # Alternative to SPRING_DATASOURCE_URL
DATABASE_USERNAME=username              # Alternative to SPRING_DATASOURCE_USERNAME
DATABASE_PASSWORD=password              # Alternative to SPRING_DATASOURCE_PASSWORD
```

---

## Example: Complete Railway Configuration

If your PostgreSQL service is named "Postgres":

```bash
# Application Profile
SPRING_PROFILES_ACTIVE=prod

# Database Connection (using Railway's PostgreSQL)
SPRING_DATASOURCE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}
SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}

# Schema Management (choose one approach)
SPRING_LIQUIBASE_ENABLED=false
JPA_HIBERNATE_DDL_AUTO=update

# Logging (optional - for debugging)
LOG_LEVEL_APP=DEBUG
LOG_LEVEL_HIBERNATE_SQL=DEBUG
```

Save these variables in your Railway application service, and deploy!

---

## Quick Commands for Railway CLI (Optional)

If you have Railway CLI installed:

```bash
# View logs in real-time
railway logs

# View logs with follow
railway logs --follow

# Deploy from command line
railway up

# Open Railway dashboard
railway open
```
