# balance-watch-srv
server side for balance watch

#########################
## MySQL Config
#########################

-- Local install of MySQL Server 8.0.21 Community Edition (CE) for dev
MySQL 8.0 Connector which use libmysqlclient 8.0 support is required for compliance with the new authentication based on SHA256 password methods.

MySQL Root Password: Allo123!

MySQL User Account:
user: springuser
pass: GoGoPass321!

#########################
## NGINX config for http { block directive
#########################

- note: (oct 15 2020)
this works but there are problems reported in the web console at the start: ->

sockjs.js:1684 WebSocket connection to 'ws://localhost:9897/sockjs-node/469/l0vzvebt/websocket' failed: Error during WebSocket handshake: Unexpected response code: 400
WebSocketBrowserDriver @ sockjs.js:1684
WebSocketTransport @ sockjs.js:2959
SockJS._connect @ sockjs.js:829
SockJS._receiveInfo @ sockjs.js:803
g @ sockjs.js:66
EventEmitter.emit @ sockjs.js:86
(anonymous) @ sockjs.js:567
g @ sockjs.js:66
EventEmitter.emit @ sockjs.js:86
(anonymous) @ sockjs.js:374
g @ sockjs.js:66
EventEmitter.emit @ sockjs.js:86
xhr.onreadystatechange @ sockjs.js:1598
wrapFn @ zone-evergreen.js:1191
invokeTask @ zone-evergreen.js:391
runTask @ zone-evergreen.js:168
invokeTask @ zone-evergreen.js:465
invokeTask @ zone-evergreen.js:1603
globalZoneAwareCallback @ zone-evergreen.js:1629
client:169 Invalid Host/Origin header
error @ client:169
(anonymous) @ socket.js:47
sock.onmessage @ SockJSClient.js:67
EventTarget.dispatchEvent @ sockjs.js:170
(anonymous) @ sockjs.js:888
SockJS._transportMessage @ sockjs.js:886
EventEmitter.emit @ sockjs.js:86
(anonymous) @ sockjs.js:2203
EventEmitter.emit @ sockjs.js:86
(anonymous) @ sockjs.js:2148
EventEmitter.emit @ sockjs.js:86
EventSourceReceiver.es.onmessage @ sockjs.js:2251
client:172 

- this is the config

http {
    # default nginx
    ...

    server {
        listen 9897;
        
		location / {
		    proxy_pass http://localhost:4200;
		}
		
        location /mdsui/ {
		    proxy_pass http://localhost:4200;
		}
		
		location /mds/ {
		    proxy_pass http://localhost:9898;
		}
		
    }
    
  ...
  }
  
  #########################
  ## security with KeyCloak 
  #########################
  
  // startup standalone server
  ..\bin\standalone.bat -Djboss.socket.binding.port-offset=100
  http://localhost:8180/auth/admin/
  (palemire/cremePoff666)
  
  (user Mr. Pedro Gonzalo) abc@hotmail.com / kodakkkk
  
  // Token endpoint  - access token with permissions (Requesting Party Token aka RPT)
  
  Token Endpoint
  OAuth2 clients (such as front end applications) can obtain access tokens from the server using the token endpoint and use these same tokens to access resources protected by a resource server (such as back end services). In the same way, Keycloak Authorization Services provide extensions to OAuth2 to allow access tokens to be issued based on the processing of all policies associated with the resource(s) or scope(s) being requested. This means that resource servers can enforce access to their protected resources based on the permissions granted by the server and held by an access token. In Keycloak Authorization Services the access token with permissions is called a Requesting Party Token or RPT for short.
  
  // User Realm created from the Master realm: this is what users have access to
  http://localhost:8080/auth/realms/mds/account/
