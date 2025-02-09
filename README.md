Order Service API

📌 Sobre o Projeto

Este projeto consiste em uma API REST para gerenciamento de pedidos, utilizando Spring Boot, RabbitMQ, H2 Database, Swagger, Spring Security e RabbitMQ para comunicação assíncrona. O objetivo principal é processar 200 mil pedidos, evitando chamadas HTTP síncronas e garantindo escalabilidade através de filas do RabbitMQ.

🚀 Tecnologias Utilizadas

Spring Boot - Framework para facilitar o desenvolvimento

Spring Data JPA - Persistência de dados

Spring Security - Segurança e autenticação via JWT

RabbitMQ - Mensageria para evitar chamadas HTTP diretas

H2 Database - Banco de dados em memória para testes

Swagger - Documentação interativa da API

📜 Funcionalidades Principais

✔️ Cadastro, cálculo e listagem de pedidos
✔️ Comunicação assíncrona via RabbitMQ
✔️ API para geração de tokens JWT
✔️ Autenticação e autorização via Spring Security
✔️ Banco de dados H2 populado automaticamente ao iniciar
✔️ Integração com Swagger para documentação


🔑 Autenticação e Segurança

A API utiliza JWT (JSON Web Token) para autenticação.

📌 Login e geração do Token:

Para gerar um token de acesso, utilize a seguinte requisição:

curl -X POST 'http://localhost:8080/auth/login' \
  -H 'Content-Type: application/json' \
  -d '{"username": "admin", "password": "admin123"}'

Resposta esperada:

{
  "token": "eyJhbGciOiJIUzI1NiIsIn..."
}

Esse token deve ser utilizado para acessar rotas protegidas:

curl -X GET 'http://localhost:8080/orders?page=0&size=10' \
  -H 'Authorization: Bearer SEU_TOKEN_AQUI'

🛒 Processamento de Pedidos

Os pedidos são enviados para uma fila RabbitMQ ao serem criados.
Isso evita chamadas síncronas HTTP e melhora a escalabilidade.

rabbitTemplate.convertAndSend("orderQueue", orderMessage);

Uma vez na fila, um consumidor processa os pedidos assincronamente.

4️⃣ Acesse a documentação no Swagger

http://localhost:8080/swagger-ui/index.html

📌 Considerações Finais

🔹 O banco H2 já vem pré-populado ao iniciar a aplicação.
🔹 Usuário padrão: admin | Senha: admin123
🔹 RabbitMQ evita sobrecarga no sistema ao processar pedidos
🔹 JWT protege as rotas da API

