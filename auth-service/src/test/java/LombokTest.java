import com.ecommerce.platform.authservice.AuthServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = AuthServiceApplication.class)
public class LombokTest {
    @Test
    void testLogger() {
        log.info("Lombok logger is working!");
    }
}
