package vook.server.poc.springdataredis.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.poc.springdataredis.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UseRedisRepository {

    private final UserRepository userRepository;
    private final TimeToLiveSupplier timeToLiveSupplier;

    public User createUser(String id, List<String> accessVocabularies) {
        return userRepository.save(new User(id, accessVocabularies, timeToLiveSupplier.get()));
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
