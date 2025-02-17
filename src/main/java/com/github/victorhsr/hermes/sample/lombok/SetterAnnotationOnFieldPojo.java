package com.github.victorhsr.hermes.sample.lombok;

import com.github.victorhsr.hermes.core.annotations.DSLRoot;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@DSLRoot
@EqualsAndHashCode
public class SetterAnnotationOnFieldPojo {

    @Setter
    private String someString;
    @Setter
    private Boolean someBoolean;
}
