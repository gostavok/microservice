package com.algaworks.example.resilience4j.produtos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;

@Configuration
public class CircuitBreakerLogConfig {

	private final Logger logger = LoggerFactory.getLogger(CircuitBreakerLogConfig.class); 
	
	@Bean
	public RegistryEventConsumer<CircuitBreaker> cbLog(){
		return new RegistryEventConsumer<CircuitBreaker>() {
			@Override
			public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
				entryAddedEvent.getAddedEntry().getEventPublisher().onStateTransition(event -> {
					logger.info(event.toString());
				});
			}

			@Override
			public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
				
			}

			@Override
			public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
				
			}
		};
	}
	
}
