# Autoescola N116 — direção visual

Referências observadas: sete WebP do diretório Documentos/Portfolio e quatro quadros extraídos dos dois MP4 indicados pelo usuário. Não são assets para copiar: retratos, nomes e trabalhos pertencem às referências.

Princípios escolhidos: preto, branco e cinza papel; tipografia grotesca grande no login; alinhamentos firmes; moldura cinza e conteúdo branco; hierarquia por escala e espaço; transições curtas, sem efeitos que prejudiquem o trabalho.

A interface responde primeiro: quais aulas estão marcadas? Agenda e criação de agendamento aparecem antes dos cadastros auxiliares. Alunos, instrutores, especialidades, horários e usuários são os objetos reais do produto. Contagens e linhas vêm da API. Sem atividade, o sistema apresenta um estado vazio verdadeiro.

Direção da skill: framed-sheet, light, moldura cinza neutra, folha branca com raio16px, espaçamento24px. Elemento próprio: marca tipográfica N116 e agenda de instruções. O login admite tipografia editorial maior por solicitação explícita do usuário; o aplicativo preserva títulos de24–32px.

Movimento: entrada suave180ms; estados de hover e foco; respeito a prefers-reduced-motion. Não adicionar animação contínua ou efeitos de cursor.

Paper: MCP consultado; retornou Paper Desktop isn't running. Nenhuma prancha foi criada. Implementação e verificações serão feitas no navegador real; se o usuário abrir o Paper, o MCP poderá ser usado sem interromper o trabalho no código.

Validação em 06/11/2026: navegador Chromium em 1440, 768 e 390px; sem overflow na página e sem exceções JavaScript. Login, criação, edição, agendamento e inativação testados com API real e dados sintéticos em banco exclusivamente local. Diálogos recebem foco e fecham por Escape. As capturas de tela mostram esses dados de teste, não dados de produção.
