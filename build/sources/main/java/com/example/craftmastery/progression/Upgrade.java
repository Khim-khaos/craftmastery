package com.example.craftmastery.progression;

import java.util.function.Consumer;

public class Upgrade {
    private String id;
    private String name;
    private int requiredLevel;
    private Consumer<PlayerProgression> effect;

    public Upgrade(String id, String name, int requiredLevel, Consumer<PlayerProgression> effect) {
        this.id = id;
        this.name = name;
        this.requiredLevel = requiredLevel;
        this.effect = effect;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void apply(PlayerProgression progression) {
        effect.accept(progression);
    }
}
