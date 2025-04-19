CREATE TABLE IF NOT EXISTS oidc_clients (
    client_id VARCHAR(255) PRIMARY KEY,
    client_secret VARCHAR(255) NOT NULL,
    redirect_uri VARCHAR(255) NOT NULL
    );

INSERT INTO oidc_clients (client_id, client_secret, redirect_uri)
VALUES ('web-client', 'secret', 'http://localhost:8080/oidc-client.html');
