# DailyMood – Diário de Humor

## Alunos do Grupo:
  - Gabriel Paes Duarte Baltazar (RGA: 2023.1906.021-6)
  - Pedro Silva Soledade (RGA: 2023.1906.044-5)
  - Maria Fernanda Colombo (RGA: 2023.1906.011-9)

## Visão Geral:
  O DailyMood é um aplicativo mobile que permite que os usuários registrem diariamente seu humor e os principais acontecimentos do dia. O app usa essas informações para gerar gráficos sobre o bem-estar emocional do usuário ao longo do tempo.

## Papéis e Uusários:
  ### Usuário comum
  - Cadastra-se com nome, e-mail, senha e uma foto de perfil;
  - Registra seu humor diário (através de uma escala e emojis);
  - Escreve um resumo do dia (opcional);
  - Visualiza um gráfico de humor por semana/mês;

## Requisitos Funcionais de Software (RF):
  ### Funcionalidades do Aplicativo
  **RFS01.** O sistema deve permitir que o usuário se cadastre, informando nome completo, e-mail, senha e adicionando uma foto de perfil.
  
  **RFS02.** O sistema deve validar se o e-mail informado no cadastro já está em uso.
  
  **RFS03.** O sistema deve armazenar a senha utilizando hash criptográfico (ex: SHA-256).
  
  **RFS04.** O sistema deve permitir login com e-mail e senha previamente cadastrados.
  
  **RFS05.** O sistema deve exibir mensagem de erro para tentativas de login inválidas.
  
  **RFS06.** O sistema deve impedir acesso ao app por usuários não autenticados.
  
  **RFS07.** O sistema deve permitir ao usuário registrar diariamente seu humor, selecionando uma escala de 1 a 5.
  
  **RFS08.** O sistema deve permitir a seleção de um emoji representando o humor diário.
  
  **RFS09.** O sistema deve permitir que o usuário adicione uma descrição textual opcional ao registro diário.
  
  **RFS10.** O sistema deve permitir que o usuário visualize o histórico de registros anteriores.
  
  **RFS11.** O sistema deve impedir o registro de mais de um humor por dia.
  
  **RFS12.** O sistema deve permitir que o usuário edite seu perfil (nome, e-mail, foto).
  
  **RFS13.** O sistema deve exibir uma notificação diária lembrando o usuário de registrar seu humor.
  

  ### Processamento
  **RFS14.** O sistema deve armazenar os registros de humor localmente (SQLite), incluindo data, escala, emoji e descrição.
  
  **RFS15.** O sistema deve gerar gráficos semanais e mensais (ex: linha, barra ou pizza) com base nos registros do usuário.
  
  **RFS16.** O sistema deve calcular a média do humor por período (semana/mês).
  
  **RFS17.** O sistema deve tratar erros como divisão por zero, entradas nulas e formatos inválidos.


  ### Entradas Necessárias
  **RFS18.** Nome completo, e-mail e senha para cadastro.
  
  **RFS19.** Foto do usuário (opcional no cadastro, mas recomendada).
  
  **RFS20.** Escala de humor (1 a 5).
  
  **RFS21.** Emoji correspondente ao humor.
  
  **RFS22.** Texto descritivo do dia (opcional).
  

  ### Saídas
  **RFS23.** Exibição de lista com o histórico de registros de humor, ordenados por data.
  
  **RFS24.** Gráficos semanais e mensais de humor do usuário.
  
  **RFS25.** Mensagens de erro em casos de autenticação inválida, dados incorretos ou campos obrigatórios vazios.
  
  **RFS26.** Notificações push diárias para lembrar o usuário de preencher seu registro.
