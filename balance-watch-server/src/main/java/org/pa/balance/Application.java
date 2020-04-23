package org.pa.balance;

import lombok.extern.slf4j.Slf4j;
import org.pa.balance.algo.entity.FrequencyStaticEntity;
import org.pa.balance.algo.repository.FrequencyStaticRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

@Slf4j
@EnableJpaRepositories
@EnableTransactionManagement
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Autowired
	ApplicationContext ctx;

	@Autowired
    FrequencyStaticRepo frequencyStaticRepo;

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
	webServerFactoryCustomizer() {
		return factory -> factory.setContextPath("/app");
	}

	@PostConstruct
	public void insertFakeData() {

		FrequencyStaticEntity f = new FrequencyStaticEntity();
		f.setAlgoTag("BI_WEEKLY");
		f.setAlgoSpec(null);
		f.setRefDateRequired(Boolean.FALSE);

		FrequencyStaticEntity ff = new FrequencyStaticEntity();
		ff.setAlgoTag("TUE_WEEKLY");
		ff.setAlgoSpec(null);
		ff.setRefDateRequired(Boolean.FALSE);

		frequencyStaticRepo.save(f);
		frequencyStaticRepo.save(ff);


	}



}
