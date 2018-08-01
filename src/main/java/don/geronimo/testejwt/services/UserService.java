package don.geronimo.testejwt.services;

import don.geronimo.testejwt.exceptions.UserNotFoundException;
import don.geronimo.testejwt.model.User;
import don.geronimo.testejwt.model.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Service
public class UserService {

    public User getUserByLoginAndSenha(String login, String senha) throws UserNotFoundException {
        User mock = new User("skfdjsjfkjsd", "Charles Bronson", "foo", "bar");
        if((mock.getLogin().equals(login)) && (mock.getSenha().equals(senha))) {
            return mock;
        }
        else{
            throw new UserNotFoundException();
        }
    }

    public User getUserById(String id) throws UserNotFoundException {
        if(id.equals("skfdjsjfkjsd")){
            return new User("skfdjsjfkjsd", "Charles Bronson", "foo", "bar");
        }
        else{
            throw new UserNotFoundException();
        }
    }
}
