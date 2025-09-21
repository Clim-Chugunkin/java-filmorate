package ru.yandex.practicum.filmorate.model.util;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;


public class Validator {

    private final Map<Checkable, String> rules = new HashMap<>();

    public Validator addRule(Checkable rule, String description) {
        rules.put(rule, description);
        return this;
    }

    public void check() throws ValidationException {
        for (Checkable rule : rules.keySet())
            if (rule.check()) throw new ValidationException(rules.get(rule));
    }


}

