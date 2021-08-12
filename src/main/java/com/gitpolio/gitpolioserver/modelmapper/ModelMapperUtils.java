package com.gitpolio.gitpolioserver.modelmapper;

import lombok.Getter;
import org.modelmapper.ModelMapper;

public class ModelMapperUtils {
    @Getter
    private static final ModelMapper modelMapper = new ModelMapper();
}
