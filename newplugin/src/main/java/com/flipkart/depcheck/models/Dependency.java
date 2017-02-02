package com.flipkart.depcheck.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by prasanth.narra on 11/01/17.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dependency {
    private String vendor,product,version,type;

    @Override
    public String toString(){
        return vendor+product+version+type;
    }
}
