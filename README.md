# Locar-API

Projeto de estudos de Testes Unitários

Atividade a ser implementada:

CarroService
CadastrarNovoCarro
- Validar se vai já não está registrado com a mesma placa.
- Um carro terá placa , codCarro, cor, modelo, anoFab, quilometragem.
  BuscarPorPlaca
- Buscar por placa
  FiltraSemiNovosParaVenda
- Para ser elegível deve ter mais de 1 ano de fab ou 40 mil km rodados.

ViagemService
RegistrarViagem
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

Configuração do banco de dados:

Docker:
```
docker run --name postgres -e POSTGRES_PASSWORD=admin -d -p 5432:5432 postgres
```

