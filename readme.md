# Splitwise-Like

Este projeto é uma aplicação estilo Splitwise, que também atua como um **provedor OIDC completo**. Ele foi desenvolvido com Spring Boot e oferece suporte a autenticação com:
- E-mail/senha (formulário)
- Login social via Google (OAuth2)
- Emissão de tokens `id_token` e `access_token` no padrão OpenID Connect
- Revogação de tokens via base de dados

---

## Funcionalidades

### OIDC Provider
- `/authorize`: Inicia o fluxo de autorização
- `/token`: Emite `id_token` e `access_token` (ambos JWT)
- `/userinfo`: Retorna informações do usuário autenticado
- `/.well-known/openid-configuration`: Metadados do provedor
- `/.well-known/jwks.json`: Chave pública para validação de tokens

### Autenticação
- Formulário de login (`/login`)
- Login via Google OAuth2
- Emissão de sessão `JSESSIONID`

### Revogação de Tokens
- Endpoint `POST /revoke`: marca um token como revogado
- Tokens revogados são armazenados em base de dados
- Validação integrada ao fluxo JWT

### Gerador e persistência de tokens (em memória ou banco)
- Os tokens são assinados com chave RSA (RS256)
- Chaves lidas de arquivos `.pem`

---

## Geração das chaves públicas/privadas

Para gerar as chaves utilizadas na assinatura de tokens, execute:

```bash
# Gerar chave privada
openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048

# Extrair chave pública
openssl rsa -pubout -in private.pem -out public.pem
```



## Executando o projeto com Docker

```
docker build -t splitwise-oidc .
docker run -p 8080:8080 splitwise-oidc
```


Configure as variáveis de ambiente ou monte os volumes com as chaves .pem.

## Teste de fluxo OIDC com HTML
O projeto inclui uma página para simular um cliente OIDC:


```
http://localhost:8080/oidc-client.html
```
Com essa página você pode:

Iniciar o login (/authorize)
Obter o código de autorização
Trocar por tokens usando /token

## Como testar o fluxo completo

1. Inicie o projeto (./gradlew bootRun ou via Docker)
2. Acesse http://localhost:8080/oidc-client.html
3. Clique em Iniciar login
4. Faça login com usuário e senha
5. Copie o code da URL de callback
6. Troque o código por tokens usando o botão da página
