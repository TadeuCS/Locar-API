# Locar-API

## Descrição
Projeto "Locar-API" é uma aplicação Java baseada em Spring Boot para gerenciamento de locações.

Projeto de estudos de Testes Unitários

Atividade a ser implementada:

[v1]
Carro
- Validar se vai já não está registrado com a mesma placa.
- Um carro terá placa , codCarro, cor, modelo, anoFab, quilometragem.
  BuscarPorPlaca
- Buscar por placa
  FiltraSemiNovosParaVenda
- Para ser elegível deve ter mais de 1 ano de fab ou 40 mil km rodados.

[v2]
Viagem
- Uma viagem deve ter codViagem, dhIni, dhFim, distancia, codCarro.
- Validar e não permitir registrar uma viagem se já existir outra Não Iniciada ou Não finalizada.
- Adicionar viagens no banco.
  IniciarViagemPorPlaca
- Validar se não encontrar viagem não iniciada.
- Atualizar data início da viagem
  FinalizarViagemPorPlaca
- Filtrar veículo por placa e pegar última viagem não finalizada.
- Validar se tem viagem em aberto para o veículo.
- Processar Quilometragem do veículo.

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
DATABASE_HOST=localhost
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
<h3><a href="">http://localhost:8081/actuator</a></h3>

### 5 - Documentação da API
<h3><a href="">http://localhost:8081/swagger-ui/index.html</a></h3>


