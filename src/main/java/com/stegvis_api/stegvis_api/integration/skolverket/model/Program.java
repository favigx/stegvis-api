package com.stegvis_api.stegvis_api.integration.skolverket.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Program {

    private String code;
    private String name;
    private List<Orientation> orientations;

}
