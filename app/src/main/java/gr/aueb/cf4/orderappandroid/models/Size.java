package gr.aueb.cf4.orderappandroid.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Size {
    SMALL("Small"), MEDIUM("Medium"), LARGE("Large"), XL("Extra Large"), ONE_SIZE("One Size"),
    SIZE_110("Size_110"), SIZE_120("Size_120"), SIZE_130("Size_130"), SIZE_140("Size_140"), SIZE_145("Size_145"),
    SIZE_150("Size_150"), SIZE_155("Size_155"), SIZE_160("Size_160"), SIZE_165("Size_165"), SIZE_170("Size_170"),
    SIZE_175("Size_175"), SIZE_180("Size_180"), SIZE_185("Size_185"), SIZE_190("Size_190"), SIZE_195("Size_195"),
    SIZE_200("Size_200"), OZ_6("6_oz"), OZ_8("8_oz"), OZ_10("10_oz"), OZ_12("12_oz"), OZ_14("14_oz"),
    NULL("N/A");

    private final String displayName;

    Size(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Size fromDisplayName(String displayName) {
        for (Size size : values()) {
            if (size.displayName.equalsIgnoreCase(displayName)) {
                return size;
            }
        }
        throw new IllegalArgumentException("Unknown size: " + displayName);
    }
}
