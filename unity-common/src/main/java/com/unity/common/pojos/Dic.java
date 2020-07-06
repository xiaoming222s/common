package com.unity.common.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
public class Dic {
    private Long id;
    private String name;
    private String gradationCode;
    private Long idParent;
    private String dic;
    private String targetId;
}
