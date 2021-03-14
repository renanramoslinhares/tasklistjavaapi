


# JAVA API - TaskList

  ## Como acessar
    No ar: "https://tasklistjavaapi.herokuapp.com/";

    Esta é uma api JAVA que oferece ao frontend a condições de gerenciar uma 'tasklist';

    Temos as seguntes ações a baixo:
    getTasks, getTask, putTask, postTask e deleteTask.

    Para executá-las é necessário acessar o endereço publicado acima somando a ele  o caminho e método correto;
    exemplo: [GET] http://tasklistjavaapi.herokuapp.com/task/5

## // [GET] '/tasks/'
    Retorna toda a lista da tabela 'tasks'.

## // [GET] '/task/{id}'
    Retorna apenas um único registro da tabela 'tasks'. Sendo obrigatório o fornecimento do "id";  

## // [PUT] '/task/{id}'
    Edita um registro específico da tabela 'tasks'. É obrigatório o fornecimento do "id"; 'update_at' e 'done_at' não precisam ser setadas pelo front;
  
## // [POST] '/task/'
    Cria um novo registro. Sendo obrigatorio apenas o fornecimento do 'title' atravez do corpo da requisição;

## // [DELETE] '/task/{id}'
    Efetua um 'soft delete' no registro.
    Demais funções de GET devem restringir o acesso destes pelos 'client'.
