package com.aadhikat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;

@JsonRootName("FakeProducer")
@Data
@Builder
public class FakeProducerDTO {

    @JsonProperty("id")
    private String id;

}
