# 📃 Requisitos do Sistema


## 🧑‍💼 Regras de Negócio (RN)

1. Apenas consultas com status 'REALIZADA' são consideradas em relatórios de faturamento.
2. O valor da consulta é 50% do valor de referência do médico se o paciente usar um convênio.
3. Apenas consultas com status 'AGENDADA' podem ser canceladas.
4. O motivo do cancelamento é obrigatório e deve ser salvo nas observações.
5. A nova data e hora para reagendar não podem ser anteriores ao momento atual.
6. Não pode haver outra consulta já marcada para o médico no novo horário.
7. Somente pacientes ativos podem ter o histórico de consultas visualizado.
8. Apenas médicos ativos podem ter a agenda consultada.
9. O recepcionista deve estar ativo e não bloqueado para cadastrar uma consulta.
10. O e-mail de cada usuário deve ser único.
11. A senha deve ter no mínimo 8 caracteres.

## ✅ Requisitos Funcionais (RF)

1. Agendamento de consultas online.
2. Consulta de valores de consulta e pagamento direto pelo app.
3. Agrupamento de médicos favoritos.
4. Visualização de agendamentos pendentes.
5. Acesso ao histórico de consultas.
6. Geração de relatório de faturamento por período.
7. Geração de ranking de médicos com mais atendimentos no mês.
8. Agendamento automático de consultas com base em horários disponíveis.
9. Listagem de recepcionistas com filtro por status e data de admissão.
10. Cancelamento justificado de consultas.
11. Reagendamento de consultas com validação de conflitos.
12. Cadastro de consulta por recepcionista.
13. Cadastro seguro de usuários com criptografia de senha.
14. Autenticação de usuário com verificação segura de senha.

## 📊 Requisitos Não Funcionais (RNF)

1. O app deve ter uma interface intuitiva.
2. O sistema deve ter facilidade de navegação.
3. As informações devem ser claras.
4. As tarefas devem ser executadas rapidamente.
5. O app deve fornecer feedback visual e mensagens claras.
6. A interface deve evitar "quebras de expectativa".
