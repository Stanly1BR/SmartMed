package br.com.SmartMed.consultas.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    /***
     * Classe de configuração que inicializará o ModelMapper e o disponibilizará como um Bean
     * no contexto da nossa aplicação.
     */
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
}
