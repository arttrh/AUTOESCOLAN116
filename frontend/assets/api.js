const KEY = "n116-session";
export function session() {
  try {
    return JSON.parse(sessionStorage.getItem(KEY) || "null");
  } catch {
    sessionStorage.removeItem(KEY);
    return null;
  }
}
export function saveSession(value) {
  sessionStorage.setItem(KEY, JSON.stringify(value));
}
export function clearSession() {
  sessionStorage.removeItem(KEY);
}
export async function request(path, options = {}) {
  let response;
  try {
    response = await fetch("/api" + path, {
      ...options,
      headers: {
        ...(options.body ? { "Content-Type": "application/json" } : {}),
        ...(session()?.tokenJWT
          ? { Authorization: `Bearer ${session().tokenJWT}` }
          : {}),
        ...options.headers,
      },
    });
  } catch {
    throw new Error(
      "Não foi possível conectar. Confira sua conexão e tente novamente.",
    );
  }
  if (response.status === 401) {
    const error = new Error(
      path === "/login"
        ? "Confira seu usuário e senha."
        : "Sua sessão expirou. Entre novamente.",
    );
    error.status = 401;
    throw error;
  }
  if (response.status === 403)
    throw new Error("Seu perfil não tem permissão para esta ação.");
  if (response.status === 429)
    throw new Error("Muitas tentativas. Aguarde um minuto e tente novamente.");
  if (response.status >= 500)
    throw new Error(
      "O servidor está iniciando ou indisponível. Aguarde um minuto e tente novamente.",
    );
  if (response.status === 204) return null;
  const raw = await response.text();
  let body;
  try {
    body = JSON.parse(raw);
  } catch {
    if (response.ok && !raw.trim().startsWith("<")) return raw;
    throw new Error(
      raw && !raw.trim().startsWith("<")
        ? raw.slice(0, 250)
        : "O servidor não concluiu a resposta. Tente novamente em instantes.",
    );
  }
  if (!response.ok) {
    const text = Array.isArray(body)
      ? body
          .map((x) => `${x.campo || x.field}: ${x.mensagem || x.message}`)
          .join(" · ")
      : body.message || body.mensagem || body.error;
    throw new Error(
      text || "Não foi possível concluir a operação. Confira os dados.",
    );
  }
  return body;
}
export function isoDay(date) {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
}
export function parseAppointment(value) {
  if (/^\d{2}\/\d{2}\/\d{4} - \d{2}:\d{2}$/.test(value)) {
    const [date, time] = value.split(" - ");
    const [d, m, y] = date.split("/");
    return new Date(`${y}-${m}-${d}T${time}:00`);
  }
  return new Date(value);
}
export function appointmentPayload(value) {
  const [day, time] = value.split("T");
  return day.split("-").reverse().join("/") + " - " + time.slice(0, 5);
}
