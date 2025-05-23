DROP DATABASE IF EXISTS biblioteca_digital;
CREATE DATABASE biblioteca_digital;
USE biblioteca_digital;

CREATE TABLE categorias (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE autores (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    biografia TEXT
);

CREATE TABLE livros (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    editora VARCHAR(100) NOT NULL,
    anoPublicacao INT NOT NULL,
    genero VARCHAR(50) NOT NULL,
    numeroCopias INT NOT NULL,
    numeroCopiasDisponiveis INT DEFAULT 0,
    categoria_id BIGINT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE livro_autor (
    livro_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    PRIMARY KEY (livro_id, autor_id),
    FOREIGN KEY (livro_id) REFERENCES livros (id),
    FOREIGN KEY (autor_id) REFERENCES autores (id)
);

CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    dataRegistro DATE NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE emprestimos (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    livro_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    dataEmprestimo DATETIME DEFAULT CURRENT_TIMESTAMP,
    dataDevolucaoPrevista DATE NOT NULL,
    dataDevolucaoReal DATE,
    FOREIGN KEY (livro_id) REFERENCES livros (id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);

INSERT INTO categorias (nome) VALUES ('Romance');
INSERT INTO categorias (nome) VALUES ('Ficção Científica');
INSERT INTO categorias (nome) VALUES ('Mistério');


INSERT INTO autores (nome, biografia) VALUES ('Jane Austen', 'Escritora britânica conhecida por romances como "Orgulho e Preconceito".');
INSERT INTO autores (nome, biografia) VALUES ('Isaac Asimov', 'Escritor americano de ficção científica, autor de "Eu, Robô".');
INSERT INTO autores (nome, biografia) VALUES ('Agatha Christie', 'Escritora britânica, famosa por seus romances policiais.');


INSERT INTO livros (titulo, isbn, editora, anoPublicacao, genero, numeroCopias, numeroCopiasDisponiveis, categoria_id)
VALUES ('Orgulho e Preconceito', '978-0141439518', 'Penguin Classics', 1813, 'Romance', 3, 3, 1); -- Categoria 1: Romance
INSERT INTO livros (titulo, isbn, editora, anoPublicacao, genero, numeroCopias, numeroCopiasDisponiveis, categoria_id)
VALUES ('Eu, Robô', '978-0553294387', 'Bantam Books', 1950, 'Ficção Científica', 2, 2, 2); -- Categoria 2: Ficção Científica
INSERT INTO livros (titulo, isbn, editora, anoPublicacao, genero, numeroCopias, numeroCopiasDisponiveis, categoria_id)
VALUES ('Assassinato no Expresso Oriente', '978-0062073616', 'William Morrow Paperbacks', 1934, 'Mistério', 1, 1, 3); -- Categoria 3: Mistério


-- Jane Austen é a autora do livro 'Orgulho e Preconceito' (livro_id = 1, autor_id = 1)
INSERT INTO livro_autor (livro_id, autor_id) VALUES (1, 1);

-- Isaac Asimov é o autor do livro 'Eu, Robô' (livro_id = 2, autor_id = 2)
INSERT INTO livro_autor (livro_id, autor_id) VALUES (2, 2);

-- Agatha Christie é a autora do livro 'Assassinato no Expresso Oriente' (livro_id = 3, autor_id = 3)
INSERT INTO livro_autor (livro_id, autor_id) VALUES (3, 3);


INSERT INTO usuarios (nome, email, telefone, endereco, dataRegistro, status)
VALUES ('Maria Silva', 'maria.silva@email.com', '11-99999-8888', 'Rua A, 123', '2024-01-20', 'Ativo');
INSERT INTO usuarios (nome, email, telefone, endereco, dataRegistro, status)
VALUES ('João Souza', 'joao.souza@email.com', '21-98888-7777', 'Av. B, 456', '2024-02-15', 'Ativo');

-- Maria Silva (usuário 1) empresta 'Orgulho e Preconceito' (livro 1) em 2024-03-01, com devolução prevista para 2024-03-15
INSERT INTO emprestimos (livro_id, usuario_id, dataEmprestimo, dataDevolucaoPrevista)
VALUES (1, 1, '2024-03-01', '2024-03-15');

-- João Souza (usuário 2) empresta 'Eu, Robô' (livro 2) em 2024-03-05, com devolução prevista para 2024-03-20
INSERT INTO emprestimos (livro_id, usuario_id, dataEmprestimo, dataDevolucaoPrevista)
VALUES (2, 2, '2024-03-05', '2024-03-20');

SHOW TABLES;

DESCRIBE usuarios;
DESCRIBE livros;
DESCRIBE autores;
DESCRIBE categorias;
DESCRIBE emprestimos;

SELECT * FROM usuarios;
SELECT * FROM livros;
SELECT * FROM autores;
SELECT * FROM categorias;
SELECT * FROM emprestimos;

