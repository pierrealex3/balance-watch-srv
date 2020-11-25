#!/bin/bash

MY_ROOT=$(pwd)
NGINX_ROOT=/d/nginx/nginx-1.18.0
KEYCLOAK_ROOT=/d/keycloak/keycloak-11.0.3

cd "$NGINX_ROOT"
exec ./nginx.exe > "$MY_ROOT/nginx.log" &

cd "$KEYCLOAK_ROOT"
exec ./bin/standalone.bat -Djboss.socket.binding.port-offset=100 > "$MY_ROOT/keycloak.log" &





