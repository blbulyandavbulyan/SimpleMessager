package org.blbulyandavbulyan.smgeneral.message.servercommand.exceptions;

public class ArgumentTypeIsNotEqualToRequired extends CommandException {
    private final Class<?> requiredArgumentType;
    private final Class<?> providedArgumentType;

    public ArgumentTypeIsNotEqualToRequired(Class<?> requiredArgumentType, Class<?> providedArgumentType) {

        this.requiredArgumentType = requiredArgumentType;
        this.providedArgumentType = providedArgumentType;
    }

    public Class<?> getRequiredArgumentType() {
        return requiredArgumentType;
    }

    public Class<?> getProvidedArgumentType() {
        return providedArgumentType;
    }
}
