#!/bin/bash

# Test script for VoIP User Management API Authentication

echo "Testing VoIP User Management API Authentication"
echo "=============================================="

# Base URL
BASE_URL="http://localhost:8080"

# Test 1: Health check (should work without auth)
echo -e "\n1. Testing health check endpoint..."
curl -s -X GET "$BASE_URL/ping"
echo -e "\n"

# Test 2: Try to access protected endpoint without auth (should fail)
echo -e "\n2. Testing protected endpoint without authentication (should fail)..."
curl -s -X GET "$BASE_URL/api/users/all"
echo -e "\n"

# Test 3: Login to get JWT token
echo -e "\n3. Logging in to get JWT token..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}')

echo "Login response: $LOGIN_RESPONSE"

# Extract token from response (simple extraction - in production use proper JSON parsing)
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo "Failed to extract token from login response"
    exit 1
fi

echo "JWT Token: $TOKEN"

# Test 4: Access protected endpoint with valid token
echo -e "\n4. Testing protected endpoint with valid JWT token..."
curl -s -X GET "$BASE_URL/api/users/all" \
  -H "Authorization: Bearer $TOKEN"
echo -e "\n"

# Test 5: Create a new user with valid token
echo -e "\n5. Creating a new user with valid JWT token..."
CREATE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/users/create" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"newpass123","name":"New User","extension":"1002"}')

echo "Create user response: $CREATE_RESPONSE"

echo -e "\nAuthentication test completed!"
echo "If you see user data in step 4 and a successful user creation in step 5, the authentication is working correctly."

