package org.orioninc;

public enum Languages {
    JAVA("Java"),
    JAVASCRIPT("Javascript"),
    PYTHON("Python"),
    C_SHARP("C#"),
    PHP("PHP"),
    HTML("HTML"),
    C_PLUS_PLUS("C++"),
    CSS("CSS"),
    SQL("SQL"),
    RUBY("Ruby");

    private final String name;

    Languages(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Languages findByLanguage(String name) {
        Languages result = null;
        for (Languages languages : values()) {
            if (languages.name.equalsIgnoreCase(name)) {
                result = languages;
                break;
            }
        }
        return result;
    }


    @Override
    public String toString() {
        return name;
    }
}
