CREATE OR REPLACE VIEW vw_matriculas_detalhadas AS
SELECT
    m.id AS matricula_id,
    a.id AS aluno_id,
    a.nome AS aluno,
    a.cpf,
    a.email,
    a.telefone,
    p.id AS plano_id,
    p.nome AS plano,
    p.valor,
    p.duracao_dias,
    m.data_inicio,
    m.data_fim,
    m.status,

    verificar_situacao_matricula(
            m.data_fim,
            m.status
    ) AS situacao

FROM matriculas m

         INNER JOIN alunos a
                    ON a.id = m.aluno_id

         INNER JOIN planos p
                    ON p.id = m.plano_id;