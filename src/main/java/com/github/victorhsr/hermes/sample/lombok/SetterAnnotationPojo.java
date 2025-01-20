package com.github.victorhsr.hermes.sample.lombok;

import com.github.victorhsr.hermes.core.annotations.DSLRoot;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Setter
@DSLRoot
@EqualsAndHashCode
public class SetterAnnotationPojo {

    private Boolean fieldWithoutAnnotation;
}
