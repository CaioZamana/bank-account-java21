Api documentation with Swagger: http://localhost:8080/swagger-ui/index.html


Querys:

Recuperar Detalhes da Conta Poupança com Informações da Conta:

* SELECT
a.id,
a.ACCOUNT_NUMBER,
a.ACCOUNT_HOLDER,
a.BALANCE,
a.CREATION_DATE,
sa.INTEREST_RATE
FROM
ACCOUNT a
JOIN
SAVING_ACCOUNT sa
ON
a.id = sa.id;
