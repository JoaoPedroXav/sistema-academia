CREATE OR REPLACE PROCEDURE renovar_matricula(
    p_matricula_id INTEGER,
    p_nova_data_fim DATE
)
LANGUAGE plpgsql
AS $$
BEGIN

UPDATE matriculas
SET
    data_fim = p_nova_data_fim,
    status = 'ATIVA'
WHERE id = p_matricula_id;

IF NOT FOUND THEN
        RAISE EXCEPTION
            'Matrícula % não encontrada.',
            p_matricula_id;
END IF;

END;
$$;