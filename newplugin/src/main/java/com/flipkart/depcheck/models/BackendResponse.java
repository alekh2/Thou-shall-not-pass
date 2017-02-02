package com.flipkart.depcheck.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by prasanth.narra on 18/01/17.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackendResponse {
    private boolean hasNonWhiteListedDependencies;

    private List<Dependency> nonWhiteListedDependencies;
}
