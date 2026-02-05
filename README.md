# Aplicação de Envio de E-mails com Spring Boot

## 📌 Visão Geral

Sistema de envio de e-mails utilizando sendgrid

---

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Web
- Spring Mail
- Maven

---

## ⚙️ Configuração

As configurações de e-mail devem ser definidas no arquivo:

```
src/main/resources/application.properties
```

Exemplo:

```properties
sendgrid.api.key=SUA_API_KEY
sendgrid.sender.email=EMAIL_SENDGRID
sendgrid.receiver.email=EMAIL_SETOR
```

---

## 📡 Exemplo de Requisição

```http
POST /email/enviar
Content-Type: application/json
```

```json
{
  "destinatario": "usuario@dominio.com",
  "assunto": "Solicitação Recebida",
  "nome": "João Silva",
  "setor": "TI",
  "template": "solicitante_modelo_1"
}
```

---

## ▶️ Como Executar

1. Clone o repositório
2. Configure o `application.properties`
3. Execute o comando:

```bash
mvn spring-boot:run
```

4. A aplicação ficará disponível em:

```
http://localhost:8080
```

---

## 📜 Licença

Projeto de uso interno / educacional.
