package org.apereo.cas;

import io.micronaut.runtime.Micronaut;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CasMicronautWebApplication {

    public static void main(String... args) {
        Micronaut.run(CasMicronautWebApplication.class);
    }
}
