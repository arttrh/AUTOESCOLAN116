-- Remove somente a conta de demonstração que ainda usa a senha pública original.
-- ADMIN_LOGIN e ADMIN_PASSWORD criam uma conta segura na inicialização.
DELETE FROM usuarios WHERE login = 'admin' AND senha = '$2a$10$sl97w4QPplDaaWzfxn5.ButfhFpOMMisCV/HHMOPjrSOMeq5YQfwK';
