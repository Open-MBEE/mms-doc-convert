package org.openmbee.mmsdocconvert;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.openmbee")
@OpenAPIDefinition(
		info = @Info(
				title = "MMS Document Convert API",
				version = "0.0.1",
				description = "Documentation for MMS Document Convert API",
				license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.txt")
		),
		servers = {
		}
)
public class MmsDocConvertApplication {

	public static void main(String[] args) {
		SpringApplication.run(MmsDocConvertApplication.class, args);
	}

}
