package com.tasklist;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;

@CrossOrigin
@ResponseBody
@Controller
@SpringBootApplication
public class Main {
  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }
  /*
    Temos as seguntes funções a baixo:
    getTasks, getTask, putTask, postTask e deleteTask.

    Observação. Próximos passos seriam:
    - integração com biblioteca JSON;
    - implementação de bind tipo 'PreparedStatement.setString()';
  */
  // [GET] - Raiz do projeto
  @GetMapping("/")
    String index() {
      return "Esta é apenas a raiz do projeto. Acesse utilizando o método e o caminho correto. Mais informações: https://github.com/renanramoslinhares/tasklistjavaapi/";
    }

  // [GET] - getTasks retorna toda a lista da tabela 'tasks'. Por hora está retornando inteira;
  @GetMapping("/tasks/")
  String getTasks() {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT id, title, status, description FROM tasks WHERE removed_at IS NULL");
      ArrayList<String> output = new ArrayList<String>();      
      while (rs.next()) {
        String id = rs.getString("id");
        String title = rs.getString("title");
        String status = rs.getString("status");
        String description = rs.getString("description");
        output.add(
          "{\"id\":"+id
          +",\"title\":\""+title
          +"\",\"status\":\""+status
          +"\",\"description\":\""+description
          +"\"}"
        );
      }
      return output.toString();      
    } catch (Exception e) { 
      return "{\"error\":\"true\", \"message\":\""+e.getMessage()+"\"}";
    }
  }

  // [GET] - getTask retorna apenas um único registro da tabela 'tasks'. Sendo obrigatório o fornecimento do "id";
  @GetMapping("/task/{id}")
  String getTask(@PathVariable("id") String id) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(
        "SELECT title, status, description, created_at, updated_at, done_at FROM tasks WHERE id = "
        + id + " AND removed_at IS NULL LIMIT 1"
      );
      rs.next();
      String title = rs.getString("title");
      String status = rs.getString("status");
      String description = rs.getString("description");
      String created_at = rs.getString("created_at");
      String updated_at = rs.getString("updated_at");
      String done_at = rs.getString("done_at");

      return "{\"id\":"+id
        +",\"title\":\""+title
        +"\",\"status\":\""+status
        +"\",\"description\":\""+description
        +"\",\"created_at\":\""+created_at
        +"\",\"updated_at\":\""+updated_at
        +"\",\"done_at\":\""+done_at
        +"\"}";
    } catch (Exception e) {      
      return "{\"error\":\"true\", \"message\":\""+e.getMessage()+"\"}";
    }
  }

  // [PUT] - putTask edita um registro específico da tabela 'tasks'. É obrigatório o fornecimento do "id"; 'update_at' e 'done_at' não precisam ser setadas pelo front;
  @PutMapping("/task/{id}")
  String putTask(
    @PathVariable("id") String id,
    @RequestParam(name = "status", required = false) String status,
    @RequestParam(name = "title", required = false) String title,
    @RequestParam(name = "description", required = false) String description
  ) {

    String subStringSqlStatus = "";
    if(status.equals("done")) {
      subStringSqlStatus = "status='"+status+"', done_at=NOW(), ";
    } else if(status.equals("open")){
      subStringSqlStatus = "status='"+status+"', done_at=NULL, ";
    }

    String subStringSqlTitle= title != null ? "title='"+title+"', " : "";

    String subStringSqlDescription = description != null ? "description='"+description+"', " : "";

    String SQL = "UPDATE tasks SET "
      +subStringSqlStatus
      +subStringSqlTitle
      +subStringSqlDescription
      +"updated_at=NOW() WHERE id = "
      +id;

    try (
      Connection connection = dataSource.getConnection();
      PreparedStatement stmt = connection.prepareStatement(SQL)
    ) {
      stmt.executeUpdate();
      return "{\"success\":\"true\"}";
    } catch (Exception e) {      
      return "{\"error\":\"true\", \"message\":\""+e.getMessage()+"\"}";
    }
  }

  // [POST] - postTask cria um novo registro. Sendo obrigatorio apenas o fornecimento do 'title' atravez do corpo da requisição;
  @PostMapping("/task/")
  String postTask(@RequestParam("title") String title) {
    String SQL = "INSERT INTO tasks (title, status, created_at) VALUES ('"+title+"', 'open', NOW())";
    try (
      Connection connection = dataSource.getConnection();
      Statement stmt = connection.createStatement();
    ) {
      stmt.executeUpdate(SQL);
      return "{\"success\":\"true\"}";
    } catch (Exception e) {      
      return "{\"error\":\"true\", \"message\":\""+e.getMessage()+"\"}";
    }
  }

  // [DELETE] - deleteTask efetua um 'soft delete' no registro. Demais funções de GET devem restringir o acesso destes pelos 'client'.
  @DeleteMapping("/task/{id}")
  String deleteTask(@PathVariable("id") String id) {
    String SQL = "UPDATE tasks SET removed_at=NOW() WHERE id = "+id;
    try (
      Connection connection = dataSource.getConnection();
      PreparedStatement pstmt = connection.prepareStatement(SQL)
    ) {
      pstmt.executeUpdate();
      return "{\"success\":\"true\"}";
    } catch (Exception e) {      
      return "{\"error\":\"true\", \"message\":\""+e.getMessage()+"\"}";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
