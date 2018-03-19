package sn.dialibah.kiss.webapi.common.configurations.security;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.google.common.collect.Lists.newArrayList;

/**
 * by osow on 15/11/17.
 * for kiss-api
 */
@Configuration
public class DozerConfiguration {
	@Bean
	public DozerBeanMapper getDozeBeanMapper() {
		return new DozerBeanMapper(newArrayList("dozer/global.xml"));
	}
}
