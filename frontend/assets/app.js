import {
  request,
  session,
  saveSession,
  clearSession,
  registrationPayload,
  isoDay,
  parseAppointment,
  appointmentPayload,
} from "./api.js";
const $ = (id) => document.getElementById(id);
const names = {
  agenda: "Agenda",
  alunos: "Alunos",
  instrutores: "Instrutores",
  usuarios: "Equipe",
  configuracoes: "Minha conta",
};
const descriptions = {
  agenda: "Organize as próximas aulas e acompanhe cada caminho.",
  alunos: "Cada novo caminho começa com uma pessoa.",
  instrutores: "Conheça quem prepara os próximos condutores.",
  usuarios: "As pessoas que mantêm tudo em movimento.",
  configuracoes: "Seu acesso, do seu jeito.",
};
const singular = {
  alunos: "aluno",
  instrutores: "instrutor",
  usuarios: "usuário",
};
const kinds = {
  MOTOS: "Motos",
  CARROS: "Carros",
  VANS: "Vans",
  CAMINHOES: "Caminhões",
};
let view = "agenda",
  pageNumber = 0,
  records = [],
  week = startOfWeek(new Date()),
  selectedDay = isoDay(new Date()),
  appointments = [],
  editId = null,
  currentForm = null;
function element(tag, text, className) {
  const el = document.createElement(tag);
  if (text != null) el.textContent = text;
  if (className) el.className = className;
  return el;
}
function startOfWeek(value) {
  const d = new Date(value);
  d.setHours(0, 0, 0, 0);
  d.setDate(d.getDate() - ((d.getDay() + 6) % 7));
  return d;
}
function addDays(value, n) {
  const d = new Date(value);
  d.setDate(d.getDate() + n);
  return d;
}
function showMessage(text) {
  $("page-message").textContent = text;
  $("page-message").hidden = !text;
}
function showAccessForm(kind) {
  const registering = kind === "register";
  $("login-form").hidden = registering;
  $("register-form").hidden = !registering;
  $("login-help").hidden = registering;
  $("register-help").hidden = !registering;
  $("access-title").innerHTML = registering
    ? "Crie seu<br />acesso."
    : "Bom ter<br />você por aqui.";
  $("access-description").textContent = registering
    ? "Cadastre-se para acompanhar a autoescola."
    : "Entre para acompanhar a sua autoescola.";
  $("login-error").hidden = true;
  $("register-error").hidden = true;
  (registering ? $("register-login") : $("login")).focus();
}
function loginScreen(message = "") {
  clearSession();
  document.querySelector(".skip-link").href = "#conteudo";
  $("workspace").hidden = true;
  $("login-screen").hidden = false;
  showAccessForm("login");
  $("login-error").textContent = message;
  $("login-error").hidden = !message;
}
function showWorkspace() {
  document.querySelector(".skip-link").href = "#app-content";
  $("login-screen").hidden = true;
  $("workspace").hidden = false;
  $("users-nav").hidden = session()?.perfil !== "ADMIN";
}
async function handleFailure(error) {
  if (error.status === 401) loginScreen(error.message);
  else showMessage(error.message);
}
async function enter(login, senha) {
  const data = await request("/login", {
    method: "POST",
    body: JSON.stringify({ login, senha }),
  });
  if (!data.tokenJWT || !data.id)
    throw new Error("O servidor não concluiu o acesso. Tente novamente.");
  saveSession(data);
  showWorkspace();
  location.hash = "agenda";
  await navigate();
}
$("login-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  const button = event.currentTarget.querySelector('[type="submit"]');
  const label = button.querySelector("span");
  button.disabled = true;
  label.textContent = "Entrando…";
  $("login-error").hidden = true;
  try {
    await enter($("login").value.trim(), $("senha").value);
    $("senha").value = "";
  } catch (error) {
    $("login-error").textContent = error.message;
    $("login-error").hidden = false;
  } finally {
    button.disabled = false;
    label.textContent = "Entrar no sistema";
  }
});
$("register-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  const button = event.currentTarget.querySelector('[type="submit"]');
  const label = button.querySelector("span");
  button.disabled = true;
  label.textContent = "Criando…";
  $("register-error").hidden = true;
  try {
    const data = registrationPayload(
      $("register-login").value,
      $("register-password").value,
      $("register-confirm").value,
    );
    await request("/usuarios/cadastro", {
      method: "POST",
      body: JSON.stringify(data),
    });
    event.target.reset();
    await enter(data.login, data.senha);
  } catch (error) {
    $("register-error").textContent = error.message;
    $("register-error").hidden = false;
  } finally {
    button.disabled = false;
    label.textContent = "Criar conta";
  }
});
$("open-register").addEventListener("click", () => showAccessForm("register"));
$("open-login").addEventListener("click", () => showAccessForm("login"));
$("mostrar-senha").addEventListener("click", () => {
  const visible = $("senha").type === "password";
  $("senha").type = visible ? "text" : "password";
  $("mostrar-senha").textContent = visible ? "Ocultar" : "Mostrar";
  $("mostrar-senha").setAttribute("aria-pressed", String(visible));
});
$("logout").addEventListener("click", () => loginScreen());
$("mobile-toggle").addEventListener("click", () => {
  const open = $("workspace").classList.toggle("menu-open");
  $("mobile-toggle").setAttribute("aria-expanded", String(open));
});
window.addEventListener("hashchange", () => navigate());
async function navigate() {
  if (!session()) return;
  view = location.hash.slice(1) in names ? location.hash.slice(1) : "agenda";
  if (view === "usuarios" && session().perfil !== "ADMIN") view = "agenda";
  pageNumber = 0;
  $("workspace").classList.remove("menu-open");
  $("mobile-toggle").setAttribute("aria-expanded", "false");
  document
    .querySelectorAll("[data-view]")
    .forEach((a) => a.toggleAttribute("aria-current", false));
  document
    .querySelector(`[data-view="${view}"]`)
    ?.setAttribute("aria-current", "page");
  $("breadcrumb-current").textContent = names[view];
  $("page-title").textContent = names[view] + ".";
  $("page-description").textContent = descriptions[view];
  $("page-kicker").textContent =
    view === "agenda" ? "Um dia de cada vez." : "Autoescola N116";
  $("agenda-view").hidden = view !== "agenda";
  $("list-view").hidden = !(view in singular);
  $("settings-view").hidden = view !== "configuracoes";
  $("primary-action").hidden = view === "configuracoes";
  $("primary-label").textContent =
    view === "agenda" ? "Agendar aula" : "Cadastrar " + (singular[view] || "");
  $("search").value = "";
  showMessage("");
  try {
    if (view === "agenda") await loadAgenda();
    else if (view in singular) await loadList();
  } catch (error) {
    await handleFailure(error);
  }
}
async function checkConnection() {
  try {
    await request("/health_check");
    $("connection-status").textContent = "Sistema conectado";
    $("connection-status").classList.add("online");
  } catch {
    $("connection-status").textContent = "Conexão indisponível";
    $("connection-status").classList.remove("online");
  }
}
async function loadAgenda() {
  $("schedule-list").textContent = "Carregando as aulas…";
  const end = addDays(week, 7);
  const [aulas, alunos, instrutores] = await Promise.all([
    request(`/instrucoes?inicio=${isoDay(week)}&fim=${isoDay(end)}`),
    request("/alunos?size=4"),
    request("/instrutores?size=4"),
  ]).catch((error) => {
    $("schedule-list").textContent = "Não foi possível carregar as aulas.";
    throw error;
  });
  appointments = aulas;
  $("count-aulas").textContent = String(aulas.length);
  $("count-alunos").textContent = String(alunos.totalElements);
  $("count-instrutores").textContent = String(instrutores.totalElements);
  $("today-label").textContent = new Intl.DateTimeFormat("pt-BR", {
    day: "2-digit",
    month: "short",
  })
    .format(new Date())
    .replace(" de ", " ");
  $("period-label").textContent =
    `${new Intl.DateTimeFormat("pt-BR", { day: "numeric", month: "long" }).format(week)} — ${new Intl.DateTimeFormat("pt-BR", { day: "numeric", month: "long" }).format(addDays(week, 6))}`;
  renderDays();
  renderSchedule();
  renderPeople(instrutores.content);
  await checkConnection();
}
function renderDays() {
  const container = $("week-days");
  container.replaceChildren();
  for (let i = 0; i < 7; i++) {
    const day = addDays(week, i),
      key = isoDay(day),
      button = element("button", null, "week-day");
    button.type = "button";
    button.classList.toggle("selected", key === selectedDay);
    button.classList.toggle("today", key === isoDay(new Date()));
    button.setAttribute("aria-pressed", String(key === selectedDay));
    button.setAttribute(
      "aria-label",
      new Intl.DateTimeFormat("pt-BR", { dateStyle: "full" }).format(day),
    );
    button.append(
      element(
        "span",
        new Intl.DateTimeFormat("pt-BR", { weekday: "short" })
          .format(day)
          .replace(".", ""),
      ),
      element("strong", String(day.getDate()).padStart(2, "0")),
    );
    if (appointments.some((a) => isoDay(parseAppointment(a.data)) === key))
      button.append(element("span", null, "day-dot"));
    button.addEventListener("click", () => {
      selectedDay = key;
      renderDays();
      renderSchedule();
    });
    container.append(button);
  }
}
function emptyState(title, description, action) {
  const state = element("div", null, "empty-state");
  state.append(
    element("span", "↗", "empty-symbol"),
    element("h3", title),
    element("p", description),
  );
  if (action) {
    const button = element("button", "Agendar uma aula ↗", "text-button");
    button.type = "button";
    button.addEventListener("click", () => openForm("agenda"));
    state.append(button);
  }
  return state;
}
function renderSchedule() {
  const rows = appointments
    .filter((a) => isoDay(parseAppointment(a.data)) === selectedDay)
    .sort((a, b) => parseAppointment(a.data) - parseAppointment(b.data));
  $("selected-day-label").textContent = new Intl.DateTimeFormat("pt-BR", {
    weekday: "long",
    day: "numeric",
    month: "long",
  }).format(new Date(selectedDay + "T12:00:00"));
  $("day-count").textContent =
    `${rows.length} aula${rows.length === 1 ? "" : "s"}`;
  const list = $("schedule-list");
  list.replaceChildren();
  if (!rows.length) {
    list.append(
      emptyState(
        "Um espaço para o próximo caminho.",
        "Ainda não há aulas neste dia. Escolha um horário para começar.",
        true,
      ),
    );
    return;
  }
  for (const row of rows) {
    const button = element("button", null, "schedule-row");
    button.type = "button";
    const identity = element("span");
    identity.append(
      element("span", row.aluno, "schedule-person"),
      element(
        "span",
        `${row.instrutor} · ${kinds[row.especialidade] || row.especialidade}`,
        "schedule-detail",
      ),
    );
    button.append(
      element(
        "span",
        new Intl.DateTimeFormat("pt-BR", {
          hour: "2-digit",
          minute: "2-digit",
        }).format(parseAppointment(row.data)),
        "schedule-time",
      ),
      identity,
      element("span", "↗"),
    );
    button.addEventListener("click", () => showAppointment(row));
    list.append(button);
  }
}
function renderPeople(people) {
  const container = $("instructors-preview");
  container.replaceChildren();
  if (!people.length) {
    container.append(
      element(
        "p",
        "Cadastre o primeiro instrutor para começar.",
        "small muted",
      ),
    );
    return;
  }
  for (const person of people) {
    const row = element("div", null, "person-row"),
      identity = element("span");
    identity.append(
      element("span", person.nome, "person-name"),
      element(
        "span",
        kinds[person.especialidade] || person.especialidade,
        "person-caption",
      ),
    );
    row.append(
      element(
        "span",
        person.nome
          .split(" ")
          .filter(Boolean)
          .slice(0, 2)
          .map((x) => x[0])
          .join("")
          .toUpperCase(),
        "person-avatar",
      ),
      identity,
    );
    container.append(row);
  }
}
for (const [id, n] of [
  ["previous-week", -7],
  ["next-week", 7],
])
  $(id).addEventListener("click", async () => {
    week = addDays(week, n);
    selectedDay = isoDay(week);
    try {
      await loadAgenda();
    } catch (error) {
      handleFailure(error);
    }
  });
$("this-week").addEventListener("click", async () => {
  week = startOfWeek(new Date());
  selectedDay = isoDay(new Date());
  try {
    await loadAgenda();
  } catch (error) {
    handleFailure(error);
  }
});
async function loadList() {
  const data = await request(
    `/${view}?page=${pageNumber}&size=10&sort=${view === "usuarios" ? "login" : "nome"}`,
  );
  records = data.content;
  $("list-count").textContent =
    `${data.totalElements} cadastro${data.totalElements === 1 ? "" : "s"}`;
  $("page-info").textContent =
    `${data.number + 1} de ${Math.max(1, data.totalPages)}`;
  $("prev-page").disabled = data.first;
  $("next-page").disabled = data.last;
  renderList();
  await checkConnection();
}
function renderList() {
  const headings =
    view === "usuarios"
      ? ["Usuário", "Perfil", "Ações"]
      : view === "instrutores"
        ? ["Instrutor", "Contato", "Especialidade", "Ações"]
        : ["Aluno", "Contato", "Ações"];
  const tr = element("tr");
  headings.forEach((x) => tr.append(element("th", x)));
  $("table-head").replaceChildren(tr);
  $("table-body").replaceChildren();
  const query = $("search").value.trim().toLocaleLowerCase("pt-BR");
  const rows = records.filter((row) =>
    (row.nome || row.login).toLocaleLowerCase("pt-BR").includes(query),
  );
  $("list-empty").hidden = rows.length > 0;
  $("list-empty").replaceChildren();
  if (!rows.length)
    $("list-empty").append(
      emptyState(
        query
          ? "Nenhum resultado nesta página."
          : "O primeiro cadastro abre novos caminhos.",
        query
          ? "Tente outro nome ou avance para outra página."
          : `Use o botão acima para cadastrar ${singular[view]}.`,
        false,
      ),
    );
  for (const row of rows) {
    const tr = element("tr");
    tr.append(element("td", row.nome || row.login));
    if (view === "usuarios")
      tr.append(
        element("td", row.perfil === "ADMIN" ? "Administrador" : "Equipe"),
      );
    else tr.append(element("td", row.email));
    if (view === "instrutores")
      tr.append(element("td", kinds[row.especialidade] || row.especialidade));
    const actions = element("div", null, "table-actions");
    for (const [label, handler] of [
      ["Editar", () => openForm(view, row.id)],
      ["Inativar", () => confirmInactivate(view, row)],
    ]) {
      const button = element("button", label);
      button.type = "button";
      button.setAttribute("aria-label", `${label} ${row.nome || row.login}`);
      button.addEventListener("click", handler);
      actions.append(button);
    }
    const cell = element("td");
    cell.append(actions);
    tr.append(cell);
    $("table-body").append(tr);
  }
}
$("search").addEventListener("input", renderList);
for (const [id, n] of [
  ["prev-page", -1],
  ["next-page", 1],
])
  $(id).addEventListener("click", async () => {
    pageNumber += n;
    try {
      await loadList();
    } catch (error) {
      handleFailure(error);
    }
  });
$("primary-action").addEventListener("click", () => openForm(view));
function field(
  parent,
  name,
  label,
  {
    type = "text",
    value = "",
    required = true,
    options = null,
    help = "",
    pattern = null,
    full = false,
    maxLength = null,
  } = {},
) {
  const wrap = element("div", null, "field" + (full ? " full-width" : "")),
    lab = element("label", label);
  lab.htmlFor = "field-" + name;
  const input = document.createElement(options ? "select" : "input");
  input.id = "field-" + name;
  input.name = name;
  if (options) {
    input.add(new Option("Selecione", ""));
    for (const option of options)
      input.add(new Option(option.label, option.value));
  } else input.type = type;
  input.required = required;
  if (pattern) input.pattern = pattern;
  if (maxLength) input.maxLength = maxLength;
  input.value = String(value ?? "");
  if (type === "email") input.autocomplete = "email";
  if (type === "password") input.autocomplete = "new-password";
  wrap.append(lab, input);
  if (help) {
    const p = element("p", help, "small muted");
    p.id = input.id + "-help";
    input.setAttribute("aria-describedby", p.id);
    wrap.append(p);
  }
  parent.append(wrap);
  return input;
}
async function openForm(resource, id = null) {
  currentForm = resource;
  editId = id;
  const dialog = $("form-dialog"),
    fields = $("dialog-fields");
  fields.replaceChildren();
  $("dialog-error").hidden = true;
  $("save-entity").hidden = false;
  $("save-entity").disabled = true;
  dialog.showModal();
  $("dialog-title").textContent =
    resource === "agenda"
      ? "Um novo caminho."
      : (id ? "Editar " : "Cadastrar ") + singular[resource] + ".";
  $("dialog-kicker").textContent =
    resource === "agenda" ? "Agendamento de aula" : "Autoescola N116";
  try {
    if (resource === "agenda") {
      // shortcut: seletores limitados a100 cadastros, trocar por busca paginada quando a escola ultrapassar esse volume.
      const [alunos, instrutores] = await Promise.all([
        request("/alunos?size=100"),
        request("/instrutores?size=100"),
      ]);
      field(fields, "idAluno", "Aluno", {
        options: alunos.content.map((p) => ({ value: p.id, label: p.nome })),
        full: true,
      });
      field(fields, "idInstrutor", "Instrutor", {
        options: instrutores.content.map((p) => ({
          value: p.id,
          label: `${p.nome} · ${kinds[p.especialidade]}`,
        })),
        full: true,
      });
      field(fields, "data", "Data e horário", {
        type: "datetime-local",
        value: selectedDay + "T09:00",
        full: true,
        help: "As aulas começam em hora cheia, de segunda a sábado, entre 6h e 20h. Agende com pelo menos 30 minutos de antecedência.",
      });
    } else {
      const record = id ? await request(`/${resource}/${id}`) : {};
      if (resource === "usuarios") {
        field(fields, "login", "Usuário", {
          value: record.login,
          full: true,
          maxLength: 100,
        });
        if (!id)
          field(fields, "senha", "Senha inicial", {
            type: "password",
            full: true,
            help: "De 8 a 72 caracteres.",
          }).minLength = 8;
        field(fields, "perfil", "Perfil", {
          value: record.perfil,
          options: [
            { value: "USER", label: "Equipe" },
            { value: "ADMIN", label: "Administrador" },
          ],
          full: true,
        });
      } else {
        field(fields, "nome", "Nome completo", {
          value: record.nome,
          full: true,
          maxLength: 100,
        });
        field(fields, "email", "E-mail", {
          type: "email",
          value: record.email,
          maxLength: 100,
        });
        const telefone = field(fields, "telefone", "Telefone", {
          type: "tel",
          value: record.telefone,
          help: "Exemplo: (11) 99999-9999",
          pattern: "\\(\\d{2}\\) \\d{5}-\\d{4}",
        });
        telefone.addEventListener("input", () => {
          const digits = telefone.value.replace(/\D/g, "").slice(0, 11);
          if (digits.length === 11)
            telefone.value = `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7)}`;
        });
        if (!id)
          field(
            fields,
            resource === "alunos" ? "cpf" : "cnh",
            resource === "alunos" ? "CPF" : "CNH",
            {
              help: "Somente números, de 9 a 11 dígitos.",
              pattern: "\\d{9,11}",
              maxLength: 11,
            },
          );
        if (resource === "instrutores")
          field(fields, "especialidade", "Especialidade", {
            value: record.especialidade,
            options: Object.entries(kinds).map(([value, label]) => ({
              value,
              label,
            })),
          });
        const address = element("fieldset");
        address.append(element("legend", "Endereço"));
        fields.append(address);
        for (const [key, label, extra] of [
          ["logradouro", "Logradouro", { full: true, maxLength: 100 }],
          ["numero", "Número", { required: false, maxLength: 10 }],
          ["complemento", "Complemento", { required: false, maxLength: 20 }],
          ["bairro", "Bairro", { maxLength: 100 }],
          ["cidade", "Cidade", { maxLength: 100 }],
          [
            "uf",
            "UF",
            {
              pattern: "[A-Z]{2}",
              maxLength: 2,
              help: "Duas letras maiúsculas.",
            },
          ],
          [
            "cep",
            "CEP",
            {
              pattern: "[0-9]{8}",
              maxLength: 8,
              help: "8 dígitos, sem traço.",
            },
          ],
        ])
          field(address, key, label, {
            value: record.endereco?.[key],
            ...extra,
          });
      }
    }
    $("save-entity").disabled = false;
  } catch (error) {
    $("dialog-error").textContent = error.message;
    $("dialog-error").hidden = false;
  }
}
$("entity-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  $("save-entity").disabled = true;
  $("dialog-error").hidden = true;
  try {
    const values = Object.fromEntries(new FormData(event.currentTarget));
    let path = "/" + currentForm,
      payload;
    if (currentForm === "agenda") {
      path = "/instrucoes";
      payload = {
        idAluno: Number(values.idAluno),
        idInstrutor: Number(values.idInstrutor),
        data: appointmentPayload(values.data),
      };
    } else if (currentForm === "usuarios")
      payload = {
        login: values.login,
        perfil: values.perfil,
        ...(!editId ? { senha: values.senha } : { id: editId }),
      };
    else {
      payload = {
        nome: values.nome,
        email: values.email,
        telefone: values.telefone,
        endereco: Object.fromEntries(
          [
            "logradouro",
            "numero",
            "complemento",
            "bairro",
            "cidade",
            "uf",
            "cep",
          ].map((k) => [k, values[k]]),
        ),
      };
      if (editId) payload.id = editId;
      else
        payload[currentForm === "alunos" ? "cpf" : "cnh"] =
          values[currentForm === "alunos" ? "cpf" : "cnh"];
      if (currentForm === "instrutores")
        payload.especialidade = values.especialidade;
    }
    await request(path, {
      method: editId ? "PUT" : "POST",
      body: JSON.stringify(payload),
    });
    $("form-dialog").close();
    if (view === "agenda") await loadAgenda();
    else await loadList();
    showMessage("Tudo certo. Informações salvas com sucesso.");
  } catch (error) {
    $("dialog-error").textContent = error.message;
    $("dialog-error").hidden = false;
  } finally {
    $("save-entity").disabled = false;
  }
});
for (const id of ["close-dialog", "cancel-dialog"])
  $(id).addEventListener("click", () => $("form-dialog").close());
function showAppointment(row) {
  $("dialog-title").textContent = "Detalhes da aula.";
  $("dialog-kicker").textContent = "Autoescola N116";
  $("dialog-fields").replaceChildren();
  $("dialog-error").hidden = true;
  $("save-entity").hidden = true;
  const dl = element("dl", null, "read-only-details");
  for (const [label, value] of [
    ["Aluno", row.aluno],
    ["Instrutor", row.instrutor],
    ["Especialidade", kinds[row.especialidade]],
    [
      "Horário",
      new Intl.DateTimeFormat("pt-BR", {
        dateStyle: "long",
        timeStyle: "short",
      }).format(parseAppointment(row.data)),
    ],
  ]) {
    const line = element("div");
    line.append(element("dt", label), element("dd", value));
    dl.append(line);
  }
  $("dialog-fields").append(dl);
  $("form-dialog").showModal();
}
function confirmInactivate(resource, row) {
  $("confirm-description").textContent =
    `${row.nome || row.login} deixará de aparecer nos cadastros ativos.`;
  $("confirm-error").hidden = true;
  $("confirm-dialog").showModal();
  $("accept-confirm").onclick = async () => {
    $("accept-confirm").disabled = true;
    try {
      await request(`/${resource}/${row.id}`, { method: "DELETE" });
      $("confirm-dialog").close();
      await loadList();
      showMessage("Cadastro inativado com sucesso.");
    } catch (error) {
      $("confirm-error").textContent = error.message;
      $("confirm-error").hidden = false;
    } finally {
      $("accept-confirm").disabled = false;
    }
  };
}
$("cancel-confirm").addEventListener("click", () =>
  $("confirm-dialog").close(),
);
$("password-form").addEventListener("submit", async (event) => {
  event.preventDefault();
  const button = event.currentTarget.querySelector('[type="submit"]');
  button.disabled = true;
  $("password-message").classList.remove("success");
  try {
    if ($("new-password").value !== $("confirm-password").value)
      throw new Error("As novas senhas precisam ser iguais.");
    await request("/usuarios", {
      method: "PATCH",
      body: JSON.stringify({
        id: session().id,
        senhaAntiga: $("old-password").value,
        senhaNova: $("new-password").value,
      }),
    });
    event.target.reset();
    $("password-message").textContent = "Sua senha foi atualizada.";
    $("password-message").classList.add("success");
  } catch (error) {
    $("password-message").textContent = error.message;
  } finally {
    button.disabled = false;
  }
});
if (session()) {
  showWorkspace();
  navigate();
} else loginScreen();
