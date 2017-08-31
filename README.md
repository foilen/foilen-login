# About

This is a Single Sign-On login service. Its API is available in *com.foilen:foilen-login-api*.

# Usage

## Configuration file

You need to create a json configuration file that maps the object LoginConfig.

Here is an example of the content:

```json
{
  "mysqlHostName" : "127.0.0.1",
  "mysqlPort" : 3306,
  "mysqlDatabaseName" : "foilen_login",
  "mysqlDatabaseUserName" : "foilen_login",
  "mysqlDatabasePassword" : "fjGu38d!f",
  
  "cookieUserName" : "fl_user_id",
  "cookieDateName" : "fl_date",
  "cookieSignatureName" : "fl_signature",
  "cookieSignatureSalt" : "mas6tA1/Gw",

  "csrfSalt" : "LmTbI7TDhg",

  "applicationId" : "7vND73OyFA",

  "fromEmail" : "login@example.com",
  "administratorEmail" : "admin@example.com",

  "loginBaseUrl" : "http://login.example.com"
}
```

You can then specify the full path of that file as the *configFile* argument when launching the app or as the
*CONFIG_FILE* environment variable.

## Launch the application for testing on machine

```bash

# Create database and user
mysql -u root -p << _EOF
CREATE DATABASE foilen_login;
GRANT ALL ON foilen_login.* TO foilen_login@localhost IDENTIFIED BY 'fjGu38d!f';
_EOF

# Prepare the config file
cat > /tmp/foilen_config.json << _EOF
{
  "mysqlHostName" : "127.0.0.1",
  "mysqlPort" : 3306,
  "mysqlDatabaseName" : "foilen_login",
  "mysqlDatabaseUserName" : "foilen_login",
  "mysqlDatabasePassword" : "fjGu38d!f",
  
  "cookieUserName" : "fl_user_id",
  "cookieDateName" : "fl_date",
  "cookieSignatureName" : "fl_signature",
  "cookieSignatureSalt" : "mas6tA1/Gw",

  "csrfSalt" : "LmTbI7TDhg",

  "applicationId" : "7vND73OyFA",

  "fromEmail" : "login@example.com",
  "administratorEmail" : "admin@example.com",

  "loginBaseUrl" : "http://login.example.com"
}
_EOF

# Compile
./gradlew build

# Run
java -jar build/libs/foilen-login-master-SNAPSHOT.jar --configFile /tmp/foilen_config.json

# Open your browser on http://localhost:14010/

# Check the database tables
mysql -u root -p foilen_login << _EOF
SHOW TABLES;
_EOF


```

## Supported environment parameters (for Docker)

* `CONFIG_FILE`: The full path to the JSON configuration file
* `MYSQL_PORT_3306_TCP_ADDR`: Overrides the *mysqlHostName* when linked a MariaDB or MySQL server with the *mysql* name
* `MYSQL_PORT_3306_TCP_PORT`: Overrides the *mysqlPort* when linked a MariaDB or MySQL server with the *mysql* name

## Launch the application for testing in Docker (locally)


```bash

# Compile and create image
./create-local-release.sh

# Prepare the config file
cat > /tmp/foilen_config.json << _EOF
{
  "mysqlDatabaseName" : "foilen_login",
  "mysqlDatabaseUserName" : "foilen_login",
  "mysqlDatabasePassword" : "fjGu38d!f",
  
  "cookieUserName" : "fl_user_id",
  "cookieDateName" : "fl_date",
  "cookieSignatureName" : "fl_signature",
  "cookieSignatureSalt" : "mas6tA1/Gw",

  "csrfSalt" : "LmTbI7TDhg",

  "applicationId" : "7vND73OyFA",

  "fromEmail" : "login@example.com",
  "administratorEmail" : "admin@example.com",

  "loginBaseUrl" : "http://login.example.com"
}
_EOF

# Run mariadb
MYSQL_ROOT_PASS=qwerty
docker run \
  --rm \
  --name foilen-login_mariadb \
  --env MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASS \
  --detach \
  mariadb:10.2.8

# Create database and user
sleep 20s
docker run -i \
  --link foilen-login_mariadb:mysql \
  --rm \
  mariadb:10.2.8 \
  sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"' << _EOF
CREATE DATABASE foilen_login;
GRANT ALL ON foilen_login.* TO 'foilen_login'@'172.17.0.%' IDENTIFIED BY 'fjGu38d!f';
_EOF

# Run login service
docker run \
   --rm \
  --link foilen-login_mariadb:mysql \
  --volume /tmp/foilen_config.json:/foilen_config.json \
  --env CONFIG_FILE=/foilen_config.json \
  --name foilen-login_webapp \
  --detach \
  foilen-login:master-SNAPSHOT
docker logs -f foilen-login_webapp

URL=$(docker inspect foilen-login_webapp | grep '"IPAddress"' | head -n 1 | cut -d '"' -f 4)
echo Open your browser on http://$URL:14010/

# Stop everything
docker stop foilen-login_webapp foilen-login_mariadb

```

## Launch the application for testing in Docker (from the Hub)


```bash

# Prepare the config file
cat > /tmp/foilen_config.json << _EOF
{
  "mysqlDatabaseName" : "foilen_login",
  "mysqlDatabaseUserName" : "foilen_login",
  "mysqlDatabasePassword" : "fjGu38d!f",
  
  "cookieUserName" : "fl_user_id",
  "cookieDateName" : "fl_date",
  "cookieSignatureName" : "fl_signature",
  "cookieSignatureSalt" : "mas6tA1/Gw",

  "csrfSalt" : "LmTbI7TDhg",

  "applicationId" : "7vND73OyFA",

  "fromEmail" : "login@example.com",
  "administratorEmail" : "admin@example.com",

  "loginBaseUrl" : "http://login.example.com"
}
_EOF

# Run mariadb
MYSQL_ROOT_PASS=qwerty
docker run \
  --rm \
  --name foilen-login_mariadb \
  --env MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASS \
  --detach \
  mariadb:10.2.8

# Create database and user
sleep 20s
docker run -i \
  --link foilen-login_mariadb:mysql \
  --rm \
  mariadb:10.2.8 \
  sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"' << _EOF
CREATE DATABASE foilen_login;
GRANT ALL ON foilen_login.* TO 'foilen_login'@'172.17.0.%' IDENTIFIED BY 'fjGu38d!f';
_EOF

# Run login service
docker run \
   --rm \
  --link foilen-login_mariadb:mysql \
  --volume /tmp/foilen_config.json:/foilen_config.json \
  --env CONFIG_FILE=/foilen_config.json \
  --name foilen-login_webapp \
  --detach \
  foilen/foilen-login
docker logs -f foilen-login_webapp

IP=$(docker inspect foilen-login_webapp | grep '"IPAddress"' | head -n 1 | cut -d '"' -f 4)
echo Open your browser on http://$IP:14010/

# Stop everything
docker stop foilen-login_webapp foilen-login_mariadb

```
