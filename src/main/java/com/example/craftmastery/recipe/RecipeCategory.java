package com.example.craftmastery.recipe;

public enum RecipeCategory {
    MAGICAL("Magical"),
    TECHNICAL("Technical"),
    ORDINARY("Ordinary");

    private final String displayName;

    RecipeCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
