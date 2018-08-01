package don.geronimo.testejwt.controller;

import don.geronimo.testejwt.exceptions.UserNotFoundException;
import don.geronimo.testejwt.model.User;
import don.geronimo.testejwt.model.UserDTO;
import don.geronimo.testejwt.services.UserService;
import don.geronimo.testejwt.utils.MensagemErro;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.*;

@RestController
public class LoginController {
    @Value("${sistema.secret}")
    private String jwtSecret;
    private UserService userService;
    @Autowired
    public LoginController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/secure/userdata")
    public ResponseEntity<?> getUserData(final HttpServletRequest request) throws UserNotFoundException {
        return ResponseEntity.ok( userService.getUserById((String) request.getAttribute("iduser")) );
    }
    @PostMapping("/user")
    public ResponseEntity<?> getToken(@RequestBody UserDTO usu)throws UserNotFoundException{
        //se o usu nao for o usu com a senha certa explode. Se for, faz o jwt e retorna.
        User user = userService.getUserByLoginAndSenha(usu.getLogin(), usu.getSenha());
        String token =
                Jwts.builder()
                        .setSubject("")
                        .claim("iduser", user.getId())
                        .setIssuedAt(new Date())
                        .signWith(SignatureAlgorithm.HS256, jwtSecret)
                        .compact();

        Map<String,Object> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<MensagemErro> usuarioNaoEncontradoExceptionHandler(Exception ex) {
        MensagemErro erro = new MensagemErro(HttpStatus.FORBIDDEN, "Usuario não encontrado", ex);
        return new ResponseEntity<MensagemErro>(erro, HttpStatus.FORBIDDEN);
    }
}
