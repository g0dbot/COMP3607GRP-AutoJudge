package com.hado90.config.style;

/**
 * Enum representing style-related configuration keys.
 * Each key corresponds to a specific aspect of the application's style,
 * such as text colors, background colors, font families, and sizes.
 */
public enum StyleKey {
    // Text-related color configurations
    TEXT_MAIN_COLOR,
    TEXT_LABEL_COLOR,
    TEXT_DISABLED_COLOR,

    // Background color configurations
    BG_MAIN_COLOR,
    BG_SECONDARY_COLOR,
    BG_DISABLED_COLOR,

    // Primary color shades
    PRIMARY_COLOR_SHADE_MAIN,
    PRIMARY_COLOR_SHADE_ACTIVE,
    PRIMARY_COLOR_SHADE_DISABLED,

    // Secondary color shades
    SECONDARY_COLOR_SHADE_MAIN,
    SECONDARY_COLOR_SHADE_ACTIVE,
    SECONDARY_COLOR_SHADE_DISABLED,

    // Tertiary color shades
    TERTIARY_COLOR_SHADE_MAIN,
    TERTIARY_COLOR_SHADE_ACTIVE,
    TERTIARY_COLOR_SHADE_DISABLED,

    // Emotion-related color configurations (negative, positive, neutral)
    EMO_COLOR_NEG_SHADE_MAIN,
    EMO_COLOR_NEG_SHADE_ACTIVE,
    EMO_COLOR_NEG_SHADE_DISABLED,
    EMO_COLOR_POS_SHADE_MAIN,
    EMO_COLOR_POS_SHADE_ACTIVE,
    EMO_COLOR_POS_SHADE_DISABLED,
    EMO_COLOR_NEU_SHADE_MAIN,
    EMO_COLOR_NEU_SHADE_ACTIVE,
    EMO_COLOR_NEU_SHADE_DISABLED,

    // Font family configurations
    PRIMARY_FONT_FAMILY,
    SECONDARY_FONT_FAMILY,

    // Size-related configurations for various elements
    SIZE_XS1, SIZE_XS2, SIZE_XS3,
    SIZE_S1, SIZE_S2, SIZE_S3,
    SIZE_M1, SIZE_M2, SIZE_M3,
    SIZE_L1, SIZE_L2, SIZE_L3,
    SIZE_XL1, SIZE_XL2, SIZE_XL3,

    // Window size configurations
    SIZE_WIN_WIDTH_S,
    SIZE_WIN_WIDTH_M,
    SIZE_WIN_WIDTH_L,
    SIZE_WIN_HEIGHT_S,
    SIZE_WIN_HEIGHT_M,
    SIZE_WIN_HEIGHT_L,
}