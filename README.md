# 🚗 Auto Escola N116

<div align="center">

**API REST para gestão de autoescola — cadastro de alunos, instrutores e agendamento de instruções**

![Java](https://img.shields.io/badge/Java-25-ED8936?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

</div>

---

## 📋 Sobre o Projeto

Uma autoescola tem três coisas pra controlar: quem são os alunos, quem são os
instrutores e quando cada aula acontece. As duas primeiras são CRUD. A terceira
é onde o projeto fica interessante.

Agendar uma instrução envolve sete regras que precisam valer ao mesmo tempo, e
em vez de empilhar `if` dentro do service, cada uma virou uma classe própria
implementando `ValidadorAgendamento`. Adicionar uma regra nova é criar uma
classe — o caso de uso não muda.

O outro ponto do projeto é o e-mail de confirmação. Ele **não** faz parte do
fluxo de agendamento: a instrução é salva, uma mensagem vai pro RabbitMQ, e um
consumidor cuida do envio. Se o SMTP do Gmail estiver lento ou fora do ar, quem
agendou já recebeu a resposta há muito tempo.

### ✨ Funcionalidades

- ✅ Cadastro, listagem paginada, detalhe, atualização e exclusão de **alunos**
- ✅ Mesmo ciclo para **instrutores**, com especialidade (motos, carros, vans, caminhões)
- ✅ Gestão de **usuários** com troca de senha e perfis `ADMIN` / `USER`
- ✅ Login com **JWT** — sem sessão no servidor
- ✅ **Agendamento de instruções** com cadeia de sete validações
- ✅ **E-mail de confirmação assíncrono** via RabbitMQ, com template Thymeleaf
- ✅ Cache em memória com **Caffeine** nas consultas mais repetidas
- ✅ Schema versionado com **Flyway** (7 migrations)
- ✅ Documentação **OpenAPI/Swagger** gerada a partir do código
- ✅ Endpoint de `health_check`

---

## 🧩 As sete regras de agendamento

Estão em `application/core/validation/instrucao/`, cada uma isolada:

| Validador | Regra |
|-----------|-------|
| `ValidadorHorarioFuncionamento` | Nada aos domingos; primeira aula 6h, última 20h |
| `ValidadorHorarioInteiro` | Só hora cheia — 08:00 sim, 08:30 não |
| `ValidadorAntecedenciaMinima` | Mínimo de 30 minutos entre o pedido e a aula |
| `ValidadorInstrutorDisponivel` | O instrutor não pode já ter aula naquele horário |
| `ValidadorInstrutorAtivo` | Instrutor inativo não recebe agendamento |
| `ValidadorAlunoAtivo` | Aluno inativo também não |
| `ValidadorLimiteDiarioAluno` | Um aluno, uma instrução por dia |

O caso de uso `AgendaDeInstrucoes` recebe a lista de validadores injetada e
percorre todos antes de persistir. Ele não sabe quantos são nem o que cada um faz.

---

## 🏗️ Arquitetura

Hexagonal (ports & adapters). O domínio fica no centro e não conhece Spring:

```
application/
├── core/
│   ├── domain/        # Aluno, Instrutor, Instrucao, Usuario, VO Endereco, enums
│   ├── usecase/       # AlunoService, InstrutorService, AgendaDeInstrucoes, LoginService
│   ├── validation/    # os validadores de agendamento
│   └── service/       # AutenticacaoService, EmailNotificacaoService
└── port/
    ├── in/            # o que o mundo pode pedir ao domínio
    └── out/           # o que o domínio precisa do mundo (repositórios)

adapter/
├── in/
│   ├── controller/    # REST: controllers, requests, responses, mappers
│   └── rabbitmq/      # consumidor das mensagens de e-mail
└── out/               # persistência JPA, entidades e mappers

config/                # security, rabbitmq, cache, documentação
exception/             # exceções de domínio por contexto
```

Trocar MySQL por Postgres, ou REST por outro transporte, é mexer em `adapter/` —
`application/core/` continua igual.

---

## 🚀 Como Rodar

### Com Docker Compose (recomendado)

Sobe API, MySQL e RabbitMQ juntos. A aplicação só inicia depois que os dois
passam no healthcheck.

#### 1. Clone o repositório

```bash
git clone https://github.com/arttrh/AUTOESCOLAN116.git
cd AUTOESCOLAN116
```

#### 2. Crie um arquivo `.env` na raiz

```env
MYSQL_ROOT_PASSWORD=troque-isto
MYSQL_DATABASE=autoescolan116
MYSQL_USER=appuser
MYSQL_PASSWORD=troque-isto

JWT_SECRET=uma-chave-longa-e-aleatoria

RABBITMQ_USER=guest
RABBITMQ_PASS=guest

# Conta de envio dos e-mails de confirmação.
# SENHA_APP é a senha de app do Gmail, não a senha da conta.
E_MAIL=seu-email@gmail.com
SENHA_APP=xxxx xxxx xxxx xxxx
```

#### 3. Suba tudo

```bash
docker compose up -d
```

| Serviço | Endereço |
|---------|----------|
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Health check | http://localhost:8080/health_check |
| RabbitMQ (painel) | http://localhost:15672 |

---

### Rodando local, sem container

**Pré-requisitos:** Java 25, Maven 3.9+, MySQL 8 e RabbitMQ em pé.

```bash
# perfil dev aponta pra localhost
export MYSQL_DEV_USER=root
export MYSQL_DEV_PASS=sua-senha
export JWT_SECRET=uma-chave-longa-e-aleatoria
export E_MAIL=seu-email@gmail.com
export SENHA_APP="xxxx xxxx xxxx xxxx"

./mvnw spring-boot:run
```

O perfil `dev` é o padrão (`spring.profiles.active=dev` em `application.properties`).

---

## 🔌 Endpoints

Todos exigem `Authorization: Bearer <token>`, menos `/login` e `/health_check`.

### Autenticação

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/login` | Recebe credenciais e devolve o JWT |

### Alunos · Instrutores

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/alunos` · `/instrutores` | Cadastra |
| `GET` | `/alunos` · `/instrutores` | Lista paginado |
| `GET` | `/alunos/{id}` · `/instrutores/{id}` | Detalha |
| `PUT` | `/alunos` · `/instrutores` | Atualiza |
| `DELETE` | `/alunos/{id}` · `/instrutores/{id}` | Exclui |

### Usuários

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/usuarios` | Cadastra |
| `GET` | `/usuarios` | Lista paginado |
| `GET` | `/usuarios/{id}` | Detalha |
| `PUT` | `/usuarios` | Atualiza |
| `PATCH` | `/usuarios` | Troca a senha |
| `DELETE` | `/usuarios/{id}` | Exclui |

### Instruções

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/instrucoes` | Agenda uma aula — passa pelos sete validadores |

---

## 📝 Exemplo de uso

### 1. Autenticar

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login": "admin", "senha": "sua-senha"}'
```

### 2. Agendar uma instrução

```bash
curl -X POST http://localhost:8080/instrucoes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "idAluno": 1,
    "idInstrutor": 1,
    "data": "2026-09-15T09:00:00"
  }'
```

Se a data cair num domingo, tiver minuto quebrado, faltar menos de 30 minutos ou
o instrutor já estiver ocupado, a resposta é `400` com a mensagem do validador
que barrou. Deu certo, o e-mail de confirmação sai pela fila.

---

## 🛠️ Tecnologias

| Tecnologia | Papel no projeto |
|------------|------------------|
| Java 25 | Linguagem |
| Spring Boot 3.2 | Framework base |
| Spring Security + java-jwt | Autenticação stateless por perfil |
| Spring Data JPA | Persistência |
| MySQL 8.4 | Banco de dados |
| Flyway | Migrations versionadas |
| RabbitMQ | Fila dos e-mails de confirmação |
| Spring Mail + Thymeleaf | Montagem e envio do e-mail |
| Caffeine | Cache em memória |
| springdoc-openapi | Swagger UI |
| Docker Compose | API, banco e broker num comando |

---

## 🐛 Problemas comuns

**A aplicação sobe e cai na hora**
Provavelmente falta variável de ambiente. `JWT_SECRET`, `E_MAIL` e `SENHA_APP`
não têm valor padrão em produção — confira o `.env`.

**Erro de autenticação no envio de e-mail**
`SENHA_APP` precisa ser uma *senha de app* do Google (com verificação em duas
etapas ligada), não a senha normal da conta.

**Flyway reclamando de checksum**
Alguma migration já aplicada foi editada depois. Em desenvolvimento, derrube o
volume do banco (`docker compose down -v`) e suba de novo.

**Porta 3306 ocupada**
Já tem um MySQL local rodando. Pare ele, ou mude a porta publicada no
`docker-compose.yml`.

---

## 👤 Autor

**Arthur Lucas**
GitHub: [@arttrh](https://github.com/arttrh)

---

<div align="center">

**Projeto desenvolvido durante a formação no SENAI**

⭐ Se este projeto te ajudou, deixa uma estrela!

</div>
