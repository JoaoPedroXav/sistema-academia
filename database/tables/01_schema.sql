CREATE TABLE alunos (
                        id SERIAL PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        cpf VARCHAR(14) UNIQUE NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        telefone VARCHAR(20),
                        ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE planos (
                        id SERIAL PRIMARY KEY,
                        nome VARCHAR(50) NOT NULL,
                        descricao VARCHAR(255),
                        valor NUMERIC(10,2) NOT NULL,
                        duracao_dias INTEGER NOT NULL,
                        ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE matriculas (
                            id SERIAL PRIMARY KEY,
                            aluno_id INTEGER NOT NULL,
                            plano_id INTEGER NOT NULL,
                            data_inicio DATE NOT NULL,
                            data_fim DATE NOT NULL,
                            status VARCHAR(20) NOT NULL DEFAULT 'ATIVA',

                            CONSTRAINT fk_matricula_aluno
                                FOREIGN KEY (aluno_id)
                                    REFERENCES alunos(id),

                            CONSTRAINT fk_matricula_plano
                                FOREIGN KEY (plano_id)
                                    REFERENCES planos(id)
);