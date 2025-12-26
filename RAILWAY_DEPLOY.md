# Railway Deployment Configuration

## Quick Setup (Simplest Way)

### 1. Add PostgreSQL Service in Railway

- In your Railway project, click "New" → "Database" → "Add PostgreSQL"
- Railway will automatically create the database and set environment variables

### 2. Configure Application Environment Variables

In your Railway service settings, add these variables:

```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_LIQUIBASE_ENABLED=false
JPA_HIBERNATE_DDL_AUTO=update
```

### 3. Configure Database Connection

Railway provides PostgreSQL variables automatically. Add these derived variables:

```bash
DATABASE_URL=jdbc:postgresql://${{PGHOST}}:${{PGPORT}}/${{PGDATABASE}}
DATABASE_USERNAME=${{PGUSER}}
DATABASE_PASSWORD=${{PGPASSWORD}}
```

**Note**: In Railway, use `${{VARIABLE_NAME}}` syntax to reference other environment variables.

---

## Alternative: Using Liquibase (More Control)

If you want to use Liquibase for database migrations:

```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_LIQUIBASE_ENABLED=true
JPA_HIBERNATE_DDL_AUTO=none
```

This will run Liquibase migrations on startup. Make sure your Liquibase changelogs are properly configured.

---

## Environment Variables Reference

### Required

- `SPRING_PROFILES_ACTIVE`: Set to `prod`
- `DATABASE_URL`: PostgreSQL JDBC URL
- `DATABASE_USERNAME`: PostgreSQL username
- `DATABASE_PASSWORD`: PostgreSQL password

### Optional

- `SPRING_LIQUIBASE_ENABLED`: `true` or `false` (default: `true`)
- `JPA_HIBERNATE_DDL_AUTO`: `none`, `validate`, `update`, `create`, `create-drop` (default: `none`)
- `SERVER_PORT`: Port number (default: `8080`, Railway sets this automatically)

---

## Troubleshooting

### "Connection refused" error

- Verify DATABASE_URL is correctly formatted
- Check that PostgreSQL service is running in Railway
- Ensure database variables are properly referenced

### Liquibase errors

- Set `SPRING_LIQUIBASE_ENABLED=false` to disable Liquibase
- Or ensure your Liquibase changelogs are valid

### Port binding issues

- Railway automatically sets PORT variable
- Application uses port 8080 by default
- No action needed unless you have custom port requirements
