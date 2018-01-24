package com.theopus.restservice.config;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Room;
import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleModule module = new SimpleModule("custom",
                Version.unknownVersion());
        module.addSerializer(Group.class, new GroupSerializer());
        module.addSerializer(Teacher.class, new TeacherSerializer());
        module.addSerializer(Room.class, new RoomSerializer());
        module.addSerializer(Subject.class, new SubjectSerializer());
        module.addDeserializer(LocalDate.class, new MyLocalDateDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    public static class GroupSerializer extends JsonSerializer<Group> {
        @Override
        public void serialize(Group value, JsonGenerator jgen,
                              SerializerProvider provider) throws IOException {
            if (value != null) {
                jgen.writeStartObject();
                jgen.writeStringField("id", value.getId().toString());
                jgen.writeStringField("name", value.getName());
                jgen.writeEndObject();
            }
        }
    }

    public static class TeacherSerializer extends JsonSerializer<Teacher> {
        @Override
        public void serialize(Teacher value, JsonGenerator jgen,
                              SerializerProvider provider) throws IOException {
            if (value != null) {
                jgen.writeStartObject();
                jgen.writeStringField("id", value.getId().toString());
                jgen.writeStringField("name", value.getName());
                jgen.writeEndObject();
            }
        }
    }

    public static class RoomSerializer extends JsonSerializer<Room> {
        @Override
        public void serialize(Room value, JsonGenerator jgen,
                              SerializerProvider provider) throws IOException {
            if (value != null) {
                jgen.writeStartObject();
                jgen.writeStringField("id", value.getId().toString());
                jgen.writeStringField("name", value.getName());
                jgen.writeEndObject();
            }
        }
    }

    public static class SubjectSerializer extends JsonSerializer<Subject> {
        @Override
        public void serialize(Subject value, JsonGenerator jgen,
                              SerializerProvider provider) throws IOException {
            if (value != null) {
                jgen.writeStartObject();
                jgen.writeStringField("id", value.getId().toString());
                jgen.writeStringField("name", value.getName());
                jgen.writeEndObject();
            }
        }
    }

    public static class MyLocalDateDeserializer extends JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return LocalDate.parse(p.getText());
        }
    }

    @Bean
    public DateTimeFormatConfiguration dateTimeFormatConfiguration() {
        return new DateTimeFormatConfiguration();
    }

    public static class DateTimeFormatConfiguration extends WebMvcConfigurerAdapter {

        @Override
        public void addFormatters(FormatterRegistry registry) {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-d"));
            registrar.registerFormatters(registry);
        }
    }

}
