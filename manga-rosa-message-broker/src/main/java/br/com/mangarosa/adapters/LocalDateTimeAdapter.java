package br.com.mangarosa.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
// Importa classes da biblioteca Gson para personalizar como objetos são convertidos para JSON e vice-versa


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// Importa classes para tratar exceções de entrada/saída, trabalhar com data/hora e formatá-las


public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
// Declara a classe que extende TypeAdapter para manipular a conversão de LocalDateTime


    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    // Define um formatador padrão para as datas no formato ISO (padrão internacional de data e hora)

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
    // Sobrescreve o método write para converter LocalDateTime em string JSON

        out.value(value.format(formatter));
        // Escreve o valor do LocalDateTime no JSON formatado usando o DateTimeFormatter

    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
    // Sobrescreve o método read para converter string JSON de volta para LocalDateTime

        return LocalDateTime.parse(in.nextString(), formatter);
        // Lê a próxima string do JSON e a converte de volta para um objeto LocalDateTime usando o DateTimeFormatter

    }
}
