package me.itzg.batts.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXB;

import me.itzg.batts.domain.BatteryTypes;
import me.itzg.batts.service.ConfigException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class BatteryTypesLoader {
	@Autowired
	ResourceLoader resourceLoader;

	@Bean
	public BatteryTypes batteryTypes() throws IOException {
		Resource resource = resourceLoader
				.getResource("classpath:battery-types.xml");
		if (!resource.exists()) {
			throw new ConfigException("Could not find battery-types.xml");
		}

		final InputStream inputStream = resource.getInputStream();
		try {
			return JAXB.unmarshal(inputStream,
					BatteryTypes.class);
		} finally {
			inputStream.close();
		}
	}
}
