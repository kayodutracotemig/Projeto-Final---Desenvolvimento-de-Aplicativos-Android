# Multiverso
App Android que lista personagens de Rick and Morty, permite buscar e filtrar por status e salvar favoritos na conta de cada utilizador.

## API utilizada
Rick and Morty API, endereço base `https://rickandmortyapi.com/api/`. Usamos `GET /character` (lista com busca por nome, filtro de status e paginação) e `GET /character/{id}` (ecrã de detalhe).

## Prints
**Login:** ![login](prints/login.png)
**Cadastro:** ![cadastro](prints/cadastro.png)
**Lista:** ![lista](prints/lista.png)
**Detalhe:** ![detalhe](prints/detalhe.png)
**Favoritos:** ![favoritos](prints/favoritos.png)
**Erro (modo avião):** ![erro](prints/erro.png)

## Divisão de tarefas
* **Lucas Machado:** Camada de Network e Data (Integração da API com Retrofit, base de dados Room e Repository).
* **Kayo Dutra:** Camadas de User Interface e ViewModel (Construção das interfaces em Compose, estados e fluxo do Navigation).
* **Leonardo Henrique:** Autenticação local e sessão (Ecrãs de cadastro/login, hash de senhas e persistência da sessão do utilizador).

## O que aprendemos
- Lucas Machado: Juntar o Room com as chamadas do Retrofit usando coroutines ajudou-me a entender de verdade como o Repository funciona na prática.
- Kayo Dutra: Configurar as rotas no Navigation Compose e ligar as interfaces aos estados do ViewModel deu trabalho no início, mas o resultado final ficou muito fluido.
- Leonardo Henrique: Fazer o hash da senha e garantir que a app mantivesse o utilizador com sessão iniciada mesmo após ser fechada deu-me uma base ótima de como gerir sessões.