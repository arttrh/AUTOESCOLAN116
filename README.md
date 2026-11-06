# Autoescola N116

Gestão de alunos, instrutores, usuários e aulas. A API usa Java 25, Spring Boot 4.1.1, JPA e MySQL 8.4. A interface usa HTML, CSS e JavaScript, sem framework e sem etapa de compilação.

A estrutura original foi mantida: controllers recebem as requisições, os casos de uso aplicam as regras, e os adapters implementam o acesso ao banco. As pastas `adapter/out` e `application/port/out` fazem parte do código e não devem ser ignoradas pelo Git.

## Funcionalidades

- Login JWT com perfis `ADMIN` e `USER`.
- Cadastro, consulta, atualização e inativação de alunos e instrutores.
- Administração da equipe e troca da própria senha.
- Agenda semanal alimentada pela API, sem dados fictícios no frontend.
- Agendamento com regras de horário, antecedência, aluno ativo, instrutor ativo e disponibilidade.
- Seleção automática de instrutor por especialidade quando seu ID não é informado na API.

Aulas acontecem de segunda a sábado, começam em hora cheia entre 6h e 20h e exigem pelo menos 30 minutos de antecedência. Cada aluno pode ter uma aula por dia.

## Executar a API localmente

Requisitos: Docker e Docker Compose, ou Java 25, Maven e um MySQL disponível.

```sh
cp .env.example .env
```

Preencha as senhas e `JWT_SECRET` no arquivo `.env`. Não use as senhas de suas contas GitHub ou Vercel. Depois execute:

```sh
docker compose up --build
```

A API fica em `http://127.0.0.1:8080`, a documentação em `/swagger-ui/index.html` e a verificação de saúde em `/health_check`.

`ADMIN_LOGIN` e `ADMIN_PASSWORD` criam o administrador inicial caso ele ainda não exista. A senha não é fixada no código nem nas migrations. Para executar sem Docker, forneça `DATABASE_URL` (formato JDBC), `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `JWT_SECRET`, `ADMIN_LOGIN` e `ADMIN_PASSWORD` ao processo Java.

## Interface

O diretório `frontend/` é a raiz do projeto na Vercel. `vercel.json` encaminha `/api/*` para o backend; atualize o destino se o endereço do serviço mudar.

O token permanece no armazenamento da aba e é removido ao sair. As páginas de equipe ficam disponíveis apenas para administradores; a API também verifica essas permissões.

A busca na tabela filtra a página atual. Os seletores de agendamento carregam até 100 alunos e 100 instrutores; acima desse volume, devem ser substituídos por busca paginada.

## Testes

A integração usa um banco MySQL separado, configurado por `TEST_DATABASE_URL`, `TEST_DATABASE_USERNAME` e `TEST_DATABASE_PASSWORD`.

```sh
./mvnw verify
node --test frontend/tests/*.mjs
```

A configuração padrão dos testes aponta para `127.0.0.1:3312/autoescola_test`. O workflow de CI cria seu próprio MySQL na porta 3306.

Os testes cobrem autenticação, contas inativas, permissões, troca de senha, cadastro, edição parcial, duplicidade, regras de agendamento, consulta por período e disponibilidade de instrutores. Os testes do frontend verificam o contrato das datas e o tratamento de erros da API.

## E-mails

A integração original com RabbitMQ e SMTP está desabilitada por padrão. Ela exige `NOTIFICATIONS_ENABLED=true`, um RabbitMQ acessível e as variáveis `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME` e `SMTP_PASSWORD`. O envio externo não faz parte da validação local do fluxo principal.

## Configuração e dados

Credenciais ficam em variáveis de ambiente. Arquivos `.env`, tokens e dados reais de alunos não devem entrar no repositório. A limpeza do histórico não revoga uma credencial já exposta: a senha de aplicativo do provedor de e-mail precisa ser revogada na conta que a emitiu.

A migration V8 remove apenas o administrador de demonstração que ainda usa o hash de senha público original; a inicialização cria a conta configurada por ambiente. As migrations anteriores foram preservadas. O Flyway aplica as migrations e o Hibernate valida o schema. Não substitua migrations já executadas em um banco com dados sem planejar a compatibilidade.
