package br.com.projeto.demoparkapi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.projeto.demoparkapi.web.dto.usuarioDto.UsuarioPasswordDto;
import br.com.projeto.demoparkapi.web.dto.usuarioDto.UsuarioRequestDTO;
import br.com.projeto.demoparkapi.web.dto.usuarioDto.UsuarioResponseDTO;
import br.com.projeto.demoparkapi.web.exceptionHandler.ErrorMessage;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.psql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.psql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUsuario_ComUsernameEPasswordValidos_RetonarUsuarioCriadoComStatus201() {
        UsuarioResponseDTO responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDTO("test@email.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.id()).isNotNull();
        Assertions.assertThat(responseBody.username()).isEqualTo("test@email.com");
        Assertions.assertThat(responseBody.role()).isEqualTo("CLIENTE");
    }

    @Test
    public void createUsuario_ComUsernameInvalido_RetonarErrorMessageStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDTO("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDTO("test.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDTO("test@email.", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComPasswordInvalido_RetornarErroMessageStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDTO("test@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDTO("test@email.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDTO("test@email.com", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUsuario_ComUsernameRepetido_RetornarErrorMessage409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDTO("ana@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void buscarUsuario_ComIdExistente_RetornarUsuarioComStatus200() {

        UsuarioResponseDTO responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.id()).isEqualTo(100);
        Assertions.assertThat(responseBody.username()).isEqualTo("ana@email.com");
        Assertions.assertThat(responseBody.role()).isEqualTo("ADMIN");
    }

    @Test
    public void buscarUsuario_ComIdInexistente_RetornarErrorMessageStatus404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/0")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void editarPassword_ComIdEDadosValidos_RetornarStatus204() {
        testClient
                .put()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioPasswordDto("123456", "101010", "101010"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void editarPassword_ComCamposInvalidos_RetornarErrorMessageStatus422() {
        ErrorMessage responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("12345", "101010", "101010"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("1234567", "101010", "101010"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("123456", "10101", "101010"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("123456", "10101", "101010"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("123456", "1010101", "101010"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("123456", "101010", "10101"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("123456", "101010", "1010101"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void editarPassword_ComSenhasInvalidas_RetornarErroMessageStatus403() {
        ErrorMessage responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("113456", "101010", "101010"))
            .exchange()
            .expectStatus().isEqualTo(403)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("123456", "101011", "101010"))
            .exchange()
            .expectStatus().isEqualTo(403)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/100")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("123456", "101010", "101011"))
            .exchange()
            .expectStatus().isEqualTo(403)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void editarPassword_ComIdDoUsuarioInexistente_RetornarErrorMessage404() {
        ErrorMessage responseBody = testClient
            .put()
            .uri("/api/v1/usuarios/0")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioPasswordDto("123456", "101010", "101010"))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    
}
