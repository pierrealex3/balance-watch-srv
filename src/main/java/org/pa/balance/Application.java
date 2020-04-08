package org.pa.balance;

import org.pa.balance.model.*;
import org.pa.balance.service.TransactionTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.math.BigDecimal;
import java.util.Arrays;

@EnableJpaRepositories
@EnableTransactionManagement
@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Autowired
	ApplicationContext ctx;

	@Autowired
	TransactionTemplateService transactionTemplateService;

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
	webServerFactoryCustomizer() {
		return factory -> factory.setContextPath("/app");
	}


//	@PostConstruct
//	public void inspectData() {
//
//		for (TransactionTemplate transactionTemplate : transactionTemplateService.fetchAll()) {
//
//			List<Frequency> lf = transactionTemplate.getFrequencyList();
//			if (lf.isEmpty())
//				System.out.println("empty");
//
//		}
//
//
//
//	}

	//@PostConstruct
	public void insertFakeData() {
		TransactionBoardRepo tbrepo = ctx.getBean(TransactionBoardRepo.class);

		TransactionBoard tb1 = new TransactionBoard();
		tb1.setAcctId("067827");
		tb1.setMonth(4);
		tb1.setYear(2019);
		tb1.setStartAmt(new BigDecimal("3500.50"));

		TransactionBoard tb2 = new TransactionBoard();
		tb2.setAcctId("067827");
		tb2.setMonth(5);
		tb2.setYear(2019);
		tb2.setStartAmt(new BigDecimal("1200.00"));

		tbrepo.save(tb1);
		tbrepo.save(tb2);

		FrequencyRepo frepo = ctx.getBean(FrequencyRepo.class);

		Frequency f1 = new Frequency();
		f1.setAlgo("WEEKLY_FRI");
		f1.setDescription("Kindergarten");

		Frequency f2 = new Frequency();
		f2.setAlgo("MONTH_LAST_DAY");
		f2.setDescription("Kindergarten xtra");

		//frepo.save(f1);
		//frepo.save(f2);

		TransactionTemplate tt = new TransactionTemplate();
		tt.setAmount(new BigDecimal("400.00"));
		tt.setType("magic mountain kindergarten");
		tt.setWay(TransactionWay.DEBIT);
		tt.getFrequencyList().addAll(Arrays.asList(f1, f2));

		f1.setTransactionTemplate(tt);
		f2.setTransactionTemplate(tt);

		TransactionTemplateRepo ttrepo = ctx.getBean(TransactionTemplateRepo.class);
		ttrepo.save(tt);

		TransactionTemplate tt2 = new TransactionTemplate();
		tt2.setAmount(new BigDecimal("198.00"));
		tt2.setType("jeep");
		tt2.setWay(TransactionWay.DEBIT);

		ttrepo.save(tt2);


	}



}
