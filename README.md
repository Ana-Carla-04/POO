Gerador de Provas - Projeto Acadêmico
O Gerador de Provas é uma aplicação desktop desenvolvida em Java com interface gráfica JavaFX, criada para facilitar a vida de professores na criação e gerenciamento de avaliações acadêmicas. O sistema permite o cadastro completo de disciplinas, com nome e código, e oferece suporte a três tipos de questões: múltipla escolha, verdadeiro/falso e discursivas, cada uma podendo ser classificada por nível de dificuldade (fácil, médio ou difícil) e associada a uma disciplina e assunto específicos.

A partir desse banco de questões, o professor pode gerar provas personalizadas, selecionando disciplinas, assuntos e a quantidade desejada de questões por nível de dificuldade, com a opção de exportar o resultado em formato PDF com formatação profissional. O sistema também conta com uma poderosa ferramenta de busca, que permite localizar provas e questões por meio de filtros como disciplina, assunto, dificuldade e até mesmo semestre letivo, extraído automaticamente da data de criação das provas.

O projeto foi desenvolvido seguindo boas práticas de engenharia de software, como os padrões de projeto DAO, Service Layer, Observer e MVC, garantindo uma arquitetura limpa, modular e de fácil manutenção. Utiliza MySQL como banco de dados relacional e iText para geração de PDFs, sendo gerenciado pelo Maven. A aplicação é voltada para professores da UFERSA e demais instituições de ensino que buscam uma solução prática e eficiente para o dia a dia acadêmico.

Aqui são apenas lagumas telas do projeto:
<img width="1365" height="704" alt="imageLogin" src="https://github.com/user-attachments/assets/2af1a0f7-3e1f-487b-ad00-2fe19bff4728" />
<img width="1364" height="701" alt="imageDisciplinas" src="https://github.com/user-attachments/assets/a8f6bb36-d95c-4042-913a-bbf12621d5fd" />
<img width="1365" height="706" alt="imageQuestões" src="https://github.com/user-attachments/assets/93efdf90-f61a-4051-ac7b-bf3072c13621" />
<img width="1363" height="700" alt="imageBuscar" src="https://github.com/user-attachments/assets/e319c389-f2c5-4359-a8e2-37ba93b9370f" />
<img width="1363" height="701" alt="imageGeraProva" src="https://github.com/user-attachments/assets/fc7ac1e6-f31f-4f5d-95b1-57619d5a97eb" />
<img width="1364" height="701" alt="imageProva" src="https://github.com/user-attachments/assets/fdc2717a-24d3-4a34-b934-d3bb1d901cbc" />
<img width="1364" height="703" alt="imageRelatorio" src="https://github.com/user-attachments/assets/ee88cc81-41e0-4515-84f0-e37004716f2f" />
<img width="1365" height="702" alt="imageProvaSalva" src="https://github.com/user-attachments/assets/964dc7a0-b9c9-4607-8c8f-b1e6ab39533d" />

Aqui é a imagem da nossa UML:
<img width="5301" height="2946" alt="DiagramadeClassesPOO-Equipe05 drawio1" src="https://github.com/user-attachments/assets/d737e7fb-1ec6-4678-a147-f69c19fe22c0" />


Para recriar o nosso banco de dados, tem esse escript aqui:

DROP DATABASE IF EXISTS poo;
CREATE DATABASE poo;
USE poo;

CREATE TABLE professor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE disciplina (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    codigo VARCHAR(20) NOT NULL,
    professor_id INT NOT NULL,
    FOREIGN KEY (professor_id) REFERENCES professor(id) ON DELETE CASCADE
);

CREATE TABLE assunto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    disciplina_id INT NOT NULL,
    FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
);

CREATE TABLE questao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    enunciado TEXT NOT NULL,
    assunto VARCHAR(150) NULL,
    nivel INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    disciplina_id INT NOT NULL,
    FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
);

CREATE TABLE alternativa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    texto_alternativa TEXT NOT NULL,
    questao_id INT NOT NULL,
    verdadeira BOOLEAN NULL,
    respostaDiscursiva VARCHAR(2000),
    FOREIGN KEY (questao_id) REFERENCES questao(id) ON DELETE CASCADE
);

CREATE TABLE prova (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    instituicao VARCHAR(150) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    disciplina_id INT NOT NULL,
    professor VARCHAR(255),
    FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
);

CREATE TABLE prova_questao (
    prova_id INT NOT NULL,
    questao_id INT NOT NULL,
    PRIMARY KEY (prova_id, questao_id),
    FOREIGN KEY (prova_id) REFERENCES prova(id) ON DELETE CASCADE,
    FOREIGN KEY (questao_id) REFERENCES questao(id) ON DELETE CASCADE
);



