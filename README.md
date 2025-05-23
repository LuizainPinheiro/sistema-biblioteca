📚 Sistema Integrado de Gestão de Livros para Biblioteca Digital
Este projeto consiste em um Sistema Integrado de Gestão de Livros para uma biblioteca municipal, desenvolvido com o objetivo de otimizar a administração do acervo por meio de operações CRUD (Cadastro, Consulta, Atualização e Deleção). A solução é estruturada em uma arquitetura Full-Stack, composta por um back-end em Java com Spring Boot e um front-end em React.js.

✨ Visão Geral
A aplicação visa modernizar a gestão bibliotecária, automatizando processos e melhorando a experiência de usuários e administradores.

Funcionalidades Implementadas
Gerenciamento de Livros: Permite o cadastro, listagem, atualização e exclusão de livros, incluindo detalhes como título, ISBN, autor(es), categoria, ano de publicação e número de cópias disponíveis.

Gerenciamento de Autores: Funcionalidades para cadastro e listagem de autores.

Gerenciamento de Categorias: Funcionalidades para cadastro e listagem de categorias de livros.

Gerenciamento de Usuários: Funcionalidades para cadastro e listagem de usuários da biblioteca.

Gerenciamento de Empréstimos: Controle de empréstimos e devoluções de livros.

Interface de Usuário: Um front-end intuitivo que facilita a interação com todas as funcionalidades do sistema.

🚀 Tecnologias Empregadas
O projeto utiliza um conjunto de tecnologias atuais para assegurar desempenho, manutenibilidade e escalabilidade.

Back-end (Project-API)
Linguagem: Java (versão 17 ou superior)

Framework: Spring Boot (versão 3.x)

Persistência de Dados: Spring Data JPA e Hibernate

Banco de Dados: MySQL

Ambiente de Desenvolvimento (IDE): IntelliJ IDEA

Gerenciamento de Dependências: Maven

Estilo Arquitetural: API RESTful, organizada em camadas (Model, Repository, Service, Controller)

Front-end (project-frontend)
Biblioteca JavaScript: React.js

Ferramenta de Build: Vite

Gerenciamento de Rotas: react-router-dom

Requisições HTTP: axios

Estilização: CSS Puro

Ambiente de Desenvolvimento (IDE): VS Code

Gerenciador de Pacotes: npm

🏗️ Estrutura do Projeto
A organização do código reflete a arquitetura modular, com diretórios dedicados a cada componente principal.

├── Project-API/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── br/com/fecaf/
│   │   │   │       ├── autor/
│   │   │   │       ├── categoria/
│   │   │   │       ├── emprestimo/
│   │   │   │       │   ├── controller/
│   │   │   │       │   ├── dto/
│   │   │   │       │   ├── model/
│   │   │   │       │   ├── repository/
│   │   │   │       │   └── service/
│   │   │   │       ├── livro/
│   │   │   │       └── usuario/
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── database/
│   │   │           └── model.sql
│   ├── pom.xml
│   └── ... (outros arquivos e pastas do Maven/IDE)
│
└── project-frontend/
    ├── public/
    ├── src/
    │   ├── components/
    │   ├── pages/
    │   ├── services/
    │   ├── App.jsx
    │   └── index.css
    ├── package.json
    └── ... (outros arquivos de configuração do React/Vite)

⚙️ Instruções para Execução Local
Para configurar e executar o projeto em um ambiente local, siga os passos detalhados abaixo.

Pré-requisitos
Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

JDK (Java Development Kit) versão 17 ou superior

Maven

Node.js (versão LTS recomendada)

npm (Node Package Manager)

MySQL Server

Git

1. Configuração do Banco de Dados (MySQL)
Verifique se o MySQL Server está em execução.

Utilize um cliente MySQL (e.g., MySQL Workbench, linha de comando) para criar o banco de dados. O nome padrão esperado pela aplicação é biblioteca_digital.

CREATE DATABASE IF NOT EXISTS biblioteca_digital;
USE biblioteca_digital;

Execute os scripts SQL de criação de tabelas e, se aplicável, de inserção de dados iniciais, localizados em Project-API/src/main/resources/database/model.sql.

2. Execução do Back-end (Project-API)
Navegue até o diretório raiz do módulo de back-end (Project-API) via terminal:

cd Project-API

Edite o arquivo src/main/resources/application.properties para configurar as credenciais do seu banco de dados MySQL:

spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_digital
spring.datasource.username=seu_usuario_mysql
spring.datasource.password=sua_senha_mysql
spring.jpa.hibernate.ddl-auto=update # Ou 'create' para criação automática das tabelas na primeira execução
spring.jpa.show-sql=true
server.port=8080 # Porta padrão da API

Compile e inicie a aplicação Spring Boot utilizando Maven:

mvn clean install
mvn spring-boot:run

A API estará acessível em http://localhost:8080/api/{recurso}.

3. Execução do Front-end (project-frontend)
Abra um novo terminal e navegue até o diretório raiz do módulo de front-end (project-frontend):

cd project-frontend

Instale as dependências do projeto:

npm install

Inicie a aplicação React:

npm run dev

O front-end estará disponível em http://localhost:5173 (ou na porta indicada pelo Vite no terminal).

Obrigada pela atenção!
