package ru.mdm.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MdmAuditServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ru.mdm.audit.MdmAuditServiceApplication.class, args);
	}

}
