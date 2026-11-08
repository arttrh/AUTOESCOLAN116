import { test } from "node:test";
import assert from "node:assert/strict";
import {
  appointmentPayload,
  parseAppointment,
  isoDay,
  request,
  saveSession,
  clearSession,
  registrationPayload,
} from "../assets/api.js";
test("datas de aula mantêm o dia e horário locais no contrato da API", () => {
  assert.equal(appointmentPayload("2026-11-06T09:00"), "06/11/2026 - 09:00");
  const date = parseAppointment("06/11/2026 - 09:00");
  assert.equal(isoDay(date), "2026-11-06");
  assert.equal(date.getHours(), 9);
});
test("token é enviado no header e o403 vira mensagem legível", async () => {
  const values = new Map();
  globalThis.sessionStorage = {
    getItem: (k) => values.get(k),
    setItem: (k, v) => values.set(k, v),
    removeItem: (k) => values.delete(k),
  };
  saveSession({ tokenJWT: "teste" });
  let captured;
  globalThis.fetch = async (url, options) => {
    captured = { url, options };
    return { status: 403 };
  };
  await assert.rejects(() => request("/usuarios"), /permissão/);
  assert.equal(captured.options.headers.Authorization, "Bearer teste");
  assert.equal(captured.url, "/api/usuarios");
  clearSession();
});
test("cadastro confirma a senha e nunca envia perfil administrativo", () => {
  assert.deepEqual(
    registrationPayload(" novo-usuario ", "SenhaNova123!", "SenhaNova123!"),
    { login: "novo-usuario", senha: "SenhaNova123!" },
  );
  assert.throws(
    () => registrationPayload("novo-usuario", "SenhaNova123!", "diferente"),
    /iguais/,
  );
});
