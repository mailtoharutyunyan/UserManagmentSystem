package am.nova.user.management.demo.controller;

import am.nova.user.management.demo.exception.UserNotFoundException;
import am.nova.user.management.demo.payload.UserPayload;
import am.nova.user.management.demo.projection.UserProjection;
import am.nova.user.management.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectionFactory projectionFactory;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void createUser(@RequestBody UserPayload userPayload){
        userService.create(userPayload);
    }

    @GetMapping(value = "/{userId}")
    public UserProjection getUser(@PathVariable UUID userId){
        return projectionFactory.createProjection(UserProjection.class, userService.getUser(userId));
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFoundExceptionHandler(){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.message = "user with this id not found";
        errorMessage.status = "NOT_FOUND";
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    public static class ErrorMessage{
        public String message;
        public String status;
    }
}
