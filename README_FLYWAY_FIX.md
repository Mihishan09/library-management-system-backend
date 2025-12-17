# Fix Failed Flyway Migration

## Problem
The error shows: `Schema 'library_management' contains a failed migration to version 001.20251203003002 !`

## Solution

You need to manually delete the failed migration record from the database. Here are two options:

### Option 1: Using MySQL Workbench (Recommended)
1. Open MySQL Workbench
2. Connect to your MySQL server
3. Run this SQL:

```sql
USE library_management;

-- Delete the failed migration record
DELETE FROM flyway_schema_history 
WHERE version = '001.20251203003002';

-- Verify it's deleted
SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 5;
```

### Option 2: Using MySQL Command Line
If MySQL is in your PATH, run:
```bash
mysql -u root -p20220224@Mn -e "USE library_management; DELETE FROM flyway_schema_history WHERE version = '001.20251203003002';"
```

### Option 3: Use the SQL Script
I've created `fix_failed_migration.sql` in the project root. Run it in MySQL Workbench.

## After Fixing
Once you've deleted the failed migration record, restart the backend:
```bash
cd C:\Users\User\Desktop\library-management-system
mvn spring-boot:run
```

The migration should now run successfully!

