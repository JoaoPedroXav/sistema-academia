CREATE OR REPLACE FUNCTION verificar_situacao_matricula(
    p_data_fim DATE,
    p_status VARCHAR
)
RETURNS VARCHAR
LANGUAGE plpgsql
AS $$
BEGIN
    IF UPPER(p_status) = 'CANCELADA' THEN
        RETURN 'CANCELADA';

    ELSIF UPPER(p_status) = 'INATIVA' THEN
        RETURN 'INATIVA';

    ELSIF UPPER(p_status) = 'ATIVA'
          AND p_data_fim < CURRENT_DATE THEN
        RETURN 'VENCIDA';

ELSE
        RETURN 'ATIVA';
END IF;
END;
$$;