package vook.server.poc.springdataredis.usecases;

import org.springframework.data.repository.CrudRepository;
import vook.server.poc.springdataredis.model.User;

public interface UserRepository extends CrudRepository<User, String> {
}
