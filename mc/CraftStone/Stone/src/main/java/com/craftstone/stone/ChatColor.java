package com.craftstone.stone;

/**
 * Enum used for coloring strings
 * @author kmate
 *
 */
public enum ChatColor {
    BLACK('0', 0x00),
    DARK_BLUE('1', 0x1),
    DARK_GREEN('2', 0x2),
    DARK_AQUA('3', 0x3),
    DARK_RED('4', 0x4),
    DARK_PURPLE('5', 0x5),
    GOLD('6', 0x6),
    GRAY('7', 0x7),
    DARK_GRAY('8', 0x8),
    BLUE('9', 0x9),
    GREEN('a', 0xA),
    AQUA('b', 0xB),
    RED('c', 0xC),
    LIGHT_PURPLE('d', 0xD),
    YELLOW('e', 0xE),
    WHITE('f', 0xF),
    MAGIC('k', 0x10, true),
    BOLD('l', 0x11, true),
    STRIKETHROUGH('m', 0x12, true),
    ITALIC('o', 0x14, true),
    RESET('r', 0x15);

    private char code;
    private boolean isFormating;

    static final char PREF_CHAR = '\u00A7';
    
    private ChatColor(char charCode, int decAsciiCode) {
        this(charCode, decAsciiCode, false);
    }

    private ChatColor(char charCode, int decAsciiCode, boolean isformatValue) {
        this.code = charCode;
        this.isFormating = isformatValue;
    }

    public char getChar() {
        return code;
    }

    @Override
    public String toString() {
        return ((new StringBuilder().append(PREF_CHAR).append(code)).toString());
    }

    public boolean isItAFormating() {
        return isFormating;
    }

    public boolean isItColor() {
        if (!isFormating && this != RESET) {
            return true;
        } else {
            return false;
        }
    }
}
