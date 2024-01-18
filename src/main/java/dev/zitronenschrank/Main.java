package dev.zitronenschrank;

import java.nio.charset.StandardCharsets;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.extensions.jackson.ParseJsons;
import org.apache.beam.sdk.io.mqtt.MqttIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Mean;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.PCollection;

import org.apache.logging.log4j.Logger;
import org.joda.time.Duration;
import org.apache.logging.log4j.LogManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.zitronenschrank.uplink.UplinkMessage;

public class Main {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(Main.class.getName());
        logger.info("Hello world!");

        Pipeline pipeline;
        PCollection<byte[]> mqttRecords;

        PipelineOptions options = PipelineOptionsFactory.create();
        // options.setRunner(FlinkRunner.class);
        pipeline = Pipeline.create(options);

        mqttRecords = pipeline.apply(MqttIO.read()
                .withConnectionConfiguration(MqttIO.ConnectionConfiguration.create("tcp://lorastuff:1883",
                        "application/+/device/70b3d5581000003d/event/up")));

        PCollection<UplinkMessage> uplinkRecords = mqttRecords.apply(ParDo.of(new ParseIntoUplinkMessage()));

        PCollection<Float> temperatureRecords = uplinkRecords.apply(ParDo.of(new UplinkToTemperature()));
        PCollection<Float> humidityRecords = uplinkRecords.apply(ParDo.of(new UplinkToHumidity()));

        PCollection<Double> tempMean = temperatureRecords
                .apply(Window.into(FixedWindows.of(Duration.standardMinutes(10))))
                // .apply(Combine.globally(Mean.<Float>of()).withoutDefaults())
                .apply(Mean.<Float>globally().withoutDefaults());

        PCollection<Double> humidityMean = humidityRecords
                .apply(Window.into(FixedWindows.of(Duration.standardMinutes(10))))
                // .apply(Combine.globally(Mean.<Float>of()).withoutDefaults())
                .apply(Mean.<Float>globally().withoutDefaults());

        tempMean.apply(ParDo.of(new WriteIntoLog("Mean Temperature")));
        humidityMean.apply(ParDo.of(new WriteIntoLog("Mean Temperature")));

        pipeline.run();
    }
}

class WriteIntoLog extends DoFn<Double, Void> {

    private final String name;

    public WriteIntoLog(String name) {
        this.name = name;
    }

    @ProcessElement
    public void processElement(@Element Double input) {
        Logger logger = LogManager.getLogger(WriteIntoLog.class.getName());

        logger.info(this.name + ": " + input);

    }
}

class ParseIntoUplinkMessage extends DoFn<byte[], UplinkMessage> {

    @ProcessElement
    public void processElement(@Element byte[] input, OutputReceiver<UplinkMessage> output) {
        // Logger logger = LogManager.getLogger(ParseIntoUplinkMessage.class.getName());

        String jsonString = new String(input, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            UplinkMessage data = objectMapper.readValue(jsonString, UplinkMessage.class);
            if (data.getObject() != null) {
                output.output(data);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}

class UplinkToTemperature extends DoFn<UplinkMessage, Float> {

    @ProcessElement
    public void processElement(@Element UplinkMessage input, OutputReceiver<Float> output) {
        Logger logger = LogManager.getLogger(UplinkToTemperature.class.getName());
        Float value = input.getObject().getTemperature();
        if (value != null) {
            output.output(value);
        }
        logger.info(value);
        logger.debug(value);
    }
}

class UplinkToHumidity extends DoFn<UplinkMessage, Float> {

    @ProcessElement
    public void processElement(@Element UplinkMessage input, OutputReceiver<Float> output) {
        Logger logger = LogManager.getLogger(UplinkToHumidity.class.getName());
        Float value = input.getObject().getHumidity();
        if (value != null) {
            output.output(value);
        }
        logger.info(value);
        logger.debug(value);
    }
}