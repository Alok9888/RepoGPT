package RepoGPT.backend.services;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import RepoGPT.backend.repository.UserRepository;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.transaction.annotation.Transactional;
import RepoGPT.backend.entity.User;
import java.util.UUID;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {
  public final UserRepository userRepository;
  public final TextEncryptor textEncryptor;

  @Transactional(readOnly = true)
  public User requiredById(long id){
    return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
  }

  public String decryptToken(User user) {
    return textEncryptor.decrypt(user.getAccessToken());
  }

  private static long toLong(Object value){
    if (value instanceof Number) {
      return ((Number) value).longValue();
    } else if (value instanceof String) {
      try {
        return Long.parseLong((String) value);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid value for long conversion: " + value);
      }
    } else {
      throw new IllegalArgumentException("Unsupported type for long conversion: " + value.getClass().getName());
    }
  }
}
