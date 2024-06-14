# Locar-API

## Descrição
Projeto "Locar-API" é uma aplicação Java baseada em Spring Boot para gerenciamento de locações.

## Requisitos
- JDK (Java Development Kit) 17 ou superior
- Docker

## Iniciando a Aplicação

### 1. Clonar o Repositório
Clone o repositório para sua máquina local:

```bash
git clone https://github.com/seu-usuario/locar-api.git

cd locar-api
```

### 2 - Variáveis da aplicação
Ajuste as variáveis do arquivo .env caso necessário
```
# Variáveis para o servidor de aplicação - Spring Boot
SERVER_PORT=8081

# Variáveis para Docker Compose
DATABASE_PORT=5433
DATABASE_NAME=locadb
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
```

### 3 - Subindo a base de dados
Execute o comando abaixo para criar o banco e iniciar a aplicação
```
docker-compose up --build -d
```

### 4 - Acesse a aplicação
<h2><a href="">http://localhost:8081/actuator</a></h2>