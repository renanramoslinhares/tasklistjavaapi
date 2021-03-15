# TaskList - API
### Feito com Java e Spring

  ## Sobre
  Este projeto é parte de um desafio. Composto por:
  - Este backend. Aqui chamado apenas como API;
  - E o frontend. Que se encontra neste repositório: https://github.com/renanramoslinhares/tasklistfront

  Esta API oferece ao frontend as condições de gerenciar as tarefas, que são objeto do projeto;

  Temos as seguintes ações no controller `Main.java`:
  getTasks, getTask, putTask, postTask e deleteTask.

  ## Como consumir
  Para executar você deve enviar uma requisição para a URL da api: "https://tasklistjavaapi.herokuapp.com/";
  É necessário somar a ela o caminho e o método correto;
  Veja abaixo quais são eles.
  Exemplo: [GET] `http://tasklistjavaapi.herokuapp.com/task/1`.
  
  ## Requisições

  #### // [GET] '/tasks/'
  Retorna toda a lista da tabela 'tasks'.

  #### // [GET] '/task/{id}'
  Retorna apenas um único registro da tabela 'tasks'. Sendo obrigatório o fornecimento do "id";  

  #### // [PUT] '/task/{id}'
  Edita um registro específico da tabela 'tasks'. É obrigatório o fornecimento do "id"; 'update_at' e 'done_at' não precisam ser setadas pelo front;
  
  #### // [POST] '/task/'
  Cria um novo registro. Sendo obrigatorio apenas o fornecimento do 'title' atravez do corpo da requisição;

  #### // [DELETE] '/task/{id}'
  Efetua um 'soft delete' no registro.
  Demais funções de GET devem restringir o acesso destes pelos 'client'.
