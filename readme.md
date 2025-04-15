
# **HubSpot Integration API - Spring Boot**

## 📌 **Descrição**
Esta API fornece endpoints para **autenticação OAuth** e **gerenciamento de contatos** via **HubSpot**, incluindo:
- **Autorização OAuth**
- **Troca de código por token**
- **Criação de contatos**
- **Processamento de Webhooks para criação de contatos**

## 🚀 **Como Usar**

### 🔹 **1️⃣ Autenticação OAuth**
#### ➤ **Gerar URL de Autorização**
**Endpoint: GET /authorization/url**     
**Parâmetros:**    
| Nome         | Tipo   | Obrigatório | Descrição |
|-------------|--------|------------|------------|
| `clientId`  | `String` | ✅ | ID do cliente fornecido pelo HubSpot |
| `redirectUri` | `String` | ✅ | URL de redirecionamento após a autenticação |
| `scope` | `String` | ✅ | Permissões de acesso separadas por espaço |

**Exemplo de requisição:**


GET http://localhost:8080/authorization/url?clientId=CLIENT_ID&redirectUri=REDIRECT_URI&scope=crm.objects.contacts.write%20oauth

**Resposta:**
```json
{
  "statusCode": 200,
  "url": "https://app.hubspot.com/oauth/authorize?client_id=CLIENT_ID&redirect_uri=REDIRECT_URI&scope=crm.objects.contacts.write%20oauth"
}
```
#### ➤ **Trocar Código de Autorização por Token Endpoint**
**Endpoint: GET /authorization/token**

**Parâmetros:**  
| Nome         | Tipo   | Obrigatório | Descrição |
|-------------|--------|------------|------------|
| `clientId`  | `String` | ✅ | ID do cliente fornecido pelo HubSpot |
| `redirectUri` | `String` | ✅ | URL de redirecionamento após a autenticação |
| `clientSecret` | `String` | ✅ | Chave secreta do cliente para autenticação |
| `code` | `String` | ✅ | Código de autorização recebido do HubSpot |

Exemplo de requisição:
GET http://localhost:8080/authorization/token?clientId=CLIENT_ID&redirectUri=REDIRECT_URI&clientSecret=CLIENT_SECRET&code=CODE

**Resposta:**
```json
{
  "access_token": "ACCESS_TOKEN",
  "refresh_token": "REFRESH_TOKEN",
  "expires_in": 3600
}
```

#### ➤ **Criar um Contato**
**Endpoint: POST /contact/create**

**Headers:**  
| Nome                 | Obrigatório | Descrição                          |
|----------------------|------------|----------------------------------|
| `authorization_token` | ✅         | Token OAuth obtido via autenticação |


**Corpo da requisição:**  
```json
{
  "properties": {
    "email": "teste@example.com",
    "firstname": "João",
    "lastname": "Silva",
    "phone": "+55 11 99999-0000",
    "company": "Exemplo Ltda"
  }
}
```

**Resposta:**
```json
{
  "id": "******",
  "properties": {
    "company": "Exemplo Ltda",
    "createdate": "2025-04-13T22:36:58.210Z",
    "email": "teste@example.com",
    "firstname": "João",
    "hs_all_contact_vids": "********",
    "hs_associated_target_accounts": "0",
    "hs_calculated_phone_number": "+5511999990000",
    "hs_calculated_phone_number_country_code": "BR",
    "hs_currently_enrolled_in_prospecting_agent": "false",
    "hs_email_domain": "example.com"
  },
  "createdAt": "2025-04-13T22:36:58.210Z",
  "updatedAt": "2025-04-13T22:36:58.210Z",
  "archived": false
}
```

#### ➤ **Processamento de Webhook**  
**Endpoint: POST /contact/creation/webhook**  

**Corpo da requisição (enviado pelo HubSpot):**
```json
[
  {
    "eventType": "contact.creation",
    "objectId": "67890",
    "properties": {
      "email": "novo@example.com",
      "firstname": "João",
      "lastname": "Pereira"
    }
  }
]
```

**Resposta:**
```json
{
  "message": "Eventos processados com sucesso."
}
```


## 🛠 **Passo a Passo para Usar a API**

Este guia explica como **autenticar no HubSpot**, **gerar um token**, e **criar contatos** usando a API.

### ✅ **1 - Gerar URL de Autorização**
Antes de poder acessar os dados do HubSpot, você precisa **gerar a URL de autorização**.

**Endpoint:**  
GET /authorization/url  
**Parâmetros obrigatórios:**  
| Nome          | Tipo     | Obrigatório | Descrição |
|--------------|---------|------------|------------|
| `clientId`   | `String` | ✅ | ID do cliente fornecido pelo HubSpot |
| `redirectUri` | `String` | ✅ | URL para onde o usuário será redirecionado após autorizar |
| `scope`      | `String` | ✅ | Escopos de permissão separados por espaço |


**Resposta esperada:**
```json
{
  "statusCode": 200,
  "url": "https://app.hubspot.com/oauth/authorize?client_id=CLIENT_ID&redirect_uri=REDIRECT_URI&scope=crm.objects.contacts.write%20oauth"
}
```
### 🔹 Acesse essa URL no navegador  
Após gerar a URL, copie e cole no navegador. O HubSpot exibirá uma página para que você autorize o acesso.

### ✅ **2 - Obter codigo de autorização**  
Quando o usuário autorizar, o HubSpot o redirecionará para a redirectUri definida, anexando um código de autorização.
Exemplo da URL gerada pelo HubSpot após autorização:  
```json
http://localhost:8080/authenticated?code=1234567890abcdef
```
Copie o código code=1234567890abcdef, pois ele será necessário para o próximo passo.

### ✅ **3 - Trocando o codigo pelo token**  
Agora, trocaremos o código por um token de acesso.
```json
GET /authorization/token
```
**Parâmetros obrigatórios:**  
| Nome          | Tipo     | Obrigatório | Descrição                                     |
|--------------|---------|------------|----------------------------------------------|
| `clientId`    | `String` | ✅        | ID do cliente                               |
| `redirectUri` | `String` | ✅        | Mesmo redirect URI usado na autorização     |
| `clientSecret` | `String` | ✅        | Chave secreta do cliente                    |
| `code`        | `String` | ✅        | Código gerado pelo HubSpot

**Resposta esperada:**
```json
{
  "access_token": "ACCESS_TOKEN",
  "refresh_token": "REFRESH_TOKEN",
  "expires_in": 3600
}

```
Guarde o access_token, pois ele será necessário para criar contatos
### ✅ **4 - Criar um contato no HubSpot**
Com o access_token obtido no passo anterior, agora podemos criar um contato.  
**Endpoint:**  
POST /contact/create
**Headers obrigatórios:**  
| Nome                 | Obrigatório | Descrição                                  |
|----------------------|------------|------------------------------------------|
| `authorization_token` | ✅        | Token OAuth obtido no passo anterior     |


**Corpo da requisição:**
```json
{
  "properties": {
    "email": "teste@example.com",
    "firstname": "João",
    "lastname": "Silva",
    "phone": "+55 11 99999-0000",
    "company": "Exemplo Ltda"
  }
}
```
**Resposta esperada:**
```json
{
  "id": "******",
  "properties": {
    "company": "Exemplo Ltda",
    "createdate": "2025-04-13T22:36:58.210Z",
    "email": "teste@example.com",
    "firstname": "João",
    "hs_all_contact_vids": "********",
    "hs_associated_target_accounts": "0",
    "hs_calculated_phone_number": "+5511999990000",
    "hs_calculated_phone_number_country_code": "BR",
    "hs_currently_enrolled_in_prospecting_agent": "false",
    "hs_email_domain": "example.com"
  },
  "createdAt": "2025-04-13T22:36:58.210Z",
  "updatedAt": "2025-04-13T22:36:58.210Z",
  "archived": false
}
```
