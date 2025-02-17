package com.github.victorhsr.hermes.sample.lombok;

import com.github.victorhsr.hermes.core.annotations.DSLRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@DSLRoot
@EqualsAndHashCode
public class DataAnnotationPojo {

    private String foo;
    private Boolean bar;
}
