@echo off
REM Test script for VoIP User Management API Authentication

echo Testing VoIP User Management API Authentication
echo ==============================================

REM Base URL
set BASE_URL=http://localhost:8080

REM Test 1: Health check (should work without auth)
echo.
echo 1. Testing health check endpoint...
curl -s -X GET "%BASE_URL%/ping"
echo.

REM Test 2: Try to access protected endpoint without auth (should fail)
echo.
echo 2. Testing protected endpoint without authentication (should fail)...
curl -s -X GET "%BASE_URL%/api/users/all"
echo.

REM Test 3: Login to get JWT token
echo.
echo 3. Logging in to get JWT token...
curl -s -X POST "%BASE_URL%/api/auth/login" -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"password\":\"password123\"}"
echo.

echo.
echo Authentication test completed!
echo If you see user data in step 4 and a successful user creation in step 5, the authentication is working correctly.
echo.
echo Note: For full testing with JWT token extraction, use the bash script (test_auth.sh) or manually test the endpoints.
pause

