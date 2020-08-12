package org.pa.balance;

import lombok.extern.slf4j.Slf4j;
import org.pa.balance.algo.entity.FrequencyStaticEntity;
import org.pa.balance.algo.repository.FrequencyStaticRepo;
import org.pa.balance.frequency.entity.FrequencyConfigEntity;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	// TODO remove that once Cloud Database is online
	@PostConstruct
	public void insertFakeData() {

		FrequencyStaticEntity f = new FrequencyStaticEntity();
		f.setAlgoTag("BI_WEEKLY");
		f.setRefDateRequired(Boolean.TRUE);

		FrequencyConfigEntity fe = new FrequencyConfigEntity();
		fe.setAlgoSpec("TUESDAY");
		fe.setDescription("Tuesday - every two weeks");
		fe.setAlgoTag(f);

		List<FrequencyConfigEntity> l = new ArrayList<>(Arrays.asList(fe));
		f.setFrequencyConfigList(l);

		FrequencyStaticEntity ff = new FrequencyStaticEntity();
		ff.setAlgoTag("TUE_WEEKLY");
		ff.setRefDateRequired(Boolean.FALSE);

		FrequencyConfigEntity ffe = new FrequencyConfigEntity();
		ffe.setAlgoSpec("");
		ffe.setDescription("Tuesday - every week");
		ffe.setAlgoTag(ff);;

		List<FrequencyConfigEntity> ll = new ArrayList<>(Arrays.asList(ffe));
		ff.setFrequencyConfigList(ll);

		frequencyStaticRepo.save(f);
		frequencyStaticRepo.save(ff);


	}



}
